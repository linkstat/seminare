package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class Novedad {

	private int cod;
	private String desc;
	private EstadoTramite estado;
	private LocalDateTime estadoFechaModif;
	private LocalDateTime fechaFin;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaSolicitud;
	private UUID id;
	private boolean reqAprobDireccion;
	public EstadoTramite m_EstadoTramite;
	public Empleado m_Empleado;

	public Novedad(){

	}

	
	public void aprobar(){

	}

	public void procesar(){

	}

	public void rechazar(){

	}

	public void registrarNovedad(){

	}
}//end Novedad