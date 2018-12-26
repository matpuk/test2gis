package com.test2gis.api.v1.converter;

import com.test2gis.api.v1.dto.SeatDto;
import com.test2gis.domain.entities.Seat;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SeatDtoConverter {
    public SeatDto convert(Seat s) {
        return new SeatDto(s.getId().getSeatId(), s.getId().getHallId(), s.isBooked());
    }

    public List<SeatDto> convert(List<Seat> seats){
        return seats.stream().map(this::convert).collect(toList());
    }
}
