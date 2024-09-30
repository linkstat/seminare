import java.util.Date;
import java.util.List;

/**
 * Write a description of class Main here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Main
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class Main
     */
    public Main()
    {
        // initialise instance variables
        //nada
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public static void main(String[] args)
    {
        // Crear un horario fijo
        Horario horarioFijo = new HorarioFijo(35, new Date(9, 0), new Date(17, 0));

        // Crear una guardia de 12 horas
        Horario guardiaEstandar = new GuardiaEstandar(48, 2);  // 48 horas semanales, no más de 2 guardias continuas

        // Crear un horario variable
        List<Date> diasTrabajados = Array.asList(Date.MONDAY, Date.WEDNESDAY, Date.FRIDAY);
        Horario horarioVariable = new HorarioVariable(35, diasTrabajados);

        // Usar polimorfismo para trabajar con los horarios
        System.out.println("Horas totales de horario fijo: " + horarioFijo.calcularHorasTotales());
        System.out.println("Horas totales de guardia: " + guardiaEstandar.calcularHorasTotales());
        System.out.println("Horas totales de horario variable: " + horarioVariable.calcularHorasTotales());

        // Validar una guardia de 12 horas
        if (((GuardiaEstandar) guardiaEstandar).validarGuardia(1)) {
            System.out.println("Guardia válida");
        } else {
            System.out.println("Guardia no válida");
        }
    }
}
