package ar.com.hmu.service.diagramacion;

import ar.com.hmu.model.HorarioBase;
import ar.com.hmu.model.HorarioFeriante;
import ar.com.hmu.model.HorarioGuardiaEnfermeria;
import ar.com.hmu.model.HorarioNocturno;
import ar.com.hmu.model.JornadaLaboral;
import ar.com.hmu.model.TipoJornada;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Validador de las jornadas de un diagrama de servicio.
 *
 * <p><b>Reglas estructurales</b> ({@link #validar}) — bloquean el envío a
 * aprobación:</p>
 * <ol>
 *   <li>Jornada de tipo con horario ({@code requiereHorario()}) sin
 *       ingreso/egreso cargados, o con egreso anterior o igual al ingreso.</li>
 *   <li>Superposición de jornadas del mismo empleado (los intervalos
 *       [ingreso, egreso] no pueden pisarse; el nocturno que cruza
 *       medianoche se compara por sus timestamps completos).</li>
 *   <li>Descanso mínimo: no pueden transcurrir menos de
 *       {@value #DESCANSO_MINIMO_HORAS} horas corridas entre el egreso de
 *       una jornada y el ingreso de la siguiente (condicionalidad general
 *       del HMU).</li>
 * </ol>
 *
 * <p><b>Advertencias de carga mensual</b> ({@link #advertenciasCargaMensual})
 * — no bloquean, se muestran en el panel de validación: comparan las horas
 * totales de cada empleado contra lo esperado por su modalidad, en las
 * modalidades donde el número mensual es directo (Nocturno: exacto;
 * Feriante y GuardiaEnfermeria: mínimo). Las modalidades semanales
 * (Estandar, Semanal, DXI, GuardiaMedica) requieren lógica de ventanas por
 * semana — deuda para un pase posterior.</p>
 *
 * <p>La cobertura mínima del servicio ({@code Servicio.dotacionMinima})
 * está prevista pero NO se valida en este pase (decisión de alcance).</p>
 */
public final class DiagramaValidator {

    static final int DESCANSO_MINIMO_HORAS = 10;

    /** Duración mínima de toda jornada con horario (caso "viene 1 hora a
     *  completar la semana"). */
    public static final int DURACION_MINIMA_HORAS = 1;
    /** Máximo de un turno normal (definición del usuario 2026-07-08). */
    public static final int MAX_TURNO_HORAS = 12;
    /** Máximo PLANIFICADO de una guardia: 24 h exactas — la extensión del
     *  egreso real pertenece al registro de marcaciones, no al diagrama. */
    public static final int MAX_GUARDIA_HORAS = 24;

    private DiagramaValidator() {
        // Sólo métodos estáticos.
    }

    /**
     * Reglas estructurales sobre las jornadas del diagrama. Lista vacía =
     * diagrama válido para enviar a aprobación.
     */
    public static List<Violacion> validar(List<JornadaLaboral> jornadas) {
        List<Violacion> violaciones = new ArrayList<>();
        if (jornadas == null || jornadas.isEmpty()) {
            return violaciones;
        }

        Map<UUID, List<JornadaLaboral>> porEmpleado = agruparConHorario(jornadas, violaciones);

        for (Map.Entry<UUID, List<JornadaLaboral>> entry : porEmpleado.entrySet()) {
            List<JornadaLaboral> delEmpleado = entry.getValue();
            delEmpleado.sort(Comparator.comparing(JornadaLaboral::getFechaIngreso));

            for (int i = 1; i < delEmpleado.size(); i++) {
                JornadaLaboral previa = delEmpleado.get(i - 1);
                JornadaLaboral actual = delEmpleado.get(i);

                if (actual.getFechaIngreso().isBefore(previa.getFechaEgreso())) {
                    violaciones.add(new Violacion(entry.getKey(), actual.getFecha(),
                            "Superposición de jornadas: la jornada del " + actual.getFecha()
                                    + " comienza antes de que termine la del " + previa.getFecha() + "."));
                } else {
                    Duration descanso = Duration.between(previa.getFechaEgreso(), actual.getFechaIngreso());
                    if (descanso.toHours() < DESCANSO_MINIMO_HORAS) {
                        violaciones.add(new Violacion(entry.getKey(), actual.getFecha(),
                                "Descanso insuficiente: " + descanso.toHours() + " h entre el egreso del "
                                        + previa.getFecha() + " y el ingreso del " + actual.getFecha()
                                        + " (mínimo " + DESCANSO_MINIMO_HORAS + " h corridas)."));
                    }
                }
            }
        }
        return violaciones;
    }

    /**
     * Advertencias de carga horaria mensual por empleado, para las
     * modalidades con número mensual directo. No bloquean el envío.
     *
     * @param jornadas            jornadas del diagrama.
     * @param horariosPorEmpleado horario vigente de cada empleado; los
     *                            empleados ausentes del mapa se saltean.
     */
    public static List<Violacion> advertenciasCargaMensual(List<JornadaLaboral> jornadas,
                                                           Map<UUID, HorarioBase> horariosPorEmpleado) {
        List<Violacion> advertencias = new ArrayList<>();
        if (jornadas == null || jornadas.isEmpty() || horariosPorEmpleado == null) {
            return advertencias;
        }

        Map<UUID, Long> horasPorEmpleado = new HashMap<>();
        for (JornadaLaboral j : jornadas) {
            if (j.tieneHorario() && j.getEmpleadoId() != null) {
                Duration d = j.calcularDuracion();
                if (d != null) {
                    horasPorEmpleado.merge(j.getEmpleadoId(), d.toHours(), Long::sum);
                }
            }
        }

        for (Map.Entry<UUID, HorarioBase> entry : horariosPorEmpleado.entrySet()) {
            UUID empleadoId = entry.getKey();
            long horas = horasPorEmpleado.getOrDefault(empleadoId, 0L);

            switch (entry.getValue()) {
                case HorarioNocturno h -> {
                    long esperadas = (long) h.getDuracionJornadaHoras() * h.getNumeroJornadasMensuales();
                    if (esperadas > 0 && horas != esperadas) {
                        advertencias.add(new Violacion(empleadoId, null,
                                "Carga mensual nocturna: " + horas + " h planificadas, se esperan "
                                        + esperadas + " h."));
                    }
                }
                case HorarioFeriante h -> {
                    if (h.getHorasMinimasMensuales() > 0 && horas < h.getHorasMinimasMensuales()) {
                        advertencias.add(new Violacion(empleadoId, null,
                                "Carga mensual feriante: " + horas + " h planificadas, mínimo "
                                        + h.getHorasMinimasMensuales() + " h."));
                    }
                }
                case HorarioGuardiaEnfermeria h -> {
                    long esperadas = (long) h.getNumeroGuardias12Horas() * h.getDuracionGuardia12Horas()
                            + (long) h.getNumeroGuardias10Horas() * h.getDuracionGuardia10Horas();
                    if (esperadas > 0 && horas < esperadas) {
                        advertencias.add(new Violacion(empleadoId, null,
                                "Carga mensual guardia enfermería: " + horas + " h planificadas, se esperan "
                                        + esperadas + " h."));
                    }
                }
                default -> {
                    // Modalidades semanales: validación por ventanas de semana,
                    // diferida a un pase posterior.
                }
            }
        }
        return advertencias;
    }

    /**
     * Límites de duración por tipo de jornada (regla estructural): mínimo
     * {@value #DURACION_MINIMA_HORAS} h para todas; máximo
     * {@value #MAX_TURNO_HORAS} h para turno normal y
     * {@value #MAX_GUARDIA_HORAS} h para guardias (activa o pasiva).
     *
     * @return la violación, o {@code null} si la duración es válida.
     */
    public static Violacion validarDuracion(JornadaLaboral j) {
        Duration d = Duration.between(j.getFechaIngreso(), j.getFechaEgreso());
        if (d.toMinutes() < DURACION_MINIMA_HORAS * 60L) {
            return new Violacion(j.getEmpleadoId(), j.getFecha(),
                    "Duración insuficiente: toda jornada debe durar al menos "
                            + DURACION_MINIMA_HORAS + " h.");
        }
        int maximo = (j.getTipo() == TipoJornada.TURNO_NORMAL) ? MAX_TURNO_HORAS : MAX_GUARDIA_HORAS;
        if (d.toMinutes() > maximo * 60L) {
            return new Violacion(j.getEmpleadoId(), j.getFecha(),
                    "Duración excesiva: " + (d.toMinutes() / 60.0) + " h supera el máximo de "
                            + maximo + " h para " + j.getTipo() + ".");
        }
        return null;
    }

    // ============================================================
    // Helpers privados
    // ============================================================

    /**
     * Agrupa por empleado las jornadas que tienen horario, registrando de
     * paso las violaciones estructurales de la regla 1 (horario faltante o
     * invertido en tipos que lo requieren).
     */
    private static Map<UUID, List<JornadaLaboral>> agruparConHorario(List<JornadaLaboral> jornadas,
                                                                     List<Violacion> violaciones) {
        Map<UUID, List<JornadaLaboral>> porEmpleado = new HashMap<>();
        for (JornadaLaboral j : jornadas) {
            if (j.getTipo() != null && j.getTipo().requiereHorario() && !j.tieneHorario()) {
                violaciones.add(new Violacion(j.getEmpleadoId(), j.getFecha(),
                        "Jornada " + j.getTipo() + " sin horario de ingreso/egreso."));
                continue;
            }
            if (j.tieneHorario()) {
                if (!j.getFechaEgreso().isAfter(j.getFechaIngreso())) {
                    violaciones.add(new Violacion(j.getEmpleadoId(), j.getFecha(),
                            "Horario inválido: el egreso debe ser posterior al ingreso."));
                    continue;
                }
                Violacion duracion = validarDuracion(j);
                if (duracion != null) {
                    violaciones.add(duracion);
                    continue;
                }
                porEmpleado.computeIfAbsent(j.getEmpleadoId(), k -> new ArrayList<>()).add(j);
            }
        }
        return porEmpleado;
    }
}
