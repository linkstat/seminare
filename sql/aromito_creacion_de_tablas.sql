/* ============================================================================
 * Aromito - DDL de creación de tablas (PostgreSQL 13+)
 *
 * Migración desde MariaDB realizada en el paso 3 del roadmap.
 *
 * Decisiones de la migración:
 *  - Tipo UUID nativo (vs. BINARY(16) en MariaDB). gen_random_uuid() está
 *    built-in desde PostgreSQL 13, no requiere extensión pgcrypto.
 *  - Los antiguos ENUM(...) inline de MariaDB se traducen a CREATE TYPE ...
 *    AS ENUM, con nombres tipados (sexo, agrupacion_cargo, etc.). Desde Java
 *    se setean con casts explícitos en las queries: ?::sexo.
 *  - DATETIME → TIMESTAMP (sin zona horaria; el HMU opera en una sola zona).
 *  - BLOB → BYTEA.
 *  - DOUBLE → DOUBLE PRECISION; INT → INTEGER.
 *  - Backticks eliminados; PostgreSQL no los soporta (usa "" para identif.).
 *  - El ciclo Servicio.direccionID → Empleado.id se resuelve creando Servicio
 *    sin esa FK y agregándola con ALTER TABLE tras crear Empleado.
 *  - ENGINE=InnoDB no aplica.
 *  - SET FOREIGN_KEY_CHECKS no aplica; se reordenan tablas o se difieren FKs.
 *
 * Decisiones de diseño preservadas del rediseño previo (ver paso 1):
 *  - El polimorfismo de Usuario se resuelve por roles (tabla Rol + Usuario_Rol),
 *    no por Class Table Inheritance. Por eso no existen tablas Direccion,
 *    JefaturaDeServicio, OficinaDePersonal: son sólo roles.
 *  - Empleado se mantiene como subclase persistida porque tiene atributos
 *    propios (francosCompensatoriosUtilizados, horarioActualID).
 *  - Usuario.tipoUsuario fue eliminada (el discriminador vive en Usuario_Rol).
 *  - FKs que antes apuntaban a las tablas hijas eliminadas ahora apuntan a
 *    Empleado(id); el rol esperado se valida a nivel aplicación.
 *
 * Para regenerar desde cero:
 *   DROP DATABASE aromito; CREATE DATABASE aromito;
 *   psql -d aromito -f aromito_creacion_de_tablas.sql
 * ========================================================================== */


/* ----------------------------------------------------------------------------
 * Tipos enumerados
 *
 * Se nombran por dominio (no por tabla.columna) para reusarlos si dos
 * columnas comparten el mismo conjunto de valores. Los nombres son lower
 * snake_case por convención de PostgreSQL.
 * -------------------------------------------------------------------------- */
CREATE TYPE agrupacion_cargo AS ENUM (
    'INDEFINIDO', 'PLANTAPOLITICA', 'JEFATURA', 'TECNICO', 'ADMINISTRATIVO',
    'SERVICIO', 'ENFERMERIA', 'MEDICO', 'PROFESIONAL'
);

CREATE TYPE agrupacion_servicio AS ENUM (
    'ADMINISTRATIVO', 'SERVICIO', 'MEDICO', 'ENFERMERIA', 'TECNICO', 'PLANTAPOLITICA'
);

CREATE TYPE sexo AS ENUM ('FEMENINO', 'MASCULINO', 'OTRO');

CREATE TYPE tipo_rol_autorizacion AS ENUM (
    'EMPLEADO', 'JEFATURADESERVICIO', 'OFICINADEPERSONAL', 'DIRECCION'
);

CREATE TYPE tipo_rol_memo_autorizacion AS ENUM (
    'JEFATURADESERVICIO', 'OFICINADEPERSONAL', 'DIRECCION', 'USUARIO'
);

CREATE TYPE estado_memo_autorizacion AS ENUM ('PENDIENTE', 'AUTORIZADO', 'RECHAZADO', 'OBSERVADO');

CREATE TYPE tipo_marcacion AS ENUM ('INGRESO', 'EGRESO');

CREATE TYPE estado_diagrama AS ENUM
    ('BORRADOR', 'PENDIENTE_APROBACION', 'APROBADO', 'OBSERVADO');

