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
	private HorarioBase horarioBase;
	private FrancoCompensatorio francoCompensatorio;
	private JefaturaDeServicio jefaturaDeServicio;
	private OficinaDePersonal oficinaDePersonal;

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

}