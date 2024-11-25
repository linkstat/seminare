package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class Novedad {

	private int cod;
	private String desc;
	private EstadoTramite estadoTramite;
	private LocalDateTime estadoFechaModif;
	private LocalDateTime fechaFin;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaSolicitud;
	private UUID id;
	private boolean reqAprobDireccion;
	private Agente agente;

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