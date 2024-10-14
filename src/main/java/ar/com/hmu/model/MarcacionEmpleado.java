package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public class MarcacionEmpleado {

	private Empleado empleado;
	private LocalDateTime fechaMarcacion;
	private UUID id;
	private String observaciones;
	private TipoMarcacion tipoMarcacion;
	private boolean validada;
	public Empleado m_Empleado;
	public TipoMarcacion m_TipoMarcacion;

	public MarcacionEmpleado(){

	}

	
	public boolean esEgreso(){
		return false;
	}

	public boolean esIngreso(){
		return false;
	}

	public void validarMarcacion(){

	}
}//end MarcacionEmpleado