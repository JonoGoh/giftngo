package dev.jonogoh.giftngo.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import dev.jonogoh.giftngo.domain.Entry;
import dev.jonogoh.giftngo.service.FileParserService;

@ExtendWith(MockitoExtension.class)
public class FileParserTest {

  private final FileParserService fileParser = new FileParserService();

  @Test
  void testParse() throws Exception {
    MockMultipartFile mockMultipartFile = new MockMultipartFile(
        "file", "EntryFile.txt", "multipart/form-data",
        "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1".getBytes());
    List<Entry> mockOutcome = List.of(Entry.builder()
        .uuid("18148426-89e1-11ee-b9d1-0242ac120002")
        .id("1X1D14")
        .name("John Smith")
        .likes("Likes Apricots")
        .transport("Rides A Bike")
        .avgSpeed(6.2)
        .topSpeed(12.1)
        .build());

    List<Entry> entries = fileParser.parse(mockMultipartFile);
    assertEquals(mockOutcome, entries);
  }
}
