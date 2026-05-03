package ar.com.hmu.controller;

import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.model.Memorandum;
import ar.com.hmu.model.MemorandumDestinatario;
import ar.com.hmu.model.Usuario;
import ar.com.hmu.service.MemorandumService;
import ar.com.hmu.util.AlertUtils;
import ar.com.hmu.util.AppInfo;
import ar.com.hmu.util.MarkdownRenderer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador de la pantalla de redacción de memorándums.
 *
 * <p>Soporta dos modos:</p>
 * <ul>
 *   <li><b>Nuevo</b>: el controller se abre sin memo precargado y el remitente
 *       redacta desde cero.</li>
 *   <li><b>Editar borrador</b>: se le pasa un {@link Memorandum} en estado
 *       BORRADOR del propio remitente, y se precargan asunto, contenido y
 *       destinatarios para modificar antes de guardar/enviar.</li>
 * </ul>
 *
 * <p>El editor es un {@link TextArea} plano con toolbar que inserta sintaxis
 * Markdown sobre el cursor o la selección. El botón "Vista previa" abre un
 * popup modal con el render Markdown→HTML del contenido actual.</p>
 */
public class RedactarMemorandumController {

    private static final int CONTENIDO_MAX = 8000;
    private static final int ASUNTO_MAX = 255;

    @FXML private TextField asuntoTextField;
    @FXML private ComboBox<Usuario> destinatariosComboBox;
    @FXML private ListView<Usuario> destinatariosSeleccionadosList;
    @FXML private TextArea contenidoTextArea;
    @FXML private Button enviarButton;

    private MemorandumService memorandumService;
    private Usuario usuarioActual;
    /** Memo a editar (modo "editar borrador"); null en modo "nuevo". */
    private Memorandum memoEdicion;

    private final Set<UUID> destinatariosIds = new HashSet<>();

    public void setMemorandumService(MemorandumService s) { this.memorandumService = s; }
    public void setUsuarioActual(Usuario u) { this.usuarioActual = u; }
    public void setMemoEdicion(Memorandum m) { this.memoEdicion = m; }

