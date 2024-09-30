
/**
 * Write a description of class HorarioAbierto here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HorarioAbierto extends Horario
{
    // instance variables - replace the example below with your own
    //nada

    /**
     * Constructor for objects of class HorarioAbierto
     */
    public HorarioAbierto()
    {
        // initialise instance variables
        super(35);  // Por ejemplo, 35 horas semanales
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    @Override
    public int calcularHorasTotales()
    {
        // Para horarios abiertos, podrías devolver simplemente las horas semanales
        return horasSemanales;
    }
}
