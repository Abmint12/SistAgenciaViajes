-- =====================================
-- SISTEMA AGENCIA DE VIAJES
-- =====================================

-- =====================================
-- USUARIOS
-- =====================================

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'VENDEDOR',
    activo BOOLEAN DEFAULT TRUE
);

-- =====================================
-- CLIENTES
-- =====================================

CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(200)
);

-- =====================================
-- DESTINOS
-- =====================================

CREATE TABLE destinos (
    id_destino SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500)
);

-- =====================================
-- RESERVAS
-- =====================================

CREATE TABLE reservas (
    id BIGSERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    id_destino INTEGER NOT NULL,
    fecha_viaje DATE NOT NULL,
    cant_pasajes INTEGER NOT NULL CHECK(cant_pasajes > 0),
    costo_total NUMERIC(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',

    CONSTRAINT fk_reserva_cliente
        FOREIGN KEY(id_cliente)
        REFERENCES clientes(id),

    CONSTRAINT fk_reserva_destino
        FOREIGN KEY(id_destino)
        REFERENCES destinos(id_destino)
);

-- =====================================
-- PAGOS
-- =====================================

CREATE TABLE pagos (
    id BIGSERIAL PRIMARY KEY,
    reserva_id BIGINT NOT NULL,
    fecha_pago DATE DEFAULT CURRENT_DATE,
    monto NUMERIC(10,2) NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',

    CONSTRAINT fk_pago_reserva
        FOREIGN KEY(reserva_id)
        REFERENCES reservas(id)
);

-- =====================================
-- FACTURAS
-- =====================================

CREATE TABLE facturas (
    id SERIAL PRIMARY KEY,
    id_pago BIGINT NOT NULL UNIQUE,
    numero_factura VARCHAR(30) NOT NULL UNIQUE,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subtotal NUMERIC(10,2) NOT NULL,
    impuesto NUMERIC(10,2) NOT NULL,
    total NUMERIC(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'EMITIDA',

    CONSTRAINT fk_factura_pago
        FOREIGN KEY(id_pago)
        REFERENCES pagos(id)
);
-- =====================================
-- USUARIOS
-- =====================================

INSERT INTO usuarios (nombre_usuario, contrasena, rol)
VALUES
('admin', 'admin123', 'ADMIN'),
('ladyzhin', 'ventas123', 'VENDEDOR'),
('juanagz2', 'ventas456', 'VENDEDOR');

-- =====================================
-- CLIENTES
-- =====================================

INSERT INTO clientes
(nombre, apellido, cedula, telefono, correo, direccion)
VALUES
('Arelis Elizabeth', 'Sanchez Villamar', '0952757367', '0923624234', 'Are.eliz@gmail.com', 'Durán'),
('Elvis Eden', 'Figueroa Sanchez', '0912345678', '0991112233', 'evlised@gmail.com', 'Guayaquil'),
('Raquel Cinthia', 'Jimenez Jimenez', '0923456789', '0987654321', 'Rqe_jz@gmail.com', 'Quito'),
('Jenny Estefania', 'Villamar Espinoza', '0934567890', '0976543210', 'jennyest@gmail.com', 'Cuenca'),
('Teresa Leonor', 'Almeida Sesme', '0945678901', '0965432109', 'jose@gmail.com', 'Manta');

-- =====================================
-- DESTINOS
-- =====================================

INSERT INTO destinos
(nombre, pais, ciudad, descripcion)
VALUES
('Islas Galápagos', 'Ecuador', 'Puerto Ayora', 'Destino turístico'),
('Montañita', 'Ecuador', 'Santa Elena', 'Playa turística'),
('Machu Picchu', 'Perú', 'Cusco', 'Maravilla del mundo'),
('Cartagena', 'Colombia', 'Cartagena', 'Destino histórico'),
('Cancún', 'México', 'Cancún', 'Playas del Caribe');

-- =====================================
-- RESERVAS
-- =====================================

INSERT INTO reservas
(id_cliente, id_destino, fecha_viaje, cant_pasajes, costo_total, estado)
VALUES
(1,1,'2026-08-10',2,800.00,'CONFIRMADA'),
(2,2,'2026-08-15',4,1200.00,'CONFIRMADA'),
(3,3,'2026-09-05',2,1800.00,'PENDIENTE'),
(4,4,'2026-09-20',3,1500.00,'CONFIRMADA'),
(5,5,'2026-10-12',5,3000.00,'CONFIRMADA');

-- =====================================
-- PAGOS
-- =====================================

INSERT INTO pagos
(reserva_id, fecha_pago, monto, metodo_pago, estado)
VALUES
(1,'2026-07-01',800.00,'TARJETA','PAGADO'),
(2,'2026-07-02',1200.00,'TRANSFERENCIA','PAGADO'),
(3,'2026-07-03',900.00,'EFECTIVO','PENDIENTE'),
(4,'2026-07-04',1500.00,'TARJETA','PAGADO'),
(5,'2026-07-05',3000.00,'TRANSFERENCIA','PAGADO');

-- =====================================
-- FACTURAS
-- =====================================

INSERT INTO facturas
(id_pago, numero_factura, fecha_emision, subtotal, impuesto, total, estado)
VALUES
(1,'FAC-000001','2026-07-01 10:00:00',714.29,85.71,800.00,'EMITIDA'),
(2,'FAC-000002','2026-07-02 11:30:00',1071.43,128.57,1200.00,'EMITIDA'),
(4,'FAC-000003','2026-07-04 15:00:00',1339.29,160.71,1500.00,'EMITIDA'),
(5,'FAC-000004','2026-07-05 16:20:00',2678.57,321.43,3000.00,'EMITIDA');


