-- consulta  general
-- SELECT hex(idEmpleado) AS idEmpleado, nombre, apellido, cuil FROM Empleado;

/*
-- Listar todos los servicios
SELECT 
    CONCAT_WS('-', 
        SUBSTR(HEX(idServicio), 1, 8),
        SUBSTR(HEX(idServicio), 9, 4),
        SUBSTR(HEX(idServicio), 13, 4),
        SUBSTR(HEX(idServicio), 17, 4),
        SUBSTR(HEX(idServicio), 21)
    ) AS idServicio_legible
FROM Servicio
WHERE nombreServicio = 'Cardiología';
*/

-- mostrar los emplados del agrupamiento médico
SELECT 
    HEX(e.idEmpleado) AS idEmpleado, 
    e.nombre, 
    e.apellido, 
    e.cuil, 
    s.nombreServicio
FROM 
    Empleado e
JOIN 
    Servicio s ON e.servicio_id = s.idServicio
WHERE 
    s.agrupacion = 'medico';
