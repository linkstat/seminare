package ar.com.hmu.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class RegistroJornadaLaboral {

	private Agente agente;
	private LocalDateTime fecha;
	private LocalDateTime fechaEgreso;
	private LocalDateTime fechaIngreso;
	private List<MarcacionAgente> marcacionesAgente;

	public RegistroJornadaLaboral(){

	}

	
	/**
	 * 
	 * @param marcacion
	 */
	public void agregarMarcacion(MarcacionAgente marcacion){

	}

	public Duration calcularDuracionJornada(){
		return null;
	}

	public double getHorasExtraDisp(){
		return 0;
	}

	public boolean tieneOmisiones(){
		return false;
	}

	public boolean tieneTardanza(){
		return false;
	}
}//end RegistroJornadaLaboral