package dev.jonogoh.giftngo.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import dev.jonogoh.giftngo.config.FeatureFlagConfig;
import dev.jonogoh.giftngo.domain.RequestLog;

@ExtendWith(MockitoExtension.class)
public class FileValidationServiceTest {

  @Mock
  private FeatureFlagConfig featureFlagConfig;

  @Mock
  private IpValidationService ipValidationService;

  @InjectMocks
  private FileValidationService fileValidationService;

  @Test
  void skipIfFeatureFlagDisabled() throws Exception {
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "EntryFile.txt", "multipart/form-data", "sample data".getBytes());
    RequestLog requestLog = new RequestLog();
    requestLog.setRequestIpAddress("127.0.0.1");

    when(featureFlagConfig.isValidationEnabled()).thenReturn(false);

    fileValidationService.validate(mockFile, requestLog);

    verifyNoInteractions(ipValidationService); // IP validation shouldn't be called
  }

  @Test
  void shouldValidateFileWhenFeatureFlagIsEnabled() throws Exception {
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "EntryFile.txt", "multipart/form-data", "sample data".getBytes());
    RequestLog requestLog = new RequestLog();
    requestLog.setRequestIpAddress("127.0.0.1");

    when(featureFlagConfig.isValidationEnabled()).thenReturn(true);
    when(ipValidationService.validateIp(anyString()))
        .thenReturn(Map.of("countryCode", "US", "isp", "TestISP"));

    fileValidationService.validate(mockFile, requestLog);

    verify(ipValidationService).validateIp("127.0.0.1");
  }

  @Test
  void shouldThrowExceptionWhenFileIsEmpty() {
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "EntryFile.txt", "multipart/form-data", "".getBytes());
    RequestLog requestLog = new RequestLog();

    when(featureFlagConfig.isValidationEnabled()).thenReturn(true);

    // Act & Assert
    assertThatThrownBy(() -> fileValidationService.validate(mockFile, requestLog))
        .isInstanceOf(Exception.class)
        .hasMessage("File is empty");

    verifyNoInteractions(ipValidationService); // IP validation shouldn't be called
  }

  @Test
  void shouldThrowExceptionWhenFileTypeIsInvalid() {
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "EntryFile.jpg", "multipart/form-data", "sample data".getBytes());
    RequestLog requestLog = new RequestLog();
    when(featureFlagConfig.isValidationEnabled()).thenReturn(true);

    // Act & Assert
    assertThatThrownBy(() -> fileValidationService.validate(mockFile, requestLog))
        .isInstanceOf(Exception.class)
        .hasMessage("Invalid file type. Only TXT files are supported.");

    verifyNoInteractions(ipValidationService); // IP validation shouldn't be called
  }
}
