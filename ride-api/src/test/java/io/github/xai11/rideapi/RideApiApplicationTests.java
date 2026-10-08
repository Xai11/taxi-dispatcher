package io.github.xai11.rideapi;

import io.github.xai11.rideapi.model.RideStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RideApiApplicationTests {

    @Test
    void rideStatusesAreDefined() {
        assertEquals(6, RideStatus.values().length);
    }
}
