package ar.com.hmu.model;


import ar.com.hmu.roles.impl.AgenteRoleImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Clase para representar Servicios (esto es: áreas, oficinas)
 *
 */
public class Servicio {

	private UUID id;
	private String nombre;
	private Agrupacion agrupacion;
	private Usuario direccion;

	private UUID direccionId;

	private List<Usuario> agentes;
	private List<DiagramaDeServicio> diagramas;

	public Servicio() {
	}

	public Servicio(UUID id, String nombre, Agrupacion agrupacion) {
		this.id = id;
		this.nombre = nombre;
		this.agrupacion = agrupacion;
		this.direccion = null;
	}

	public Servicio(UUID id, String nombre, Agrupacion agrupacion, UUID direccionId) {
		this.id = id;
		this.nombre = nombre;
		this.agrupacion = agrupacion;
		this.direccionId = direccionId;

	}

	public Servicio(UUID id, String nombre, Agrupacion agrupacion, List<Usuario> agentes, List<DiagramaDeServicio> diagramas) {
		this.id = id;
		this.nombre = nombre;
		this.agrupacion = agrupacion;
		this.direccionId = null;
		this.agentes = agentes;
		this.diagramas = diagramas;
	}

	@Override
	public String toString() {
		return this.getNombre();
	}

	// Setters

	public void setId(UUID id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setAgrupacion(Agrupacion agrupacion) {
		this.agrupacion = agrupacion;
	}

	public void setDireccion(Usuario direccion) {
		this.direccion = direccion;
	}

	public void setDireccionId(UUID direccionId) {
		this.direccionId = direccionId;
	}

	public void setAgentes(List<Usuario> agentes) {
		this.agentes = agentes;
	}

	public void setDiagramas(List<DiagramaDeServicio> diagramas) {
		this.diagramas = diagramas;
	}

	// Getters

	public UUID getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public Agrupacion getAgrupacion() {
		return agrupacion;
	}

	public List<DiagramaDeServicio> getDiagramas() {
		return diagramas;
	}

	public Usuario getDireccion() {
		return direccion;
	}

	public UUID getDireccionId() {
		return direccionId;
	}

	public List<Usuario> getAgentes(){
		return null;
	}


	/**
	 * 
	 * Método para agregar agentes a un servicio
	 */
	public void addAgente(Usuario agente){
		if (agente.hasRoleBehavior(AgenteRoleImpl.class)) {
			this.agentes.add(agente);
		} else {
			throw new IllegalArgumentException("El usuario no tiene el rol de Agente.");
		}
	}

	/**
	 * 
	 * @param agente
	 */
	public void delAgente(Usuario agente){
		this.agentes.remove(agente);
	}

	/**
	 * 
	 * @param fechaActual
	 */
	public DiagramaDeServicio getDiagramaActual(LocalDateTime fechaActual){
		return null;
	}

	/**
	 * 
	 * @param fechaEspecifica
	 */
	public DiagramaDeServicio getDiagramaFechaEspecifica(LocalDateTime fechaEspecifica){
		return null;
	}

	/**
	 * 
	 * @param diagrama
	 */
	public void setDiagrama(DiagramaDeServicio diagrama){

	}

}
