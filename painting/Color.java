package ch.epfl.imhof.painting;

/**
 * Classe qui représente une couleur, décrite par ses trois composantes rouge, verte et bleue.
 * 
 * @author Cédric Viaccoz (250396)
 * @author Matteo Giorla (246524)
 */
public class Color {

    public final static Color CONST_RED = new Color(1, 0, 0);
    public final static Color CONST_GREEN = new Color(0, 1, 0);
    public final static Color CONST_BLUE = new Color(0, 0, 1);
    public final static Color CONST_BLACK = new Color(0, 0, 0);
    public final static Color CONST_WHITE = new Color(1, 1, 1);
    
    private double red;
    private double green;
    private double blue;

    /**
     * Le constructeur privé de Color
     * 
     * @param red la composante rouge de la couleur
     * @param green la composante verte de la couleur
     * @param blue la composante bleue de la couleur
     */
    private Color(double red, double green, double blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    /**
     * @return la composante rouge de la couleur
     */
    public double red()
    {
        return red;
    }
    
    /**
     * @return la composante verte de la couleur
     */
    public double green()
    {
        return green;
    }
    
    /**
     * @return la composante bleue de la couleur
     */
    public double blue()
    {
        return blue;
    }
    
    /**
     * Méthode construisant la couleur grise dont les trois composantes sont égales à cette valeur.
     * 
     * @param value la valeur des trois composantes
     * @return un objet de type Color représentant la couleur grise
     * @throws IllegalArgumentException si la valeur n'est pas comprise entre 0 et 1
     */
    public static Color gray(double value) throws IllegalArgumentException
    {
        if(value<0.0 || value>1.0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            return new Color(value, value, value);
        }
    }
    
    /**
     * Méthode prenant en arguments les trois composantes individuelles, comprises entre 0 et 1, et construisant la couleur correspondantes
     * 
     * @param redComp la composante rouge de la couleur
     * @param greenComp la composante verte de la couleur
     * @param blueComp la composante bleue de la couleur
     * @return la couleur construite à partir des trois composantes
     * @throws IllegalArgumentException si une des composantes n'est pas comprise entre 0 et 1
     */
    public static Color rgb(double redComp, double greenComp, double blueComp) throws IllegalArgumentException
    {
        if(redComp<0.0 || redComp>1.0)
        {
            throw new IllegalArgumentException("invalid red component: " + redComp);
        }
        if(greenComp<0.0 || greenComp>1.0)
        {
            throw new IllegalArgumentException("invalid green component: " + greenComp);
        }
        if(blueComp<0.0 || blueComp>1.0)
        {
            throw new IllegalArgumentException("invalid blue component: " + blueComp);
        }
        
        return new Color(redComp, greenComp, blueComp);
        
    }
    
    /**
     * Méthode prenant en arguments les trois composantes individuelles « empaquetées » dans un entier de type int, la composante rouge se trouvant dans les bits 23 à 16, la composante verte dans les bits 15 à 8 et la composante bleue dans les bits 7 à 0
     * 
     * @param packedColor les trois composantes empaquetées dans un entier de type int
     * @return la couleur créée à partir de ces 3 composantes
     * @throws IllegalArgumentException si l'entier n'est pas empaqueté de manière conforme
     */
    public static Color rgb(int packedColor) throws IllegalArgumentException
    {
        if((packedColor >>24) > 0)
        {
            throw new IllegalArgumentException("Entier compacté non conforme");
        }
        else
        {
            return new Color(((packedColor >> 16) & 0xFF) / 255d, ((packedColor >>  8) & 0xFF) / 255d, ((packedColor >>  0) & 0xFF) / 255d);
        }
    }
    
    /**
     * Méthode permettant de multiplier deux couleurs entre elles, ce qui se fait par multiplication des composantes individuelles.
     * 
     * @param secondColor la couleur par laquelle multiplier la couleur à laquelle la méthode est appliquée
     * @return la couleur obtenue par multiplication des 2 couleurs
     */
    public Color multiply(Color secondColor)
    {
        double newRed = this.red()*secondColor.red();
        double newGreen = this.green()*secondColor.green();
        double newBlue = this.blue()*secondColor.blue();
        return new Color(newRed, newGreen, newBlue);
    }
    
    /**
     * Méthode qui converti une couleur de type ch.epfl.imhof.painting.Color en couleur de type java.awt.Color.
     * 
     * @return la couleur de type java.awt.Color
     */
    public java.awt.Color toAWTColor()
    {
        return new java.awt.Color((float)red, (float)green, (float)blue);
    }
    
}
