package ar.com.hmu.model;

import java.time.LocalDateTime;

import ar.com.hmu.roles.impl.AgenteRoleImpl;
import ar.com.hmu.roles.impl.JefeDeServicioRoleImpl;

public class FrancoCompensatorio {

	private Usuario autorizadaPor; // Usuario con rol de JefeDeServicio
	private Usuario jefeDeServicio; // Usuario con rol de JefeDeServicio
	private double cantHoras;
	private String desc;
	private Usuario agente; // Usuario con rol de Agente
	private EstadoTramite estadoTramite;
	private LocalDateTime fechaAutorizacion;
	private LocalDateTime fechaDeAplicacion;

	public FrancoCompensatorio() {
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

	public void setAutorizadaPor(Usuario autorizadaPor) {
		if (autorizadaPor.hasRoleBehavior(JefeDeServicioRoleImpl.class)) {
			this.autorizadaPor = autorizadaPor;
		} else {
			throw new IllegalArgumentException("El usuario no tiene el rol de Jefe de Servicio.");
		}
	}

	public Usuario getAutorizadaPor() {
		return autorizadaPor;
	}

	public void setJefeDeServicio(Usuario jefeDeServicio) {
		if (jefeDeServicio.hasRoleBehavior(JefeDeServicioRoleImpl.class)) {
			this.jefeDeServicio = jefeDeServicio;
		} else {
			throw new IllegalArgumentException("El usuario no tiene el rol de Jefe de Servicio.");
		}
	}

	public Usuario getJefeDeServicio() {
		return jefeDeServicio;
	}

	// Resto de getters y setters...

	// Métodos actualizados
	public void autorizar() {
		// Implementar lógica de autorización
	}

	public void descontarHorasExtra() {
		// Implementar lógica para descontar horas extra
	}

	public EstadoTramite getEstado() {
		return estadoTramite;
	}

	public void solicitarFranco() {
		// Implementar lógica para solicitar franco compensatorio
	}

	public boolean verificarHorasSuficientes() {
		// Implementar lógica para verificar horas suficientes
		return false;
	}

}
