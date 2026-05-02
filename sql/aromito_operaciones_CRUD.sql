/* ============================================================================
 * Aromito - Operaciones CRUD de inicialización y prueba (PostgreSQL)
 *
 * Este script:
 *  1. Limpia los registros de todas las tablas (preservando schema y la
 *     seed data de EstadoTramite del DDL).
 *  2. Inserta domicilios, cargos, servicios y usuarios reales del HMU para
 *     uso de demo y pruebas funcionales.
 *  3. Asigna roles a los usuarios.
 *  4. Demuestra algunos UPDATE/DELETE típicos.
 *
 * Estrategia de migración desde MariaDB:
 *  - Se eliminaron las variables `SET @x`. En su lugar, se usan subqueries
 *    con claves naturales (apellidos+nombres, numero de cargo, nombre de
 *    servicio). Es más verboso pero más explícito y robusto.
 *  - El ciclo Usuario.servicioID → Servicio.direccionID → Empleado.id se
 *    rompe insertando primero Usuarios sin servicioID, luego Empleados,
 *    luego Servicios, y finalmente UPDATE Usuario SET servicioID. PostgreSQL
 *    no tiene un equivalente directo de SET FOREIGN_KEY_CHECKS = 0, y dejar
 *    el FK como DEFERRABLE complica el modelo: se prefiere romper el ciclo
 *    a nivel de datos.
 *  - CURDATE() → CURRENT_DATE; UUID() → gen_random_uuid(); RAND() → random();
 *    UUID_TO_BIN/BIN_TO_UUID se eliminan (UUID es tipo nativo).
 * ========================================================================== */


-- ----------------------------------------------------------------------------
-- 1) Limpieza de registros (preserva EstadoTramite — la seedea el DDL)
-- ----------------------------------------------------------------------------
TRUNCATE TABLE
    RegistroJornadaLaboral, MarcacionEmpleado, ParteDiario_Empleado, ParteDiario,
    HoraExtra, FrancoCompensatorio,
    Memorandum_Autorizacion, Memorandum_Firmante, Memorandum_Destinatario, Memorandum,
    Planificacion, DiagramaDeServicio, JornadaLaboral,
    Empleado_Novedad, Novedad, Autorizacion,
    Usuario_Rol, Rol,
    Servicio_JefaturaDeServicio,
    Empleado, Usuario, Servicio,
    Domicilio, Cargo,
    HorarioAbierto, HorarioJefeServicioGuardiaPasiva, HorarioGuardiaEnfermeria,
    HorarioGuardiaMedica, HorarioDXI, HorarioFeriante, HorarioNocturno,
    HorarioSemanal, HorarioEstandar, HorarioConFranquicia, Horario, HorarioBase
RESTART IDENTITY CASCADE;


