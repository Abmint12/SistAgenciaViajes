-- =====================================
-- SISTEMA AGENCIA DE VIAJES
-- =====================================

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'VENDEDOR',
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(200)
);

CREATE TABLE destinos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    descripcion TEXT
);

CREATE TABLE reservas (
    id SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    id_destino INTEGER NOT NULL,
    fecha_viaje DATE NOT NULL,
    cant_pasajes INTEGER NOT NULL CHECK (cant_pasajes > 0),
    costo_total NUMERIC(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',

    CONSTRAINT fk_reserva_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES clientes(id),

    CONSTRAINT fk_reserva_destino
        FOREIGN KEY (id_destino)
        REFERENCES destinos(id)
);

CREATE TABLE pagos (
    id SERIAL PRIMARY KEY,
    id_reserva INTEGER NOT NULL,
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    monto NUMERIC(10,2) NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL,
    estado VARCHAR(20) DEFAULT 'PAGADO',

    CONSTRAINT fk_pago_reserva
        FOREIGN KEY (id_reserva)
        REFERENCES reservas(id)
);

CREATE TABLE facturas (
    id SERIAL PRIMARY KEY,
    id_pago INTEGER NOT NULL,
    numero_factura VARCHAR(30) UNIQUE NOT NULL,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subtotal NUMERIC(10,2) NOT NULL,
    impuesto NUMERIC(10,2) NOT NULL,
    total NUMERIC(10,2) NOT NULL,

    CONSTRAINT fk_factura_pago
        FOREIGN KEY (id_pago)
        REFERENCES pagos(id)
);
INSERT INTO usuarios
(nombre_usuario, contrasena, rol)
VALUES
('admin', 'admin123', 'VENDEDOR');

INSERT INTO clientes
(nombre, apellido, cedula, telefono, correo, direccion)
VALUES
('Luis', 'Zambrano', '0951767367',
 '0923624234',
 'lu.zab.vl@gmail.com',
 'Duran');

INSERT INTO destinos
(nombre, pais, ciudad, descripcion)
VALUES
('Islas Galápagos',
 'Ecuador',
 'Puerto Ayora',
 'Destino turístico');
