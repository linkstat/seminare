USE aromito;

-- Limpiar registros de las tablas

-- Deshabilitar las Restricciones de Claves Foráneas:
SET FOREIGN_KEY_CHECKS = 0;

-- Eliminar datos de las tablas
DELETE FROM RegistroJornadaLaboral;
DELETE FROM MarcacionEmpleado;
DELETE FROM ParteDiario_Empleado;
DELETE FROM ParteDiario;
DELETE FROM HoraExtra;
DELETE FROM FrancoCompensatorio;
DELETE FROM Memorandum_Autorizacion;
DELETE FROM Memorandum_Firmante;
DELETE FROM Memorandum_Destinatario;
DELETE FROM Memorandum;
DELETE FROM Planificacion;
DELETE FROM DiagramaDeServicio;
DELETE FROM JornadaLaboral;
DELETE FROM Empleado_Novedad;
DELETE FROM Novedad;
DELETE FROM EstadoTramite;
DELETE FROM Autorizacion;
DELETE FROM Empleado;
DELETE FROM OficinaDePersonal;
DELETE FROM JefaturaDeServicio;
DELETE FROM Servicio;
DELETE FROM Direccion;
DELETE FROM Usuario;
DELETE FROM Domicilio;
DELETE FROM Cargo;
DELETE FROM HorarioAbierto;
DELETE FROM HorarioJefeServicioGuardiaPasiva;
DELETE FROM HorarioGuardiaEnfermeria;
DELETE FROM HorarioGuardiaMedica;
DELETE FROM HorarioDXI;
DELETE FROM HorarioFeriante;
DELETE FROM HorarioNocturno;
DELETE FROM HorarioSemanal;
DELETE FROM HorarioEstandar;
DELETE FROM HorarioConFranquicia;
DELETE FROM Horario;
DELETE FROM HorarioBase;

-- Habilitar restricciones de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- Insertar domicilios para poder asignarselos a usuarios
INSERT INTO Domicilio (id, calle, numeracion, ciudad, provincia)
VALUES
    (UUID_TO_BIN(UUID()), 'Defensa', 1200, 'Córdoba', 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Lavalleja', 3050, 'Córdoba', 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Av Belgrano Sur', 134, 'La Rioja', 'La Rioja'),
    (UUID_TO_BIN(UUID()), 'Av. Marcelo T. de Alvear', 120, 'Córdoba', 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Av. Libertador', 1450, 'Alta Gracia', 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Catamarca', 441, 'Córdoba', 'Córdoba'),
    (UUID_TO_BIN(UUID()), 'Junín', FLOOR(RAND() * 1000 + 1), 'Rosario', 'Santa Fe'),
    (UUID_TO_BIN(UUID()), 'Mate de Luna', FLOOR(RAND() * 1000 + 1), 'Famaillá', 'Tucumán'),
    (UUID_TO_BIN(UUID()), 'Gobernación', FLOOR(RAND() * 1000 + 1), 'Santa Fe', 'Santa Fe'),
    (UUID_TO_BIN(UUID()), 'Güemes', FLOOR(RAND() * 1000 + 1), 'Iruya', 'Jujuy'),
    (UUID_TO_BIN(UUID()), '9 de julio', FLOOR(RAND() * 1000 + 1), 'Corrientes', 'Corrientes'),
    (UUID_TO_BIN(UUID()), 'Av. del Trabajo', FLOOR(RAND() * 1000 + 1), 'Rosario', 'Santa Fe'),
    (UUID_TO_BIN(UUID()), 'Calle del Río', FLOOR(RAND() * 1000 + 1), 'Paraná', 'Entre Ríos'),
    (UUID_TO_BIN(UUID()), 'Av. Central', FLOOR(RAND() * 1000 + 1), 'Salta', 'Salta'),
    (UUID_TO_BIN(UUID()), 'Av. Siempre Viva', FLOOR(RAND() * 1000 + 1), 'Rawson', 'Chubut'),
    (UUID_TO_BIN(UUID()), 'San Martín', FLOOR(RAND() * 1000 + 1), 'Bariloche', 'Río Negro');

-- Obtener el ID del domicilio del HMU (Catamarca 441)
SELECT id INTO @HMUdomID
FROM Domicilio
WHERE calle = 'Catamarca' AND numeracion = 441 AND ciudad = 'Córdoba' AND provincia = 'Córdoba'
LIMIT 1;
-- Opcional: verificar que se obtuvo el ID correctamente
SELECT BIN_TO_UUID2(@HMUdomID) AS domicilioHMU;

