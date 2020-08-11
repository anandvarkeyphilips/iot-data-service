package io.exnihilo.iotdataservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PreDestroy;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is the entry point for spring boot admin server application. There are lots of
 * customizations available.
 *
 * @author Anand Varkey Philips
 * @since 2.0.6.RELEASE
 */
@Slf4j
@SpringBootApplication
public class IotDataServiceApplication {

  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Kolkata");
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd:MMM:YYY hh:mm:ss:SSS a");
  private String applicationName;

  public static void main(String[] args) {
    SpringApplicationBuilder app = new SpringApplicationBuilder(IotDataServiceApplication.class);
    app.build().run();
  }

  @Value("${application.name}")
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void logApplicationStart() {
    log.info("{} Application is started at Indian Standard Time: {}", applicationName,
        ZonedDateTime.now(ZONE_ID).format(DATE_TIME_FORMATTER));
  }

  @PreDestroy
  public void logApplicationExit() {
    log.info("{} Application is being gracefully stopped at Indian Standard Time: {}", applicationName,
        ZonedDateTime.now(ZONE_ID).format(DATE_TIME_FORMATTER));
  }
}