    @FXML
    public void initialize() {
        StringConverter<Usuario> converter = new StringConverter<>() {
            @Override
            public String toString(Usuario u) {
                if (u == null) return "";
                return u.getApellidos() + ", " + u.getNombres();
            }
            @Override
            public Usuario fromString(String s) { return null; }
        };
        destinatariosComboBox.setConverter(converter);
        destinatariosSeleccionadosList.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Usuario u, boolean empty) {
                super.updateItem(u, empty);
                setText(empty || u == null ? "" : u.getApellidos() + ", " + u.getNombres());
            }
        });
    }

    /** Llamar después de set* para terminar de inicializar la pantalla. */
    public void postInitialize() {
        cargarDestinatariosValidos();

        if (memoEdicion != null) {
            // Modo editar: precargar datos del borrador.
            asuntoTextField.setText(memoEdicion.getAsunto() != null ? memoEdicion.getAsunto() : "");
            contenidoTextArea.setText(memoEdicion.getContenido() != null ? memoEdicion.getContenido() : "");
            for (MemorandumDestinatario d : memoEdicion.getDestinatarios()) {
                destinatariosComboBox.getItems().stream()
                        .filter(u -> u.getId().equals(d.getUsuarioId()))
                        .findFirst()
                        .ifPresent(this::agregarDestinatario);
            }
        }
    }

    // ============================================================
    // Destinatarios
    // ============================================================

    private void cargarDestinatariosValidos() {
        try {
            List<Usuario> validos = memorandumService.destinatariosValidosPara(usuarioActual);
            destinatariosComboBox.getItems().setAll(validos);
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al cargar destinatarios válidos: " + e.getMessage());
        }
    }

    @FXML
    private void onAgregarDestinatario() {
        Usuario seleccionado = destinatariosComboBox.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        agregarDestinatario(seleccionado);
        destinatariosComboBox.getSelectionModel().clearSelection();
    }

    private void agregarDestinatario(Usuario u) {
        if (destinatariosIds.add(u.getId())) {
            destinatariosSeleccionadosList.getItems().add(u);
        }
    }

    @FXML
    private void onQuitarDestinatario() {
        Usuario sel = destinatariosSeleccionadosList.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        destinatariosSeleccionadosList.getItems().remove(sel);
        destinatariosIds.remove(sel.getId());
    }

    // ============================================================
    // Toolbar Markdown
    // ============================================================

    @FXML private void onBold() { envolverSeleccion("**", "**", "negrita"); }
    @FXML private void onItalic() { envolverSeleccion("*", "*", "itálica"); }

    @FXML
    private void onHeading() {
        prefijarLineaActual("## ");
    }

    @FXML
    private void onListaNoOrdenada() {
        prefijarLineasSeleccionadas("- ");
    }

    @FXML
    private void onListaOrdenada() {
        prefijarLineasSeleccionadasNumerado();
    }

    @FXML
    private void onVistaPrevia() {
        String html = MarkdownRenderer.renderToHtmlDocument(contenidoTextArea.getText());
        WebView wv = new WebView();
        wv.getEngine().loadContent(html, "text/html");

        BorderPane root = new BorderPane(wv);
        root.setPrefSize(700, 500);

        Stage stage = new Stage();
        stage.setTitle("Vista previa :: " + AppInfo.PRG_LONG_TITLE);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(asuntoTextField.getScene().getWindow());
        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream(AppInfo.ICON_IMAGE)));
        } catch (Exception ignored) {}
        stage.showAndWait();
    }

    private void envolverSeleccion(String pre, String post, String defaultText) {
        IndexRange sel = contenidoTextArea.getSelection();
        String texto = contenidoTextArea.getText();
        int caret = contenidoTextArea.getCaretPosition();
        if (sel.getLength() > 0) {
            String seleccionado = texto.substring(sel.getStart(), sel.getEnd());
            String reemplazo = pre + seleccionado + post;
            contenidoTextArea.replaceText(sel, reemplazo);
            contenidoTextArea.positionCaret(sel.getStart() + reemplazo.length());
        } else {
            String inserto = pre + defaultText + post;
            contenidoTextArea.insertText(caret, inserto);
            // Selecciona el placeholder para que el usuario pueda escribir encima.
            contenidoTextArea.selectRange(caret + pre.length(), caret + pre.length() + defaultText.length());
        }
        contenidoTextArea.requestFocus();
    }

    private void prefijarLineaActual(String prefijo) {
        String texto = contenidoTextArea.getText();
        int caret = contenidoTextArea.getCaretPosition();
        int inicioLinea = texto.lastIndexOf('\n', caret - 1) + 1;
        contenidoTextArea.insertText(inicioLinea, prefijo);
        contenidoTextArea.positionCaret(caret + prefijo.length());
        contenidoTextArea.requestFocus();
    }

    private void prefijarLineasSeleccionadas(String prefijo) {
        IndexRange sel = contenidoTextArea.getSelection();
        if (sel.getLength() == 0) {
            prefijarLineaActual(prefijo);
            return;
        }
        String texto = contenidoTextArea.getText();
        int inicio = texto.lastIndexOf('\n', sel.getStart() - 1) + 1;
        int fin = sel.getEnd();
        String bloque = texto.substring(inicio, fin);
        String[] lineas = bloque.split("\n", -1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lineas.length; i++) {
            if (i > 0) sb.append("\n");
            sb.append(prefijo).append(lineas[i]);
        }
        contenidoTextArea.replaceText(inicio, fin, sb.toString());
        contenidoTextArea.requestFocus();
    }

    private void prefijarLineasSeleccionadasNumerado() {
        IndexRange sel = contenidoTextArea.getSelection();
        String texto = contenidoTextArea.getText();
        int caret = contenidoTextArea.getCaretPosition();

        int inicio, fin;
        if (sel.getLength() == 0) {
            inicio = texto.lastIndexOf('\n', caret - 1) + 1;
            fin = caret;
        } else {
            inicio = texto.lastIndexOf('\n', sel.getStart() - 1) + 1;
            fin = sel.getEnd();
        }
        String bloque = texto.substring(inicio, fin);
        String[] lineas = bloque.split("\n", -1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lineas.length; i++) {
            if (i > 0) sb.append("\n");
            sb.append(i + 1).append(". ").append(lineas[i]);
        }
        contenidoTextArea.replaceText(inicio, fin, sb.toString());
        contenidoTextArea.requestFocus();
    }

    // ============================================================
    // Acciones de guardado / envío
    // ============================================================

    @FXML
    private void onGuardarBorrador() {
        Memorandum memo = construirMemoDesdeFormulario();
        if (memo == null) return;
        try {
            Memorandum guardado = persistirBorrador(memo);
            this.memoEdicion = guardado;
            AlertUtils.showInfo("Borrador guardado.");
        } catch (ServiceException e) {
            AlertUtils.showErr(e.getMessage());
        }
    }

    @FXML
    private void onEnviar() {
        Memorandum memo = construirMemoDesdeFormulario();
        if (memo == null) return;
        try {
            Memorandum guardado = persistirBorrador(memo);
            this.memoEdicion = guardado;
            memorandumService.enviar(guardado.getId(), usuarioActual);
            AlertUtils.showInfo("Memorándum enviado.");
            cerrar();
        } catch (ServiceException e) {
            AlertUtils.showErr(e.getMessage());
        }
    }

    /** Decide entre guardarBorrador (modo nuevo) o actualizarBorrador
     *  (modo edición) según si el memo ya existe. */
    private Memorandum persistirBorrador(Memorandum memo) throws ServiceException {
        if (memoEdicion != null && memoEdicion.getId() != null) {
            return memorandumService.actualizarBorrador(memo, usuarioActual);
        }
        return memorandumService.guardarBorrador(memo, usuarioActual);
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    // ============================================================
    // Helpers
    // ============================================================

    /** Lee los campos del formulario y arma un Memorandum válido o muestra
     *  un alert y devuelve null si la validación de UI falla. La validación
     *  de negocio profunda la hace el Service. */
    private Memorandum construirMemoDesdeFormulario() {
        String asunto = asuntoTextField.getText() != null ? asuntoTextField.getText().trim() : "";
        String contenido = contenidoTextArea.getText() != null ? contenidoTextArea.getText().trim() : "";

        if (asunto.isEmpty()) {
            AlertUtils.showWarn("Ingresá un asunto.");
            return null;
        }
        if (asunto.length() > ASUNTO_MAX) {
            AlertUtils.showWarn("El asunto excede el máximo de " + ASUNTO_MAX + " caracteres.");
            return null;
        }
        if (contenido.isEmpty()) {
            AlertUtils.showWarn("Ingresá el contenido del memorándum.");
            return null;
        }
        if (contenido.length() > CONTENIDO_MAX) {
            AlertUtils.showWarn("El contenido excede el máximo de " + CONTENIDO_MAX + " caracteres.");
            return null;
        }
        if (destinatariosSeleccionadosList.getItems().isEmpty()) {
            AlertUtils.showWarn("Agregá al menos un destinatario.");
            return null;
        }

        Memorandum m = new Memorandum();
        if (memoEdicion != null) {
            m.setId(memoEdicion.getId()); // preserva id del borrador en edición
        }
        m.setAsunto(asunto);
        m.setContenido(contenido);
        List<MemorandumDestinatario> dests = new ArrayList<>();
        for (Usuario u : destinatariosSeleccionadosList.getItems()) {
            dests.add(new MemorandumDestinatario(null, u.getId()));
        }
        m.setDestinatarios(dests);
        return m;
    }

    private void cerrar() {
        Stage stage = (Stage) asuntoTextField.getScene().getWindow();
        stage.close();
    }
}
