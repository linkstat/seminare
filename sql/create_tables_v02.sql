/* Definición de funciones personalizadas
 * Dado que se decidió utilizar UUID y almacenarlos en formato binario, contar con una funcion que facilite la insersión y consulta de estos datos, nos facilitará enormemente la vida.
 * Las funciones propuestas (obtenidas desde https://mariadb.com/kb/en/uuid-data-type/ ), usan el enfoque de MySQL 8.0+ (UUID_TO_BIN(uuid, swap_flag=1), donde swap_flag=1 indica que los bytes deben reordenarse para mejorar la localidad de referencia en los índices.
 * Ventajas:
 * Mejora el rendimiento de los índices al ordenar los UUIDs de manera más secuencial.
 * Reduce la fragmentación y los costos de mantenimiento de índices en bases de datos con gran cantidad de inserciones.
 * Desventajas:
 * Los UUIDs almacenados no coincidirán con su representación estándar si se leen directamente desde la BD sin usar las funciones adecuadas.
 * Requiere ser cuidadosos: hay que asegurar que todos los componentes del sistema manejen correctamente el reordenamiento para evitar inconsistencias.
*/
-- Fucncion para convertir (y almacenar) un UUID en un binario de 16 bytes
DELIMITER $$
CREATE FUNCTION `UUID_TO_BIN`(uuid CHAR(36))
RETURNS BINARY(16)
DETERMINISTIC
BEGIN
  RETURN UNHEX(CONCAT(
    SUBSTRING(uuid, 25, 12),
    SUBSTRING(uuid, 20, 4),
    SUBSTRING(uuid, 15, 4),
    SUBSTRING(uuid, 10, 4),
    SUBSTRING(uuid, 1, 8)
  ));
END$$
DELIMITER ;

-- Fucnción para recuperar y convertir de nuevo a UUID
DELIMITER $$
CREATE FUNCTION `BIN_TO_UUID2`(b BINARY(16))
RETURNS CHAR(36) CHARSET ascii
DETERMINISTIC
BEGIN
   DECLARE hexStr CHAR(32);
   SET hexStr = HEX(b);
   RETURN LOWER(CONCAT(
     SUBSTR(hexStr, 25, 8), '-',      -- eeeeeeee
     SUBSTR(hexStr, 21, 4), '-',      -- eeee
     SUBSTR(hexStr, 13, 8), '-',      -- ddddcccc
     SUBSTR(hexStr, 9, 4), '-',       -- bbbb
     SUBSTR(hexStr, 1, 8)             -- aaaaaaaa
  ));
END$$
DELIMITER ;


/* Tabla HorarioBase: para la clase abstracta HorarioBase
 * Implementación de la Herencia entre Horarios
 * Utilizamos la estrategia de Tabla por Subclase, donde:
 * HorarioBase es la tabla base que contiene el identificador y el tipo de horario.
 * Horario y HorarioConFranquicia son subclases que extienden HorarioBase.
 */
CREATE TABLE HorarioBase (
    id BINARY(16) PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL -- 'Horario', 'HorarioConFranquicia'
);

/* Tabla Horario: para la clase Horario
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

-- Tabla HorarioConFranquicia: para la clase HorarioConFranquicia
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


-- Tabla Cargo: representa la clase Cargo
CREATE TABLE Cargo (
    id BINARY(16) PRIMARY KEY,
    numero INT NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    agrupacion ENUM('ADMINISTRATIVO', 'SERVICIO', 'MEDICO', 'ENFERMERIA', 'TECNICO', 'PLANTA POLITICA') NOT NULL
);

-- Tabla Domicilio -- Representa a la clase Domicilio
 CREATE TABLE Domicilio (
    id BINARY(16) PRIMARY KEY,
    calle VARCHAR(255) NOT NULL,
    numeracion INT,
    barrio VARCHAR(255),
    localidad VARCHAR(255),
    ciudad VARCHAR(255),
    provincia VARCHAR(255)
);

/* Tabla Usuarios
 * Representa a la clase Usuario (y es base para sus herederas): JefeDeServicio, OficinaDePersonal y Direccion
 * La herencia puede manejarse de varias formas en SQL.
 * Usaremos la estrategia de Tabla por Subclase (Class Table Inheritance)
 * donde cada subclase tiene su propia tabla que extiende la tabla de la superclase
 * mediante una clave primaria que es también clave foránea a la tabla de la superclase.
 */
CREATE TABLE Usuario (
    id BINARY(16) PRIMARY KEY,
    nombres VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    mail VARCHAR(255) NOT NULL,
    cuil BIGINT NOT NULL,
    fechaAlta DATE NOT NULL,
    sexo ENUM('FEMENINO', 'MASCULINO', 'OTRO') NOT NULL,
    tel BIGINT,
    estado BOOLEAN NOT NULL,
    domicilioID BINARY(16),
    cargoID BINARY(16),
    tipoUsuario VARCHAR(50) NOT NULL, -- Indica si: 'Direccion', 'Empleado', 'JefaturaDeServicio', 'OficinaDePersonal'
    FOREIGN KEY (domicilioID) REFERENCES Domicilio(id),
    FOREIGN KEY (cargoID) REFERENCES Cargo(id)
);

-- Tabla Direccion: para la clase Direccion; no tiene atributos específicos
CREATE TABLE Direccion (
    id BINARY(16) PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id)
    -- No tiene atributos adicionales directos
);

-- Tabla Servicio: para la clase Servicio
CREATE TABLE Servicio (
    id BINARY(16) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    agrupacion ENUM('ADMINISTRATIVO', 'SERVICIO', 'MEDICO', 'ENFERMERIA', 'TECNICO', 'PLANTA POLITICA') NOT NULL,
    direccionID BINARY(16) NOT NULL,
    FOREIGN KEY (direccionID) REFERENCES Direccion(id)
);

