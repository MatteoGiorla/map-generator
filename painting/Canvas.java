package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.*;

/**
 * Interface qui représente une toile.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public interface Canvas {

    /**
     * Méthode permettant de dessiner sur la toile une polyligne donnée (de type
     * PolyLine) avec un style de ligne donné
     * 
     * @param polyline
     *            la polyligne donnée
     * @param lineStyle
     *            le style de ligne donné, de type LineStyle
     */
    void drawPolyLine(PolyLine polyline, LineStyle lineStyle);

    /**
     * Méthode permettant de dessiner sur la toile un polygone donné (de type
     * Polygon) avec une couleur donnée
     * 
     * @param polygon
     *            le polygone donné
     * @param color
     *            la couleur donnée, de type ch.epfl.imhof.painting.Color.
     */
    void drawPolygon(Polygon polygon, Color color);
}
