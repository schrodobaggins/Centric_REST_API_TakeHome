
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleInternalServerError(HttpServletRequest request, Exception exception) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", Instant.now().toString());
        responseMap.put("status", 500);
        responseMap.put("message", "Unhandled exception caught");

        return new ResponseEntity<Object>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = JSONTypeException.class)
    public ResponseEntity<Map<String, Object>> JsonTypeException(HttpServletRequest request, JSONTypeException exception) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", Instant.now().toString());
        responseMap.put("status", 400);
        responseMap.put("message", exception.getMessage());
        responseMap.put("path", request.getRequestURI());
        return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable (HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", Instant.now().toString());
        responseMap.put("status", 400);
        responseMap.put("message", "JSON contains invalid input");

        return new ResponseEntity<Object>(responseMap, HttpStatus.BAD_REQUEST);
    }

}