package ar.com.hmu.model;

import java.time.LocalDateTime;
import java.util.UUID;

import ar.com.hmu.roles.impl.AgenteRoleImpl;

public class MarcacionAgente {

	private Usuario agente; // Usuario con rol de Agente
	private LocalDateTime fechaMarcacion;
	private UUID id;
	private String observaciones;
	private TipoMarcacion tipoMarcacion;
	private boolean validada;

	public MarcacionAgente() {
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

	public boolean esEgreso() {
		// Implementar lógica para determinar si es egreso
		return false;
	}

	public boolean esIngreso() {
		// Implementar lógica para determinar si es ingreso
		return false;
	}

	public void validarMarcacion() {
		// Implementar lógica para validar marcación
	}

}
