package ar.com.hmu.model;


import java.time.LocalDateTime;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class HoraExtra {

	private JefaturaDeServicio autorizadaPor;
	private String desc;
	private Empleado empleado;
	private EstadoTramite estadoAutorizacion;
	private LocalDateTime fechaAutorizacion;
	private int fechaEgreso;
	private int fechaIngreso;
	private int ponderacion;
	public Empleado m_Empleado;
	public JefaturaDeServicio m_JefaturaDeServicio;
	public EstadoTramite m_EstadoTramite;

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