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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
                .message(messageFactory.getMessage("request.method.not-supported", ex.getMethod()))
                .path(requestPath(request))
                .exception(ex)
                .error("method", messageFactory.getMessage("request.method.not-supported.hint", ex.getMethod(), ex.getSupportedHttpMethods()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message(messageFactory.getMessage("request.media-type.not-supported", ex.getContentType()))
                .path(requestPath(request))
                .exception(ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message(ex.getMostSpecificCause().getMessage())
                .path(requestPath(request))
                .exception(ex)
                .error(ex.getPropertyName(), messageFactory.getMessage("request.body.type-mismatch.hint", ex.getRequiredType(), ex.getValue() != null ? ex.getValue().getClass().getSimpleName() : "null"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleBindException(@NonNull BindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message(messageFactory.getMessage("request.body.invalid-binding"))
                .path(requestPath(request))
                .exception(ex);

        Map<String, String> errors = new HashMap<>();
        for (final FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            final Object rejectedValue = fieldError.getRejectedValue();

            if (rejectedValue == null || ClassUtils.isPrimitiveOrWrapper(rejectedValue.getClass()) || rejectedValue instanceof String) {
                errors.put(fieldError.getField(), messageFactory.getMessage("request.body.invalid-binding.hint-1", fieldError.getRejectedValue()));
            } else {
                errors.put(fieldError.getField(), messageFactory.getMessage("request.body.invalid-binding.hint-2"));
            }
        }

        for (final ObjectError objectError : ex.getBindingResult().getGlobalErrors()) {
            errors.put(objectError.getObjectName(), objectError.getDefaultMessage());
        }

        error.errors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.NOT_FOUND)
                .message(messageFactory.getMessage("request.handler.not-found", ex.getHttpMethod(), ex.getRequestURL()))
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
                .message(messageFactory.getMessage("request.not-readable"))
                .exception(ex)
                .path(requestPath(request))
                .error("request", ex.getMostSpecificCause().getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message(messageFactory.getMessage("request.constraint-violation"))
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
                .code(ResponseCode.BAD_REQUEST)
                .message(ex.getMessage())
                .path(requestPath(request))
                .exception(ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    protected String requestPath(WebRequest request) {
        return request instanceof ServletWebRequest ? ((ServletWebRequest) request).getRequest().getRequestURI() : null;
    }
}
