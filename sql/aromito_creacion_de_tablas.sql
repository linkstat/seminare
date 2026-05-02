/* ============================================================================
 * Aromito - DDL de creación de tablas (MariaDB)
 *
 * Estado: motor MariaDB. La migración a PostgreSQL es un paso posterior del
 * roadmap (ver CLAUDE.md). Mientras tanto se mantienen UUID en BINARY(16) y
 * las funciones de conversión UUID_TO_BIN / BIN_TO_UUID.
 *
 * Decisiones de diseño aplicadas en este DDL:
 *  - El polimorfismo de Usuario se resuelve por roles (tabla Rol + Usuario_Rol),
 *    no por Class Table Inheritance. Por eso se eliminan las tablas hijas
 *    Direccion, JefaturaDeServicio (ex JefeDeServicio) y OficinaDePersonal,
 *    que no aportaban atributos propios.
 *  - Se mantiene la tabla Empleado (ex Agente) porque sí tiene atributos
 *    propios (francosCompensatoriosUtilizados, horarioActualID).
 *  - La columna Usuario.tipoUsuario se eliminó (el discriminador ahora vive
 *    en Usuario_Rol).
 *  - Nomenclatura unificada: Empleado en vez de Agente; JefaturaDeServicio
 *    en vez de JefeDeServicio (este último vive como nombre de rol y como
 *    sufijo en la tabla intermedia Servicio_JefaturaDeServicio).
 *  - Las FK que antes apuntaban a tablas hijas eliminadas (Direccion,
 *    JefaturaDeServicio, OficinaDePersonal) ahora apuntan a Empleado(id):
 *    se asume que el rol esperado se valida a nivel aplicación.
 * ========================================================================== */


/* ----------------------------------------------------------------------------
 * Funciones de conversión UUID <-> BINARY(16)
 *
 * Permiten manipular UUIDs como CHAR(36) desde la aplicación mientras se
 * almacenan compactos en BINARY(16). Se eliminarán en la migración a
 * PostgreSQL (donde existe el tipo UUID nativo).
 * -------------------------------------------------------------------------- */
DELIMITER $$
CREATE FUNCTION `UUID_TO_BIN`(uuid CHAR(36))
RETURNS BINARY(16)
DETERMINISTIC
BEGIN
  RETURN UNHEX(CONCAT(
    SUBSTRING(uuid, 1, 8),      -- aaaaaaaa
    SUBSTRING(uuid, 10, 4),     -- bbbb
    SUBSTRING(uuid, 15, 4),     -- cccc
    SUBSTRING(uuid, 20, 4),     -- dddd
    SUBSTRING(uuid, 25, 12)     -- eeeeeeeeeeee
  ));
END$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION `BIN_TO_UUID`(b BINARY(16))
RETURNS CHAR(36) CHARSET ascii
DETERMINISTIC
BEGIN
   DECLARE hexStr CHAR(32);
   SET hexStr = HEX(b);
   RETURN LOWER(CONCAT(
     SUBSTR(hexStr, 1, 8), '-',      -- aaaaaaaa
     SUBSTR(hexStr, 9, 4), '-',      -- bbbb
     SUBSTR(hexStr, 13, 4), '-',     -- cccc
     SUBSTR(hexStr, 17, 4), '-',     -- dddd
     SUBSTR(hexStr, 21, 12)          -- eeeeeeeeeeee
  ));
END$$
DELIMITER ;


/* ----------------------------------------------------------------------------
 * Jerarquía de Horarios (Class Table Inheritance)
 *
 * HorarioBase es la raíz; Horario y HorarioConFranquicia son sus dos ramas.
 * Las modalidades concretas (HorarioEstandar, HorarioSemanal, etc.) heredan
 * de Horario.
 * -------------------------------------------------------------------------- */
CREATE TABLE HorarioBase (
    id BINARY(16) PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL -- 'Horario', 'HorarioConFranquicia'
);

