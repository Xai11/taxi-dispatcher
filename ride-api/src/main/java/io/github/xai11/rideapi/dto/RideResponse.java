package io.github.xai11.rideapi.dto;

import io.github.xai11.rideapi.model.Ride;
import io.github.xai11.rideapi.model.RideStatus;

import java.time.Instant;
import java.util.UUID;

public record RideResponse(
        UUID id,
        String passengerId,
        String driverId,
        RideStatus status,
        Instant createdAt
) {
    public static RideResponse from(Ride ride) {
        return new RideResponse(ride.getId(), ride.getPassengerId(), ride.getDriverId(),
                ride.getStatus(), ride.getCreatedAt());
    }
}
