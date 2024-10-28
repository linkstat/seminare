package ar.com.hmu.model;


import java.util.List;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class Direccion extends Usuario {

	private List<JefaturaDeServicio> listaJefaturasDeServicio;
	private Novedad novedad;

	// Constructor vacío o con atributos propios, ya no accede a la base de datos.
	public Direccion() {
		super();
	}

	@Override
	public void setServicio(Servicio servicio) {
		throw new UnsupportedOperationException("No se puede cambiar el servicio para los usuarios de tipo Dirección.");
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