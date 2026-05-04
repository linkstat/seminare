# Aromito

Sistema de Gestión de Ausentismo Hospitalario para el **Hospital Municipal de
Urgencias (HMU)** de Córdoba, Argentina. Desarrollado originalmente como
Seminario de Práctica de Licenciatura en Informática (Universidad Siglo 21,
2024) y continuado en evolución hacia un sistema de producción.

## Alcance del sistema

Aromito es **mucho más** que un reemplazo del parte diario. Es un sistema
integrado para gestionar todo el flujo de Recursos Humanos del HMU:

1. **ABM de personal y servicios**: altas, bajas, modificaciones de empleados,
   asignación a servicios, cargos.
2. **Diagramación de servicios**: cada jefatura genera el diagrama mensual con
   los horarios/guardias de los empleados de su servicio. Se valida y envía a
   la Oficina de Personal para aprobación.
3. **Gestión de marcaciones**: ingesta de marcaciones del reloj, conformación
   de jornadas laborales, detección de omisiones.
4. **Gestión de novedades** (con flujo de solicitud/aprobación):
   - Inasistencias (justificadas, injustificadas, fuerza mayor).
   - Cambios de horario (CH) y cambios de guardia (CG).
   - Horas extras y cálculo automático de francos compensatorios.
   - Pases de salida y devolución de horas.
   - Omisiones de marcación.
5. **Memorandos digitales**: comunicación formal entre empleado, jefatura,
   Oficina de Personal y Dirección.
6. **Generación del Parte Diario**: el reporte mensual hacia la Subsecretaría
   de Capital Humano. Idealmente, la Oficina de Personal solo le da el
   visto bueno al parte ya generado por el sistema.
7. **Reportes personalizados** y monitoreo en tiempo real de disponibilidad
   del personal.

El "parte diario" actual en Excel **es solo la salida** de uno de los muchos
procesos que Aromito gestiona.

## Modalidades de horario soportadas

El HMU funciona 24/7 con múltiples regímenes horarios. El sistema modela
cada uno con su propia tabla, herencia de `Horario`:

- **Estándar**: 35 hs/semana, 7 hs/día, días hábiles.
- **Semanal**: 35 hs/semana distribuidas irregularmente.
- **Nocturno**: 140 hs/mes, 14 jornadas de 10 hs (típicamente 21:00-07:00).
- **Feriante**: 120 hs/mes mínimas, 10 guardias de 12 hs en días no laborables.
- **Guardia médica**: 36 hs/semana, 3 guardias de 12 hs.
- **Guardia enfermería**: 140 hs/mes, 10 guardias de 12 hs + 2 de 10 hs.
- **DXI** (Diagnóstico por Imágenes): 24 hs/semana (legislación especial).
- **Jefe de servicio con guardia pasiva**: 1 hora menos por día.
- **Abierto**: 35 hs/semana en cualquier horario (no usado actualmente).
- **Con franquicia**: decorator que reduce N horas a otro horario base.

Condicionalidad general: no pueden transcurrir menos de 10 horas corridas
entre un egreso y un ingreso.

## Requerimientos

Resultado del proceso de elicitación. Los **RFS** son requerimientos
funcionales validados (alcance comprometido); los **RCSF** son candidatos
para evolución futura.

### Funcionales validados (RFS)

| ID | Requerimiento |
| --- | --- |
| RFS01 | La Oficina de Personal puede gestionar altas, bajas y modificaciones de empleados en la base de datos centralizada. |
| RFS02 | Las Jefaturas de Servicio generan diagramas de servicio personalizados, asignando empleados a turnos y guardias específicos. |
| RFS03 | Consultar en tiempo real la disponibilidad del personal, incluyendo horarios programados, licencias y faltas. |
| RFS04 | Los empleados registran horas extras; el total acumulado de horas extras disponibles se actualiza automáticamente con cada nuevo registro. |
| RFS05 | Registrar las horas utilizadas en pases de salida y calcular las horas pendientes de devolución. |
| RFS06 | Registrar omisiones de marcación de ingreso o egreso e incorporarlas al parte diario. |
| RFS07 | Los empleados envían memorándums a sus jefaturas, a la Oficina de Personal o a la Dirección del Hospital. |
| RFS08 | Generar reportes personalizados de horas trabajadas, francos compensatorios, licencias y cumplimiento de horarios, según el rol del usuario. |
| RFS09 | Enviar alertas automáticas para recordar fechas límite de presentación de diagramas de servicio y eventos importantes. |
| RFS10 | Controlar el acceso a las funcionalidades según el rol del usuario (Empleado, Jefatura de Servicio, Oficina de Personal, Dirección). |
| RFS11 | Los empleados generan francos compensatorios a partir de sus horas extras disponibles y los solicitan a la jefatura de servicio correspondiente. |