CREATE TYPE tipo_jornada AS ENUM
    ('TURNO_NORMAL', 'GUARDIA_ACTIVA', 'GUARDIA_PASIVA',
     'FRANCO', 'FRANCO_COMPENSATORIO', 'LICENCIA');


/* ----------------------------------------------------------------------------
 * Jerarquía de Horarios (Class Table Inheritance)
 *
 * HorarioBase es la raíz; Horario y HorarioConFranquicia son sus dos ramas.
 * Las modalidades concretas (HorarioEstandar, HorarioSemanal, etc.) heredan
 * de Horario.
 * -------------------------------------------------------------------------- */
CREATE TABLE HorarioBase (
    id UUID PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL  -- 'Horario', 'HorarioConFranquicia'
);

CREATE TABLE Horario (
    id UUID PRIMARY KEY,
    fechaIngreso TIMESTAMP NOT NULL,
    fechaEgreso TIMESTAMP NOT NULL,
    jornadasPlanificadas INTEGER,
    reglasHorario VARCHAR(255),
    horarioBaseID UUID NOT NULL,
    modalidad VARCHAR(50) NOT NULL,
    -- Posibles valores de modalidad: 'HorarioEstandar', 'HorarioSemanal',
    -- 'HorarioNocturno', 'HorarioFeriante', 'HorarioDXI',
    -- 'HorarioGuardiaEnfermeria', 'HorarioGuardiaMedica',
    -- 'HorarioJefeServicioGuardiaPasiva', 'HorarioAbierto'
    FOREIGN KEY (id) REFERENCES HorarioBase(id)
);

CREATE TABLE HorarioConFranquicia (
    id UUID PRIMARY KEY,
    fechaIngreso TIMESTAMP NOT NULL,
    fechaEgreso TIMESTAMP NOT NULL,
    horasFranquicia INTEGER NOT NULL,
    horarioDecoradoID UUID NOT NULL,
    FOREIGN KEY (id) REFERENCES HorarioBase(id),
    FOREIGN KEY (horarioDecoradoID) REFERENCES Horario(id)
);

