package ch.epfl.imhof.dem;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;

/**
 * Classe permettant de dessiner un relief ombré coloré.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public class ReliefShader {

    private Projection projection;
    private HGTDigitalElevationModel fieldModel;
    private Vector3 lightSourceVector;

    /**
     * Constructeur de la classe ReliefShader
     * 
     * @param projection
     *            la projection utilisée pour le relief
     * @param fieldModel
     *            le model de terrain extrait du fichier HGT
     * @param lightSourceVector
     *            le vecteur qui pointe en direction de la source lumineuse
     *            utilisée pour la création du relief
     */
    public ReliefShader(Projection projection,
            HGTDigitalElevationModel fieldModel, Vector3 lightSourceVector) {
        this.projection = projection;
        this.fieldModel = fieldModel;
        this.lightSourceVector = lightSourceVector;
    }

    /**
     * Méthode qui créée l'image du relief
     * 
     * @param pBL
     *            le point bas-gauche de l'image
     * @param pTR
     *            le point heut-droite de l'image
     * @param width
     *            la largeur de l'image
     * @param height
     *            la hauteur de l'image
     * @param radius
     *            le rayon de floutage
     * @return l'image du relief
     */
    public BufferedImage shadedRelief(Point pBL, Point pTR, int width,
            int height, float radius) {
        if (radius == 0) {
            System.out.println("width : " + width + " height : " + height);
            Function<Point, Point> coordinateChange = Point
                    .alignedCoordinateChange(new Point(0, height), pBL, new Point(
                            width, 0), pTR);
            return drawShadedReliefWithoutBlur(width, height, coordinateChange);
        } else {
            Kernel kernel = calculateBlurKernel(radius);
            int zoneTampon = (int) Math.floor(kernel.getWidth() / 2);
            Function<Point, Point> coordinateChange = Point
                    .alignedCoordinateChange(new Point(zoneTampon, height+zoneTampon), pBL, new Point(
                            width+zoneTampon, zoneTampon), pTR);
            BufferedImage imageToCut = blurImage(
                    drawShadedReliefWithoutBlur(width + 2 * zoneTampon, height
                            + 2 * zoneTampon, coordinateChange), kernel);
            return imageToCut.getSubimage(zoneTampon,zoneTampon,width,height);
        }
    }

    private BufferedImage drawShadedReliefWithoutBlur(int width, int height,
            Function<Point, Point> function) {
        BufferedImage shadedRelief = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Point pixelPosition = new Point(j, i);
                Point currentPosition = function.apply(pixelPosition);
                PointGeo currentPositionGeo = projection
                        .inverse(currentPosition);
                Vector3 normalVector = fieldModel.normalAt(currentPositionGeo);
                double cosinus = normalVector.scalarProduct(lightSourceVector)
                        / lightSourceVector.norm() / normalVector.norm();

                double r = 0.5 * (cosinus + 1.0);
                double g = 0.5 * (cosinus + 1.0);
                double b = 0.5 * (0.7 * cosinus + 1.0);

                Color pixelColor = Color.rgb(r, g, b);
                java.awt.Color finalPixelColor = pixelColor.toAWTColor();
                int intRGB = finalPixelColor.getRGB();
                shadedRelief.setRGB(j, i, intRGB);
            }
        }

        return shadedRelief;
    }

    private Kernel calculateBlurKernel(double radius) {
        double sigma = radius / 3;
        double tempN = 2.0 * Math.ceil(radius) + 1;
        int n = (int) tempN;
        float[] VectorGauss = new float[n];
        float sum = 0f;
        for (int i = 0; i < VectorGauss.length; ++i) {
            double distanceFromCenter = (double) i - Math.ceil(n / 2);
            float gaussIndice = (float) Math
                    .pow(Math.E,
                            -((distanceFromCenter * distanceFromCenter) / (2 * sigma * sigma)));
            VectorGauss[i] = gaussIndice;
            sum = sum + gaussIndice;
        }

        // normalisation du vecteur.
        for (int i = 0; i < VectorGauss.length; ++i) {
            VectorGauss[i] = VectorGauss[i] / sum;
        }

        return new Kernel(n, 1, VectorGauss);

    }

    private BufferedImage blurImage(BufferedImage image, Kernel kernel) {
        Kernel horizontalKernel = kernel;
        BufferedImage target = new BufferedImage(image.getWidth(),
                image.getHeight(), image.getType());
        Kernel verticalKernel = new Kernel(1, kernel.getWidth(),
                kernel.getKernelData(null));
        ConvolveOp verticalBlurrer = new ConvolveOp(verticalKernel,
                ConvolveOp.EDGE_NO_OP, null);
        ConvolveOp horizontalBlurrer = new ConvolveOp(horizontalKernel,
                ConvolveOp.EDGE_NO_OP, null);
        target = verticalBlurrer.filter(horizontalBlurrer.filter(image, null),
                null);
        return target;
    }
}
