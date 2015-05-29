package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Hérite de la classe PolyLine et représente une polyligne ouverte.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class OpenPolyLine extends PolyLine {

    /**
     * Constuit une polyligne ouverte de sommets
     * 
     * @param points
     *            ensemble de points qui vont former la polyligne ouverte
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * méthode permettant d'informer de la nature d'une polyligne (ouverte ou
     * fermée)
     * 
     * @return un boolean de type false
     */
    @Override
    public boolean isClosed() {
        return false;
    }

}
