package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class HorarioNocturno extends Horario {

	private List<LocalDateTime> diasProgramados;
	private int duracionJornadaHoras;
	private int numeroJornadasMensuales;

	public HorarioNocturno(){

	}

	
	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesEspecificas(){
		return false;
	}
}//end HorarioNocturno