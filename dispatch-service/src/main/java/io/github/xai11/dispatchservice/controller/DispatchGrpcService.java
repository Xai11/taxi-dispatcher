package io.github.xai11.dispatchservice.controller;

import io.github.xai11.dispatchservice.grpc.Ack;
import io.github.xai11.dispatchservice.grpc.AssignRequest;
import io.github.xai11.dispatchservice.grpc.AssignResponse;
import io.github.xai11.dispatchservice.grpc.DispatchGrpc;
import io.github.xai11.dispatchservice.grpc.Driver;
import io.github.xai11.dispatchservice.grpc.Empty;
import io.github.xai11.dispatchservice.grpc.FindDriverRequest;
import io.github.xai11.dispatchservice.grpc.Location;
import io.github.xai11.dispatchservice.grpc.ReleaseRequest;
import io.github.xai11.dispatchservice.grpc.TrackRequest;
import io.github.xai11.dispatchservice.repository.DriverGeoIndex;
import io.github.xai11.dispatchservice.repository.DriverRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class DispatchGrpcService extends DispatchGrpc.DispatchImplBase {

    private final DriverRepository drivers;
    private final DriverGeoIndex geoIndex;

    public DispatchGrpcService(DriverRepository drivers, DriverGeoIndex geoIndex) {
        this.drivers = drivers;
        this.geoIndex = geoIndex;
    }

    @Override
    public void findDriver(FindDriverRequest request, StreamObserver<Driver> responseObserver) {
        // TODO: geoIndex.findNearby -> выбрать ближайшего FREE водителя
        responseObserver.onError(Status.UNIMPLEMENTED.withDescription("findDriver").asRuntimeException());
    }

    @Override
    public void assignDriver(AssignRequest request, StreamObserver<AssignResponse> responseObserver) {
        // TODO: атомарно FREE -> BUSY (защита от гонок), вернуть assigned=false, если уже занят
        responseObserver.onError(Status.UNIMPLEMENTED.withDescription("assignDriver").asRuntimeException());
    }

    @Override
    public void releaseDriver(ReleaseRequest request, StreamObserver<Empty> responseObserver) {
        // TODO: BUSY -> FREE
        responseObserver.onError(Status.UNIMPLEMENTED.withDescription("releaseDriver").asRuntimeException());
    }

    @Override
    public void streamDriverLocation(TrackRequest request, StreamObserver<Location> responseObserver) {
        // TODO: подписать responseObserver на обновления позиции водителя, снять подписку при отмене
        responseObserver.onError(Status.UNIMPLEMENTED.withDescription("streamDriverLocation").asRuntimeException());
    }

    @Override
    public StreamObserver<Location> reportLocation(StreamObserver<Ack> responseObserver) {
        return new StreamObserver<>() {
            private long received;

            @Override
            public void onNext(Location location) {
                geoIndex.update(location.getDriverId(), location.getLatitude(), location.getLongitude());
                received++;
                // TODO: опубликовать обновление подписчикам streamDriverLocation
            }

            @Override
            public void onError(Throwable t) {
                // TODO: через heartbeat-таймаут пометить водителя OFFLINE
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Ack.newBuilder().setReceived(received).build());
                responseObserver.onCompleted();
            }
        };
    }
}
