package com.test2gis.api.v1;

import com.test2gis.ApplicationConfig;
import com.test2gis.api.v1.dto.HallDto;
import com.test2gis.api.v1.dto.SeatDto;
import com.test2gis.errorhandling.ErrorMessage;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HallResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ApplicationConfig();
    }

    private static String TEST_DB = "testbookingdb.mv.db";
    private static String SAMPLE_DB = "db/bookingdb.mv.db.sample";

    @BeforeClass
    public static void setupDb() throws Exception {
        File db = new File(TEST_DB);
        db.delete();

        Files.copy(new File(SAMPLE_DB).toPath(), db.toPath());
    }

    private HallDto currentHall;
    private Map<Long, SeatDto> currentHallSeats;

    @Before
    public void setupCurrentHall() {
        if (currentHall == null) {
            Response response = target("v1/halls/1").request(MediaType.APPLICATION_JSON_TYPE).get();
            if (response.getStatus() == Status.OK.getStatusCode()) {
                currentHall = response.readEntity(HallDto.class);
            }
        }
        if (currentHall != null) {
            currentHallSeats = getCurrentHallSeats(currentHall.getId());
        }
    }

    private Map<Long, SeatDto> getCurrentHallSeats(Long hallId) {
        Response response = target("v1/halls/" + hallId + "/seats")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        if (response.getStatus() == Status.OK.getStatusCode()) {
            List<SeatDto> seats = response.readEntity(new GenericType<List<SeatDto>>(){});
            return seats.stream().collect(Collectors.toMap(SeatDto::getId, item -> item));
        }

        return null;
    }

    private final static Set<HallDto> expectedHalls = new HashSet<>(Arrays.asList(
            new HallDto(1L, "Small Hall", 10),
            new HallDto(2L, "Medium Hall", 50),
            new HallDto(3L, "Big Hall", 100)
    ));

    @Test
    public void hallsListTest() {
        Response response = target("v1/halls").request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));

        Set<HallDto> halls = new HashSet<>(response.readEntity(new GenericType<List<HallDto>>(){}));
        assertThat(halls.size(), is(3));
        assertEquals(halls, expectedHalls);
    }

    @Test
    public void hallInfoTest() {
        assertNotEquals(currentHall, null);
        assertEquals(currentHall, new HallDto(1L, "Small Hall", 10));
    }

    @Test
    public void hallInvalidIdTest() {
        Response response = target("v1/halls/10").request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus(), is(Status.NOT_FOUND.getStatusCode()));

        ErrorMessage msg = response.readEntity(ErrorMessage.class);
        assertThat(msg.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
        assertEquals(msg.getMessage(), "Hall not found: 10");
    }

    @Test
    public void hallSeatsListTest() {
        assertNotEquals(currentHall, null);
        assertNotEquals(currentHallSeats, null);
    }

    @Test
    public void hallSeatsListValidSizeTest() {
        assertNotEquals(currentHall, null);
        assertNotEquals(currentHallSeats, null);

        assertThat(currentHallSeats.size(), is(currentHall.getNumSeats()));
    }

    @Test
    public void hallSeatsListValidContentTest() {
        assertNotEquals(currentHall, null);
        assertNotEquals(currentHallSeats, null);

        for (SeatDto s: currentHallSeats.values()) {
            assertThat(s.getHallId(), is(currentHall.getId()));
            assertTrue(s.getId() > 0 && s.getId() <= currentHall.getNumSeats());
        }
    }

    @Test
    public void hallBookSeatsOneTest() {
        List<Long> seats = Collections.singletonList(1L);

        Response response = target("v1/halls/1/bookSeats")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(seats));
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));

        Map<Long, SeatDto> updatedSeats = getCurrentHallSeats(currentHall.getId());
        assertTrue(updatedSeats.containsKey(1L));
        assertTrue(updatedSeats.get(1L).isBooked());
    }

    @Test
    public void hallBookSeatsManyTest() {
        List<Long> seats = new ArrayList<>(Arrays.asList(2L, 3L));

        Response response = target("v1/halls/1/bookSeats")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(seats));
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));

        Map<Long, SeatDto> updatedSeats = getCurrentHallSeats(currentHall.getId());
        assertTrue(updatedSeats.containsKey(2L));
        assertTrue(updatedSeats.containsKey(3L));
        assertTrue(updatedSeats.get(2L).isBooked());
        assertTrue(updatedSeats.get(3L).isBooked());
    }

    @Test
    public void hallBookSeatsOneInvalidTest() {
        List<Long> seats = Collections.singletonList(4L);

        Response response = target("v1/halls/1/bookSeats")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(seats));
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));

        response = target("v1/halls/1/bookSeats")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(seats));
        assertThat(response.getStatus(), is(Status.CONFLICT.getStatusCode()));
    }

    @Test
    public void hallBookSeatsManyInvalidTest() {
        List<Long> seats = Collections.singletonList(6L);
        Response response = target("v1/halls/1/bookSeats")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(seats));
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));

        seats = new ArrayList<>(Arrays.asList(5L, 6L));
        response = target("v1/halls/1/bookSeats")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(seats));
        assertThat(response.getStatus(), is(Status.CONFLICT.getStatusCode()));

        Map<Long, SeatDto> updatedSeats = getCurrentHallSeats(currentHall.getId());
        assertTrue(updatedSeats.containsKey(5L));
        assertTrue(updatedSeats.containsKey(6L));
        assertTrue(!updatedSeats.get(5L).isBooked());
        assertTrue(updatedSeats.get(6L).isBooked());
    }
}
