package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe qui représente un polygone
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public final class Polygon {

    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;

    /**
     * Construit un polygone avec l'enveloppe et les trous donnés
     * 
     * @param shell
     *            l'enveloppe donnée
     * @param holes
     *            la liste de trous
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<>(holes));
    }

    /**
     * Construit un polygone avec l'enveloppe donnée, sans trous
     * 
     * @param shell
     *            l'enveloppe donnée
     */
    public Polygon(ClosedPolyLine shell) {
        this.shell = shell;
        this.holes = Collections.emptyList();
    }

    /**
     * Getter de l'enveloppe
     * 
     * @return une copie de l'enveloppe
     */
    public ClosedPolyLine shell() {
        return shell;
    }

    /**
     * Getter de la liste de trous
     * 
     * @return une copie de la liste de trous
     */
    public List<ClosedPolyLine> holes() {
        return holes;
    }
}