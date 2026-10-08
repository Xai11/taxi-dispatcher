package io.github.xai11.dispatchservice.repository;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/** Гео-индекс позиций водителей в Redis (GEOADD / GEOSEARCH). */
@Component
public class DriverGeoIndex {

    private static final String KEY = "drivers:geo";

    private final StringRedisTemplate redis;

    public DriverGeoIndex(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void update(String driverId, double lat, double lon) {
        redis.opsForGeo().add(KEY, new Point(lon, lat), driverId);
    }

    public Point position(String driverId) {
        List<Point> points = redis.opsForGeo().position(KEY, driverId);
        return points == null || points.isEmpty() ? null : points.get(0);
    }

    /** ID водителей в радиусе, отсортированные от ближайшего. */
    public List<String> findNearby(double lat, double lon, double radiusKm) {
        var results = redis.opsForGeo().radius(KEY,
                new Circle(new Point(lon, lat), new Distance(radiusKm, Metrics.KILOMETERS)));
        if (results == null) {
            return List.of();
        }
        return results.getContent().stream()
                .map(r -> r.getContent())
                .map(GeoLocation::getName)
                .toList();
        // TODO: отсортировать по расстоянию (RadiusGeoCommandArgs.sortAscending) и отфильтровать FREE
    }
}
