/* Definición de funciones personalizadas
 * Dado que se decidió utilizar UUID y almacenarlos en formato binario, contar con una función que facilite la inserción y consulta de estos datos, nos facilitará enormemente la vida.
 * Los UUIDs se almacenan usando la representación estándar, de esta forma podemos manipular sin problemas usando java.util.UUID.
*/
-- Función para convertir (y almacenar) un UUID en un binario de 16 bytes
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

-- Función para recuperar y convertir de nuevo a UUID
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


-- Tabla HorarioBase
/* 
 * Implementación de la Herencia entre Horarios
 * Utilizamos la estrategia de Tabla por Subclase, donde:
 * HorarioBase es la tabla base que contiene el identificador y el tipo de horario.
 * Horario y HorarioConFranquicia son subclases que extienden HorarioBase.
 */
CREATE TABLE HorarioBase (
    id BINARY(16) PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL -- 'Horario', 'HorarioConFranquicia'
);

-- Tabla Horario
/* 
 * Agregamos una columna 'modalidad' para identificar el tipo de horario y facilitar consultas:
 * Posibles valores: 'HorarioEstandar', 'HorarioSemanal', 'HorarioNocturno',
 * 'HorarioFeriante', 'HorarioDXI', 'HorarioGuardiaEnfermeria', 'HorarioGuardiaMedica',
 * 'HorarioJefeServicioGuardiaPasiva', 'HorarioAbierto'
 */
CREATE TABLE Horario (
    id BINARY(16) PRIMARY KEY,
    fechaIngreso DATETIME NOT NULL,
    fechaEgreso DATETIME NOT NULL,
    jornadasPlanificadas INT,
    reglasHorario VARCHAR(255),
    horarioBaseID BINARY(16) NOT NULL,
    FOREIGN KEY (id) REFERENCES HorarioBase(id),
    modalidad VARCHAR(50) NOT NULL
);

-- Tabla HorarioConFranquicia
CREATE TABLE HorarioConFranquicia (
    id BINARY(16) PRIMARY KEY,
    fechaIngreso DATETIME NOT NULL,
    fechaEgreso DATETIME NOT NULL,
    horasFranquicia INT NOT NULL,
    horarioDecoradoID BINARY(16) NOT NULL,
    FOREIGN KEY (id) REFERENCES HorarioBase(id),
    FOREIGN KEY (horarioDecoradoID) REFERENCES Horario(id)
);

