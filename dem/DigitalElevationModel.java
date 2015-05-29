package ch.epfl.imhof.dem;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.PointGeo;

/**
 * Interface représentant un modèle numérique du terrain.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public interface DigitalElevationModel extends AutoCloseable {

    /**
     * Méthode qui prend un point en coordonnées WGS 84 en argument et retourne
     * le vecteur normal à la Terre en ce point, déterminé au moyen de la
     * technique approximative présentée ci-dessus.
     * 
     * @param point
     *            le point donné, en coordonnées WGS 84
     * @return le vecteur normal à la Terre en ce point
     * @throws IllegalArgumentException
     *             si le point pour lequel la normale est demandé ne fait pas
     *             partie de la zone couverte par le MNT
     */
    Vector3 normalAt(PointGeo point) throws IllegalArgumentException;

}