### Candidatos funcionales (RCSF)

| ID | Tema | Detalle |
| --- | --- | --- |
| RCSF01 | Gestión de licencias con automatización | Solicitud, aprobación y gestión de licencias (enfermedad, vacaciones, etc.) desde el sistema, con flujo de aprobación que involucra a Jefatura de Servicio y Oficina de Personal. **Nota de alcance**: Aromito gestionaría el flujo interno de solicitud/aprobación; la liquidación oficial sigue en VeDi. |
| RCSF02 | Integración con sistemas externos (VeDi) | Integrar con sistemas municipales mediante APIs si en el futuro las exponen para consulta o intercambio automático. |
| RCSF03 | Reportes avanzados con análisis predictivo | Reportes con análisis predictivo de disponibilidad futura de personal y posibles ausencias en base a datos históricos. |
| RCSF04 | Automatización de asignación horaria / asistencia de IA | Generar automáticamente horarios y guardias según reglas de disponibilidad, optimizando la asignación sin intervención manual. |
| RCSF05 | Auditoría y seguimiento de cambios | Auditoría detallada de cambios en datos de empleados y de decisiones tomadas por supervisores. |
| RCSF06 | Integración con Nextcloud / ONLYOFFICE para adjuntos | El HMU dispone de Nextcloud y ONLYOFFICE autoalojados. La idea es habilitar adjuntos en memorándums (y otros documentos) seleccionables sólo desde una carpeta de grupo con plantillas .docx oficiales (membretado del hospital), evitando archivos arbitrarios y manteniendo la formalidad institucional. |

## Stack

- **Lenguaje**: Java 25 LTS (release sept 2025, soporte hasta 2033).
  - Migración desde Java 23 con la que arrancó el proyecto.
- **Build**: Apache Maven (groupId: `ar.com.hmu`, artifactId: `aromito`).
- **UI**: JavaFX 25 + FXML + Scene Builder 23+.
  - Plugin: `org.openjfx:javafx-maven-plugin:0.0.8`.
  - Main class: `ar.com.hmu.ui.LoginScreen`.
  - Multiplataforma: las natives se resuelven según el OS de build.
    Producción target = Windows; desarrollo posible en Linux.
- **Persistencia**: PostgreSQL (migración desde MariaDB completada en
  commit `3761f5b`).
  - Driver: `org.postgresql:postgresql:42.7.11`.
  - Razón de la migración: tipo `UUID` nativo (vs. `BINARY(16)` y funciones
    de conversión), mejor soporte JDBC, mejores tipos para rangos de fechas.
- **Seguridad**: Argon2id para password hashing (migración desde BCrypt
  completada).
  - Librería: `de.mkammerer:argon2-jvm:2.12` (LGPL).
  - Parámetros: m=19456 KiB, t=2, p=1 (perfil OWASP recomendado).
- **Configuración**: SnakeYAML 2.0 (archivo `config.yaml` en resources).
  Secciones: `db:` (PostgreSQL) y `smtp:` (relay para notificaciones por
  email; si `smtp.host` está vacío, el `EmailNotificationService` opera
  en modo no-op silencioso).
- **Markdown**: `com.vladsch.flexmark:flexmark-all:0.64.8` para parsear
  el contenido de los memorándums (almacenado como Markdown crudo) y
  renderizarlo a HTML para mostrar en `WebView`.
