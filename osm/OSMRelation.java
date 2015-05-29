package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente une relation OSM
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class OSMRelation extends OSMEntity {

    private final List<Member> members;

    /**
     * Construit une entité OSM dotée de l'identifiant unique, de sa liste des
     * membres (qui sont des entités OSM) et des attributs donnés;
     * 
     * @param id
     *            l'identificateur de la relation
     * @param members
     *            liste des membres (Chemin, Noeud, ou relation) constituant la
     *            relation
     * @param attributes
     *            attributs attachés à la relation
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<>(members));
    }

    /**
     * @return la liste des membres de la relation.
     */
    public List<Member> members() {
        return Collections.unmodifiableList(new ArrayList<>(this.members));
    }

    /**
     * Classe imbriquée statiquement servant à représenter un membre OSM.
     */
    public final static class Member {
        private final Type type;
        private final String role;
        private final OSMEntity member;

        /**
         * Construit le membre OSM avec son type parmis les 3 types d'entités
         * OSM en décrivant son rôle.
         * 
         * @param type
         *            type du membre(NODE, WAY, ou RELATION) selon un
         *            énumérateur
         * @param role
         *            chaîne de caractère décrivant le rôle du membre
         * @param member
         *            entité OSM du membre
         */
        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }

        /**
         * @return le type du membre (NODE, WAY, ou RELATION)
         */
        public Type type() {
            return this.type;
        }

        /**
         * @return la chaîne de caractère décrivant le rôle
         */
        public String role() {
            return this.role;
        }

        /**
         * @return l'entité OSM constituant le membre
         */
        public OSMEntity member() {
            return this.member;
        }

        /**
         * énumérateur imbriquée dans la classe membre, servant à choisir parmis
         * les 3 types de membre disponibles (NODE, WAY, ou RELATION)
         *
         */
        public enum Type {
            NODE, WAY, RELATION
        };
    }

    /**
     * Builder imbriqué statiquement servant à construire un chemin en étape.
     *
     */
    public final static class Builder extends
            ch.epfl.imhof.osm.OSMEntity.Builder {

        private List<Member> members = new ArrayList<>();

        /**
         * Construit un bâtisseur pour une entité OSM identifiée par l'entier
         * donné
         * 
         * @param id
         *            l'identifiant de l'entité
         */
        public Builder(long id) {
            super(id);
        }

        /**
         * ajoute un membre à la liste du bâtisseur
         * 
         * @param type
         *            type du membre
         * @param role
         *            chaîne de caractère décrivant le rôle du membre
         * @param newMember
         *            entité OSM constituant le membre
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            this.members.add(new Member(type, role, newMember));
        }

        /**
         * @return une nouvelle relation OSM construite à la base de l'instance
         *         de ce builder.
         * @throws IllegalStateException
         *             lorsque il est définit que l'entité OSM n'est pas
         *             complète
         */
        public OSMRelation build() throws IllegalStateException {
            if (super.isIncomplete()) {
                throw new IllegalStateException();
            } else {
                Attributes a = super.attributes.build();
                return new OSMRelation(super.id, this.members, a);
            }
        }
    }
}
