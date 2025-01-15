package dev.jonogoh.giftngo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jonogoh.giftngo.service.FileProcessorService;

@WebMvcTest(FileProcessorController.class)
public class FileProcessorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FileProcessorService fileProcessorService;

  @Test
  void testProcessFile() throws Exception {
    MockMultipartFile mockFile = new MockMultipartFile(
        "file", "EntryFile.txt", "multipart/form-data", "sample data".getBytes());
    String mockResponse = "File processed successfully";

    when(fileProcessorService.processFile(Mockito.any())).thenReturn(mockResponse);

    mockMvc.perform(multipart("/api/files/process")
        .file(mockFile)
        .contentType(MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(content().string(mockResponse));
  }

  @Test
  void testProcessFile_emptyFile() throws Exception {
    MockMultipartFile emptyFile = new MockMultipartFile(
        "file", "EntryFile.txt", "multipart/form-data", "".getBytes());

    mockMvc.perform(multipart("/api/files/process")
        .file(emptyFile)
        .contentType(MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }
}
