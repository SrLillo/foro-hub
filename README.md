Foro Hub 

Creación de una API REST para un foro online.

El foro es un espacio donde todos los participantes de una plataforma pueden plantear sus preguntas sobre determinados tópicos. Los estudiantes de los cursos utilizan el foro para sacar sus dudas sobre los cursos y proyectos en los que participan.

Este desafío replica el proceso del foro a nivel de back end, por medio de la creación de una API REST usando Spring.

Nuestra API se centra específicamente en los tópicos, y permite a los usuarios:

Crear un nuevo tópico;
Mostrar todos los tópicos creados;
Mostrar un tópico específico;
Actualizar un tópico;
Eliminar un tópico;
Y algunas otras funcionalidades.

Usamos lo que normalmente conocemos como principios CRUD* (CREATE, READ, UPDATE, DELETE) y el framework de Spring nos facilita mucho el trabajo.

Además, esta API REST se implementa con las siguientes características:
Rutas implementadas siguiendo las mejores prácticas del modelo REST;
Validaciones realizadas según las reglas de negocio;
Implementación de una base de datos relacional para la persistencia de la información;
Servicio de autenticación/autorización para restringir el acceso a la información.

Configuración de Entorno de Desarrollo Java

ForoHub se basa en las siguientes configuraciones

Java JDK: versión 17
Maven: versión 4
Spring Boot: versión 3.3.0
MySQL: versión 8.0
IDE IntelliJ IDEA
MySQL

Configuración al crear el proyecto con Spring Initializr:

Java (versión 17)
Maven (versión 4)
Spring Boot
Proyecto en formato JAR

Dependencias agregadas al crear el proyecto con Spring Initializr:

Lombok
Spring Web
Spring Boot DevTools
Spring Data JPA
Flyway Migration
MySQL Driver
Validation
Spring Security

Diagrama de Base de Datos

La base de datos para almacenar la información de la aplicación se centra en la tabla de tópicos con las siguientes informaciones:

id
título
mensaje
fecha de creación
status
autor
curso
respuestas

Configuración en el proyecto

A fin de integrar la base de datos en el proyecto se incluyen en el archivo pom.xml las siguientes dependencias:

Validation
MySQL Driver
Spring Data JPA
Flyway Migration

Se configuran también las application.properties con los datos de url, nombre de usuario y contraseña de nuestra base de datos, definiendo el driver de la base de datos, así como los datos de inicio de sesión con usuario y contraseña.

Migración en el proyecto

Se ejecutan varias migraciones por medio de archivos con extensión .sql que contienen comandos en lenguaje SQL para la configuración de la base de datos creada y construcción de tablas.

Se utilizan las anotaciones @‌PostMapping y @‌Transactional en la clase controladora para realizar la persistencia de los datos.


Autenticación

Con la aplicación de Spring Security como dependencia incluida en el archivo pom.xml, solo los usuarios autenticados pueden interactuar con la API.

Se implementa un mecanismo de autenticación en la API para que los usuarios puedan autenticarse y enviar solicitudes.

Configuración de seguridad

Para la autenticación del proyecto se establece la clase SecurityConfigurations con información para el acceso a través de solicitudes http, utilizando anotaciones como @Configuration y @EnableWebSecurity, así como la clase spring HttpSecurity.
Autenticación en el código Java
El proceso de autenticación en la API se realiza con la implementación de un controller responsable de recibir las solicitudes de inicio de sesión. Se utilizan las anotaciones @RestController y @RequestMapping para definir la URL del controller.
Además, se utiliza una clase DTO para recibir los datos de inicio de sesión y contraseña, y luego autenticar al usuario en el método AuthenticationManager presente en la clase SecurityConfigurations.
→ Se utilizan las anotaciones @PostMapping, @RequestBody y @Valid para recibir y validar los datos de la solicitud.
Base de datos
Para implementar el mecanismo de autenticación en la API, se modifica la estructura de la base de datos, incluyendo una nueva tabla para almacenar los datos de autenticación de los usuarios por medio de migraciones con Flyway.