-- Obtener el ID del domicilio del HIM (Lavalleja 3050)
SELECT id INTO @HIMdomID
FROM Domicilio
WHERE calle = 'Lavalleja' AND numeracion = 3050 AND ciudad = 'Córdoba' AND provincia = 'Córdoba'
LIMIT 1;
-- Opcional: verificar que se obtuvo el ID correctamente
SELECT BIN_TO_UUID2(@HIMdomID) AS domicilioHIM;

-- Generar UUIDs y almacenarlos en variables, para luego usar con JefaturaDeServicios
SET @JefeAdmisionID = UUID_TO_BIN(UUID());
SET @JefeBiomedicaID = UUID_TO_BIN(UUID());
SET @JefeCardiologiaID = UUID_TO_BIN(UUID());
SET @JefeCirugiaID = UUID_TO_BIN(UUID());
SET @JefeDxiID = UUID_TO_BIN(UUID());
SET @JefeEnfermeriaID = UUID_TO_BIN(UUID());
SET @JefeEsterilizacionID = UUID_TO_BIN(UUID());
SET @JefeFarmaciaID = UUID_TO_BIN(UUID());
SET @JefeHabilitacionID = UUID_TO_BIN(UUID());
SET @JefeInformaticaID = UUID_TO_BIN(UUID());
SET @JefeNutricionID = UUID_TO_BIN(UUID());
SET @JefeTraumatoID = UUID_TO_BIN(UUID());
SET @JefeToxicologiaID = UUID_TO_BIN(UUID());

-- Generar UUIDs y almacenarlos en variables, para luego usar con Servicios
SET @AdmisionID = UUID_TO_BIN(UUID());
SET @BiomedicaID = UUID_TO_BIN(UUID());
SET @CardiologiaID = UUID_TO_BIN(UUID());
SET @CirugiaID = UUID_TO_BIN(UUID());
SET @DxiID = UUID_TO_BIN(UUID());
SET @EnfermeriaID = UUID_TO_BIN(UUID());
SET @EsterilizacionID = UUID_TO_BIN(UUID());
SET @FarmaciaID = UUID_TO_BIN(UUID());
SET @HabilitacionID = UUID_TO_BIN(UUID());
SET @InformaticaID = UUID_TO_BIN(UUID());
SET @NutricionID = UUID_TO_BIN(UUID());
SET @TraumatoID = UUID_TO_BIN(UUID());
SET @ToxicologiaID = UUID_TO_BIN(UUID());

-- Generar una contraseña inicial usando Bcrypt
/* Por defecto, utlizaremos la contraseña Aromito2024
 * En GNU/Linux, la herramienta 'htpasswd' (parte de Apache HTTP Server tools), genera un hash de bcrypt con:
 *
 *   htpasswd -bnBC 10 "" "miContraseñaSegura123" | tr -d ':\n'
 * 
 * De forma online, Bcrypt-Generator.com (en https://bcrypt-generator.com/ ), puede generar contraseñas.
 * String: Aromito1
 * Rounds: 12
 */
SET @initPass = '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO';