-- Tabla HorarioEstandar
CREATE TABLE HorarioEstandar (
    id BINARY(16) PRIMARY KEY,
    diasLaborables VARCHAR(255) NOT NULL,
    horasPorDia INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

-- Tabla HorarioSemanal
CREATE TABLE HorarioSemanal (
    id BINARY(16) PRIMARY KEY,
    distribucionSemanal VARCHAR(255) NOT NULL,
    horaInicioPorDia VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

-- Tabla HorarioNocturno
CREATE TABLE HorarioNocturno (
    id BINARY(16) PRIMARY KEY,
    diasProgramados VARCHAR(255) NOT NULL,
    duracionJornadaHoras INT NOT NULL,
    numeroJornadasMensuales INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

-- Tabla HorarioFeriante
CREATE TABLE HorarioFeriante (
    id BINARY(16) PRIMARY KEY,
    diasNoLaborables VARCHAR(255) NOT NULL,
    duracionGuardiaHoras INT NOT NULL,
    guardiasProgramadas VARCHAR(255) NOT NULL,
    horasMinimasMensuales INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

-- Tabla HorarioDXI
CREATE TABLE HorarioDXI (
    id BINARY(16) PRIMARY KEY,
    distribucionHoraria VARCHAR(255) NOT NULL,
    horaInicioPorDia VARCHAR(255) NOT NULL,
    horasSemanales INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

-- Tabla HorarioGuardiaMedica
CREATE TABLE HorarioGuardiaMedica (
    id BINARY(16) PRIMARY KEY,
    duracionGuardiaHoras INT NOT NULL,
    fechasGuardias VARCHAR(255) NOT NULL,
    numeroGuardiasSemanal INT NOT NULL,
    permitirGuardiasContinuas BOOLEAN NOT NULL,
    tiempoDescansoMinimoHoras INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

-- Tabla HorarioGuardiaEnfermeria
CREATE TABLE HorarioGuardiaEnfermeria (
    id BINARY(16) PRIMARY KEY,
    duracionGuardia10Horas INT NOT NULL,
    duracionGuardia12Horas INT NOT NULL,
    fechasGuardias VARCHAR(255) NOT NULL,
    numeroGuardias10Horas INT NOT NULL,
    numeroGuardias12Horas INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

-- Tabla HorarioJefeServicioGuardiaPasiva
CREATE TABLE HorarioJefeServicioGuardiaPasiva (
    id BINARY(16) PRIMARY KEY,
    diasLaborables VARCHAR(255) NOT NULL,
    horasPorDia INT NOT NULL,
    FOREIGN KEY (id) REFERENCES Horario(id)
);

-- Tabla HorarioAbierto
CREATE TABLE HorarioAbierto (
    id BINARY(16) PRIMARY KEY,
    flexibilidadHoraria BOOLEAN NOT NULL,
    horasSemanales INT NOT NULL,
    preferenciasHorarias VARCHAR(255),
    FOREIGN KEY (id) REFERENCES Horario(id)
);


-- Tabla Cargo
CREATE TABLE Cargo (
    id BINARY(16) PRIMARY KEY,
    numero INT NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    agrupacion ENUM('ADMINISTRATIVO', 'SERVICIO', 'MEDICO', 'ENFERMERIA', 'TECNICO', 'PLANTA POLITICA') NOT NULL
);

-- Tabla Domicilio
 CREATE TABLE Domicilio (
    id BINARY(16) PRIMARY KEY,
    calle VARCHAR(255) NOT NULL,
    numeracion INT,
    barrio VARCHAR(255),
    ciudad VARCHAR(255),
    localidad VARCHAR(255),
    provincia VARCHAR(255)
);



SET FOREIGN_KEY_CHECKS = 0; -- Deshabilitar las Restricciones de Claves Foráneas
-- Tabla Servicio
CREATE TABLE Servicio (
    id BINARY(16) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    agrupacion ENUM('ADMINISTRATIVO', 'SERVICIO', 'MEDICO', 'ENFERMERIA', 'TECNICO', 'PLANTA POLITICA') NOT NULL,
    direccionID BINARY(16) NOT NULL,
    FOREIGN KEY (direccionID) REFERENCES Direccion(id)
);
SET FOREIGN_KEY_CHECKS = 1; -- Habilitar las Restricciones de Claves Foráneas


 -- Tabla Usuario
/*
 * Representa a la clase Usuario (y es base para sus herederas): JefaturaDeServicio, OficinaDePersonal y Direccion
 * La herencia puede manejarse de varias formas en SQL.
 * Usaremos la estrategia de Tabla por Subclase (Class Table Inheritance)
 * donde cada subclase tiene su propia tabla que extiende la tabla de la superclase
 * mediante una clave primaria que es también clave foránea a la tabla de la superclase.
 */
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
    tipoUsuario VARCHAR(50) NOT NULL, -- Indica si: 'Direccion', 'Empleado', 'JefaturaDeServicio', 'OficinaDePersonal'
    passwd VARCHAR(255) NOT NULL,
    profile_image BLOB,
    FOREIGN KEY (domicilioID) REFERENCES Domicilio(id),
    FOREIGN KEY (cargoID) REFERENCES Cargo(id),
    FOREIGN KEY (servicioID) REFERENCES Servicio(id)
);

-- Tabla Direccion
CREATE TABLE Direccion (
    id BINARY(16) PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id)
    -- No tiene atributos adicionales directos
);

-- Tabla JefaturaDeServicio
CREATE TABLE JefaturaDeServicio (
    id BINARY(16) PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id)
);

-- Tabla OficinaDePersonal
CREATE TABLE OficinaDePersonal (
    id BINARY(16) PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id),
    reportesGenerados INT
);

-- Tabla Empleado
CREATE TABLE Empleado (
    id BINARY(16) PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id),
    francosCompensatoriosUtilizados INT,
    horarioActualID BINARY(16),
    jefaturaID BINARY(16) NOT NULL,
    FOREIGN KEY (horarioActualID) REFERENCES HorarioBase(id),
    FOREIGN KEY (jefaturaID) REFERENCES JefaturaDeServicio(id)
);


/* Notas:
 * Integridad Referencial: Cada Empleado está asociado a una JefaturaDeServicio y a un Servicio.
 * Coherencia: Dado que la JefaturaDeServicio está asociada al Servicio, podemos validar que el Empleado y la JefaturaDeServicio pertenecen al mismo Servicio.
 * Restricciones Adicionales: Agregamos una restricción para asegurar que el servicioID del Empleado coincida con el servicioID de la JefaturaDeServicio a la que está asignado.
 */
/* Lo siguiente no funciona (MySQL/MariaDB no permite SELECT dentro de CHECK), asi que probamos otra cosa
ALTER TABLE Empleado
ADD CONSTRAINT chk_servicio_consistente CHECK (
    servicioID = (
        SELECT servicioID
        FROM JefaturaDeServicio
        WHERE id = Empleado.jefaturaID
    )
);
 */
-- Trigger BEFORE INSERT
-- cambiamos el delimitador por defecto para no tener problemas
DELIMITER $$
CREATE TRIGGER trg_check_servicio_consistente_before_insert
BEFORE INSERT ON Empleado
FOR EACH ROW
BEGIN
    DECLARE empleadoServicioID BINARY(16);
    DECLARE jefaturaServicioID BINARY(16);

    -- Obtener el servicioID del Empleado desde la tabla Usuario
    SELECT servicioID INTO empleadoServicioID
    FROM Usuario
    WHERE id = NEW.id;

    -- Verificar si NEW.jefaturaID no es NULL
    IF NEW.jefaturaID IS NOT NULL THEN
        -- Obtener el servicioID de la JefaturaDeServicio desde la tabla Usuario
        SELECT servicioID INTO jefaturaServicioID
        FROM Usuario
        WHERE id = NEW.jefaturaID;

        -- Si no se encuentra el servicioID de la JefaturaDeServicio, generar un error
        IF jefaturaServicioID IS NULL THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'La JefaturaDeServicio especificada no tiene un servicio asignado.';
        END IF;

        -- Verificar si los servicioID coinciden
        IF empleadoServicioID != jefaturaServicioID THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'El servicioID del Empleado debe coincidir con el servicioID de la JefaturaDeServicio.';
        END IF;
    END IF;
    -- Si NEW.jefaturaID es NULL, no se realiza la validación
END$$
-- restablecemos el delimitador por defecto
DELIMITER ;

-- Trigger BEFORE UPDATE
-- cambiamos el delimitador por defecto para no tener problemas
DELIMITER $$
CREATE TRIGGER trg_check_servicio_consistente_before_update
BEFORE UPDATE ON Empleado
FOR EACH ROW
BEGIN
    DECLARE empleadoServicioID BINARY(16);
    DECLARE jefaturaServicioID BINARY(16);

    -- Obtener el servicioID del Empleado desde la tabla Usuario
    SELECT servicioID INTO empleadoServicioID
    FROM Usuario
    WHERE id = NEW.id;

    -- Verificar si NEW.jefaturaID no es NULL
    IF NEW.jefaturaID IS NOT NULL THEN
        -- Obtener el servicioID de la JefaturaDeServicio desde la tabla Usuario
        SELECT servicioID INTO jefaturaServicioID
        FROM Usuario
        WHERE id = NEW.jefaturaID;

        -- Si no se encuentra el servicioID de la JefaturaDeServicio, generar un error
        IF jefaturaServicioID IS NULL THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'La JefaturaDeServicio especificada no tiene un servicio asignado.';
        END IF;

        -- Verificar si los servicioID coinciden
        IF empleadoServicioID != jefaturaServicioID THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'El servicioID del Empleado debe coincidir con el servicioID de la JefaturaDeServicio.';
        END IF;
    END IF;
    -- Si NEW.jefaturaID es NULL, no se realiza la validación
END$$
-- restablecemos el delimitador por defecto
DELIMITER ;


-- Tabla Autorizacion
CREATE TABLE Autorizacion (
    id BINARY(16) PRIMARY KEY,
    fechaAutorizacion DATETIME NOT NULL,
    tipo ENUM('DIRECCION', 'JEFATURADESERVICIO', 'OFICINADEPERSONAL', 'USUARIO') NOT NULL,
    autorizadoPorID BINARY(16) NOT NULL,
    FOREIGN KEY (autorizadoPorID) REFERENCES Usuario(id)
);

-- Tabla EstadoTramite
CREATE TABLE EstadoTramite (
    id BINARY(16) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);
-- Inicializar tabla EstadoTramite
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


-- Tabla Novedad
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

-- Tabla Intermedia Empleado_Novedad (Por la novedad 99, tenemos una relación mucho-a-muchos que salvaguardar)
CREATE TABLE Empleado_Novedad (
    empleadoID BINARY(16) NOT NULL,
    novedadID BINARY(16) NOT NULL,
    PRIMARY KEY (empleadoID, novedadID),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (novedadID) REFERENCES Novedad(id)
);

-- Tabla JornadaLaboral
CREATE TABLE JornadaLaboral (
    id BINARY(16) PRIMARY KEY,
    fechaIngreso DATETIME NOT NULL,
    fechaEgreso DATETIME NOT NULL
);

-- Tabla DiagramaDeServicio
CREATE TABLE DiagramaDeServicio (
    id BINARY(16) PRIMARY KEY,
    estado VARCHAR(50) NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    servicioID BINARY(16) NOT NULL,
    FOREIGN KEY (servicioID) REFERENCES Servicio(id)
);

-- Tabla Planificacion
/* Para el atributo 'planificaciones' de la clase DiagramaDeServicio.
 * Relaciona DiagramaDeServicio, Empleado y JornadaLaboral.
 * Representa las planificaciones de cada empleado en un diagrama de servicio.
 */
CREATE TABLE Planificacion (
    diagramaID BINARY(16) NOT NULL,
    empleadoID BINARY(16) NOT NULL,
    jornadaID BINARY(16) NOT NULL,
    PRIMARY KEY (diagramaID, empleadoID, jornadaID),
    FOREIGN KEY (diagramaID) REFERENCES DiagramaDeServicio(id),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (jornadaID) REFERENCES JornadaLaboral(id)
);

-- Tabla Memorandum
CREATE TABLE Memorandum (
    id BINARY(16) PRIMARY KEY,
    asunto VARCHAR(255) NOT NULL,
    contenido TEXT NOT NULL,
    fechaEnvio DATETIME,
    fechaRecepcion DATETIME,
    estado BINARY(16) NOT NULL,
    remitenteID BINARY(16) NOT NULL,
    FOREIGN KEY (estado) REFERENCES EstadoTramite(id),
    FOREIGN KEY (remitenteID) REFERENCES Usuario(id)
);

-- Tabla Memorandum_Destinatario (relación muchos-a-muchos entre 'Memorandum' y 'Usuario' para los destinatarios)
CREATE TABLE Memorandum_Destinatario (
    memorandumID BINARY(16) NOT NULL,
    usuarioID BINARY(16) NOT NULL,
    fechaRecepcion DATETIME,
    PRIMARY KEY (memorandumID, usuarioID),
    FOREIGN KEY (memorandumID) REFERENCES Memorandum(id),
    FOREIGN KEY (usuarioID) REFERENCES Usuario(id)
);

-- Tabla Memorandum_Firmante (relación muchos-a-muchos entre 'Memorandum' y 'Usuario' para los firmantes, incluyendo la fecha y hora de la firma)
CREATE TABLE Memorandum_Firmante (
    memorandumID BINARY(16) NOT NULL,
    usuarioID BINARY(16) NOT NULL,
    fechaFirma DATETIME NOT NULL,
    PRIMARY KEY (memorandumID, usuarioID),
    FOREIGN KEY (memorandumID) REFERENCES Memorandum(id),
    FOREIGN KEY (usuarioID) REFERENCES Usuario(id)
);

-- Tabla Memorandum_Autorizacion (registra las autorizaciones necesarias y las obtenidas para cada memo)
CREATE TABLE Memorandum_Autorizacion (
    id BINARY(16) PRIMARY KEY,
    memorandumID BINARY(16) NOT NULL,
    tipoAutorizacionID ENUM('JefeDeServicio', 'OficinaDePersonal', 'Direccion', 'Usuario') NOT NULL,
    autorizadoPorID BINARY(16),
    fechaAutorizacion DATETIME,
    estado ENUM('PENDIENTE', 'AUTORIZADO', 'RECHAZADO') NOT NULL,
    FOREIGN KEY (memorandumID) REFERENCES Memorandum(id),
    FOREIGN KEY (autorizadoPorID) REFERENCES Usuario(id)
);


-- Tabla FrancoCompensatorio
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
    FOREIGN KEY (jefaturaID) REFERENCES JefaturaDeServicio(id)
);

-- Tabla HoraExtra
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
    FOREIGN KEY (jefaturaID) REFERENCES JefaturaDeServicio(id)
);


