package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public abstract class Horario implements HorarioBase {

	private LocalDateTime fechaEgreso;
	private LocalDateTime fechaIngreso;
	private List<JornadaLaboral> jornadasPlanificadas;
	private String reglasHorario;

	public Horario(){

	}


	// Getters / Setters

	public LocalDateTime getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(LocalDateTime fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	public LocalDateTime getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDateTime fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public List<JornadaLaboral> getJornadasPlanificadas() {
		return jornadasPlanificadas;
	}

	public void setJornadasPlanificadas(List<JornadaLaboral> jornadasPlanificadas) {
		this.jornadasPlanificadas = jornadasPlanificadas;
	}

	public String getReglasHorario() {
		return reglasHorario;
	}

	public void setReglasHorario(String reglasHorario) {
		this.reglasHorario = reglasHorario;
	}


	public abstract List<JornadaLaboral> calcularJornadas();

	public boolean verificarCondicionesGenerales(){
		return false;
	}
}//end Horario