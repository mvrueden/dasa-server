package com.example.test.model.restriction;

import com.example.test.model.Device;

import java.util.Objects;

public class AndRestriction implements Restriction {

    private final Restriction[] restrictions;

    AndRestriction(Restriction... restrictions) {
        Objects.requireNonNull(restrictions);
        this.restrictions = restrictions;
    }

    @Override
    public boolean matches(Device device) {
        for (Restriction r : restrictions) {
            if (!r.matches(device)) {
                return false;
            }
        }
        return true;
    }
}
