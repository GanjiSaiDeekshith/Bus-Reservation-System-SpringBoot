package com.bus.model;

import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bus")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long busId;

    private String busName;
    private String driverName;
    private String busType;
    private Time arrivalTime;
    private Time departureTime;
    private Integer seats;
    private Integer availableSeats;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    public Bus() {
    }

    public Bus(Long busId, String busName, String driverName, String busType,
               Time arrivalTime, Time departureTime,
               Integer seats, Integer availableSeats,
               Double price, Route route) {
        this.busId = busId;
        this.busName = busName;
        this.driverName = driverName;
        this.busType = busType;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.seats = seats;
        this.availableSeats = availableSeats;
        this.price = price;
        this.route = route;
    }
    

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
