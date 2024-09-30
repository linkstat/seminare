import java.util.Date;

/**
 * Write a description of class HorarioFijo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HorarioFijo extends Horario
{
    // instance variables - replace the example below with your own
    private Date horaInicio;
    private Date horaFin;

    /**
     * Constructor for objects of class HorarioFijo
     */
    public HorarioFijo(int horasSemanales, Date horaInicio, Date horaFin)
    {
        // initialise instance variables
        super(horasSemanales);
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
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
        return horasSemanales;  // Horario fijo no varía
    }

}
