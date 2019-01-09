package com.test2gis.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {
    @EmbeddedId
    private SeatId id;

    @Column(name = "is_booked")
    private boolean isBooked;

    public SeatId getId() {
        return id;
    }

    public void setId(SeatId id) {
        this.id = id;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}
