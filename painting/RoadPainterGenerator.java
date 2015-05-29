package ch.epfl.imhof.painting;

import ch.epfl.imhof.painting.LineStyle.Join;
import ch.epfl.imhof.painting.LineStyle.Termination;
import ch.epfl.imhof.Attributed;

import java.util.function.Predicate;

/**
 * Un générateur de peintre de réseau routier.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public class RoadPainterGenerator {

    private RoadPainterGenerator() {

    }

    /**
     * Méthode qui retourne le peintre pour le réseau routier correspondant aux
     * spécifications données
     * 
     * @param roadSpec
     *            un nombre variable de spécifications de routes en arguments
     * @return le peintre pour le réseau routier correspondant aux
     *         spécifications données
     */
    public static Painter painterForRoads(RoadSpec... roadSpec) {

        // initialisation des peintres qui serontt modifiés dans la boucle.
        Painter innerBridgePainter = roadSpec[0].innerBridge;
        Painter outerBridgePainter = roadSpec[0].outerBridge;
        Painter innerRoadPainter = roadSpec[0].innerRoad;
        Painter outerRoadPainter = roadSpec[0].outerRoad;
        Painter tunnelPainter = roadSpec[0].tunnel;
        for (int i = 1; i < roadSpec.length; ++i) {
            innerBridgePainter = innerBridgePainter
                    .above(roadSpec[i].innerBridge);
            outerBridgePainter = outerBridgePainter
                    .above(roadSpec[i].outerBridge);
            innerRoadPainter = innerRoadPainter.above(roadSpec[i].innerRoad);
            outerRoadPainter = outerRoadPainter.above(roadSpec[i].outerRoad); //PROBLEME AVEC OUTERROADPAINTER POUR BUGNON
            tunnelPainter = tunnelPainter.above(roadSpec[i].tunnel);
        }

        return innerBridgePainter.above(outerBridgePainter.above(
                innerRoadPainter).above(outerRoadPainter.above(tunnelPainter)));
    }

    /**
     * Classe qui représente une spécification de route qui décrit le dessin
     * d'un type de route donné
     *
     */
    public static class RoadSpec {

        private final Predicate<Attributed<?>> IS_BRIDGE = Filters
                .tagged("bridge");
        private final Predicate<Attributed<?>> IS_TUNNEL = Filters
                .tagged("tunnel");

        private final Painter innerBridge;
        private final Painter outerBridge;
        private final Painter innerRoad;
        private final Painter outerRoad;
        private final Painter tunnel;

        /**
         * Constructeur de RoadSpec
         * 
         * @param filter
         *            le filtre (de type Predicate<Attributed<?>>) permettant de
         *            sélectionner ce type de route
         * @param wi
         *            la largeur du trait de l'intérieur
         * @param ci
         *            la couleur du trait de l'intérieur
         * @param wc
         *            la largeur de la bordure du trait
         * @param cc
         *            la couleur de la bordure du trait
         */
        public RoadSpec(Predicate<Attributed<?>> filter, float wi, Color ci,
                float wc, Color cc) {
            innerBridge = Painter
                    .line(wi, ci, Termination.CAP_ROUND, Join.JOIN_ROUND,
                            new float[0]).when(IS_BRIDGE).when(filter);
            outerBridge = Painter
                    .line((wi + 2.0f * wc), cc, Termination.CAP_BUTT,
                            Join.JOIN_ROUND, new float[0]).when(IS_BRIDGE)
                    .when(filter);
            innerRoad = Painter
                    .line(wi, ci, Termination.CAP_ROUND, Join.JOIN_ROUND,
                            new float[0]).when(IS_BRIDGE.negate())
                    .when(IS_TUNNEL.negate()).when(filter);
            outerRoad = Painter
                    .line((wi + 2.0f * wc), cc, Termination.CAP_ROUND,
                            Join.JOIN_ROUND, new float[0])
                    .when(IS_BRIDGE.negate()).when(IS_TUNNEL.negate())
                    .when(filter);
            float[] tunnelAlternation = { (2.0f * wi), (2.0f * wi) };
            tunnel = Painter
                    .line(wi / 2.0f, cc, Termination.CAP_BUTT, Join.JOIN_ROUND,
                            tunnelAlternation).when(IS_TUNNEL).when(filter);
        }
    }
}