-- ----------------------------------------------------------------------------
-- 2) Domicilios de prueba
-- ----------------------------------------------------------------------------
INSERT INTO Domicilio (id, calle, numeracion, barrio, ciudad, localidad, provincia)
VALUES
    (gen_random_uuid(), 'Defensa', 1200, NULL, 'Córdoba', NULL, 'Córdoba'),
    (gen_random_uuid(), 'Lavalleja', 3050, NULL, 'Córdoba', NULL, 'Córdoba'),
    (gen_random_uuid(), 'Av Belgrano Sur', 134, NULL, 'La Rioja', NULL, 'La Rioja'),
    (gen_random_uuid(), 'Av. Marcelo T. de Alvear', 120, NULL, 'Córdoba', NULL, 'Córdoba'),
    (gen_random_uuid(), 'Av. Libertador', 1450, NULL, 'Alta Gracia', NULL, 'Córdoba'),
    (gen_random_uuid(), 'Catamarca', 441, NULL, 'Córdoba', NULL, 'Córdoba'),
    (gen_random_uuid(), 'Junín', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Rosario', NULL, 'Santa Fe'),
    (gen_random_uuid(), 'Mate de Luna', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Famaillá', NULL, 'Tucumán'),
    (gen_random_uuid(), 'Gobernación', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Santa Fe', NULL, 'Santa Fe'),
    (gen_random_uuid(), 'Güemes', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Iruya', NULL, 'Jujuy'),
    (gen_random_uuid(), '9 de julio', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Corrientes', NULL, 'Corrientes'),
    (gen_random_uuid(), 'Av. del Trabajo', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Rosario', NULL, 'Santa Fe'),
    (gen_random_uuid(), 'Calle del Río', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Paraná', NULL, 'Entre Ríos'),
    (gen_random_uuid(), 'Av. Central', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Salta', NULL, 'Salta'),
    (gen_random_uuid(), 'Av. Siempre Viva', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Rawson', NULL, 'Chubut'),
    (gen_random_uuid(), 'San Martín', FLOOR(random() * 1000 + 1)::INTEGER, NULL, 'Bariloche', NULL, 'Río Negro');


-- ----------------------------------------------------------------------------
-- 3) Cargos
-- ----------------------------------------------------------------------------
INSERT INTO Cargo (id, numero, descripcion, agrupacion) VALUES
    (gen_random_uuid(), 0,    'Cargo sin definir',                       'INDEFINIDO'),
    (gen_random_uuid(), 200,  'Directivos',                              'PLANTAPOLITICA'),
    (gen_random_uuid(), 300,  'Jefaturas de Servicio',                   'JEFATURA'),
    (gen_random_uuid(), 400,  'Técnicos',                                'TECNICO'),
    (gen_random_uuid(), 500,  'Administrativos',                         'ADMINISTRATIVO'),
    (gen_random_uuid(), 600,  'Servicios',                               'SERVICIO'),
    (gen_random_uuid(), 700,  'Enfermería Técnicos',                     'ENFERMERIA'),
    (gen_random_uuid(), 800,  'Médicos',                                 'MEDICO'),
    (gen_random_uuid(), 900,  'Enfermería Profesionales',                'ENFERMERIA'),
    (gen_random_uuid(), 1200, 'Profesionales sin agrupación específica', 'PROFESIONAL');


-- ----------------------------------------------------------------------------
-- 4) Usuarios
--
-- Se insertan SIN servicioID para romper el ciclo Usuario↔Servicio↔Empleado.
-- El servicioID se asigna después de que existan los Servicios (paso 7).
--
-- Los hashes son BCrypt (rounds=12). El primer login los re-hashea a Argon2id.
-- ----------------------------------------------------------------------------
INSERT INTO Usuario (id, nombres, apellidos, mail, cuil, fechaAlta, sexo, estado, domicilioID, passwd) VALUES
    -- Dirección (4 personas)
    (gen_random_uuid(), 'Mariano Gustavo', 'Marino',         'direccion@hmu.com.ar',    20224448885, CURRENT_DATE, 'MASCULINO', TRUE,
        (SELECT id FROM Domicilio WHERE calle = 'Catamarca' AND numeracion = 441 LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Federico',        'Huergo Sánchez', 'fedesubdir@hmu.com.ar',   20259993331, CURRENT_DATE, 'MASCULINO', TRUE,
        (SELECT id FROM Domicilio WHERE calle = 'Lavalleja' AND numeracion = 3050 LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Gloria',          'Longoni',        'subdirectora@hmu.com.ar', 27129997773, CURRENT_DATE, 'FEMENINO',  TRUE,
        (SELECT id FROM Domicilio WHERE ciudad = 'Alta Gracia' LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Fabricio',        'Vitali',         'subdireccion@hmu.com.ar', 20239997772, CURRENT_DATE, 'MASCULINO', TRUE,
        (SELECT id FROM Domicilio WHERE calle = 'Catamarca' AND numeracion = 441 LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),

    -- Jefaturas de Servicio (13 personas)
    (gen_random_uuid(), 'Andrea',          'Balconte',       'andrea@hmu.com.ar',       27295554447, CURRENT_DATE, 'FEMENINO',  TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Silvina',         'Maestro',        'smaestro@hmu.com.ar',     27274422442, CURRENT_DATE, 'FEMENINO',  TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Maximiliano',     'Titarelli',      'drtita@hmu.com.ar',       20268944448, CURRENT_DATE, 'MASCULINO', TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Juan Ignacio',    'Morales',        'jimorales@hmu.com.ar',    20281324547, CURRENT_DATE, 'MASCULINO', TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Tania',           'Plaza',          'tplaza@hmu.com.ar',       27224444445, CURRENT_DATE, 'FEMENINO',  TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Matías',          'Pérez Cabral',   'mcp@hmu.com.ar',          20289445441, CURRENT_DATE, 'MASCULINO', TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'María Pía',       'Arancibia',      'piaarancibia@hmu.com.ar', 27254344447, CURRENT_DATE, 'FEMENINO',  TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Carlos Fernando', 'Roberts',        'ferroberts@hmu.com.ar',   20276444446, CURRENT_DATE, 'MASCULINO', TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Juan Manuel',     'Roqué',          'jmroque@hmu.com.ar',      20220361118, CURRENT_DATE, 'OTRO',      TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Alejandra',       'Boqué',          'aleboque@hmu.com.ar',     27174444446, CURRENT_DATE, 'FEMENINO',  TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Omar Wenceslao',  'Sánchez',        'owsanchez@hmu.com.ar',    20124644445, CURRENT_DATE, 'MASCULINO', TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Andrea',          'Vilkelis',       'avilkelis@hmu.com.ar',    27214434447, CURRENT_DATE, 'FEMENINO',  TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),

    -- Oficina de Personal (Maurino — también es jefa de servicio)
    (gen_random_uuid(), 'Florencia',       'Maurino',        'flormaurino@hmu.com.ar',  27284644443, CURRENT_DATE, 'FEMENINO',  TRUE,
        (SELECT id FROM Domicilio ORDER BY random() LIMIT 1),
        '$2a$12$TCAy.mqVsaRIwjQs0imr/uD0xNoUq/W3LSVys3EISI5hdBREwZW5a'),

    -- Empleados (no jefaturas, no dirección)
    (gen_random_uuid(), 'Juan',            'Aniceto',           'janiceto@hmu.com.ar',    20124543421, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Baltazar',        'Garzón',            'baltig@hmu.com.ar',      20554644448, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Pedro',           'Taborda',           'pltaborda@hmu.com.ar',   20154786445, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'María Laura',     'Vargas Ruíz',       'mlvargas@hmu.com.ar',    27554664563, CURRENT_DATE, 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'María Celeste',   'Vignetta',          'mcv@hmu.com.ar',         27554685443, CURRENT_DATE, 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Alicia',          'Vivas',             'avivas@hmu.com.ar',      27554567443, CURRENT_DATE, 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Matías Quique',   'Canga Castellanos', 'lestat@hmu.com.ar',      20554649743, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Mauricio Elio',   'Garay',             'mgaray@hmu.com.ar',      24574741745, CURRENT_DATE, 'OTRO',      TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Eva Patricia',    'Usandivares',       'epu@hmu.com.ar',         27556786453, CURRENT_DATE, 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Edith',           'Tolay',             'etolay@hmu.com.ar',      20454676443, CURRENT_DATE, 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'José Luis',       'Terrieris',         'jterrieris@hmu.com.ar',  20554543453, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Claudia',         'Tarifa',            'clautarifa@hmu.com.ar',  27540678443, CURRENT_DATE, 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Erica',           'Tapia',             'erikatapia@hmu.com.ar',  27554560863, CURRENT_DATE, 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Demetrio',        'Tampares',          'demetam@hmu.com.ar',     24784044443, CURRENT_DATE, 'OTRO',      TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Alejandro',       'Suizer',            'amsuizer@hmu.com.ar',    20526456443, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'David',           'Suárez',            'dsuarez@hmu.com.ar',     20554897343, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Soledad',         'Romero',            'soleromero@hmu.com.ar',  27864567893, CURRENT_DATE, 'FEMENINO',  TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Pedro',           'Rius',              'pedrorius@hmu.com.ar',   20554674843, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Damian',          'Rabbat',            'drabbat@hmu.com.ar',     20558244443, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Ismael',          'Puig',              'ipuig@hmu.com.ar',       20567974443, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Diego',           'Pascolo',           'dpascolo@hmu.com.ar',    20456645843, CURRENT_DATE, 'MASCULINO', TRUE, (SELECT id FROM Domicilio ORDER BY random() LIMIT 1), '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO'),
    (gen_random_uuid(), 'Sebastián',       'Bustos',            'sebustos@hmu.com.ar',    24554978443, CURRENT_DATE, 'OTRO',      TRUE,
        (SELECT id FROM Domicilio WHERE calle = 'Av. Siempre Viva' LIMIT 1),
        '$2a$12$x9SDga8sk3DyjvdeIwkhl.2e9wcWHewfHEFQFKkFp1.FiAKcyRZUG');


-- ----------------------------------------------------------------------------
-- 5) Empleado: insertar a TODOS los Usuarios
--
-- Convención: todo Usuario del hospital es también Empleado (tiene horario,
-- francos, etc.), incluso si su rol principal es jefatura/dirección/oficina.
-- El "tipo" se discrimina por roles asignados (Usuario_Rol), no por presencia
-- en sub-tablas.
-- ----------------------------------------------------------------------------
INSERT INTO Empleado (id, francosCompensatoriosUtilizados, horarioActualID)
SELECT id, 0, NULL FROM Usuario;


-- ----------------------------------------------------------------------------
-- 6) Servicios
--
-- direccionID se resuelve con subquery contra Usuario por nombre+apellido.
-- ----------------------------------------------------------------------------
INSERT INTO Servicio (id, nombre, agrupacion, direccionID) VALUES
    (gen_random_uuid(), 'Administración',             'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Admisión',                   'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Anatomía Patológica',        'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Anestesia',                  'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Auditoría Médica',           'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Biomédica',                  'TECNICO',        (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Bioquímica',                 'SERVICIO',       (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Camilleros',                 'SERVICIO',       (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Capacitación y Docencia',    'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Cirugía',                    'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Cirugía Plástica',           'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Clínica Médica',             'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Diagnóstico por Imágenes',   'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Dirección',                  'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Enfermería',                 'ENFERMERIA',     (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Esterilización',             'SERVICIO',       (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Facturación',                'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Farmacia',                   'SERVICIO',       (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Habilitación',               'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Hemoterapia',                'SERVICIO',       (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Informática',                'TECNICO',        (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Instrumentación Quirúrgica', 'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Kinesiología',               'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Laboratorio',                'SERVICIO',       (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Lavadero',                   'SERVICIO',       (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Mantenimiento',              'TECNICO',        (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Medicina Legal',             'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Neurocirugía',               'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Nutrición',                  'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Personal',                   'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Quirófano',                  'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Registro Médico (Archivo)',  'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Salud Mental',               'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Secretaría Técnica',         'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Servicio Social',            'ADMINISTRATIVO', (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Tecnoeléctrica',             'TECNICO',        (SELECT id FROM Usuario WHERE apellidos = 'Vitali')),
    (gen_random_uuid(), 'Toxicología',                'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo')),
    (gen_random_uuid(), 'Traumatología y Ortopedia',  'MEDICO',         (SELECT id FROM Usuario WHERE apellidos = 'Marino' AND nombres = 'Mariano Gustavo'));


-- ----------------------------------------------------------------------------
-- 7) Asignación de servicioID a cada Usuario (cierra el ciclo)
-- ----------------------------------------------------------------------------
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Dirección')                 WHERE apellidos IN ('Marino', 'Huergo Sánchez', 'Longoni', 'Vitali');
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Admisión')                  WHERE apellidos = 'Balconte';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Biomédica')                 WHERE apellidos = 'Maestro';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Cirugía')                   WHERE apellidos = 'Titarelli';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Diagnóstico por Imágenes')  WHERE apellidos = 'Morales';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Enfermería')                WHERE apellidos = 'Plaza';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Esterilización')            WHERE apellidos = 'Pérez Cabral';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Farmacia')                  WHERE apellidos = 'Arancibia';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Habilitación')              WHERE apellidos = 'Roberts';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Informática')               WHERE apellidos IN ('Roqué', 'Canga Castellanos', 'Garay');
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Nutrición')                 WHERE apellidos = 'Boqué';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Traumatología y Ortopedia') WHERE apellidos IN ('Sánchez', 'Rabbat', 'Puig');
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Toxicología')               WHERE apellidos IN ('Vilkelis', 'Tapia', 'Tampares');
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Personal')                  WHERE apellidos IN ('Maurino', 'Garzón', 'Taborda', 'Usandivares');
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Esterilización')            WHERE apellidos = 'Vargas Ruíz';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Cirugía')                   WHERE apellidos IN ('Vignetta', 'Suizer', 'Pascolo');
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Habilitación')              WHERE apellidos = 'Vivas';
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Diagnóstico por Imágenes')  WHERE apellidos IN ('Aniceto', 'Rius');
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Enfermería')                WHERE apellidos IN ('Tolay', 'Terrieris', 'Tarifa');
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Biomédica')                 WHERE apellidos IN ('Suárez', 'Bustos');
UPDATE Usuario SET servicioID = (SELECT id FROM Servicio WHERE nombre = 'Admisión')                  WHERE apellidos = 'Romero';


-- ----------------------------------------------------------------------------
-- 8) Vincular Jefaturas con Servicios
-- ----------------------------------------------------------------------------
INSERT INTO Servicio_JefaturaDeServicio (servicioID, empleadoID) VALUES
    ((SELECT id FROM Servicio WHERE nombre = 'Admisión'),                  (SELECT id FROM Usuario WHERE apellidos = 'Balconte')),
    ((SELECT id FROM Servicio WHERE nombre = 'Biomédica'),                 (SELECT id FROM Usuario WHERE apellidos = 'Maestro')),
    ((SELECT id FROM Servicio WHERE nombre = 'Cirugía'),                   (SELECT id FROM Usuario WHERE apellidos = 'Titarelli')),
    ((SELECT id FROM Servicio WHERE nombre = 'Diagnóstico por Imágenes'),  (SELECT id FROM Usuario WHERE apellidos = 'Morales')),
    ((SELECT id FROM Servicio WHERE nombre = 'Enfermería'),                (SELECT id FROM Usuario WHERE apellidos = 'Plaza')),
    ((SELECT id FROM Servicio WHERE nombre = 'Esterilización'),            (SELECT id FROM Usuario WHERE apellidos = 'Pérez Cabral')),
    ((SELECT id FROM Servicio WHERE nombre = 'Farmacia'),                  (SELECT id FROM Usuario WHERE apellidos = 'Arancibia')),
    ((SELECT id FROM Servicio WHERE nombre = 'Habilitación'),              (SELECT id FROM Usuario WHERE apellidos = 'Roberts')),
    ((SELECT id FROM Servicio WHERE nombre = 'Informática'),               (SELECT id FROM Usuario WHERE apellidos = 'Roqué')),
    ((SELECT id FROM Servicio WHERE nombre = 'Nutrición'),                 (SELECT id FROM Usuario WHERE apellidos = 'Boqué')),
    ((SELECT id FROM Servicio WHERE nombre = 'Personal'),                  (SELECT id FROM Usuario WHERE apellidos = 'Maurino')),
    ((SELECT id FROM Servicio WHERE nombre = 'Toxicología'),               (SELECT id FROM Usuario WHERE apellidos = 'Vilkelis')),
    ((SELECT id FROM Servicio WHERE nombre = 'Traumatología y Ortopedia'), (SELECT id FROM Usuario WHERE apellidos = 'Sánchez' AND nombres = 'Omar Wenceslao'));


-- ----------------------------------------------------------------------------
-- 9) Roles
-- ----------------------------------------------------------------------------
INSERT INTO Rol (id, nombre, descripcion) VALUES
    (gen_random_uuid(), 'Empleado',           'Empleado'),
    (gen_random_uuid(), 'JefaturaDeServicio', 'Jefatura de Servicio'),
    (gen_random_uuid(), 'OficinaDePersonal',  'Oficina de Personal'),
    (gen_random_uuid(), 'Direccion',          'Directivo');


-- ----------------------------------------------------------------------------
-- 10) Asignación de roles
--
-- Todos reciben 'Empleado'. Las jefaturas/dirección/oficina reciben sus
-- roles específicos adicionales.
-- ----------------------------------------------------------------------------
-- Empleado para todos
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT u.id, r.id FROM Usuario u, Rol r WHERE r.nombre = 'Empleado';

-- JefaturaDeServicio para los jefes (incluye a Maurino, que también es OdP)
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT u.id, r.id FROM Usuario u, Rol r
WHERE r.nombre = 'JefaturaDeServicio'
  AND u.apellidos IN (
      'Balconte', 'Maestro', 'Titarelli', 'Morales', 'Plaza', 'Pérez Cabral',
      'Arancibia', 'Roberts', 'Roqué', 'Boqué', 'Maurino', 'Vilkelis', 'Sánchez'
  );

-- OficinaDePersonal para Maurino
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT u.id, r.id FROM Usuario u, Rol r
WHERE r.nombre = 'OficinaDePersonal' AND u.apellidos = 'Maurino';

-- Direccion para los 4 directivos
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT u.id, r.id FROM Usuario u, Rol r
WHERE r.nombre = 'Direccion'
  AND u.apellidos IN ('Marino', 'Huergo Sánchez', 'Longoni', 'Vitali');

-- Marino también ejerce JefaturaDeServicio
INSERT INTO Usuario_Rol (usuario_id, rol_id)
SELECT u.id, r.id FROM Usuario u, Rol r
WHERE r.nombre = 'JefaturaDeServicio'
  AND u.apellidos = 'Marino' AND u.nombres = 'Mariano Gustavo';


-- ----------------------------------------------------------------------------
-- 11) Asignación inicial de Cargos
-- ----------------------------------------------------------------------------
UPDATE Usuario SET cargoID = (SELECT id FROM Cargo WHERE numero = 0);  -- todos en 'INDEFINIDO' por defecto

UPDATE Usuario
SET cargoID = (SELECT id FROM Cargo WHERE numero = 400)  -- TECNICO
WHERE servicioID IN (SELECT id FROM Servicio WHERE nombre IN ('Biomédica', 'Informática'));

UPDATE Usuario
SET cargoID = (SELECT id FROM Cargo WHERE numero = 300)  -- JEFATURA
WHERE apellidos = 'Roqué' OR apellidos = 'Maurino';


-- ----------------------------------------------------------------------------
-- 12) Verificaciones
-- ----------------------------------------------------------------------------
-- Asignaciones de jefaturas
SELECT s.id            AS servicio_id,
       s.nombre        AS servicio_nombre,
       e.id            AS jefatura_id,
       u.apellidos || ', ' || u.nombres AS jefatura_de_servicio
FROM Servicio s
JOIN Servicio_JefaturaDeServicio sjs ON s.id = sjs.servicioID
JOIN Empleado e ON sjs.empleadoID = e.id
JOIN Usuario u ON e.id = u.id
ORDER BY s.nombre;

-- Asignaciones de roles
SELECT u.id        AS usuario_id,
       u.apellidos,
       r.id        AS rol_id,
       r.nombre    AS rol_nombre
FROM Usuario u
JOIN Usuario_Rol ur ON u.id = ur.usuario_id
JOIN Rol r ON ur.rol_id = r.id
ORDER BY u.apellidos, r.nombre;


-- ----------------------------------------------------------------------------
-- 13) Ejemplos de UPDATE / DELETE
-- ----------------------------------------------------------------------------

-- Modificar el teléfono al Jefe de Informática
UPDATE Usuario SET tel = 3517553799 WHERE apellidos = 'Roqué';
SELECT nombres, apellidos, tel FROM Usuario WHERE apellidos = 'Roqué';

-- Borrar a 'Soledad Romero' (cae todo por ON DELETE CASCADE)
DELETE FROM Empleado WHERE id = (SELECT id FROM Usuario WHERE apellidos = 'Romero' AND nombres = 'Soledad');
DELETE FROM Usuario  WHERE apellidos = 'Romero' AND nombres = 'Soledad';
