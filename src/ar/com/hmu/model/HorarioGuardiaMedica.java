package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class HorarioGuardiaMedica extends Horario {

	private int duracionGuardiaHoras;
	private List<LocalDateTime> fechasGuardias;
	private int numeroGuardiasSemanal;
	private boolean permitirGuardiasContinuas;
	private int tiempoDescansoMinimoHoras;

	public HorarioGuardiaMedica(){

	}

	
	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesEspecificas(){
		return false;
	}
}//end HorarioGuardiaMedica