Registro de un nuevo tópico
La API cuenta con un endpoint para el registro de tópicos, y acepta solicitudes del tipo POST para la URI /tópicos.
Los datos del tópico (título, mensaje, autor y curso) son enviados en el cuerpo de la solicitud, en formato JSON.
→ Se utiliza la anotación @RequestBody para que el proyecto Spring reciba correctamente los datos del cuerpo de la solicitud.
→ El tópico es guardado en la base de datos construida para el proyecto, utilizando el método save del JpaRepository para realizar la persistencia de los datos del tópico creado.
→ Para la validación de los datos se utiliza la anotación Java integrada en Spring @Valid.
Reglas de negocio
Todos los campos son obligatorios, por lo tanto, es necesario verificar si todos los campos se están ingresando correctamente.
La API no permite el registro de tópicos duplicados (con el mismo título y mensaje).

Listado de tópicos
La API cuenta con un endpoint para el listado de todos los tópicos, y acepta solicitudes del tipo GET para la URI /tópicos.
Los datos de los tópicos (título, mensaje, fecha de creación, estado, autor y curso) son devueltos en el cuerpo de la respuesta, en formato JSON.
→ Se trabaja con JpaRepository asociado al tópico, y en la lista de datos de la base de datos se utiliza el método findAll.
Se desarrollan también las opciones de mostrar los primeros 10 resultados ordenados por fecha de creación del tópico en orden ASC, los resultados usando un criterio de búsqueda, como por ej. nombre de curso y año específico.
Se realiza una paginación de los resultados usando la anotación @PageableDefault para manejar el volumen de datos presentados al usuario.

Detalle de tópicos
La API cuenta con un endpoint para el listado de todos los tópicos, y acepta solicitudes del tipo GET para la URI /tópicos/{id}.
Los datos de los tópicos (título, mensaje, fecha de creación, estado, autor y curso) son devueltos en el cuerpo de la respuesta, en formato JSON.
→ Se utiliza la anotación @‌PathVariable para recibir el campo de ID de la solicitud GET.
Reglas de negocio
Solicitar el campo ID para realizar la consulta es una acción obligatoria, ya que tu usuario necesita poder visualizar los detalles de un tópico solicitando una consulta a los datos en la base de datos. En este caso, es necesario verificar si el campo ID se ingresó correctamente.
Actualización de tópico
La API cuenta con un endpoint para la actualización de los datos de un determinado tópico, y acepta solicitudes del tipo PUT para la URI /tópicos/{id}.
Las mismas reglas de negocio del registro de un tópico se aplican también en su actualización.
A fin de actualizar el tópico se solicita y verifica el campo ID de la solicitud, verificando si el tópico existe ya en la base de datos. Se utiliza el método isPresent() de la clase Java Optional.
Se utiliza la anotación @PathVariable para obtener el ID de la solicitud PUT.

Eliminación de tópico
La API cuenta con un endpoint para la eliminación de un tópico específico, el cual debe aceptar solicitudes del tipo DELETE para la URI /tópicos/{id}.
A fin de actualizar el tópico (eliminándolo) primero se  solicita y verifica el campo ID de la solicitud. Se utiliza la anotación @PathVariable para obtener el ID de la solicitud PUT y el método isPresent() de la clase Java Optional.
Por último, al tratarse de la eliminación de un elemento específico de la base de datos, se utiliza el método deleteById del JpaRepository.

Control de acceso

Token JWT
Generación y validación del token
Se utiliza la clase de servicio, TokenService, para aislar la generación y validación del token.
En la clase, se implementa el método "generarToken()", utilizando la biblioteca JWT para crear un token con el algoritmo HMAC256 y una contraseña secreta. También se agrega la funcionalidad de configurar la fecha de expiración del token.

Documentación
La API es accesible por medio de Swagger http://localhost:8081/swagger-ui/index.html
Y la estructura del proyecto en formato JSON es visible por medio de http://localhost:8081/v3/api-docs

Pruebas de la API
Además de las pruebas manuales de las funcionalidades de la API realizadas con Insomnia (https://insomnia.rest) se incluyen algunos tests para el Repositorio y el Controlador principal.


