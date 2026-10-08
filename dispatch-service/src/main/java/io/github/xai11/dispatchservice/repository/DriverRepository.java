package io.github.xai11.dispatchservice.repository;

import io.github.xai11.dispatchservice.model.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<DriverEntity, String> {
}