CREATE TABLE Horario (
    id BINARY(16) PRIMARY KEY,
    fechaIngreso DATETIME NOT NULL,
    fechaEgreso DATETIME NOT NULL,
    jornadasPlanificadas INT,
    reglasHorario VARCHAR(255),
    horarioBaseID BINARY(16) NOT NULL,
    modalidad VARCHAR(50) NOT NULL,
    -- Posibles valores de modalidad: 'HorarioEstandar', 'HorarioSemanal',
    -- 'HorarioNocturno', 'HorarioFeriante', 'HorarioDXI',
    -- 'HorarioGuardiaEnfermeria', 'HorarioGuardiaMedica',
    -- 'HorarioJefeServicioGuardiaPasiva', 'HorarioAbierto'
    FOREIGN KEY (id) REFERENCES HorarioBase(id)
);

CREATE TABLE HorarioConFranquicia (
    id BINARY(16) PRIMARY KEY,
    fechaIngreso DATETIME NOT NULL,
    fechaEgreso DATETIME NOT NULL,
    horasFranquicia INT NOT NULL,
    horarioDecoradoID BINARY(16) NOT NULL,
    FOREIGN KEY (id) REFERENCES HorarioBase(id),
    FOREIGN KEY (horarioDecoradoID) REFERENCES Horario(id)
);

