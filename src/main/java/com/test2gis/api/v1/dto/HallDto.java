package com.test2gis.api.v1.dto;

public class HallDto {

    private Long id;
    private String name;
    private int numSeats;

    public HallDto() {}

    public HallDto(Long id, String name, int numSeats) {
        this.id = id;
        this.name = name;
        this.numSeats = numSeats;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HallDto)) {
            return false;
        }

        HallDto obj = (HallDto) o;
        return id == null ? obj.getId() == null : id.equals(obj.getId()) &&
                name == null ? obj.getName() == null : name.equals(obj.getName()) &&
                numSeats == obj.getNumSeats();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    @Override
    public String toString() {
        return "HallDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numSeats=" + numSeats +
                '}';
    }
}
