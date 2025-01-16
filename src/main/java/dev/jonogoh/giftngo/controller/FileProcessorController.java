package dev.jonogoh.giftngo.controller;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.jonogoh.giftngo.domain.Outcome;
import dev.jonogoh.giftngo.domain.RequestLog;
import dev.jonogoh.giftngo.exception.IpBlockedException;
import dev.jonogoh.giftngo.service.FileProcessorService;
import dev.jonogoh.giftngo.service.RequestLoggerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileProcessorController {

  private final FileProcessorService fileProcessorService;

  private final RequestLoggerService requestLoggerService;

  @PostMapping("/process")
  public ResponseEntity<List<Outcome>> process(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    RequestLog requestLog = requestLoggerService.initializeLog(request);
    try {
      List<Outcome> outcome = fileProcessorService.processFile(file, requestLog);

      requestLog.setResponseCode(200);
      return ResponseEntity.ok()
          .header(CONTENT_DISPOSITION, "attachment; filename=\"OutcomeFile.json\"")
          .contentType(APPLICATION_JSON)
          .body(outcome);
    } catch (IpBlockedException e) {
      requestLog.setResponseCode(403);
      return ResponseEntity.status(403)
          .body(null);
    } catch (Exception e) {
      requestLog.setResponseCode(500);
      return ResponseEntity.badRequest()
          .body(null);
    } finally {
      requestLoggerService.finalizeLog(requestLog);
    }
  }
}
