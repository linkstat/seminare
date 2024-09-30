
/**
 * Write a description of class Horario here.
 * 
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 20240929
 */
public abstract class Horario
{
    // instance variables - replace the example below with your own
    protected int horasSemanales;

    /**
     * Constructor for objects of class Horario
     */
    public Horario(int horasSemanales)
    {
        // initialise instance variables
        this.horasSemanales = horasSemanales;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public abstract int calcularHorasTotales();
    
}
