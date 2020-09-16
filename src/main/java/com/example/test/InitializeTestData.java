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

@Component
@Profile("!prod")
public class InitializeTestData {

    @Autowired
    private DeviceService deviceService;

    @PostConstruct
    public void populateDatabase() throws IOException {
        deviceService.registerDevice(Device.builder()
                .id(1L)
                .type(Type.Satellite)
                .energy(1)
                .status(Status.NO_WARNING).build());
    }

}
