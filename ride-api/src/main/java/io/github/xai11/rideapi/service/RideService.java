package io.github.xai11.rideapi.service;

import io.github.xai11.dispatchservice.grpc.Driver;
import io.github.xai11.rideapi.grpc.DispatchClient;
import io.github.xai11.rideapi.dto.CreateRideRequest;
import io.github.xai11.rideapi.model.Ride;
import io.github.xai11.rideapi.model.RideStatus;
import io.github.xai11.rideapi.repository.RideRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@Service
public class RideService {

    private static final double SEARCH_RADIUS_KM = 3.0;

    private final RideRepository rides;
    private final DispatchClient dispatch;

    public RideService(RideRepository rides, DispatchClient dispatch) {
        this.rides = rides;
        this.dispatch = dispatch;
    }

    public Ride create(CreateRideRequest req) {
        Ride ride = new Ride();
        ride.setPassengerId(req.passengerId());
        ride.setPickupLat(req.pickupLat());
        ride.setPickupLon(req.pickupLon());
        ride.setDropoffLat(req.dropoffLat());
        ride.setDropoffLon(req.dropoffLon());
        ride = rides.save(ride);

        try {
            Driver driver = dispatch.findDriver(req.pickupLat(), req.pickupLon(), SEARCH_RADIUS_KM);
            if (dispatch.assignDriver(ride.getId().toString(), driver.getId())) {
                ride.setDriverId(driver.getId());
                ride.setStatus(RideStatus.ASSIGNED);
            } else {
                ride.setStatus(RideStatus.NO_DRIVERS);
            }
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                ride.setStatus(RideStatus.NO_DRIVERS);
            } else {
                throw e;
            }
        }
        return rides.save(ride);
    }

    public Ride get(UUID id) {
        return rides.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found"));
    }

    public List<Ride> history(String passengerId) {
        return rides.findByPassengerIdOrderByCreatedAtDesc(passengerId);
    }

    public Ride cancel(UUID id) {
        Ride ride = get(id);
        // TODO: проверить допустимость перехода статуса
        if (ride.getDriverId() != null) {
            dispatch.releaseDriver(id.toString(), ride.getDriverId());
        }
        ride.setStatus(RideStatus.CANCELLED);
        return rides.save(ride);
    }

    public Ride complete(UUID id) {
        Ride ride = get(id);
        // TODO: проверить допустимость перехода статуса, рассчитать стоимость
        if (ride.getDriverId() != null) {
            dispatch.releaseDriver(id.toString(), ride.getDriverId());
        }
        ride.setStatus(RideStatus.COMPLETED);
        return rides.save(ride);
    }
}
