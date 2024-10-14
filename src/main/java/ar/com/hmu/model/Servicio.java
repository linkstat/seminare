package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class Servicio {

	private Agrupacion agrupacion;
	private List<DiagramaDeServicio> diagramas;
	private List<Empleado> empleados;
	private UUID id;
	private String nombre;
	public Agrupacion m_Agrupacion;

	public Servicio(){

	}

	
	/**
	 * 
	 * @param empleado
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
}//end Servicio