package ch.epfl.imhof.osm;

import java.util.*;


/**
 * Classe qui représente une carte OpenStreetMap, c'est-à-dire un ensemble de
 * chemins et de relations.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class OSMMap {

    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    /**
     * construit une carte OSM avec les chemins et les relations donnés.
     * 
     * @param ways
     *            les chemins donnés
     * @param relations
     *            les relations données
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {

        this.ways = Collections.unmodifiableList(new ArrayList<>(ways));
        this.relations = Collections
                .unmodifiableList(new ArrayList<>(relations));
    }

    /**
     * @return la liste des chemins de la carte
     */
    public List<OSMWay> ways() {
        return this.ways;
    }

    /**
     * @return la liste des relations de la carte
     */
    public List<OSMRelation> relations() {
        return this.relations;
    }

    /**
     * Builder imbriqué statiquement servant à construire un nœud en plusieurs
     * étapes
     */
    public final static class Builder {

        private Map<Long, OSMWay> ways;
        private Map<Long, OSMRelation> relations;
        private Map<Long, OSMNode> node;

        /**
         * Construit un batisseur de Map
         */
        public Builder() {
            ways = new HashMap<>();
            relations = new HashMap<>();
            node = new HashMap<>();
        }

        /**
         * ajoute le nœud donné au bâtisseur
         * 
         * @param newNode
         *            le noeud donné
         */
        public void addNode(OSMNode newNode) {
            node.put(newNode.id(), newNode);
        }

        /**
         * retourne le nœud dont l'identifiant unique est égal à celui donné, ou
         * null si ce nœud n'a pas été ajouté précédemment au bâtisseur
         * 
         * @param id
         *            l'identifiant donné
         * @return le noeud dont l'id unique est égal à celui donné
         */
        public OSMNode nodeForId(long id) {
           return node.get(id);
        }

        /**
         * ajoute le chemin donné à la carte en cours de construction
         * 
         * @param newWay
         *            le chemin à ajouter
         */
        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }

        /**
         * retourne le chemin dont l'identifiant unique est égal à celui donné,
         * ou null si ce chemin n'a pas été ajouté précédemment au bâtisseur
         * 
         * @param id
         *            l'identifiant donné
         * @return le chemin dont l'id unique est égal à celui donné
         */
        public OSMWay wayForId(long id) {
                return ways.get(id);
        }

        /**
         * ajoute la relation donnée à la carte en cours de construction
         * 
         * @param newRelation
         *            la relation donnée
         */
        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }

        /**
         * retourne la relation dont l'identifiant unique est égal à celui
         * donné, ou null si cette relation n'a pas été ajoutée précédemment au
         * bâtisseur
         * 
         * @param id
         *            l'identifiant donné
         * @return la relation dont l'id unique est égal à celui donné
         */
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }

        /**
         * construit une carte OSM avec les chemins et les relations ajoutés
         * jusqu'à présent
         * 
         * @return un objet de type OSMMap
         */
        public OSMMap build() {

            return new OSMMap(ways.values(), relations.values());
        }

    }
}