-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-12-2025 a las 00:42:16
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `hrs`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumnos`
--

CREATE TABLE `alumnos` (
  `id` bigint(20) NOT NULL,
  `apellido` varchar(255) NOT NULL,
  `dni` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `telefono` int(11) NOT NULL,
  `activo` bit(1) NOT NULL,
  `cantidad_clases` int(11) NOT NULL,
  `fecha_inscripcion` date NOT NULL,
  `propietario` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alumnos`
--

INSERT INTO `alumnos` (`id`, `apellido`, `dni`, `email`, `fecha_nacimiento`, `nombre`, `telefono`, `activo`, `cantidad_clases`, `fecha_inscripcion`, `propietario`) VALUES
(2, 'Amicone', 25726035, 'luckidetrenque@gmail.com', '1977-01-26', 'Luciano', 1163069810, b'1', 12, '2025-12-05', b'1'),
(3, 'Rodriguez', 50706759, 'catalina@gmail.com', '2011-07-01', 'Catalina', 1163069810, b'0', 8, '2025-12-11', b'0'),
(4, 'Rodriguez', 55897254, '', '2016-07-06', 'Paulina', 657854869, b'0', 12, '2025-12-18', b'0'),
(6, 'Della Valle', 25988783, '', '1977-05-26', 'Nora', 65875485, b'0', 4, '2025-12-19', b'0'),
(7, 'Rodriguez', 25897548, '', '1987-05-26', 'Luciano', 47586969, b'1', 16, '2025-12-20', b'0'),
(8, 'Rodriguez', 20321547, 'mariela@gmail.com', '2001-12-26', 'Mariela', 2325689, b'1', 16, '2025-12-23', b'1');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `caballos`
--

CREATE TABLE `caballos` (
  `id` bigint(20) NOT NULL,
  `disponible` bit(1) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `tipo_caballo` enum('ESCUELA','PRIVADO') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `caballos`
--

INSERT INTO `caballos` (`id`, `disponible`, `nombre`, `tipo_caballo`) VALUES
(1, b'1', 'Apolo', 'ESCUELA'),
(2, b'1', 'Paisanito', 'ESCUELA'),
(3, b'1', 'Lobo', 'ESCUELA'),
(4, b'1', 'Pangare', 'ESCUELA'),
(5, b'1', 'Agatha', 'ESCUELA'),
(6, b'1', 'Ninja', 'ESCUELA'),
(7, b'1', 'Morocha', 'ESCUELA'),
(8, b'1', 'Toledo', 'ESCUELA'),
(9, b'1', 'Faustina', 'ESCUELA'),
(10, b'1', 'Pico', 'ESCUELA'),
(11, b'1', 'Palomo', 'ESCUELA'),
(12, b'1', 'Rocky', 'ESCUELA'),
(13, b'1', 'Alegra', 'PRIVADO'),
(14, b'1', 'Neo', 'PRIVADO'),
(15, b'1', 'Caliope', 'PRIVADO'),
(16, b'1', 'Aramis', 'PRIVADO'),
(17, b'1', 'Flecha', 'PRIVADO'),
(18, b'1', 'Luna', 'PRIVADO'),
(19, b'1', 'Choco', 'PRIVADO'),
(20, b'1', 'Caramelo', 'PRIVADO'),
(21, b'1', 'Franz', 'PRIVADO'),
(22, b'1', 'Adorado', 'PRIVADO'),
(23, b'1', 'Tordo', 'PRIVADO'),
(24, b'1', 'Lucky', 'PRIVADO'),
(25, b'1', 'Cruzada', 'PRIVADO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clases`
--

CREATE TABLE `clases` (
  `id` bigint(20) NOT NULL,
  `dia` date NOT NULL,
  `especialidad` enum('ADIESTRAMIENTO','EQUINOTERAPIA','EQUITACION') NOT NULL,
  `estado` enum('CANCELADA','COMPLETADA','EN_CURSO','PROGRAMADA') NOT NULL,
  `hora` time(6) NOT NULL,
  `observaciones` varchar(50) DEFAULT NULL,
  `alumno_id` bigint(20) NOT NULL,
  `caballo_id` bigint(20) NOT NULL,
  `instructor_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clases`
--

INSERT INTO `clases` (`id`, `dia`, `especialidad`, `estado`, `hora`, `observaciones`, `alumno_id`, `caballo_id`, `instructor_id`) VALUES
(1, '2025-12-26', 'EQUITACION', 'PROGRAMADA', '20:00:00.000000', '', 7, 5, 1),
(2, '2025-12-26', 'EQUITACION', 'PROGRAMADA', '21:00:00.000000', '', 2, 5, 1),
(3, '2025-12-26', 'EQUITACION', 'PROGRAMADA', '21:43:00.000000', '', 2, 2, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `instructores`
--

CREATE TABLE `instructores` (
  `id` bigint(20) NOT NULL,
  `apellido` varchar(255) NOT NULL,
  `dni` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `telefono` int(11) NOT NULL,
  `activo` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `instructores`
--

INSERT INTO `instructores` (`id`, `apellido`, `dni`, `email`, `fecha_nacimiento`, `nombre`, `telefono`, `activo`) VALUES
(1, 'Moreno', 22914785, 'mariela@gmail.com', '1974-12-02', 'Claudia', 2325689, b'1'),
(2, 'Perez', 29852147, 'lili@gmail.com', '1974-12-02', 'Lilian', 1254783, b'1');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKf2c9i14wnebiikl4lldh5yam2` (`dni`);

--
-- Indices de la tabla `caballos`
--
ALTER TABLE `caballos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `clases`
--
ALTER TABLE `clases`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3i2yra9bwri0qopqgbkuhisff` (`alumno_id`),
  ADD KEY `FKk61heaydt3i4b4ocdwsikjtdb` (`caballo_id`),
  ADD KEY `FKamr1i7vbaau2m29b645ono8t3` (`instructor_id`);

--
-- Indices de la tabla `instructores`
--
ALTER TABLE `instructores`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKqriq18yokttoy92wix3l08sfn` (`dni`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `caballos`
--
ALTER TABLE `caballos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT de la tabla `clases`
--
ALTER TABLE `clases`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `instructores`
--
ALTER TABLE `instructores`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `clases`
--
ALTER TABLE `clases`
  ADD CONSTRAINT `FK3i2yra9bwri0qopqgbkuhisff` FOREIGN KEY (`alumno_id`) REFERENCES `alumnos` (`id`),
  ADD CONSTRAINT `FKamr1i7vbaau2m29b645ono8t3` FOREIGN KEY (`instructor_id`) REFERENCES `instructores` (`id`),
  ADD CONSTRAINT `FKk61heaydt3i4b4ocdwsikjtdb` FOREIGN KEY (`caballo_id`) REFERENCES `caballos` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
