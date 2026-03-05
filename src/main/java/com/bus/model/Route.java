package com.bus.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @Column(name = "route_from", nullable = false)
    private String routeFrom;

    @Column(name = "route_to", nullable = false)
    private String routeTo;

    @Column(name = "distance", nullable = false)
    private int distance;

    @OneToMany(mappedBy = "route")
    private List<Bus> buses;

    public Route() {
    }

    public Route(Long routeId, String routeFrom, String routeTo, int distance) {
        this.routeId = routeId;
        this.routeFrom = routeFrom;
        this.routeTo = routeTo;
        this.distance = distance;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getRouteFrom() {
        return routeFrom;
    }

    public void setRouteFrom(String routeFrom) {
        this.routeFrom = routeFrom;
    }

    public String getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(String routeTo) {
        this.routeTo = routeTo;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Bus> getBuses() {
        return buses;
    }

    public void setBuses(List<Bus> buses) {
        this.buses = buses;
    }
}
