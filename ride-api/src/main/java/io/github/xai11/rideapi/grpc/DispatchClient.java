package io.github.xai11.rideapi.grpc;

import io.github.xai11.dispatchservice.grpc.AssignRequest;
import io.github.xai11.dispatchservice.grpc.Driver;
import io.github.xai11.dispatchservice.grpc.DispatchGrpc;
import io.github.xai11.dispatchservice.grpc.FindDriverRequest;
import io.github.xai11.dispatchservice.grpc.Location;
import io.github.xai11.dispatchservice.grpc.ReleaseRequest;
import io.github.xai11.dispatchservice.grpc.TrackRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/** Тонкая обёртка над gRPC-стабом dispatch-service. */
@Component
public class DispatchClient {

    private static final long DEADLINE_SECONDS = 3;

    @GrpcClient("dispatch")
    private DispatchGrpc.DispatchBlockingStub stub;

    public Driver findDriver(double lat, double lon, double radiusKm) {
        return stub.withDeadlineAfter(DEADLINE_SECONDS, TimeUnit.SECONDS)
                .findDriver(FindDriverRequest.newBuilder()
                        .setLatitude(lat).setLongitude(lon).setRadiusKm(radiusKm).build());
    }

    public boolean assignDriver(String rideId, String driverId) {
        return stub.withDeadlineAfter(DEADLINE_SECONDS, TimeUnit.SECONDS)
                .assignDriver(AssignRequest.newBuilder()
                        .setRideId(rideId).setDriverId(driverId).build())
                .getAssigned();
    }

    public void releaseDriver(String rideId, String driverId) {
        stub.withDeadlineAfter(DEADLINE_SECONDS, TimeUnit.SECONDS)
                .releaseDriver(ReleaseRequest.newBuilder()
                        .setRideId(rideId).setDriverId(driverId).build());
    }

    /** Блокирующий server-stream позиций водителя; потребляется в SSE-эндпоинте. */
    public Iterator<Location> streamDriverLocation(String rideId, String driverId) {
        return stub.streamDriverLocation(TrackRequest.newBuilder()
                .setRideId(rideId).setDriverId(driverId).build());
    }
}
