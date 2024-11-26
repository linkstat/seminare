package ar.com.hmu.model;

import java.time.LocalDateTime;

import ar.com.hmu.roles.impl.AgenteRoleImpl;
import ar.com.hmu.roles.impl.JefeDeServicioRoleImpl;

public class HoraExtra {

	private Usuario autorizadaPor; // Usuario con rol de JefeDeServicio
	private Usuario jefeDeServicio; // Usuario con rol de JefeDeServicio
	private EstadoTramite estadoAutorizacion;
	private EstadoTramite estadoTramite;
	private String desc;
	private Usuario agente; // Usuario con rol de Agente
	private LocalDateTime fechaAutorizacion;
	private int fechaEgreso;
	private int fechaIngreso;
	private int ponderacion;

	public HoraExtra(){

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
		if (this.autorizadaPor == null) {
			throw new IllegalStateException("No se ha establecido el usuario que autoriza.");
		}
		// Cambiar estadoAutorizacion, establecer fechaAutorizacion, etc.
	}

	public double calcularHorasDisponibles() {
		// Implementar lógica para calcular horas disponibles
		return 0;
	}

	public void solicitarAutorizacion() {
		// Implementar lógica para solicitar autorización
	}

}
