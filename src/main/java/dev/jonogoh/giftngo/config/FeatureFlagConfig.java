package dev.jonogoh.giftngo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "feature")
@Getter
@Setter
public class FeatureFlagConfig {
  private boolean skipValidation;
}