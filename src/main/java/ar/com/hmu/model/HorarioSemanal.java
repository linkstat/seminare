package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import ar.com.hmu.utils.DiaSemana;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
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