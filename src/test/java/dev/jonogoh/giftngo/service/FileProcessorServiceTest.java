package dev.jonogoh.giftngo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import dev.jonogoh.giftngo.domain.Entry;
import dev.jonogoh.giftngo.domain.Outcome;
import dev.jonogoh.giftngo.domain.RequestLog;
import dev.jonogoh.giftngo.util.OutcomeTransformer;

@ExtendWith(MockitoExtension.class )
public class FileProcessorServiceTest {

  @Mock
  private FileValidationService fileValidator;

  @Mock
  private FileParserService fileParser;

  @Mock
  private OutcomeTransformer outcomeTransformer;

  @InjectMocks
  private FileProcessorService fileProcessorService;

  @Test
  void processFile_success() throws Exception {
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "EntryFile.txt", "multipart/form-data", "sample data".getBytes());
    List<Entry> mockEntries = Collections.singletonList(Entry.builder()
        .uuid("uuid")
        .id("id")
        .name("name")
        .likes("likes")
        .transport("transport")
        .avgSpeed(10.0)
        .topSpeed(20.0)
        .build());
    RequestLog log = new RequestLog();

    List<Outcome> mockOutcome = List.of(Outcome.builder()
        .name("name")
        .transport("transport")
        .topSpeed(20.0)
        .build());

    when(fileParser.parse(any())).thenReturn(mockEntries);
    when(outcomeTransformer.transform(mockEntries)).thenReturn(mockOutcome);

    List<Outcome> result = fileProcessorService.processFile(mockFile, log);

    assertEquals(mockOutcome, result);
    verify(fileParser, times(1)).parse(any());
    verify(outcomeTransformer, times(1)).transform(mockEntries);
  }

  @Test
  void processFile_invalidFile() throws Exception {
    MockMultipartFile invalidFile = new MockMultipartFile(
        "file", "EntryFile.txt", "multipart/form-data", "".getBytes());
        RequestLog log = new RequestLog();

    when(fileParser.parse(any())).thenThrow(new IllegalArgumentException("Invalid file"));

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      fileProcessorService.processFile(invalidFile, log);
    });

    assertEquals("Invalid file", exception.getMessage());
    verify(fileParser, times(1)).parse(any());
  }
}
