package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class HorarioFeriante extends Horario {

	private List<LocalDateTime> diasNoLaborables;
	private int duracionGuardiaHoras;
	private List<JornadaLaboral> guardiasProgramadas;
	private int horasMinimasMensuales;

	public HorarioFeriante(){

	}

	
	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesEspecificas(){
		return false;
	}
}//end HorarioFeriante