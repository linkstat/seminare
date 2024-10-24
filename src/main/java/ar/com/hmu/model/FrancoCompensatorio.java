package ar.com.hmu.model;


import java.time.LocalDateTime;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class FrancoCompensatorio {

	private JefaturaDeServicio autorizadaPor;
	private double cantHoras;
	private String desc;
	private Empleado empleado;
	private EstadoTramite estado;
	private LocalDateTime fechaAutorizacion;
	private LocalDateTime fechaDeAplicacion;
	public EstadoTramite m_EstadoTramite;
	public JefaturaDeServicio m_JefaturaDeServicio;
	public Empleado m_Empleado;

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