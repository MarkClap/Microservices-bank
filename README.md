# 🏦 Reto Backend - Arquitectura de Microservicios

![Java](https://img.shields.io/badge/Java-17+-orange?logo=java) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen?logo=springboot) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-316192?logo=postgresql) ![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker) ![License](https://img.shields.io/badge/License-MIT-blue)

## 📖 Descripción General

Sistema de backend escalable basado en arquitectura de microservicios con patrón **Backend for Frontend (BFF)**. Proporciona una solución integral para gestionar información de clientes, sus productos financieros y autenticación segura mediante JWT.

### Características Principales

✅ **Autenticación segura** - JWT con Spring Security ✅ **Encriptación de datos** - Códigos únicos de cliente encriptados ✅ **BFF reactivo** - Spring WebFlux para alta concurrencia ✅ **Microservicios desacoplados** - Independencia y escalabilidad ✅ **Documentación automática** - Swagger/OpenAPI integrado ✅ **Contenedorización completa** - Docker & Docker Compose ✅ **Testing exhaustivo** - JUnit 5 + MockWebServer

----------

## 🛠️ Stack Tecnológico

### Backend

-   **Java 17** - Lenguaje de programación
-   **Spring Boot 3.5.10** - Framework principal
-   **Spring WebFlux** - Programación reactiva (BFF)
-   **Spring Data JPA** - Persistencia de datos
-   **Spring Security + OAuth2** - Autenticación segura

### Base de Datos

-   **PostgreSQL** - Base de datos relacional

### Librerías Complementarias

-   **MapStruct** - Mapeo de DTOs
-   **Lombok** - Reducción de boilerplate
-   **OpenAPI/Swagger** - Documentación interactiva

### Testing & QA

-   **JUnit 5** - Framework de testing
-   **MockWebServer** - Mocks de microservicios

### DevOps

-   **Docker** - Contenedorización
-   **Docker Compose** - Orquestación local
-   **Maven 3.8+** - Gestión de dependencias

----------

## 🚀 Guía de Ejecución

#### Levantar la Infraestructura

```bash
docker-compose up --build

```

**Salida esperada:**

```
✓ PostgreSQL iniciado en puerto 5432
✓ cliente-microservice iniciado en puerto 8082
✓ productos-microservice iniciado en puerto 8083
✓ bff-microservice iniciado en puerto 8081

```
----------

## 📚 Documentación de APIs

### Swagger/OpenAPI

Accede a la documentación interactiva en tu navegador:


| Servicio | URL | 
|----------|-----|
| BFF | http://localhost:8081/bff/swagger-ui.html |
| Cliente MS | http://localhost:8082/cliente/swagger-ui.html |
| Productos MS | http://localhost:8083/productos/swagger-ui.html |
----------

## 🐳 Gestión de Docker

### Comandos Útiles

**Levantar servicios (background):**

**Ver logs en tiempo real:**

```bash
docker-compose logs -f
```

**Ver logs de un servicio específico:**

```bash
docker-compose logs -f bff-microservice
```

**Detener contenedores **

```bash
docker-compose down
```

**Detener y eliminar volúmenes**

```bash
docker-compose down -v
```

**Reconstruir imágenes:**

```bash
docker-compose up --build
```

----------