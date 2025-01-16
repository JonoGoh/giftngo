package dev.jonogoh.giftngo.controller;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jonogoh.giftngo.domain.RequestLog;
import dev.jonogoh.giftngo.service.FileProcessorService;
import dev.jonogoh.giftngo.service.RequestLoggerService;

@WebMvcTest(FileProcessorController.class)
public class FileProcessorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FileProcessorService fileProcessorService;

  @MockitoBean
  private RequestLoggerService requestLoggerService;

  @Test
  void testProcessFile() throws Exception {
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "EntryFile.txt", "multipart/form-data", "sample data".getBytes());

    when(fileProcessorService.processFile(any(), any())).thenReturn(List.of());
    when(requestLoggerService.initializeLog(any())).thenReturn(new RequestLog());

    mockMvc.perform(multipart("/api/files/process")
        .file(mockFile)
        .contentType(MULTIPART_FORM_DATA))
        .andExpect(status().isOk());
  }

  // @Test
  // void testProcessFile_emptyFile() throws Exception {
  //   MockMultipartFile emptyFile = new MockMultipartFile(
  //       "file", "EntryFile.txt", "multipart/form-data", "".getBytes());

  //   mockMvc.perform(multipart("/api/files/process")
  //       .file(emptyFile)
  //       .contentType(MULTIPART_FORM_DATA))
  //       .andExpect(status().isBadRequest());
  // }
}
