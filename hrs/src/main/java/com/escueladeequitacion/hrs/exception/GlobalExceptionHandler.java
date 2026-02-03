package com.escueladeequitacion.hrs.exception;

import com.escueladeequitacion.hrs.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 * Captura y estandariza las respuestas de error.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

        /**
         * Maneja errores de validación de DTOs (@Valid).
         * Se ejecuta cuando fallan las validaciones de @NotNull, @Min, @Email, etc.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                // Extraer todos los errores de validación
                Map<String, String> errores = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach((error) -> {
                        String nombreCampo = ((FieldError) error).getField();
                        String mensajeError = error.getDefaultMessage();
                        errores.put(nombreCampo, mensajeError);
                });

                // Crear respuesta estructurada
                ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                "Error de validación en los datos enviados",
                                request.getRequestURI());
                errorResponseDto.setErrores(errores);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
        }

        /**
         * Maneja ResourceNotFoundException (recurso no encontrado).
         * Se lanza cuando un ID no existe en la base de datos.
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
                        ResourceNotFoundException ex,
                        HttpServletRequest request) {

                ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
        }

        /**
         * Maneja BusinessException (reglas de negocio violadas).
         * Ejemplo: instructor inactivo, caballo no disponible, etc.
         */
        @ExceptionHandler(BusinessException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponseDto> handleBusinessException(
                        BusinessException ex,
                        HttpServletRequest request) {

                ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                HttpStatus.BAD_REQUEST.value(),
                                "Business Rule Violation",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
        }

        /**
         * Maneja ConflictException (recursos duplicados).
         * Se lanza cuando intentas crear algo que ya existe.
         * Ejemplo: DNI duplicado, nombre de caballo duplicado, etc.
         */
        @ExceptionHandler(ConflictException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public ResponseEntity<ErrorResponseDto> handleConflictException(
                        ConflictException ex,
                        HttpServletRequest request) {

                ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                HttpStatus.CONFLICT.value(),
                                "Conflict",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
        }

        /**
         * Maneja UnauthorizedException (errores de autenticación).
         * Se lanza cuando un usuario no está autenticado o no tiene permisos.
         */
        @ExceptionHandler(UnauthorizedException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(
                        UnauthorizedException ex,
                        HttpServletRequest request) {

                ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                HttpStatus.UNAUTHORIZED.value(),
                                "Unauthorized", "Credenciales inválidas: " + ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
        }

        /**
         * Maneja ValidationException (validaciones personalizadas de negocio).
         * Se lanza cuando los datos no cumplen con reglas específicas del dominio.
         */
        @ExceptionHandler(ValidationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponseDto> handleValidationException(
                        ValidationException ex,
                        HttpServletRequest request) {

                ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Error",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
        }

        /**
         * Maneja IllegalArgumentException (argumentos inválidos).
         * Ejemplo: cantidad de clases no permitida (debe ser 4, 8, 12 o 16).
         */
        /**
         * Maneja IllegalArgumentException (argumentos inválidos).
         * 
         * @deprecated Usar ValidationException para nuevos casos de validación.
         */
        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {

                ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                HttpStatus.BAD_REQUEST.value(),
                                "Invalid Argument",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
        }

        /**
         * Maneja excepciones genéricas no capturadas.
         * Última línea de defensa para errores inesperados.
         */
        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ResponseEntity<ErrorResponseDto> handleGlobalException(
                        Exception ex,
                        HttpServletRequest request) {

                ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "Ocurrió un error inesperado en el servidor: " + ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
        }
}