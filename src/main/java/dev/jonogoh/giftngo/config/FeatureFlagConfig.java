package dev.jonogoh.giftngo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagConfig {

  @Value("${feature.validation.enabled}")
  private boolean validationEnabled;

  public boolean isValidationEnabled() {
    return validationEnabled;
}
}
