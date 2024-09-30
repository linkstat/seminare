import java.util.Date;
import java.util.List;

/**
 * Write a description of class HorarioVariable here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HorarioVariable extends Horario
{
    // instance variables - replace the example below with your own
    private List<Date> diasTrabajados;  // Lista de días que trabaja en la semana

    /**
     * Constructor for objects of class HorarioVariable
     */
    public HorarioVariable(int horasSemanales, List<Date> diasTrabajados)
    {
        // initialise instance variables
        super(horasSemanales);
        this.diasTrabajados = diasTrabajados;
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
        // Aquí podrías implementar un cálculo basado en los días trabajados
        return horasSemanales;
    }
}
