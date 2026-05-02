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

## Stack

- **Lenguaje**: Java 25 LTS (release sept 2025, soporte hasta 2033).
  - Migración desde Java 23 con la que arrancó el proyecto.
- **Build**: Apache Maven (groupId: `ar.com.hmu`, artifactId: `aromito`).
- **UI**: JavaFX 25 + FXML + Scene Builder 23+.
  - Plugin: `org.openjfx:javafx-maven-plugin:0.0.8`.
  - Main class: `ar.com.hmu.ui.LoginScreen`.
  - Multiplataforma: las natives se resuelven según el OS de build.
    Producción target = Windows; desarrollo posible en Linux.
- **Persistencia**: PostgreSQL (migración en curso desde MariaDB).
  - Driver: `org.postgresql:postgresql:42.7.11`.
  - Razón de la migración: tipo `UUID` nativo (vs. `BINARY(16)` y funciones
    de conversión), mejor soporte JDBC, mejores tipos para rangos de fechas.
- **Seguridad**: Argon2id para password hashing.
  - Migración prevista desde BCrypt original (Aromito post-cátedra puede
    evolucionar libremente). Hashes BCrypt y Argon2 se distinguen por prefijo
    (`$2a$` vs `$argon2id$`); pueden coexistir durante la transición.
  - Librería: `de.mkammerer:argon2-jvm:2.12` (LGPL).
  - Parámetros: m=19456 KiB, t=2, p=1 (perfil OWASP recomendado).
- **Configuración**: SnakeYAML 2.0 (archivo `config.yaml` en resources).

## Comandos frecuentes

```bash
# Compilar
mvn clean compile

# Empaquetar
mvn clean package

# Ejecutar
mvn javafx:run

# Tests (TODO: framework aún no configurado, evaluar JUnit 5)
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
├── service/      # lógica de negocio (LoginService, etc.)
├── ui/           # JavaFX (pantallas, controllers FXML)
└── util/         # utilidades generales

src/main/resources/
├── config/
│   └── config.yaml
├── css/
├── fonts/
├── fxml/
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
- **Horarios**: jerarquía descrita arriba.
- **Roles y autorizaciones**: `Rol`, `Usuario_Rol`, `Autorizacion`,
  `EstadoTramite`.
- **Novedades y partes**: `Novedad`, `Empleado_Novedad`, `ParteDiario`,
  `ParteDiario_Empleado`.
- **Diagramación**: `JornadaLaboral`, `DiagramaDeServicio`, `Planificacion`.
- **Memorandos**: `Memorandum`, `Memorandum_Destinatario`,
  `Memorandum_Firmante`, `Memorandum_Autorizacion`.
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
- BCrypt original → migración a Argon2id en evolución post-cátedra.

## Migración en curso: MariaDB → PostgreSQL

**Estado**: el DDL original está en MariaDB. La migración implica:

1. Reescribir DDL: `BINARY(16)` → `UUID`, `ENUM(...)` → `CREATE TYPE`,
   `DATETIME` → `TIMESTAMP`, `BOOLEAN` (mismo), ajustar triggers a PL/pgSQL.
2. Eliminar funciones de conversión `uuidToBin()` / `binToUuid()` en Java
   (innecesarias con tipo UUID nativo).
3. Adaptar scripts de inserción (sin `UUID_TO_BIN` / `BIN_TO_UUID`; usar
   `gen_random_uuid()` de PostgreSQL).
4. Cambiar driver y URL JDBC.
5. Mantener compatibilidad con el modelo Java existente (que se simplifica).

## Convenciones

- **Branches**: `main` (producción), `testing` (desarrollo activo).
- **Código fuente**: en español (entidades del dominio en español, ej.
  `Empleado` no `Employee`). Comentarios y JavaDoc en español.
- **Atribución de commits asistidos por IA**: cuando Claude Code colabore en
  un commit, se incluye `Co-authored-by: Claude <noreply@anthropic.com>` en
  el mensaje (Claude Code lo agrega automáticamente).

## No contempla (alcance excluido del sistema)

- Liquidación de sueldos (lo hace Capital Humano vía sistema VeDi).
- Licencias de cualquier tipo (ordinarias, sanitarias, etc. también VeDi).
- Carpetas médicas (gestión externa).
- Integraciones automatizadas con sistemas municipales (no proveen API).

## Documentación complementaria

- Documento de seminario: `docs/HAMANN-PABLO-ALEJANDRO-AP4_git.pdf`.
- Diagramas: `diagrams/`.
- Scripts SQL: `sql/`.
- Repositorio: <https://github.com/linkstat/seminare>.
