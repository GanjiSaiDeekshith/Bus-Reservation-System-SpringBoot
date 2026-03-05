package com.bus.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    private String reservationStatus;
    private String reservationType;
    private LocalDate reservationDate;
    private String reservationTime;
    private String source;
    private String destination;
    
    @Getter@Setter

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean journeyStarted;
    private boolean journeyEnded;

    private LocalDateTime journeyDate;
    private Integer seatsRequested;

    public Reservation() {}

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isJourneyStarted() {
        return journeyStarted;
    }

    public void setJourneyStarted(boolean journeyStarted) {
        this.journeyStarted = journeyStarted;
    }

    public boolean isJourneyEnded() {
        return journeyEnded;
    }

    public void setJourneyEnded(boolean journeyEnded) {
        this.journeyEnded = journeyEnded;
    }

    public LocalDateTime getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDateTime journeyDate) {
        this.journeyDate = journeyDate;
    }

    public Integer getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(Integer seatsRequested) {
        this.seatsRequested = seatsRequested;
    }

    @Transient
    public String getStatus() {
        if (journeyEnded) return "Ended";
        if (journeyStarted) return "Started";
        return "Not started";
    }
}
