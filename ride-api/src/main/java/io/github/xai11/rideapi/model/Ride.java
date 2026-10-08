package io.github.xai11.rideapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue
    private UUID id;

    private String passengerId;
    private String driverId;

    private double pickupLat;
    private double pickupLon;
    private double dropoffLat;
    private double dropoffLon;

    @Enumerated(EnumType.STRING)
    private RideStatus status = RideStatus.SEARCHING;

    private Instant createdAt = Instant.now();

    public UUID getId() { return id; }
    public String getPassengerId() { return passengerId; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
    public double getPickupLat() { return pickupLat; }
    public void setPickupLat(double pickupLat) { this.pickupLat = pickupLat; }
    public double getPickupLon() { return pickupLon; }
    public void setPickupLon(double pickupLon) { this.pickupLon = pickupLon; }
    public double getDropoffLat() { return dropoffLat; }
    public void setDropoffLat(double dropoffLat) { this.dropoffLat = dropoffLat; }
    public double getDropoffLon() { return dropoffLon; }
    public void setDropoffLon(double dropoffLon) { this.dropoffLon = dropoffLon; }
    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
}
