package com.example.test.service;

import com.example.test.model.Device;
import com.example.test.model.DeviceFilter;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    Optional<Device> findOne(DeviceFilter filter);
    List<Device> find(DeviceFilter filter);
    Optional<Device> findById(long id);
    void registerDevice(Device device);
    void unregisterDevice(Device device);
    void clearDevice(String sessionId);
    Optional<SessionHandle> claimDevice(DeviceFilter deviceFilter);
    List<Device> findAll();
}
