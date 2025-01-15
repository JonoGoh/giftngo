package dev.jonogoh.giftngo.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import dev.jonogoh.giftngo.config.FeatureFlagConfig;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class FIleValidator {

  @NonNull
  private final FeatureFlagConfig featureFlagConfig;

  @NonNull
  private final IPValidator ipValidator;

  public void validate(MultipartFile file) throws Exception {
    if (featureFlagConfig.isValidationEnabled()) {
      log.info("Validating file: {}", file.getOriginalFilename());
      validateFile(file);
    } else {
      log.info("Validation disabled for file: {}", file.getOriginalFilename());
    }
  }

  private void validateFile(MultipartFile file) throws Exception {
    basicValidationChecks(file);
    ipValidationChecks(file);
  }

  @SuppressWarnings("null")
  private void basicValidationChecks(MultipartFile file) throws Exception {
    if (file.isEmpty()) {
      throw new Exception("File is empty");
    }

    if (!file.getOriginalFilename().endsWith(".txt")) {
      throw new Exception("Invalid file type. Only TXT files are supported.");
    }
  }

  private void ipValidationChecks(MultipartFile file) throws Exception {
    ipValidator.ipValidation(file);
  }
}