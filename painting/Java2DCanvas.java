package ch.epfl.imhof.painting;

import java.awt.image.BufferedImage;
import java.util.function.Function;

import ch.epfl.imhof.geometry.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * Classe qui représente une toile qui dessine les primitives qu'on lui demande
 * de dessiner dans une image discrète
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public class Java2DCanvas implements Canvas {

    private Point pBL;
    private Point pTR;
    private int width;
    private int height;
    private int resolution;
    private ch.epfl.imhof.painting.Color backgroundColor;
    private Function<Point, Point> coordinateChange;
    private BufferedImage image;
    private Graphics2D ctx;

    /**
     * Le constructeur de la toile
     * 
     * @param pBL
     *            le point bas-gauche
     * @param pTR
     *            le point haut-droit
     * @param width
     *            la largeur de la toile
     * @param height
     *            la hauteur de la toile
     * @param resolution
     *            la résolution de l'image
     * @param color
     *            la couleur de l'arrière plan de la toile
     */
    public Java2DCanvas(Point pBL, Point pTR, int width, int height,
            int resolution, ch.epfl.imhof.painting.Color color) {
        this.pBL = pBL;
        this.pTR = pTR;
        this.width = width;
        this.height = height;
        this.resolution = resolution;
        this.backgroundColor = color;
        coordinateChange = Point.alignedCoordinateChange(pBL, new Point(0,
                (height / (double) resolution) * 72), pTR, new Point(
                (width / (double) resolution) * 72, 0));
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        ctx = image.createGraphics();
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        ctx.setColor(backgroundColor.toAWTColor());
        ctx.fillRect(0, 0, width, height);
        ctx.scale((double) resolution / 72.0, (double) resolution / 72.0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.epfl.imhof.painting.Canvas#drawPolyLine(ch.epfl.imhof.geometry.PolyLine
     * , ch.epfl.imhof.painting.LineStyle)
     */
    @Override
    public void drawPolyLine(PolyLine polyline, LineStyle lineStyle) {
        ctx.setColor(lineStyle.color().toAWTColor());
        Stroke s;
        if (lineStyle.alternation().length != 0) {
            s = new BasicStroke(lineStyle.width(), lineStyle.termination()
                    .ordinal(), lineStyle.join().ordinal(), (float) 10.0,
                    lineStyle.alternation(), (float) 0);
        } else {
            s = new BasicStroke(lineStyle.width(), lineStyle.termination()
                    .ordinal(), lineStyle.join().ordinal(), (float) 10.0);
        }
        ctx.setStroke(s);
        Path2D path = new Path2D.Double();
        path.moveTo(coordinateChange.apply(polyline.points().get(0)).x(),
                coordinateChange.apply(polyline.points().get(0)).y());
        int i=0;
        for (Point point : polyline.points()) {
            if(i!=0)
            {
                path.lineTo(coordinateChange.apply(point).x(), coordinateChange
                        .apply(point).y());
            }
            ++i;
        }
        if (polyline.isClosed()) {
            path.closePath();
        }
        ctx.draw(path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.epfl.imhof.painting.Canvas#drawPolygon(ch.epfl.imhof.geometry.Polygon,
     * ch.epfl.imhof.painting.Color)
     */
    @Override
    public void drawPolygon(Polygon polygon, ch.epfl.imhof.painting.Color color) {
        ctx.setColor(color.toAWTColor());
        Path2D path = new Path2D.Double();
        path.moveTo(
                coordinateChange.apply(polygon.shell().points().get(0)).x(),
                coordinateChange.apply(polygon.shell().points().get(0)).y());
        for (Point point : polygon.shell().points()) {
            path.lineTo(coordinateChange.apply(point).x(), coordinateChange
                    .apply(point).y());
        }
        path.closePath();
        Area surface = new Area(path);
        if (!polygon.holes().isEmpty()) {
            for (ClosedPolyLine polyline : polygon.holes()) {
                Path2D hole = new Path2D.Double();
                hole.moveTo(coordinateChange.apply(polyline.points().get(0))
                        .x(), coordinateChange.apply(polyline.points().get(0))
                        .y());
                for (Point point : polyline.points()) {
                    hole.lineTo(coordinateChange.apply(point).x(),
                            coordinateChange.apply(point).y());
                }
                Area holeToSubtract = new Area(hole);
                surface.subtract(holeToSubtract);
            }
        }
        ctx.fill(surface);
    }

    /**
     * Méthode qui retourne l'image créée
     * 
     * @return l'image créée
     */
    public BufferedImage image() {
        return image;
    }

}
