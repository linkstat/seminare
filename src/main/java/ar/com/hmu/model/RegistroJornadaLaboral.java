package ar.com.hmu.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.com.hmu.roles.impl.AgenteRoleImpl;

public class RegistroJornadaLaboral {

	private Usuario agente; // Usuario con rol de Agente
	private LocalDateTime fecha;
	private LocalDateTime fechaEgreso;
	private LocalDateTime fechaIngreso;
	private List<MarcacionAgente> marcacionesAgente;

	public RegistroJornadaLaboral() {
		this.marcacionesAgente = new ArrayList<>();
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

	public void agregarMarcacion(MarcacionAgente marcacion) {
		this.marcacionesAgente.add(marcacion);
	}

	public Duration calcularDuracionJornada() {
		// Implementar lógica para calcular la duración de la jornada
		return null;
	}

	public double getHorasExtraDisp() {
		// Implementar lógica para obtener horas extra disponibles
		return 0;
	}

	public boolean tieneOmisiones() {
		// Implementar lógica para verificar omisiones
		return false;
	}

	public boolean tieneTardanza() {
		// Implementar lógica para verificar tardanzas
		return false;
	}

}
