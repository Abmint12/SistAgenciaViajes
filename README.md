# Sistema Agencia de Viajes Grupo12
# Agencia de Viajes - Backend API

## Descripción

API REST desarrollada con **Spring Boot** para la gestión de una agencia de viajes.
API->localhost o puerto 8080

El sistema permite administrar:

* Clientes
* Destinos
* Reservas
* Pagos

La aplicación implementa operaciones CRUD completas utilizando Spring Boot, Spring Data JPA y PostgreSQL.


## Estructura del proyecto

```
src
├── controller
├── dto
├── model
├── repository
├── service
│   └── impl
└── config
```

## Base de datos

La configuraciones con PostgreSQL estan por defecto, puerto escucha:5432. 

Configurar el archivo:

```
src/main/resources/application.properties
```

Ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/agencia_viajes
spring.datasource.username=root
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Ejecutar el proyecto

Clonar el repositorio:

```bash
git clone https://github.com/Abmint12/SistAgenciaViajes.git
```

Entrar al proyecto:

```bash
cd backend
ls
cd backend

```

Ejecutar:

```bash
./mvnw spring-boot:run
```

o desde tu IDE favorito.

---

## API REST

| Método | Endpoint       | Descripción        |
| ------ | -------------- | ------------------ |
| GET    | /clientes      | Listar clientes    |
| POST   | /clientes      | Crear cliente      |
| PUT    | /clientes/{id} | Actualizar cliente |
| DELETE | /clientes/{id} | Eliminar cliente   |
| GET    | /destinos      | Listar destinos    |
| POST   | /destinos      | Crear destino      |
| PUT    | /destinos/{id} | Actualizar destino |
| DELETE | /destinos/{id} | Eliminar destino   |
| GET    | /reservas      | Listar reservas    |
| POST   | /reservas      | Crear reserva      |
| PUT    | /reservas/{id} | Actualizar reserva |
| DELETE | /reservas/{id} | Eliminar reserva   |
| GET    | /pagos         | Listar pagos       |
| POST   | /pagos         | Registrar pago     |
| PUT    | /pagos/{id}    | Actualizar pago    |
| DELETE | /pagos/{id}    | Eliminar pago      |

---

## Estado del proyecto

Versión estable **v1.0.0**

Backend  funcional con operaciones CRUD para todas las entidades principales.

---

## Autor

Luis A. Zambrano
