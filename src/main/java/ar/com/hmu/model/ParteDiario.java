package ar.com.hmu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import ar.com.hmu.roles.impl.EmpleadoRoleImpl;
import ar.com.hmu.roles.impl.OficinaDePersonalRoleImpl;

public class ParteDiario extends Reporte {

	private List<Usuario> empleados; // Lista de usuarios con rol de Empleado
	private LocalDateTime fechaDeCierre;
	private Map<Usuario, LocalDateTime> modificadoPor; // Map de usuarios con rol de OficinaDePersonal
	private Usuario oficinaDePersonal; // Usuario con rol de OficinaDePersonal
	private Usuario empleado; // Usuario con rol de Empleado

	public ParteDiario() {
		this.empleados = new ArrayList<>();
		this.modificadoPor = new HashMap<>();
	}

	// Métodos actualizados

	public void agregarEmpleado(Usuario empleado) {
		if (empleado.hasRoleBehavior(EmpleadoRoleImpl.class)) {
			this.empleados.add(empleado);
		} else {
			throw new IllegalArgumentException("El usuario no tiene el rol de Empleado.");
		}
	}

	public void enviarCapitalHumano() {
		// Implementar lógica para enviar a capital humano
	}

	public void generarParte() {
		// Implementar lógica para generar el parte diario
	}

	public List<Novedad> obtenerNovedades() {
		// Implementar lógica para obtener novedades
		return null;
	}

}
