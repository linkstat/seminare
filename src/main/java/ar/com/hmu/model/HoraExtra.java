package ar.com.hmu.model;


import java.time.LocalDateTime;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class HoraExtra {

	private JefeDeServicio autorizadaPor;
	private JefeDeServicio jefeDeServicio;
	private EstadoTramite estadoAutorizacion;
	private EstadoTramite estadoTramite;
	private String desc;
	private Agente agente;
	private LocalDateTime fechaAutorizacion;
	private int fechaEgreso;
	private int fechaIngreso;
	private int ponderacion;

	public HoraExtra(){

	}

	
	/**
	 * 
	 * @param horaExtra
	 */
	public void autorizar(HoraExtra horaExtra){

	}

	public double calcularHorasDisponibles(){
		return 0;
	}

	public void solicitarAutorizacion(){

	}
}//end HoraExtra