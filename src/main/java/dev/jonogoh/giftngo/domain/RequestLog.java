package dev.jonogoh.giftngo.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
public class RequestLog {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String requestId;
  private String requestUri;
  private LocalDateTime requestTimestamp;
  private int responseCode;
  private String requestIpAddress;
  private String requestCountryCode;
  private String requestIpProvider;
  private long timeLapsed;

  @Version
  private int version;
}