-- Insertar en Usuario
INSERT INTO Usuario (id, nombres, apellidos, mail, cuil, fechaAlta, sexo, estado, domicilioID, tipoUsuario, passwd)
VALUES
    (UUID_TO_BIN(UUID()), 'Mariano Gustavo', 'Marino', 'direccion@hmu.com.ar', 20224448885, CURDATE(), 'MASCULINO', TRUE, @HMUdomID, 'Direccion', @initPass),
    (UUID_TO_BIN(UUID()), 'Federico', 'Huergo Sánchez', 'fedesubdir@hmu.com.ar', 20259993331, CURDATE(), 'MASCULINO', TRUE, @HIMdomID, 'Direccion', @initPass),
    (UUID_TO_BIN(UUID()), 'Gloria', 'Longoni', 'subdirectora@hmu.com.ar', 27129997773, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio WHERE ciudad = 'Alta Gracia' LIMIT 1), 'Direccion', @initPass),
    (UUID_TO_BIN(UUID()), 'Fabricio', 'Vitali', 'subdireccion@hmu.com.ar', 20239997772, CURDATE(), 'MASCULINO', TRUE, @HMUdomID, 'Direccion', @initPass),
    (@JefeAdmisionID, 'Andrea', 'Balconte', 'andrea@hmu.com.ar', 27295554447, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeBiomedicaID, 'Silvina', 'Maestro', 'smaestro@hmu.com.ar', 27274422442, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeCardiologiaID, 'Juan', 'Aniceto', 'janiceto@hmu.com.ar', 20124543421, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeCirugiaID, 'Maximiliano', 'Titarelli', 'drtita@hmu.com.ar', 20268944448, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeDxiID, 'Juan Ignacio', 'Morales', 'jimorales@hmu.com.ar', 20281324547, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeEnfermeriaID, 'Tania', 'Plaza', 'tplaza@hmu.com.ar', 27224444445, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeEsterilizacionID, 'Matías', 'Pérez Cabral', 'mcp@hmu.com.ar', 20289445441, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeFarmaciaID, 'María Pía', 'Arancibia', 'piaarancibia@hmu.com.ar', 27254344447, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeHabilitacionID, 'Carlos Fernando', 'Roberts', 'ferroberts@hmu.com.ar', 20276444446, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeInformaticaID, 'Juan Manuel', 'Roqué', 'jmroque@hmu.com.ar', 20220361118, CURDATE(), 'OTRO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeNutricionID, 'Alejandra', 'Boqué', 'aleboque@hmu.com.ar', 27174444446, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeTraumatoID, 'Omar Wenceslao', 'Sánchez', 'owsanchez@hmu.com.ar', 20124644445, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (@JefeToxicologiaID, 'Andrea', 'Vilkelis', 'avilkelis@hmu.com.ar', 27214434447, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'JefaturaDeServicio', @initPass),
    (UUID_TO_BIN(UUID()), 'Florencia', 'Maurino', 'flormaurino@hmu.com.ar', 27284644443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'OficinaDePersonal', @initPass),
    (UUID_TO_BIN(UUID()), 'Baltazar', 'Garzón', 'baltig@hmu.com.ar', 20554644448, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Pedro', 'Taborda', 'pltaborda@hmu.com.ar', 20154786445, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'María Laura', 'Vargas Ruíz', 'mlvargas@hmu.com.ar', 27554664563, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'María Celeste', 'Vignetta', 'mcv@hmu.com.ar', 27554685443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Alicia', 'Vivas', 'baltig@hmu.com.ar', 27554567443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Matías Quique', 'Cnga Castellanos', 'lestat@hmu.com.ar', 20554649743, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Mauricio Elio', 'Garay', 'baltig@hmu.com.ar', 24574741745, CURDATE(), 'OTRO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Eva Patricia', 'Usandivares', 'epu@hmu.com.ar', 27556786453, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Edith', 'Tolay', 'etolay@hmu.com.ar', 20454676443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'José Luis', 'Terrieris', 'jterrieris@hmu.com.ar', 20554543453, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Claudia', 'Tarifa', 'clautarifa@hmu.com.ar', 27540678443, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Erica', 'Tapia', 'erikatapia@hmu.com.ar', 27554560863, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Demetrio', 'Tampares', 'demetam@hmu.com.ar', 24784044443, CURDATE(), 'OTRO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Alejandro', 'Suizer', 'amsuizer@hmu.com.ar', 20526456443, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'David', 'Suárez', 'dsuarez@hmu.com.ar', 20554897343, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Soledad', 'Romero', 'soleromero@hmu.com.ar', 27864567893, CURDATE(), 'FEMENINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Pedro', 'Rius', 'pedrorius@hmu.com.ar', 20554674843, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Damian', 'Rabbat', 'drabbat@hmu.com.ar', 20558244443, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Ismael', 'Puig', 'ipuig@hmu.com.ar', 20567974443, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Diego', 'Pascolo', 'dpascolo@hmu.com.ar', 20456645843, CURDATE(), 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY RAND() LIMIT 1), 'Empleado', @initPass),
    (UUID_TO_BIN(UUID()), 'Sebastián', 'Bustos', 'sebustos@hmu.com.ar', 24554978443, CURDATE(), 'OTRO', TRUE, (SELECT id FROM Domicilio WHERE calle = 'Av. Siempre Viva' LIMIT 1), 'Empleado', @initPass);

-- Insertar en la tabla 'Direccion' utilizando los 'id' guardados previamente para esos usuarios
INSERT INTO Direccion (id)
SELECT id FROM Usuario WHERE tipoUsuario = 'Direccion';

-- Insertar en Oficina de Personal
INSERT INTO OficinaDePersonal (id, reportesGenerados)
VALUES ( (SELECT id FROM Usuario WHERE tipoUsuario = 'OficinaDePersonal' LIMIT 1), 0 );

-- Obtener el ID del actual director (cuyo apellido es 'Marino')
SELECT D.id INTO @dirID
FROM Direccion D
JOIN Usuario U ON D.id = U.id
WHERE U.apellidos = 'Marino' AND U.tipoUsuario = 'Direccion'
LIMIT 1;
-- Opcionalmente, verificar que se obtuvo el ID correctamente
SELECT BIN_TO_UUID2(@dirID) AS dirID;