CREATE TABLE HorarioEstandar (
    id BINARY(16) PRIMARY KEY,
    diasLaborables VARCHAR(255) NOT NULL,
    horasPorDia INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioSemanal (
    id BINARY(16) PRIMARY KEY,
    distribucionSemanal VARCHAR(255) NOT NULL,
    horaInicioPorDia VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioNocturno (
    id BINARY(16) PRIMARY KEY,
    diasProgramados VARCHAR(255) NOT NULL,
    duracionJornadaHoras INT NOT NULL,
    numeroJornadasMensuales INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioFeriante (
    id BINARY(16) PRIMARY KEY,
    diasNoLaborables VARCHAR(255) NOT NULL,
    duracionGuardiaHoras INT NOT NULL,
    guardiasProgramadas VARCHAR(255) NOT NULL,
    horasMinimasMensuales INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioDXI (
    id BINARY(16) PRIMARY KEY,
    distribucionHoraria VARCHAR(255) NOT NULL,
    horaInicioPorDia VARCHAR(255) NOT NULL,
    horasSemanales INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioGuardiaMedica (
    id BINARY(16) PRIMARY KEY,
    duracionGuardiaHoras INT NOT NULL,
    fechasGuardias VARCHAR(255) NOT NULL,
    numeroGuardiasSemanal INT NOT NULL,
    permitirGuardiasContinuas BOOLEAN NOT NULL,
    tiempoDescansoMinimoHoras INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioGuardiaEnfermeria (
    id BINARY(16) PRIMARY KEY,
    duracionGuardia10Horas INT NOT NULL,
    duracionGuardia12Horas INT NOT NULL,
    fechasGuardias VARCHAR(255) NOT NULL,
    numeroGuardias10Horas INT NOT NULL,
    numeroGuardias12Horas INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioJefeServicioGuardiaPasiva (
    id BINARY(16) PRIMARY KEY,
    diasLaborables VARCHAR(255) NOT NULL,
    horasPorDia INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

CREATE TABLE HorarioAbierto (
    id BINARY(16) PRIMARY KEY,
    flexibilidadHoraria BOOLEAN NOT NULL,
    horasSemanales INT NOT NULL,
    preferenciasHorarias VARCHAR(255),
    FOREIGN KEY (id) REFERENCES Horario(id)
);


/* ----------------------------------------------------------------------------
 * Cargo y Domicilio
 * -------------------------------------------------------------------------- */
CREATE TABLE Cargo (
    id BINARY(16) PRIMARY KEY,
    numero INT UNIQUE NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    agrupacion ENUM('INDEFINIDO', 'PLANTAPOLITICA', 'JEFATURA', 'TECNICO', 'ADMINISTRATIVO', 'SERVICIO', 'ENFERMERIA', 'MEDICO', 'PROFESIONAL') NOT NULL
);

CREATE TABLE Domicilio (
    id BINARY(16) PRIMARY KEY,
    calle VARCHAR(255) NOT NULL,
    numeracion INT,
    barrio VARCHAR(255),
    ciudad VARCHAR(255),
    localidad VARCHAR(255),
    provincia VARCHAR(255)
);


/* ----------------------------------------------------------------------------
 * Servicio y tabla intermedia Servicio_JefaturaDeServicio
 *
 * Servicio.direccionID apunta a Empleado(id): el empleado con rol DIRECCION
 * a cargo del servicio. La validación del rol queda a nivel aplicación.
 *
 * Servicio_JefaturaDeServicio mantiene la cardinalidad N:M (un servicio
 * puede tener varios jefes; un mismo empleado puede ser jefe de varios
 * servicios). La columna empleadoID FK a Empleado(id), donde se espera
 * que ese empleado tenga el rol JEFATURADESERVICIO.
 *
 * Se desactivan los FK checks porque hay un ciclo de dependencias entre
 * Servicio, Usuario y Empleado.
 * -------------------------------------------------------------------------- */
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE Servicio (
    id BINARY(16) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    agrupacion ENUM('ADMINISTRATIVO', 'SERVICIO', 'MEDICO', 'ENFERMERIA', 'TECNICO', 'PLANTAPOLITICA') NOT NULL,
    direccionID BINARY(16) NOT NULL,
    FOREIGN KEY (direccionID) REFERENCES Empleado(id)
);

CREATE TABLE Servicio_JefaturaDeServicio (
    servicioID BINARY(16) NOT NULL,
    empleadoID BINARY(16) NOT NULL,
    PRIMARY KEY (servicioID, empleadoID),
    FOREIGN KEY (servicioID) REFERENCES Servicio(id) ON DELETE CASCADE,
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id) ON DELETE CASCADE
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;


/* ----------------------------------------------------------------------------
 * Usuario
 *
 * Tabla principal de personas. El polimorfismo (qué tipo de usuario es)
 * se resuelve por la tabla Usuario_Rol, no por una columna discriminadora.
 * -------------------------------------------------------------------------- */
CREATE TABLE Usuario (
    id BINARY(16) PRIMARY KEY,
    fechaAlta DATE NOT NULL,
    estado BOOLEAN NOT NULL,
    cuil BIGINT UNIQUE NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    nombres VARCHAR(255) NOT NULL,
    sexo ENUM('FEMENINO', 'MASCULINO', 'OTRO') NOT NULL,
    mail VARCHAR(255) NOT NULL,
    tel BIGINT,
    domicilioID BINARY(16),
    cargoID BINARY(16),
    servicioID BINARY(16),
    passwd VARCHAR(255) NOT NULL,
    profile_image BLOB,
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
    id BINARY(16) PRIMARY KEY,
    francosCompensatoriosUtilizados INT,
    horarioActualID BINARY(16),
    FOREIGN KEY (id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (horarioActualID) REFERENCES HorarioBase(id)
);


/* ----------------------------------------------------------------------------
 * Roles
 *
 * Múltiples Roles por Usuario. Los cambios de roles son frecuentes (p.ej.
 * asignar a un Empleado como jefe temporalmente), así que manejarlos como
 * entidades evita modificar la jerarquía de clases para cada cambio.
 * -------------------------------------------------------------------------- */
CREATE TABLE Rol (
    id BINARY(16) PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE Usuario_Rol (
    usuario_id BINARY(16) NOT NULL,
    rol_id BINARY(16) NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES Rol(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_usuario ON Usuario_Rol(usuario_id);
CREATE INDEX idx_rol ON Usuario_Rol(rol_id);


/* ----------------------------------------------------------------------------
 * Autorización y EstadoTramite
 *
 * Autorizacion.tipoRol (ex Autorizacion.tipo): identifica el rol con el que
 * se firmó la autorización. Valores alineados con TipoUsuario en código.
 * TODO post-migración: revisar si esta tabla sigue teniendo sentido o si su
 * propósito puede cubrirse con Memorandum_Autorizacion + Usuario_Rol.
 * -------------------------------------------------------------------------- */
CREATE TABLE Autorizacion (
    id BINARY(16) PRIMARY KEY,
    fechaAutorizacion DATETIME NOT NULL,
    tipoRol ENUM('EMPLEADO', 'JEFATURADESERVICIO', 'OFICINADEPERSONAL', 'DIRECCION') NOT NULL,
    autorizadoPorID BINARY(16) NOT NULL,
    FOREIGN KEY (autorizadoPorID) REFERENCES Usuario(id)
);

CREATE TABLE EstadoTramite (
    id BINARY(16) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);
INSERT INTO EstadoTramite (id, nombre) VALUES
    (UUID_TO_BIN(UUID()), 'BORRADOR'),
    (UUID_TO_BIN(UUID()), 'ENVIADO'),
    (UUID_TO_BIN(UUID()), 'PENDIENTE DE FIRMA'),
    (UUID_TO_BIN(UUID()), 'PENDIENTE DE AUTORIZACION'),
    (UUID_TO_BIN(UUID()), 'AUTORIZADO'),
    (UUID_TO_BIN(UUID()), 'RECHAZADO'),
    (UUID_TO_BIN(UUID()), 'OBSERVADO'),
    (UUID_TO_BIN(UUID()), 'LEIDO'),
    (UUID_TO_BIN(UUID()), 'COMPLETADO');


/* ----------------------------------------------------------------------------
 * Novedades
 * -------------------------------------------------------------------------- */
CREATE TABLE Novedad (
    id BINARY(16) PRIMARY KEY,
    cod INT NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    estadoFechaModif TIMESTAMP,
    fechaInicio DATE,
    fechaFin DATE,
    fechaSolicitud DATE,
    reqAprobDireccion BOOLEAN NOT NULL,
    estadoTramiteID BINARY(16),
    FOREIGN KEY (estadoTramiteID) REFERENCES EstadoTramite(id)
);

-- Por la novedad 99 se requiere relación N:M Empleado <-> Novedad.
CREATE TABLE Empleado_Novedad (
    empleadoID BINARY(16) NOT NULL,
    novedadID BINARY(16) NOT NULL,
    PRIMARY KEY (empleadoID, novedadID),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (novedadID) REFERENCES Novedad(id)
);


/* ----------------------------------------------------------------------------
 * Diagramación de servicios
 * -------------------------------------------------------------------------- */
CREATE TABLE JornadaLaboral (
    id BINARY(16) PRIMARY KEY,
    fechaIngreso DATETIME NOT NULL,
    fechaEgreso DATETIME NOT NULL
);

CREATE TABLE DiagramaDeServicio (
    id BINARY(16) PRIMARY KEY,
    estado VARCHAR(50) NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    servicioID BINARY(16) NOT NULL,
    FOREIGN KEY (servicioID) REFERENCES Servicio(id)
);

-- Para el atributo 'planificaciones' de DiagramaDeServicio.
-- Relaciona DiagramaDeServicio, Empleado y JornadaLaboral.
CREATE TABLE Planificacion (
    diagramaID BINARY(16) NOT NULL,
    empleadoID BINARY(16) NOT NULL,
    jornadaID BINARY(16) NOT NULL,
    PRIMARY KEY (diagramaID, empleadoID, jornadaID),
    FOREIGN KEY (diagramaID) REFERENCES DiagramaDeServicio(id),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (jornadaID) REFERENCES JornadaLaboral(id)
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
    id BINARY(16) PRIMARY KEY,
    asunto VARCHAR(255) NOT NULL,
    contenido TEXT NOT NULL,
    fechaEnvio DATETIME,
    fechaRecepcion DATETIME,
    estadoTramiteID BINARY(16) NOT NULL,
    remitenteID BINARY(16) NOT NULL,
    FOREIGN KEY (estadoTramiteID) REFERENCES EstadoTramite(id),
    FOREIGN KEY (remitenteID) REFERENCES Usuario(id)
);

CREATE TABLE Memorandum_Destinatario (
    memorandumID BINARY(16) NOT NULL,
    usuarioID BINARY(16) NOT NULL,
    fechaRecepcion DATETIME,
    PRIMARY KEY (memorandumID, usuarioID),
    FOREIGN KEY (memorandumID) REFERENCES Memorandum(id),
    FOREIGN KEY (usuarioID) REFERENCES Usuario(id)
);

CREATE TABLE Memorandum_Firmante (
    memorandumID BINARY(16) NOT NULL,
    usuarioID BINARY(16) NOT NULL,
    fechaFirma DATETIME NOT NULL,
    PRIMARY KEY (memorandumID, usuarioID),
    FOREIGN KEY (memorandumID) REFERENCES Memorandum(id),
    FOREIGN KEY (usuarioID) REFERENCES Usuario(id)
);

CREATE TABLE Memorandum_Autorizacion (
    id BINARY(16) PRIMARY KEY,
    memorandumID BINARY(16) NOT NULL,
    tipoRol ENUM('JEFATURADESERVICIO', 'OFICINADEPERSONAL', 'DIRECCION', 'USUARIO') NOT NULL,
    autorizadoPorID BINARY(16),
    fechaAutorizacion DATETIME,
    estado ENUM('PENDIENTE', 'AUTORIZADO', 'RECHAZADO') NOT NULL,
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
    id BINARY(16) PRIMARY KEY,
    cantHoras DOUBLE NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fechaAutorizacion DATETIME,
    fechaDeAplicacion DATE,
    estadoTramiteID BINARY(16) NOT NULL,
    autorizadaPorID BINARY(16),
    empleadoID BINARY(16) NOT NULL,
    jefaturaID BINARY(16),
    FOREIGN KEY (estadoTramiteID) REFERENCES EstadoTramite(id),
    FOREIGN KEY (autorizadaPorID) REFERENCES Usuario(id),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (jefaturaID) REFERENCES Empleado(id)
);

CREATE TABLE HoraExtra (
    id BINARY(16) PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    fechaIngreso DATETIME NOT NULL,
    fechaEgreso DATETIME NOT NULL,
    ponderacion INT NOT NULL,
    fechaAutorizacion DATETIME,
    estadoTramiteID BINARY(16) NOT NULL,
    autorizadaPorID BINARY(16),
    empleadoID BINARY(16) NOT NULL,
    jefaturaID BINARY(16),
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
    id BINARY(16) PRIMARY KEY,
    fechaDeCierre DATETIME,
    periodo VARCHAR(50) NOT NULL,
    modificadoPorID BINARY(16),
    oficinaPersonalID BINARY(16) NOT NULL,
    FOREIGN KEY (modificadoPorID) REFERENCES Usuario(id),
    FOREIGN KEY (oficinaPersonalID) REFERENCES Empleado(id)
);

CREATE TABLE ParteDiario_Empleado (
    parteDiarioID BINARY(16) NOT NULL,
    empleadoID BINARY(16) NOT NULL,
    PRIMARY KEY (parteDiarioID, empleadoID),
    FOREIGN KEY (parteDiarioID) REFERENCES ParteDiario(id),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id)
);

CREATE TABLE MarcacionEmpleado (
    id BINARY(16) PRIMARY KEY,
    fechaMarcacion DATETIME NOT NULL,
    observaciones VARCHAR(255),
    tipoMarcacion ENUM('INGRESO', 'EGRESO') NOT NULL,
    validada BOOLEAN NOT NULL,
    empleadoID BINARY(16) NOT NULL,
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id)
);

/* RegistroJornadaLaboral
 * Las marcaciones referenciadas DEBEN pertenecer al mismo Empleado, la de
 * ingreso ser de tipo INGRESO y la de egreso de tipo EGRESO. Estas
 * restricciones se validan a nivel aplicación.
 */
CREATE TABLE RegistroJornadaLaboral (
    id BINARY(16) PRIMARY KEY,
    fecha DATE NOT NULL,
    empleadoID BINARY(16) NOT NULL,
    marcacionIngresoID BINARY(16) NOT NULL,
    marcacionEgresoID BINARY(16) NOT NULL,
    duracionJornada INT, -- dato calculado, denormalizado para conveniencia
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (marcacionIngresoID) REFERENCES MarcacionEmpleado(id),
    FOREIGN KEY (marcacionEgresoID) REFERENCES MarcacionEmpleado(id)
);


/* ----------------------------------------------------------------------------
 * Índices adicionales
 * -------------------------------------------------------------------------- */
CREATE INDEX idx_servicio_direccion ON Servicio (direccionID);

ALTER TABLE Domicilio
    ADD INDEX `idx_calle` (`calle`),
    ADD INDEX `idx_barrio` (`barrio`),
    ADD INDEX `idx_ciudad` (`ciudad`),
    ADD INDEX `idx_localidad` (`localidad`);

ALTER TABLE Usuario
    ADD INDEX `idx_apellidos`(`apellidos`),
    ADD INDEX `idx_nombres`(`nombres`),
    ADD UNIQUE INDEX `idx_mail`(`mail`),
    ADD UNIQUE INDEX `idx_tel`(`tel`);

ALTER TABLE Servicio
    ADD UNIQUE INDEX `idx_nombre`(`nombre`);
