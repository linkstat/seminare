package ar.com.hmu.model;


import ar.com.hmu.utils.PasswordUtils;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Pablo Alejandro Hamann <linkstat@hmu.com.ar>
 * @version 1.0
  */
public abstract class Usuario {

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

	// Getters y Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public long getCuil() {
		return cuil;
	}

	public void setCuil(long cuil) {
		this.cuil = cuil;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public int getTel() {
		return tel;
	}

	public void setTel(int tel) {
		this.tel = tel;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
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

	// Setter para la contraseña (encriptada con BCrypt)
	/**
	 * Establece la contraseña del usuario cifrándola con BCrypt antes de almacenarla.
	 *
	 * @param rawPassword La contraseña en texto plano proporcionada por el usuario.
	 */
	public void setPassword(String rawPassword) {
		this.password = PasswordUtils.hashPassword(rawPassword);
	}

	// Método para validar la contraseña sin exponerla
	/**
	 * Valida si la contraseña proporcionada coincide con la contraseña cifrada almacenada.
	 *
	 * @param rawPassword Contraseña en texto plano proporcionada por el usuario.
	 * @return true si la contraseña coincide, false de lo contrario.
	 */
	public boolean validatePassword(String rawPassword) {
		return PasswordUtils.validatePassword(rawPassword, this.password);
	}

	/**
	 * Verifica si la contraseña actual del usuario es la predeterminada.
	 * La contraseña predeterminada es la que coincide con el número de CUIL del usuario cifrado con BCrypt.
	 *
	 * @return true si la contraseña es la predeterminada, false en caso contrario.
	 */
	public boolean isDefaultPassword() {
		// Convertimos el CUIL a cadena de caracteres para comprobar
		String defaultPassword = String.valueOf(this.cuil);
		return PasswordUtils.validatePassword(defaultPassword, this.password);
	}

	/**
	 * 
	 * @param servicio
	 */
	public void asignarAServicio(Servicio servicio){
		//void
	}

	public void consultarHorario(){

	}

	/**
	 * 
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