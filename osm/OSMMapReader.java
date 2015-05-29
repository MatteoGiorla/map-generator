package ch.epfl.imhof.osm;

import java.io.*;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.util.zip.GZIPInputStream;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

import java.util.HashMap;

/**
 * Classe qui permet de construire une carte OpenStreetMap à partir de données
 * stockées dans un fichier au format XML
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class OSMMapReader {

    /**
     * Constructeur par défaut privé et vide car la classe est non instanciable
     */
    private OSMMapReader() {
    }

    /**
     * lit la carte OSM contenue dans le fichier de nom donné, en le
     * décompressant avec gzip si et seulement si le second argument est vrai.
     * Lève l'exception SAXException en cas d'erreur dans le format du fichier
     * XML contenant la carte, ou l'exception IOException en cas d'autre erreur
     * d'entrée/sortie, p.ex. si le fichier n'existe pas
     * 
     * @param fileName
     *            la carte OSM contenue dans le fichier de nom donné
     * @param unGZip
     *            l'argument qui détermine si le fichier doit être décompressé
     *            avec GZIP
     * @return un objet de type OSMMap construit à partir du fichier XML donné
     * @throws SAXException
     *             en cas d'erreur dans le format du fichier XML contenant la
     *             carte
     * @throws IOException
     *             en cas d'autre erreur d'entrée/sortie, p.ex. si le fichier
     *             n'existe pas
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws SAXException, IOException {
        OSMMap.Builder map = new OSMMap.Builder();
        InputStream file = null;
        if (unGZip) {
            file = new GZIPInputStream(new FileInputStream(fileName));
        } else {
            file = new FileInputStream(fileName);
        }

        XMLReader r = XMLReaderFactory.createXMLReader();
        r.setContentHandler(new DefaultHandler() {
            private OSMWay.Builder wayForMap;
            private OSMRelation.Builder relationForMap;
            private HashMap<String, String> tagForEntity = new HashMap<>();

            @Override
            public void startElement(String uri, String lName, String qName,
                    Attributes atts) throws SAXException {
                switch (qName) {
                case "node":
                    try {
                        OSMNode.Builder noeud = new OSMNode.Builder(Long
                                .parseLong(atts.getValue("id")), new PointGeo(
                                Math.toRadians(Double.parseDouble(atts
                                        .getValue("lon"))), Math
                                        .toRadians(Double.parseDouble(atts
                                                .getValue("lat")))));

                        OSMNode newNode = noeud.build();
                        map.addNode(newNode);
                        tagForEntity = new HashMap<>();
                    } catch (IllegalStateException | IllegalArgumentException e) {
                    }
                    break;

                case "way":
                    wayForMap = new OSMWay.Builder(Long.parseLong(atts
                            .getValue("id")));
                    tagForEntity = new HashMap<>();
                    break;

                case "nd":
                    if (map.nodeForId(Long.parseLong(atts.getValue("ref"))) != null) {
                        wayForMap.addNode(map.nodeForId(Long.parseLong(atts
                                .getValue("ref"))));
                    } else {
                        wayForMap.setIncomplete();
                    }
                    break;

                case "relation":
                    relationForMap = new OSMRelation.Builder(Long
                            .parseLong(atts.getValue("id")));
                    tagForEntity = new HashMap<>();
                    break;

                case "member":
                    if (atts.getValue("type").equals("node")) {
                        if (map.nodeForId(Long.parseLong(atts.getValue("ref"))) != null) {
                            relationForMap.addMember(Type.NODE, atts
                                    .getValue("role"), map.nodeForId(Long
                                    .parseLong(atts.getValue("ref"))));
                        } else {
                            relationForMap.setIncomplete();
                        }
                    } else if (atts.getValue("type").equals("way")) {
                        if (map.wayForId(Long.parseLong(atts.getValue("ref"))) != null) {
                            relationForMap.addMember(Type.WAY, atts
                                    .getValue("role"), map.wayForId(Long
                                    .parseLong(atts.getValue("ref"))));
                        } else {
                            relationForMap.setIncomplete();
                        }
                    } else if (atts.getValue("type").equals("relation")) {
                        if (map.relationForId(Long.parseLong(atts
                                .getValue("ref"))) != null) {
                            relationForMap.addMember(Type.RELATION, atts
                                    .getValue("role"), map.relationForId(Long
                                    .parseLong(atts.getValue("ref"))));
                        } else {
                            relationForMap.setIncomplete();
                        }
                    } else {

                    }
                    break;

                case "tag":
                    tagForEntity.put(atts.getValue("k"), atts.getValue("v"));
                    break;

                default:
                    // ignorer les autres balises
                    break;
                }

            }

            @Override
            public void endElement(String uri, String lName, String qName) {

                switch (lName) {
                case "way":
                    if (!wayForMap.isIncomplete()) {
                        for (HashMap.Entry<String, String> entry : tagForEntity
                                .entrySet()) {
                            wayForMap.setAttribute(entry.getKey(),
                                    entry.getValue());
                        }
                        OSMWay newWay = null;
                        try {
                            newWay = wayForMap.build();
                        } catch (IllegalStateException e) {

                        }
                        if (newWay != null) {
                            map.addWay(newWay);
                        }
                    }
                    break;

                case "relation":
                    if (!relationForMap.isIncomplete()) {
                        try {
                            for (HashMap.Entry<String, String> entry : tagForEntity
                                    .entrySet()) {
                                relationForMap.setAttribute(entry.getKey(),
                                        entry.getValue());
                            }
                            try {
                                OSMRelation newRelation = relationForMap
                                        .build();
                                map.addRelation(newRelation);
                            } catch (IllegalStateException e) {
                            }
                        } catch (NullPointerException e) {
                        }
                    }
                    break;

                default:
                    // ignorer les autres balises
                    break;
                }
            }
        });
        try (InputStream i = file) {
            r.parse(new InputSource(i));
        }
        OSMMap newMap = map.build();
        return newMap;
    }
}
