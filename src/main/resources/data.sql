-- ============================================================
--  Datos de prueba - Epicentro Gourmet - Grupo 09
--  Se ejecuta solo en cada arranque de la aplicacion.
--  Empieza borrando todo, asi cada arranque deja la base
--  siempre en el mismo estado conocido.
--  Claves: admin1234 / resp1234 / emp1234  (guardadas con BCrypt)
-- ============================================================

-- ---------- limpieza (hijos primero, por las claves foraneas) ----------
DELETE FROM item_pedido;
DELETE FROM pedido;
DELETE FROM plato;
DELETE FROM cocinero;
DELETE FROM cajero;
DELETE FROM personal;
DELETE FROM food_truck;
DELETE FROM puesto_desarmable;
DELETE FROM unidad_de_venta;
DELETE FROM costo;
DELETE FROM festival;
DELETE FROM usuario_rol;
DELETE FROM usuario;

-- ---------- FESTIVALES ----------
-- 1: ya termino, dura 38 dias (el ejemplo del enunciado)
-- 2: esta ocurriendo ahora, dura 52 dias (el otro ejemplo del enunciado)
INSERT INTO festival (id_festival, nombre, descripcion, imagen_url, temporada, fecha_inicio, fecha_fin) VALUES
 (1, 'Sabores de Verano',   'Festival de cocina de estacion con food trucks y puestos tematicos.', 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800', 'VERANO',    '2026-01-10', '2026-02-16'),
 (2, 'Epicentro Primavera', 'La edicion mas grande del ano: 50 dias de gastronomia al aire libre.', 'https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800', 'PRIMAVERA', '2026-10-01', '2026-11-21');

-- ---------- COSTOS (los valores del enunciado, por festival) ----------
INSERT INTO costo (id_costo, tipo, valor, id_festival) VALUES
 ( 1, 'POR_SUPERFICIE',          500.00, 1),
 ( 2, 'POR_MONTAJE',              10.00, 1),
 ( 3, 'PLUS_ELECTRICIDAD',      2000.00, 1),
 ( 4, 'SUELDO_BASE',          100000.00, 1),
 ( 5, 'ANIO_ANTIGUEDAD',        5000.00, 1),
 ( 6, 'PLUS_COCINERO',        150000.00, 1),
 ( 7, 'PLUS_COCINERO_AYUDANTE',100000.00, 1),
 ( 8, 'PLUS_LAVAPLATOS',       50000.00, 1),
 ( 9, 'POR_SUPERFICIE',          500.00, 2),
 (10, 'POR_MONTAJE',              10.00, 2),
 (11, 'PLUS_ELECTRICIDAD',      2000.00, 2),
 (12, 'SUELDO_BASE',          100000.00, 2),
 (13, 'ANIO_ANTIGUEDAD',        5000.00, 2),
 (14, 'PLUS_COCINERO',        150000.00, 2),
 (15, 'PLUS_COCINERO_AYUDANTE',100000.00, 2),
 (16, 'PLUS_LAVAPLATOS',       50000.00, 2);

-- ---------- USUARIOS ----------
-- password admin1234 -> $2a$10$vvHgC7...
-- password resp1234  -> $2a$10$RnWxoA...
-- password emp1234   -> $2a$10$GmgwYa...
INSERT INTO usuario (id_usuario, email, password, activo, primer_ingreso_hecho) VALUES
 (1, 'admin@epicentro.com',  '$2a$10$vvHgC7CMMCVRTalPRnIrDuls4rKW1xmVyyhXrZ2Mt7Iv51mLtGDTK', 1, 1),
 (2, 'laura@epicentro.com',  '$2a$10$RnWxoAFRacMAb6Al2gWJNu49jluRRT/.f0axi3uSCuVcwplcNtX.G', 1, 1),
 (3, 'marcos@epicentro.com', '$2a$10$RnWxoAFRacMAb6Al2gWJNu49jluRRT/.f0axi3uSCuVcwplcNtX.G', 1, 1),
 (4, 'carla@epicentro.com',  '$2a$10$RnWxoAFRacMAb6Al2gWJNu49jluRRT/.f0axi3uSCuVcwplcNtX.G', 1, 1),
 (5, 'ana@epicentro.com',    '$2a$10$GmgwYaBI2NDc8UVa3hoMDe1BHPXJc9HFKfoJlKNV3o32G06XbfWgu', 1, 0),
 (6, 'luis@epicentro.com',   '$2a$10$GmgwYaBI2NDc8UVa3hoMDe1BHPXJc9HFKfoJlKNV3o32G06XbfWgu', 1, 0),
 (7, 'sofia@epicentro.com',  '$2a$10$GmgwYaBI2NDc8UVa3hoMDe1BHPXJc9HFKfoJlKNV3o32G06XbfWgu', 1, 0),
 (8, 'diego@epicentro.com',  '$2a$10$GmgwYaBI2NDc8UVa3hoMDe1BHPXJc9HFKfoJlKNV3o32G06XbfWgu', 1, 0),
 (9, 'baja@epicentro.com',   '$2a$10$GmgwYaBI2NDc8UVa3hoMDe1BHPXJc9HFKfoJlKNV3o32G06XbfWgu', 0, 1);

INSERT INTO usuario_rol (id_usuario_rol, id_usuario, rol) VALUES
 (1, 1, 'ADMINISTRADOR'),
 (2, 2, 'RESPONSABLE'),
 (3, 3, 'RESPONSABLE'),
 (4, 4, 'RESPONSABLE'),
 (5, 5, 'EMPLEADO'),
 (6, 6, 'EMPLEADO'),
 (7, 7, 'EMPLEADO'),
 (8, 8, 'EMPLEADO'),
 (9, 9, 'EMPLEADO');

-- ---------- UNIDADES DE VENTA ----------
-- el codigo unico tiene que tener exactamente 10 caracteres (validarCodigo)
INSERT INTO unidad_de_venta (id_unidad, nombre_comercial, superficie, codigo_unico, id_festival, id_responsable) VALUES
 (1, 'La Parrilla Rodante', 20.0, 'FT-PARRI01', 2, 2),
 (2, 'Carpa Gourmet',       30.0, 'PD-CARPA01', 2, 3),
 (3, 'Burger Express',      15.0, 'FT-BURGE01', 1, 4);

INSERT INTO food_truck (id_unidad, patente, requiere_electricidad) VALUES
 (1, 'AB123CD', 1),
 (3, 'EF456GH', 0);

INSERT INTO puesto_desarmable (id_unidad, cantidad_carpas, tiempo_montaje) VALUES
 (2, 4, 40);

-- ---------- PERSONAL ----------
INSERT INTO personal (id_personal, nombre, apellido, dni, fecha_nacimiento, fecha_ingreso, id_unidad, id_usuario) VALUES
 (1, 'Ana',   'Diaz',  '35111222', '1998-03-14', '2024-02-01', 3, 5),
 (2, 'Luis',  'Paz',   '30222333', '1990-07-22', '2023-01-15', 1, 6),
 (3, 'Sofia', 'Gomez', '38333444', '2000-11-05', '2025-04-10', 1, 7),
 (4, 'Diego', 'Ruiz',  '40444555', '2002-05-30', '2025-08-20', 2, 8),
 (5, 'Nadia', 'Soto',  '39555666', '2001-09-12', '2025-01-05', 1, 9);

INSERT INTO cocinero (id_personal, rol_cocina) VALUES
 (1, 'LAVAPLATOS'),
 (3, 'COCINERO'),
 (5, 'AYUDANTE');

INSERT INTO cajero (id_personal, turno) VALUES
 (2, 'MANANA'),
 (4, 'NOCHE');

-- ---------- PLATOS ----------
INSERT INTO plato (id_plato, nombre, precio_venta, costo_produccion, id_unidad) VALUES
 (1, 'Milanesa completa', 8500.50, 3000.00, 1),
 (2, 'Gaseosa 500ml',     1200.25,  400.00, 1),
 (3, 'Papas rusticas',    4500.00, 1500.00, 1),
 (4, 'Empanada de carne', 1800.00,  600.00, 2),
 (5, 'Copa de vino',      6000.00, 2500.00, 2),
 (6, 'Hamburguesa doble', 9000.00, 3500.00, 3);

-- ---------- PEDIDOS ----------
INSERT INTO pedido (id_pedido, fecha, total, id_unidad) VALUES
 (1, '2026-10-15', 20601.75, 1),
 (2, '2026-10-16',  5700.25, 1),
 (3, '2026-10-18', 34002.00, 1),
 (4, '2026-10-15', 16800.00, 2),
 (5, '2026-10-19',  9000.00, 2),
 (6, '2026-01-20', 18000.00, 3);

INSERT INTO item_pedido (id_item_pedido, cantidad, subtotal, id_pedido, id_plato) VALUES
 ( 1, 2, 17001.00, 1, 1),
 ( 2, 3,  3600.75, 1, 2),
 ( 3, 1,  4500.00, 2, 3),
 ( 4, 1,  1200.25, 2, 2),
 ( 5, 4, 34002.00, 3, 1),
 ( 6, 6, 10800.00, 4, 4),
 ( 7, 1,  6000.00, 4, 5),
 ( 8, 5,  9000.00, 5, 4),
 ( 9, 2, 18000.00, 6, 6);
