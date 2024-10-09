package ar.com.hmu.model;


import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public abstract class Usuario {

	private String apellidos;
	private Cargo cargo;
	private int cuil;
	private Domicilio domicilio;
	private boolean estado;
	private LocalDate fechaAlta;
	private UUID id;
	private String mail;
	private String nombres;
	private Sexo sexo;
	private int tel;
	public Cargo m_Cargo;
	public Domicilio m_Domicilio;
	public Sexo m_Sexo;

	public Usuario(){

	}

	
	/**
	 * 
	 * @param servicio
	 */
	public void asignarAServicio(Servicio servicio){

	}

	public void cerrarSesion(){

	}

	public void consultarHorario(){

	}

	/**
	 * 
	 * @param reporte
	 */
	public void generarReporte(int reporte){

	}

	/**
	 * 
	 * @param credenciales
	 */
	public boolean iniciarSesion(int credenciales){
		return false;
	}

	public void registrarHorasExtra(){

	}

	public void registrarNovedad(){

	}

	/**
	 * 
	 * @param sexo
	 */
	public void setSexo(Sexo sexo){

	}

	public void solicitarFC(){

	}
}//end Usuario