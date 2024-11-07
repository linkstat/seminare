package ar.com.hmu.model;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Jornada Laboral
 * Generar una buena descripción
 */
public class JornadaLaboral {

	private LocalDateTime fechaIngreso;
	private LocalDateTime fechaEgreso;
	private boolean isValid;

	public JornadaLaboral() {
	}

	public JornadaLaboral(LocalDateTime fechaIngreso, LocalDateTime fechaEgreso) {
		this.fechaIngreso = fechaIngreso;
		this.fechaEgreso = fechaEgreso;
	}


	// Setters

	public void setFechaIngreso(LocalDateTime fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public void setFechaEgreso(LocalDateTime fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}


	// Getters

	public LocalDateTime getFechaIngreso() {
		return fechaIngreso;
	}

	public LocalDateTime getFechaEgreso() {
		return fechaEgreso;
	}


	// Otros métodos

	/**
	 * Calcula la duración de una jornada laboral
	 * @return la duración de la jornada como un objeto Duration, o null si no se puede calcular.
	 */
	public Duration calcularDuracion(){
		if (fechaIngreso != null && fechaEgreso != null && !fechaEgreso.isBefore(fechaIngreso)) {
			return Duration.between(fechaIngreso, fechaEgreso);
		} else {
			return null; // Retorna null si no se puede calcular una duración válida.
		}
	}


	/**
	 * Verifica si la jornada laboral es válida.
	 * Una jornada es válida si tanto la fecha de ingreso como la fecha de egreso están definidas
	 * y la fecha de egreso es posterior a la fecha de ingreso.
	 *
	 * @return true si la jornada es válida, false de lo contrario.
	 */
	public boolean isValid() {
		if (fechaIngreso != null && fechaEgreso != null && fechaEgreso.isAfter(fechaIngreso)) {
			this.isValid = true;
		} else {
			this.isValid = false;
		}
		return isValid;
	}

	/**
	 * Valida la duración de la jornada en función de un tiempo mínimo.
	 *
	 * @param duracion la duración mínima requerida para considerar la jornada como válida.
	 * @return true si la duración de la jornada es mayor o igual a la duración proporcionada, false de lo contrario.
	 */
	public boolean validarDuracion(Duration duracion) {
		Duration jornadaDuracion = calcularDuracion();
		if (jornadaDuracion != null && !jornadaDuracion.minus(duracion).isNegative()) {
			this.isValid = true;
			return true;
		} else {
			this.isValid = false;
			return false;
		}
	}

}
