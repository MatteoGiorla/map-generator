package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.Math;
import java.io.DataInputStream;

/**
 * Classe qui représente un MNT stocké dans un fichier au format HGT.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public class HGTDigitalElevationModel implements DigitalElevationModel {

    private String fileName;
    private PointGeo bottomLeft;
    private long length;
    private double resolution;
    private double s;
    private short[][] dataMNT;

    /**
     * Constructeur de la classe HGTDigitalElevationModel
     * 
     * @param file
     *            le fichier HGT donné
     * @throws IllegalArgumentException
     *             si le format ou le nom du fichier donné n'est pas valide
     */
    public HGTDigitalElevationModel(File file) throws IllegalArgumentException {
        fileName = file.getName();
        char signLat = fileName.charAt(0);
        double latSign;
        if (signLat == 'N') {
            latSign = 1;
        } else if (signLat == 'S') {
            latSign = -1;
        } else {
            throw new IllegalArgumentException("Le nom du fichier est invalide");
        }
        String southWestCornerLat = fileName.substring(1, 3);
        char signLong = fileName.charAt(3);
        double longSign;
        if (signLong == 'E') {
            longSign = 1;
        } else if (signLong == 'W') {
            longSign = -1;
        } else {
            throw new IllegalArgumentException("Le nom du fichier est invalide");
        }
        String southWestCornerLong = fileName.substring(4, 7);
        String extension = fileName.substring(7, 11);
        if (!extension.equals(".hgt")) {
            throw new IllegalArgumentException(
                    "extension invalide. Doit être \".hgt\"");
        }
        double longitude;
        double latitude;
        try {
            longitude = longSign * Double.parseDouble(southWestCornerLong);
            latitude = latSign * Double.parseDouble(southWestCornerLat);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le nom du fichier est invalide");
        }
        bottomLeft = new PointGeo(Math.toRadians(longitude),
                Math.toRadians(latitude));
        length = file.length();
        double squareRoot = Math.sqrt(length / 2);
        if ((int) squareRoot != squareRoot) {
            throw new IllegalArgumentException("File size not valid");
        }
        resolution = Math.toRadians(1 / (squareRoot - 1));
        s = Earth.RADIUS * resolution;

        int size = (int) squareRoot;
        int i = 0;
        int j = size - 1;
        dataMNT = new short[size][size];

        // Lecture du fichier
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
                while (in.available() > 0) {
                    short value = in.readShort();
                    dataMNT[i][j] = value;
                    ++i;
                    if (i == size) {
                        i = 0;
                        --j;
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        this.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.epfl.imhof.dem.DigitalElevationModel#normalAt(ch.epfl.imhof.PointGeo)
     */
    @Override
    public Vector3 normalAt(PointGeo point) throws IllegalArgumentException {
        if ((bottomLeft.longitude() > point.longitude() || point.longitude() >= bottomLeft
                .longitude() + Math.toRadians(1))
                || (bottomLeft.latitude() > point.latitude() || point
                        .latitude() >= bottomLeft.latitude()
                        + Math.toRadians(1))) {
            throw new IllegalArgumentException(
                    "Le point pour lequel la normale est demandé ne fait pas partie de la zone couverte par le MNT");
        }
        int i = (int) ((point.longitude() - bottomLeft.longitude()) / resolution);
        int j = (int) ((point.latitude() - bottomLeft.latitude()) / resolution);
        double x = 0.5
                * s
                * (dataMNT[i][j] - dataMNT[i + 1][j] + dataMNT[i][j + 1] - dataMNT[i + 1][j + 1]);
        double y = 0.5
                * s
                * (dataMNT[i][j] + dataMNT[i + 1][j] - dataMNT[i][j + 1] - dataMNT[i + 1][j + 1]);
        double z = s * s;
        Vector3 normal = new Vector3(x, y, z);
        return normal;
    }

}