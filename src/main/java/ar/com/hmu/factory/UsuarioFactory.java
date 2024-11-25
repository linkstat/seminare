package ar.com.hmu.factory;
import ar.com.hmu.constants.TipoUsuario;
import ar.com.hmu.model.*;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Clase {@code UsuarioFactory} que se encarga de crear instancias de las subclases de {@link Usuario}.
 * <p>
 * Esta clase sigue el <i>Patrón <b>Factory</b></i> para instanciar el objeto adecuado del tipo {@link Usuario}
 * dependiendo de la información proporcionada en un {@link ResultSet}. Facilita la lógica de creación
 * de diferentes tipos de usuarios basados en el tipo especificado en la base de datos.
 * <p>
 * Es utilizada principalmente para convertir los datos almacenados en la base de datos en objetos del
 * modelo que puedan ser manipulados por la aplicación.
 * </p>
 */public class UsuarioFactory {

    /**
     * Crea una instancia de {@link Usuario} basada en la información proporcionada en un {@link ResultSet}.
     * <p>
     * Este método lee la columna {@code tipoUsuario} del {@code ResultSet} para determinar el tipo específico
     * de usuario que se debe instanciar (como {@code Agente}, {@code JefeDeServicio}, etc.).
     * A continuación, asigna los valores comunes de {@code Usuario} desde el {@code ResultSet} a la instancia creada.
     *
     * @param resultSet el {@link ResultSet} que contiene la información del usuario desde la base de datos.
     *                  Se espera que esté posicionado en una fila válida.
     * @return una instancia específica de {@link Usuario} con los valores asignados desde el {@code ResultSet}.
     * @throws SQLException si ocurre un error al acceder al {@code ResultSet}.
     * @throws IllegalArgumentException si el {@code tipoUsuario} no coincide con un tipo conocido.
     */
    public static Usuario createUsuario(ResultSet resultSet) throws SQLException {
        String tipoUsuario = resultSet.getString("tipoUsuario");
        Usuario usuario;

        switch (tipoUsuario) {
            case "AGENTE":
                usuario = new Agente();
                break;
            case "JEFEDESERVICIO":
                usuario = new JefeDeServicio();
                break;
            case "OFICINADEPERSONAL":
                usuario = new OficinaDePersonal();
                break;
            case "DIRECCION":
                usuario = new Direccion();
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuario desconocido: " + tipoUsuario);
        }

        // Asignar el ID (UUID) del usuario directamente desde la columna convertida con BIN_TO_UUID
        usuario.setId(UUID.fromString(resultSet.getString("id")));

        // Asignar otros campos
        usuario.setFechaAlta(resultSet.getDate("fechaAlta").toLocalDate());
        usuario.setEstado(resultSet.getBoolean("estado"));
        usuario.setCuil(resultSet.getLong("cuil"));
        usuario.setApellidos(resultSet.getString("apellidos"));
        usuario.setNombres(resultSet.getString("nombres"));
        usuario.setSexo(Sexo.valueOf(resultSet.getString("sexo").toUpperCase()));
        usuario.setMail(resultSet.getString("mail"));
        usuario.setTel(resultSet.getLong("tel"));

        try {
            usuario.setTipoUsuario(TipoUsuario.fromInternalName(resultSet.getString("tipoUsuario")));
        } catch (IllegalArgumentException | NullPointerException e) {
            usuario.setTipoUsuario(TipoUsuario.AGENTE);
            throw new SQLException("TipoUsuario inválido: " + resultSet.getString("tipoUsuario"), e);
        }

        // Cargar el hash de la contraseña almacenado
        String passwordHash = resultSet.getString("passwd");
        if (passwordHash != null && !passwordHash.isEmpty()) {
            usuario.setPasswordHash(passwordHash);
        }
        // Obtener la imagen de perfil (si existe)
        Blob profileImageBlob = resultSet.getBlob("profile_image");
        if (profileImageBlob != null) {
            int blobLength = (int) profileImageBlob.length();
            usuario.setProfileImage(profileImageBlob.getBytes(1, blobLength));
        }

        // Asignar los IDs de las relaciones (sin cargar las entidades)
        String domicilioIdStr = resultSet.getString("domicilioID");
        if (domicilioIdStr != null) {
            usuario.setDomicilioId(UUID.fromString(domicilioIdStr));
        }

        String cargoIdStr = resultSet.getString("cargoID");
        if (cargoIdStr != null) {
            usuario.setCargoId(UUID.fromString(cargoIdStr));
        }

        String servicioIdStr = resultSet.getString("servicioID");
        if (servicioIdStr != null) {
            usuario.setServicioId(UUID.fromString(servicioIdStr));
        }

        return usuario;
    }

}
