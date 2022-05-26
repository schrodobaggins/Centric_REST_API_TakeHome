
package REST_API_TakeHome.exceptions;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleInternalServerError(HttpServletRequest req, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Instant.now().toString());
        response.put("status", 500);
        response.put("message", "Unhandled exception caught");

        return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}