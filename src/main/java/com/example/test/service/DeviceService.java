package com.example.test.service;

import com.example.test.model.Device;
import com.example.test.model.DeviceFilter;
import com.example.test.rest.error.DeviceInUseException;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    Optional<Device> findOne(DeviceFilter filter);
    List<Device> find(DeviceFilter filter);
    Optional<Device> findById(long id);
    void registerDevice(Device device);
    void unregisterDevice(Device device);
    void clearDevice(String sessionId);
    Optional<SessionHandle> claimDevice(DeviceFilter deviceFilter) throws DeviceInUseException;
    List<Device> findAll();
    long countAll();
}
