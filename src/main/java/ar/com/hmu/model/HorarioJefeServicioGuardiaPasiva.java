package ar.com.hmu.model;


import java.util.List;
import ar.com.hmu.util.DiaSemana;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class HorarioJefeServicioGuardiaPasiva extends Horario {

	private List<DiaSemana> diasLaborables;
	private int horasPorDia;

	public HorarioJefeServicioGuardiaPasiva(){

	}

	
	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesEspecificas(){
		return false;
	}
}//end HorarioJefeServicioGuardiaPasiva