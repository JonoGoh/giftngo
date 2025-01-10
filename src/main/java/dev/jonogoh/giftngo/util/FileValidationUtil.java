package dev.jonogoh.giftngo.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidationUtil {

  public void validate(MultipartFile file) throws Exception {
    if (file.isEmpty()) {
      throw new Exception("File is empty");
    }

    if (!file.getOriginalFilename().endsWith(".txt")) {
      throw new Exception("Invalid file type. Only TXT files are supported.");
    }
  }
}
