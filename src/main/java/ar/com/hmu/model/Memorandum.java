package ar.com.hmu.model;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class Memorandum {

	private String asunto;
	private List<TipoAutorizacion> autorizacionesNecesarias;
	private List<Autorizacion> autorizacionesObtenidas;
	private String contenido;
	private List<Usuario> destinatarios;
	private EstadoTramite estado;
	private LocalDateTime fechaEnvio;
	private LocalDateTime fechaFirma;
	private LocalDateTime fechaRecepcion;
	private List<Usuario> firmante;
	private UUID id;
	public EstadoTramite m_EstadoTramite;
	public TipoAutorizacion m_TipoAutorizacion;
	public Autorizacion m_Autorizacion;
	public Usuario m_Usuario;

	public Memorandum(){

	}

	
	/**
	 * 
	 * @param usuario
	 */
	public void agregarDestinatario(Usuario usuario){

	}

	/**
	 * 
	 * @param autorizadoPor
	 */
	public void autorizar(Usuario autorizadoPor){

	}

	/**
	 * 
	 * @param usuario
	 */
	public void eliminarDestinatario(Usuario usuario){

	}

	public void enviar(){

	}

	/**
	 * 
	 * @param tipo
	 */
	public boolean esAutorizacionNecesaria(TipoAutorizacion tipo){
		return false;
	}

	/**
	 * 
	 * @param EstadoMemorandum
	 */
	public void obtenerEstado(int EstadoMemorandum){

	}

	/**
	 * 
	 * @param rechazadoPor
	 * @param razon
	 */
	public void rechazar(Usuario rechazadoPor, String razon){

	}

	/**
	 * 
	 * @param tipo
	 */
	public void solicitarAutorizacion(TipoAutorizacion tipo){

	}
}//end Memorandum