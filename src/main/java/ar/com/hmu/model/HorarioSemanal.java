package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import ar.com.hmu.util.DiaSemana;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class HorarioSemanal extends Horario {

	private Map<DiaSemana, Integer> distribucionSemanal;
	private Map<DiaSemana, LocalDateTime> horaInicioPorDia;

	public HorarioSemanal(){

	}

	
	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesEspecificas(){
		return false;
	}
}//end HorarioSemanal