-- Tabla JefaturaDeServicio: para la clase JefaturaDeServicio; añadimos los atributos específicos
CREATE TABLE JefaturaDeServicio (
    id BINARY(16) PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id),
    servicioID BINARY(16) NOT NULL,
    FOREIGN KEY (servicioID) REFERENCES Servicio(id)
);

-- Tabla OficinaDePersonal: para la clase OficinaDePersonal; añadimos los atributos específicos
CREATE TABLE OficinaDePersonal (
    id BINARY(16) PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id),
    reportesGenerados INT
);

-- Tabla Empleado: para la clase Empelado; añadimos los atributos específicos
CREATE TABLE Empleado (
    id BINARY(16) PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id),
    francosCompensatoriosUtilizados INT,
    horarioActualID BINARY(16),
    jefaturaID BINARY(16) NOT NULL,
    servicioID BINARY(16),
    FOREIGN KEY (horarioActualID) REFERENCES HorarioBase(id),
    FOREIGN KEY (jefaturaID) REFERENCES JefaturaDeServicio(id),
    FOREIGN KEY (servicioID) REFERENCES Servicio(id)
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
    DECLARE jefaturaServicioID BINARY(16);

    -- Verificar si NEW.jefaturaID no es NULL
    IF NEW.jefaturaID IS NOT NULL THEN
        -- Intentar obtener el servicioID de la JefaturaDeServicio asociada
        SELECT servicioID INTO jefaturaServicioID
        FROM JefaturaDeServicio
        WHERE id = NEW.jefaturaID;

        -- Si no se encuentra la JefaturaDeServicio, generar un error
        IF jefaturaServicioID IS NULL THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'La JefaturaDeServicio especificada no existe.';
        END IF;

        -- Verificar si los servicioID coinciden
        IF NEW.servicioID != jefaturaServicioID THEN
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
    DECLARE jefaturaServicioID BINARY(16);

    -- Verificar si NEW.jefaturaID no es NULL
    IF NEW.jefaturaID IS NOT NULL THEN
        -- Intentar obtener el servicioID de la JefaturaDeServicio asociada
        SELECT servicioID INTO jefaturaServicioID
        FROM JefaturaDeServicio
        WHERE id = NEW.jefaturaID;

        -- Si no se encuentra la JefaturaDeServicio, generar un error
        IF jefaturaServicioID IS NULL THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'La JefaturaDeServicio especificada no existe.';
        END IF;

        -- Verificar si los servicioID coinciden
        IF NEW.servicioID != jefaturaServicioID THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'El servicioID del Empleado debe coincidir con el servicioID de la JefaturaDeServicio.';
        END IF;
    END IF;
    -- Si NEW.jefaturaID es NULL, no se realiza la validación
END$$
-- restablecemos el delimitador por defecto
DELIMITER ;


-- Tabla Autorizacion: para la clase Autorizacion
CREATE TABLE Autorizacion (
    id BINARY(16) PRIMARY KEY,
    fechaAutorizacion DATETIME NOT NULL,
    tipo ENUM('DIRECCION', 'JEFATURADESERVICIO', 'OFICINADEPERSONAL', 'USUARIO') NOT NULL,
    autorizadoPorID BINARY(16) NOT NULL,
    FOREIGN KEY (autorizadoPorID) REFERENCES Usuario(id)
);

-- Tabla EstadoTramite -- Representa a la clase EstadoTramite
CREATE TABLE EstadoTramite (
    id BINARY(16) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);
-- Inicializar Tabla EstadoTramite
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


-- Tabla Novedad -- Representa a la clase Novedad
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

-- Tabla Intermedia Empleado_Novedad (Por la novead 99, tenemos una relación mucho-a-muchos que salvaguardar)
CREATE TABLE Empleado_Novedad (
    empleadoID BINARY(16) NOT NULL,
    novedadID BINARY(16) NOT NULL,
    PRIMARY KEY (empleadoID, novedadID),
    FOREIGN KEY (empleadoID) REFERENCES Empleado(id),
    FOREIGN KEY (novedadID) REFERENCES Novedad(id)
);

-- Tabla JornadaLaboral: de la clase JornadaLaboral
CREATE TABLE JornadaLaboral (
    id BINARY(16) PRIMARY KEY,
    fechaIngreso DATETIME NOT NULL,
    fechaEgreso DATETIME NOT NULL
);

-- Tabla DiagramaDeServicio: para la clase DiagramaDeServicio
CREATE TABLE DiagramaDeServicio (
    id BINARY(16) PRIMARY KEY,
    estado VARCHAR(50) NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    servicioID BINARY(16) NOT NULL,
    FOREIGN KEY (servicioID) REFERENCES Servicio(id)
);

/* Tabla Planificacion: para el atributo 'planificaciones' de la clase DiagramaDeServicio
*  Relaciona DiagramaDeServicio, Empleado y JornadaLaboral.
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


-- Tabla FrancoCompensatorio: para la clase FrancoCompensatorio
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

-- Tabla HoraExtra: para clase HoraExtra
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


/* -- Tabla RegistroJornadaLaboral
 * Notas:
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



/* Extra:
 * Índices en Claves Foráneas:
 * Agregamos índices en las columnas que son claves foráneas y se utilizan en consultas frecuentes.
 */
CREATE INDEX idx_empleado_jefatura ON Empleado (jefaturaID);
CREATE INDEX idx_servicio_direccion ON Servicio (direccionID);
