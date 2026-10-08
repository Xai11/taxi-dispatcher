package io.github.xai11.dispatchservice;

import io.github.xai11.dispatchservice.model.DriverState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DispatchServiceApplicationTests {

    @Test
    void driverStatesAreDefined() {
        assertEquals(3, DriverState.values().length);
    }
}
