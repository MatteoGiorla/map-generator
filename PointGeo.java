package ch.epfl.imhof;

import java.lang.Math;

/**
 * Un point à la surface de la Terre, en coordonnées sphériques.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class PointGeo {

    private final double longitude;
    private final double latitude;

    /**
     * Construit un point avec la latitude et la longitude données.
     * 
     * @param longitude
     *            la longitude du point, en radians
     * @param latitude
     *            la latitude du point, en radians
     * @throws IllegalArgumentException
     *             si la latitude est invalide, c-à-d. si le paramètre entré est
     *             entre -pi/2 ou pi/2
     * @throws IllegalArgumentException
     *             si la longitude est invalide, c-à-d. si le paramètre entré
     *             est entre -pi ou pi
     */

    public PointGeo(double longitude, double latitude)
            throws IllegalArgumentException {
        if (longitude < -Math.PI || longitude > Math.PI) {
            throw new IllegalArgumentException("Longitude non conforme, elle devrait être comprise entre -pi et pi ");
        } else {
            this.longitude = longitude;
        }

        if (latitude < -Math.PI / 2 || latitude > Math.PI / 2) {
            throw new IllegalArgumentException("Latitude non conforme, elle devrait être comprise entre -pi/2 et pi/2 ");
        } else {
            this.latitude = latitude;
        }
    }

    /**
     * @return la longitude du point
     */
    public double longitude() {
        return longitude;
    }

    /**
     * @return la latitude du point
     */
    public double latitude() {
        return latitude;
    }
}