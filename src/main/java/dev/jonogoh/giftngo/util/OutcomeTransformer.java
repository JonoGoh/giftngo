package dev.jonogoh.giftngo.util;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jonogoh.giftngo.domain.Entry;
import dev.jonogoh.giftngo.domain.Outcome;

@Component
public class OutcomeTransformer {

  public String transform(List<Entry> entries) throws Exception {
    List<Outcome> outcomes = entries.stream()
        .map(entry -> Outcome.builder()
            .name(entry.getName())
            .transport(entry.getTransport())
            .topSpeed(entry.getTopSpeed())
            .build())
        .collect(toList());

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(outcomes);
  }
}
