package ar.com.hmu.model;


import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class ParteDiario extends Reporte {

	private List<Agente> agentes;
	private LocalDateTime fechaDeCierre;
	private Map<OficinaDePersonal, LocalDateTime> modificadoPor;
	private OficinaDePersonal oficinaDePersonal;
	private Agente agente;

	public ParteDiario(){

	}

	
	/**
	 * 
	 * @param agente
	 */
	public void agregarAgente(Agente agente){

	}

	public void enviarCapitalHumano(){

	}

	public void generarParte(){

	}

	public List<Novedad> obtenerNovedades(){
		return null;
	}
}//end ParteDiario