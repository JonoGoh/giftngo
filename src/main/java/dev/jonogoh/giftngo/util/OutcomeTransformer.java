package dev.jonogoh.giftngo.util;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.jonogoh.giftngo.domain.Entry;
import dev.jonogoh.giftngo.domain.Outcome;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutcomeTransformer {

  public List<Outcome> transform(List<Entry> entries) throws Exception {
    return entries.stream()
        .map(entry -> Outcome.builder()
            .name(entry.getName())
            .transport(entry.getTransport())
            .topSpeed(entry.getTopSpeed())
            .build())
        .collect(toList());
  }
}
