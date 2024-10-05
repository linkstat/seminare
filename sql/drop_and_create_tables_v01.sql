-- Crear la base de datos 'aromito' (en caso de no existir)
CREATE DATABASE IF NOT EXISTS aromito;

-- Trabajaremos sobre la BD 'aromito'
USE aromito;

-- Tabla de Servicio
DROP TABLE IF EXISTS Servicio;
FLUSH TABLES;
CREATE TABLE Servicio (
    idServicio BINARY(16) PRIMARY KEY,
    nombreServicio VARCHAR(50),
    agrupacion ENUM('administrativo', 'medico', 'enfermeria', 'tecnica', 'servicio')
);

-- Tabla de Horario
DROP TABLE IF EXISTS Horario;
FLUSH TABLES;
CREATE TABLE Horario (
    idHorario BINARY(16) PRIMARY KEY,
    tipoHorario ENUM('7horas', '35semanales', 'guardia12hs', 'guardiaFeriantes', 'nocturno', 'abierto'),
    horasAsignadas INT
);

-- Tabla de Empleado
DROP TABLE IF EXISTS Empleado;
FLUSH TABLES;
CREATE TABLE Empleado (
    idEmpleado BINARY(16) PRIMARY KEY,
    nombre VARCHAR(60),
    apellido VARCHAR(60),
    cuil VARCHAR(11),
    servicio_id BINARY(16),
    horario_id BINARY(16),
    FOREIGN KEY (servicio_id) REFERENCES Servicio(idServicio),
    FOREIGN KEY (horario_id) REFERENCES Horario(idHorario)
);

-- Tabla de JefaturaDeServicio
DROP TABLE IF EXISTS JefaturaDeServicio;
FLUSH TABLES;
CREATE TABLE JefaturaDeServicio (
    idJefatura BINARY(16) PRIMARY KEY,
    idEmpleado BINARY(16), -- El jefe de servicio es un empleado
    servicio_id BINARY(16),
    FOREIGN KEY (idEmpleado) REFERENCES Empleado(idEmpleado),
    FOREIGN KEY (servicio_id) REFERENCES Servicio(idServicio)
);

-- Tabla de OficinaDePersonal
DROP TABLE IF EXISTS OficinaDePersonal;
FLUSH TABLES;
CREATE TABLE OficinaDePersonal (
    idOficina BINARY(16) PRIMARY KEY,
    nombre VARCHAR(50),
    jefe_id BINARY(16)
);

-- Tabla de HorasExtras
DROP TABLE IF EXISTS HorasExtras;
FLUSH TABLES;
CREATE TABLE HorasExtras (
    idHoraExtra BINARY(16) PRIMARY KEY,
    idEmpleado BINARY(16),
    cantidadHoras INT,
    fecha DATE,
    estado ENUM('Pendiente', 'Aprobado', 'Rechazado'),
    FOREIGN KEY (idEmpleado) REFERENCES Empleado(idEmpleado)
);

-- Tabla de Novedad
DROP TABLE IF EXISTS Novedad;
FLUSH TABLES;
CREATE TABLE Novedad (
    idNovedad BINARY(16) PRIMARY KEY,
    descripcion VARCHAR(255),
    fecha DATE,
    empleado_id BINARY(16),
    estado ENUM('pendiente', 'aprobado', 'rechazado'),
    FOREIGN KEY (empleado_id) REFERENCES Empleado(idEmpleado)
);

-- Tabla de DiagramaDeServicio
DROP TABLE IF EXISTS DiagramaDeServicio;
FLUSH TABLES;
CREATE TABLE DiagramaDeServicio (
    idDiagrama BINARY(16) PRIMARY KEY,
    fechaInicio DATE,
    fechaFin DATE,
    estado ENUM('Pendiente', 'Aprobado', 'Rechazado'),
    servicio_id BINARY(16),
    FOREIGN KEY (servicio_id) REFERENCES Servicio(idServicio)
);

-- Tabla de ParteDiario
DROP TABLE IF EXISTS ParteDiario;
FLUSH TABLES;
CREATE TABLE ParteDiario (
    idParte BINARY(16) PRIMARY KEY,
    fecha DATE,
    empleado_id BINARY(16),
    modificadoPor BINARY(16),
    FOREIGN KEY (empleado_id) REFERENCES Empleado(idEmpleado),
    FOREIGN KEY (modificadoPor) REFERENCES OficinaDePersonal(idOficina)
);
