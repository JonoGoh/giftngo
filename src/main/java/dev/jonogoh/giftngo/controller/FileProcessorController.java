package dev.jonogoh.giftngo.controller;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.jonogoh.giftngo.service.FileProcessorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileProcessorController {

  private final FileProcessorService fileProcessorService;

  @Autowired
  public FileProcessorController(FileProcessorService fileProcessorService) {
    this.fileProcessorService = fileProcessorService;
  }

  @PostMapping("/process")
  public ResponseEntity<ByteArrayResource> processFile(@RequestParam("file") MultipartFile file) {
    log.info("Received file: {}", file.getOriginalFilename());

    try {
      String outcomeJson = fileProcessorService.processFile(file);
      ByteArrayResource resource = new ByteArrayResource(outcomeJson.getBytes());

      return ResponseEntity.ok()
          .header(CONTENT_DISPOSITION, "attachment; filename=\"OutcomeFile.json\"")
          .contentType(APPLICATION_JSON)
          .body(resource);
    } catch (Exception e) {
      log.error(null, e);
      return ResponseEntity.badRequest()
          .body(null);
    }
  }

}
