package ch.epfl.imhof;

import java.util.Map;
import java.util.HashMap;
import java.lang.Integer;
import java.util.Set;
import java.util.Collections;

/**
 * Attributes, représente un ensemble d'attributs et la valeur qui leur est
 * associée. La classe attributes est une table associative immuable dont les
 * clés et les valeurs sont des chaines de caractères
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class Attributes {

    private final Map<String, String> attributes;

    /**
     * Constructeur d'attributes qui construit un ensemble d'attributs avec les
     * paires clé/valeur présentes dans la table associative donnée
     * 
     * @param attributes
     *            une table associative
     */
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections
                .unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * @return vrai si la table associative est vide ou faux si elle ne l'est
     *         pas
     */
    public boolean isEmpty() {
        return this.attributes.isEmpty();
    }

    /**
     * @param key
     *            une des clés de la table associative
     * @return vrai si la table associative contient la clé donnée en paramètre
     *         ou faux si ce n'est pas le cas
     */
    public boolean contains(String key) {
        return this.attributes.containsKey(key);
    }

    /**
     * @param key
     *            une des clés de la table associative
     * @return la valeur associée à la clé donnée en entrée
     */
    public String get(String key) {
        return this.attributes.get(key);
    }

    /**
     * @param key
     *            une des clés de la table associative
     * @param defaultValue
     *            la valeur par défaut à renvoyer si la clé donnée n'est pas
     *            présente dans la table associative
     * @return la valeur associée à la clé donnée en entrée ou la valeur par
     *         défaut si elle n'est pas présente
     */
    public String get(String key, String defaultValue) {
        if (contains(key)) {
            return this.attributes.get(key);
        } else {
            return defaultValue;
        }
    }

    /**
     * @param key
     *            la valeur associée à la clé donnée en entrée
     * @param defaultValue
     *            la valeur par défaut à renvoyer si la clé donnée n'est pas
     *            présente dans la table associative
     * @return la valeur associée à la clé donnée en entrée si elle est de type
     *         int ou la valeur par défaut si elle n'est pas de type int
     */
    public int get(String key, int defaultValue) {
        try {
            return Integer.parseInt(this.attributes.get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * @param keysToKeep
     *            la liste des clés dont il faut garder les paires clé/valeur de
     *            la table associative
     * @return objet de type Attributes, une nouvelle liste de paires
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        HashMap<String, String> tempMap = new HashMap<String, String>();
        for (String s : keysToKeep) {
            if (this.attributes.containsKey(s)) {
                tempMap.put(s, this.attributes.get(s));
            }
        }
        return new Attributes(tempMap);
    }

    /**
     * Builder imbriqué statiquement servant à constuire des attributs
     */
    public final static class Builder {

        private Map<String, String> attributes = new HashMap<String, String>();

        /**
         * @param key
         *            la clé de l'élément de la table associative
         * @param value
         *            la valeur de l'élément de la table associative
         */
        public void put(String key, String value) {

            attributes.put(key, value);
        }

        /**
         * @return un objet de type Attributes
         */
        public Attributes build() {

            return new Attributes(this.attributes);
        }
    }
}