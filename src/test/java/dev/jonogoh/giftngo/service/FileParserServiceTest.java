package dev.jonogoh.giftngo.service;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import dev.jonogoh.giftngo.domain.Entry;

@ExtendWith(MockitoExtension.class)
public class FileParserServiceTest {

  @InjectMocks
  private FileParserService fileParserService;

  @Test
  void parseValidFile() throws Exception {
    MockMultipartFile validFile = new MockMultipartFile(
        "file",
        "EntryFile.txt",
        "text/plain",
        """
                18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1
                3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5
                1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|8.5|15.3
            """.getBytes());

    List<Entry> entries = fileParserService.parse(validFile);

    assertThat(entries).hasSize(3);
    assertThat(entries.get(0).getUuid()).isEqualTo("18148426-89e1-11ee-b9d1-0242ac120002");
    assertThat(entries.get(0).getName()).isEqualTo("John Smith");
    assertThat(entries.get(0).getTransport()).isEqualTo("Rides A Bike");
    assertThat(entries.get(0).getAvgSpeed()).isEqualTo(6.2);
    assertThat(entries.get(0).getTopSpeed()).isEqualTo(12.1);
  }

  @Test
  void shouldReturnEmptyListWhenFileIsEmpty() throws Exception {
    MockMultipartFile emptyFile = new MockMultipartFile("file", "EntryFile.txt", "text/plain", "".getBytes());

    List<Entry> entries = fileParserService.parse(emptyFile);

    assertThat(entries).isEmpty();
  }

  @Test
  void shouldParseFileWithExtraNewlines() throws Exception {
      MockMultipartFile fileWithNewlines = new MockMultipartFile(
          "file",
          "FileWithNewlines.txt",
          "text/plain",
          """
              
              18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1
              
              3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5
              
          """.getBytes()
      );

      List<Entry> entries = fileParserService.parse(fileWithNewlines);

      assertThat(entries).hasSize(2);
  }
}
