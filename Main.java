package ch.epfl.imhof;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.painting.SwissPainter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * Main du programme.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public class Main {

    /**
     * Méthode principale du programme
     * 
     * @param args
     *            le tableau de String
     */
    public static void main(String[] args) {
        final double MM_RADIUS = 1.7;

        String osmFile = args[0];
        String hgtStringFile = args[1];
        double bottomLeftLongitude = Math
                .toRadians(Double.parseDouble(args[2]));
        double bottomLeftLatitude = Math.toRadians(Double.parseDouble(args[3]));
        double topRightLongitude = Math.toRadians(Double.parseDouble(args[4]));
        double topRightLatitude = Math.toRadians(Double.parseDouble(args[5]));
        int dpiResolution = Integer.parseInt(args[6]);
        String pngFile = args[7];

        Painter painter = SwissPainter.painter();

        Projection p = new CH1903Projection();
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(p);
        OSMMap osmMap = null;
        try {
            osmMap = OSMMapReader.readOSMFile(osmFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        ch.epfl.imhof.Map map = transformer.transform(osmMap);

        PointGeo pBL = new PointGeo(bottomLeftLongitude, bottomLeftLatitude);
        PointGeo pTR = new PointGeo(topRightLongitude, topRightLatitude);

        Point bL = p.project(pBL);
        Point tR = p.project(pTR);

        double pixelMeterResolution = dpiResolution * 39.3700787;
        int imageHeight = (int) Math.round(pixelMeterResolution
                * (Math.toRadians(46.5459) - Math.toRadians(46.5032))
                * Earth.RADIUS / 25000);
        int imageWidth = (int) Math.round(imageHeight
                * ((tR.x() - bL.x()) / (tR.y() - bL.y())));

        Java2DCanvas canvas = new Java2DCanvas(bL, tR, imageWidth, imageHeight,
                dpiResolution, Color.CONST_WHITE);

        painter.drawMap(map, canvas);

        BufferedImage mapImage = canvas.image();

        float blurRadius = (float) ((MM_RADIUS * dpiResolution) / 25.4);
        File hgtFile = new File(hgtStringFile);
        HGTDigitalElevationModel hgt = new HGTDigitalElevationModel(hgtFile);
        Vector3 lightVector = new Vector3(-1.0, 1.0, 1.0);
        ReliefShader reliefShader = new ReliefShader(p, hgt, lightVector);
        BufferedImage shadowImage = reliefShader.shadedRelief(bL, tR,
                imageWidth, imageHeight, blurRadius);
        BufferedImage finalImage = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < imageHeight; ++i) {
            for (int j = 0; j < imageWidth; ++j) {
                int mapRgb = mapImage.getRGB(j, i);
                int shadowRgb = shadowImage.getRGB(j, i);
                Color mapPixelColor = Color.rgb(mapRgb);
                Color shadowPixelColor = Color.rgb(shadowRgb);
                java.awt.Color finalPixelColor = mapPixelColor.multiply(
                        shadowPixelColor).toAWTColor();
                int intRGB = finalPixelColor.getRGB();
                finalImage.setRGB(j, i, intRGB);
            }
        }

        try {
            ImageIO.write(finalImage, "png", new File(pngFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
