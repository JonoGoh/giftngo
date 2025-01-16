package dev.jonogoh.giftngo.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.jonogoh.giftngo.config.FeatureFlagConfig;
import dev.jonogoh.giftngo.domain.RequestLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileValidationService {

  private final FeatureFlagConfig featureFlagConfig;

  private final IpValidationService ipValidationService;

  public void validate(MultipartFile file, RequestLog requestLog) throws Exception {
    if (featureFlagConfig.isValidationEnabled()) {
      log.info("Validating file: {}", file.getOriginalFilename());
      validateFile(file, requestLog);
    } else {
      log.info("Validation disabled for file: {}", file.getOriginalFilename());
    }
  }

  private void validateFile(MultipartFile file, RequestLog requestLog) throws Exception {
    basicValidationChecks(file);
    ipValidationChecks(requestLog);
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

  private void ipValidationChecks(RequestLog requestLog) throws Exception {
    String ip = requestLog.getRequestIpAddress();
    Map<String, String> ipInfo = ipValidationService.validateIp(ip);
    requestLog.setRequestCountryCode(ipInfo.get("countryCode"));
    requestLog.setRequestIpProvider(ipInfo.get("isp"));
  }
}