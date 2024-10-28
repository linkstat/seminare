package ar.com.hmu.model;


import java.util.List;
import ar.com.hmu.utils.DiaSemana;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class HorarioEstandar extends Horario {

	private List<DiaSemana> diasLaborables;
	private int horasPorDia;

	public HorarioEstandar(){

	}

	
	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesEspecificas(){
		return false;
	}
}//end HorarioEstandar