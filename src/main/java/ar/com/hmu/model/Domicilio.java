package ar.com.hmu.model;

import java.util.UUID;

/**
 * Representa un domicilio con varios atributos como calle, numeración, barrio, ciudad, localidad y provincia.
 * Implementa el patrón de diseño Builder para permitir la creación de objetos con parámetros opcionales.
 */
public class Domicilio {
	private UUID id;
	private String calle;
	private String numeracion;
	private String barrio;
	private String ciudad;
	private String localidad;
	private String provincia;

	/**
	 * Constructor privado que inicializa un objeto Domicilio con los valores proporcionados por el Builder.
	 *
	 * @param builder la instancia de Builder que contiene los datos para inicializar el objeto Domicilio.
	 */
	private Domicilio(Builder builder) {
		this.id = builder.id;
		this.calle = builder.calle;
		this.numeracion = builder.numeracion;
		this.barrio = builder.barrio;
		this.ciudad = builder.ciudad;
		this.localidad = builder.localidad;
		this.provincia = builder.provincia;
	}

	/**
	 * Builder estático que facilita la construcción de un objeto Domicilio.
	 * Este patrón permite la creación de objetos Domicilio con solo los atributos necesarios, manteniendo
	 * la legibilidad y flexibilidad en el código.
	 */
	public static class Builder {
		private UUID id;
		private String calle;
		private String numeracion;
		private String barrio;
		private String ciudad;
		private String localidad;
		private String provincia;

		/**
		 * Establece el UUID del domicilio.
		 *
		 * @param id el UUID del domicilio.
		 * @return la instancia del Builder para permitir la concatenación de métodos.
		 */
		public Builder setId(UUID id) {
			this.id = id;
			return this;
		}

		/**
		 * Establece la calle del domicilio.
		 *
		 * @param calle el nombre de la calle.
		 * @return la instancia del Builder para permitir la concatenación de métodos.
		 */
		public Builder setCalle(String calle) {
			this.calle = calle;
			return this;
		}

		/**
		 * Establece la numeración del domicilio.
		 *
		 * @param numeracion el número del domicilio.
		 * @return la instancia del Builder para permitir la concatenación de métodos.
		 */
		public Builder setNumeracion(String numeracion) {
			this.numeracion = numeracion;
			return this;
		}

		/**
		 * Establece el barrio del domicilio.
		 *
		 * @param barrio el nombre del barrio.
		 * @return la instancia del Builder para permitir la concatenación de métodos.
		 */
		public Builder setBarrio(String barrio) {
			this.barrio = barrio;
			return this;
		}

		/**
		 * Establece la ciudad del domicilio.
		 *
		 * @param ciudad el nombre de la ciudad.
		 * @return la instancia del Builder para permitir la concatenación de métodos.
		 */
		public Builder setCiudad(String ciudad) {
			this.ciudad = ciudad;
			return this;
		}

		/**
		 * Establece la localidad del domicilio.
		 *
		 * @param localidad el nombre de la localidad.
		 * @return la instancia del Builder para permitir la concatenación de métodos.
		 */
		public Builder setLocalidad(String localidad) {
			this.localidad = localidad;
			return this;
		}

		/**
		 * Establece la provincia del domicilio.
		 *
		 * @param provincia el nombre de la provincia.
		 * @return la instancia del Builder para permitir la concatenación de métodos.
		 */
		public Builder setProvincia(String provincia) {
			this.provincia = provincia;
			return this;
		}

		/**
		 * Construye y devuelve un objeto Domicilio con los valores proporcionados al Builder.
		 *
		 * @return un nuevo objeto Domicilio.
		 */
		public Domicilio build() {
			return new Domicilio(this);
		}

	}


	// Setters

	public void setId(UUID id) {
		this.id = id;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public void setNumeracion(String numeracion) {
		this.numeracion = numeracion;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}


	// Getters

	public UUID getId() {
		return id;
	}

	public String getCalle() {
		return calle;
	}

	public String getNumeracion() {
		return numeracion;
	}

	public String getBarrio() {
		return barrio;
	}

	public String getCiudad() {
		return ciudad;
	}

	public String getLocalidad() {
		return localidad;
	}

	public String getProvincia() {
		return provincia;
	}
}
