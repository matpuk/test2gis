package com.test2gis.api.v1.converter;

import com.test2gis.api.v1.dto.HallDto;
import com.test2gis.domain.entities.Hall;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class HallDtoConverter {
    public HallDto convert(Hall h) {
        return new HallDto(h.getId(), h.getName(), h.getNumSeats());
    }

    public List<HallDto> convert(List<Hall> halls){
        return halls.stream().map(this::convert).collect(toList());
    }
}
