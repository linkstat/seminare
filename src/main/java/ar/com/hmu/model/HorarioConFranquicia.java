package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class HorarioConFranquicia implements HorarioBase {

	private LocalDateTime fechaEgreso;
	private LocalDateTime fechaIngreso;
	private HorarioBase horarioDecorado;
	private int horasFranquicia;

	public HorarioConFranquicia(){

	}

	
	public int calcularHorasMensuales(){
		return 0;
	}

	public List<JornadaLaboral> calcularJornadas(){
		return null;
	}

	public boolean verificarCondicionesGenerales(){
		return false;
	}
}//end HorarioConFranquicia