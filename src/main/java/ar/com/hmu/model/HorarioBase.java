package ar.com.hmu.model;


import java.util.List;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
  */
public interface HorarioBase {

	public List<JornadaLaboral> calcularJornadas();

	public boolean verificarCondicionesGenerales();

}