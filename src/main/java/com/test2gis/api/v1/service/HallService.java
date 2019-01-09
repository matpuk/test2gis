package com.test2gis.api.v1.service;

import com.test2gis.api.v1.converter.HallDtoConverter;
import com.test2gis.api.v1.converter.SeatDtoConverter;
import com.test2gis.api.v1.dto.HallDto;
import com.test2gis.api.v1.dto.SeatDto;
import com.test2gis.domain.HallRepository;
import com.test2gis.domain.entities.Hall;
import com.test2gis.domain.entities.Seat;
import com.test2gis.domain.entities.SeatId;
import com.test2gis.errorhandling.AppException;
import com.test2gis.errorhandling.NotFoundException;
import org.hibernate.exception.ConstraintViolationException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HallService {

    @Inject
    private HallRepository repository;

    @Inject
    private HallDtoConverter hallDtoConverter;

    @Inject
    private SeatDtoConverter seatDtoConverter;

    public List<HallDto> getAll() {
        return hallDtoConverter.convert(repository.findAll());
    }

    public HallDto getById(Long hallId) throws NotFoundException {
        return hallDtoConverter.convert(getHallById(hallId));
    }

    public List<SeatDto> getSeats(Long hallId) throws NotFoundException {
        Hall h = getHallById(hallId);

        // Hall.seats are just booked seats. But we must return complete seats list (booked and free)
        List<SeatDto> allSeats = new LinkedList<>();
        Set<Long> bookedSeatIds = new HashSet<>();
        Set<Long> freeSeatIds = new HashSet<>();

        // Add booked seats to all seats list and remember their ids
        for (Seat s : h.getSeats()) {
            allSeats.add(seatDtoConverter.convert(s));
            bookedSeatIds.add(s.getId().getSeatId());
        }

        // Generate all possible seat ids for the hall
        for (Long i = 1L; i <= h.getNumSeats(); i++) {
            freeSeatIds.add(i);
        }

        // Remove booked ids
        freeSeatIds.removeAll(bookedSeatIds);

        // Generate free seats
        for (Long seatId : freeSeatIds) {
            allSeats.add(new SeatDto(seatId, h.getId(), false));
        }

        return allSeats;
    }

    public void bookSeats(Long hallId, List<Long> seatIds) throws Exception {
        Hall h = getHallById(hallId);
        validateSeatIds(seatIds, h.getNumSeats());

        List<Seat> seats = new LinkedList<>();
        for (Long seatId : seatIds) {
            SeatId sid = new SeatId();
            sid.setHallId(hallId);
            sid.setSeatId(seatId);

            Seat s = new Seat();
            s.setId(sid);
            s.setBooked(true);

            seats.add(s);
        }
        try {
            repository.bookSeats(seats);
        } catch (ConstraintViolationException ex) {
            throw new AppException(
                    Response.Status.CONFLICT.getStatusCode(),
                    "At least one requested seat is already booked"
            );
        }
    }

    private Hall getHallById(Long id) throws NotFoundException {
        Hall h = repository.findById(id);
        if (h == null) {
            throw new NotFoundException("Hall not found: " + id);
        }

        return h;
    }

    private void validateSeatIds(List<Long> ids, int maxId) throws AppException {
        for (Long i : ids) {
            if (i < 0 || i > maxId) {
                throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid seat ID: " + i);
            }
        }
    }
}