- **Email**: `jakarta.mail:jakarta.mail-api:2.1.3` +
  `org.eclipse.angus:angus-mail:2.0.3` (implementación de referencia
  post-migración Jakarta). Envío async fire-and-forget vía
  `ExecutorService` de 2 threads daemon.

## Comandos frecuentes

```bash
# Compilar
mvn clean compile

# Empaquetar
mvn clean package

# Ejecutar
mvn javafx:run

# Tests (JUnit 5 + Mockito + AssertJ + Testcontainers)
mvn test
```

## Estructura del proyecto

```text
src/main/java/ar/com/hmu/
├── auth/         # autenticación (LoginService, etc.)
├── config/       # ConfigReader, DatabaseConfig (lee config.yaml)
├── constants/    # constantes globales
├── controller/   # controladores MVC (intermediarios entre ui y service)
├── exceptions/   # excepciones del dominio
├── factory/      # UsuarioFactory y otras factories
├── model/        # entidades del dominio (Usuario, Empleado, Novedad, etc.)
├── repository/
│   └── dao/      # DAOs (UsuarioDAO, etc.) + DatabaseConnector
├── service/
│   ├── *.java    # lógica de negocio (LoginService, MemorandumService, etc.)
│   └── notification/  # NotificationService + EmailNotificationService
├── ui/           # JavaFX (pantallas, controllers FXML)
└── util/         # utilidades generales (MarkdownRenderer, AlertUtils, etc.)

src/main/resources/
├── config/
│   └── config.yaml
├── css/         # MainMenuMosaicoStd.css, bandejaMemorandums.css, etc.
├── fonts/
├── fxml/        # pantallas (loginScreen, mainMenuMosaico, abmServicio,
│              #            bandejaMemorandums, detalleMemorandum,
│              #            redactarMemorandum, etc.)
└── images/
```

## Modelo de datos (resumen)

Base de datos: `aromito` (homenaje a *Acacia caven*, árbol nativo de Córdoba).

### Estrategia de herencia

**Class Table Inheritance**: cada subclase tiene su propia tabla, vinculada
a la tabla padre por una FK que también es la PK.

- `Usuario` (clase abstracta) → `Empleado`, `JefaturaDeServicio`,
  `OficinaDePersonal`, `Direccion`.
- `HorarioBase` → `Horario` → `HorarioEstandar`, `HorarioSemanal`,
  `HorarioNocturno`, `HorarioFeriante`, `HorarioDXI`,
  `HorarioGuardiaMedica`, `HorarioGuardiaEnfermeria`,
  `HorarioJefeServicioGuardiaPasiva`, `HorarioAbierto`.
- `HorarioBase` → `HorarioConFranquicia` (decorator de un Horario existente).

### Identificadores

UUIDs en todas las tablas. En PostgreSQL: tipo `UUID` nativo. En el código
Java: `java.util.UUID` mapeado directamente por el driver JDBC (sin
conversiones manuales bytes ↔ string).

### Tablas principales (24 en el prototipo)

- **Usuarios y servicios**: `Usuario`, `Empleado`, `JefaturaDeServicio`,
  `OficinaDePersonal`, `Direccion`, `Servicio`, `Cargo`, `Domicilio`.
  - `Servicio` tiene `encargadoUsuarioID` (FK a `Usuario`): apunta al
    encargado actual del papeleo (autorizaciones de memos, etc.).
    Normalmente coincide con la jefatura del servicio; en ausencia
    puede asignarse a otro empleado del servicio. Editable desde el
    ABM de Servicios por OP, Dirección y el propio jefe.
- **Horarios**: jerarquía descrita arriba.
- **Roles y autorizaciones**: `Rol`, `Usuario_Rol`, `Autorizacion`,
  `EstadoTramite`.
- **Novedades y partes**: `Novedad`, `Empleado_Novedad`, `ParteDiario`,
  `ParteDiario_Empleado`.