-- Obtener el ID del actual subdirector (cuyo apellido es 'Vitali')
SELECT D.id INTO @subdirID
FROM Direccion D
JOIN Usuario U ON D.id = U.id
WHERE U.apellidos = 'Vitali' AND U.tipoUsuario = 'Direccion'
LIMIT 1;
-- Opcionalmente, verificar que se obtuvo el ID correctamente
SELECT BIN_TO_UUID2(@subdirID) AS subdirID;

-- Insertar Servicios
INSERT INTO Servicio (id, nombre, agrupacion, direccionID)
VALUES
    (@AdmisionID, 'Admisión', 'ADMINISTRATIVO', @subdirID),
    (@BiomedicaID, 'Biomédica', 'TECNICO', @subdirID),
    (@CardiologiaID, 'Cardiología', 'MEDICO', @dirID),
    (@CirugiaID, 'Cirugía', 'MEDICO', @dirID),
    (@DxiID, 'Diagnóstico por imágenes', 'MEDICO', @dirID),
    (@EnfermeriaID, 'Enfermería', 'ENFERMERIA', @dirID),
    (@EsterilizacionID, 'Esterilización', 'SERVICIO', @subdirID),
    (@FarmaciaID, 'Farmacia', 'SERVICIO', @subdirID),
    (@HabilitacionID, 'Habilitación', 'ADMINISTRATIVO', @dirID),
    (@InformaticaID, 'Informática', 'TECNICO', @subdirID),
    (@NutricionID, 'Nutrición', 'SERVICIO', @subdirID),
    (@TraumatoID, 'Traumatología', 'MEDICO', @dirID),
    (@ToxicologiaID, 'Toxicología', 'MEDICO', @dirID);


-- Insertar en JefaturaDeServicio
INSERT INTO JefaturaDeServicio (id, servicioID)
VALUES
    (@JefeAdmisionID, @AdmisionID),
    (@JefeBiomedicaID, @BiomedicaID),
    (@JefeCardiologiaID, @CardiologiaID),
    (@JefeCirugiaID, @CirugiaID),
    (@JefeDxiID, @DxiID),
    (@JefeEnfermeriaID, @EnfermeriaID),
    (@JefeEsterilizacionID, @EsterilizacionID),
    (@JefeFarmaciaID, @FarmaciaID),
    (@JefeHabilitacionID, @HabilitacionID),
    (@JefeInformaticaID, @InformaticaID),
    (@JefeNutricionID, @NutricionID),
    (@JefeTraumatoID, @TraumatoID),
    (@JefeToxicologiaID, @ToxicologiaID);


-- Desasignar todos los Empleados de sus servicios
UPDATE Empleado
SET servicioID = NULL;

-- Insertar solamente nuevos Empleados (aquellos que aún no están en la tabla 'Empleado', pero si en 'Usuario')
INSERT INTO Empleado (id, francosCompensatoriosUtilizados, horarioActualID, jefaturaID, servicioID)
SELECT
    U.id AS id,
    0 AS francosCompensatoriosUtilizados,
    NULL AS horarioActualID,
    @jefaturaID := (
        SELECT J.id
        FROM JefaturaDeServicio J
        ORDER BY RAND()
        LIMIT 1
    ) AS jefaturaID,
    (SELECT J.servicioID FROM JefaturaDeServicio J WHERE J.id = @jefaturaID) AS servicioID
FROM Usuario U
LEFT JOIN Empleado E ON U.id = E.id
WHERE
    U.tipoUsuario = 'Empleado' AND
    E.id IS NULL
LIMIT 20;


-- Insertar Empleados (actualizando registros existentes o reemplazámdolos)
REPLACE INTO Empleado (id, francosCompensatoriosUtilizados, horarioActualID, jefaturaID, servicioID)
SELECT
    U.id AS id,
    0 AS francosCompensatoriosUtilizados,
    NULL AS horarioActualID,
    @jefaturaID := (
        SELECT J.id
        FROM JefaturaDeServicio J
        ORDER BY RAND()
        LIMIT 1
    ) AS jefaturaID,
    (SELECT J.servicioID FROM JefaturaDeServicio J WHERE J.id = @jefaturaID) AS servicioID
FROM Usuario U
WHERE U.tipoUsuario = 'Empleado'
LIMIT 20;



-- Listar usuarios Empleados
SELECT
    BIN_TO_UUID2(E.id) AS empleado_id,
    U.apellidos,
    U.nombres,
    U.cuil,
    U.mail,
    U.tel,
    S.nombre AS area_de_servicio
