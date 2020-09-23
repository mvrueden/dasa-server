package com.example.test;

import com.example.test.model.Device;
import com.example.test.service.DeviceService;
import com.example.test.model.Status;
import com.example.test.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

@Component
@Profile("!prod")
public class InitializeTestData {

    private final DeviceService deviceService;

    public InitializeTestData(@Autowired DeviceService deviceService) {
        this.deviceService = Objects.requireNonNull(deviceService);
    }

    @PostConstruct
    public void populateDatabase() throws IOException {
        deviceService.registerDevice(Device.builder()
            .id(1L)
            .type(Type.Satellite)
            .energy(1)
            .status(Status.NO_WARNING).build());
        deviceService.registerDevice(Device.builder()
            .id(2L)
            .type(Type.SpaceStation)
            .energy(0.1f)
            .status(Status.NO_WARNING).build());
        deviceService.registerDevice(Device.builder()
            .id(3L)
            .type(Type.Satellite)
            .energy(0.5f)
            .status(Status.WARNINGS).build());
    }
}
