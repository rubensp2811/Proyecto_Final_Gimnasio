# 🏋️ Sistema de Gestión de Gimnasio - API REST

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.10-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Private-red.svg)]()

## 📋 Descripción del Proyecto

API REST completa para la gestión integral de un gimnasio desarrollada con Spring Boot. El sistema permite administrar socios, clases, entrenadores, reservas, bonos, planes de membresía y horarios de forma eficiente y escalable.

Este proyecto implementa una arquitectura en capas (Controller-Service-Repository), validaciones de negocio robustas, paginación de resultados, y documentación interactiva con Swagger/OpenAPI.

---

## 🗃️ Modelo Entidad-Relación

![Diagrama ER](Proyecto_Final_Gimnasio/docs/MER.png)

### Entidades Principales

- **Socio** (Entidad Principal): Representa los miembros del gimnasio
- **Reserva** (Entidad Transaccional): Registra las reservas de clases con fechas y estados
- **Clase**: Clases disponibles en el gimnasio (privadas o grupales)
- **Entrenador**: Personal entrenador del gimnasio
- **Bono**: Bonos de clases para socios
- **Plan**: Planes de membresía disponibles
- **Horario**: Horarios de las clases

### Relaciones Implementadas

- **Uno a Muchos (1:N)**:
  - Un Socio puede tener múltiples Bonos
  - Un Socio puede tener múltiples Reservas
  - Un Plan puede estar asociado a múltiples Socios
  - Una Clase puede tener múltiples Horarios
  - Una Clase puede tener múltiples Reservas
  - Un Entrenador puede impartir múltiples Clases (pero cada clase tiene un solo entrenador)

- **Muchos a Muchos (N:M)**:
  - **Socio ↔ Clase**: Un socio puede reservar múltiples clases y una clase puede tener múltiples socios. Esta relación N:M se implementa a través de la entidad **Reserva** como tabla intermedia, que además almacena datos adicionales como fecha de reserva, estado (CONFIRMADA, CANCELADA, COMPLETADA) y referencia opcional a un bono utilizado.

---

## 📡 Endpoints de la API

### 🏠 Home
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Página de inicio con redirección a Swagger |

### 👥 Socios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/socios` | Obtener todos los socios (paginado) |
| GET | `/api/socios/activos` | Obtener socios activos (paginado) |
| GET | `/api/socios/buscar` | Buscar socios por filtros (nombre, email, estado) |
| GET | `/api/socios/{id}` | Obtener socio por ID |
| POST | `/api/socios` | Crear nuevo socio |
| PUT | `/api/socios/{id}` | Actualizar socio existente |
| DELETE | `/api/socios/{id}` | Eliminar socio |

### 🧘 Clases
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/clases` | Obtener todas las clases (paginado) |
| GET | `/api/clases/buscar` | Buscar clases por filtros (nombre, fecha, tipo) |
| GET | `/api/clases/populares` | Obtener clases más populares |
| GET | `/api/clases/{id}` | Obtener clase por ID |
| POST | `/api/clases` | Crear nueva clase |
| DELETE | `/api/clases/{id}` | Eliminar clase |

### 💪 Entrenadores
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/entrenadores` | Obtener todos los entrenadores (paginado) |
| GET | `/api/entrenadores/especialidad` | Buscar por especialidad (paginado) |
| GET | `/api/entrenadores/{id}` | Obtener entrenador por ID |
| POST | `/api/entrenadores` | Crear nuevo entrenador |
| PUT | `/api/entrenadores/{id}` | Actualizar entrenador |
| DELETE | `/api/entrenadores/{id}` | Eliminar entrenador |

### 📅 Reservas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/reservas` | Obtener todas las reservas (paginado) |
| GET | `/api/reservas/buscar` | Buscar por socio o estado (paginado) |
| GET | `/api/reservas/{id}` | Obtener reserva por ID |
| POST | `/api/reservas` | Crear nueva reserva |
| PATCH | `/api/reservas/{id}/cancelar` | Cancelar reserva |
| DELETE | `/api/reservas/{id}` | Eliminar reserva |

### 🎟️ Bonos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/bonos` | Obtener todos los bonos (paginado) |
| GET | `/api/bonos/socio/{idSocio}` | Obtener bonos de un socio |
| GET | `/api/bonos/{id}` | Obtener bono por ID |
| POST | `/api/bonos` | Crear nuevo bono |
| DELETE | `/api/bonos/{id}` | Eliminar bono |

### 📋 Planes
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/planes` | Obtener todos los planes disponibles |

### ⏰ Horarios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/horarios` | Obtener todos los horarios |
| GET | `/api/horarios/{id}` | Obtener horario por ID |
| POST | `/api/horarios` | Crear nuevo horario |
| DELETE | `/api/horarios/{id}` | Eliminar horario |

