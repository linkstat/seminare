package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import ar.com.hmu.utils.DiaSemana;


/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class HorarioDXI extends Horario {

	private Map<DiaSemana, Integer> distribucionHoraria;
	private Map<DiaSemana, LocalDateTime> horaInicioPorDia;
	private int horasSemanales;

	public HorarioDXI(){

	}

	
	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesEspecificas(){
		return false;
	}
}//end HorarioDXI