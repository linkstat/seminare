package ar.com.hmu.model;


import java.util.List;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class HorarioAbierto extends Horario {

	private boolean flexibilidadHoraria;
	private int horasSemanales;
	private List<JornadaLaboral> preferenciasHorarias;

	public HorarioAbierto(){

	}

	
	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesEspecificas(){
		return false;
	}
}//end HorarioAbierto