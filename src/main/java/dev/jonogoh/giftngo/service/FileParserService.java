package dev.jonogoh.giftngo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.jonogoh.giftngo.domain.Entry;

@Service
public class FileParserService {

  public List<Entry> parse(MultipartFile file) throws Exception {
    List<Entry> entries = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.trim().split("\\|");
        if (parts.length == 7) {
          entries.add(Entry.builder()
              .uuid(parts[0])
              .id(parts[1])
              .name(parts[2])
              .likes(parts[3])
              .transport(parts[4])
              .avgSpeed(Double.parseDouble(parts[5]))
              .topSpeed(Double.parseDouble(parts[6]))
              .build());
        }
      }
    }
    return entries;
  }
}
