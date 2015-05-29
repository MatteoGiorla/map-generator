package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe mère des types ouverts et fermés de Polylignes, sert à représenter
 * abstraitement un ensemble de points
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public abstract class PolyLine {

    private final List<Point> points;

    /**
     * Construit une polyligne avec les sommets donnés
     * 
     * @param points
     *            collection d'objets de types Point.
     * @throws IllegalArgumentException
     *             lorsque une liste de points vide est entrée en paramètre.
     * 
     */
    public PolyLine(List<Point> points) throws IllegalArgumentException {
        if (points.isEmpty()) {
            throw new IllegalArgumentException("La liste de point est vide");
        } else {
            this.points = Collections.unmodifiableList(new ArrayList<>(points)); 
        }
    }

    /**
     * Méthode publique abstraite pour savoir si une polyligne est fermée ou non
     * 
     * @return vrai si et seulement si la polyligne est fermée
     */
    public abstract boolean isClosed();

    /**
     * @return le premier sommet de la polyligne
     */
    public Point firstPoint() {
        return new Point(points.get(0).x(), points.get(0).y());
    }

    /**
     * @return la liste des sommets de la polyligne
     */
    public List<Point> points() {
        return points;
    }

    /**
     * Builder imbriqué statiquement servant à constuire l'ensemble de points
     * pour Polyline
     *
     */
    public final static class Builder {

        private List<Point> points = new ArrayList<>();

        /**
         * Ajoute un point à la fin de la liste des sommets de la polyligne en
         * cours de construction
         * 
         * @param newPoint
         *            Point qui va être ajouté (en copie profonde) à la List du
         *            Builder
         */
        public void addPoint(Point newPoint) {
            points.add(newPoint);
        }

        /**
         * Construit et retourne une polyligne ouverte avec les points ajoutés
         * jusqu'à présent
         * 
         * @return une polyligne ouverte avec les points ajoutés jusqu'à présent
         */
        public OpenPolyLine buildOpen() {
            try {
                return new OpenPolyLine(this.points);
            } catch (IllegalArgumentException e) {
                System.out.println(e);
                return null;
            }
        }

        /**
         * Construit et retourne une polyligne fermée avec les points ajoutés
         * jusqu'à présent
         * 
         * @return une polyligne fermée avec les points ajoutés jusqu'à présent
         */
        public ClosedPolyLine buildClosed() {
            try {
                return new ClosedPolyLine(this.points);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}