---

## ⚙️ Instalación y Ejecución

### Prerrequisitos

- Java 21 o superior
- Maven 3.6+
- MariaDB 10.5+
- Git

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd Proyecto_Final_Gimasio_2
```

2. **Configurar la base de datos**

Crear una base de datos en MariaDB:
```sql
CREATE DATABASE Gimnasio_Final;
```

3. **Configurar las credenciales**

Editar el archivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3307/Gimnasio_Final
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

4. **Compilar el proyecto**
```bash
mvnw clean install
```

5. **Ejecutar la aplicación**
```bash
mvnw spring-boot:run
```

6. **Acceder a la aplicación**

- **API Base**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Interfaz gráfica personalizada**: http://localhost:8080/swagger-ui/index2.html
- **API Docs JSON**: http://localhost:8080/api-docs

---

## 👨‍💻 Autor y Tecnologías

### Autor

**Rubén**
- Proyecto desarrollado como trabajo final de Acceso a Datos - DAM2

### Tecnologías Utilizadas

#### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.1.10** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM (Object-Relational Mapping)
- **Spring Validation** - Validación de datos

#### Base de Datos
- **MariaDB** - Sistema de gestión de base de datos

#### Documentación
- **SpringDoc OpenAPI 2.2.0** - Documentación automática de la API
- **Swagger UI** - Interfaz interactiva para probar endpoints

#### Utilidades
- **Lombok 1.18.34** - Reducción de código boilerplate
- **MapStruct 1.6.0** - Mapeo automático entre entidades y DTOs
- **Maven** - Gestión de dependencias

---

## 📝 Características Destacadas

### ✅ Arquitectura en Capas
- **@RestController**: Gestión de peticiones HTTP
- **@Service**: Lógica de negocio y validaciones
- **@Repository**: Acceso a datos con Spring Data JPA

### ✅ DTOs (Data Transfer Objects)
Desacoplamiento entre la capa de presentación y la capa de datos para mayor seguridad y flexibilidad.

### ✅ Validaciones de Negocio
- Control de plazas disponibles en clases
- Validación de bonos activos y saldo
- Prevención de reservas duplicadas
- Control de estados de reservas (CONFIRMADA, CANCELADA, COMPLETADA)
- Email único por socio
- Solo los socios activos pueden cambiar de plan
- Solo los socios activos pueden realizar reservas
- Solo los socios con plan PREMIUM pueden reservar clases grupales
- No se puede reservar una clase si ya está reservada por el mismo socio
- No se puede cancelar una reserva menos de 1 hora antes de la clase
- Las clases grupales no requieren bono
- Las clases privadas requieren bono
- El bono debe pertenecer al socio que realiza la reserva
- El bono debe estar activo
- El bono debe tener sesiones restantes
- El entrenador asociado al bono debe estar activo
- La hora de fin de un horario debe ser posterior a la hora de inicio

---

## 🔒 Reglas de Negocio Implementadas

1. **Gestión de Reservas**:
   - No se pueden realizar reservas si no hay plazas disponibles
   - Las reservas con bono descuentan del saldo disponible
   - Solo se pueden cancelar reservas en estado CONFIRMADA
   - No se puede reservar una clase si ya está reservada por el mismo socio
   - No se puede cancelar una reserva menos de 1 hora antes de la clase
   - Solo los socios activos pueden realizar reservas
   - Solo los socios con plan PREMIUM pueden reservar clases grupales
   - No se puede reservar una clase privada sin bono
   - No se puede reservar una clase grupal con bono

2. **Control de Bonos**:
   - Los bonos tienen un número limitado de clases
   - El saldo de bonos se actualiza automáticamente con cada reserva
   - El bono debe pertenecer al socio que realiza la reserva
   - El bono debe estar activo
   - El bono debe tener sesiones restantes
   - El entrenador asociado al bono debe estar activo

3. **Validación de Socios**:
   - Email único por socio
   - Validación de datos obligatorios (nombre, email, teléfono)
   - No se puede crear/actualizar un socio con email duplicado
   - Solo los socios activos pueden cambiar de plan
   - No se puede cambiar el plan de un socio inactivo

4. **Clases Privadas vs Grupales**:
   - Las clases privadas tienen aforo limitado a 1
   - Las clases grupales pueden tener múltiples participantes

5. **Horarios**:
   - La hora de fin de un horario debe ser posterior a la hora de inicio

---

## 📄 Licencia

Este proyecto es privado.

---

Proyecto desarrollado como parte del módulo de Acceso a Datos del Ciclo Formativo de Desarrollo de Aplicaciones Multiplataforma (DAM).
