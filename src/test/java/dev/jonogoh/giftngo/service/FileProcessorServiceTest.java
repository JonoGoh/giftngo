package dev.jonogoh.giftngo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import dev.jonogoh.giftngo.domain.Entry;
import dev.jonogoh.giftngo.util.FileParser;
import dev.jonogoh.giftngo.util.FileValidationUtil;
import dev.jonogoh.giftngo.util.OutcomeTransformer;

@SpringBootTest
public class FileProcessorServiceTest {

  @Mock
  private FileValidationUtil validationUtil;

  @Mock
  private FileParser fileParser;

  @Mock
  private OutcomeTransformer outcomeTransformer;

  @InjectMocks
  private FileProcessorService fileProcessorService;

  @Test
  void processFile_success() throws Exception {

    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "test.txt", "text/plain", "sample data".getBytes());
    List<Entry> mockEntries = Collections.singletonList(Entry.builder()
        .uuid("uuid")
        .id("id")
        .name("name")
        .likes("likes")
        .transport("transport")
        .avgSpeed(10.0)
        .topSpeed(20.0)
        .build());
    String mockOutcome = "[{\"name\":\"name\",\"transport\":\"transport\",\"topSpeed\":20.0}]";

    when(fileParser.parse(any())).thenReturn(mockEntries);
    when(outcomeTransformer.transform(mockEntries)).thenReturn(mockOutcome);

    String result = fileProcessorService.processFile(mockFile);

    assertEquals(mockOutcome, result);
    verify(fileParser, times(1)).parse(any());
    verify(outcomeTransformer, times(1)).transform(mockEntries);

  }
}
