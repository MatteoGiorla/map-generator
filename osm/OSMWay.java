package ch.epfl.imhof.osm;

import java.util.*;
import ch.epfl.imhof.*;

/**
 * Représente un chemin OSM
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class OSMWay extends OSMEntity {

    private final List<OSMNode> nodes;

    /**
     * Construit une entité OSM dotée de l'identifiant unique, de sa liste de
     * noeud et des attributs donnés
     * 
     * @param id
     *            L'identificateur du chemin
     * @param nodes
     *            La liste de noeud dont le chemin est constitué
     * @param attributes
     *            attributs attaché au chemin
     * @throws IllegalArgumentException
     *             Lorsque la liste de noeud compte moins de deux points (donc
     *             n'est pas un chemin)
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes)
            throws IllegalArgumentException {
        super(id, attributes);
        if (nodes.size() < 2) {
            throw new IllegalArgumentException("La liste d'OSMNode est trop petite pour constituer une OSMWay");
        } else {
            this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
        }

    }

    /**
     * permet de connaître le nombre de noeud constituant le chemin
     * 
     * @return le nombre de noeud dans le chemin OSM
     */
    public int nodesCount() {
        return this.nodes.size();
    }

    /**
     * renvoie une vue sur la liste de noeud du chemin.
     * 
     * @return une version non modifiable (et immuable même) de la liste de
     *         noeuds constituant le chemin.
     */
    public List<OSMNode> nodes() {
        return Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    /**
     * @return une version non modifiable de la liste sans son dernier élément
     *         si celui ci est égal au premier
     */
    public List<OSMNode> nonRepeatingNodes() {
        if (this.isClosed()) {
            int lastBound = nodes().size();
            return nodes().subList(0, lastBound - 1);
        } else {
            return nodes();
        }
    }

    /**
     * retourne vrai ssi le chemin est fermé, c'est à dire que son dernier noeud
     * est égal au premier.
     * 
     * @return un boolean de type true si le premier élément est égal au dernier
     */
    public boolean isClosed() {
        if (firstNode().equals(lastNode())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * renvoie le premier noeud du chemin
     * 
     * @return le premier OSMNode de la liste nodes
     */
    public OSMNode firstNode() {
        return nodes.get(0);
    }

    /**
     * renvoie le dernier noeud du chemin
     * 
     * @return le dernier OSMNode de la liste nodes
     */
    public OSMNode lastNode() {
        int lastItem = nodes.size() - 1;
        return nodes.get(lastItem);
    }

    /**
     * Builder imbriqué statiquement servant à construire un chemin en étape.
     *
     */
    public static class Builder extends OSMEntity.Builder {
        private List<OSMNode> nodes = new ArrayList<>();

        /**
         * Constructeur du Builder faisant appel au constructeur de la classe
         * mère.
         * 
         * @param id
         *            identificateur du chemin qui sera construit ensuite par le
         *            Builder
         */
        public Builder(long id) {
            super(id);
        }

        /**
         * méthode ajoutant un noeud à la liste de noeud.
         * 
         * @param newNode
         *            Noeud qui va être ajouté à la liste de noeud constituant
         *            le chemin.
         */
        public void addNode(OSMNode newNode) {
            nodes.add(newNode);
        }

        /**
         * Construit le chemin
         * 
         * 
         * @return un nouveau chemin OSM construit à la base de l'instance de ce
         *         builder.
         * @throws IllegalStateException
         *             lorsque le chemin est incomplet
         */
        public OSMWay build() throws IllegalStateException {
            if (this.isIncomplete()) {
                throw new IllegalStateException();
            } else {
                Attributes a = super.attributes.build();
                try {
                    return new OSMWay(super.id, this.nodes, a);
                } catch (IllegalArgumentException e) {
                    throw new IllegalStateException();
                }
            }
        }

        @Override
        public boolean isIncomplete() {
            if (this.nodes.isEmpty() || nodes.size()<2 || incomplete) {
                return true;
            } else {
                return false;
            }
        }
    }
}