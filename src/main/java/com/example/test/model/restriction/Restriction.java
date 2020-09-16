package com.example.test.model.restriction;

import com.example.test.model.Device;

public interface Restriction {
    boolean matches(Device device);
}
