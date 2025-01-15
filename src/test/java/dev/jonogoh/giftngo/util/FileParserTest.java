package dev.jonogoh.giftngo.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import dev.jonogoh.giftngo.domain.Entry;

public class FileParserTest {

  private final FileParser fileParser = new FileParser();

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
        .avgSpeed(Double.valueOf(6.2))
        .topSpeed(Double.valueOf(12.1))
        .build());

    List<Entry> entries = fileParser.parse(mockMultipartFile);
    assertEquals(mockOutcome, entries);
  }
}
