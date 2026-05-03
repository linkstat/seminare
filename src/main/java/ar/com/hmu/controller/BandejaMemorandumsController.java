package ar.com.hmu.controller;

import ar.com.hmu.constants.TipoUsuario;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.model.EstadoTramite;
import ar.com.hmu.model.Memorandum;
import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.EstadoTramiteRepository;
import ar.com.hmu.repository.UsuarioRepository;
import ar.com.hmu.service.MemorandumService;
import ar.com.hmu.util.AlertUtils;
import ar.com.hmu.util.AppInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador de la pantalla principal de memorándums (bandejas y acciones).
 *
 * <p>Tres pestañas: Recibidos / Enviados / Pendientes de autorizar.
 * Doble click en una fila abre el detalle. La pestaña "Pendientes" se
 * oculta para usuarios sin rol de jefatura/OP/dirección.</p>
 */
public class BandejaMemorandumsController {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML private TabPane tabPane;
    @FXML private Tab tabRecibidos;
    @FXML private Tab tabEnviados;
    @FXML private Tab tabPendientes;

    @FXML private Label resumenLabel;
    @FXML private Button nuevoMemoButton;

    @FXML private TableView<Memorandum> recibidosTable;
    @FXML private TableColumn<Memorandum, String> recibidosFechaCol;
    @FXML private TableColumn<Memorandum, String> recibidosAsuntoCol;
    @FXML private TableColumn<Memorandum, String> recibidosRemitenteCol;
    @FXML private TableColumn<Memorandum, String> recibidosEstadoCol;

    @FXML private TableView<Memorandum> enviadosTable;
    @FXML private TableColumn<Memorandum, String> enviadosFechaCol;
    @FXML private TableColumn<Memorandum, String> enviadosAsuntoCol;
    @FXML private TableColumn<Memorandum, String> enviadosEstadoCol;

    @FXML private TableView<Memorandum> pendientesTable;
    @FXML private TableColumn<Memorandum, String> pendientesFechaCol;
    @FXML private TableColumn<Memorandum, String> pendientesAsuntoCol;
    @FXML private TableColumn<Memorandum, String> pendientesRemitenteCol;

    private MemorandumService memorandumService;
    private UsuarioRepository usuarioRepository;
    private EstadoTramiteRepository estadoTramiteRepository;
    private Usuario usuarioActual;

    /** Cache de nombres de usuario por UUID para no martillar la BD. */
    private final Map<UUID, String> nombresUsuarioCache = new HashMap<>();

    public void setMemorandumService(MemorandumService memorandumService) {
        this.memorandumService = memorandumService;
    }

    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void setEstadoTramiteRepository(EstadoTramiteRepository estadoTramiteRepository) {
        this.estadoTramiteRepository = estadoTramiteRepository;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarDoubleClickAbreDetalle(recibidosTable);
        configurarDoubleClickAbreDetalle(enviadosTable);
        configurarDoubleClickAbreDetalle(pendientesTable);
    }

    /** Llamar tras setear todas las dependencias. Carga las bandejas y
     *  ajusta visibilidad según rol. */
    public void postInitialize() {
        boolean puedeAutorizar = usuarioActual != null && usuarioActual.hasRole(
                TipoUsuario.JEFATURADESERVICIO,
                TipoUsuario.OFICINADEPERSONAL,
                TipoUsuario.DIRECCION);
        if (!puedeAutorizar) {
            tabPane.getTabs().remove(tabPendientes);
        }
        cargarBandejas();
    }

    @FXML
    private void onRefrescar() {
        nombresUsuarioCache.clear();
        cargarBandejas();
    }

    @FXML
    private void onNuevoMemo() {
        // La pantalla de redacción se implementa en el siguiente commit;
        // por ahora avisamos sin abrir nada.
        AlertUtils.showInfo("Redacción de memorándums: próximo paso del módulo.");
    }

    // ============================================================
    // Carga de bandejas
    // ============================================================

