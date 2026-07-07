package ar.com.hmu.controller;

import ar.com.hmu.constants.TipoUsuario;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.model.DiagramaDeServicio;
import ar.com.hmu.model.EstadoDiagrama;
import ar.com.hmu.model.JornadaLaboral;
import ar.com.hmu.model.Servicio;
import ar.com.hmu.model.TipoJornada;
import ar.com.hmu.model.Usuario;
import ar.com.hmu.service.ServicioService;
import ar.com.hmu.service.diagramacion.DiagramaService;
import ar.com.hmu.service.diagramacion.Violacion;
import ar.com.hmu.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Controller de la pantalla de diagramación de servicio (RFS02).
 *
 * <p>Vista tabla empleado × día: una fila por empleado del servicio, una
 * columna por día del rango del diagrama. Cada celda muestra la jornada en
 * forma compacta (07–14 / G 08–20 / F / FC / LIC) coloreada por tipo, con
 * el detalle en tooltip. Doble click abre el editor de jornada (sólo en
 * BORRADOR/OBSERVADO y si el usuario puede gestionar el servicio).</p>
 *
 * <p>La vista calendario mensual (toggle) llega en el próximo pase de la
 * Fase 3.</p>
 *
 * <p>Los cambios se acumulan en memoria ({@code dirty}) y se persisten con
 * "Guardar" (reemplazo total de jornadas con control optimista). "Validar"
 * guarda si hace falta y muestra violaciones (✖ bloquean el envío) y
 * advertencias de carga mensual (⚠ no bloquean) en el panel lateral,
 * resaltando las celdas afectadas. ESC cierra respetando {@code dirty}.</p>
 */
public class DiagramacionServicioController {

    // --- Top ---
    @FXML private ComboBox<Servicio> servicioCombo;
    @FXML private ComboBox<DiagramaDeServicio> diagramaCombo;
    @FXML private Button nuevoDiagramaButton;
    @FXML private Label estadoChip;
    @FXML private HBox observacionBanner;
    @FXML private Label observacionLabel;

    // --- Center ---
    @FXML private TableView<Usuario> grillaTable;
    @FXML private ListView<String> validacionList;
    @FXML private Label validacionResumenLabel;

    // --- Bottom ---
    @FXML private Label ayudaLabel;
    @FXML private Button eliminarButton;
    @FXML private Button validarButton;
    @FXML private Button guardarButton;
    @FXML private Button enviarButton;

    // --- Dependencias (inyectadas por el caller) ---
    private DiagramaService diagramaService;
    private ServicioService servicioService;
    private Usuario usuarioActual;

    // --- Estado en memoria ---
    private DiagramaDeServicio diagramaActual;
    /** Jornadas indexadas por empleado y fecha (el modelo de la grilla). */
    private final Map<UUID, Map<LocalDate, JornadaLaboral>> jornadasPorEmpleado = new HashMap<>();
    /** Celdas a resaltar tras la última validación: clave "empleadoId|fecha". */
    private final Set<String> celdasConViolacion = new HashSet<>();
    private boolean dirty;

    private static final DateTimeFormatter FECHA_CORTA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA_MIN = DateTimeFormatter.ofPattern("HH:mm");

    // ============================================================
    // Inyección + inicialización
    // ============================================================

    public void setDiagramaService(DiagramaService diagramaService) {
        this.diagramaService = diagramaService;
    }

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    /** Llamar después de inyectar dependencias. */
    public void postInitialize() {
        configurarCombos();
        cargarServicios();
        configurarEscCierra();
        actualizarControles();
    }

