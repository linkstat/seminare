/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MariaDB
 Source Server Version : 110502
 Source Host           : localhost:3306
 Source Schema         : aromito

 Target Server Type    : MariaDB
 Target Server Version : 110502
 File Encoding         : 65001

 Date: 10/11/2024 23:16:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for autorizacion
-- ----------------------------
DROP TABLE IF EXISTS `autorizacion`;
CREATE TABLE `autorizacion`  (
  `id` binary(16) NOT NULL,
  `fechaAutorizacion` datetime(0) NOT NULL,
  `tipo` enum('DIRECCION','JEFATURADESERVICIO','OFICINADEPERSONAL','USUARIO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `autorizadoPorID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `autorizadoPorID`(`autorizadoPorID`) USING BTREE,
  CONSTRAINT `autorizacion_ibfk_1` FOREIGN KEY (`autorizadoPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cargo
-- ----------------------------
DROP TABLE IF EXISTS `cargo`;
CREATE TABLE `cargo`  (
  `id` binary(16) NOT NULL,
  `numero` int(11) NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `agrupacion` enum('ADMINISTRATIVO','SERVICIO','MEDICO','ENFERMERIA','TECNICO','PLANTA POLITICA') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  INDEX `servicioID`(`servicioID`) USING BTREE,
  CONSTRAINT `diagramadeservicio_ibfk_1` FOREIGN KEY (`servicioID`) REFERENCES `servicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for direccion
-- ----------------------------
DROP TABLE IF EXISTS `direccion`;
CREATE TABLE `direccion`  (
  `id` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `direccion_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of direccion
-- ----------------------------
INSERT INTO `direccion` VALUES (0x0028F8594024A9A311EF8E9548EAF6AE);
INSERT INTO `direccion` VALUES (0x0028F8594024A9A311EF8E9548EB00E6);
INSERT INTO `direccion` VALUES (0x0028F8594024A9A311EF8E9548EB0553);
INSERT INTO `direccion` VALUES (0x0028F8594024A9A311EF8E9548EB0AF8);

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
  INDEX `idx_calle`(`calle`) USING BTREE,
  INDEX `idx_barrio`(`barrio`) USING BTREE,
  INDEX `idx_ciudad`(`ciudad`) USING BTREE,
  INDEX `idx_localidad`(`localidad`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of domicilio
-- ----------------------------
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E6359B, 'Defensa', 1200, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E63B5E, 'Lavalleja', 3050, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E63E95, 'Av Belgrano Sur', 134, NULL, 'La Rioja', NULL, 'La Rioja');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E64156, 'Av. Marcelo T. de Alvear', 120, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E643FA, 'Av. Libertador', 1450, NULL, 'Alta Gracia', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E646A5, 'Catamarca', 441, NULL, 'Córdoba', NULL, 'Córdoba');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E6492D, 'Junín', 880, NULL, 'Rosario', NULL, 'Santa Fe');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E64BD5, 'Mate de Luna', 290, NULL, 'Famaillá', NULL, 'Tucumán');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E64EA0, 'Gobernación', 811, NULL, 'Santa Fe', NULL, 'Santa Fe');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E65115, 'Güemes', 186, NULL, 'Iruya', NULL, 'Jujuy');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E65519, '9 de julio', 495, NULL, 'Corrientes', NULL, 'Corrientes');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E657D1, 'Av. del Trabajo', 914, NULL, 'Rosario', NULL, 'Santa Fe');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E65B15, 'Calle del Río', 88, NULL, 'Paraná', NULL, 'Entre Ríos');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E65F1B, 'Av. Central', 698, NULL, 'Salta', NULL, 'Salta');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E66221, 'Av. Siempre Viva', 222, NULL, 'Rawson', NULL, 'Chubut');
INSERT INTO `domicilio` VALUES (0x0028F8594024A9A311EF8E9548E663EC, 'San Martín', 18, NULL, 'Bariloche', NULL, 'Río Negro');

-- ----------------------------
-- Table structure for empleado
-- ----------------------------
DROP TABLE IF EXISTS `empleado`;
CREATE TABLE `empleado`  (
  `id` binary(16) NOT NULL,
  `francosCompensatoriosUtilizados` int(11) NULL DEFAULT NULL,
  `horarioActualID` binary(16) NULL DEFAULT NULL,
  `jefaturaID` binary(16) NOT NULL,
  `servicioID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `horarioActualID`(`horarioActualID`) USING BTREE,
  INDEX `servicioID`(`servicioID`) USING BTREE,
  INDEX `idx_empleado_jefatura`(`jefaturaID`) USING BTREE,
  CONSTRAINT `empleado_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `empleado_ibfk_2` FOREIGN KEY (`horarioActualID`) REFERENCES `horariobase` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `empleado_ibfk_3` FOREIGN KEY (`jefaturaID`) REFERENCES `jefaturadeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `empleado_ibfk_4` FOREIGN KEY (`servicioID`) REFERENCES `servicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of empleado
-- ----------------------------
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB50CE, 0, NULL, 0x0028F8594024A9A311EF8E9548E8360B, 0x0028F8594024A9A311EF8E9548E953F6);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB57B0, 0, NULL, 0x0028F8594024A9A311EF8E9548E87E20, 0x0028F8594024A9A311EF8E9548E9A30B);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB5E86, 0, NULL, 0x0028F8594024A9A311EF8E9548E8360B, 0x0028F8594024A9A311EF8E9548E953F6);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB6536, 0, NULL, 0x0028F8594024A9A311EF8E9548E85AB7, 0x0028F8594024A9A311EF8E9548E9718D);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB6BE6, 0, NULL, 0x0028F8594024A9A311EF8E9548E7D81A, 0x0028F8594024A9A311EF8E9548E90A1B);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB7297, 0, NULL, 0x0028F8594024A9A311EF8E9548E7F2C9, 0x0028F8594024A9A311EF8E9548E92D6E);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB7955, 0, NULL, 0x0028F8594024A9A311EF8E9548E79799, 0x0028F8594024A9A311EF8E9548E8B31F);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB8093, 0, NULL, 0x0028F8594024A9A311EF8E9548E7BCDD, 0x0028F8594024A9A311EF8E9548E8E30B);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB87A2, 0, NULL, 0x0028F8594024A9A311EF8E9548E7BCDD, 0x0028F8594024A9A311EF8E9548E8E30B);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB8E57, 0, NULL, 0x0028F8594024A9A311EF8E9548E8911F, 0x0028F8594024A9A311EF8E9548E9D8C0);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB95EA, 0, NULL, 0x0028F8594024A9A311EF8E9548E7F2C9, 0x0028F8594024A9A311EF8E9548E92D6E);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EB9C7C, 0, NULL, 0x0028F8594024A9A311EF8E9548E8360B, 0x0028F8594024A9A311EF8E9548E953F6);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EBA2CF, 0, NULL, 0x0028F8594024A9A311EF8E9548E85AB7, 0x0028F8594024A9A311EF8E9548E9718D);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EBA96D, 0, NULL, 0x0028F8594024A9A311EF8E9548E85AB7, 0x0028F8594024A9A311EF8E9548E9718D);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EBB005, 0, NULL, 0x0028F8594024A9A311EF8E9548E7D81A, 0x0028F8594024A9A311EF8E9548E90A1B);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EBBED0, 0, NULL, 0x0028F8594024A9A311EF8E9548E7AB21, 0x0028F8594024A9A311EF8E9548E8C34F);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EBC567, 0, NULL, 0x0028F8594024A9A311EF8E9548E85AB7, 0x0028F8594024A9A311EF8E9548E9718D);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EBCC9F, 0, NULL, 0x0028F8594024A9A311EF8E9548E7E4C9, 0x0028F8594024A9A311EF8E9548E91C4E);
INSERT INTO `empleado` VALUES (0x0028F8594024A9A311EF8E9548EBD648, 0, NULL, 0x0028F8594024A9A311EF8E9548E85AB7, 0x0028F8594024A9A311EF8E9548E9718D);

-- ----------------------------
-- Table structure for empleado_novedad
-- ----------------------------
DROP TABLE IF EXISTS `empleado_novedad`;
CREATE TABLE `empleado_novedad`  (
  `empleadoID` binary(16) NOT NULL,
  `novedadID` binary(16) NOT NULL,
  PRIMARY KEY (`empleadoID`, `novedadID`) USING BTREE,
  INDEX `novedadID`(`novedadID`) USING BTREE,
  CONSTRAINT `empleado_novedad_ibfk_1` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `empleado_novedad_ibfk_2` FOREIGN KEY (`novedadID`) REFERENCES `novedad` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
-- Table structure for francocompensatorio
-- ----------------------------
DROP TABLE IF EXISTS `francocompensatorio`;
CREATE TABLE `francocompensatorio`  (
  `id` binary(16) NOT NULL,
  `cantHoras` double NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaAutorizacion` datetime(0) NULL DEFAULT NULL,
  `fechaDeAplicacion` date NULL DEFAULT NULL,
  `estadoTramiteID` binary(16) NOT NULL,
  `autorizadaPorID` binary(16) NULL DEFAULT NULL,
  `empleadoID` binary(16) NOT NULL,
  `jefaturaID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estadoTramiteID`(`estadoTramiteID`) USING BTREE,
  INDEX `autorizadaPorID`(`autorizadaPorID`) USING BTREE,
  INDEX `empleadoID`(`empleadoID`) USING BTREE,
  INDEX `jefaturaID`(`jefaturaID`) USING BTREE,
  CONSTRAINT `francocompensatorio_ibfk_1` FOREIGN KEY (`estadoTramiteID`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `francocompensatorio_ibfk_2` FOREIGN KEY (`autorizadaPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `francocompensatorio_ibfk_3` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `francocompensatorio_ibfk_4` FOREIGN KEY (`jefaturaID`) REFERENCES `jefaturadeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for horaextra
-- ----------------------------
DROP TABLE IF EXISTS `horaextra`;
CREATE TABLE `horaextra`  (
  `id` binary(16) NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaIngreso` datetime(0) NOT NULL,
  `fechaEgreso` datetime(0) NOT NULL,
  `ponderacion` int(11) NOT NULL,
  `fechaAutorizacion` datetime(0) NULL DEFAULT NULL,
  `estadoTramiteID` binary(16) NOT NULL,
  `autorizadaPorID` binary(16) NULL DEFAULT NULL,
  `empleadoID` binary(16) NOT NULL,
  `jefaturaID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estadoTramiteID`(`estadoTramiteID`) USING BTREE,
  INDEX `autorizadaPorID`(`autorizadaPorID`) USING BTREE,
  INDEX `empleadoID`(`empleadoID`) USING BTREE,
  INDEX `jefaturaID`(`jefaturaID`) USING BTREE,
  CONSTRAINT `horaextra_ibfk_1` FOREIGN KEY (`estadoTramiteID`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horaextra_ibfk_2` FOREIGN KEY (`autorizadaPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horaextra_ibfk_3` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horaextra_ibfk_4` FOREIGN KEY (`jefaturaID`) REFERENCES `jefaturadeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for horario
-- ----------------------------
DROP TABLE IF EXISTS `horario`;
CREATE TABLE `horario`  (
  `id` binary(16) NOT NULL,
  `fechaIngreso` datetime(0) NOT NULL,
  `fechaEgreso` datetime(0) NOT NULL,
  `jornadasPlanificadas` int(11) NULL DEFAULT NULL,
  `reglasHorario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `horarioBaseID` binary(16) NOT NULL,
  `modalidad` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `horario_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horariobase` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
-- Table structure for horariobase
-- ----------------------------
DROP TABLE IF EXISTS `horariobase`;
CREATE TABLE `horariobase`  (
  `id` binary(16) NOT NULL,
  `tipo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for horarioconfranquicia
-- ----------------------------
DROP TABLE IF EXISTS `horarioconfranquicia`;
CREATE TABLE `horarioconfranquicia`  (
  `id` binary(16) NOT NULL,
  `fechaIngreso` datetime(0) NOT NULL,
  `fechaEgreso` datetime(0) NOT NULL,
  `horasFranquicia` int(11) NOT NULL,
  `horarioDecoradoID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `horarioDecoradoID`(`horarioDecoradoID`) USING BTREE,
  CONSTRAINT `horarioconfranquicia_ibfk_1` FOREIGN KEY (`id`) REFERENCES `horariobase` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `horarioconfranquicia_ibfk_2` FOREIGN KEY (`horarioDecoradoID`) REFERENCES `horario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
-- Table structure for jefaturadeservicio
-- ----------------------------
DROP TABLE IF EXISTS `jefaturadeservicio`;
CREATE TABLE `jefaturadeservicio`  (
  `id` binary(16) NOT NULL,
  `servicioID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `servicioID`(`servicioID`) USING BTREE,
  CONSTRAINT `jefaturadeservicio_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `jefaturadeservicio_ibfk_2` FOREIGN KEY (`servicioID`) REFERENCES `servicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jefaturadeservicio
-- ----------------------------
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E79799, 0x0028F8594024A9A311EF8E9548E8B31F);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E7AB21, 0x0028F8594024A9A311EF8E9548E8C34F);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E7BCDD, 0x0028F8594024A9A311EF8E9548E8E30B);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E7CB49, 0x0028F8594024A9A311EF8E9548E8F7F0);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E7D81A, 0x0028F8594024A9A311EF8E9548E90A1B);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E7E4C9, 0x0028F8594024A9A311EF8E9548E91C4E);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E7F2C9, 0x0028F8594024A9A311EF8E9548E92D6E);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E814B1, 0x0028F8594024A9A311EF8E9548E93E0B);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E8360B, 0x0028F8594024A9A311EF8E9548E953F6);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E85AB7, 0x0028F8594024A9A311EF8E9548E9718D);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E87E20, 0x0028F8594024A9A311EF8E9548E9A30B);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E8911F, 0x0028F8594024A9A311EF8E9548E9D8C0);
INSERT INTO `jefaturadeservicio` VALUES (0x0028F8594024A9A311EF8E9548E8A1E9, 0x0028F8594024A9A311EF8E9548EA0636);

-- ----------------------------
-- Table structure for jornadalaboral
-- ----------------------------
DROP TABLE IF EXISTS `jornadalaboral`;
CREATE TABLE `jornadalaboral`  (
  `id` binary(16) NOT NULL,
  `fechaIngreso` datetime(0) NOT NULL,
  `fechaEgreso` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for marcacionempleado
-- ----------------------------
DROP TABLE IF EXISTS `marcacionempleado`;
CREATE TABLE `marcacionempleado`  (
  `id` binary(16) NOT NULL,
  `fechaMarcacion` datetime(0) NOT NULL,
  `observaciones` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tipoMarcacion` enum('INGRESO','EGRESO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `validada` tinyint(1) NOT NULL,
  `empleadoID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `empleadoID`(`empleadoID`) USING BTREE,
  CONSTRAINT `marcacionempleado_ibfk_1` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for memorandum
-- ----------------------------
DROP TABLE IF EXISTS `memorandum`;
CREATE TABLE `memorandum`  (
  `id` binary(16) NOT NULL,
  `asunto` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `contenido` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fechaEnvio` datetime(0) NULL DEFAULT NULL,
  `fechaRecepcion` datetime(0) NULL DEFAULT NULL,
  `estado` binary(16) NOT NULL,
  `remitenteID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estado`(`estado`) USING BTREE,
  INDEX `remitenteID`(`remitenteID`) USING BTREE,
  CONSTRAINT `memorandum_ibfk_1` FOREIGN KEY (`estado`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_ibfk_2` FOREIGN KEY (`remitenteID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for memorandum_autorizacion
-- ----------------------------
DROP TABLE IF EXISTS `memorandum_autorizacion`;
CREATE TABLE `memorandum_autorizacion`  (
  `id` binary(16) NOT NULL,
  `memorandumID` binary(16) NOT NULL,
  `tipoAutorizacionID` enum('JefeDeServicio','OficinaDePersonal','Direccion','Usuario') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `autorizadoPorID` binary(16) NULL DEFAULT NULL,
  `fechaAutorizacion` datetime(0) NULL DEFAULT NULL,
  `estado` enum('PENDIENTE','AUTORIZADO','RECHAZADO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `memorandumID`(`memorandumID`) USING BTREE,
  INDEX `autorizadoPorID`(`autorizadoPorID`) USING BTREE,
  CONSTRAINT `memorandum_autorizacion_ibfk_1` FOREIGN KEY (`memorandumID`) REFERENCES `memorandum` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_autorizacion_ibfk_2` FOREIGN KEY (`autorizadoPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for memorandum_destinatario
-- ----------------------------
DROP TABLE IF EXISTS `memorandum_destinatario`;
CREATE TABLE `memorandum_destinatario`  (
  `memorandumID` binary(16) NOT NULL,
  `usuarioID` binary(16) NOT NULL,
  `fechaRecepcion` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`memorandumID`, `usuarioID`) USING BTREE,
  INDEX `usuarioID`(`usuarioID`) USING BTREE,
  CONSTRAINT `memorandum_destinatario_ibfk_1` FOREIGN KEY (`memorandumID`) REFERENCES `memorandum` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_destinatario_ibfk_2` FOREIGN KEY (`usuarioID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for memorandum_firmante
-- ----------------------------
DROP TABLE IF EXISTS `memorandum_firmante`;
CREATE TABLE `memorandum_firmante`  (
  `memorandumID` binary(16) NOT NULL,
  `usuarioID` binary(16) NOT NULL,
  `fechaFirma` datetime(0) NOT NULL,
  PRIMARY KEY (`memorandumID`, `usuarioID`) USING BTREE,
  INDEX `usuarioID`(`usuarioID`) USING BTREE,
  CONSTRAINT `memorandum_firmante_ibfk_1` FOREIGN KEY (`memorandumID`) REFERENCES `memorandum` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `memorandum_firmante_ibfk_2` FOREIGN KEY (`usuarioID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for novedad
-- ----------------------------
DROP TABLE IF EXISTS `novedad`;
CREATE TABLE `novedad`  (
  `id` binary(16) NOT NULL,
  `cod` int(11) NOT NULL,
  `descripcion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estado` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estadoFechaModif` timestamp(0) NULL DEFAULT NULL,
  `fechaInicio` date NULL DEFAULT NULL,
  `fechaFin` date NULL DEFAULT NULL,
  `fechaSolicitud` date NULL DEFAULT NULL,
  `reqAprobDireccion` tinyint(1) NOT NULL,
  `estadoTramiteID` binary(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `estadoTramiteID`(`estadoTramiteID`) USING BTREE,
  CONSTRAINT `novedad_ibfk_1` FOREIGN KEY (`estadoTramiteID`) REFERENCES `estadotramite` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oficinadepersonal
-- ----------------------------
DROP TABLE IF EXISTS `oficinadepersonal`;
CREATE TABLE `oficinadepersonal`  (
  `id` binary(16) NOT NULL,
  `reportesGenerados` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `oficinadepersonal_ibfk_1` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oficinadepersonal
-- ----------------------------
INSERT INTO `oficinadepersonal` VALUES (0x0028F8594024A9A311EF8E9548EB496A, 0);

-- ----------------------------
-- Table structure for partediario
-- ----------------------------
DROP TABLE IF EXISTS `partediario`;
CREATE TABLE `partediario`  (
  `id` binary(16) NOT NULL,
  `fechaDeCierre` datetime(0) NULL DEFAULT NULL,
  `periodo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `modificadoPorID` binary(16) NULL DEFAULT NULL,
  `oficinaID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `modificadoPorID`(`modificadoPorID`) USING BTREE,
  INDEX `oficinaID`(`oficinaID`) USING BTREE,
  CONSTRAINT `partediario_ibfk_1` FOREIGN KEY (`modificadoPorID`) REFERENCES `usuario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `partediario_ibfk_2` FOREIGN KEY (`oficinaID`) REFERENCES `oficinadepersonal` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for partediario_empleado
-- ----------------------------
DROP TABLE IF EXISTS `partediario_empleado`;
CREATE TABLE `partediario_empleado`  (
  `parteDiarioID` binary(16) NOT NULL,
  `empleadoID` binary(16) NOT NULL,
  PRIMARY KEY (`parteDiarioID`, `empleadoID`) USING BTREE,
  INDEX `empleadoID`(`empleadoID`) USING BTREE,
  CONSTRAINT `partediario_empleado_ibfk_1` FOREIGN KEY (`parteDiarioID`) REFERENCES `partediario` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `partediario_empleado_ibfk_2` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for planificacion
-- ----------------------------
DROP TABLE IF EXISTS `planificacion`;
CREATE TABLE `planificacion`  (
  `diagramaID` binary(16) NOT NULL,
  `empleadoID` binary(16) NOT NULL,
  `jornadaID` binary(16) NOT NULL,
  PRIMARY KEY (`diagramaID`, `empleadoID`, `jornadaID`) USING BTREE,
  INDEX `empleadoID`(`empleadoID`) USING BTREE,
  INDEX `jornadaID`(`jornadaID`) USING BTREE,
  CONSTRAINT `planificacion_ibfk_1` FOREIGN KEY (`diagramaID`) REFERENCES `diagramadeservicio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `planificacion_ibfk_2` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `planificacion_ibfk_3` FOREIGN KEY (`jornadaID`) REFERENCES `jornadalaboral` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for registrojornadalaboral
-- ----------------------------
DROP TABLE IF EXISTS `registrojornadalaboral`;
CREATE TABLE `registrojornadalaboral`  (
  `id` binary(16) NOT NULL,
  `fecha` date NOT NULL,
  `empleadoID` binary(16) NOT NULL,
  `marcacionIngresoID` binary(16) NOT NULL,
  `marcacionEgresoID` binary(16) NOT NULL,
  `duracionJornada` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `empleadoID`(`empleadoID`) USING BTREE,
  INDEX `marcacionIngresoID`(`marcacionIngresoID`) USING BTREE,
  INDEX `marcacionEgresoID`(`marcacionEgresoID`) USING BTREE,
  CONSTRAINT `registrojornadalaboral_ibfk_1` FOREIGN KEY (`empleadoID`) REFERENCES `empleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `registrojornadalaboral_ibfk_2` FOREIGN KEY (`marcacionIngresoID`) REFERENCES `marcacionempleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `registrojornadalaboral_ibfk_3` FOREIGN KEY (`marcacionEgresoID`) REFERENCES `marcacionempleado` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for servicio
-- ----------------------------
DROP TABLE IF EXISTS `servicio`;
CREATE TABLE `servicio`  (
  `id` binary(16) NOT NULL,
  `nombre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `agrupacion` enum('ADMINISTRATIVO','SERVICIO','MEDICO','ENFERMERIA','TECNICO','PLANTA POLITICA') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `direccionID` binary(16) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_servicio_direccion`(`direccionID`) USING BTREE,
  CONSTRAINT `servicio_ibfk_1` FOREIGN KEY (`direccionID`) REFERENCES `direccion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of servicio
-- ----------------------------
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E8B31F, 'Admisión', 'ADMINISTRATIVO', 0x0028F8594024A9A311EF8E9548EB0AF8);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E8C34F, 'Biomédica', 'TECNICO', 0x0028F8594024A9A311EF8E9548EB0AF8);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E8E30B, 'Cardiología', 'MEDICO', 0x0028F8594024A9A311EF8E9548EAF6AE);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E8F7F0, 'Cirugía', 'MEDICO', 0x0028F8594024A9A311EF8E9548EAF6AE);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E90A1B, 'Diagnóstico por imágenes', 'MEDICO', 0x0028F8594024A9A311EF8E9548EAF6AE);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E91C4E, 'Enfermería', 'ENFERMERIA', 0x0028F8594024A9A311EF8E9548EAF6AE);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E92D6E, 'Esterilización', 'SERVICIO', 0x0028F8594024A9A311EF8E9548EB0AF8);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E93E0B, 'Farmacia', 'SERVICIO', 0x0028F8594024A9A311EF8E9548EB0AF8);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E953F6, 'Habilitación', 'ADMINISTRATIVO', 0x0028F8594024A9A311EF8E9548EAF6AE);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E9718D, 'Informática', 'TECNICO', 0x0028F8594024A9A311EF8E9548EB0AF8);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E9A30B, 'Nutrición', 'SERVICIO', 0x0028F8594024A9A311EF8E9548EB0AF8);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548E9D8C0, 'Traumatología', 'MEDICO', 0x0028F8594024A9A311EF8E9548EAF6AE);
INSERT INTO `servicio` VALUES (0x0028F8594024A9A311EF8E9548EA0636, 'Toxicología', 'MEDICO', 0x0028F8594024A9A311EF8E9548EAF6AE);

-- ----------------------------
-- Table structure for usuario
-- ----------------------------
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario`  (
  `id` binary(16) NOT NULL,
  `fechaAlta` date NOT NULL,
  `cuil` bigint(20) NOT NULL,
  `apellidos` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nombres` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sexo` enum('FEMENINO','MASCULINO','OTRO') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tipoUsuario` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `domicilioID` binary(16) NULL DEFAULT NULL,
  `tel` bigint(20) NULL DEFAULT NULL,
  `cargoID` binary(16) NULL DEFAULT NULL,
  `passwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `profile_image` blob NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_cuil`(`cuil`) USING BTREE,
  UNIQUE INDEX `idx_mail`(`mail`) USING BTREE,
  UNIQUE INDEX `idx_tel`(`tel`) USING BTREE,
  INDEX `idx_domicilioID`(`domicilioID`) USING BTREE,
  INDEX `idx_cargoID`(`cargoID`) USING BTREE,
  INDEX `idx_apellidos`(`apellidos`) USING BTREE,
  INDEX `idx_nombres`(`nombres`) USING BTREE,
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`domicilioID`) REFERENCES `domicilio` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `usuario_ibfk_2` FOREIGN KEY (`cargoID`) REFERENCES `cargo` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usuario
-- ----------------------------
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E79799, '2024-10-20', 27295554447, 'Balconte', 'Andrea', 'FEMENINO', 1, 'andrea@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E65519, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E7AB21, '2024-10-20', 27274422442, 'Maestro', 'Silvina', 'FEMENINO', 1, 'smaestro@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E65115, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E7BCDD, '2024-10-20', 20124543421, 'Aniceto', 'Juan', 'MASCULINO', 1, 'janiceto@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E64BD5, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E7CB49, '2024-10-20', 20268944448, 'Titarelli', 'Maximiliano', 'MASCULINO', 1, 'drtita@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E63B5E, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E7D81A, '2024-10-20', 20281324547, 'Morales', 'Juan Ignacio', 'MASCULINO', 1, 'jimorales@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E6492D, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E7E4C9, '2024-10-20', 27224444445, 'Plaza', 'Tania', 'FEMENINO', 1, 'tplaza@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E63E95, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E7F2C9, '2024-10-20', 20289445441, 'Pérez Cabral', 'Matías', 'MASCULINO', 1, 'mcp@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E66221, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E814B1, '2024-10-20', 27254344447, 'Arancibia', 'María Pía', 'FEMENINO', 1, 'piaarancibia@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E66221, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E8360B, '2024-10-20', 20276444446, 'Roberts', 'Carlos Fernando', 'MASCULINO', 1, 'ferroberts@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E657D1, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E85AB7, '2024-10-20', 20220361118, 'Roqué', 'Juan Manuel', 'OTRO', 1, 'jmroque@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E63E95, 3517553799, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E87E20, '2024-10-20', 27174444446, 'Boqué', 'Alejandra', 'FEMENINO', 1, 'aleboque@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E65B15, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E8911F, '2024-10-20', 20124644445, 'Sánchez', 'Omar Wenceslao', 'MASCULINO', 1, 'owsanchez@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E63E95, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548E8A1E9, '2024-10-20', 27214434447, 'Vilkelis', 'Andrea', 'FEMENINO', 1, 'avilkelis@hmu.com.ar', 'JefaturaDeServicio', 0x0028F8594024A9A311EF8E9548E6359B, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EAF6AE, '2024-10-20', 20224448885, 'Marino', 'Mariano Gustavo', 'MASCULINO', 1, 'direccion@hmu.com.ar', 'Direccion', 0x0028F8594024A9A311EF8E9548E646A5, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB00E6, '2024-10-20', 20259993331, 'Huergo Sánchez', 'Federico', 'MASCULINO', 1, 'fedesubdir@hmu.com.ar', 'Direccion', 0x0028F8594024A9A311EF8E9548E63B5E, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB0553, '2024-10-20', 27129997773, 'Longoni', 'Gloria', 'FEMENINO', 1, 'subdirectora@hmu.com.ar', 'Direccion', 0x0028F8594024A9A311EF8E9548E643FA, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB0AF8, '2024-10-20', 20239997772, 'Vitali', 'Fabricio', 'MASCULINO', 1, 'subdireccion@hmu.com.ar', 'Direccion', 0x0028F8594024A9A311EF8E9548E646A5, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB496A, '2024-10-20', 27284644443, 'Maurino', 'Florencia', 'FEMENINO', 1, 'flormaurino@hmu.com.ar', 'OficinaDePersonal', 0x0028F8594024A9A311EF8E9548E65B15, NULL, NULL, '$2a$10$WsqtGM6XEQFBeSvXmrm8NutwUpFR.S0PpgY15Uvx5xSUU6ILLqDk6', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB50CE, '2024-10-20', 20554644448, 'Garzón', 'Baltazar', 'MASCULINO', 1, 'baltig@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E63E95, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB57B0, '2024-10-20', 20154786445, 'Taborda', 'Pedro', 'MASCULINO', 1, 'pltaborda@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E65115, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB5E86, '2024-10-20', 27554664563, 'Vargas Ruíz', 'María Laura', 'FEMENINO', 1, 'mlvargas@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E6492D, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB6536, '2024-10-20', 27554685443, 'Vignetta', 'María Celeste', 'FEMENINO', 1, 'mcv@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E643FA, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB6BE6, '2024-10-20', 27554567443, 'Vivas', 'Alicia', 'FEMENINO', 1, 'avivas@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E63B5E, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB7297, '2024-10-20', 20554649743, 'Cnga Castellanos', 'Matías Quique', 'MASCULINO', 1, 'lestat@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E65F1B, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB7955, '2024-10-20', 24574741745, 'Garay', 'Mauricio Elio', 'OTRO', 1, 'mgaray@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E6359B, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB8093, '2024-10-20', 27556786453, 'Usandivares', 'Eva Patricia', 'FEMENINO', 1, 'epu@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E65519, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB87A2, '2024-10-20', 20454676443, 'Tolay', 'Edith', 'FEMENINO', 1, 'etolay@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E65F1B, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB8E57, '2024-10-20', 20554543453, 'Terrieris', 'José Luis', 'MASCULINO', 1, 'jterrieris@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E64156, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB95EA, '2024-10-20', 27540678443, 'Tarifa', 'Claudia', 'FEMENINO', 1, 'clautarifa@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E65115, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EB9C7C, '2024-10-20', 27554560863, 'Tapia', 'Erica', 'FEMENINO', 1, 'erikatapia@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E64EA0, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EBA2CF, '2024-10-20', 24784044443, 'Tampares', 'Demetrio', 'OTRO', 1, 'demetam@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E66221, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EBA96D, '2024-10-20', 20526456443, 'Suizer', 'Alejandro', 'MASCULINO', 1, 'amsuizer@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E65F1B, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EBB005, '2024-10-20', 20554897343, 'Suárez', 'David', 'MASCULINO', 1, 'dsuarez@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E66221, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EBBED0, '2024-10-20', 20554674843, 'Rius', 'Pedro', 'MASCULINO', 1, 'pedrorius@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E64EA0, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EBC567, '2024-10-20', 20558244443, 'Rabbat', 'Damian', 'MASCULINO', 1, 'drabbat@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E64EA0, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EBCC9F, '2024-10-20', 20567974443, 'Puig', 'Ismael', 'MASCULINO', 1, 'ipuig@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E663EC, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EBD648, '2024-10-20', 20456645843, 'Pascolo', 'Diego', 'MASCULINO', 1, 'dpascolo@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E65F1B, NULL, NULL, '$2a$12$dUczG8OtzxElYH.qAXbtUOHVKORj4zJH2eoy5WXfXqXUm0vRq3obO', NULL);
INSERT INTO `usuario` VALUES (0x0028F8594024A9A311EF8E9548EBEF31, '2024-10-20', 24554978443, 'Bustos', 'Sebastián', 'OTRO', 1, 'sebustos@hmu.com.ar', 'Empleado', 0x0028F8594024A9A311EF8E9548E66221, NULL, NULL, '$2a$12$x9SDga8sk3DyjvdeIwkhl.2e9wcWHewfHEFQFKkFp1.FiAKcyRZUG', NULL);

-- ----------------------------
-- Function structure for BIN_TO_UUID2
-- ----------------------------
DROP FUNCTION IF EXISTS `BIN_TO_UUID2`;
delimiter ;;
CREATE FUNCTION `BIN_TO_UUID2`(b BINARY(16))
 RETURNS char(36) CHARSET ascii COLLATE ascii_general_ci
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
CREATE FUNCTION `UUID_TO_BIN`(uuid CHAR(36))
 RETURNS binary(16)
  DETERMINISTIC
BEGIN
  RETURN UNHEX(CONCAT(
    SUBSTRING(uuid, 25, 12),
    SUBSTRING(uuid, 20, 4),
    SUBSTRING(uuid, 15, 4),
    SUBSTRING(uuid, 10, 4),
    SUBSTRING(uuid, 1, 8)
  ));
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table empleado
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_check_servicio_consistente_before_insert`;
delimiter ;;
CREATE TRIGGER `trg_check_servicio_consistente_before_insert` BEFORE INSERT ON `empleado` FOR EACH ROW BEGIN
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
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table empleado
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_check_servicio_consistente_before_update`;
delimiter ;;
CREATE TRIGGER `trg_check_servicio_consistente_before_update` BEFORE UPDATE ON `empleado` FOR EACH ROW BEGIN
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
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
