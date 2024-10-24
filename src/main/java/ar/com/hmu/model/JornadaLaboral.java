package ar.com.hmu.model;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class JornadaLaboral {

	private LocalDateTime fechaEgreso;
	private LocalDateTime fechaIngreso;

	public JornadaLaboral(){

	}

	
	public Duration calcularDuracion(){
		return null;
	}

	public boolean esValido(){
		return false;
	}

	/**
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 */
	public boolean validarDuracion(LocalDate fechaInicio, LocalDate fechaFin){
		return false;
	}
}//end JornadaLaboral