/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MariaDB
 Source Server Version : 110502 (11.5.2-MariaDB)
 Source Host           : localhost:3306
 Source Schema         : aromito

 Target Server Type    : MariaDB
 Target Server Version : 110502 (11.5.2-MariaDB)
 File Encoding         : 65001

 Date: 24/11/2024 21:46:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for agente
-- ----------------------------
DROP TABLE IF EXISTS `agente`;
CREATE TABLE `agente`  (
  `id` binary(16) NOT NULL,
  `francosCompensatoriosUtilizados` int(11) NULL DEFAULT NULL,
  `horarioActualID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `horarioActualID`(`horarioActualID` ASC) USING BTREE,
  CONSTRAINT `agente_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `agente_ibfk_2` FOREIGN KEY (`horarioActualID`) REFERENCES `horariobase` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of agente
-- ----------------------------
INSERT INTO `agente` VALUES (0x41D379FAAAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D37F48AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3853AAAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D392D7AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D398F1AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D39B15AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D39CC3AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D39EDAAAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3A0FFAAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3A3F2AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3A898AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3ABE3AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3AEEEAAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3B0EBAAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3B2B7AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3B484AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3B820AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3BA59AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3BE0EAAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3C0B4AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x41D3C3A4AAC411EFA64E0028F8594024, 0, NULL);
INSERT INTO `agente` VALUES (0x47E4F1ECAAC411EFA64E0028F8594024, 0, NULL);

-- ----------------------------
-- Table structure for agente_novedad
-- ----------------------------
DROP TABLE IF EXISTS `agente_novedad`;
CREATE TABLE `agente_novedad`  (
  `agenteID` binary(16) NOT NULL,
  `novedadID` binary(16) NOT NULL,
  PRIMARY KEY (`agenteID`, `novedadID`) USING BTREE,
  INDEX `novedadID`(`novedadID` ASC) USING BTREE,
  CONSTRAINT `agente_novedad_ibfk_1` FOREIGN KEY (`agenteID`) REFERENCES `agente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `agente_novedad_ibfk_2` FOREIGN KEY (`novedadID`) REFERENCES `novedad` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of agente_novedad
-- ----------------------------

-- ----------------------------
-- Table structure for autorizacion
-- ----------------------------
DROP TABLE IF EXISTS `autorizacion`;
CREATE TABLE `autorizacion`  (
  `id` binary(16) NOT NULL,
  `fechaAutorizacion` datetime NOT NULL,
  `tipo` enum('AGENTE','JEFEDESERVICIO','OFICINADEPERSONAL','DIRECCION') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `autorizadoPorID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `autorizadoPorID`(`autorizadoPorID` ASC) USING BTREE,
  CONSTRAINT `autorizacion_ibfk_1` FOREIGN KEY (`autorizadoPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of autorizacion
-- ----------------------------

-- ----------------------------
-- Table structure for cargo
-- ----------------------------
DROP TABLE IF EXISTS `cargo`;
CREATE TABLE `cargo`  (
  `id` binary(16) NOT NULL,
  `numero` int(11) NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `agrupacion` enum('INDEFINIDO','PLANTAPOLITICA','JEFATURA','TECNICO','ADMINISTRATIVO','SERVICIO','ENFERMERIA','MEDICO','PROFESIONAL') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `numero`(`numero` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cargo
-- ----------------------------
INSERT INTO `cargo` VALUES (0x41D1BD20AAC411EFA64E0028F8594024, 0, 'Cargo sin definir', 'INDEFINIDO');
INSERT INTO `cargo` VALUES (0x41D1C7DCAAC411EFA64E0028F8594024, 200, 'Directivos', 'PLANTAPOLITICA');
INSERT INTO `cargo` VALUES (0x41D1D018AAC411EFA64E0028F8594024, 300, 'Jefaturas de Servicio', 'JEFATURA');
INSERT INTO `cargo` VALUES (0x41D1D77BAAC411EFA64E0028F8594024, 400, 'Técnicos', 'TECNICO');
INSERT INTO `cargo` VALUES (0x41D1E018AAC411EFA64E0028F8594024, 500, 'Administrativos', 'ADMINISTRATIVO');
INSERT INTO `cargo` VALUES (0x41D1E7FDAAC411EFA64E0028F8594024, 600, 'Servicios', 'SERVICIO');
INSERT INTO `cargo` VALUES (0x41D1EF18AAC411EFA64E0028F8594024, 700, 'Enfermería Técnicos', 'ENFERMERIA');
INSERT INTO `cargo` VALUES (0x41D1F6F8AAC411EFA64E0028F8594024, 800, 'Médicos', 'MEDICO');
INSERT INTO `cargo` VALUES (0x41D1FFAAAAC411EFA64E0028F8594024, 900, 'Enfermería Profesionales', 'ENFERMERIA');
INSERT INTO `cargo` VALUES (0x41D206B5AAC411EFA64E0028F8594024, 1200, 'Profesionales sin agrupación específica', 'PROFESIONAL');

-- ----------------------------
-- Table structure for diagramadeservicio
-- ----------------------------
DROP TABLE IF EXISTS `diagramadeservicio`;
CREATE TABLE `diagramadeservicio`  (
  `id` binary(16) NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaFin` date NOT NULL,
  `servicioID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `servicioID`(`servicioID` ASC) USING BTREE,
  CONSTRAINT `diagramadeservicio_ibfk_1` FOREIGN KEY (`servicioID`) REFERENCES `servicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of diagramadeservicio
-- ----------------------------

-- ----------------------------
-- Table structure for direccion
-- ----------------------------
DROP TABLE IF EXISTS `direccion`;
CREATE TABLE `direccion`  (
  `id` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `direccion_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of direccion
-- ----------------------------
INSERT INTO `direccion` VALUES (0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `direccion` VALUES (0x41D354ABAAC411EFA64E0028F8594024);
INSERT INTO `direccion` VALUES (0x41D3566CAAC411EFA64E0028F8594024);
INSERT INTO `direccion` VALUES (0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `direccion` VALUES (0x47DFDD81AAC411EFA64E0028F8594024);

-- ----------------------------
-- Table structure for domicilio
-- ----------------------------
DROP TABLE IF EXISTS `domicilio`;
CREATE TABLE `domicilio`  (
  `id` binary(16) NOT NULL,
  `calle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `numeracion` int(11) NULL DEFAULT NULL,
  `barrio` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ciudad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `localidad` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `provincia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_calle`(`calle` ASC) USING BTREE,
  INDEX `idx_barrio`(`barrio` ASC) USING BTREE,
  INDEX `idx_ciudad`(`ciudad` ASC) USING BTREE,
  INDEX `idx_localidad`(`localidad` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of domicilio
-- ----------------------------
INSERT INTO `domicilio` VALUES (0x41CF4B3FAAC411EFA64E0028F8594024, 'Defensa', 1200, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x41CF4DDEAAC411EFA64E0028F8594024, 'Lavalleja', 3050, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x41CF4ECFAAC411EFA64E0028F8594024, 'Av Belgrano Sur', 134, NULL, 'La Rioja', NULL, 'La Rioja');
INSERT INTO `domicilio` VALUES (0x41CF4F55AAC411EFA64E0028F8594024, 'Av. Marcelo T. de Alvear', 120, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x41CF4FD4AAC411EFA64E0028F8594024, 'Av. Libertador', 1450, NULL, 'Alta Gracia', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x41CF5047AAC411EFA64E0028F8594024, 'Catamarca', 441, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x41CF50BAAAC411EFA64E0028F8594024, 'Junín', 574, NULL, 'Rosario', NULL, 'Santa Fe');
INSERT INTO `domicilio` VALUES (0x41CF514FAAC411EFA64E0028F8594024, 'Mate de Luna', 245, NULL, 'Famaillá', NULL, 'Tucumán');
INSERT INTO `domicilio` VALUES (0x41CF51CAAAC411EFA64E0028F8594024, 'Gobernación', 504, NULL, 'Santa Fe', NULL, 'Santa Fe');
INSERT INTO `domicilio` VALUES (0x41CF523CAAC411EFA64E0028F8594024, 'Güemes', 781, NULL, 'Iruya', NULL, 'Jujuy');
INSERT INTO `domicilio` VALUES (0x41CF52BAAAC411EFA64E0028F8594024, '9 de julio', 396, NULL, 'Corrientes', NULL, 'Corrientes');
INSERT INTO `domicilio` VALUES (0x41CF5362AAC411EFA64E0028F8594024, 'Av. del Trabajo', 634, NULL, 'Rosario', NULL, 'Santa Fe');
INSERT INTO `domicilio` VALUES (0x41CF53D7AAC411EFA64E0028F8594024, 'Calle del Río', 984, NULL, 'Paraná', NULL, 'Entre Ríos');
INSERT INTO `domicilio` VALUES (0x41CF5443AAC411EFA64E0028F8594024, 'Av. Central', 17, NULL, 'Salta', NULL, 'Salta');
INSERT INTO `domicilio` VALUES (0x41CF54B4AAC411EFA64E0028F8594024, 'Av. Siempre Viva', 130, NULL, 'Rawson', NULL, 'Chubut');
INSERT INTO `domicilio` VALUES (0x41CF5524AAC411EFA64E0028F8594024, 'San Martín', 600, NULL, 'Bariloche', NULL, 'Río Negro');

-- ----------------------------
-- Table structure for estadotramite
-- ----------------------------
DROP TABLE IF EXISTS `estadotramite`;
CREATE TABLE `estadotramite`  (
  `id` binary(16) NOT NULL,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of estadotramite
-- ----------------------------

-- ----------------------------
-- Table structure for francocompensatorio
-- ----------------------------
DROP TABLE IF EXISTS `francocompensatorio`;
CREATE TABLE `francocompensatorio`  (
  `id` binary(16) NOT NULL,
  `cantHoras` double NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaAutorizacion` datetime NULL DEFAULT NULL,
  `fechaDeAplicacion` date NULL DEFAULT NULL,
  `estadoTramiteID` binary(16) NOT NULL,
  `autorizadaPorID` binary(16) NULL DEFAULT NULL,
  `agenteID` binary(16) NOT NULL,
  `jefeDeServicioID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estadoTramiteID`(`estadoTramiteID` ASC) USING BTREE,
  INDEX `autorizadaPorID`(`autorizadaPorID` ASC) USING BTREE,
  INDEX `agenteID`(`agenteID` ASC) USING BTREE,
  INDEX `jefeDeServicioID`(`jefeDeServicioID` ASC) USING BTREE,
  CONSTRAINT `francocompensatorio_ibfk_1` FOREIGN KEY (`estadoTramiteID`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `francocompensatorio_ibfk_2` FOREIGN KEY (`autorizadaPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `francocompensatorio_ibfk_3` FOREIGN KEY (`agenteID`) REFERENCES `agente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `francocompensatorio_ibfk_4` FOREIGN KEY (`jefeDeServicioID`) REFERENCES `jefedeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of francocompensatorio
-- ----------------------------

-- ----------------------------
-- Table structure for horaextra
-- ----------------------------
DROP TABLE IF EXISTS `horaextra`;
CREATE TABLE `horaextra`  (
  `id` binary(16) NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaIngreso` datetime NOT NULL,
  `fechaEgreso` datetime NOT NULL,
  `ponderacion` int(11) NOT NULL,
  `fechaAutorizacion` datetime NULL DEFAULT NULL,
  `estadoTramiteID` binary(16) NOT NULL,
  `autorizadaPorID` binary(16) NULL DEFAULT NULL,
  `agenteID` binary(16) NOT NULL,
  `jefeDeServicioID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estadoTramiteID`(`estadoTramiteID` ASC) USING BTREE,
  INDEX `autorizadaPorID`(`autorizadaPorID` ASC) USING BTREE,
  INDEX `agenteID`(`agenteID` ASC) USING BTREE,
  INDEX `jefeDeServicioID`(`jefeDeServicioID` ASC) USING BTREE,
  CONSTRAINT `horaextra_ibfk_1` FOREIGN KEY (`estadoTramiteID`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horaextra_ibfk_2` FOREIGN KEY (`autorizadaPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horaextra_ibfk_3` FOREIGN KEY (`agenteID`) REFERENCES `agente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horaextra_ibfk_4` FOREIGN KEY (`jefeDeServicioID`) REFERENCES `jefedeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horaextra
-- ----------------------------

-- ----------------------------
-- Table structure for horario
-- ----------------------------
DROP TABLE IF EXISTS `horario`;
CREATE TABLE `horario`  (
  `id` binary(16) NOT NULL,
  `fechaIngreso` datetime NOT NULL,
  `fechaEgreso` datetime NOT NULL,
  `jornadasPlanificadas` int(11) NULL DEFAULT NULL,
  `reglasHorario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `horarioBaseID` binary(16) NOT NULL,
  `modalidad` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horario_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horariobase` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horario
-- ----------------------------

-- ----------------------------
-- Table structure for horarioabierto
-- ----------------------------
DROP TABLE IF EXISTS `horarioabierto`;
CREATE TABLE `horarioabierto`  (
  `id` binary(16) NOT NULL,
  `flexibilidadHoraria` tinyint(1) NOT NULL,
  `horasSemanales` int(11) NOT NULL,
  `preferenciasHorarias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioabierto_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioabierto
-- ----------------------------

-- ----------------------------
-- Table structure for horariobase
-- ----------------------------
DROP TABLE IF EXISTS `horariobase`;
CREATE TABLE `horariobase`  (
  `id` binary(16) NOT NULL,
  `tipo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horariobase
-- ----------------------------

-- ----------------------------
-- Table structure for horarioconfranquicia
-- ----------------------------
DROP TABLE IF EXISTS `horarioconfranquicia`;
CREATE TABLE `horarioconfranquicia`  (
  `id` binary(16) NOT NULL,
  `fechaIngreso` datetime NOT NULL,
  `fechaEgreso` datetime NOT NULL,
  `horasFranquicia` int(11) NOT NULL,
  `horarioDecoradoID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `horarioDecoradoID`(`horarioDecoradoID` ASC) USING BTREE,
  CONSTRAINT `horarioconfranquicia_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horariobase` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horarioconfranquicia_ibfk_2` FOREIGN KEY (`horarioDecoradoID`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioconfranquicia
-- ----------------------------

-- ----------------------------
-- Table structure for horariodxi
-- ----------------------------
DROP TABLE IF EXISTS `horariodxi`;
CREATE TABLE `horariodxi`  (
  `id` binary(16) NOT NULL,
  `distribucionHoraria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horaInicioPorDia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horasSemanales` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horariodxi_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horariodxi
-- ----------------------------

-- ----------------------------
-- Table structure for horarioestandar
-- ----------------------------
DROP TABLE IF EXISTS `horarioestandar`;
CREATE TABLE `horarioestandar`  (
  `id` binary(16) NOT NULL,
  `diasLaborables` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horasPorDia` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioestandar_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioestandar
-- ----------------------------

-- ----------------------------
-- Table structure for horarioferiante
-- ----------------------------
DROP TABLE IF EXISTS `horarioferiante`;
CREATE TABLE `horarioferiante`  (
  `id` binary(16) NOT NULL,
  `diasNoLaborables` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `duracionGuardiaHoras` int(11) NOT NULL,
  `guardiasProgramadas` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horasMinimasMensuales` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioferiante_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioferiante
-- ----------------------------

-- ----------------------------
-- Table structure for horarioguardiaenfermeria
-- ----------------------------
DROP TABLE IF EXISTS `horarioguardiaenfermeria`;
CREATE TABLE `horarioguardiaenfermeria`  (
  `id` binary(16) NOT NULL,
  `duracionGuardia10Horas` int(11) NOT NULL,
  `duracionGuardia12Horas` int(11) NOT NULL,
  `fechasGuardias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `numeroGuardias10Horas` int(11) NOT NULL,
  `numeroGuardias12Horas` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioguardiaenfermeria_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioguardiaenfermeria
-- ----------------------------

-- ----------------------------
-- Table structure for horarioguardiamedica
-- ----------------------------
DROP TABLE IF EXISTS `horarioguardiamedica`;
CREATE TABLE `horarioguardiamedica`  (
  `id` binary(16) NOT NULL,
  `duracionGuardiaHoras` int(11) NOT NULL,
  `fechasGuardias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `numeroGuardiasSemanal` int(11) NOT NULL,
  `permitirGuardiasContinuas` tinyint(1) NOT NULL,
  `tiempoDescansoMinimoHoras` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarioguardiamedica_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarioguardiamedica
-- ----------------------------

-- ----------------------------
-- Table structure for horariojefeservicioguardiapasiva
-- ----------------------------
DROP TABLE IF EXISTS `horariojefeservicioguardiapasiva`;
CREATE TABLE `horariojefeservicioguardiapasiva`  (
  `id` binary(16) NOT NULL,
  `diasLaborables` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horasPorDia` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horariojefeservicioguardiapasiva_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horariojefeservicioguardiapasiva
-- ----------------------------

-- ----------------------------
-- Table structure for horarionocturno
-- ----------------------------
DROP TABLE IF EXISTS `horarionocturno`;
CREATE TABLE `horarionocturno`  (
  `id` binary(16) NOT NULL,
  `diasProgramados` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `duracionJornadaHoras` int(11) NOT NULL,
  `numeroJornadasMensuales` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horarionocturno_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horarionocturno
-- ----------------------------

-- ----------------------------
-- Table structure for horariosemanal
-- ----------------------------
DROP TABLE IF EXISTS `horariosemanal`;
CREATE TABLE `horariosemanal`  (
  `id` binary(16) NOT NULL,
  `distribucionSemanal` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `horaInicioPorDia` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horariosemanal_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of horariosemanal
-- ----------------------------

-- ----------------------------
-- Table structure for jefedeservicio
-- ----------------------------
DROP TABLE IF EXISTS `jefedeservicio`;
CREATE TABLE `jefedeservicio`  (
  `id` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `jefedeservicio_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jefedeservicio
-- ----------------------------
INSERT INTO `jefedeservicio` VALUES (0x41D13E28AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D146E2AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D15291AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D15D73AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D16702AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D16FF4AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D178E7AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D1805AAAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D18896AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D19031AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D1A5EDAAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x41D1B272AAC411EFA64E0028F8594024);
INSERT INTO `jefedeservicio` VALUES (0x47E16AD0AAC411EFA64E0028F8594024);

-- ----------------------------
-- Table structure for jornadalaboral
-- ----------------------------
DROP TABLE IF EXISTS `jornadalaboral`;
CREATE TABLE `jornadalaboral`  (
  `id` binary(16) NOT NULL,
  `fechaIngreso` datetime NOT NULL,
  `fechaEgreso` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jornadalaboral
-- ----------------------------

-- ----------------------------
-- Table structure for marcacionagente
-- ----------------------------
DROP TABLE IF EXISTS `marcacionagente`;
CREATE TABLE `marcacionagente`  (
  `id` binary(16) NOT NULL,
  `fechaMarcacion` datetime NOT NULL,
  `observaciones` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tipoMarcacion` enum('INGRESO','EGRESO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `validada` tinyint(1) NOT NULL,
  `agenteID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `agenteID`(`agenteID` ASC) USING BTREE,
  CONSTRAINT `marcacionagente_ibfk_1` FOREIGN KEY (`agenteID`) REFERENCES `agente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of marcacionagente
-- ----------------------------

-- ----------------------------
-- Table structure for memorandum
-- ----------------------------
DROP TABLE IF EXISTS `memorandum`;
CREATE TABLE `memorandum`  (
  `id` binary(16) NOT NULL,
  `asunto` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `contenido` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaEnvio` datetime NULL DEFAULT NULL,
  `fechaRecepcion` datetime NULL DEFAULT NULL,
  `estado` binary(16) NOT NULL,
  `remitenteID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estado`(`estado` ASC) USING BTREE,
  INDEX `remitenteID`(`remitenteID` ASC) USING BTREE,
  CONSTRAINT `memorandum_ibfk_1` FOREIGN KEY (`estado`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_ibfk_2` FOREIGN KEY (`remitenteID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of memorandum
-- ----------------------------

-- ----------------------------
-- Table structure for memorandum_autorizacion
-- ----------------------------
DROP TABLE IF EXISTS `memorandum_autorizacion`;
CREATE TABLE `memorandum_autorizacion`  (
  `id` binary(16) NOT NULL,
  `memorandumID` binary(16) NOT NULL,
  `tipoAutorizacionID` enum('JefeDeServicio','OficinaDePersonal','Direccion','Usuario') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `autorizadoPorID` binary(16) NULL DEFAULT NULL,
  `fechaAutorizacion` datetime NULL DEFAULT NULL,
  `estado` enum('PENDIENTE','AUTORIZADO','RECHAZADO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `memorandumID`(`memorandumID` ASC) USING BTREE,
  INDEX `autorizadoPorID`(`autorizadoPorID` ASC) USING BTREE,
  CONSTRAINT `memorandum_autorizacion_ibfk_1` FOREIGN KEY (`memorandumID`) REFERENCES `memorandum` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_autorizacion_ibfk_2` FOREIGN KEY (`autorizadoPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of memorandum_autorizacion
-- ----------------------------

-- ----------------------------
-- Table structure for memorandum_destinatario
-- ----------------------------
DROP TABLE IF EXISTS `memorandum_destinatario`;
CREATE TABLE `memorandum_destinatario`  (
  `memorandumID` binary(16) NOT NULL,
  `usuarioID` binary(16) NOT NULL,
  `fechaRecepcion` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`memorandumID`, `usuarioID`) USING BTREE,
  INDEX `usuarioID`(`usuarioID` ASC) USING BTREE,
  CONSTRAINT `memorandum_destinatario_ibfk_1` FOREIGN KEY (`memorandumID`) REFERENCES `memorandum` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_destinatario_ibfk_2` FOREIGN KEY (`usuarioID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of memorandum_destinatario
-- ----------------------------

-- ----------------------------
-- Table structure for memorandum_firmante
-- ----------------------------
DROP TABLE IF EXISTS `memorandum_firmante`;
CREATE TABLE `memorandum_firmante`  (
  `memorandumID` binary(16) NOT NULL,
  `usuarioID` binary(16) NOT NULL,
  `fechaFirma` datetime NOT NULL,
  PRIMARY KEY (`memorandumID`, `usuarioID`) USING BTREE,
  INDEX `usuarioID`(`usuarioID` ASC) USING BTREE,
  CONSTRAINT `memorandum_firmante_ibfk_1` FOREIGN KEY (`memorandumID`) REFERENCES `memorandum` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_firmante_ibfk_2` FOREIGN KEY (`usuarioID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of memorandum_firmante
-- ----------------------------

-- ----------------------------
-- Table structure for novedad
-- ----------------------------
DROP TABLE IF EXISTS `novedad`;
CREATE TABLE `novedad`  (
  `id` binary(16) NOT NULL,
  `cod` int(11) NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estadoFechaModif` timestamp NULL DEFAULT NULL,
  `fechaInicio` date NULL DEFAULT NULL,
  `fechaFin` date NULL DEFAULT NULL,
  `fechaSolicitud` date NULL DEFAULT NULL,
  `reqAprobDireccion` tinyint(1) NOT NULL,
  `estadoTramiteID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estadoTramiteID`(`estadoTramiteID` ASC) USING BTREE,
  CONSTRAINT `novedad_ibfk_1` FOREIGN KEY (`estadoTramiteID`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of novedad
-- ----------------------------

-- ----------------------------
-- Table structure for oficinadepersonal
-- ----------------------------
DROP TABLE IF EXISTS `oficinadepersonal`;
CREATE TABLE `oficinadepersonal`  (
  `id` binary(16) NOT NULL,
  `reportesGenerados` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `oficinadepersonal_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oficinadepersonal
-- ----------------------------
INSERT INTO `oficinadepersonal` VALUES (0x41D19A8CAAC411EFA64E0028F8594024, 0);
INSERT INTO `oficinadepersonal` VALUES (0x47E2EA79AAC411EFA64E0028F8594024, 0);

-- ----------------------------
-- Table structure for partediario
-- ----------------------------
DROP TABLE IF EXISTS `partediario`;
CREATE TABLE `partediario`  (
  `id` binary(16) NOT NULL,
  `fechaDeCierre` datetime NULL DEFAULT NULL,
  `periodo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `modificadoPorID` binary(16) NULL DEFAULT NULL,
  `oficinaID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `modificadoPorID`(`modificadoPorID` ASC) USING BTREE,
  INDEX `oficinaID`(`oficinaID` ASC) USING BTREE,
  CONSTRAINT `partediario_ibfk_1` FOREIGN KEY (`modificadoPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `partediario_ibfk_2` FOREIGN KEY (`oficinaID`) REFERENCES `oficinadepersonal` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of partediario
-- ----------------------------

-- ----------------------------
-- Table structure for partediario_agente
-- ----------------------------
DROP TABLE IF EXISTS `partediario_agente`;
CREATE TABLE `partediario_agente`  (
  `parteDiarioID` binary(16) NOT NULL,
  `agenteID` binary(16) NOT NULL,
  PRIMARY KEY (`parteDiarioID`, `agenteID`) USING BTREE,
  INDEX `agenteID`(`agenteID` ASC) USING BTREE,
  CONSTRAINT `partediario_agente_ibfk_1` FOREIGN KEY (`parteDiarioID`) REFERENCES `partediario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `partediario_agente_ibfk_2` FOREIGN KEY (`agenteID`) REFERENCES `agente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of partediario_agente
-- ----------------------------

-- ----------------------------
-- Table structure for planificacion
-- ----------------------------
DROP TABLE IF EXISTS `planificacion`;
CREATE TABLE `planificacion`  (
  `diagramaID` binary(16) NOT NULL,
  `agenteID` binary(16) NOT NULL,
  `jornadaID` binary(16) NOT NULL,
  PRIMARY KEY (`diagramaID`, `agenteID`, `jornadaID`) USING BTREE,
  INDEX `agenteID`(`agenteID` ASC) USING BTREE,
  INDEX `jornadaID`(`jornadaID` ASC) USING BTREE,
  CONSTRAINT `planificacion_ibfk_1` FOREIGN KEY (`diagramaID`) REFERENCES `diagramadeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `planificacion_ibfk_2` FOREIGN KEY (`agenteID`) REFERENCES `agente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `planificacion_ibfk_3` FOREIGN KEY (`jornadaID`) REFERENCES `jornadalaboral` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of planificacion
-- ----------------------------

-- ----------------------------
-- Table structure for registrojornadalaboral
-- ----------------------------
DROP TABLE IF EXISTS `registrojornadalaboral`;
CREATE TABLE `registrojornadalaboral`  (
  `id` binary(16) NOT NULL,
  `fecha` date NOT NULL,
  `agenteID` binary(16) NOT NULL,
  `marcacionIngresoID` binary(16) NOT NULL,
  `marcacionEgresoID` binary(16) NOT NULL,
  `duracionJornada` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `agenteID`(`agenteID` ASC) USING BTREE,
  INDEX `marcacionIngresoID`(`marcacionIngresoID` ASC) USING BTREE,
  INDEX `marcacionEgresoID`(`marcacionEgresoID` ASC) USING BTREE,
  CONSTRAINT `registrojornadalaboral_ibfk_1` FOREIGN KEY (`agenteID`) REFERENCES `agente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `registrojornadalaboral_ibfk_2` FOREIGN KEY (`marcacionIngresoID`) REFERENCES `marcacionagente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `registrojornadalaboral_ibfk_3` FOREIGN KEY (`marcacionEgresoID`) REFERENCES `marcacionagente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registrojornadalaboral
-- ----------------------------

-- ----------------------------
-- Table structure for rol
-- ----------------------------
DROP TABLE IF EXISTS `rol`;
CREATE TABLE `rol`  (
  `id` binary(16) NOT NULL,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `nombre`(`nombre` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rol
-- ----------------------------
INSERT INTO `rol` VALUES (0x41D9024BAAC411EFA64E0028F8594024, 'Agente', 'Agente');
INSERT INTO `rol` VALUES (0x41D90C9BAAC411EFA64E0028F8594024, 'JefeDeServicio', 'Jefe de Servicio');
INSERT INTO `rol` VALUES (0x41D91581AAC411EFA64E0028F8594024, 'OficinaDePersonal', 'Oficina de Personal');
INSERT INTO `rol` VALUES (0x41D91F9EAAC411EFA64E0028F8594024, 'Direccion', 'Directivo');

-- ----------------------------
-- Table structure for servicio
-- ----------------------------
DROP TABLE IF EXISTS `servicio`;
CREATE TABLE `servicio`  (
  `id` binary(16) NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `agrupacion` enum('ADMINISTRATIVO','SERVICIO','MEDICO','ENFERMERIA','TECNICO','PLANTAPOLITICA') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `direccionID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_nombre`(`nombre` ASC) USING BTREE,
  INDEX `idx_servicio_direccion`(`direccionID` ASC) USING BTREE,
  CONSTRAINT `servicio_ibfk_1` FOREIGN KEY (`direccionID`) REFERENCES `direccion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of servicio
-- ----------------------------
INSERT INTO `servicio` VALUES (0x41CFF2ADAAC411EFA64E0028F8594024, 'Administración', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41CFFFB0AAC411EFA64E0028F8594024, 'Admisión', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0072CAAC411EFA64E0028F8594024, 'Anatomía Patológica', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0165CAAC411EFA64E0028F8594024, 'Anestesia', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D01F3AAAC411EFA64E0028F8594024, 'Auditoría Médica', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D02630AAC411EFA64E0028F8594024, 'Biomédica', 'TECNICO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D02CECAAC411EFA64E0028F8594024, 'Bioquímica', 'SERVICIO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D03362AAC411EFA64E0028F8594024, 'Camilleros', 'SERVICIO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D039E7AAC411EFA64E0028F8594024, 'Capacitación y Docencia', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0402BAAC411EFA64E0028F8594024, 'Cirugía', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D04832AAC411EFA64E0028F8594024, 'Cirugía Plástica', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D04F99AAC411EFA64E0028F8594024, 'Clínica Médica', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D056A8AAC411EFA64E0028F8594024, 'Diagnóstico por Imágenes', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D060CAAAC411EFA64E0028F8594024, 'Dirección', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D067A0AAC411EFA64E0028F8594024, 'Enfermería', 'ENFERMERIA', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D06E88AAC411EFA64E0028F8594024, 'Esterilización', 'SERVICIO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D076B0AAC411EFA64E0028F8594024, 'Facturación', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D07D7CAAC411EFA64E0028F8594024, 'Farmacia', 'SERVICIO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0844CAAC411EFA64E0028F8594024, 'Habilitación', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D08A94AAC411EFA64E0028F8594024, 'Hemoterapia', 'SERVICIO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D090C9AAC411EFA64E0028F8594024, 'Informática', 'TECNICO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D09782AAC411EFA64E0028F8594024, 'Instrumentación Quirúrgica', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D09D8DAAC411EFA64E0028F8594024, 'Kinesiología', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0A36EAAC411EFA64E0028F8594024, 'Laboratorio', 'SERVICIO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0A959AAC411EFA64E0028F8594024, 'Lavadero', 'SERVICIO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0B1BAAAC411EFA64E0028F8594024, 'Mantenimiento', 'TECNICO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0B907AAC411EFA64E0028F8594024, 'Medicina Legal', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0C190AAC411EFA64E0028F8594024, 'Neurocirugía', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0C7E0AAC411EFA64E0028F8594024, 'Nutrición', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0CDFFAAC411EFA64E0028F8594024, 'Personal', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0D58EAAC411EFA64E0028F8594024, 'Quirófano', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0DBD7AAC411EFA64E0028F8594024, 'Registro Médico (Archivo)', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0E3A6AAC411EFA64E0028F8594024, 'Salud Mental', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0F226AAC411EFA64E0028F8594024, 'Secretaría Técnica', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D0FE38AAC411EFA64E0028F8594024, 'Servicio Social', 'ADMINISTRATIVO', 0x41D35018AAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D11C3DAAC411EFA64E0028F8594024, 'Tecnoeléctrica', 'TECNICO', 0x41D3589CAAC411EFA64E0028F8594024);
INSERT INTO `servicio` VALUES (0x41D1344CAAC411EFA64E0028F8594024, 'Traumatología y Ortopedia', 'MEDICO', 0x41D35018AAC411EFA64E0028F8594024);

-- ----------------------------
-- Table structure for servicio_jefedeservicio
-- ----------------------------
DROP TABLE IF EXISTS `servicio_jefedeservicio`;
CREATE TABLE `servicio_jefedeservicio`  (
  `servicioID` binary(16) NOT NULL,
  `jefedeservicioID` binary(16) NOT NULL,
  PRIMARY KEY (`servicioID`, `jefedeservicioID`) USING BTREE,
  INDEX `jefedeservicioID`(`jefedeservicioID` ASC) USING BTREE,
  CONSTRAINT `servicio_jefedeservicio_ibfk_1` FOREIGN KEY (`servicioID`) REFERENCES `servicio` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `servicio_jefedeservicio_ibfk_2` FOREIGN KEY (`jefedeservicioID`) REFERENCES `jefedeservicio` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of servicio_jefedeservicio
-- ----------------------------
INSERT INTO `servicio_jefedeservicio` VALUES (0x41CFFFB0AAC411EFA64E0028F8594024, 0x41D13E28AAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D02630AAC411EFA64E0028F8594024, 0x41D146E2AAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D0402BAAC411EFA64E0028F8594024, 0x41D15291AAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D056A8AAC411EFA64E0028F8594024, 0x41D15D73AAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D067A0AAC411EFA64E0028F8594024, 0x41D16702AAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D06E88AAC411EFA64E0028F8594024, 0x41D16FF4AAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D07D7CAAC411EFA64E0028F8594024, 0x41D178E7AAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D0844CAAC411EFA64E0028F8594024, 0x41D1805AAAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D090C9AAC411EFA64E0028F8594024, 0x41D18896AAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D0C7E0AAC411EFA64E0028F8594024, 0x41D19031AAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D0CDFFAAC411EFA64E0028F8594024, 0x41D19A8CAAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D12925AAC411EFA64E0028F8594024, 0x41D1A5EDAAC411EFA64E0028F8594024);
INSERT INTO `servicio_jefedeservicio` VALUES (0x41D1344CAAC411EFA64E0028F8594024, 0x41D1B272AAC411EFA64E0028F8594024);

-- ----------------------------
-- Table structure for usuario
-- ----------------------------
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario`  (
  `id` binary(16) NOT NULL,
  `fechaAlta` date NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `cuil` bigint(20) NOT NULL,
  `apellidos` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nombres` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sexo` enum('FEMENINO','MASCULINO','OTRO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tel` bigint(20) NULL DEFAULT NULL,
  `domicilioID` binary(16) NULL DEFAULT NULL,
  `cargoID` binary(16) NULL DEFAULT NULL,
  `servicioID` binary(16) NULL DEFAULT NULL,
  `tipoUsuario` enum('AGENTE','JEFEDESERVICIO','OFICINADEPERSONAL','DIRECCION') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `passwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `profile_image` blob NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cuil`(`cuil` ASC) USING BTREE,
  UNIQUE INDEX `idx_mail`(`mail` ASC) USING BTREE,
  UNIQUE INDEX `idx_tel`(`tel` ASC) USING BTREE,
  INDEX `domicilioID`(`domicilioID` ASC) USING BTREE,
  INDEX `cargoID`(`cargoID` ASC) USING BTREE,
  INDEX `servicioID`(`servicioID` ASC) USING BTREE,
  INDEX `idx_apellidos`(`apellidos` ASC) USING BTREE,
  INDEX `idx_nombres`(`nombres` ASC) USING BTREE,
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`domicilioID`) REFERENCES `domicilio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `usuario_ibfk_2` FOREIGN KEY (`cargoID`) REFERENCES `cargo` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `usuario_ibfk_3` FOREIGN KEY (`servicioID`) REFERENCES `servicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usuario
-- ----------------------------
INSERT INTO `usuario` VALUES (0x41D13E28AAC411EFA64E0028F8594024, '2024-11-24', 1, 27295554447, 'Balconte', 'Andrea', 'FEMENINO', 'andrea@hmu.com.ar', NULL, 0x41CF50BAAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41CFFFB0AAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D146E2AAC411EFA64E0028F8594024, '2024-11-24', 1, 27274422442, 'Maestro', 'Silvina', 'FEMENINO', 'smaestro@hmu.com.ar', NULL, 0x41CF50BAAAC411EFA64E0028F8594024, 0x41D1D77BAAC411EFA64E0028F8594024, 0x41D02630AAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D15291AAC411EFA64E0028F8594024, '2024-11-24', 1, 20268944448, 'Titarelli', 'Maximiliano', 'MASCULINO', 'drtita@hmu.com.ar', NULL, 0x41CF50BAAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0402BAAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D15D73AAC411EFA64E0028F8594024, '2024-11-24', 1, 20281324547, 'Morales', 'Juan Ignacio', 'MASCULINO', 'jimorales@hmu.com.ar', NULL, 0x41CF54B4AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D056A8AAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D16702AAC411EFA64E0028F8594024, '2024-11-24', 1, 27224444445, 'Plaza', 'Tania', 'FEMENINO', 'tplaza@hmu.com.ar', NULL, 0x41CF5524AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D067A0AAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D16FF4AAC411EFA64E0028F8594024, '2024-11-24', 1, 20289445441, 'Pérez Cabral', 'Matías', 'MASCULINO', 'mcp@hmu.com.ar', NULL, 0x41CF5443AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D06E88AAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D178E7AAC411EFA64E0028F8594024, '2024-11-24', 1, 27254344447, 'Arancibia', 'María Pía', 'FEMENINO', 'piaarancibia@hmu.com.ar', NULL, 0x41CF54B4AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D07D7CAAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D1805AAAC411EFA64E0028F8594024, '2024-11-24', 1, 20276444446, 'Roberts', 'Carlos Fernando', 'MASCULINO', 'ferroberts@hmu.com.ar', NULL, 0x41CF4B3FAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0844CAAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D18896AAC411EFA64E0028F8594024, '2024-11-24', 1, 20220361118, 'Roqué', 'Juan Manuel', 'OTRO', 'jmroque@hmu.com.ar', 3517553799, 0x41CF5524AAC411EFA64E0028F8594024, 0x41D1D018AAC411EFA64E0028F8594024, 0x41D090C9AAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D19031AAC411EFA64E0028F8594024, '2024-11-24', 1, 27174444446, 'Boqué', 'Alejandra', 'FEMENINO', 'aleboque@hmu.com.ar', NULL, 0x41CF5524AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0C7E0AAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D19A8CAAC411EFA64E0028F8594024, '2024-11-24', 1, 27284644443, 'Maurino', 'Florencia', 'FEMENINO', 'flormaurino@hmu.com.ar', NULL, 0x41CF51CAAAC411EFA64E0028F8594024, 0x41D1D018AAC411EFA64E0028F8594024, 0x41D0CDFFAAC411EFA64E0028F8594024, 'OFICINADEPERSONAL', '$2a$10$7lkhHYuEFTcp/FDV5woWseLw9M2yyIscjczfRhcC9CTJvy2d9Y6VW', NULL);
INSERT INTO `usuario` VALUES (0x41D1A5EDAAC411EFA64E0028F8594024, '2024-11-24', 1, 27214434447, 'Vilkelis', 'Andrea', 'FEMENINO', 'avilkelis@hmu.com.ar', NULL, 0x41CF514FAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D12925AAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D1B272AAC411EFA64E0028F8594024, '2024-11-24', 1, 20124644445, 'Sánchez', 'Omar Wenceslao', 'MASCULINO', 'owsanchez@hmu.com.ar', NULL, 0x41CF5047AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D1344CAAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D35018AAC411EFA64E0028F8594024, '2024-11-24', 1, 20224448885, 'Marino', 'Mariano Gustavo', 'MASCULINO', 'direccion@hmu.com.ar', NULL, 0x41CF5047AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D060CAAAC411EFA64E0028F8594024, 'DIRECCION', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D354ABAAC411EFA64E0028F8594024, '2024-11-24', 1, 20259993331, 'Huergo Sánchez', 'Federico', 'MASCULINO', 'fedesubdir@hmu.com.ar', NULL, 0x41CF4DDEAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D060CAAAC411EFA64E0028F8594024, 'DIRECCION', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3566CAAC411EFA64E0028F8594024, '2024-11-24', 1, 27129997773, 'Longoni', 'Gloria', 'FEMENINO', 'subdirectora@hmu.com.ar', NULL, 0x41CF4FD4AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D060CAAAC411EFA64E0028F8594024, 'DIRECCION', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3589CAAC411EFA64E0028F8594024, '2024-11-24', 1, 20239997772, 'Vitali', 'Fabricio', 'MASCULINO', 'subdireccion@hmu.com.ar', NULL, 0x41CF5047AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D060CAAAC411EFA64E0028F8594024, 'DIRECCION', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D379FAAAC411EFA64E0028F8594024, '2024-11-24', 1, 20124543421, 'Aniceto', 'Juan', 'MASCULINO', 'janiceto@hmu.com.ar', NULL, 0x41CF514FAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D056A8AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D37F48AAC411EFA64E0028F8594024, '2024-11-24', 1, 20554644448, 'Garzón', 'Baltazar', 'MASCULINO', 'baltig@hmu.com.ar', NULL, 0x41CF4F55AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0CDFFAAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3853AAAC411EFA64E0028F8594024, '2024-11-24', 1, 20154786445, 'Taborda', 'Pedro', 'MASCULINO', 'pltaborda@hmu.com.ar', NULL, 0x41CF50BAAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0CDFFAAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D392D7AAC411EFA64E0028F8594024, '2024-11-24', 1, 27554664563, 'Vargas Ruíz', 'María Laura', 'FEMENINO', 'mlvargas@hmu.com.ar', NULL, 0x41CF50BAAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D06E88AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D398F1AAC411EFA64E0028F8594024, '2024-11-24', 1, 27554685443, 'Vignetta', 'María Celeste', 'FEMENINO', 'mcv@hmu.com.ar', NULL, 0x41CF50BAAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0402BAAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D39B15AAC411EFA64E0028F8594024, '2024-11-24', 1, 27554567443, 'Vivas', 'Alicia', 'FEMENINO', 'avivas@hmu.com.ar', NULL, 0x41CF4DDEAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0844CAAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D39CC3AAC411EFA64E0028F8594024, '2024-11-24', 1, 20554649743, 'Canga Castellanos', 'Matías Quique', 'MASCULINO', 'lestat@hmu.com.ar', NULL, 0x41CF514FAAC411EFA64E0028F8594024, 0x41D1D77BAAC411EFA64E0028F8594024, 0x41D090C9AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D39EDAAAC411EFA64E0028F8594024, '2024-11-24', 1, 24574741745, 'Garay', 'Mauricio Elio', 'OTRO', 'mgaray@hmu.com.ar', NULL, 0x41CF54B4AAC411EFA64E0028F8594024, 0x41D1D77BAAC411EFA64E0028F8594024, 0x41D090C9AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3A0FFAAC411EFA64E0028F8594024, '2024-11-24', 1, 27556786453, 'Usandivares', 'Eva Patricia', 'FEMENINO', 'epu@hmu.com.ar', NULL, 0x41CF5047AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0CDFFAAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3A3F2AAC411EFA64E0028F8594024, '2024-11-24', 1, 20454676443, 'Tolay', 'Edith', 'FEMENINO', 'etolay@hmu.com.ar', NULL, 0x41CF5362AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D067A0AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3A898AAC411EFA64E0028F8594024, '2024-11-24', 1, 20554543453, 'Terrieris', 'José Luis', 'MASCULINO', 'jterrieris@hmu.com.ar', NULL, 0x41CF514FAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D067A0AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3ABE3AAC411EFA64E0028F8594024, '2024-11-24', 1, 27540678443, 'Tarifa', 'Claudia', 'FEMENINO', 'clautarifa@hmu.com.ar', NULL, 0x41CF4F55AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D067A0AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3AEEEAAC411EFA64E0028F8594024, '2024-11-24', 1, 27554560863, 'Tapia', 'Erica', 'FEMENINO', 'erikatapia@hmu.com.ar', NULL, 0x41CF4ECFAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D12925AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3B0EBAAC411EFA64E0028F8594024, '2024-11-24', 1, 24784044443, 'Tampares', 'Demetrio', 'OTRO', 'demetam@hmu.com.ar', NULL, 0x41CF4F55AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D12925AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3B2B7AAC411EFA64E0028F8594024, '2024-11-24', 1, 20526456443, 'Suizer', 'Alejandro', 'MASCULINO', 'amsuizer@hmu.com.ar', NULL, 0x41CF54B4AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0402BAAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3B484AAC411EFA64E0028F8594024, '2024-11-24', 1, 20554897343, 'Suárez', 'David', 'MASCULINO', 'dsuarez@hmu.com.ar', NULL, 0x41CF523CAAC411EFA64E0028F8594024, 0x41D1D77BAAC411EFA64E0028F8594024, 0x41D02630AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3B820AAC411EFA64E0028F8594024, '2024-11-24', 1, 20554674843, 'Rius', 'Pedro', 'MASCULINO', 'pedrorius@hmu.com.ar', NULL, 0x41CF4B3FAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D056A8AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3BA59AAC411EFA64E0028F8594024, '2024-11-24', 1, 20558244443, 'Rabbat', 'Damian', 'MASCULINO', 'drabbat@hmu.com.ar', NULL, 0x41CF5443AAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D1344CAAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3BE0EAAC411EFA64E0028F8594024, '2024-11-24', 1, 20567974443, 'Puig', 'Ismael', 'MASCULINO', 'ipuig@hmu.com.ar', NULL, 0x41CF514FAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D1344CAAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3C0B4AAC411EFA64E0028F8594024, '2024-11-24', 1, 20456645843, 'Pascolo', 'Diego', 'MASCULINO', 'dpascolo@hmu.com.ar', NULL, 0x41CF4B3FAAC411EFA64E0028F8594024, 0x41D1BD20AAC411EFA64E0028F8594024, 0x41D0402BAAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x41D3C3A4AAC411EFA64E0028F8594024, '2024-11-24', 1, 24554978443, 'Bustos', 'Sebastián', 'OTRO', 'sebustos@hmu.com.ar', NULL, 0x41CF54B4AAC411EFA64E0028F8594024, 0x41D1D77BAAC411EFA64E0028F8594024, 0x41D02630AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$x9SDga8sk3DyjvdeIwkhl.2e9wcWHewfHEFQFKkFp1.FiAKcyRZUG', NULL);
INSERT INTO `usuario` VALUES (0x47DFDD81AAC411EFA64E0028F8594024, '2015-03-01', 1, 20112223331, 'Bonifacio', 'Aguirre', 'MASCULINO', 'bonifacio.a@hmu.com.ar', 3512223344, 0x41CF5443AAC411EFA64E0028F8594024, 0x41D1C7DCAAC411EFA64E0028F8594024, 0x41D060CAAAC411EFA64E0028F8594024, 'DIRECCION', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x47E16AD0AAC411EFA64E0028F8594024, '2012-07-01', 1, 20112223332, 'Valentino', 'Marquez', 'MASCULINO', 'vale.mar@hmu.com.ar', 3512223355, 0x41CF4F55AAC411EFA64E0028F8594024, 0x41D1D018AAC411EFA64E0028F8594024, 0x41D060CAAAC411EFA64E0028F8594024, 'JEFEDESERVICIO', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x47E2EA79AAC411EFA64E0028F8594024, '2010-12-15', 1, 27112223333, 'Felicitas', 'Herrera', 'FEMENINO', 'feliherrra@hmu.com.ar', 3512223366, 0x41CF5047AAC411EFA64E0028F8594024, 0x41D1E018AAC411EFA64E0028F8594024, 0x41D0CDFFAAC411EFA64E0028F8594024, 'OFICINADEPERSONAL', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x47E4F1ECAAC411EFA64E0028F8594024, '2021-09-01', 1, 20112223334, 'Luis', 'Masson', 'MASCULINO', 'lmasson@hmu.com.ar', 3512223377, 0x41CF4B3FAAC411EFA64E0028F8594024, 0x41D1D77BAAC411EFA64E0028F8594024, 0x41D090C9AAC411EFA64E0028F8594024, 'AGENTE', '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);

-- ----------------------------
-- Table structure for usuario_rol
-- ----------------------------
DROP TABLE IF EXISTS `usuario_rol`;
CREATE TABLE `usuario_rol`  (
  `usuario_id` binary(16) NOT NULL,
  `rol_id` binary(16) NOT NULL,
  PRIMARY KEY (`usuario_id`, `rol_id`) USING BTREE,
  INDEX `idx_usuario`(`usuario_id` ASC) USING BTREE,
  INDEX `idx_rol`(`rol_id` ASC) USING BTREE,
  CONSTRAINT `usuario_rol_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `usuario_rol_ibfk_2` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usuario_rol
-- ----------------------------
INSERT INTO `usuario_rol` VALUES (0x41D18896AAC411EFA64E0028F8594024, 0x41D9024BAAC411EFA64E0028F8594024);
INSERT INTO `usuario_rol` VALUES (0x41D18896AAC411EFA64E0028F8594024, 0x41D90C9BAAC411EFA64E0028F8594024);
INSERT INTO `usuario_rol` VALUES (0x41D19A8CAAC411EFA64E0028F8594024, 0x41D9024BAAC411EFA64E0028F8594024);
INSERT INTO `usuario_rol` VALUES (0x41D19A8CAAC411EFA64E0028F8594024, 0x41D90C9BAAC411EFA64E0028F8594024);
INSERT INTO `usuario_rol` VALUES (0x41D19A8CAAC411EFA64E0028F8594024, 0x41D91581AAC411EFA64E0028F8594024);
INSERT INTO `usuario_rol` VALUES (0x41D35018AAC411EFA64E0028F8594024, 0x41D90C9BAAC411EFA64E0028F8594024);
INSERT INTO `usuario_rol` VALUES (0x41D35018AAC411EFA64E0028F8594024, 0x41D91F9EAAC411EFA64E0028F8594024);
INSERT INTO `usuario_rol` VALUES (0x47DFDD81AAC411EFA64E0028F8594024, 0x41D91F9EAAC411EFA64E0028F8594024);
INSERT INTO `usuario_rol` VALUES (0x47E2EA79AAC411EFA64E0028F8594024, 0x41D91581AAC411EFA64E0028F8594024);

-- ----------------------------
-- Function structure for BIN_TO_UUID
-- ----------------------------
DROP FUNCTION IF EXISTS `BIN_TO_UUID`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `BIN_TO_UUID`(b BINARY(16)) RETURNS char(36) CHARSET ascii COLLATE ascii_general_ci
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
END
;;
delimiter ;

-- ----------------------------
-- Function structure for BIN_TO_UUID2
-- ----------------------------
DROP FUNCTION IF EXISTS `BIN_TO_UUID2`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `BIN_TO_UUID2`(b BINARY(16)) RETURNS char(36) CHARSET ascii COLLATE ascii_general_ci
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
END
;;
delimiter ;

-- ----------------------------
-- Function structure for UUID_TO_BIN
-- ----------------------------
DROP FUNCTION IF EXISTS `UUID_TO_BIN`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `UUID_TO_BIN`(uuid CHAR(36)) RETURNS binary(16)
    DETERMINISTIC
BEGIN
  RETURN UNHEX(CONCAT(
    SUBSTRING(uuid, 1, 8),      -- aaaaaaaa
    SUBSTRING(uuid, 10, 4),     -- bbbb
    SUBSTRING(uuid, 15, 4),     -- cccc
    SUBSTRING(uuid, 20, 4),     -- dddd
    SUBSTRING(uuid, 25, 12)     -- eeeeeeeeeeee
  ));
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
