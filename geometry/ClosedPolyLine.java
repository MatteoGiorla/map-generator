package ch.epfl.imhof.geometry;

import java.lang.Math;
import java.util.List;

/**
 * Hérite de la classe PolyLine et représente une polyligne fermé.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class ClosedPolyLine extends PolyLine {

    /**
     * Constructeur d'une polyligne fermé à partir d'un ensemble de points
     * 
     * @param points
     *            ensemble de points qui vont former le polyligne ouvert
     */
    public ClosedPolyLine(List<Point> points) throws IllegalArgumentException {
        super(points);
    }

    /**
     * méthode permettant d'informer de la nature d'une polyligne (ouverte ou
     * fermée)
     * 
     * @return un boolean de type true
     */
    @Override
    public boolean isClosed() {
        return true;
    }

    /**
     * @return l'aire, toujours positive, de la polyligne
     */
    public double area() {
        double tempArea = 0;
        for (int i = 0; i < points().size(); ++i) {
            tempArea = tempArea
                    + points().get(indiceG(points(), i)).x()
                    * (points().get(indiceG(points(), i + 1)).y() - points()
                            .get(indiceG(points(), i - 1)).y());
        }
        // être sur que le calcul de l'air doit prendre en compte la coordonnée
        // Y du premier point en tant que le "yi+1" de la formule
        return 0.5 * (Math.abs(tempArea));
    }

    /**
     * @param p
     *            le point donné
     * @return vrai si et seulement si le point donné se trouve à l'intérieur de
     *         la polyligne
     */
    public boolean containsPoint(Point p) {
        int indice = 0;
        for (int i = 0; i < points().size(); ++i) {

            Point p1 = new Point(points().get(indiceG(points(), i)).x(),
                    points().get(indiceG(points(), i)).y());
            Point p2 = new Point(points().get(indiceG(points(), i + 1)).x(),
                    points().get(indiceG(points(), i + 1)).y());
            if (p1.y() <= p.y()) {
                if (p2.y() > p.y() && pointIsLeft(p, p1, p2)) {
                    indice = indice + 1;
                }
            } else {
                if (p2.y() <= p.y() && pointIsLeft(p, p2, p1)) {
                    indice = indice - 1;
                }
            }
        }
        return (indice != 0);
    }

    /**
     * Détermine si un point se trouve à gauche d'une ligne définie par 2 points
     * 
     * @param p
     *            le point à tester
     * @param p1
     *            le premier point qui détermine la droite
     * @param p2
     *            le deuxième point qui détermine la droite
     * @return vrai si et seulement si le point se trouve à gauche de la droite
     *         donnée
     */
    private boolean pointIsLeft(Point p, Point p1, Point p2) {
        return (triangleArea(p, p1, p2) > 0); 
    }

    /**
     * Méthode qui calcule l'aire d'un triangle
     * 
     * @param sommet0
     *            premier sommet du triangle
     * @param sommet1
     *            deuxième sommet du triangle
     * @param sommet2
     *            troisième sommet du triangle
     * @return l'aire signée du triangle à partir des trois sommets
     */
    private double triangleArea(Point sommet0, Point sommet1, Point sommet2) {
        double part1 = (sommet1.x() - sommet0.x())
                * (sommet2.y() - sommet0.y());
        double part2 = (sommet2.x() - sommet0.x())
                * (sommet1.y() - sommet0.y());
        return 0.5 * (part1 - part2);
    }

    /**
     * Méthode qui permet d'obtenir l'indice généralisé
     * 
     * @param points
     *            la liste de points qui permet d'en déduire sa taille
     * @param i
     *            l'indice en question, qu'il faut "généraliser"
     * @return l'indice généralisé
     */
    private int indiceG(List<Point> points, int i) {
        return Math.floorMod(i, points.size());
    }
}