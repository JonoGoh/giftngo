package dev.jonogoh.giftngo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"OutcomeFile.json\"")
          .contentType(MediaType.APPLICATION_JSON)
          .body(resource);
    } catch (Exception e) {
      log.error(null, e);
      return ResponseEntity.badRequest()
          .body(null);
    }
  }

}
