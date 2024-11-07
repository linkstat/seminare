package ar.com.hmu.model;

import ar.com.hmu.utils.PasswordUtils;
import ar.com.hmu.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann
 * @version 1.0
 */
public abstract class Usuario {

	public static final String NOMBRE_SERVICIO_DIRECCION = "Dirección";
	public static final String NOMBRE_SERVICIO_PERSONAL = "Personal";
	private UUID id;
	private LocalDate fechaAlta;
	private long cuil;
	private String apellidos;
	private String nombres;
	private Sexo sexo;
	private boolean estado;
	private String mail;
	private Domicilio domicilio;
	private int tel;
	private Cargo cargo;
	private String password;
	private byte[] profileImage;
	private Servicio servicio;

	// Constructor por defecto
	public Usuario(){
		//void
	}

	// Constructor con los datos principales
	public Usuario(UUID id, LocalDate fechaAlta, long cuil, String apellidos, String nombres, Sexo sexo, boolean estado, String mail, Domicilio domicilio, int tel, Cargo cargo) {
		this.id = id;
		this.fechaAlta = fechaAlta;
		this.cuil = cuil;
		this.apellidos = apellidos;
		this.nombres = nombres;
		this.sexo = sexo;
		this.estado = estado;
		this.mail = mail;
		this.domicilio = domicilio;
		this.tel = tel;
		this.cargo = cargo;
	}

	// Getters

	public UUID getId() {
		return id;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public long getCuil() {
		return cuil;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getNombres() {
		return nombres;
	}

	public String getNombreCompleto() {
		return nombres + " " + apellidos;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public String getMail() {
		return mail;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public int getTel() {
		return tel;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	// Método para obtener la contraseña cifrada (solo para uso interno de la base de datos)
	/**
	 * Este método permite al UsuarioRepository acceder a la contraseña ya cifrada
	 * y usarla para persistir en la base de datos.
	 * Este método no expone la contraseña en texto plano, sino la versión hashada
	 * que ya está lista para ser almacenada.
	 * @return EncryptedPassword
	 */
	public String getEncryptedPassword() {
		return this.password;
	}

	/**
	 * Obtiene el servicio al que pertenece el usuario.
	 *
	 * @return el servicio asociado al usuario.
	 */
	public Servicio getServicio() {
		return servicio;
	}

	public boolean isEstado() {
		return estado;
	}

	/**
	 * Verifica si la contraseña actual del usuario es la predeterminada.
	 * La contraseña predeterminada es la que coincide con el número de CUIL del usuario cifrado con BCrypt.
	 *
	 * @return true si la contraseña es la predeterminada, false en caso contrario.
	 */
	public boolean isDefaultPassword() {
		// Convertimos el CUIL a cadena de caracteres para comprobar
		char[] defaultPasswordArray = String.valueOf(this.cuil).toCharArray();
		try {
			// Validar la contraseña usando el método actualizado de PasswordUtils
			return PasswordUtils.validatePassword(defaultPasswordArray, this.password);
		} finally {
			// Limpiar la memoria del `char[]` después de usarlo
			Arrays.fill(defaultPasswordArray, '\0');
		}
	}

	// Método para validar la contraseña sin exponerla
	/**
	 * Valida si la contraseña proporcionada coincide con la contraseña cifrada almacenada.
	 *
	 * @param rawPasswordArray Contraseña en texto plano proporcionada por el usuario.
	 * @return true si la contraseña coincide, false de lo contrario.
	 */
	public boolean validatePassword(char[] rawPasswordArray) {
		return PasswordUtils.validatePassword(rawPasswordArray, this.password);
	}

	// Setters

	public void setId(UUID id) {
		this.id = id;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public void setCuil(long cuil) {
		this.cuil = cuil;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public void setTel(int tel) {
		this.tel = tel;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	// Método para establecer la contraseña desde la base de datos
	public void setPasswordHash(String passwordHash) {
		this.password = passwordHash;
	}

	// Setter para la contraseña (encriptada con BCrypt)
	/**
	 * Establece la contraseña del usuario cifrándola con BCrypt antes de almacenarla.
	 *
	 * @param rawPasswordArray La contraseña en texto plano proporcionada por el usuario.
	 */
	public void setPassword(char[] rawPasswordArray) {
		this.password = PasswordUtils.hashPassword(rawPasswordArray);
	}

	public boolean setDefaultPassword() {
		// Convertir CUIL a cadena de caracteres
		char[] defaultPasswordArray = String.valueOf(this.cuil).toCharArray();
		try {
			// Establecer la contraseña por defecto cifrada (solo actualiza el objeto Usuario)
			setPassword(defaultPasswordArray);
			return true; // Si el cambio es exitoso, retorna true
		} finally {
			// Limpiar los array con la contraseña por defecto para que no permanezca en memoria
			Arrays.fill(defaultPasswordArray, '\0');
		}
	}


	/**
	 * Cambia la contraseña del usuario, asegurando que se validen las credenciales actuales y que las nuevas contraseñas coincidan.
	 * <p>
	 * Este método se encarga de validar la contraseña actual, verificar que la nueva contraseña coincida con la confirmación,
	 * y posteriormente actualizar la contraseña almacenada con su versión hasheada. Se implementa la limpieza de datos sensibles
	 * como los valores en texto plano de las contraseñas, inmediatamente después de ser utilizados, para reducir el riesgo de
	 * exposición de datos.
	 *
	 * @param currentPassword     La contraseña actual del usuario en formato de arreglo de caracteres (char[]).
	 * @param newPassword         La nueva contraseña propuesta por el usuario en formato de arreglo de caracteres (char[]).
	 * @param confirmNewPassword  Confirmación de la nueva contraseña, ingresada nuevamente por el usuario en formato de arreglo de caracteres (char[]).
	 * @return {@code true} si la contraseña se cambió exitosamente, de lo contrario lanza una excepción si alguna validación falla.
	 * @throws IllegalArgumentException Si la contraseña actual no es correcta o si las nuevas contraseñas no coinciden.
	 *
	 * <p><b>Nota:</b> Los arreglos de caracteres se limpian inmediatamente después de ser utilizados para evitar que los datos
	 * en texto plano queden en la memoria.
	 */
	public boolean changePassword(char[] currentPassword, char[] newPassword, char[] confirmNewPassword) {
		try {
			// Paso 1: Validar la contraseña actual
			if (!validatePassword(currentPassword)) {
				throw new IllegalArgumentException("La contraseña actual no es correcta.");
			}

			// Paso 2: Validar que la nueva contraseña coincide con la confirmación
			if (!Arrays.equals(newPassword, confirmNewPassword)) {
				throw new IllegalArgumentException("Las nuevas contraseñas no coinciden.");
			}

			// Paso 3: Establecer la nueva contraseña cifrada (solo actualiza el objeto Usuario)
			setPassword(newPassword);

			return true; // Si el cambio es exitoso, retorna true
		} finally {
			// Paso 4: Limpiar los arrays de contraseñas para evitar que permanezcan en memoria
			Arrays.fill(currentPassword, '\0');
			Arrays.fill(newPassword, '\0');
			Arrays.fill(confirmNewPassword, '\0');
		}
	}

	/**
	 * Establece el servicio al que pertenece el usuario.
	 *
	 * @param servicio el servicio a asociar con el usuario.
	 */
	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public void consultarHorario(){

	}

	/**
	 * Genera un tipo de Reporte
	 * @param reporte
	 */
	public void generarReporte(int reporte){
		//void
	}

	public void registrarHorasExtra(){
		//void
	}

	public void registrarNovedad(){
		//void
	}

	public void solicitarFC(){
		//void
	}

}