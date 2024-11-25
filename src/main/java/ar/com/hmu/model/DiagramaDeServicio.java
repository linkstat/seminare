package ar.com.hmu.model;

import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * Diagrama de Servicio
 * Representa una planificación horaria de un servicio
  */
public class DiagramaDeServicio {

	private UUID id;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private JornadaLaboral jornadaLaboral;
	private Map<Agente, List<JornadaLaboral>> planificaciones;
	private Servicio servicio;
	private EstadoTramite estado;

	public DiagramaDeServicio(UUID id, LocalDateTime fechaInicio, LocalDateTime fechaFin, JornadaLaboral jornadaLaboral, Map<Agente, List<JornadaLaboral>> planificaciones, Servicio servicio, EstadoTramite estado) {
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.jornadaLaboral = jornadaLaboral;
		this.planificaciones = planificaciones;
		this.servicio = servicio;
		this.estado = estado;
	}

	// Setters

	public void setId(UUID id) {
		this.id = id;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setJornadaLaboral(JornadaLaboral jornadaLaboral) {
		this.jornadaLaboral = jornadaLaboral;
	}

	public void setPlanificaciones(Map<Agente, List<JornadaLaboral>> planificaciones) {
		this.planificaciones = planificaciones;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public void setEstado(EstadoTramite estado) {
		this.estado = estado;
	}


	// Getters

	public UUID getId() {
		return id;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public JornadaLaboral getJornadaLaboral() {
		return jornadaLaboral;
	}

	public Map<Agente, List<JornadaLaboral>> getPlanificaciones() {
		return planificaciones;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public EstadoTramite getEstado() {
		return estado;
	}

	// Otros métodos

	public void addPlanificacion(Agente agente, JornadaLaboral jornada){

	}

	public List<JornadaLaboral> getPlanificacionAgente(Agente agente){
		return null;
	}

	public Map<Agente, List<JornadaLaboral>> getPlanificacionServicio(){
		return null;
	}

}