-- Tabla ParteDiario
CREATE TABLE ParteDiario (
    id BINARY(16) PRIMARY KEY,
    fechaDeCierre DATETIME,
    periodo VARCHAR(50) NOT NULL,
    modificadoPorID BINARY(16),
    oficinaID BINARY(16) NOT NULL,
    FOREIGN KEY (modificadoPorID) REFERENCES Usuario(id),
    FOREIGN KEY (oficinaID) REFERENCES OficinaDePersonal(id)
);

-- Tabla Intermedia ParteDiario_Empleado (relaciona ParteDiario con Empleado)
CREATE TABLE ParteDiario_Empleado (
    parteDiarioID BINARY(16) NOT NULL,
    empleadoID BINARY(16) NOT NULL,
    PRIMARY KEY (parteDiarioID, empleadoID),
    FOREIGN KEY (parteDiarioID) REFERENCES ParteDiario(id),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id)
);

-- Tabla MarcacionEmpleado
CREATE TABLE MarcacionEmpleado (
    id BINARY(16) PRIMARY KEY,
    fechaMarcacion DATETIME NOT NULL,
    observaciones VARCHAR(255),
    tipoMarcacion ENUM('INGRESO', 'EGRESO') NOT NULL,
    validada BOOLEAN NOT NULL,
    empleadoID BINARY(16) NOT NULL,
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id)
);


