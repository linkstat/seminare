package ar.com.hmu.model;


import java.util.List;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class JefaturaDeServicio extends Usuario {

	private List<Empleado> listaEmpleados;
	private Servicio servicio;
	public Direccion m_Direccion;
	public Servicio m_Servicio;

	public JefaturaDeServicio(){

	}

	
	/**
	 * 
	 * @param franco
	 */
	public void autorizarFrancoCompensatorio(FrancoCompensatorio franco){

	}

	/**
	 * 
	 * @param horaExtra
	 */
	public void autorizarHorasExtras(HoraExtra horaExtra){

	}

	public void consultarDisponibilidad(){

	}

	public void generarDiagramaDeServicio(){

	}

	/**
	 * 
	 * @param reporte
	 */
	public void generarReporte(int reporte){

	}
}//end JefaturaDeServicio