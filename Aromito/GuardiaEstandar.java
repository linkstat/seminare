
/**
 * Write a description of class GuardiaEstandar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GuardiaEstandar extends Horario
{
    // instance variables - replace the example below with your own
    private int maxGuardiasContinuas;  // Por ejemplo, no más de 2 guardias continuas

    /**
     * Constructor for objects of class GuardiaEstandar
     */
    public GuardiaEstandar(int horasSemanales, int maxGuardiasContinuas)
    {
        // initialise instance variables
        super(horasSemanales);
        this.maxGuardiasContinuas = maxGuardiasContinuas;
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public boolean validarGuardia(int guardiasContinuas)
    {
        // put your code here
        return guardiasContinuas <= maxGuardiasContinuas;
    }
    
    @Override
    public int calcularHorasTotales()
    {
        // Suponiendo que las guardias son de 12 horas
        return horasSemanales;
    }
}
