package io.github.xai11.rideapi.repository;

import io.github.xai11.rideapi.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    List<Ride> findByPassengerIdOrderByCreatedAtDesc(String passengerId);
}
