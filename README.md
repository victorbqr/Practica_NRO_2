# Sistema de Gestión Universitaria 🎓

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-Auth-orange)](https://jwt.io/)
[![Swagger](https://img.shields.io/badge/Swagger-3.0-lightgreen)](https://swagger.io/)

## 📌 Descripción del Proyecto
Sistema backend para la gestión de:
- **Estudiantes** (inscripciones, materias)
- **Docentes** (asignación a materias)
- **Materias** (prerequisitos, créditos)
- **Autenticación JWT** con roles (Admin, Docente, Estudiante)

## 🛠 Tecnologías Principales
| Tecnología       | Uso                              |
|------------------|----------------------------------|
| Spring Boot 3    | Framework backend                |
| PostgreSQL 15    | Base de datos relacional         |
| JWT              | Autenticación segura             |
| Redis            | Caché de alto rendimiento        |
| Swagger          | Documentación de API             |
| Lombok           | Reducción de código boilerplate  |

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Java 17+
- PostgreSQL 15+
- Redis 7+

## 📚 Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/universidad/
│   │   ├── config/          # Config Security, Redis, Swagger
│   │   ├── controller/      # REST Controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── model/           # JPA Entidades
│   │   ├── registro/        # Manejo de configuracion de autenticacion de usuario 
│   │   ├── repository/      # Spring Data JPA
│   │   ├── service/         # Lógica empresarial
│   │   ├── validation/      # validaciones
│   │   └── UniversidadApplication.java
│   └── resources/
│       ├── application.properties
│       └── application-dev.properties
```
## 🔐 Roles y Accesos
Rol	Permisos	Endpoints Clave
ADMIN	Full access	Todos
DOCENTE	Gestión de materias/evaluaciones	/materias/, /evaluaciones/
ESTUDIANTE	Inscripciones propias	/inscripciones/, /materias/ (GET)

## 🌐 Endpoints Principales
Autenticación
POST /api/auth/login - Genera token JWT

Estudiantes
GET /api/estudiantes/ - Lista todos los estudiantes

POST /api/estudiantes/ - Crea nuevo estudiante

Materias
POST /api/materias/ - Crea nueva materia

PUT /api/materias/{id}/asignar-docente - Asigna docente

## 📄 Licencia
MIT © Victor Bernardo Quispe Rojas 2025
