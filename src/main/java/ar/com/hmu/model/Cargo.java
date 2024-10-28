package ar.com.hmu.model;


import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
 */
public class Cargo {

	private UUID id;
	private Agrupacion agrupacion;
	private String descripcion;
	private Integer numero;

	public Cargo(UUID id, int numero, String descripcion, Agrupacion agrupacion) {
		this.id = id;
		this.numero = numero;
		this.descripcion = descripcion;
		this.agrupacion = agrupacion;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Agrupacion getAgrupacion() {
		return agrupacion;
	}

	public void setAgrupacion(Agrupacion agrupacion) {
		this.agrupacion = agrupacion;
	}

	/**
	 * 
	 * @param cargo
	 */
	public void setCargo(String cargo){

	}
}//end Cargo