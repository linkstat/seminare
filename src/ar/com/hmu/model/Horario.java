package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public abstract class Horario implements HorarioBase {

	private LocalDateTime fechaEgreso;
	private LocalDateTime fechaIngreso;
	private List<JornadaLaboral> jornadasPlanificadas;
	private String reglasHorario;

	public Horario(){

	}

	
	public abstract List<JornadaLaboral> calcularJornadas();

	public boolean verificarCondicionesGenerales(){
		return false;
	}
}//end Horario