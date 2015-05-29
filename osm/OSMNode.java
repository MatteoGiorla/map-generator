package ch.epfl.imhof.osm;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Attributes;

/**
 * Représente un nœud OSM
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class OSMNode extends OSMEntity {

    private final PointGeo position;

    /**
     * Construit un nœud OSM avec l'identifiant, la position et les attributs
     * donnés
     * 
     * @param id
     *            l'identifiant donné
     * @param position
     *            la position donnée
     * @param attributes
     *            les attributs donnés
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        this.position = position;
    }

    /**
     * @return la position du nœud
     */
    public PointGeo position() {

        return this.position;
    }

    /**
     * Builder imbriqué statiquement servant à construire un nœud en plusieurs
     * étapes
     */
    public final static class Builder extends
            ch.epfl.imhof.osm.OSMEntity.Builder {

       private PointGeo position;

        /**
         * Construit un bâtisseur pour un nœud ayant l'identifiant et la
         * position donnés
         * 
         * @param id
         *            l'identifiant de l'entité
         * @param position
         *            la position donnée
         */
        public Builder(long id, PointGeo position) {
            super(id);
            this.position = position;
        }

        /**
         * construit un nœud OSM avec l'identifiant et la position passés au
         * constructeur, et les éventuels attributs ajoutés jusqu'ici au
         * bâtisseur. Lève l'exception IllegalStateException si le nœud en cours
         * de construction est incomplet, c-à-d si la méthode setIncomplete a
         * été appelée sur ce bâtisseur depuis sa création
         * 
         * @return un objet de type OSMNode
         */
        public OSMNode build() throws IllegalStateException {
            if (isIncomplete()) {
                throw new IllegalStateException();
            } else {
                Attributes a = super.attributes.build();
                return new OSMNode(super.id, this.position, a);
            }
        }
    }
}