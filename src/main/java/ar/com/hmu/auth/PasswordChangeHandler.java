package ar.com.hmu.auth;

import ar.com.hmu.model.Usuario;
import ar.com.hmu.utils.AlertUtils;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;

import java.util.Arrays;

/**
 * Clase encargada de gestionar el cambio de contraseñas de los usuarios.
 * <p>
 * Esta clase proporciona una interfaz para solicitar al usuario su contraseña actual,
 * la nueva contraseña, y su confirmación. Si el cambio es exitoso, la contraseña se actualiza
 * en el objeto {@link Usuario}. Si el usuario cancela el proceso, se puede implementar una lógica
 * para cerrar la sesión u obligarlo a cambiar la contraseña más adelante.
 */
public class PasswordChangeHandler {

    /**
     * Muestra un diálogo para cambiar la contraseña del usuario.
     * <p>
     * Este método presenta una ventana emergente con los campos necesarios para cambiar la contraseña.
     * Si el cambio es exitoso, la contraseña se actualiza en el objeto {@link Usuario}.
     *
     * @param usuario El usuario autenticado que necesita cambiar su contraseña.
     * @param onSuccess Runnable que se ejecutará si el cambio de contraseña es exitoso.
     * @param onCancel Runnable que se ejecutará si el usuario cancela el cambio de contraseña.
     */
    public void showChangePasswordDialog(Usuario usuario, Runnable onSuccess, Runnable onCancel) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Cambiar Contraseña");
        dialog.setHeaderText("Cambio de contraseña.");

        // Campos de contraseña
        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Contraseña actual");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Nueva contraseña");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Repetir nueva contraseña");

        // Añadir campos a la ventana
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(new VBox(10, currentPasswordField, newPasswordField, confirmPasswordField));

        // Procesar el resultado del diálogo
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Convertir las contraseñas a char[]
                char[] currentPassword = currentPasswordField.getText().toCharArray();
                char[] newPassword = newPasswordField.getText().toCharArray();
                char[] confirmPassword = confirmPasswordField.getText().toCharArray();

                try {
                    // Intentar cambiar la contraseña usando el método changePassword en Usuario
                    usuario.changePassword(currentPassword, newPassword, confirmPassword);
                    AlertUtils.showInfo("Contraseña cambiada exitosamente.");

                    // Llamar al callback onSuccess para proceder con la lógica después del cambio exitoso
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } catch (IllegalArgumentException e) {
                    AlertUtils.showErr(e.getMessage());
                    // Reabrir el diálogo si hay un error
                    showChangePasswordDialog(usuario, onSuccess, onCancel);
                } finally {
                    // Limpiar las contraseñas del arreglo para mayor seguridad
                    Arrays.fill(currentPassword, '\0');
                    Arrays.fill(newPassword, '\0');
                    Arrays.fill(confirmPassword, '\0');
                }
            } else {
                // Llamar al callback onCancel si el usuario cancela el diálogo
                if (onCancel != null) {
                    onCancel.run();
                }
            }
        });
    }
}
