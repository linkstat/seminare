-- Insertar servicios
INSERT INTO Servicio (idServicio, nombreServicio, agrupacion) 
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'Cardiología', 'medico'),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Toxicología', 'medico'),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Traumatología', 'medico'),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Enfermería', 'enfermeria'),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Administración', 'administrativo'),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Admisión', 'administrativo'),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Archivo', 'administrativo'),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Habilitación', 'administrativo');


-- Insertar horarios
INSERT INTO Horario (idHorario, tipoHorario, horasAsignadas)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), '7horas', 35),
       (UNHEX(REPLACE(UUID(), '-', '')), 'guardia12hs', 120),
       (UNHEX(REPLACE(UUID(), '-', '')), 'nocturno', 17);


-- Insertar empleados
INSERT INTO Empleado (idEmpleado, nombre, apellido, cuil, servicio_id, horario_id) 
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'Juan', 'Pérez', '20123456789', 
        (SELECT idServicio FROM Servicio WHERE nombreServicio = 'Cardiología'), 
        (SELECT idHorario FROM Horario WHERE tipoHorario = '7horas')),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Ana', 'García', '30123456789', 
        (SELECT idServicio FROM Servicio WHERE nombreServicio = 'Enfermería'), 
        (SELECT idHorario FROM Horario WHERE tipoHorario = 'guardia12hs')),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Luis', 'López', '40123456789', 
        (SELECT idServicio FROM Servicio WHERE nombreServicio = 'Administración'), 
        (SELECT idHorario FROM Horario WHERE tipoHorario = 'nocturno'));


-- Insertar horas extras
INSERT INTO HorasExtras (idHoraExtra, idEmpleado, cantidadHoras, fecha, estado)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 
        (SELECT idEmpleado FROM Empleado WHERE nombre = 'Juan'), 4, '2023-10-01', 'Pendiente'),
       (UNHEX(REPLACE(UUID(), '-', '')), 
        (SELECT idEmpleado FROM Empleado WHERE nombre = 'Ana'), 6, '2023-10-03', 'Aprobado');


-- Insertar novedades
INSERT INTO Novedad (idNovedad, descripcion, fecha, empleado_id, estado)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'Falta justificada por enfermedad', '2023-10-05', 
        (SELECT idEmpleado FROM Empleado WHERE nombre = 'Luis'), 'Pendiente'),
       (UNHEX(REPLACE(UUID(), '-', '')), 'Solicitud de franco compensatorio', '2023-10-06', 
        (SELECT idEmpleado FROM Empleado WHERE nombre = 'Ana'), 'Aprobado');


