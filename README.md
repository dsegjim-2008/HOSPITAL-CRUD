# HospitalOS — Sistema CRUD Hospitalario

Sistema de gestión clínica full-stack para la administración de médicos, pacientes, citas y episodios clínicos. Desarrollado con **Spring Boot 4 + React 19 + PostgreSQL**, orquestado con **Docker Compose**.

---

## Índice

1. [Tecnologías](#tecnologías)
2. [Estructura del proyecto](#estructura-del-proyecto)
3. [Cómo ejecutar el proyecto](#cómo-ejecutar-el-proyecto)
4. [Credenciales por defecto](#credenciales-por-defecto)
5. [Endpoints de la API](#endpoints-de-la-api)
6. [Convenciones de documentación](#convenciones-de-documentación)
7. [Buenas prácticas aplicadas y áreas de mejora](#buenas-prácticas-aplicadas-y-áreas-de-mejora)

---

## Tecnologías

| Capa        | Tecnología                          | Versión |
|-------------|-------------------------------------|---------|
| Backend     | Spring Boot                         | 4.0.5   |
| ORM         | Spring Data JPA / Hibernate         | —       |
| Base de datos | PostgreSQL                        | 16      |
| Frontend    | React + Vite                        | 19 / 8  |
| Estilos     | Tailwind CSS                        | 4       |
| Calendario  | FullCalendar                        | 6       |
| HTTP client | Axios                               | 1.15    |
| Alertas UI  | SweetAlert2                         | 11      |
| Boilerplate | Lombok                              | —       |
| Contenedores| Docker + Docker Compose             | —       |

---

## Estructura del proyecto

```
HOSPITAL-CRUD/
├── docker-compose.yml          # Orquestación de los 4 servicios
├── client/                     # Frontend React
│   ├── Dockerfile.dev
│   ├── vite.config.js
│   ├── tailwind.config.js
│   └── src/
│       ├── App.jsx             # Raíz de la app, router de vistas y sesión
│       ├── components/
│       │   ├── Login.jsx           # Formulario de acceso
│       │   ├── MedicoList.jsx      # CRUD de médicos (solo admin)
│       │   ├── PacienteList.jsx    # Lista y detalle de pacientes
│       │   ├── HuerfanosList.jsx   # Pacientes sin médico asignado
│       │   ├── CalendarioCitas.jsx # Agenda del médico (FullCalendar)
│       │   ├── CitaModal.jsx       # Modal crear/editar cita
│       │   ├── CitasDiaModal.jsx   # Modal citas de un día
│       │   ├── EditPacienteModal.jsx # Modal edición de paciente
│       │   └── HistorialModal.jsx  # Modal historial clínico
│       └── services/
│           ├── hospitalService.js  # Llamadas a la API (médicos, pacientes, citas)
│           └── userService.js      # Stub de servicio de usuarios (sin usar actualmente)
└── server/                     # Backend Spring Boot
    ├── Dockerfile.dev
    ├── build.gradle
    └── src/main/java/com/empresa/server/
        ├── ServerApplication.java      # Punto de entrada
        ├── config/
        │   └── DataSeeder.java         # Datos iniciales de prueba
        ├── controller/
        │   ├── MedicoController.java   # GET, POST, DELETE /api/medicos
        │   ├── PacienteController.java # GET, POST, PUT /api/pacientes
        │   ├── CitaController.java     # GET, POST, PUT, DELETE /api/citas
        │   └── EpisodioController.java # POST /api/episodios/{pacienteId}
        ├── service/
        │   ├── MedicoService.java      # Lógica de negocio de médicos
        │   └── PacienteService.java    # Lógica de negocio de pacientes
        ├── dto/
        │   ├── MedicoDTO.java
        │   ├── PacienteDTO.java
        │   ├── CitaDTO.java
        │   └── EpisodioDTO.java
        ├── model/
        │   ├── Medico.java
        │   ├── Paciente.java
        │   ├── Cita.java
        │   └── Episodio.java
        └── repository/
            ├── MedicoRepository.java
            ├── PacienteRepository.java
            ├── CitaRepository.java
            └── EpisodioRepository.java
```

---

## Cómo ejecutar el proyecto

### Prerrequisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado y en ejecución.
- No se necesita Java, Node.js ni PostgreSQL instalados localmente.

### Pasos

```bash
# 1. Clona el repositorio
git clone https://github.com/tu-usuario/HOSPITAL-CRUD.git
cd HOSPITAL-CRUD

# 2. Levanta todos los servicios en modo desarrollo
docker compose up --build
```

Docker levantará cuatro contenedores:

| Servicio    | URL local                   | Descripción                     |
|-------------|-----------------------------|---------------------------------|
| `db`        | `localhost:5432`            | PostgreSQL 16                   |
| `pgadmin`   | http://localhost:5050       | Cliente web de PostgreSQL        |
| `server`    | http://localhost:8081/api   | API REST Spring Boot             |
| `client`    | http://localhost:5173       | Aplicación React (Vite HMR)     |

> **Hot-reload activo:** Los cambios en `./client/src` y `./server/src` se reflejan automáticamente sin reconstruir la imagen gracias a los volúmenes en `docker-compose.yml`.

### Detener los servicios

```bash
docker compose down
```

Para eliminar también los volúmenes de datos (base de datos):

```bash
docker compose down -v
```

---

## Credenciales por defecto

> ⚠️ Estas credenciales son solo para desarrollo local. No usar en producción.

| Rol         | Usuario      | Contraseña |
|-------------|--------------|------------|
| Admin       | `admin`      | `admin`    |
| Médico      | `davseg1990` | `123`      |

Los usuarios de médico se generan automáticamente con el formato `primNombrePrimApellidoAño` al registrar un médico. La contraseña inicial para todos los médicos es `123`.

---

## Endpoints de la API

Base URL: `http://localhost:8081/api`

### Médicos

| Método   | Ruta                | Descripción                                         |
|----------|---------------------|-----------------------------------------------------|
| `GET`    | `/medicos`          | Lista todos los médicos                             |
| `POST`   | `/medicos`          | Crea un médico (usuario generado automáticamente)   |
| `DELETE` | `/medicos/{id}`     | Elimina médico y desasigna sus pacientes            |

### Pacientes

| Método   | Ruta                      | Descripción                                    |
|----------|---------------------------|------------------------------------------------|
| `GET`    | `/pacientes`              | Lista todos los pacientes                      |
| `POST`   | `/pacientes`              | Registra un nuevo paciente                     |
| `GET`    | `/pacientes/{id}`         | Obtiene un paciente con su historial           |
| `PUT`    | `/pacientes/{id}`         | Actualiza datos o reasigna médico              |
| `GET`    | `/pacientes/huerfanos`    | Lista pacientes sin médico asignado            |

### Citas

| Método   | Ruta                         | Descripción                     |
|----------|------------------------------|---------------------------------|
| `GET`    | `/citas/medico/{medicoId}`   | Citas de un médico ordenadas    |
| `POST`   | `/citas`                     | Programa una nueva cita         |
| `PUT`    | `/citas/{id}`                | Actualiza una cita existente    |
| `DELETE` | `/citas/{id}`                | Cancela/elimina una cita        |

### Episodios clínicos

| Método   | Ruta                         | Descripción                              |
|----------|------------------------------|------------------------------------------|
| `POST`   | `/episodios/{pacienteId}`    | Añade un episodio al historial           |

---

## Convenciones de documentación

### Backend — Javadoc

Todas las clases y métodos públicos del backend siguen el estándar **Javadoc**. La estructura es:

```java
/**
 * Descripción breve de lo que hace la clase o método.
 *
 * Descripción extendida con detalles de comportamiento,
 * efectos secundarios o decisiones de diseño relevantes.
 *
 * @param nombreParam Descripción del parámetro
 * @return Descripción de lo que se retorna
 * @throws TipoExcepcion Cuándo se lanza esta excepción
 * @author Sistema CRUD Hospitalario
 * @version 1.0
 * @since 1.0
 * @see ClaseRelacionada
 */
```

**Reglas aplicadas en este proyecto:**

- Cada `@RestController`, `@Service`, `@Entity` y `@Component` tiene Javadoc de clase.
- Cada método público tiene `@param`, `@return` y `@throws` documentados.
- Los campos de entidades y DTOs tienen Javadoc de campo explicando restricciones.
- Los `@Transactional` y `@Transactional(readOnly=true)` están justificados en el Javadoc.

### Frontend — Comentarios JSX

Los componentes React usan comentarios en línea en mayúsculas para marcar secciones estructurales:

```jsx
{/* SIDEBAR: Descripción de la sección */}
{/* CONTENIDO PRINCIPAL: Descripción */}
{/* FOOTER DEL SIDEBAR: Descripción */}
```

Los servicios (`hospitalService.js`) agrupan funciones por entidad con comentarios de sección:

```js
// MÉDICOS
// PACIENTES
// CITAS
```

---

## Buenas prácticas aplicadas y áreas de mejora

### ✅ Prácticas correctas

| Área | Práctica |
|------|----------|
| Backend | Patrón DTO correcto: las entidades JPA nunca se exponen directamente a la API |
| Backend | Capa de servicio con `@Transactional` y `@Transactional(readOnly=true)` bien aplicados |
| Backend | `FetchType.LAZY` en todas las relaciones `@ManyToOne` para evitar N+1 queries |
| Backend | `DataSeeder` con verificación previa (`if (medicoRepo.count() == 0)`) |
| Backend | Javadoc exhaustivo en todas las clases y métodos públicos |
| Backend | Lombok reduce código boilerplate de forma consistente |
| Frontend | Capa de servicios (`hospitalService.js`) centralizada para todas las peticiones HTTP |
| Frontend | Componentes bien separados por responsabilidad |
| Docker | Hot-reload en desarrollo mediante volúmenes montados |
| Docker | `node_modules` excluido del volumen compartido con `/app/node_modules` anónimo |

### ⚠️ Áreas de mejora identificadas

#### Seguridad (prioritario)

| Problema | Archivo | Recomendación |
|----------|---------|---------------|
| **Login implementado en el cliente**: se descargan todos los médicos y se compara localmente | `hospitalService.js` | Implementar endpoint `POST /api/auth/login` en el backend con validación server-side |
| **Credenciales admin hardcodeadas** en el frontend | `Login.jsx` | Mover al backend con Spring Security |
| **Contraseñas en texto plano** (`"123"`) | `Medico.java`, `DataSeeder.java` | Usar BCrypt (`PasswordEncoder` de Spring Security) |
| **`@CrossOrigin` con URL hardcodeada** | Todos los controllers | Centralizar CORS en una clase `@Configuration` y leer la URL desde `application.properties` |
| **Credenciales de BD en `application.properties`** | `application.properties` | Usar variables de entorno (`${DB_PASSWORD}`) ya que Docker Compose las inyecta |

#### Arquitectura

| Problema | Archivo | Recomendación |
|----------|---------|---------------|
| `CitaController` y `EpisodioController` acceden a repositorios directamente | `CitaController.java`, `EpisodioController.java` | Crear `CitaService` y `EpisodioService` para mantener consistencia en la arquitectura |
| Inyección de dependencias con `@Autowired` en campo | Mayoría de clases | Preferir inyección por constructor (como ya hace `DataSeeder`) — es más testeable y evita dependencias circulares |
| Método `actualizarPaciente()` marcado como `@deprecated` pero sin eliminar | `PacienteService.java` | Eliminar el método o completar la migración al nuevo |
| `userService.js` definido pero no utilizado | `userService.js` | Eliminar o integrar al flujo de autenticación |
| `orElseThrow()` sin mensaje en `CitaController` | `CitaController.java` | Añadir mensaje: `orElseThrow(() -> new RuntimeException("Cita no encontrada"))` |

#### Calidad de código

| Problema | Archivo | Recomendación |
|----------|---------|---------------|
| Sin validación de campos en DTOs | Todos los DTOs | Añadir `@NotNull`, `@NotBlank`, `@Valid` de Jakarta Validation |
| `spring.jpa.show-sql=true` activo | `application.properties` | Mover a un perfil `dev` separado (`application-dev.properties`) |
| Sin paginación en endpoints de lista | `PacienteController`, `MedicoController` | Usar `Pageable` de Spring Data para datasets grandes |
| El estado de sesión se pierde al refrescar | `App.jsx` | Persistir la sesión en `localStorage` o `sessionStorage` |
