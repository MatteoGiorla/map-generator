package ch.epfl.imhof;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collections;

/**
 * Classe qui représente un graphe non orienté. Il s'agit d'une classe générique
 * dont le paramètre de type, N ci-dessous, représente le type des nœuds du
 * graphe
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */

public final class Graph<N> {

    private final Map<N, Set<N>> neighbors;

    /**
     * construit un graphe non orienté avec la table d'adjacence donnée
     * 
     * @param neighbors
     *            la table d'adjacence donnée
     */
    public Graph(Map<N, Set<N>> neighbors) {
        Map<N, Set<N>> tempNeighbors = new HashMap<>();
        for (Map.Entry<N, Set<N>> entry : neighbors.entrySet()) {
            tempNeighbors.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        this.neighbors = tempNeighbors;
    }

    /**
     * Builder imbriqué statiquement servant à construire un nœud en plusieurs
     * étapes, générique comme la classe graphe.
     */
    public static final class Builder<N> {

        private Map<N, Set<N>> neighbors = new HashMap<>();

        /**
         * ajoute le nœud donné au graphe en cours de construction, s'il n'en
         * faisait pas déjà partie
         * 
         * @param n
         *            le noeud donné
         */
        public void addNode(N n) {
            neighbors.putIfAbsent(n, new HashSet<>());
        }

        /**
         * ajoute une arête entre les deux nœuds donnés au graphe en cours de
         * construction. En d'autres termes, cette méthode ajoute le premier
         * nœud à l'ensemble des voisins du second, et vice versa. Lève
         * l'exception IllegalArgumentException si l'un des nœuds n'appartient
         * pas au graphe en cours de construction
         * 
         * @param n1
         *            le premier des 2 noeuds donnés
         * @param n2
         *            le deuxième des 2 noeuds donnés
         * @throws IllegalArgumentException
         *             si l'un des nœuds n'appartient pas au graphe en cours de
         *             construction
         */
        public void addEdge(N n1, N n2) throws IllegalArgumentException {
            if ((!neighbors.containsKey(n1)) || (!neighbors.containsKey(n2))) {
                throw new IllegalArgumentException();
            } else {
                neighbors.get(n1).add(n2);
                neighbors.get(n2).add(n1);
            }
        }

        /**
         * construit le graphe composé des nœuds et arêtes ajoutés jusqu'à
         * présent au bâtisseur
         * 
         * @return un objet de type Graph
         */
        public Graph<N> build() {
            return new Graph<N>(neighbors);
        }

    }

    /**
     * @return l'ensemble des nœuds du graphe
     */
    public Set<N> nodes() {
        Map<N, Set<N>> tempMap = Collections.unmodifiableMap(new HashMap<>(
                neighbors));
        return tempMap.keySet();
    }

    /**
     * retourne l'ensemble des nœuds voisins du nœud donné. Lève l'exception
     * IllegalArgumentException si le nœud donné ne fait pas partie du graphe
     * 
     * @param node
     *            le noeud donné
     * @return l'ensemble des noeuds voisins du noeud donné
     * @throws IllegalArgumentException
     *             si le noeud donné ne fait pas partie du graphe
     */
    public Set<N> neighborsOf(N node) throws IllegalArgumentException {
        if (!neighbors.containsKey(node)) {
            throw new IllegalArgumentException();
        } else {
            return Collections.unmodifiableSet(new HashSet<>(neighbors
                    .get(node)));
        }
    }
}