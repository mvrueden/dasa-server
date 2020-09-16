package com.example.test.model.restriction;

import java.util.List;
import java.util.Objects;

public final class Restrictions {
    private Restrictions() {}

    public static AndRestriction and(Restriction left, Restriction right) {
        return new AndRestriction(left, right);
    }

    public static AndRestriction and(List<Restriction> restrictions) {
        Objects.requireNonNull(restrictions);
        return new AndRestriction(restrictions.toArray(new Restriction[restrictions.size()]));
    }

    public static OrRestriction or(Restriction left, Restriction right) {
        return new OrRestriction(left, right);
    }
}
