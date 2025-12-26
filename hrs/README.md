# HRS - API REST Escuela de Equitación

Este módulo `hrs` contiene la API REST desarrollada en Java con Spring Boot para gestionar la escuela de equitación.

## Resumen
- Tecnologías: Java 17/21 (según configuración), Spring Boot, Spring Data JPA, H2 / MySQL (dependiendo del perfil), Maven.
- Propósito: gestionar entidades principales de la aplicación: `Alumno`, `Instructor`, `Caballo`, `Clase` y `Persona`.

## Estructura principal
- `controller/` — endpoints REST que exponen las operaciones CRUD y búsquedas.
- `dto/` — objetos de transferencia (requests/responses) con validaciones.
- `model/` — entidades JPA que representan las tablas de la base de datos.
- `repository/` — interfaces Spring Data JPA para persistencia.
- `service/` — lógica de negocio y orquestación entre repositorios y controladores.
- `enums/` — enumeraciones usadas por modelos (`CantidadClases`, `TipoCaballo`, `Estado`, `Rol`).
- `utility/` — utilidades compartidas (mensajes, codificadores, etc.).

## Flujo de funcionamiento
1. El cliente HTTP llama a un endpoint definido en `controller` (por ejemplo `GET /api/alumnos`).
2. El `Controller` valida la entrada (anotaciones de validación en `dto`) y delega la operación al `service` correspondiente.
3. El `Service` contiene la lógica del negocio y utiliza los `repository` para leer/escribir en la base de datos.
4. Los `repository` son implementados automáticamente por Spring Data JPA y mapean las entidades en `model` con la base de datos.
5. El resultado se transforma/encapsula en un `DTO` si procede y es devuelto al cliente con el status HTTP apropiado.

## Validación y manejo de errores
- Las validaciones de request usan anotaciones de `jakarta.validation` en los `DTO`.
- Los errores controlados devuelven objetos `Message` o respuestas uniformes; se recomienda un `@ControllerAdvice` para centralizar errores.

## Persistencia
- Configuración en `application.properties` (puede usar H2 para desarrollo y MySQL en producción). Revisa el archivo `src/main/resources/application.properties`.

## Ejecutar localmente
1. Configura `JAVA_HOME` (recomendado JDK 17 o 21) y `MAVEN_HOME` o usa `mvnw`.
2. Desde la carpeta `hrs` ejecuta:
```powershell
mvn spring-boot:run
```
3. La API estará disponible en `http://localhost:8080` por defecto.

## Buenas prácticas y recomendaciones
- Mantener las entidades JPA con colecciones inicializadas (`new HashSet<>`) para evitar NPEs con proxies.
- Usar excepciones específicas (p. ej. `ResourceNotFoundException`) y `@ControllerAdvice` para respuestas de error consistentes.
- Centralizar la lógica en `service` para facilitar tests unitarios.

## Contribuir
- Usa ramas temáticas y crea PRs con cambios pequeños y bien documentados.
- Ejecuta `mvn -DskipTests clean package` antes de abrir un PR para verificar compilación.

---
Archivo generado/actualizado por el asistente.

## Flujo de la aplicación
Una persona quiere tomar clases en la escuela, para eso se registra como alumno
con los datos personales (dni, nombre, apellido, teléfono, email), además debe indicar 
la cantidad de clases que desea tomar en el mes (4, 8, 12 o 16), se registra la fecha de inscripción y si tiene caballo propio o no.
Para el caso de tener caballo propio y que no esté registrado en la escuela, debe darlo de alta indicando el nombre, 
asignandole el tipo como "privado" y el si esta disponible y a que alumno pertenece.
Para el caso de los instructores, se registran con sus datos personales y si esta activo o no.
Para registrar una clase, el administrador debe asignar un instructor, un caballo (puede ser privado o de la escuela), el alumno que tomará la clase, la especialidad y el dia y la hora de inicio, calculando la duración.
El instructor puede ver las clases que tiene asignadas y cambiar el estado de la clase.
Para generar un reporte diario de clases, se puede filtrar por instructor, alumno, caballo, estado y fecha.
<!-- Una persona quiere tomar clases de equitación. El flujo sería:
1. La persona se registra como `Alumno` a través del endpoint `POST /api/alumnos`.
2. El `Alumno` puede ver los `Caballo` disponibles mediante `GET /api/caballos`.
Si el caballo es privado, se debe dar de alta
3. El `Alumno` reserva una `Clase` con un `Instructor` y un `Caballo` específico usando `POST /api/clases`.
4. El `Instructor` puede ver las `Clase` asignadas y gestionar su estado (confirmar, cancelar) a través de los endpoints correspondientes.
5. El sistema registra todas las interacciones y actualiza el estado de las entidades en la base de datos. -->