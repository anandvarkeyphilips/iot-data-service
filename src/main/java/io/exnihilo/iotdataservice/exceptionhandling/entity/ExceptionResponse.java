package io.exnihilo.iotdataservice.exceptionhandling.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * Response Class for Rest Exceptions
 *
 * @author Anand Varkey Philips
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class ExceptionResponse {

  private Instant timestamp = Instant.now();

  private String message;
  private int errorStatusCode;
  private String details;
  private String type;


  public ExceptionResponse(String message, int errorStatusCode, String details) {
    this(message, errorStatusCode, details, null);
  }

  public ExceptionResponse(String message, int errorStatusCode, String details, String type) {
    this.message = message;
    this.errorStatusCode = errorStatusCode;
    this.details = details;
    this.type = type;
  }
}
