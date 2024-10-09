package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
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

	public boolean verificarCondicionesEspec�ficas(){
		return false;
	}
}//end HorarioDXI