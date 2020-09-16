package com.example.test.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "Builder")
public class Device {
    private Long id;

    private float energy;

    private Status status;

    private Type type;
}
