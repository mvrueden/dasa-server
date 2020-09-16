package com.example.test.service;

import com.example.test.model.Device;
import com.example.test.model.DeviceFilter;
import com.example.test.model.restriction.Restriction;
import com.example.test.model.restriction.Restrictions;
import com.example.test.rest.error.DeviceInUseException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SimpleDeviceService implements DeviceService {

    private final List<Device> devices = Lists.newCopyOnWriteArrayList();
    private final Map<String, Device> claims = new ConcurrentHashMap<>();

    @Override
    public Optional<Device> findOne(DeviceFilter filter) {
        Objects.requireNonNull(filter);
        final List<Device> devices = find(filter);
        if (devices.size() == 0) {
            return Optional.empty();
        }
        if (devices.size() > 1) {
            log.debug("Query matched multiple devices. Devices matching: {}. Devices returning: {}. Determining Factor: First in List", devices.size(), 1);
        }
        return Optional.of(devices.get(0));
    }

    @Override
    public List<Device> find(DeviceFilter filter) {
        Objects.requireNonNull(filter);
        final List<Restriction> restrictions = Lists.newArrayList();
        // TODO MVR make more dynamic... is very simple and won't work anymore with database
        if (filter.getEnergy() != null) {
            restrictions.add(d -> d.getEnergy() == filter.getEnergy());
        }
        if (filter.getStatus() != null) {
            restrictions.add(d -> d.getStatus() == filter.getStatus());
        }
        if (filter.getTyp() != null) {
            restrictions.add(d -> d.getType() == filter.getTyp());
        }
        if (filter.getDeviceId() != null) {
            restrictions.add(d -> Objects.equals(d.getId(), filter.getDeviceId()));
        }
        final Restriction restriction = Restrictions.and(restrictions);
        final List<Device> matchingDevices = findAll().stream()
                .filter(d -> restriction.matches(d))
                .collect(Collectors.toUnmodifiableList());
        return matchingDevices;
    }

    @Override
    public Optional<Device> findById(long id) {
        final DeviceFilter deviceFilter = DeviceFilter.builder().deviceId(id).build();
        final Optional<Device> one = findOne(deviceFilter);
        return one;
    }

    @Override
    public void registerDevice(Device device) {
        Objects.requireNonNull(device);
        final Optional<Device> byId = findById(device.getId());
        if (!byId.isPresent()) {
            devices.add(device);
        } else {
            log.warn("Device with id {} already registered.", device.getId());
        }
    }

    @Override
    public void unregisterDevice(Device device) {
        Objects.requireNonNull(device);
        findById(device.getId()).ifPresent((theDevice) -> devices.remove(theDevice));
        claims.entrySet().stream().filter(e -> e.getValue() == device).map(Map.Entry::getKey).forEach(claims::remove);
    }

    @Override
    public void clearDevice(String sessionId) {
        claims.remove(sessionId);
    }

    @Override
    public Optional<SessionHandle> claimDevice(DeviceFilter deviceFilter) {
        final List<Device> devices = find(deviceFilter);
        if (!devices.isEmpty()) {
            final Device availableDevice = devices.stream()
                    .filter(device -> !this.claims.containsValue(device))
                    .findAny()
                    .orElseThrow(() -> new DeviceInUseException());
            final String sessionId = UUID.randomUUID().toString();
            claims.put(sessionId, availableDevice);
            return Optional.of(new SessionHandle(sessionId, availableDevice.getId()));
        }
        return Optional.empty();
    }

    @Override
    public List<Device> findAll() {
        return Collections.unmodifiableList(devices);
    }
}
