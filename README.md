#  Sistema de Agencia de Viajes-GRUPO 12

- Aguilera Zambrano Juan José
-  Camatón Laínez Segundo Rodolfo
-  Palma Carreño Diego Fernando
-  Zambrano Valverde Luis Abraham
-  Zhinin Muruzumbay Lady Nathaly

Proyecto desarrollado para la gestión de una agencia de viajes, que permite administrar clientes, reservas, destinos y facturación.

El sistema está dividido en tres capas:
- 🖥️ Frontend: React + Vite
- ⚙️ Backend: Spring Boot (Java)
- 🗄️ Base de datos: PostgreSQL

---
## Tecnologías utilizadas

### Frontend
- React
- Vite
- JavaScript
- Axios
- Tailwind CSS (si aplica)

### Backend
- Java
- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Security (si aplica)

### Base de Datos
- MySQL / PostgreSQL (según configuración)

---

## 📁 Estructura del proyecto en develop

SistAgenciaViajes/
│
├── backend/ # API REST con Spring Boot
├── frontend/ # Aplicación React
├── database/ # Scripts SQL
├── .gitignore
└── README.md


---

## ⚙️ Instalación del proyecto

### 1. Clonar repositorio desde develop
```bash
git clone -b develop https://github.com/Abmint12/SistAgenciaViajes.git

---
#### 2. Ejectuar frontend
```bash
cd frontend
npm install
npm run dev
---

#### 3. Ejectuar backend
```bash
cd backend
ls
cd backend

```

Ejecutar:

```bash
./mvnw spring-boot:run
```

