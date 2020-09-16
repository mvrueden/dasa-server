package com.example.test.model.restriction;

import com.example.test.model.Device;

import java.util.Arrays;
import java.util.Objects;

public class OrRestriction implements Restriction {
    private final Restriction[] restrictions;

    OrRestriction(Restriction... restrictions) {
        Objects.requireNonNull(restrictions);
        this.restrictions = restrictions;
    }

    @Override
    public boolean matches(Device device) {
        long count = Arrays.stream(restrictions).filter(r -> r.matches(device)).count();
        return count > 0;
    }
}
