package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class MarcacionAgente {

	private Agente agente;
	private LocalDateTime fechaMarcacion;
	private UUID id;
	private String observaciones;
	private TipoMarcacion tipoMarcacion;
	private boolean validada;

	public MarcacionAgente(){

	}

	
	public boolean esEgreso(){
		return false;
	}

	public boolean esIngreso(){
		return false;
	}

	public void validarMarcacion(){

	}
}
