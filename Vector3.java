package ch.epfl.imhof;

import java.lang.Math;

/**
 * Un vecteur tridimensionnel.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public class Vector3 {

    private double x;
    private double y;
    private double z;

    /**
     * Constructeur d'un vecteur tridimensionnel
     * 
     * @param x
     *            la premiere composante du vecteur
     * @param y
     *            la deuxieme composante du vecteur
     * @param z
     *            la troisième composante du vecteur
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return la norme du vecteur
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * @return le vecteur normalisé
     */
    public Vector3 normalized() {
        double norm = this.norm();
        double newX = this.x / norm;
        double newY = this.y / norm;
        double newZ = this.z / norm;
        return new Vector3(newX, newY, newZ);
    }

    /**
     * @param vector
     *            le vecteur donné
     * @return le produit scalaire entre le vecteur auquel la méthode est
     *         appliquée et le vecteur donné
     */
    public double scalarProduct(Vector3 vector) {
        return this.x * vector.x() + this.y * vector.y() + this.z * vector.z();
    }

    /**
     * @return la première composante du vecteur
     */
    public double x() {
        return this.x;
    }

    /**
     * @return la deuxième composante du vecteur
     */
    public double y() {
        return this.y;
    }

    /**
     * @return la troisième composante du vecteur
     */
    public double z() {
        return this.z;
    }
}
