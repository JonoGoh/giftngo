package dev.jonogoh.giftngo.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Outcome {
    private String name;
    private String transport;
    private double topSpeed;
}
