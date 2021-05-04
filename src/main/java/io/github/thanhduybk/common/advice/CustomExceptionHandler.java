package io.github.thanhduybk.common.advice;

import io.github.thanhduybk.common.exception.BadRequestException;
import io.github.thanhduybk.common.exception.InternalErrorException;
import io.github.thanhduybk.common.exception.ResourceDuplicatedException;
import io.github.thanhduybk.common.exception.ResourceNotFoundException;
import io.github.thanhduybk.common.factory.MessageFactory;
import io.github.thanhduybk.common.payload.response.Response;
import io.github.thanhduybk.common.payload.response.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class CustomExceptionHandler {
    protected final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    protected final MessageFactory messageFactory;

    public CustomExceptionHandler(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @ExceptionHandler({ResourceNotFoundException.class, ResourceDuplicatedException.class, BadRequestException.class})
    protected ResponseEntity<?> handleCustomException(Exception ex, WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.BAD_REQUEST)
                .message(ex.getMessage())
                .path(requestPath(request))
                .exception(ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({InternalErrorException.class})
    protected ResponseEntity<?> handleInternalErrorException(InternalErrorException ex, WebRequest request) {
        log.info("Resolved {} with error {}", ex.getClass().getSimpleName(), ex.getMessage());

        Response<?> error = Response.getInstance()
                .code(ResponseCode.UNEXPECTED_ERROR)
                .message(ex.getMessage())
                .path(requestPath(request))
                .exception(ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    protected String requestPath(WebRequest request) {
        return request instanceof ServletWebRequest ? ((ServletWebRequest) request).getRequest().getRequestURI() : null;
    }
}
