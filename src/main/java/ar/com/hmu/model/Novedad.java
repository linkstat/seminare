package ar.com.hmu.model;

import java.time.LocalDateTime;
import java.util.UUID;

import ar.com.hmu.roles.impl.AgenteRoleImpl;

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
	private Usuario agente; // Usuario con rol de Agente

	public Novedad() {
	}

	// Getters y Setters con verificación de roles

	public void setAgente(Usuario agente) {
		if (agente.hasRoleBehavior(AgenteRoleImpl.class)) {
			this.agente = agente;
		} else {
			throw new IllegalArgumentException("El usuario no tiene el rol de Agente.");
		}
	}

	public Usuario getAgente() {
		return agente;
	}

	// Métodos actualizados

	public void aprobar() {
		// Implementar lógica para aprobar la novedad
	}

	public void procesar() {
		// Implementar lógica para procesar la novedad
	}

	public void rechazar() {
		// Implementar lógica para rechazar la novedad
	}

	public void registrarNovedad() {
		// Implementar lógica para registrar la novedad
	}

}
