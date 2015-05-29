package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * Classe mère de toutes les classes représentant les entités d'OpenStreetMap
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public abstract class OSMEntity {

    private final long id;
    private final Attributes attributes;

    /**
     * Construit une entité OSM dotée de l'identifiant unique et des attributs
     * donnés
     * 
     * @param id
     *            l'identifiant donné
     * @param attributes
     *            les attributs uniques
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * @return l'identifiant unique de l'entité
     */
    public long id() {
        return this.id;
    }

    /**
     * @return les attributs de l'entité
     */
    public Attributes attributes() {
        return this.attributes;
    }

    /**
     * @param key
     *            l'attribut donné
     * @return vrai si et seulement si l'entité possède l'attribut passé en
     *         argument
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }

    /**
     * @param key
     *            l'attribut donné
     * @return la valeur de l'attribut donné, ou null si celui-ci n'existe pas
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }

    /**
     * Builder imbriqué statiquement servant à constuire des attributs
     */
    public abstract static class Builder {

        protected long id;
        protected Attributes.Builder attributes = new Attributes.Builder();
        protected boolean incomplete = false;

        /**
         * Construit un bâtisseur pour une entité OSM identifiée par l'entier
         * donné
         * 
         * @param id
         *            l'identifiant de l'entité
         */
        public Builder(long id) {
            this.id = id;
        }

        /**
         * Ajoute l'association (clef, valeur) donnée à l'ensemble d'attributs
         * de l'entité en cours de construction. Si un attribut de même nom
         * avait déjà été ajouté précédemment, sa valeur est remplacée par celle
         * donnée
         * 
         * @param key
         *            la clé de l'élément de la table associative
         * @param value
         *            la valeur de l'élément de la table associative
         */
        public void setAttribute(String key, String value) {
            attributes.put(key, value);
        }

        /**
         * Déclare que l'entité en cours de construction est incomplète
         */
        public void setIncomplete() {
            this.incomplete = true;
        }

        /**
         * @return vrai si et seulement si l'entité en cours de construction est
         *         incomplète, c-à-d si la méthode setIncomplete a été appelée
         *         au moins une fois sur ce bâtisseur depuis sa création
         */
        public boolean isIncomplete() {
            return incomplete;
        }
    }
}