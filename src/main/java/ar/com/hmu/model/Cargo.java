package ar.com.hmu.model;


import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
 */
public class Cargo {

	private UUID id;
	private Agrupacion agrupacion;
	private String desc;
	private Integer numero;

	public Cargo(){

	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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