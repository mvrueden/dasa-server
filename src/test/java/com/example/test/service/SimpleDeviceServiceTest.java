package com.example.test.service;

import com.example.test.model.Device;
import com.example.test.model.DeviceFilter;
import com.example.test.model.Status;
import com.example.test.model.Type;
import com.example.test.rest.error.DeviceInUseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;

public class SimpleDeviceServiceTest {

    private DeviceService deviceService = new SimpleDeviceService();

    @BeforeEach
    public void init() {
        deviceService.registerDevice(Device.builder()
                .energy(1)
                .id(1L).type(Type.Satellite)
                .status(Status.WARNINGS)
                .build());
    }

    @AfterEach
    public void after() {
        deviceService.findAll().forEach(deviceService::unregisterDevice);
    }

    @Test
    // TODO MVR make it a bit nicer, as Optional<>... and .get() may
    //  throw an error in case of programming error, and test should fail properly and not with weird exceptions
    public void verifyClaim() {
        assertThat(deviceService.findAll(), hasSize(1));
        final DeviceFilter filter = DeviceFilter.builder().deviceId(1L).build();
        final SessionHandle sessionHandle = deviceService.claimDevice(filter).get();
        assertThat(sessionHandle, notNullValue());
        assertThat(sessionHandle.getSessionId(), notNullValue());

        try {
            // should fail
            final Optional<SessionHandle> sessionHandle2 = deviceService.claimDevice(filter);
            assertThat(sessionHandle2, is(Optional.empty()));
            fail("Was expecting exception, which was not thrown"); // TODO MVR maybe use consumer construct
        } catch (DeviceInUseException ex) {
            // expected
        }

        // Free device
        deviceService.clearDevice(sessionHandle.getSessionId());
        final SessionHandle sessionHandle3 = deviceService.claimDevice(filter).get();
        assertThat(sessionHandle3, notNullValue());
        assertThat(sessionHandle3.getSessionId(), notNullValue());
        assertThat(sessionHandle3.getSessionId(), not(is(sessionHandle.getSessionId())));
    }

    @Test
    public void verifyFind() {
        final DeviceFilter filter = DeviceFilter.builder().typ(Type.SpaceStation).build();
        assertThat(deviceService.find(filter), hasSize(0));

        final Device device = Device.builder()
                .id(2L)
                .type(Type.SpaceStation)
                .energy(0.5f)
                .status(Status.NO_WARNING).build();
        deviceService.registerDevice(device);

        assertThat(deviceService.find(filter), hasSize(1));
        assertThat(deviceService.findOne(filter).get(), is(device));
    }
}
