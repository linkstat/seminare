package ar.com.hmu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import ar.com.hmu.roles.impl.AgenteRoleImpl;
import ar.com.hmu.roles.impl.OficinaDePersonalRoleImpl;

public class ParteDiario extends Reporte {

	private List<Usuario> agentes; // Lista de usuarios con rol de Agente
	private LocalDateTime fechaDeCierre;
	private Map<Usuario, LocalDateTime> modificadoPor; // Map de usuarios con rol de OficinaDePersonal
	private Usuario oficinaDePersonal; // Usuario con rol de OficinaDePersonal
	private Usuario agente; // Usuario con rol de Agente

	public ParteDiario() {
		this.agentes = new ArrayList<>();
		this.modificadoPor = new HashMap<>();
	}

	// Métodos actualizados

	public void agregarAgente(Usuario agente) {
		if (agente.hasRoleBehavior(AgenteRoleImpl.class)) {
			this.agentes.add(agente);
		} else {
			throw new IllegalArgumentException("El usuario no tiene el rol de Agente.");
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
