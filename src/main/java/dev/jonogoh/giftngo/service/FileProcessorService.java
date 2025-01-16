package dev.jonogoh.giftngo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.jonogoh.giftngo.domain.Entry;
import dev.jonogoh.giftngo.domain.Outcome;
import dev.jonogoh.giftngo.domain.RequestLog;
import dev.jonogoh.giftngo.util.OutcomeTransformer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileProcessorService {

  private final FileValidationService fileValidator;

  private final FileParserService fileParser;

  private final OutcomeTransformer transformer;

  public List<Outcome> processFile(MultipartFile file, RequestLog log) throws Exception {
    fileValidator.validate(file, log);

    List<Entry> entries = fileParser.parse(file);
    return transformer.transform(entries);
  }
}