CREATE TABLE HorarioEstandar (
    id UUID PRIMARY KEY,
    diasLaborables VARCHAR(255) NOT NULL,
    horasPorDia INTEGER NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioSemanal (
    id UUID PRIMARY KEY,
    distribucionSemanal VARCHAR(255) NOT NULL,
    horaInicioPorDia VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioNocturno (
    id UUID PRIMARY KEY,
    diasProgramados TEXT NOT NULL,
    duracionJornadaHoras INTEGER NOT NULL,
    numeroJornadasMensuales INTEGER NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioFeriante (
    id UUID PRIMARY KEY,
    diasNoLaborables TEXT NOT NULL,
    duracionGuardiaHoras INTEGER NOT NULL,
    guardiasProgramadas TEXT NOT NULL,
    horasMinimasMensuales INTEGER NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioDXI (
    id UUID PRIMARY KEY,
    distribucionHoraria VARCHAR(255) NOT NULL,
    horaInicioPorDia VARCHAR(255) NOT NULL,
    horasSemanales INTEGER NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioGuardiaMedica (
    id UUID PRIMARY KEY,
    duracionGuardiaHoras INTEGER NOT NULL,
    fechasGuardias TEXT NOT NULL,
    numeroGuardiasSemanal INTEGER NOT NULL,
    permitirGuardiasContinuas BOOLEAN NOT NULL,
    tiempoDescansoMinimoHoras INTEGER NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioGuardiaEnfermeria (
    id UUID PRIMARY KEY,
    duracionGuardia10Horas INTEGER NOT NULL,
    duracionGuardia12Horas INTEGER NOT NULL,
    fechasGuardias TEXT NOT NULL,
    numeroGuardias10Horas INTEGER NOT NULL,
    numeroGuardias12Horas INTEGER NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioJefeServicioGuardiaPasiva (
    id UUID PRIMARY KEY,
    diasLaborables VARCHAR(255) NOT NULL,
    horasPorDia INTEGER NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioAbierto (
    id UUID PRIMARY KEY,
    flexibilidadHoraria BOOLEAN NOT NULL,
    horasSemanales INTEGER NOT NULL,
    preferenciasHorarias VARCHAR(255),
    FOREIGN KEY (id) REFERENCES Horario(id)
);


/* ----------------------------------------------------------------------------
 * Cargo y Domicilio
 * -------------------------------------------------------------------------- */
CREATE TABLE Cargo (
    id UUID PRIMARY KEY,
    numero INTEGER UNIQUE NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    agrupacion agrupacion_cargo NOT NULL
);

CREATE TABLE Domicilio (
    id UUID PRIMARY KEY,
    calle VARCHAR(255) NOT NULL,
    numeracion INTEGER,
    barrio VARCHAR(255),
    ciudad VARCHAR(255),
    localidad VARCHAR(255),
    provincia VARCHAR(255)
);


/* ----------------------------------------------------------------------------
 * Servicio (sin FK direccionID por ahora — se cierra el ciclo más abajo)
 *
 * Servicio.direccionID apunta a Empleado(id): el empleado con rol DIRECCION
 * a cargo del servicio. La validación del rol queda a nivel aplicación.
 *
 * Como Empleado todavía no existe, declaramos la columna acá pero la FK la
 * agregamos con ALTER TABLE más adelante (rompe el ciclo de dependencias).
 * -------------------------------------------------------------------------- */
CREATE TABLE Servicio (
    id UUID PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    agrupacion agrupacion_servicio NOT NULL,
    direccionID UUID NOT NULL,
    encargadoUsuarioID UUID NULL,
    dotacionMinima INTEGER NULL      -- cobertura mínima (placeholder RFS02; sin uso en pase 1)
);


/* ----------------------------------------------------------------------------
 * Usuario
 *
 * Tabla principal de personas. El polimorfismo (qué tipo de usuario es)
 * se resuelve por la tabla Usuario_Rol, no por una columna discriminadora.
 * -------------------------------------------------------------------------- */
CREATE TABLE Usuario (
    id UUID PRIMARY KEY,
    fechaAlta DATE NOT NULL,
    estado BOOLEAN NOT NULL,
    cuil BIGINT UNIQUE NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    nombres VARCHAR(255) NOT NULL,
    sexo sexo NOT NULL,
    mail VARCHAR(255) NOT NULL,
    tel BIGINT,
    domicilioID UUID,
    cargoID UUID,
    servicioID UUID,
    passwd VARCHAR(255) NOT NULL,
    profile_image BYTEA,
    FOREIGN KEY (domicilioID) REFERENCES Domicilio(id),
    FOREIGN KEY (cargoID) REFERENCES Cargo(id),
    FOREIGN KEY (servicioID) REFERENCES Servicio(id)
);


/* ----------------------------------------------------------------------------
 * Empleado (ex Agente)
 *
 * Única subclase persistida de Usuario. Se mantiene porque tiene atributos
 * propios. Las otras tres antiguas hijas (Direccion, JefaturaDeServicio,
 * OficinaDePersonal) se eliminaron: ahora son sólo roles.
 * -------------------------------------------------------------------------- */
CREATE TABLE Empleado (
    id UUID PRIMARY KEY,
    francosCompensatoriosUtilizados INTEGER,
    horarioActualID UUID,
    FOREIGN KEY (id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (horarioActualID) REFERENCES HorarioBase(id)
);


/* ----------------------------------------------------------------------------
 * Cierre del ciclo Servicio.direccionID → Empleado(id)
 * Cierre de Servicio.encargadoUsuarioID → Usuario(id) (encargado actual del
 * papeleo del servicio: aprobaciones, rechazos y observaciones de memos)
 * Y tabla intermedia Servicio_JefaturaDeServicio
 * -------------------------------------------------------------------------- */
ALTER TABLE Servicio
    ADD CONSTRAINT fk_servicio_direccion
    FOREIGN KEY (direccionID) REFERENCES Empleado(id);

ALTER TABLE Servicio
    ADD CONSTRAINT fk_servicio_encargado
    FOREIGN KEY (encargadoUsuarioID) REFERENCES Usuario(id);

CREATE TABLE Servicio_JefaturaDeServicio (
    servicioID UUID NOT NULL,
    empleadoID UUID NOT NULL,
    PRIMARY KEY (servicioID, empleadoID),
    FOREIGN KEY (servicioID) REFERENCES Servicio(id) ON DELETE CASCADE,
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id) ON DELETE CASCADE
);


/* ----------------------------------------------------------------------------
 * Roles
 *
 * Múltiples Roles por Usuario. Los cambios de roles son frecuentes (p.ej.
 * asignar a un Empleado como jefe temporalmente), así que manejarlos como
 * entidades evita modificar la jerarquía de clases para cada cambio.
 * -------------------------------------------------------------------------- */
CREATE TABLE Rol (
    id UUID PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE Usuario_Rol (
    usuario_id UUID NOT NULL,
    rol_id UUID NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES Rol(id) ON DELETE CASCADE
);

CREATE INDEX idx_usuariorol_usuario ON Usuario_Rol(usuario_id);
CREATE INDEX idx_usuariorol_rol ON Usuario_Rol(rol_id);


/* ----------------------------------------------------------------------------
 * Autorización y EstadoTramite
 *
 * Autorizacion.tipoRol (ex Autorizacion.tipo): identifica el rol con el que
 * se firmó la autorización. Valores alineados con TipoUsuario en código.
 * TODO post-migración: revisar si esta tabla sigue teniendo sentido o si su
 * propósito puede cubrirse con Memorandum_Autorizacion + Usuario_Rol.
 * -------------------------------------------------------------------------- */
CREATE TABLE Autorizacion (
    id UUID PRIMARY KEY,
    fechaAutorizacion TIMESTAMP NOT NULL,
    tipoRol tipo_rol_autorizacion NOT NULL,
    autorizadoPorID UUID NOT NULL,
    FOREIGN KEY (autorizadoPorID) REFERENCES Usuario(id)
);

CREATE TABLE EstadoTramite (
    id UUID PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);
INSERT INTO EstadoTramite (id, nombre) VALUES
    (gen_random_uuid(), 'BORRADOR'),
    (gen_random_uuid(), 'ENVIADO'),
    (gen_random_uuid(), 'PENDIENTE DE FIRMA'),
    (gen_random_uuid(), 'PENDIENTE DE AUTORIZACION'),
    (gen_random_uuid(), 'AUTORIZADO'),
    (gen_random_uuid(), 'RECHAZADO'),
    (gen_random_uuid(), 'OBSERVADO'),
    (gen_random_uuid(), 'LEIDO'),
    (gen_random_uuid(), 'COMPLETADO');


/* ----------------------------------------------------------------------------
 * Novedades
 * -------------------------------------------------------------------------- */
CREATE TABLE Novedad (
    id UUID PRIMARY KEY,
    cod INTEGER NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    estadoFechaModif TIMESTAMP,
    fechaInicio DATE,
    fechaFin DATE,
    fechaSolicitud DATE,
    reqAprobDireccion BOOLEAN NOT NULL,
    estadoTramiteID UUID,
    FOREIGN KEY (estadoTramiteID) REFERENCES EstadoTramite(id)
);

-- Por la novedad 99 se requiere relación N:M Empleado <-> Novedad.
CREATE TABLE Empleado_Novedad (
    empleadoID UUID NOT NULL,
    novedadID UUID NOT NULL,
    PRIMARY KEY (empleadoID, novedadID),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (novedadID) REFERENCES Novedad(id)
);


/* ----------------------------------------------------------------------------
 * Diagramación de servicios
 * -------------------------------------------------------------------------- */
-- El diagrama mensual de un servicio. Estado tipado (state machine
-- BORRADOR → PENDIENTE_APROBACION → APROBADO / OBSERVADO), rango de
-- vigencia [fechaInicio, fechaFin], auditoría y version optimista.
CREATE TABLE DiagramaDeServicio (
    id UUID PRIMARY KEY,
    servicioID UUID NOT NULL,
    estado estado_diagrama NOT NULL DEFAULT 'BORRADOR',
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    creadoPorID UUID NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT now(),
    updatedAt TIMESTAMP NOT NULL DEFAULT now(),
    aprobadoPorID UUID NULL,               -- se llena en APROBADO/OBSERVADO
    fechaAprobacion TIMESTAMP NULL,
    comentariosObservacion TEXT NULL,      -- motivo de OBSERVADO (espeja Memorandum_Autorizacion)
    FOREIGN KEY (servicioID) REFERENCES Servicio(id),
    FOREIGN KEY (creadoPorID) REFERENCES Usuario(id),
    FOREIGN KEY (aprobadoPorID) REFERENCES Usuario(id),
    CHECK (fechaFin >= fechaInicio)
);

-- Una fila por empleado por día del diagrama. Absorbe la ex-tabla puente
-- Planificacion: la jornada referencia directamente su diagrama y empleado.
-- fechaIngreso/Egreso son NULL para FRANCO / FRANCO_COMPENSATORIO / LICENCIA
-- (no tienen horario). La no-superposición se valida a nivel aplicación
-- (el nocturno cruza medianoche: no hay unicidad por empleado+fecha).
CREATE TABLE JornadaLaboral (
    id UUID PRIMARY KEY,
    diagramaID UUID NOT NULL,
    empleadoID UUID NOT NULL,
    fecha DATE NOT NULL,
    tipo tipo_jornada NOT NULL,
    fechaIngreso TIMESTAMP NULL,
    fechaEgreso TIMESTAMP NULL,
    observaciones TEXT NULL,
    FOREIGN KEY (diagramaID) REFERENCES DiagramaDeServicio(id) ON DELETE CASCADE,
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id)
);


/* ----------------------------------------------------------------------------
 * Memorandos
 *
 * Memorandum.estadoTramiteID (ex Memorandum.estado): renombrado para
 * uniformar con el resto de FKs hacia EstadoTramite.
 * Memorandum_Autorizacion.tipoRol (ex tipoAutorizacionID): renombrado
 * porque NO es FK (es un ENUM); valores alineados con TipoUsuario.
 * -------------------------------------------------------------------------- */
CREATE TABLE Memorandum (
    id UUID PRIMARY KEY,
    asunto VARCHAR(255) NOT NULL,
    contenido TEXT NOT NULL,
    fechaEnvio TIMESTAMP,
    fechaRecepcion TIMESTAMP,
    estadoTramiteID UUID NOT NULL,
    remitenteID UUID NOT NULL,
    FOREIGN KEY (estadoTramiteID) REFERENCES EstadoTramite(id),
    FOREIGN KEY (remitenteID) REFERENCES Usuario(id)
);

CREATE TABLE Memorandum_Destinatario (
    memorandumID UUID NOT NULL,
    usuarioID UUID NOT NULL,
    fechaRecepcion TIMESTAMP,
    PRIMARY KEY (memorandumID, usuarioID),
    FOREIGN KEY (memorandumID) REFERENCES Memorandum(id),
    FOREIGN KEY (usuarioID) REFERENCES Usuario(id)
);

CREATE TABLE Memorandum_Firmante (
    memorandumID UUID NOT NULL,
    usuarioID UUID NOT NULL,
    fechaFirma TIMESTAMP NOT NULL,
    PRIMARY KEY (memorandumID, usuarioID),
    FOREIGN KEY (memorandumID) REFERENCES Memorandum(id),
    FOREIGN KEY (usuarioID) REFERENCES Usuario(id)
);

CREATE TABLE Memorandum_Autorizacion (
    id UUID PRIMARY KEY,
    memorandumID UUID NOT NULL,
    tipoRol tipo_rol_memo_autorizacion NOT NULL,
    autorizadoPorID UUID,
    fechaAutorizacion TIMESTAMP,
    estado estado_memo_autorizacion NOT NULL,
    comentarios TEXT,
    FOREIGN KEY (memorandumID) REFERENCES Memorandum(id),
    FOREIGN KEY (autorizadoPorID) REFERENCES Usuario(id)
);


/* ----------------------------------------------------------------------------
 * Compensaciones
 *
 * jefaturaID (ex jefeDeServicioID) FK a Empleado(id): se espera que el
 * empleado tenga rol JEFATURADESERVICIO.
 * -------------------------------------------------------------------------- */
CREATE TABLE FrancoCompensatorio (
    id UUID PRIMARY KEY,
    cantHoras DOUBLE PRECISION NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fechaAutorizacion TIMESTAMP,
    fechaDeAplicacion DATE,
    estadoTramiteID UUID NOT NULL,
    autorizadaPorID UUID,
    empleadoID UUID NOT NULL,
    jefaturaID UUID,
    FOREIGN KEY (estadoTramiteID) REFERENCES EstadoTramite(id),
    FOREIGN KEY (autorizadaPorID) REFERENCES Usuario(id),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (jefaturaID) REFERENCES Empleado(id)
);

CREATE TABLE HoraExtra (
    id UUID PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    fechaIngreso TIMESTAMP NOT NULL,
    fechaEgreso TIMESTAMP NOT NULL,
    ponderacion INTEGER NOT NULL,
    fechaAutorizacion TIMESTAMP,
    estadoTramiteID UUID NOT NULL,
    autorizadaPorID UUID,
    empleadoID UUID NOT NULL,
    jefaturaID UUID,
    FOREIGN KEY (estadoTramiteID) REFERENCES EstadoTramite(id),
    FOREIGN KEY (autorizadaPorID) REFERENCES Usuario(id),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (jefaturaID) REFERENCES Empleado(id)
);


/* ----------------------------------------------------------------------------
 * Parte Diario y marcaciones
 *
 * ParteDiario.oficinaPersonalID (ex oficinaID) FK a Empleado(id): se espera
 * que el empleado tenga rol OFICINADEPERSONAL.
 * -------------------------------------------------------------------------- */
CREATE TABLE ParteDiario (
    id UUID PRIMARY KEY,
    fechaDeCierre TIMESTAMP,
    periodo VARCHAR(50) NOT NULL,
    modificadoPorID UUID,
    oficinaPersonalID UUID NOT NULL,
    FOREIGN KEY (modificadoPorID) REFERENCES Usuario(id),
    FOREIGN KEY (oficinaPersonalID) REFERENCES Empleado(id)
);

CREATE TABLE ParteDiario_Empleado (
    parteDiarioID UUID NOT NULL,
    empleadoID UUID NOT NULL,
    PRIMARY KEY (parteDiarioID, empleadoID),
    FOREIGN KEY (parteDiarioID) REFERENCES ParteDiario(id),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id)
);

CREATE TABLE MarcacionEmpleado (
    id UUID PRIMARY KEY,
    fechaMarcacion TIMESTAMP NOT NULL,
    observaciones VARCHAR(255),
    tipoMarcacion tipo_marcacion NOT NULL,
    validada BOOLEAN NOT NULL,
    empleadoID UUID NOT NULL,
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id)
);

/* RegistroJornadaLaboral
 * Las marcaciones referenciadas DEBEN pertenecer al mismo Empleado, la de
 * ingreso ser de tipo INGRESO y la de egreso de tipo EGRESO. Estas
 * restricciones se validan a nivel aplicación.
 */
CREATE TABLE RegistroJornadaLaboral (
    id UUID PRIMARY KEY,
    fecha DATE NOT NULL,
    empleadoID UUID NOT NULL,
    marcacionIngresoID UUID NOT NULL,
    marcacionEgresoID UUID NOT NULL,
    duracionJornada INTEGER, -- dato calculado, denormalizado para conveniencia
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (marcacionIngresoID) REFERENCES MarcacionEmpleado(id),
    FOREIGN KEY (marcacionEgresoID) REFERENCES MarcacionEmpleado(id)
);


/* ----------------------------------------------------------------------------
 * Índices adicionales
 * -------------------------------------------------------------------------- */
CREATE INDEX idx_servicio_direccion ON Servicio(direccionID);

CREATE INDEX idx_domicilio_calle ON Domicilio(calle);
CREATE INDEX idx_domicilio_barrio ON Domicilio(barrio);
CREATE INDEX idx_domicilio_ciudad ON Domicilio(ciudad);
CREATE INDEX idx_domicilio_localidad ON Domicilio(localidad);

CREATE INDEX idx_usuario_apellidos ON Usuario(apellidos);
CREATE INDEX idx_usuario_nombres ON Usuario(nombres);
CREATE UNIQUE INDEX idx_usuario_mail ON Usuario(mail);
CREATE UNIQUE INDEX idx_usuario_tel ON Usuario(tel);

CREATE UNIQUE INDEX idx_servicio_nombre ON Servicio(nombre);

CREATE INDEX idx_diagrama_servicio ON DiagramaDeServicio(servicioID);
CREATE INDEX idx_jornada_diagrama ON JornadaLaboral(diagramaID);
CREATE INDEX idx_jornada_empleado_fecha ON JornadaLaboral(empleadoID, fecha);
