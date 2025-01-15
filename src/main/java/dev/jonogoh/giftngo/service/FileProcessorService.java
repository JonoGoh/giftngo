package dev.jonogoh.giftngo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.jonogoh.giftngo.domain.Entry;
import dev.jonogoh.giftngo.util.FileParser;
import dev.jonogoh.giftngo.util.FIleValidator;
import dev.jonogoh.giftngo.util.OutcomeTransformer;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor
public class FileProcessorService {

  @NonNull
  private final FIleValidator validationUtils;

  @NonNull
  private final FileParser fileParser;

  @NonNull
  private final OutcomeTransformer transformer;

  public String processFile(MultipartFile file) throws Exception {
    validationUtils.validate(file);

    List<Entry> entries = fileParser.parse(file);
    return transformer.transform(entries);
  }
}
