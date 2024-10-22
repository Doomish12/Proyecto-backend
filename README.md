## Proyecto de Spring Boot con Spring Security, JPA y JWT
Este proyecto es una API RESTful que implementa autenticación y autorización utilizando Spring Security con JWT, validaciones de datos, y gestión de usuarios con roles. Las funcionalidades principales incluyen registro de usuarios, login, validación de tokens JWT, y manejo de roles.

## Características
- Autenticación y autorización con JWT.
- Registro y login de usuarios.
- Validación de tokens JWT.
- Gestión de usuarios con roles.
- Validaciones de datos en las solicitudes.
- Implementación de Spring Security y JPA.

## Requisitos Previos
- Java 21
- Maven 3.8+
- MySQL

## Endpoints de la API
Autenticación y Registro
- POST /api/auth/register - Registro de nuevos usuarios.
- POST /api/auth/login - Autenticación de usuarios.
- GET /api/auth/validar-token - Validar token JWT.

Usuarios y Roles
- GET /api/user/listaUsers - Obtener todos los usuarios (restringido a roles específicos).

# Dependencias
Este proyecto utiliza las siguientes dependencias:

- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- Spring Boot Starter Web
- Spring Boot DevTools
- MySQL Connector
- Lombok
- Spring Boot Starter Test
- Spring Security Test
- JJWT (Java JWT)
  
# Ejecución con Docker (Opcional)
- Si deseas ejecutar el proyecto utilizando Docker, sigue estos pasos:
- Para facilitar la configuración y despliegue del proyecto, se ha utilizado Docker. Sigue los siguientes pasos para ejecutar el proyecto en un contenedor Docker:
1. Asegúrate de tener Docker instalado en tu máquina.
2. Crea un archivo docker-compose.yml en la raíz del proyecto con el siguiente contenido, importante llenar el environment con los datos de su BD.

 -  version: '3.8'
-services:
 - app:
  -  image: login-backend-jwt
   - build:
     - context: .
     - dockerfile: Dockerfile
   - ports:
      - "9090:9090"
   - environment:
      - SPRING_DATASOURCE_URL=<bd_url>
      - SPRING_DATASOURCE_USERNAME= <bd_usuario>
      - SPRING_DATASOURCE_PASSWORD=<bd_contraseña>
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      - SPRING_JPA_SHOW_SQL=true
      - SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQLDialect
      - SECURITY_JWT_SECRET_KEY=<tu_clave_secreta>
3. Abre la terminal (cmd) y navega a la raíz de tu proyecto. Por ejemplo:
- cd C:\Proyectos_Personales\Login-backend-jwt

4. Ejecuta los siguientes comandos:
- Primero, para construir el contenedor: docker-compose build
- Segundo, para levantar el contenedor:    docker-compose up

## Contacto
- encisodiego155@gmail.com
  
## Créditos
Este proyecto fue desarrollado por DIEGO ANTONIO ORE ENCISO.


