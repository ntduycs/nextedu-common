package io.github.thanhduybk.common.advice;

import io.github.thanhduybk.common.factory.MessageFactory;
import io.github.thanhduybk.common.payload.response.Response;
import io.github.thanhduybk.common.payload.response.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommonExceptionHandler extends ResponseEntityExceptionHandler {
    protected final Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);

    protected final MessageFactory messageFactory;

    public CommonExceptionHandler(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message("Method " + ex.getMethod() + " not supported under given path")
                .path(requestPath(request))
                .exception(ex)
                .error("method", ex.getMethod() + " method not supported. Try with " + Arrays.toString(ex.getSupportedMethods()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message("Media type " + ex.getContentType() + " not supported for given path")
                .path(requestPath(request))
                .exception(ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        String hintMessage = String.format("Should be of type %s but value of type %s was given", ex.getRequiredType(), ex.getValue() != null ? ex.getValue().getClass().getSimpleName() : null);

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message(ex.getMostSpecificCause().getMessage())
                .path(requestPath(request))
                .exception(ex)
                .error(ex.getPropertyName(), hintMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with {} field errors and {} global errors", ex.getClass().getSimpleName(), ex.getFieldErrorCount(), ex.getGlobalErrorCount());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message("Request validation just failed. Invalid data was given")
                .path(requestPath(request))
                .exception(ex);

        error.errors(resolveBindingErrors(ex.getBindingResult()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleBindException(@NonNull BindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message("Request binding just failed. Invalid data was given")
                .path(requestPath(request))
                .exception(ex);

        error.errors(resolveBindingErrors(ex.getBindingResult()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private Map<String, Object> resolveBindingErrors(BindingResult bindingErrors) {
        Map<String, Object> errors = new HashMap<>();

        for (final FieldError fieldError : bindingErrors.getFieldErrors()) {
            final Object rejectedValue = fieldError.getRejectedValue();

            if (rejectedValue == null || ClassUtils.isPrimitiveOrWrapper(rejectedValue.getClass()) || rejectedValue instanceof String) {
                errors.put(fieldError.getField(), "Was given with invalid " + rejectedValue + " value");
            } else {
                errors.put(fieldError.getField(), "Was given with invalid value");
            }
        }

        for (final ObjectError objectError : bindingErrors.getGlobalErrors()) {
            errors.put(objectError.getObjectName(), objectError.getDefaultMessage());
        }

        return errors;
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.NOT_FOUND)
                .message("No handler found for " + ex.getHttpMethod() + " - " + ex.getRequestURL())
                .exception(ex)
                .path(requestPath(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message("Request has invalid (non-readable) format. Please check your data")
                .exception(ex)
                .path(requestPath(request))
                .error("request", ex.getMostSpecificCause().getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Map<String, Object> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message("Data constraint was violated")
                .exception(ex)
                .path(requestPath(request))
                .errors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMostSpecificCause().getMessage());

        String fullErrorMessage = ex.getMostSpecificCause().getMessage();

        int separatorIndex = StringUtils.ordinalIndexOf(fullErrorMessage, "\n", 1);

        String errorMessage = fullErrorMessage.substring(0, separatorIndex).replace("\"", "");

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message(errorMessage)
                .exception(ex)
                .path(requestPath(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.UNEXPECTED_ERROR)
                .message(ex.getMessage())
                .path(requestPath(request))
                .error("debugMessage", ex.getCause() != null ? ex.getCause().getMessage() : ex.getLocalizedMessage())
                .exception(ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    protected String requestPath(WebRequest request) {
        return request instanceof ServletWebRequest ? ((ServletWebRequest) request).getRequest().getRequestURI() : null;
    }
}