    private void cargarBandejas() {
        try {
            List<Memorandum> recibidos = memorandumService.bandejaEntrada(usuarioActual);
            List<Memorandum> enviados = memorandumService.bandejaSalida(usuarioActual);
            recibidosTable.getItems().setAll(recibidos);
            enviadosTable.getItems().setAll(enviados);

            if (tabPane.getTabs().contains(tabPendientes)) {
                List<Memorandum> pendientes = memorandumService.pendientesDeAutorizar(usuarioActual);
                pendientesTable.getItems().setAll(pendientes);
            }

            int noLeidos = memorandumService.contarNoLeidos(usuarioActual);
            resumenLabel.setText(noLeidos > 0 ? "(" + noLeidos + " sin leer)" : "");
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al cargar las bandejas: " + e.getMessage());
        }
    }

    private void configurarColumnas() {
        recibidosFechaCol.setCellValueFactory(c ->
                new SimpleStringProperty(formatearFecha(c.getValue().getFechaEnvio())));
        recibidosAsuntoCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getAsunto()));
        recibidosRemitenteCol.setCellValueFactory(c ->
                new SimpleStringProperty(nombrePorId(c.getValue().getRemitenteId())));
        recibidosEstadoCol.setCellValueFactory(c ->
                new SimpleStringProperty(estadoLegible(c.getValue())));

        enviadosFechaCol.setCellValueFactory(c ->
                new SimpleStringProperty(formatearFecha(c.getValue().getFechaEnvio())));
        enviadosAsuntoCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getAsunto()));
        enviadosEstadoCol.setCellValueFactory(c ->
                new SimpleStringProperty(estadoLegible(c.getValue())));

        pendientesFechaCol.setCellValueFactory(c ->
                new SimpleStringProperty(formatearFecha(c.getValue().getFechaEnvio())));
        pendientesAsuntoCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getAsunto()));
        pendientesRemitenteCol.setCellValueFactory(c ->
                new SimpleStringProperty(nombrePorId(c.getValue().getRemitenteId())));
    }

    private void configurarDoubleClickAbreDetalle(TableView<Memorandum> tabla) {
        tabla.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Memorandum> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    abrirDetalle(row.getItem());
                }
            });
            return row;
        });
    }

    private void abrirDetalle(Memorandum memo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detalleMemorandum.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == DetalleMemorandumController.class) {
                    DetalleMemorandumController c = new DetalleMemorandumController();
                    c.setMemorandumService(memorandumService);
                    c.setUsuarioRepository(usuarioRepository);
                    c.setEstadoTramiteRepository(estadoTramiteRepository);
                    c.setUsuarioActual(usuarioActual);
                    c.setMemorandum(memo);
                    return c;
                }
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Parent root = loader.load();
            DetalleMemorandumController controller = loader.getController();
            controller.postInitialize();

            Stage stage = new Stage();
            stage.setTitle("Memorándum :: " + AppInfo.PRG_LONG_TITLE);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream(AppInfo.ICON_IMAGE)));
            stage.showAndWait();

            // Al cerrar el detalle, refrescamos por si hubo cambios
            // (marcado leído, autorización resuelta, etc.).
            cargarBandejas();
        } catch (IOException e) {
            AlertUtils.showErr("Error al abrir el detalle del memorándum: " + e.getMessage());
        }
    }

    // ============================================================
    // Helpers
    // ============================================================

    private String formatearFecha(java.time.LocalDateTime f) {
        return f != null ? f.format(FORMATO_FECHA) : "(borrador)";
    }

    private String nombrePorId(UUID userId) {
        if (userId == null) return "";
        String cached = nombresUsuarioCache.get(userId);
        if (cached != null) return cached;
        try {
            Usuario u = usuarioRepository.readByUUID(userId);
            String nombre = u != null ? (u.getApellidos() + ", " + u.getNombres()) : "(desconocido)";
            nombresUsuarioCache.put(userId, nombre);
            return nombre;
        } catch (SQLException e) {
            return "(error)";
        }
    }

    private String estadoLegible(Memorandum memo) {
        try {
            EstadoTramite e = estadoTramiteRepository.getEstadoTramite(memo.getEstadoTramiteId());
            return e != null ? e.toDbName() : "";
        } catch (SQLException ex) {
            return "";
        }
    }
}
