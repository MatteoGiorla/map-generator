package ch.epfl.imhof;

/**
 * Attributed
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class Attributed<T> {

    private final T value;
    private final Attributes attributes;

    /**
     * Construit une valeur attribuée dont la valeur et les attributs sont ceux
     * donnés
     * 
     * @param value
     *            la valeur donnée
     * @param attributes
     *            les attributs donnés
     */
    public Attributed(T value, Attributes attributes) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * @return la valeur du paramètre générique "value" à laquelle les attributs
     *         sont attachés
     */
    public T value() {
        return this.value;
    }

    /**
     * @return les attributs attachés à la valeur
     */
    public Attributes attributes() {
        return this.attributes;
    }

    /**
     * @param attributeName
     *            l'attribut passé en argument
     * @return vrai si et seulement si les attributs incluent celui dont le nom
     *         est passé en argument
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }

    /**
     * @param attributeName
     *            l'attribut passé en argument
     * @return la valeur associée à l'attribut donné, ou null si celui-ci
     *         n'existe pas
     */
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * @param attributeName
     *            l'attribut passé en argument
     * @param defaultValue
     *            la valeur à renvoyer si la valeur associée à l'attribut
     *            n'existe pas
     * @return la valeur associée à l'attribut donné ou la valeur par défaut
     *         donnée si celui-ci n'existe pas
     */
    public String attributeValue(String attributeName, String defaultValue) {
        if (attributes.contains(attributeName)) {
            return attributes.get(attributeName);
        } else {
            return defaultValue;
        }
    }

    /**
     * @param attributeName
     *            l'attribut passé en argument
     * @param defaultValue
     *            la valeur sous forme de int à renvoyer si la valeur associée à
     *            l'attribut n'existe pas ou qu'elle n'est pas sous forme de int
     * @return la valeur entière associée à l'attribut donné ou la valeur par
     *         défaut si celui-ci n'existe pas ou si la valeur n'est pas un
     *         entier valide
     */
    public int attributeValue(String attributeName, int defaultValue) {
        try {
            return Integer.parseInt(attributes.get(attributeName));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}