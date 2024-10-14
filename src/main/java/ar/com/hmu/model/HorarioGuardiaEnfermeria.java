package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class HorarioGuardiaEnfermeria extends Horario {

	private int duracionGuardia10Horas;
	private int duracionGuardia12Horas;
	private List<LocalDateTime> fechasGuardias;
	private int numeroGuardias10Horas;
	private int numeroGuardias12Horas;

	public HorarioGuardiaEnfermeria(){

	}

	
	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesEspecificas(){
		return false;
	}
}//end HorarioGuardiaEnfermeria