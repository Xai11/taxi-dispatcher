package io.github.xai11.rideapi.controller;

import io.github.xai11.rideapi.service.RideService;
import io.github.xai11.rideapi.dto.CreateRideRequest;
import io.github.xai11.rideapi.dto.RideResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rides")
public class RideController {

    private final RideService service;

    public RideController(RideService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RideResponse create(@Valid @RequestBody CreateRideRequest request) {
        return RideResponse.from(service.create(request));
    }

    @GetMapping("/{id}")
    public RideResponse get(@PathVariable UUID id) {
        return RideResponse.from(service.get(id));
    }

    @GetMapping
    public List<RideResponse> history(@RequestParam String passengerId) {
        return service.history(passengerId).stream().map(RideResponse::from).toList();
    }

    @PostMapping("/{id}/cancel")
    public RideResponse cancel(@PathVariable UUID id) {
        return RideResponse.from(service.cancel(id));
    }

    @PostMapping("/{id}/complete")
    public RideResponse complete(@PathVariable UUID id) {
        return RideResponse.from(service.complete(id));
    }
}
