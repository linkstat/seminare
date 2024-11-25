package ar.com.hmu.model;


import java.time.LocalDateTime;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class FrancoCompensatorio {

	private Usuario autorizadaPor;
	private JefeDeServicio jefeDeServicio;
	private double cantHoras;
	private String desc;
	private Agente agente;
	private EstadoTramite estadoTramite;
	private LocalDateTime fechaAutorizacion;
	private LocalDateTime fechaDeAplicacion;

	public FrancoCompensatorio(){

	}

	
	/**
	 * 
	 * @param franco
	 */
	public void autorizar(FrancoCompensatorio franco){

	}

	public void descontarHorasExtra(){

	}

	public void getEstado(){

	}

	public void solicitarFranco(){

	}

	public boolean verificarHorasSuficientes(){
		return false;
	}
}//end FrancoCompensatorio