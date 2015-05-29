package ch.epfl.imhof.painting;

import ch.epfl.imhof.*;
import ch.epfl.imhof.geometry.*;
import ch.epfl.imhof.painting.LineStyle.Join;
import ch.epfl.imhof.painting.LineStyle.Termination;

import java.util.function.*;

/**
 * Interface fonctionnelle représentant un peintre.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
@FunctionalInterface
public interface Painter {

    /**
     * Méthode abstraite dont le but est de dessiner la première sur la seconde.
     * 
     * @param map
     *            la carte à imprimer
     * @param canvas
     *            la toile sur laquelle dessiner la carte
     */
    void drawMap(Map map, Java2DCanvas canvas);

    /**
     * @param drawingColor
     *            la couleur de dessin
     * @return un peintre dessinant l'intérieur de tous les polygones de la
     *         carte qu'il reçoit avec la couleur donnée
     */
    public static Painter polygon(Color drawingColor) {
        return (m, c) -> {
            for (Attributed<Polygon> ap : m.polygons()) {
                c.drawPolygon(ap.value(), drawingColor);
            }
        };
    }

    /**
     * @param lineStyle
     *            le style de ligne
     * @return un peintre dessinant toutes les lignes de la carte qu'on lui
     *         fournit avec le style de ligne donné
     */
    public static Painter line(LineStyle lineStyle) {
        return (m, c) -> {
            for (Attributed<PolyLine> ap : m.polyLines()) {
                c.drawPolyLine(ap.value(), lineStyle);
            }
        };
    }

    /**
     * Méthode prend en arguments les cinq paramètres de dessin d'une ligne et
     * retourne un peintre dessinant toutes les lignes de la carte qu'on lui
     * fournit avec le style correspondant
     * 
     * @param width
     *            la largeur de la ligne
     * @param color
     *            la couleur de la ligne
     * @param termination
     *            la terminaison de la ligne
     * @param join
     *            la jointure de la ligne
     * @param alternation
     *            l'alternance des sections opaques et transparentes des
     *            traitillés
     * @return un peintre dessinant toutes les lignes de la carte qu'on lui
     *         fournit avec le style correspondant
     */
    public static Painter line(float width, Color color,
            Termination termination, Join join, float... alternation) {
        LineStyle lineToApply = new LineStyle(width, color, termination, join,
                alternation);
        return (m, c) -> {
            for (Attributed<PolyLine> ap : m.polyLines()) {
                c.drawPolyLine(ap.value(), lineToApply);
            }
        };
    }

    /**
     * Méthode qui ne prend en argument que la largeur du trait et la couleur et
     * utilise les valeurs par défaut pour les autres paramètres de style et
     * retourne un peintre dessinant toutes les lignes de la carte qu'on lui
     * fournit avec le style correspondant
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     * @return un peintre dessinant toutes les lignes de la carte qu'on lui
     *         fournit avec le style correspondant
     */
    public static Painter line(float width, Color color) {
        LineStyle lineToApply = new LineStyle(width, color);
        return (m, c) -> {
            for (Attributed<PolyLine> ap : m.polyLines()) {
                c.drawPolyLine(ap.value(), lineToApply);
            }
        };
    }

    /**
     * Méthode qui retourne un peintre qui dessine les pourtours de l'enveloppe
     * et des trous de tous les polygones de la carte qu'on lui fournit
     * 
     * @param lineStyle
     *            le style de ligne à utiliser
     * @return un peintre qui dessine les pourtours de l'enveloppe et des trous
     *         de tous les polygones de la carte qu'on lui fournit
     */
    public static Painter outline(LineStyle lineStyle) {
        return (m, c) -> {
            for (Attributed<Polygon> ap : m.polygons()) {
                c.drawPolyLine(ap.value().shell(), lineStyle);
                for (PolyLine cpl : ap.value().holes()) {
                    c.drawPolyLine(cpl, lineStyle);
                }
            }
        };
    }

    /**
     * Méthode qui ne prend en argument que la largeur du trait et la couleur et
     * utilise les valeurs par défaut pour les autres paramètres de style et
     * retourne un peintre dessinant les pourtours de l'enveloppe et des trous
     * de tous les polygones de la carte qu'on lui fournit
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     * @param termination
     *            la terminaison du trait
     * @param join
     *            la jointure du trait
     * @param alternation
     *            l'alternance entre les sections opaques et transparentes des
     *            traitillés
     * @return un peintre dessinant les pourtours de l'enveloppe et des trous de
     *         tous les polygones de la carte qu'on lui fournit
     */
    public static Painter outline(float width, Color color,
            Termination termination, Join join, float[] alternation) {
        LineStyle lineToApply = new LineStyle(width, color, termination, join,
                alternation);
        return (m, c) -> {
            for (Attributed<Polygon> ap : m.polygons()) {
                c.drawPolyLine(ap.value().shell(), lineToApply);
                for (PolyLine cpl : ap.value().holes()) {
                    c.drawPolyLine(cpl, lineToApply);
                }
            }
        };
    }

    /**
     * Méthode qui ne prend en argument que la largeur du trait et la couleur et
     * utilise les valeurs par défaut pour les autres paramètres de style et
     * retourne un peintre qui dessine les pourtours de l'enveloppe et des trous
     * de tous les polygones de la carte qu'on lui fournit
     * 
     * @param width
     *            la largeur du trait
     * @param color
     *            la couleur du trait
     * @return un peintre qui dessine les pourtours de l'enveloppe et des trous
     *         de tous les polygones de la carte qu'on lui fournit
     */
    public static Painter outline(float width, Color color) {
        LineStyle lineToApply = new LineStyle(width, color);
        return (m, c) -> {
            for (Attributed<Polygon> ap : m.polygons()) {
               c.drawPolyLine(ap.value().shell(), lineToApply);
                for (PolyLine cpl : ap.value().holes()) {
                    c.drawPolyLine(cpl, lineToApply);
                }
            }
        };
    }

    /**
     * Méthode retournant un peintre se comportant comme celui auquel on
     * l'applique, si ce n'est qu'il ne considère que les éléments de la carte
     * satisfaisant le prédicat
     * 
     * @param predicate
     *            le predicat donné
     * @return un peintre se comportant comme celui auquel on l'applique, si ce
     *         n'est qu'il ne considère que les éléments de la carte
     *         satisfaisant le prédicat
     */
    public default Painter when(Predicate<Attributed<?>> predicate) {
        return (m, c) -> {
            Map.Builder tempMap = new Map.Builder();

            for (Attributed<Polygon> polygon : m.polygons()) {
                if (predicate.test(polygon)) {
                    tempMap.addPolygon(polygon);
                }
            }

            for (Attributed<PolyLine> polyline : m.polyLines()) {
                if (predicate.test(polyline)) {
                    tempMap.addPolyLine(polyline);
                }
            }
            m = tempMap.build();
            this.drawMap(m, c);
        };
    }

    /**
     * Méthode retournant un peintre dessinant d'abord la carte produite par ce
     * second peintre puis, par dessus, la carte produite par le premier peintre
     * 
     * @param otherPainter
     *            le peintre pris en argument
     * @return un peintre dessinant d'abord la carte produite par ce second
     *         peintre puis, par dessus, la carte produite par le premier
     *         peintre
     */
    public default Painter above(Painter otherPainter) {
        return (m, c) -> {
            otherPainter.drawMap(m, c);
            this.drawMap(m, c);
        };
    }

    /**
     * Méthode retournant un peintre utilisant l'attribut layer attaché aux
     * entités de la carte pour la dessiner par couches, c-à-d en dessinant
     * d'abord tous les entités de la couche –5, puis celle de la couche –4, et
     * ainsi de suite jusqu'à la couche +5
     * 
     * @return un peintre utilisant l'attribut layer attaché aux entités de la
     *         carte pour la dessiner par couches, c-à-d en dessinant d'abord
     *         tous les entités de la couche –5, puis celle de la couche –4, et
     *         ainsi de suite jusqu'à la couche +5
     */
    public default Painter layered() {
        return (m, c) -> {
            for (int i = -4; i < 6; ++i) {
                this.when(Filters.onLayer(i)).above(
                        when(Filters.onLayer(i - 1)));
            }
            this.drawMap(m, c);
        };
    }
}
