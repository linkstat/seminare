package ar.com.hmu.controller;

import ar.com.hmu.model.Usuario;
import ar.com.hmu.service.*;
import ar.com.hmu.util.ImageUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    // Servicios
    private UsuarioService usuarioService;
    private CargoService cargoService;
    private ServicioService servicioService;
    private DomicilioService domicilioService;
    private RoleService roleService;

    Usuario usuario;

    @FXML
    private ImageView imagenPerfilImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image testImage = new Image(getClass().getResourceAsStream("/images/businessman.png"));
            imagenPerfilImageView.setImage(testImage);
            System.out.println("Imagen de prueba cargada en el ImageView. Size: " + testImage.getWidth() + " x " + testImage.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar la imagen de prueba: " + e.getMessage());
        }



    }

    public void setServices(UsuarioService usuarioService, CargoService cargoService, ServicioService servicioService, DomicilioService domicilioService, RoleService roleService) {
        this.usuarioService = usuarioService;
        this.cargoService = cargoService;
        this.servicioService = servicioService;
        this.domicilioService = domicilioService;
        this.roleService = roleService;
    }

    public static void cargarImagenParametrizada(Usuario usuario, ImageView imagenPerfilImageView, Image defaultImage) {
        // Cargar imagen de perfil
        byte[] profileImageBytes = usuario.getProfileImage();
        ImageUtils.setProfileImage(imagenPerfilImageView, profileImageBytes, defaultImage);
    }
}
