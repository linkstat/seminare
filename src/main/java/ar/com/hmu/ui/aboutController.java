package ar.com.hmu.ui;

import ar.com.hmu.utils.AppInfo;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.w3c.dom.*;
import org.w3c.dom.events.*;
import java.awt.Desktop;
import java.net.URI;

public class aboutController {
    @FXML
    private ImageView logoImageView;

    @FXML
    private Label versionLabel;

    @FXML
    private Label buildLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private WebView productInfoWebView;

    @FXML
    private Text titleText;

    @FXML
    private Text subtitleText;

    @FXML
    private Text descriptionText;

    @FXML
    public void initialize() {
        // Establecer los valores de las etiquetas utilizando las constantes
        versionLabel.setText("Versión: " + AppInfo.VERSION);
        buildLabel.setText("Build: " + AppInfo.BUILD);
        dateLabel.setText("Fecha: " + AppInfo.PRODUCTION_DATE);
        // Establecer el productInfoWebView del Acerca de (y el comportamiento de links)
        WebEngine webEngine = productInfoWebView.getEngine();  // Obtiene el WebEngine
        webEngine.loadContent(AppInfo.PRODUCT_INFO);  // Carga el contenido HTML
        webEngine.setJavaScriptEnabled(true);  // Utilizo JavaScript para deshabilitar el menú contextual dentro del WebView
        // Agregar un listener para cuando el contenido haya cargado completamente
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // El contenido se ha cargado completamente
                Document doc = webEngine.getDocument();
                NodeList nodeList = doc.getElementsByTagName("a");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    EventTarget eventTarget = (EventTarget) node;
                    eventTarget.addEventListener("click", new EventListener() {
                        @Override
                        public void handleEvent(Event evt) {
                            evt.preventDefault(); // Prevenir la navegación en el WebView
                            String href = ((Element) evt.getCurrentTarget()).getAttribute("href");
                            try {
                                // Abrir el enlace en el navegador predeterminado
                                Desktop.getDesktop().browse(new URI(href));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, false);
                }
            }
        });

        // Los siguientes valores los establecimos por defecto en el fxml, pero luego vinculamos los elementos gráficos para que puedan ser seteados desde el controlador
        titleText.setText("Aromito");
        titleText.setFont(Font.font("pristina", 72));
        subtitleText.setText("Sistema de Gestión de Ausentismo Hospitalario");
        subtitleText.setFont(Font.font("Barlow Condensed", 26));
        descriptionText.setText("Solución especializada para la Administración de novedades");
        descriptionText.setFont(Font.font("Barlow Condensed", 19));
        // Opcionalmente, también se podría setear la imagen del logo (pero usamos la que se estableció por defecto en el fxml)
        // logoImageView.setImage(new Image(getClass().getResourceAsStream("@mosaic_default_image.png")));
    }

}
