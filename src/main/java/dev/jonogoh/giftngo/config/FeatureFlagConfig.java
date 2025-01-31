package dev.jonogoh.giftngo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("feature")
public class FeatureFlagConfig {

  private boolean validationEnabled;
}
