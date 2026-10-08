package io.github.xai11.rideapi.controller;

import io.github.xai11.dispatchservice.grpc.Location;
import io.github.xai11.rideapi.grpc.DispatchClient;
import io.github.xai11.rideapi.model.Ride;
import io.github.xai11.rideapi.service.RideService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/** Мост gRPC server-stream -> SSE для браузера. */
@RestController
@RequestMapping("/rides")
public class TrackController {

    private final RideService rides;
    private final DispatchClient dispatch;

    public TrackController(RideService rides, DispatchClient dispatch) {
        this.rides = rides;
        this.dispatch = dispatch;
    }

    @GetMapping("/{id}/track")
    public SseEmitter track(@PathVariable UUID id) {
        Ride ride = rides.get(id);
        SseEmitter emitter = new SseEmitter(0L);

        Thread.ofVirtual().start(() -> {
            try {
                Iterator<Location> stream = dispatch.streamDriverLocation(id.toString(), ride.getDriverId());
                while (stream.hasNext()) {
                    Location loc = stream.next();
                    emitter.send(Map.of(
                            "lat", loc.getLatitude(),
                            "lon", loc.getLongitude(),
                            "ts", loc.getTimestampMs()));
                }
                emitter.complete();
            } catch (IOException e) {
                emitter.complete(); // клиент закрыл соединение
            } catch (RuntimeException e) {
                emitter.completeWithError(e);
            }
        });
        // TODO: отменять gRPC-стрим при emitter.onCompletion / onTimeout
        return emitter;
    }
}
