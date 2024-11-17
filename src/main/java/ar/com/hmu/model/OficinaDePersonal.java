package ar.com.hmu.model;


import java.util.List;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class OficinaDePersonal extends Usuario {

	private List<Empleado> listaEmpleados;
	private int reportesGenerados;
	private Novedad novedad;

	// Constructor vacío o con atributos propios, ya no accede a la base de datos.
	public OficinaDePersonal() {
		super();
	}

	/**
	 * 
	 * @param reporte
	 */
	public void generarReporte(int reporte){

	}

	/**
	 * 
	 * @param empleado
	 */
	public void gestionarABMEmpleado(Empleado empleado){

	}

	/**
	 * 
	 * @param franco
	 */
	public void validarFrancoCompensatorio(FrancoCompensatorio franco){

	}

	/**
	 * 
	 * @param novedad
	 */
	public void validarNovedad(Novedad novedad){

	}

	/**
	 * 
	 * @param horasExtra
	 */
	public void valirdarHorasExtra(HoraExtra horasExtra){

	}
}//end OficinaDePersonal