- **Diagramación**: `JornadaLaboral`, `DiagramaDeServicio`, `Planificacion`.
- **Memorandos**: `Memorandum`, `Memorandum_Destinatario`,
  `Memorandum_Firmante` (no usada en pase 1), `Memorandum_Autorizacion`.
  - `Memorandum_Autorizacion.estado` es ENUM PG `estado_memo_autorizacion`
    con valores `PENDIENTE / AUTORIZADO / RECHAZADO / OBSERVADO`. La
    columna `comentarios TEXT` acompaña a RECHAZADO y OBSERVADO.
  - `Memorandum_Destinatario.fechaRecepcion` se reinterpretó como
    "fecha de lectura" (NULL = no leído) sin cambio de DDL.
- **Compensaciones**: `FrancoCompensatorio`, `HoraExtra`.
- **Marcaciones**: `MarcacionEmpleado`, `RegistroJornadaLaboral`.

Ver `diagrams/DiagramaEntidadRelacion.png` y `sql/` en el repo para los
esquemas completos.

## Decisiones de diseño tomadas

- Arquitectura MVC + DAO + Factory.
- Class Table Inheritance para jerarquías (Usuario, Horario).
- UUIDs como identificadores en todas las tablas.
- Configuración en YAML (no `.properties`).
- Aplicación de escritorio (no web ni móvil), foco en ergonomía para
  usuarios no técnicos.
- Username de login = CUIL del empleado.
- Atributo `servicio` desnormalizado en clase abstracta `Usuario` (decisión
  consciente de redundancia para uniformidad del modelo OO).
- Argon2id para password hashing (migrado desde BCrypt original).
- **Workflows con state machine concreta por dominio**: cada flujo
  (memorándums, futuras licencias, novedades) tiene su propia state
  machine en `service/<X>StateMachine.java` (package-private). YAGNI
  estricto: no hay abstracción genérica todavía. Cuando llegue una
  segunda implementación, evaluar extraer las primitivas comunes que
  el DDL ya separa (estado, destinatarios, autorizaciones,
  comentarios).
- **Encargado por servicio para autorizaciones**: `Servicio.encargadoUsuarioID`
  es la fuente de verdad de "quién autoriza el papeleo de este servicio".
  Editable manualmente; Aromito no detecta licencias automáticamente
  (VeDi gestiona licencias, fuera de alcance).
- **Notificaciones por email**: abstracción `NotificationService` con
  impl `EmailNotificationService` (Jakarta Mail + Angus Mail). Async
  fire-and-forget. Si SMTP no está configurado (`smtp.host` vacío),
  el servicio es no-op silencioso (útil para dev/tests).
- **Markdown en contenido de memorándums**: storage as-is en TEXT,
  render Markdown→HTML con flexmark, mostrado en JavaFX `WebView`. La
  pantalla de redacción tiene SplitPane con editor + vista previa
  live (debounce 200ms).
- **ESC cierra ventanas modales** del módulo de memorándums (bandeja,
  detalle, redacción). En redacción respeta el flag `dirty` y dispara
  diálogo de confirmación si hay cambios sin guardar.

## Módulos funcionales implementados

Snapshot al 2026-05-03. Refleja qué RFS están cubiertos end-to-end:

- **Autenticación + login** (precondición): CUIL + password Argon2id, con
  flujo de cambio obligatorio en el primer login si la contraseña es la
  por defecto. Re-hash silencioso BCrypt → Argon2id en el primer login
  exitoso.
- **RFS01 — ABM de empleados**: alta / baja lógica / modificación
  funcional desde la pantalla de Oficina de Personal. Roles asignables
  por checkboxes.
- **ABM de Servicios** (apoyo de RFS02 y del módulo de memorándums):
  alta / modificación / eliminación + selector "Encargado actual"
  visible para OP, Dirección y el jefe del propio servicio.
