package io.exnihilo.iotdataservice.exceptionhandling.handler;

import io.exnihilo.iotdataservice.exceptionhandling.NearestPlacesServiceException;
import io.exnihilo.iotdataservice.exceptionhandling.entity.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic RestExceptionHandler
 *
 * @author Anand Varkey Philips
 * @version 0.0.1
 * @since 0.0.1
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


  /**
   * Handler for all the {@link EntityNotFoundException} that occurs during a REST call.
   *
   * @param ex the EntityNotFoundException
   * @param request the WebRequest
   * @return the ExceptionResponse
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public final ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
    ExceptionResponse exceptionResponse =
            new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), request.getDescription(false));
    log.error("EntityNotFoundException: {}", exceptionResponse);
    return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NearestPlacesServiceException.class)
  public final ResponseEntity<ExceptionResponse> handleCityEagleServiceException(NearestPlacesServiceException me,
                                                                                 WebRequest request) {
    ExceptionResponse exceptionResponse =
        new ExceptionResponse(me.getMessage(), me.getHttpStatus().value(), request.getDescription(false));
    log.error("NearestPlacesServiceException: {}", exceptionResponse);
    return new ResponseEntity<>(exceptionResponse, me.getHttpStatus());
  }


  /**
   * Below are exception handling handlers for in-built exceptions.
   */

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ExceptionResponse errorDetails =
        new ExceptionResponse(ex.getMessage(), status.value(), request.getDescription(false));
    log.error("HttpRequestMethodNotSupportedException: {}", errorDetails);
    return new ResponseEntity<>(errorDetails, status);
  }

  @Override
  public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<String> errors =
        ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
    ExceptionResponse exceptionResponse =
        new ExceptionResponse("Mandatory Field Validation Failed", status.value(), errors.toString());
    log.error("MethodArgumentNotValidException: {}", exceptionResponse);
    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }
}
