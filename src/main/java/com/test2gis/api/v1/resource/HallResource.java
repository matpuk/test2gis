package com.test2gis.api.v1.resource;

import com.test2gis.api.v1.service.HallService;
import com.test2gis.api.v1.dto.HallDto;
import com.test2gis.errorhandling.AppException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/halls")
public class HallResource {

    @Inject
    private HallService hallService;

    @GET
    public Response findAll() {
        List<HallDto> halls = hallService.getAll();
        return Response.ok().entity(halls).build();
    }

    @GET
    @Path("/{hallid}")
    public Response getById(@PathParam("hallid") Long hallId) throws AppException {
        return Response.ok().entity(hallService.getById(hallId)).build();
    }

    @GET
    @Path("/{hallid}/seats")
    public Response getSeats(@PathParam("hallid") Long hallId) throws AppException {
        return Response.ok().entity(hallService.getSeats(hallId)).build();
    }

    @POST
    @Path("/{hallid}/bookSeats")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bookSeats(@PathParam("hallid") Long hallId, List<Long> seatIds) throws Exception {
        hallService.bookSeats(hallId, seatIds);
        return Response.ok().build();
    }
}
