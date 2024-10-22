package ar.com.hmu.model;


import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public abstract class Usuario {

	private UUID id;
	private LocalDate fechaAlta;
	private long cuil;
	private String apellidos;
	private String nombres;
	private Sexo sexo;
	private boolean estado;
	private String mail;
	private Domicilio domicilio;
	private int tel;
	private Cargo cargo;
	private String password;

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