package ar.com.hmu.model;


import java.util.List;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class Direccion extends Usuario {

	private List<JefaturaDeServicio> listaJefaturasDeServicio;
	public Novedad m_Novedad;

	public Direccion(){

	}

	
	/**
	 * 
	 * @param diagramaDeServicio
	 */
	public void aprobarDiagramaDeServicio(DiagramaDeServicio diagramaDeServicio){

	}

	/**
	 * 
	 * @param novedad
	 */
	public void autorizarNovedad(Novedad novedad){

	}

	/**
	 * 
	 * @param usuario
	 * @param servicio
	 */
	public void designarUsuarioAServicio(Usuario usuario, Servicio servicio){

	}

	/**
	 * 
	 * @param usuario
	 */
	public void designarUsuarioAServicio(Usuario usuario){

	}

	/**
	 * 
	 * @param reporte
	 */
	public void generarReporte(int reporte){

	}

	/**
	 * 
	 * @param reporte
	 */
	public void generarReporteGlobal(int reporte){

	}

	/**
	 * 
	 * @param usuario
	 * @param servicio
	 */
	public void removerUsuarioDeServicio(Usuario usuario, Servicio servicio){

	}

	/**
	 * 
	 * @param usuario
	 */
	public void removerUsuarioDeServicio(Usuario usuario){

	}

	/**
	 * 
	 * @param usuario
	 * @param servicioOrigen
	 * @param servicioDestino
	 */
	public void trasladarUsuarioDeServicio(Usuario usuario, Servicio servicioOrigen, Servicio servicioDestino){

	}
}//end Direccion