- **RFS07 — Memorándums digitales**: completo end-to-end.
  - Bandejas: Recibidos / Enviados y borradores / Pendientes de
    autorizar (esta última visible sólo para roles autorizadores).
  - Redacción con Markdown + toolbar (B / I / H / • / 1.) + vista
    previa live en SplitPane.
  - Selector de destinatarios con ComboBox filtrable (apellido o
    nombre) + botones toggle "+ Jefaturas" / "+ Dirección" para
    grupos institucionales.
  - State machine: `BORRADOR → ENVIADO / PEND_AUTORIZACION →
    AUTORIZADO / RECHAZADO / OBSERVADO / LEIDO`.
  - Empleado escribiendo a OP o Dirección requiere autorización del
    encargado de su servicio. Jefatura, OP y Dirección envían directo.
  - Ciclo OBSERVADO: el autorizador devuelve con comentarios; el
    remitente corrige y reenvía generando una nueva fila en
    `Memorandum_Autorizacion` (audit trail completo).
  - Notificaciones por email para los 6 eventos del ciclo (memo
    recibido, confirmación de envío, autorización requerida,
    autorizado / rechazado / observado).
  - Adjuntos: placeholder visible deshabilitado (RCSF06 — Nextcloud
    + ONLYOFFICE).
- **RFS10 — Control de acceso por rol**: implementado a nivel UI
  (visibilidad de mosaicos / pestañas) y a nivel Service (validación
  de autorización). Cuatro roles: EMPLEADO, JEFATURADESERVICIO,
  OFICINADEPERSONAL, DIRECCION.

Otros RFS (RFS02 diagramas, RFS03 disponibilidad, RFS04 horas extras,
RFS05 pases de salida, RFS06 omisiones, RFS08 reportes, RFS09 alertas,
RFS11 francos compensatorios) — pendientes; los mosaicos correspondientes
del menú principal muestran "Módulo en construcción" al hacer click.

## Estado de migraciones (post-cátedra)

Aromito post-seminario evoluciona libremente sobre las decisiones originales.
Las migraciones completadas a la fecha son:

1. **Sync SQL ↔ Java**: modelo y DAOs alineados con el DDL.
2. **BCrypt → Argon2id** para password hashing.
3. **MariaDB → PostgreSQL**: reescritura del DDL (`BINARY(16)` → `UUID`,
   `ENUM(...)` → `CREATE TYPE`, `DATETIME` → `TIMESTAMP`, triggers a
   PL/pgSQL), eliminación de funciones de conversión `uuidToBin()` /
   `binToUuid()` en Java, scripts de inserción adaptados a
   `gen_random_uuid()`, cambio de driver y URL JDBC.
4. **Java 23 → Java 25 LTS** + **JavaFX 25.0.2**.
5. **Framework de tests**: JUnit 5 (Jupiter) + Mockito + AssertJ +
   Testcontainers configurado en `pom.xml`. Surefire 3.5.2.
   - Mockito usa `mock-maker-subclass` (configurado en
     `src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker`)
     porque el inline mock maker tiene problemas con Java 25.
   - Sólo unit + integration tests; los tests de UI (TestFX) quedan
     deliberadamente fuera de alcance — la validación visual la hacen los
     usuarios.

## Convenciones

- **Branches**: `main` (producción), `testing` (desarrollo activo).
- **Código fuente**: en español (entidades del dominio en español, ej.
  `Empleado` no `Employee`). Comentarios y JavaDoc en español.
- **Atribución de commits asistidos por IA**: cuando Claude Code colabore en
  un commit, se incluye `Co-authored-by: Claude <noreply@anthropic.com>` en
  el mensaje (Claude Code lo agrega automáticamente).

## No contempla (alcance excluido del sistema)

- Liquidación de sueldos (lo hace Capital Humano vía sistema VeDi).
- **Liquidación/registro oficial de licencias en VeDi**. Aromito sí gestiona
  el flujo interno de solicitud y aprobación de licencias (ver RCSF01); la
  liquidación final permanece en VeDi.
- Carpetas médicas (gestión externa).
- Integraciones automatizadas con sistemas municipales (no proveen API; ver
  RCSF02 si esto cambiara a futuro).

## Documentación complementaria

- Documento de seminario: `docs/HAMANN-PABLO-ALEJANDRO-AP4_git.pdf`.
- Diagramas: `diagrams/`.
- Scripts SQL: `sql/`.
- Repositorio: <https://github.com/linkstat/seminare>.
