package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * Classe qui représente une carte projetée, composée d'entités géométriques
 * attribuées.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class Map {

    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;

    /**
     * Constructeur de la classe Map, permet de construire une Carte constitué
     * de Polylignes et Polygones attribués.
     * 
     * @param polyLines
     *            liste de Polylignes avec leurs attributs
     * @param polygons
     *            liste de Polygones avec leurs attributs
     */
    public Map(List<Attributed<PolyLine>> polyLines,
            List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections.unmodifiableList(new ArrayList<>(polyLines));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons));
    }

    /**
     * Builder imbriqué statiquement servant à construire par étape une Map.
     */
    public final static class Builder {

        private List<Attributed<PolyLine>> bPolyLines = new ArrayList<>();
        private List<Attributed<Polygon>> bPolygons = new ArrayList<>();

        /**
         * Ajoute une Polyligne attribué à la liste des Polylignes
         * 
         * @param newPolyLine
         *            une instance d'Attributed de Polyligne.
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            bPolyLines.add(newPolyLine);
        }

        /**
         * Ajoute un Polygone attribué à la liste des Polygones
         * 
         * @param newPolygon
         *            une instance d'Attributed de Polyligne.
         */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            bPolygons.add(newPolygon);
        }

        /**
         * Permet d'instancier une Map immuable à partir de l'instance en cours
         * du Builder.
         * 
         * @return une Map immuable constitué de la liste des polygones et
         *         polylignes du Builder.
         */
        public Map build() {
            return new Map(bPolyLines, bPolygons);
        }
    }

    /**
     * @return la liste des Polylignes constituant la Map.
     */
    public List<Attributed<PolyLine>> polyLines() {
        return polyLines;
    }

    /**
     * @return la liste des Polygones constituant la Map.
     */
    public List<Attributed<Polygon>> polygons() {
        return polygons;
    }
}
