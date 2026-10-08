package io.github.xai11.rideapi.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record CreateRideRequest(
        @NotBlank String passengerId,
        @DecimalMin("-90") @DecimalMax("90") double pickupLat,
        @DecimalMin("-180") @DecimalMax("180") double pickupLon,
        @DecimalMin("-90") @DecimalMax("90") double dropoffLat,
        @DecimalMin("-180") @DecimalMax("180") double dropoffLon
) {
}