FROM Empleado E
JOIN Usuario U ON E.id = U.id
JOIN Servicio S ON E.servicioID = S.id;



-- Listar usuarios de tipo Direccion cargados
SELECT
    BIN_TO_UUID2(id) AS usuario_id,
    nombres,
    apellidos,
    mail,
    cuil,
    fechaAlta,
    sexo,
    estado,
    tipoUsuario
FROM Usuario
WHERE tipoUsuario = 'Direccion';


-- Visualizar Servicios a cargo del Director
SELECT
    BIN_TO_UUID2(S.id) AS servicio_id,
    S.nombre AS servicio,
    S.agrupacion,
    BIN_TO_UUID2(S.direccionID) AS direccion_id,
    U.nombres AS director_nombres,
    U.apellidos AS director_apellidos
FROM Servicio S
JOIN Direccion D ON S.direccionID = D.id
JOIN Usuario U ON D.id = U.id
WHERE U.apellidos = 'Marino' AND U.tipoUsuario = 'Direccion';


-- Visualizar Servicios a cargo del Subdirector
SELECT
    BIN_TO_UUID2(S.id) AS servicio_id,
    S.nombre AS servicio,
    S.agrupacion,
    BIN_TO_UUID2(S.direccionID) AS direccion_id,
    U.nombres AS director_nombres,
    U.apellidos AS director_apellidos
FROM Servicio S
JOIN Direccion D ON S.direccionID = D.id
JOIN Usuario U ON D.id = U.id
WHERE U.apellidos = 'Vitali' AND U.tipoUsuario = 'Direccion';


-- Listar asignaciones de Jefes a Servicios y Director superior
SELECT
    BIN_TO_UUID2(J.servicioID) AS servicio_id,
    S.nombre AS servicio_nombre,
    CONCAT(UJ.nombres, ' ', UJ.apellidos) AS jefe_de_servicio,
    UJ.mail AS jefe_mail,
    UJ.fechaAlta AS jefe_fecha_alta,
    CONCAT(UD.nombres, ' ', UD.apellidos) AS director,
    UD.mail AS director_mail,
    UD.fechaAlta AS director_fecha_alta
FROM
    JefaturaDeServicio J
JOIN Usuario UJ ON J.id = UJ.id
JOIN Servicio S ON J.servicioID = S.id
JOIN Usuario UD ON S.direccionID = UD.id;


-- Contar la cantidad de empleados de cada servicio (incluiso sus jefes)
SELECT
    S.nombre AS servicio_nombre,
    COUNT(U.id) AS total_empleados
FROM
    Servicio S
LEFT JOIN (
    -- Combinar empleados y jefes de servicio
    SELECT E.id, E.servicioID
    FROM Empleado E
    UNION ALL
    SELECT J.id, J.servicioID
    FROM JefaturaDeServicio J
) U ON S.id = U.servicioID
GROUP BY S.id, S.nombre
ORDER BY total_empleados DESC;



-- Agregar/modificar el teléfono a un usuario cualquiera
-- Por ejemeplo, elijiremos modificarle el teléfono al jefe de Informática (que ya teníamos almacenado en @JefeInformaticaID)
-- Actualizamos el teléfono del usuario
UPDATE Usuario
SET tel = '3517553799'
WHERE id = @JefeInformaticaID;
-- Verificamos nuevo valor:
SELECT nombres, apellidos, tel FROM Usuario WHERE id = @JefeInformaticaID;



-- Borrar un usuario
-- Supongamos que el usuario a eliminar es 'Soledad Romero'
SET @usuarioIDEliminar = (SELECT id FROM Usuario WHERE apellidos = 'Romero' AND nombres = 'Soledad' LIMIT 1);
-- Verificar de qué tipo de usuario se trata
SELECT tipoUsuario
FROM Usuario
WHERE id = @usuarioIDEliminar;

-- Eliminación de tablas específicas
-- Si es Empleado
DELETE FROM Empleado WHERE id = @usuarioIDEliminar;

-- Si es JefaturaDeServicio
DELETE FROM JefaturaDeServicio WHERE id = @usuarioIDEliminar;

-- Si es Dirección
DELETE FROM Direccion WHERE id = @usuarioIDEliminar;

-- Si está en OficinaDePersonal
DELETE FROM OficinaDePersonal WHERE id = @usuarioIDEliminar;

-- Finalmente, eliminar de la tabla Usuario
DELETE FROM Usuario WHERE id = @usuarioIDEliminar;
-- Opcionalmente, si el usuario tuviera registros en otras tablas (como Planificacion, Novedad, etc.), también los eliminaríamos (a fines de evitar inconsistencias)
-- No es el caso

