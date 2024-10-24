package ar.com.hmu.model;

import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class DiagramaDeServicio {

	private EstadoTramite estado;
	private LocalDateTime fechaFin;
	private LocalDateTime fechaInicio;
	private UUID id;
	private Map<Empleado, List<JornadaLaboral>> planificaciones;
	public JornadaLaboral m_JornadaLaboral;
	public Servicio m_Servicio;

	public DiagramaDeServicio(){

	}

	
	/**
	 * 
	 * @param empleado
	 * @param jornada
	 */
	public void addPlanificacion(Empleado empleado, JornadaLaboral jornada){

	}

	/**
	 * 
	 * @param empleado
	 */
	public List<JornadaLaboral> getPlanificacionEmpleado(Empleado empleado){
		return null;
	}

	public Map<Empleado, List<JornadaLaboral>> getPlanificacionServicio(){
		return null;
	}
}//end DiagramaDeServicio