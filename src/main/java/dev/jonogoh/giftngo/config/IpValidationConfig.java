package dev.jonogoh.giftngo.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("validation")
public class IpValidationConfig {

  private List<String> blockedCountries;
  private List<String> blockedIsps;
}
