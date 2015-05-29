package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Représente une projection
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public interface Projection {

    /**
     * @param point
     *            en coordonnées cartésiennes
     * @return le point reçu en argument projeté sur le plan
     */
    Point project(PointGeo point);

    /**
     * @param point
     *            en coordonnées sphériques
     * @return le point reçu en argument "dé-projeté" du plan
     */
    PointGeo inverse(Point point);
}
