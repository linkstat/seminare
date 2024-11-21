-- Inicializar variables
-- Password por defecto: 'Aromito1'
SET @initPass = '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO';

-- Seleccionar un ID de Domicilio de forma aleatoria


-- Crear usuario con roles: Director
SET @cuilDir=20112223331;
SET @usuarioId1=UUID_TO_BIN(UUID());
SET @fechaAltaDir='2015-03-01';
SET @tipoUsuarioDireccion='Direccion';
SELECT r.id INTO @rolDireccion FROM Rol r WHERE r.nombre LIKE @tipoUsuarioDireccion;
SELECT id INTO @domicilioId1 FROM Domicilio ORDER BY RAND() LIMIT 1;
SELECT id INTO @cargoDirectivoId FROM Cargo c WHERE c.numero = 200;
SELECT id INTO @servDireccionId FROM Servicio s WHERE s.nombre LIKE 'Dirección';

INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, domicilioID, cargoID, servicioID, tipoUsuario, passwd)
VALUES (@usuarioId1, @fechaAltaDir, TRUE, @cuilDir, 'Bonifacio', 'Aguirre', 'MASCULINO', 'bonifacio.a@hmu.com.ar', 3512223344, @domicilioId1, @cargoDirectivoId, @servDireccionId, @tipoUsuarioDireccion, @initPass);
INSERT INTO Direccion (id) VALUES (@usuarioId1);
INSERT INTO Usuario_Rol (usuario_id, rol_id) SELECT u.id, r.id FROM Usuario u JOIN Rol r ON r.nombre = @tipoUsuarioDireccion WHERE u.id = @usuarioId1;



-- Crear usuario con roles: JefaturaDeServicio
SET @cuilJefe=20112223332;
SET @usuarioId2=UUID_TO_BIN(UUID());
SET @fechaAltaJefe='2012-07-01';
SET @tipoUsuarioJefaturaDeServicio='JefaturaDeServicio';
SELECT r.id INTO @rolJefaturaDeServicio FROM Rol r WHERE r.nombre LIKE @tipoUsuarioJefaturaDeServicio;
SELECT id INTO @domicilioId2 FROM Domicilio ORDER BY RAND() LIMIT 1;
SELECT id INTO @cargoJefeId FROM Cargo c WHERE c.numero = 300;
SELECT id INTO @servDireccionId FROM Servicio s WHERE s.nombre LIKE 'Dirección';

INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, domicilioID, cargoID, servicioID, tipoUsuario, passwd)
VALUES (@usuarioId2, @fechaAltaJefe, TRUE, @cuilJefe, 'Valentino', 'Marquez', 'MASCULINO', 'vale.mar@hmu.com.ar', 3512223355, @domicilioId2, @cargoJefeId, @servDireccionId, @tipoUsuarioJefaturaDeServicio, @initPass);
INSERT INTO JefaturaDeServicio (id) VALUES (@usuarioId2);
INSERT INTO Usuario_Rol (usuario_id, rol_id) SELECT u.id, r.id FROM Usuario u JOIN Rol r ON r.nombre = @tipoUsuarioJefaturaDeServicio WHERE u.id = @usuarioId2;



-- Crear usuario con roles: OficinaDePersonal
SET @cuilOdeP=27112223333;
SET @usuarioId3=UUID_TO_BIN(UUID());
SET @fechaAltaOdeP='2010-12-15';
SET @tipoUsuarioOdeP='OficinaDePersonal';
SELECT r.id INTO @rolJefaturaDeServicio FROM Rol r WHERE r.nombre LIKE @tipoUsuarioJefaturaDeServicio;
SELECT id INTO @domicilioId3 FROM Domicilio ORDER BY RAND() LIMIT 1;
SELECT id INTO @cargoOdePId FROM Cargo c WHERE c.numero = 500;
SELECT id INTO @servPersonalId FROM Servicio s WHERE s.nombre LIKE 'Personal';

INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, domicilioID, cargoID, servicioID, tipoUsuario, passwd)
VALUES (@usuarioId3, @fechaAltaOdeP, TRUE, @cuilOdeP, 'Felicitas', 'Herrera', 'FEMENINO', 'feliherrra@hmu.com.ar', 3512223366, @domicilioId3, @cargoOdePId, @servPersonalId, @tipoUsuarioOdeP, @initPass);
INSERT INTO OficinaDePersonal (id, reportesGenerados) VALUES (@usuarioId3, 0);
INSERT INTO Usuario_Rol (usuario_id, rol_id) SELECT u.id, r.id FROM Usuario u JOIN Rol r ON r.nombre = @tipoUsuarioOdeP WHERE u.id = @usuarioId3;

SELECT INTO @rolId id FROM Rol WHERE nombre LIKE @tipoUsuarioJefaturaDeServicio;
DELETE FROM Usuario_Rol WHERE usuario_id = @usuarioId2 AND rol_id = @rolId;



-- Crear usuario con roles: Empleado
SET @cuilEmpleado=20112223334;
SET @usuarioId4=UUID_TO_BIN(UUID());
SET @fechaAltaEmpleado='2021-09-01';
SET @tipoUsuarioEmpleado='Empleado';
SELECT r.id INTO @rolEmpleado FROM Rol r WHERE r.nombre LIKE @tipoUsuarioEmpleado;
SELECT id INTO @domicilioId4 FROM Domicilio ORDER BY RAND() LIMIT 1;
SELECT id INTO @cargoEmpleado FROM Cargo c WHERE c.numero = 400;
SELECT id INTO @servInformaticaId FROM Servicio s WHERE s.nombre LIKE 'Informática';
SELECT id INTO @domId4 FROM Domicilio ORDER BY RAND() LIMIT 1;

INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, domicilioID, cargoID, servicioID, tipoUsuario, passwd)
VALUES (@usuarioId4, @fechaAltaEmpleado, TRUE, @cuilEmpleado, 'Luis', 'Masson', 'MASCULINO', 'lmasson@hmu.com.ar', 3512223377, @domicilioId4, @cargoEmpleado, @servInformaticaId, @tipoUsuarioEmpleado, @initPass);

SELECT jds.id INTO @jefeEmpleado FROM JefaturaDeServicio jds
JOIN Usuario jefe ON jds.id = jefe.id
JOIN Servicio s ON jefe.servicioID = s.id
WHERE s.nombre = 'Informática';

INSERT INTO Empleado (id, francosCompensatoriosUtilizados, jefaturaID) VALUES (@usuarioId4, 0, @jefeEmpleado);



-- Listar todas las asignaciones de roles
SELECT 'Aromito1' AS DefaultPass, u.cuil, u.apellidos, u.nombres, BIN_TO_UUID(u.id) AS usuario_id, BIN_TO_UUID(r.id) AS rol_id, r.nombre
FROM Usuario u
JOIN Usuario_Rol ur ON u.id = ur.usuario_id
JOIN Rol r ON ur.rol_id = r.id
ORDER BY apellidos;


SELECT cuil, apellidos, nombres, passwd from Usuario WHERE cuil = 27284644443;
