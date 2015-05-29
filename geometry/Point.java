package ch.epfl.imhof.geometry;

import java.util.function.*;

/**
 * Un point définit sur un repère cartésien (hauteur et longueur). Sert pour les
 * cartes définies sur un plan.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class Point {

    private final double x;
    private final double y;

    /**
     * Construit un point représenté dans le plan par des coordonnées
     * cartésiennes.
     * 
     * @param x
     *            coordonnée du point sur l'axe des x
     * @param y
     *            coordonnée du point sur l'axe des y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return la coordonnée du point sur l'axe x
     */
    public double x() {
        return x;
    }

    /**
     * @return la coordonnée du point sur l'axe y
     */
    public double y() {
        return y;
    }

    /**
     * Méthode qui, étant donnés deux paires de points (donc un total de quatre
     * points) retourne le changement de repère correspondant
     * 
     * @param point1
     *            le premier point donné
     * @param point2
     *            le deuxième point donné, qui correspond au premier point dans
     *            le nouveau repère
     * @param point3
     *            le troisième point donné
     * @param point4
     *            le quatrième point donné, qui correspond au troisième point
     *            dans le nouveau repère
     * @return une fonction d'un point vers un autre, qui correspond au
     *         changement de repère
     * @throws IllegalArgumentException
     *             si les deux points sont situés sur une même ligne horizontale
     *             ou verticale, car il n'est alors pas possible de déterminer
     *             le changement de repère
     */
    public static Function<Point, Point> alignedCoordinateChange(Point point1,
            Point point2, Point point3, Point point4)
            throws IllegalArgumentException {
        if (point1.x() == point3.x() || point1.y() == point3.y()) {
            throw new IllegalArgumentException(
                    "both points are at the same line.");
        }

        double x1;
        double y1;
        double comp1 = point1.x();
        double comp2 = 1;
        double comp3 = point2.x();
        double comp4 = point3.x();
        double comp5 = 1;
        double comp6 = point4.x();

        if (comp1 == 0) {
            double temp = comp1;
            comp1 = comp4;
            comp4 = temp;

            temp = comp2;
            comp2 = comp5;
            comp5 = temp;

            temp = comp3;
            comp3 = comp6;
            comp6 = temp;
        }

        comp2 = comp2 / comp1;
        comp3 = comp3 / comp1;
        comp1 = 1;
        comp5 = comp5 - comp2 * comp4;
        comp6 = comp6 - comp3 * comp4;
        comp4 = 0;
        comp6 = comp6 / comp5;
        comp5 = 1;
        comp3 = comp3 - comp6 * comp2;
        comp2 = 0;
        x1 = comp3;
        y1 = comp6;

        double x2;
        double y2;
        comp1 = point1.y();
        comp2 = 1;
        comp3 = point2.y();
        comp4 = point3.y();
        comp5 = 1;
        comp6 = point4.y();

        if (comp1 == 0) {
            double temp = comp1;
            comp1 = comp4;
            comp4 = temp;

            temp = comp2;
            comp2 = comp5;
            comp5 = temp;

            temp = comp3;
            comp3 = comp6;
            comp6 = temp;
        }

        comp2 = comp2 / comp1;
        comp3 = comp3 / comp1;
        comp1 = 1;
        comp5 = comp5 - comp2 * comp4;
        comp6 = comp6 - comp3 * comp4;
        comp4 = 0;
        comp6 = comp6 / comp5;
        comp5 = 1;
        comp3 = comp3 - comp6 * comp2;
        comp2 = 0;
        x2 = comp3;
        y2 = comp6;

        return x -> new Point(x1 * x.x() + y1, x2 * x.y() + y2);
    }

}
