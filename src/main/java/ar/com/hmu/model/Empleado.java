package ar.com.hmu.model;


import java.util.List;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public class Empleado extends Usuario {

	private List<FrancoCompensatorio> francosCompensatoriosUtilizados;
	private HorarioBase horarioActual;
	private List<HoraExtra> horasExtra;
	private List<Novedad> novedad;
	private Servicio servicio;
	public HorarioBase m_HorarioBase;
	public Servicio m_Servicio;
	public FrancoCompensatorio m_FrancoCompensatorio;
	public JefaturaDeServicio m_JefaturaDeServicio;
	public OficinaDePersonal m_OficinaDePersonal;

	public Empleado(){

	}

	
	public double calcularTotalHorasExtra(){
		return 0;
	}

	/**
	 * 
	 * @param francoCompensatorio
	 */
	public void generarFrancoCompensatorio(FrancoCompensatorio francoCompensatorio){

	}

	/**
	 * 
	 * @param reporte
	 */
	public void generarReporte(Reporte reporte){

	}

	public List<FrancoCompensatorio> getFrancosCompensatoriosUtilizados(){
		return null;
	}

	public HorarioBase getHorario(){
		return null;
	}

	public List<HoraExtra> getHorasExtra(){
		return null;
	}

	public Servicio getServicio(){
		return null;
	}

	/**
	 * 
	 * @param horaExtra
	 */
	public void registrarHoraExtra(HoraExtra horaExtra){

	}

	/**
	 * 
	 * @param novedad
	 */
	public void regitrarNovedad(Novedad novedad){

	}

	/**
	 * 
	 * @param nuevoHorario
	 */
	public void setHorario(HorarioBase nuevoHorario){

	}

	/**
	 * 
	 * @param servicio
	 */
	public void setServicio(Servicio servicio){

	}
}//end Empleado