-- Tabla RegistroJornadaLaboral
/* Notas:
 * fecha: Representa la fecha de la jornada laboral.
 * empleadoID: Referencia al empleado al que pertenece la jornada.
 * marcacionIngresoID y marcacionEgresoID: FK a las marcaciones específicas de ingreso y egreso.
 * Restricciones adicionales:
 * Las marcaciones referenciadas DEBEN pertenecer al mismo empleado.
 * La marcación de ingreso debe ser de tipo INGRESO.
 * La marcación de egreso debe ser de tipo EGRESO.
 */
CREATE TABLE RegistroJornadaLaboral (
    id BINARY(16) PRIMARY KEY,
    fecha DATE NOT NULL,
    empleadoID BINARY(16) NOT NULL,
    marcacionIngresoID BINARY(16) NOT NULL,
    marcacionEgresoID BINARY(16) NOT NULL,
    duracionJornada INT, -- No es bueno guardar datos calculados, pero me puede facilitar la vida al momento de programar la lógica de negocios
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (marcacionIngresoID) REFERENCES MarcacionEmpleado(id),
    FOREIGN KEY (marcacionEgresoID) REFERENCES MarcacionEmpleado(id)
);



-- Extras
/* Creación de índices en FK (Claves Foráneas)
 * Agregamos índices en las columnas que son claves foráneas y se utilizan en consultas frecuentes.
 */
