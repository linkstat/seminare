package ar.com.hmu.model;


import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class ParteDiario extends Reporte {

	private List<Empleado> empleados;
	private LocalDateTime fechaDeCierre;
	private Map<OficinaDePersonal, LocalDateTime> modificadoPor;
	public OficinaDePersonal m_OficinaDePersonal;
	public Empleado m_Empleado;

	public ParteDiario(){

	}

	
	/**
	 * 
	 * @param empleado
	 */
	public void agregarEmpleado(Empleado empleado){

	}

	public void enviarCapitalHumano(){

	}

	public void generarParte(){

	}

	public List<Novedad> obtenerNovedades(){
		return null;
	}
}//end ParteDiario