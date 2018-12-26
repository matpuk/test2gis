package com.test2gis.api.v1.dto;

import java.util.Arrays;

public class SeatDto {

    private Long id;
    private Long hallId;
    private boolean isBooked;

    public SeatDto() {}

    public SeatDto(Long id, Long hallId, boolean isBooked) {
        this.id = id;
        this.hallId = hallId;
        this.isBooked = isBooked;
    }

    @Override
    public int hashCode() {
        if (id == null || hallId == null) {
            return 0;
        }
        return Arrays.hashCode(new Long[] {id, hallId});
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeatDto)) {
            return false;
        }

        SeatDto obj = (SeatDto) o;
        return id == null ? obj.getId() == null : id.equals(obj.getId()) &&
                hallId == null ? obj.getHallId() == null : hallId.equals(obj.getHallId()) &&
                isBooked == obj.isBooked();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public String toString() {
        return "SeatDto{" +
                "id=" + id +
                ", hallId=" + hallId +
                ", isBooked=" + isBooked +
                '}';
    }
}
