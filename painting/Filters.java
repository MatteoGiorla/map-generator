package ch.epfl.imhof.painting;

import ch.epfl.imhof.*;
import java.util.function.Predicate;

/**
 * Classe qui représente un filtre dont le but est de déterminer, étant donnée
 * une entité attribuée, si elle doit être gardée ou non.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public final class Filters {

    private Filters() {

    }

    /**
     * Méthode prenant un nom d'attribut en argument et retournant un prédicat
     * qui n'est vrai que si la valeur attribuée à laquelle on l'applique
     * possède un attribut portant ce nom, indépendemment de sa valeur
     * 
     * @param attribute
     *            le nom d'attribut donné
     * @return un prédicat qui n'est vrai que si la valeur attribuée à laquelle
     *         on l'applique possède un attribut portant ce nom, indépendemment
     *         de sa valeur
     */
    public static Predicate<Attributed<?>> tagged(String attribute) {
        return p -> p.hasAttribute(attribute);
    }

    /**
     * Méthode prenant, en plus du nom d'attribut, un nombre variable mais
     * supérieur à 0 de valeurs d'arguments, et retournant un prédicat qui n'est
     * vrai que si la valeur attribuée à laquelle on l'applique possède un
     * attribut portant le nom donné et si de plus la valeur associée à cet
     * attribut fait partie de celles données
     * 
     * @param attribute
     *            l'attribut donné
     * @param parameters
     *            un nombre variable mais supérieur à 0 d'arguments
     * @return un prédicat qui n'est vrai que si la valeur attribuée à laquelle
     *         on l'applique possède un attribut portant le nom donné et si de
     *         plus la valeur associée à cet attribut fait partie de celles
     *         données
     */
    public static Predicate<Attributed<?>> tagged(String attribute,
            String... parameters) {
        return p -> {
            boolean b = false;
            if (p.hasAttribute(attribute)) {
                for (int i = 0; i < parameters.length; ++i) {
                    b = b
                            || (p.attributeValue(attribute, "notAnAttribute")
                                    .equals(parameters[i]));
                }
            }
            return b;
        };
    }

    /**
     * Méthode prenant un numéro (entier) de couche en argument et retournant un
     * prédicat qui n'est vrai que lorsqu'on l'applique à une entité attribuée
     * appartenant à cette couche
     * 
     * @param layerNumber
     *            le nombre de couches
     * @return un prédicat qui n'est vrai qie lorsqu'on l'appllique à une entité
     *         attribuée appartenant à cette couche
     */
    public static Predicate<Attributed<?>> onLayer(int layerNumber) {
        return p -> p.attributeValue("layer", 42) == layerNumber;
    }
}
