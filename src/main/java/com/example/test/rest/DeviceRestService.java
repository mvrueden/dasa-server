package com.example.test.rest;

import com.example.test.model.Device;
import com.example.test.model.DeviceFilter;
import com.example.test.rest.error.DeviceInUseException;
import com.example.test.rest.error.InvalidDataException;
import com.example.test.service.DeviceService;
import com.example.test.service.SessionHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/devices")
public class DeviceRestService {

    @Autowired
    private DeviceService deviceService;

    @PostMapping
    @RequestMapping("/claim")
    public SessionHandle startSession(@RequestBody DeviceFilter deviceFilter) {
        validate(deviceFilter);
        final SessionHandle session = deviceService.claimDevice(deviceFilter)
            .orElseThrow(() -> new DeviceInUseException());
        return session;
    }

    @PostMapping
    @RequestMapping("/free/{sessionId}")
    public void clearSession(@PathVariable("sessionId") final String sessionId) {
        deviceService.clearDevice(sessionId);
    }

    @GetMapping
    public List<Device> listDevices() {
        return deviceService.findAll();
    }

    // TODO MVR Here we could provide even more user friendly messages.
    //  Allow for filter being a string and parse accordingly, instead of just hoping the right string is provided
    private void validate(DeviceFilter deviceFilter) {
        if (deviceFilter.getDeviceId() == null
                && deviceFilter.getEnergy() == null
                && deviceFilter.getStatus() == null
                && deviceFilter.getTyp() == null) {
            throw new InvalidDataException("At least one filter criteria must be provided");
        }
    }
}