    private void configurarCombos() {
        servicioCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Servicio s) {
                return s != null ? s.getNombre() : "";
            }
            @Override public Servicio fromString(String s) {
                return null;
            }
        });
        servicioCombo.valueProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null && confirmarDescarteSiDirty()) {
                cargarDiagramasDe(nuevo.getId());
            }
        });

        diagramaCombo.setConverter(new StringConverter<>() {
            @Override public String toString(DiagramaDeServicio d) {
                if (d == null) {
                    return "";
                }
                return d.getFechaInicio().format(FECHA_CORTA) + " – "
                        + d.getFechaFin().format(FECHA_CORTA) + "  [" + d.getEstado() + "]";
            }
            @Override public DiagramaDeServicio fromString(String s) {
                return null;
            }
        });
        diagramaCombo.valueProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null && confirmarDescarteSiDirty()) {
                cargarDiagrama(nuevo);
            }
        });

        grillaTable.setPlaceholder(new Label("Seleccioná o creá un diagrama para empezar."));
        grillaTable.getSelectionModel().setCellSelectionEnabled(true);
    }

    private void cargarServicios() {
        try {
            List<Servicio> servicios = servicioService.readAll();
            // readAll no ordena (orden físico de inserción): ordenamos acá.
            servicios.sort(java.util.Comparator.comparing(Servicio::getNombre,
                    String.CASE_INSENSITIVE_ORDER));
            if (esOpODireccion()) {
                servicioCombo.getItems().setAll(servicios);
            } else {
                // Jefatura: sólo su propio servicio, selector bloqueado.
                servicios.stream()
                        .filter(s -> s.getId().equals(usuarioActual.getServicioId()))
                        .findFirst()
                        .ifPresent(s -> servicioCombo.getItems().setAll(s));
                servicioCombo.setDisable(true);
            }
            if (!servicioCombo.getItems().isEmpty()) {
                servicioCombo.getSelectionModel().selectFirst();
            }
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al cargar los servicios: " + e.getMessage());
        }
    }

    private void configurarEscCierra() {
        grillaTable.sceneProperty().addListener((obs, vieja, nueva) -> {
            if (nueva != null) {
                nueva.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), this::onCerrar);
            }
        });
    }

    // ============================================================
    // Carga de diagramas y grilla
    // ============================================================

    private void cargarDiagramasDe(UUID servicioId) {
        try {
            List<DiagramaDeServicio> diagramas = diagramaService.diagramasDeServicio(servicioId);
            diagramaCombo.getItems().setAll(diagramas);
            if (!diagramas.isEmpty()) {
                diagramaCombo.getSelectionModel().selectFirst();
            } else {
                diagramaActual = null;
                limpiarGrilla();
                actualizarControles();
            }
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al cargar los diagramas del servicio: " + e.getMessage());
        }
    }

    private void cargarDiagrama(DiagramaDeServicio diagrama) {
        try {
            this.diagramaActual = diagrama;
            List<JornadaLaboral> jornadas = diagramaService.jornadasDe(diagrama.getId());

            jornadasPorEmpleado.clear();
            for (JornadaLaboral j : jornadas) {
                jornadasPorEmpleado
                        .computeIfAbsent(j.getEmpleadoId(), k -> new HashMap<>())
                        .put(j.getFecha(), j);
            }
            celdasConViolacion.clear();
            validacionList.getItems().clear();
            validacionResumenLabel.setText("");
            dirty = false;

            construirGrilla();
            actualizarControles();
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al cargar el diagrama: " + e.getMessage());
        }
    }

    private void limpiarGrilla() {
        grillaTable.getColumns().clear();
        grillaTable.getItems().clear();
        jornadasPorEmpleado.clear();
        celdasConViolacion.clear();
        validacionList.getItems().clear();
        validacionResumenLabel.setText("");
        dirty = false;
    }

    private void construirGrilla() {
        grillaTable.getColumns().clear();
        grillaTable.getItems().clear();
        if (diagramaActual == null) {
            return;
        }

        // Columna fija: empleado.
        TableColumn<Usuario, String> nombreCol = new TableColumn<>("Empleado");
        nombreCol.setCellValueFactory(cd -> new SimpleStringProperty(nombreDe(cd.getValue())));
        nombreCol.setPrefWidth(190);
        nombreCol.setSortable(false);
        nombreCol.setReorderable(false);
        grillaTable.getColumns().add(nombreCol);

        // Una columna por día del rango.
        for (LocalDate fecha = diagramaActual.getFechaInicio();
             !fecha.isAfter(diagramaActual.getFechaFin());
             fecha = fecha.plusDays(1)) {
            grillaTable.getColumns().add(columnaDelDia(fecha));
        }

        // Filas: los empleados del servicio.
        try {
            List<Usuario> empleados = servicioService.findUsuariosByServicio(diagramaActual.getServicioId());
            grillaTable.getItems().setAll(empleados);
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al cargar los empleados del servicio: " + e.getMessage());
        }
    }

    private TableColumn<Usuario, String> columnaDelDia(LocalDate fecha) {
        String encabezado = String.format("%02d%n%s", fecha.getDayOfMonth(), inicialDia(fecha));
        TableColumn<Usuario, String> col = new TableColumn<>(encabezado);
        col.setPrefWidth(52);
        col.setSortable(false);
        col.setReorderable(false);

        final LocalDate fechaCol = fecha;
        col.setCellValueFactory(cd ->
                new SimpleStringProperty(textoCelda(jornadaDe(cd.getValue().getId(), fechaCol))));
        col.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("celda-turno", "celda-guardia", "celda-guardia-pasiva",
                        "celda-franco", "celda-franco-comp", "celda-licencia", "celda-violacion");
                setTooltip(null);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }
                Usuario empleado = getTableRow().getItem();
                JornadaLaboral j = jornadaDe(empleado.getId(), fechaCol);
                setText(item);
                if (j != null) {
                    getStyleClass().add(estiloDe(j.getTipo()));
                    setTooltip(new Tooltip(tooltipDe(j)));
                }
                if (celdasConViolacion.contains(claveCelda(empleado.getId(), fechaCol))) {
                    getStyleClass().add("celda-violacion");
                }
                setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2 && esEditable()) {
                        editarCelda(empleado, fechaCol);
                    }
                });
            }
        });
        return col;
    }

    // ============================================================
    // Render de celdas
    // ============================================================

    private JornadaLaboral jornadaDe(UUID empleadoId, LocalDate fecha) {
        Map<LocalDate, JornadaLaboral> delEmpleado = jornadasPorEmpleado.get(empleadoId);
        return (delEmpleado != null) ? delEmpleado.get(fecha) : null;
    }

    private String textoCelda(JornadaLaboral j) {
        if (j == null || j.getTipo() == null) {
            return "";
        }
        return switch (j.getTipo()) {
            case TURNO_NORMAL -> rangoCorto(j);
            case GUARDIA_ACTIVA -> "G " + rangoCorto(j);
            case GUARDIA_PASIVA -> "GP " + rangoCorto(j);
            case FRANCO -> "F";
            case FRANCO_COMPENSATORIO -> "FC";
            case LICENCIA -> "LIC";
        };
    }

    private String rangoCorto(JornadaLaboral j) {
        if (!j.tieneHorario()) {
            return "¿?";
        }
        return horaCorta(j.getFechaIngreso()) + "–" + horaCorta(j.getFechaEgreso());
    }

    private String horaCorta(LocalDateTime t) {
        return (t.getMinute() == 0)
                ? String.format("%02d", t.getHour())
                : t.format(HORA_MIN);
    }

    private String estiloDe(TipoJornada tipo) {
        return switch (tipo) {
            case TURNO_NORMAL -> "celda-turno";
            case GUARDIA_ACTIVA -> "celda-guardia";
            case GUARDIA_PASIVA -> "celda-guardia-pasiva";
            case FRANCO -> "celda-franco";
            case FRANCO_COMPENSATORIO -> "celda-franco-comp";
            case LICENCIA -> "celda-licencia";
        };
    }

    private String tooltipDe(JornadaLaboral j) {
        StringBuilder sb = new StringBuilder(etiquetaDe(j.getTipo()));
        if (j.tieneHorario()) {
            sb.append("\n").append(j.getFechaIngreso().format(HORA_MIN))
                    .append(" → ").append(j.getFechaEgreso().format(HORA_MIN));
            if (!j.getFechaEgreso().toLocalDate().equals(j.getFecha())) {
                sb.append(" (día siguiente)");
            }
        }
        if (j.getObservaciones() != null && !j.getObservaciones().isBlank()) {
            sb.append("\n").append(j.getObservaciones());
        }
        return sb.toString();
    }

    private String etiquetaDe(TipoJornada tipo) {
        return switch (tipo) {
            case TURNO_NORMAL -> "Turno normal";
            case GUARDIA_ACTIVA -> "Guardia activa";
            case GUARDIA_PASIVA -> "Guardia pasiva";
            case FRANCO -> "Franco";
            case FRANCO_COMPENSATORIO -> "Franco compensatorio";
            case LICENCIA -> "Licencia";
        };
    }

    private String inicialDia(LocalDate fecha) {
        return switch (fecha.getDayOfWeek()) {
            case MONDAY -> "L";
            case TUESDAY -> "M";
            case WEDNESDAY -> "X";
            case THURSDAY -> "J";
            case FRIDAY -> "V";
            case SATURDAY -> "S";
            case SUNDAY -> "D";
        };
    }

    private String nombreDe(Usuario u) {
        String apellidos = u.getApellidos() != null ? u.getApellidos() : "";
        String nombres = u.getNombres() != null ? u.getNombres() : "";
        return (apellidos + ", " + nombres).trim();
    }

    private String claveCelda(UUID empleadoId, LocalDate fecha) {
        return empleadoId + "|" + fecha;
    }

    // ============================================================
    // Editor de jornada
    // ============================================================

    private void editarCelda(Usuario empleado, LocalDate fecha) {
        JornadaLaboral existente = jornadaDe(empleado.getId(), fecha);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar jornada");
        dialog.setHeaderText(nombreDe(empleado) + " — " + fecha.format(FECHA_CORTA)
                + " (" + inicialDia(fecha) + ")");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<TipoJornada> tipoCombo = new ComboBox<>();
        tipoCombo.getItems().setAll(TipoJornada.values());
        tipoCombo.setConverter(new StringConverter<>() {
            @Override public String toString(TipoJornada t) {
                return t != null ? etiquetaDe(t) : "";
            }
            @Override public TipoJornada fromString(String s) {
                return null;
            }
        });

        TextField ingresoField = new TextField();
        ingresoField.setPromptText("HH:mm");
        TextField egresoField = new TextField();
        egresoField.setPromptText("HH:mm");
        Label notaCruce = new Label("Si el egreso es anterior al ingreso, se toma como día siguiente.");
        notaCruce.getStyleClass().add("ayuda");
        TextField obsField = new TextField();
        obsField.setPromptText("Observaciones (opcional)");

        // Estado inicial.
        if (existente != null && existente.getTipo() != null) {
            tipoCombo.setValue(existente.getTipo());
            if (existente.tieneHorario()) {
                ingresoField.setText(existente.getFechaIngreso().format(HORA_MIN));
                egresoField.setText(existente.getFechaEgreso().format(HORA_MIN));
            }
            obsField.setText(existente.getObservaciones() != null ? existente.getObservaciones() : "");
        } else {
            tipoCombo.setValue(TipoJornada.FRANCO);
        }

        Runnable sincronizarHabilitacion = () -> {
            boolean conHorario = tipoCombo.getValue() != null && tipoCombo.getValue().requiereHorario();
            ingresoField.setDisable(!conHorario);
            egresoField.setDisable(!conHorario);
        };
        tipoCombo.valueProperty().addListener((obs, v, n) -> sincronizarHabilitacion.run());
        sincronizarHabilitacion.run();

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.addRow(0, new Label("Tipo:"), tipoCombo);
        grid.addRow(1, new Label("Ingreso:"), ingresoField);
        grid.addRow(2, new Label("Egreso:"), egresoField);
        grid.add(notaCruce, 1, 3);
        grid.addRow(4, new Label("Obs.:"), obsField);
        dialog.getDialogPane().setContent(grid);

        if (dialog.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        TipoJornada tipo = tipoCombo.getValue();
        LocalDateTime ingreso = null;
        LocalDateTime egreso = null;
        if (tipo.requiereHorario()) {
            try {
                LocalTime hIn = LocalTime.parse(ingresoField.getText().trim(), HORA_MIN);
                LocalTime hEg = LocalTime.parse(egresoField.getText().trim(), HORA_MIN);
                ingreso = fecha.atTime(hIn);
                // Egreso menor o igual al ingreso: la jornada cruza medianoche.
                egreso = !hEg.isAfter(hIn) ? fecha.plusDays(1).atTime(hEg) : fecha.atTime(hEg);
            } catch (DateTimeParseException e) {
                AlertUtils.showWarn("Horario inválido: usá el formato HH:mm (ej.: 07:00).");
                return;
            }
        }

        JornadaLaboral j = (existente != null) ? existente : new JornadaLaboral();
        if (j.getId() == null) {
            j.setId(UUID.randomUUID());
        }
        j.setDiagramaId(diagramaActual.getId());
        j.setEmpleadoId(empleado.getId());
        j.setFecha(fecha);
        j.setTipo(tipo);
        j.setFechaIngreso(ingreso);
        j.setFechaEgreso(egreso);
        String obs = obsField.getText();
        j.setObservaciones(obs != null && !obs.isBlank() ? obs.trim() : null);

        jornadasPorEmpleado.computeIfAbsent(empleado.getId(), k -> new HashMap<>()).put(fecha, j);
        dirty = true;
        grillaTable.refresh();
        actualizarControles();
    }

    // ============================================================
    // Acciones
    // ============================================================

    @FXML
    private void onNuevoDiagrama() {
        if (servicioCombo.getValue() == null) {
            AlertUtils.showWarn("Seleccioná un servicio primero.");
            return;
        }
        if (!confirmarDescarteSiDirty()) {
            return;
        }

        // Por defecto: el mes calendario siguiente completo.
        LocalDate primeroProximoMes = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo diagrama");
        dialog.setHeaderText("Nuevo diagrama para " + servicioCombo.getValue().getNombre());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        javafx.scene.control.DatePicker desdePicker =
                new javafx.scene.control.DatePicker(primeroProximoMes);
        javafx.scene.control.DatePicker hastaPicker =
                new javafx.scene.control.DatePicker(primeroProximoMes.withDayOfMonth(
                        primeroProximoMes.lengthOfMonth()));

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.addRow(0, new Label("Desde:"), desdePicker);
        grid.addRow(1, new Label("Hasta:"), hastaPicker);
        Label nota = new Label("La grilla inicial se genera desde el horario de cada empleado.");
        nota.getStyleClass().add("ayuda");
        grid.add(nota, 1, 2);
        dialog.getDialogPane().setContent(grid);

        if (dialog.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            DiagramaDeServicio nuevo = diagramaService.crearBorrador(
                    servicioCombo.getValue().getId(),
                    desdePicker.getValue(), hastaPicker.getValue(), usuarioActual);
            cargarDiagramasDe(servicioCombo.getValue().getId());
            diagramaCombo.getItems().stream()
                    .filter(d -> d.getId().equals(nuevo.getId()))
                    .findFirst()
                    .ifPresent(d -> diagramaCombo.getSelectionModel().select(d));
        } catch (ServiceException e) {
            AlertUtils.showErr("No se pudo crear el diagrama:\n" + e.getMessage());
        }
    }

    @FXML
    private void onGuardar() {
        if (diagramaActual == null || !dirty) {
            return;
        }
        try {
            diagramaService.guardarJornadas(diagramaActual, jornadasComoLista(), usuarioActual);
            dirty = false;
            actualizarControles();
        } catch (ServiceException e) {
            AlertUtils.showErr("No se pudo guardar el diagrama:\n" + e.getMessage());
        }
    }

    @FXML
    private void onValidar() {
        if (diagramaActual == null) {
            return;
        }
        // El validador lee de la BD: persistimos los cambios pendientes primero.
        if (dirty) {
            onGuardar();
            if (dirty) {
                return; // el guardado falló; el error ya se mostró
            }
        }
        try {
            List<Violacion> violaciones = diagramaService.validarJornadas(diagramaActual.getId());
            List<Violacion> advertencias = diagramaService.advertenciasCarga(diagramaActual.getId());

            celdasConViolacion.clear();
            validacionList.getItems().clear();
            for (Violacion v : violaciones) {
                validacionList.getItems().add("✖ " + v);
                if (v.fecha() != null) {
                    celdasConViolacion.add(claveCelda(v.empleadoId(), v.fecha()));
                }
            }
            for (Violacion a : advertencias) {
                validacionList.getItems().add("⚠ " + a.mensaje());
            }
            if (violaciones.isEmpty() && advertencias.isEmpty()) {
                validacionResumenLabel.setText("Sin violaciones ni advertencias. Listo para enviar.");
            } else {
                validacionResumenLabel.setText(violaciones.size() + " violación(es) que bloquean el envío, "
                        + advertencias.size() + " advertencia(s).");
            }
            grillaTable.refresh();
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al validar el diagrama:\n" + e.getMessage());
        }
    }

    @FXML
    private void onEnviar() {
        if (diagramaActual == null) {
            return;
        }
        if (dirty) {
            onGuardar();
            if (dirty) {
                return;
            }
        }
        if (!AlertUtils.showConfirm("¿Enviar el diagrama a aprobación de la Oficina de Personal?\n"
                + "Mientras esté pendiente no se podrá editar.")) {
            return;
        }
        try {
            diagramaService.enviarParaAprobacion(diagramaActual, usuarioActual);
            AlertUtils.showInfo("Diagrama enviado a aprobación.");
            refrescarComboDiagrama();
            actualizarControles();
        } catch (ServiceException e) {
            AlertUtils.showErr("No se pudo enviar el diagrama:\n" + e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        if (diagramaActual == null) {
            return;
        }
        if (!AlertUtils.showConfirm("¿Eliminar este borrador de diagrama?\n"
                + "Se pierden todas sus jornadas.")) {
            return;
        }
        try {
            diagramaService.eliminarBorrador(diagramaActual, usuarioActual);
            diagramaActual = null;
            dirty = false;
            cargarDiagramasDe(servicioCombo.getValue().getId());
        } catch (ServiceException e) {
            AlertUtils.showErr("No se pudo eliminar el borrador:\n" + e.getMessage());
        }
    }

    @FXML
    private void onCerrar() {
        if (!confirmarDescarteSiDirty()) {
            return;
        }
        ((Stage) grillaTable.getScene().getWindow()).close();
    }

    // ============================================================
    // Helpers de estado
    // ============================================================

    private boolean esOpODireccion() {
        return usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL, TipoUsuario.DIRECCION);
    }

    private boolean puedeGestionar() {
        if (esOpODireccion()) {
            return true;
        }
        return usuarioActual.hasRole(TipoUsuario.JEFATURADESERVICIO)
                && diagramaActual != null
                && diagramaActual.getServicioId().equals(usuarioActual.getServicioId());
    }

    private boolean esEditable() {
        return diagramaActual != null
                && (diagramaActual.getEstado() == EstadoDiagrama.BORRADOR
                    || diagramaActual.getEstado() == EstadoDiagrama.OBSERVADO)
                && puedeGestionar();
    }

    private boolean confirmarDescarteSiDirty() {
        return !dirty || AlertUtils.showConfirm(
                "Hay cambios sin guardar en la grilla. ¿Descartarlos?");
    }

    private List<JornadaLaboral> jornadasComoLista() {
        List<JornadaLaboral> lista = new ArrayList<>();
        for (Map<LocalDate, JornadaLaboral> porFecha : jornadasPorEmpleado.values()) {
            lista.addAll(porFecha.values());
        }
        return lista;
    }

    /** Recarga el combo para que la etiqueta refleje el nuevo estado. */
    private void refrescarComboDiagrama() {
        DiagramaDeServicio seleccionado = diagramaActual;
        List<DiagramaDeServicio> items = new ArrayList<>(diagramaCombo.getItems());
        diagramaCombo.getItems().setAll(items);
        diagramaCombo.getSelectionModel().select(seleccionado);
    }

    private void actualizarControles() {
        boolean hayDiagrama = diagramaActual != null;
        boolean editable = esEditable();

        eliminarButton.setDisable(!hayDiagrama || !editable
                || diagramaActual.getEstado() != EstadoDiagrama.BORRADOR);
        validarButton.setDisable(!hayDiagrama);
        guardarButton.setDisable(!editable || !dirty);
        enviarButton.setDisable(!editable);
        nuevoDiagramaButton.setDisable(servicioCombo.getValue() == null || !puedeCrear());
        ayudaLabel.setVisible(editable);

        // Chip de estado.
        estadoChip.getStyleClass().removeAll("estado-borrador", "estado-pendiente",
                "estado-aprobado", "estado-observado");
        if (hayDiagrama) {
            estadoChip.setText(etiquetaEstado(diagramaActual.getEstado())
                    + (dirty ? " •" : ""));
            estadoChip.getStyleClass().add(claseEstado(diagramaActual.getEstado()));
        } else {
            estadoChip.setText("");
        }

        // Banner de observación.
        boolean observado = hayDiagrama
                && diagramaActual.getEstado() == EstadoDiagrama.OBSERVADO
                && diagramaActual.getComentariosObservacion() != null;
        observacionBanner.setVisible(observado);
        observacionBanner.setManaged(observado);
        if (observado) {
            observacionLabel.setText(diagramaActual.getComentariosObservacion());
        }
    }

    private boolean puedeCrear() {
        return esOpODireccion() || usuarioActual.hasRole(TipoUsuario.JEFATURADESERVICIO);
    }

    private String etiquetaEstado(EstadoDiagrama estado) {
        return switch (estado) {
            case BORRADOR -> "Borrador";
            case PENDIENTE_APROBACION -> "Pendiente de aprobación";
            case APROBADO -> "Aprobado";
            case OBSERVADO -> "Observado";
        };
    }

    private String claseEstado(EstadoDiagrama estado) {
        return switch (estado) {
            case BORRADOR -> "estado-borrador";
            case PENDIENTE_APROBACION -> "estado-pendiente";
            case APROBADO -> "estado-aprobado";
            case OBSERVADO -> "estado-observado";
        };
    }
}
