package dev.jonogoh.giftngo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.jonogoh.giftngo.domain.Entry;
import dev.jonogoh.giftngo.util.FileParser;
import dev.jonogoh.giftngo.util.FileValidationUtil;
import dev.jonogoh.giftngo.util.OutcomeTransformer;

@Service
public class FileProcessorService {

  private final FileValidationUtil validationUtils;
  private final FileParser fileParser;
  private final OutcomeTransformer transformer;

  @Autowired
  public FileProcessorService(
      FileValidationUtil validationUtils,
      FileParser fileParser,
      OutcomeTransformer transformer) {
    this.validationUtils = validationUtils;
    this.fileParser = fileParser;
    this.transformer = transformer;
  }

  public String processFile(MultipartFile file) throws Exception {
    validationUtils.validate(file);

    List<Entry> entries = fileParser.parse(file);
    return transformer.transform(entries);
  }
}
