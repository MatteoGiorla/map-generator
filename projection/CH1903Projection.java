package ch.epfl.imhof.projection;

import java.lang.Math;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Projection
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class CH1903Projection implements Projection {

    /*
     * (non-Javadoc)
     * 
     * @see ch.epfl.imhof.projection.Projection#project(ch.epfl.imhof.PointGeo)
     */
    @Override
    public Point project(PointGeo point) {
        double lambda = 0.0001 * (Math.toDegrees(point.longitude()) * 3600 - 26782.5);
        double phi = 0.0001 * (Math.toDegrees(point.latitude()) * 3600 - 169028.66);
        double phiSquared = phi*phi;
        double phiCubed = phiSquared * phi;
        double lambdaSquared = lambda*lambda;
        double lambdaCubed = lambdaSquared*lambda;
        double x = 600072.37 + 211455.93 * lambda - 10938.51 * lambda * phi
                - 0.36 * lambda * phiSquared - 44.54
                * lambdaCubed;
        double y = 200147.07 + 308807.95 * phi + 3745.25
                * lambdaSquared + 76.63 * phiSquared - 194.56
                * lambdaSquared * phi + 119.79 * phiCubed;
        return new Point(x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.epfl.imhof.projection.Projection#inverse(ch.epfl.imhof.geometry.Point)
     */
    @Override
    public PointGeo inverse(Point point) {
        final double INVERSE_FACTOR = 100d/36d;
        double x = (point.x() - 600000) / 1000000;
        double xSquared = x*x;
        double y = (point.y() - 200000) / 1000000;
        double ySquared = y*y;
        double lambda = 2.6779094 + 4.728982 * x + 0.791484 * x * y
                + 0.1306 * x * ySquared - 0.0436 * (x*x*x);
        double phi = 16.9023892 + 3.238272 * y - 0.270978 * xSquared
                - 0.002528 *ySquared - 0.0447 * xSquared * y
                - 0.0140 * (y*y*y);
        double longitude = lambda * INVERSE_FACTOR;
        double latitude = phi * INVERSE_FACTOR;
        return new PointGeo(Math.toRadians(longitude), Math.toRadians(latitude));
    }
}