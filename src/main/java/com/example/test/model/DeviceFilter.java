package com.example.test.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderClassName = "Builder")
public class DeviceFilter {
    private Long deviceId;
    private Type typ;
    private Float energy;
    private Status status;

}
