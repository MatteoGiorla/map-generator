package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.ListIterator;

import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;
import ch.epfl.imhof.geometry.*;
import ch.epfl.imhof.*;
import ch.epfl.imhof.projection.*;

/**
 * Classe qui représente un convertisseur de données OSM en carte.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public final class OSMToGeoTransformer {

    private final Projection projection;

    /**
     * Construit un convertisseur OSM en géométrie qui utilise la projection
     * donnée.
     * 
     * @param projection
     *            la projection donnée
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    /**
     * Convertit une « carte » OSM en une carte géométrique projetée
     * 
     * @param map
     *            la carte OSM donnée
     * @return une carte géométrique
     */
    public Map transform(OSMMap map) {
        List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        List<Attributed<Polygon>> polygons = new ArrayList<>();

        List<String> surfaceAttribute = Arrays.asList("aeroway", "amenity",
                "building", "harbour", "historic", "landuse", "leisure",
                "man_made", "military", "natural", "office", "place", "power",
                "public_transport", "shop", "sport", "tourism", "water",
                "waterway", "wetland");

        List<String> polylineAttribute = Arrays.asList("bridge", "highway",
                "layer", "man_made", "railway", "tunnel", "waterway");
        Set<String> keysToKeepForPolyLine = new HashSet<String>(
                polylineAttribute);

        List<String> polygonAttribute = Arrays.asList("building", "landuse",
                "layer", "leisure", "natural", "waterway");

        Set<String> keysToKeepForPolygon = new HashSet<String>(polygonAttribute);

        // WAYS :
        for (OSMWay way : map.ways()) {
            if (way.isClosed()) {
                boolean polyGon = false;
                for (String attribute : surfaceAttribute) {
                    if (way.attributes().contains(attribute)
                            || way.attributes().get("area", "0").equals("1")
                            || way.attributes().get("area", "0").equals("yes")
                            || way.attributes().get("area", "0").equals("true")) {
                        polyGon = true;
                    }
                }
                if (polyGon) {
                    // PolyGon
                    List<Point> listeDePoints = new ArrayList<>();
                    for (OSMNode node : way.nonRepeatingNodes()) {
                        listeDePoints.add(projection.project(node.position()));
                    }
                    ClosedPolyLine tempClosedPolyLine = new ClosedPolyLine(
                            listeDePoints);
                    Polygon tempPolygon = new Polygon(tempClosedPolyLine);
                    Attributes newAttributesForPG = way.attributes()
                            .keepOnlyKeys(keysToKeepForPolygon);
                    if (!newAttributesForPG.isEmpty()) {
                        polygons.add(new Attributed<Polygon>(tempPolygon,
                                newAttributesForPG));
                    }
                } else {
                    // ClosedPolyLine
                    List<Point> listeDePoints = new ArrayList<>();
                    for (OSMNode node : way.nodes()) {
                        listeDePoints.add(projection.project(node.position()));
                    }
                    ClosedPolyLine tempClosedPolyLine = new ClosedPolyLine(
                            listeDePoints);
                    Attributes newAttributesForCPL = way.attributes()
                            .keepOnlyKeys(keysToKeepForPolyLine);
                    if (!newAttributesForCPL.isEmpty()) {
                        polyLines.add(new Attributed<PolyLine>(
                                tempClosedPolyLine, newAttributesForCPL));
                    }
                }
            } else if (!way.isClosed()) {
                // OpenPolyLine
                List<Point> listeDePoints = new ArrayList<>();
                for (OSMNode node : way.nodes()) {
                    listeDePoints.add(projection.project(node.position()));
                }
                OpenPolyLine tempOpenPolyLine = new OpenPolyLine(listeDePoints);
                Attributes newAttributesForOPL = way.attributes().keepOnlyKeys(
                        keysToKeepForPolyLine);
                if (!newAttributesForOPL.isEmpty()) {
                    polyLines.add(new Attributed<PolyLine>(tempOpenPolyLine,
                            newAttributesForOPL));
                }
            }
        }

        // RELATIONS :
        for (OSMRelation relation : map.relations()) {
            if (relation.attributes().get("type") != null) {
                if (relation.attributes().get("type").equals("multipolygon")) {
                    for (Attributed<Polygon> tempPolygon : assemblePolygon(
                            relation, relation.attributes())) {
                        polygons.add(tempPolygon);
                    }
                }
            }
        }
        return new Map(polyLines, polygons);
    }

    /**
     * Calcule et retourne l'ensemble des anneaux de la relation donnée ayant le
     * rôle spécifié. Cette méthode retourne une liste vide si le calcul des
     * anneaux échoue.
     * 
     * @param relation
     *            la relation donnée
     * @param role
     *            le role de la relation donnée
     * @return une liste contenant l'ensemble des anneaux de la relation donnée
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {
        List<ClosedPolyLine> tempRings = new ArrayList<>();
        Graph.Builder<OSMNode> b = new Graph.Builder<OSMNode>();
        for (OSMRelation.Member r : relation.members()) {
            if (r.role().equals(role) && r.type() == Member.Type.WAY)
            {
                OSMWay way = (OSMWay) r.member();
                OSMNode precedentNode = way.firstNode();
                b.addNode(precedentNode);
                int i = 0;
                for(OSMNode n : way.nodes())
                {
                    if(i!=0){
                        b.addNode(n);
                        b.addEdge(n, precedentNode);
                        precedentNode = n;
                    }
                    ++i;
                }
            }
        }
        
        Graph<OSMNode> graph = b.build();
        List<OSMNode> nodes = new ArrayList<>(graph.nodes());
       for(OSMNode n : nodes)
        {
            if(graph.neighborsOf(n).size() != 2)
            {
                return new ArrayList<ClosedPolyLine>();
            }
        }

        while(!nodes.isEmpty())
        {
            ClosedPolyLine.Builder cbp = new ClosedPolyLine.Builder();
                OSMNode oldNode = nodes.get(0);
                OSMNode firstNode = oldNode;
                List<OSMNode> tempNeighbor = new ArrayList<>();
                tempNeighbor.addAll(graph.neighborsOf(oldNode));
                nodes.remove(firstNode);
                cbp.addPoint(projection.project(oldNode.position()));
                OSMNode newNode;
                do {
                    newNode = tempNeighbor.get(0);
                    cbp.addPoint(projection.project(newNode.position()));
                    tempNeighbor = new ArrayList<>(graph.neighborsOf(newNode));
                    tempNeighbor.remove(oldNode);
                    nodes.remove(newNode);
                    oldNode = newNode;
                } while (!tempNeighbor.contains(firstNode));
            ClosedPolyLine polyLineToAdd = cbp.buildClosed();
            tempRings.add(polyLineToAdd);
        }
        return tempRings;
    }

    /**
     * calcule et retourne la liste des polygones attribués de la relation
     * donnée, en leur attachant les attributs donnés
     * 
     * @param relation
     *            la relation donnée
     * @param attributes
     *            les attributs donnés
     * @return une liste de polygones attribués de la relation donnée
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation,
            Attributes attributes) {
        // Tri des attributs
        List<String> polygonAttribute = Arrays.asList("building", "landuse",
                "layer", "leisure", "natural", "waterway");
        Set<String> keysToKeepForPolygon = new HashSet<String>(polygonAttribute);
        Attributes newAttributes = attributes
                .keepOnlyKeys(keysToKeepForPolygon);

        // Création des polygons
        List<Attributed<Polygon>> listOfPolygons = new ArrayList<>();
        List<ClosedPolyLine> listOfInnerClosedPolylines = ringsForRole(
                relation, "inner");
        List<ClosedPolyLine> listOfOuterClosedPolylines = ringsForRole(
                relation, "outer");

        List<ClosedPolyLine> sortedListOfInnerClosedPolylines = new ArrayList<>();
        List<ClosedPolyLine> sortedListOfOuterClosedPolylines = new ArrayList<>();

        for (ClosedPolyLine outerPL : listOfOuterClosedPolylines) {
            sortedListOfOuterClosedPolylines.add(outerPL);
        }
        for (ClosedPolyLine innerPL : listOfInnerClosedPolylines) {
            sortedListOfInnerClosedPolylines.add(innerPL);
        }

        Collections.sort(sortedListOfInnerClosedPolylines,
                new Comparator<ClosedPolyLine>() {
                    @Override
                    public int compare(ClosedPolyLine cp1, ClosedPolyLine cp2) {
                        return Double.compare(cp1.area(), cp2.area());
                    }
                });
        Collections.sort(sortedListOfOuterClosedPolylines,
                new Comparator<ClosedPolyLine>() {
                    @Override
                    public int compare(ClosedPolyLine cp1, ClosedPolyLine cp2) {
                        return Double.compare(cp1.area(), cp2.area());
                    }
                });

        List<ClosedPolyLine> addedHoles = new ArrayList<>();

        for (ClosedPolyLine outerPL : sortedListOfOuterClosedPolylines) {
            Polygon tempPolygon;
            List<ClosedPolyLine> listOfHoles = new ArrayList<>();
            for (ClosedPolyLine innerPL : sortedListOfInnerClosedPolylines) {
                if (innerPL.area() <= outerPL.area()
                        && !addedHoles.contains(innerPL)
                        && outerPL.containsPoint(innerPL.points().get(0))) {
                    listOfHoles.add(innerPL);
                    addedHoles.add(innerPL);
                }
            }
            if (listOfHoles.isEmpty()) {
                tempPolygon = new Polygon(outerPL);
            } else if (!listOfHoles.isEmpty()) {
                tempPolygon = new Polygon(outerPL, listOfHoles);
            } else {
                tempPolygon = null;
            }
            if (!newAttributes.isEmpty() && tempPolygon != null) {
                listOfPolygons.add(new Attributed<Polygon>(tempPolygon,
                        newAttributes));
            }
        }
        return listOfPolygons;
    }
}