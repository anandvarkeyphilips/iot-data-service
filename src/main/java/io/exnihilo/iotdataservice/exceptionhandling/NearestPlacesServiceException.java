package io.exnihilo.iotdataservice.exceptionhandling;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public class NearestPlacesServiceException extends RuntimeException implements Serializable {

  private static final long serialVersionUID = 1L;
  private final String errorMessage;
  private final HttpStatus httpStatus;

  public NearestPlacesServiceException(String errorMessage, HttpStatus httpStatus) {
    super(errorMessage);
    this.errorMessage = errorMessage;
    this.httpStatus = httpStatus;
  }
}
