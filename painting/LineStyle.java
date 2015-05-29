package ch.epfl.imhof.painting;

/**
 * Classe qui regroupe tous les paramètres de style utiles au dessin d'une ligne, dont la liste est donnée plus haut.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public class LineStyle {

    private float width;
    private Color color;
    private Termination termination;
    private Join join;
    private float[] alternation;
        
    /**
     * @author mac
     *
     */
    public enum Termination {
        CAP_BUTT, CAP_ROUND, CAP_SQUARE
    };
    
    public enum Join {
        JOIN_ROUND, JOIN_BEVEL, JOIN_MITER    
    };
    
    /**  
     * Constructeur principal, prenant en arguments la totalité des cinq paramètres (largeur, couleur, terminaison, jointure et alternance des sections opaques et transparentes pour le dessin en traitillés)
     * 
     * @param width la largeur du trait
     * @param color la couleur du trait
     * @param termination la terminaison du trait
     * @param join la jointure du trait
     * @param alternation l'alternance entre les sections opaques et transparentes pour les traitillés
     * @throws IllegalArgumentException si la largeur du trait est négative ou si l'un des éléments de la séquence d'alternance des segments est négatif ou nul
     */
    public LineStyle(float width, Color color, Termination termination, Join join, float[] alternation) throws IllegalArgumentException
    {
        if(width<0.0)
        {
            throw new IllegalArgumentException("negative width : " + width);
        }
        for(int i=0; i<alternation.length; ++i)
        {
            if(alternation[i]<0.0)
            {
                throw new IllegalArgumentException("the component number " + i + " of the alternation is negative : " + alternation[i]);
            }
        }
        
        this.width = width;
        this.color = color;
        this.termination = termination;
        this.join = join;
        this.alternation = alternation;
    }
    
    /**
     * Constructeur secondaire qui ne prend en arguments que la largeur et la couleur du trait, et qui appelle le constructeur principal en lui passant des valeurs par défaut pour les autres paramètres
     * 
     * @param width la largeur du trait
     * @param color la couleur du trait
     */
    public LineStyle(float width, Color color)
    {
        this(width, color, Termination.CAP_BUTT, Join.JOIN_MITER, new float[0]);
    }
    
    /**
     * @return la largeur du trait
     */
    public float width()
    {
        return width;
    }
    
    /**
     * @return la couleur du trait
     */
    public Color color()
    {
        return color;
    }
    
    /**
     * @return la terminaison du trait
     */
    public Termination termination()
    {
        return termination;
    }
    
    /**
     * @return la jointure du trait
     */
    public Join join()
    {
        return join;
    }
    
    /**
     * @return l'alternance entre sections opaques et transparentes des traitillés
     */
    public float[] alternation()
    {
        return alternation;
    }
    
    /**
     * Méthode permettant d'obtenir un style dérivé d'un autre, avec une largeur différente
     * 
     * @param otherWidth la largeur du nouveau trait
     * @return le nouveau style de ligne
     */
    public LineStyle withWidth(float otherWidth)
    {
        return new LineStyle(otherWidth, this.color, this.termination, this.join, this.alternation);
    }
    
    /**
     * Méthode permettant d'obtenir un style dérivé d'un autre, avec une couleur différente
     * 
     * @param otherColor la couleur du nouveau trait
     * @return le nouveau style de ligne
     */
    public LineStyle withColor(Color otherColor)
    {
        return new LineStyle(this.width, otherColor, this.termination, this.join, this.alternation);
    }
    
    /**     
     * Méthode permettant d'obtenir un style dérivé d'un autre, avec une terminaison différente
     * 
     * @param otherTermination la nouvelle terminaison
     * @return le nouveau style de ligne
     */
    public LineStyle withTermination(Termination otherTermination)
    {
        return new LineStyle(this.width, this.color, otherTermination, this.join, this.alternation);
    }
    
    /**
     * Méthode permettant d'obtenir un style dérivé d'un autre, avec une jointure différente
     * 
     * @param otherJoin la nouvelle jointure
     * @return le nouveau style de ligne
     */
    public LineStyle withJoin(Join otherJoin)
    {
        return new LineStyle(this.width, this.color, this.termination, otherJoin, this.alternation);
    }
    
    /**
     * Méthode permettant d'obtenir un style dérivé d'un autre, avec une alternance différente
     * 
     * @param otherAlternation la nouvelle alternance
     * @return le nouveau style de ligne
     */
    public LineStyle withAlternation(float[] otherAlternation)
    {
        return new LineStyle(this.width, this.color, this.termination, this.join, otherAlternation);
    }
    
}
