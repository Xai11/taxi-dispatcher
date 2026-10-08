package io.github.xai11.dispatchservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "drivers")
public class DriverEntity {

    @Id
    private String id;

    private String name;
    private String car;

    @Enumerated(EnumType.STRING)
    private DriverState state = DriverState.OFFLINE;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCar() { return car; }
    public void setCar(String car) { this.car = car; }
    public DriverState getState() { return state; }
    public void setState(DriverState state) { this.state = state; }
}
