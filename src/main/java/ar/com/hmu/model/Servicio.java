package ar.com.hmu.model;


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
	private List<Empleado> empleados;
	private List<DiagramaDeServicio> diagramas;

	public Servicio() {
	}

	public Servicio(UUID id, String nombre, Agrupacion agrupacion) {
		this.id = id;
		this.nombre = nombre;
		this.agrupacion = agrupacion;
	}

	public Servicio(UUID id, String nombre, Agrupacion agrupacion, List<Empleado> empleados, List<DiagramaDeServicio> diagramas) {
		this.id = id;
		this.nombre = nombre;
		this.agrupacion = agrupacion;
		this.empleados = empleados;
		this.diagramas = diagramas;
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

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
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


	/**
	 * 
	 * Método para agregar empleado a un servicio
	 */
	public void addEmpleado(Empleado empleado){

	}

	/**
	 * 
	 * @param empleado
	 */
	public void delEmpleado(Empleado empleado){

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

	public List<Empleado> getEmpleados(){
		return null;
	}

	/**
	 * 
	 * @param diagrama
	 */
	public void setDiagrama(DiagramaDeServicio diagrama){

	}

}