CREATE INDEX idx_empleado_jefatura ON Empleado (jefaturaID);
CREATE INDEX idx_servicio_direccion ON Servicio (direccionID);


-- Creación de índices en Domicilio
 ALTER TABLE Domicilio
	ADD INDEX `idx_calle` (`calle`),
	ADD INDEX `idx_barrio` (`barrio`),
	ADD INDEX `idx_ciudad` (`ciudad`),
	ADD INDEX `idx_localidad` (`localidad`);


-- Creación de índices en Usuario
 ALTER TABLE Usuario
	ADD INDEX `idx_apellidos`(`apellidos`),
	ADD INDEX `idx_nombres`(`nombres`),
	ADD UNIQUE INDEX `idx_mail`(`mail`),
	ADD UNIQUE INDEX `idx_tel`(`tel`);


-- Creación de índices en Servicio
ALTER TABLE Servicio
    ADD UNIQUE INDEX `idx_nombre`(`nombre`);


/* Creación de un usuario propietario para la BD (acceso localhost unicamente)
 * user: aromito
 * pass: aromitoSuperSecretDBPass
 */
CREATE USER 'aromito'@'localhost' IDENTIFIED BY 'aromitoSuperSecretDBPass';
GRANT ALL PRIVILEGES ON aromito.* TO 'aromito'@'localhost';
FLUSH PRIVILEGES;
