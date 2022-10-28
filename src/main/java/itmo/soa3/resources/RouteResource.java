package itmo.soa3.resources;

import itmo.soa3.dto.RouteDto;
import itmo.soa3.services.RouteService;

import javax.ejb.EJB;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("/routes")

public class RouteResource {
    @EJB
    RouteService routeService;

    HashMap<String, String> filterParameters = new HashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/")
    public Response getRoute(@PathParam("id") Integer id) {
        return routeService.getRoute(id);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoutes(@DefaultValue("+id") @QueryParam("sort") String sortList,
                                 @DefaultValue("1") @QueryParam("page") Integer page,
                                 @DefaultValue("10") @QueryParam("page_size") Integer page_size,
                                 @QueryParam("id") String id,
                                 @QueryParam("name") String name,
                                 @QueryParam("distance") String distance,
                                 @QueryParam("creationDate") String creationDate,
                                 @QueryParam("coordinates_id") String coordinatesId,
                                 @QueryParam("coordinates_x") String coordinatesX,
                                 @QueryParam("coordinates_y") String coordinatesY,
                                 @QueryParam("from_id") String fromId,
                                 @QueryParam("from_x") String fromX,
                                 @QueryParam("from_y") String fromY,
                                 @QueryParam("from_z") String fromZ,
                                 @QueryParam("to_id") String toId,
                                 @QueryParam("to_x") String toX,
                                 @QueryParam("to_y") String toY,
                                 @QueryParam("to_z") String toZ
                                 ) {
        putParameter("id", id);
        putParameter("name", name);
        putParameter("distance", distance);
        putParameter("creationDate", creationDate);
        putParameter("coordinates_id", coordinatesId);
        putParameter("coordinates_x", coordinatesX);
        putParameter("coordinates_y", coordinatesY);
        putParameter("from_id", fromId);
        putParameter("from_x", fromX);
        putParameter("from_y", fromY);
        putParameter("from_z", fromZ);
        putParameter("to_id", toId);
        putParameter("to_x", toX);
        putParameter("to_y", toY);
        putParameter("to_z", toZ);
        return routeService.getAllRoutes(sortList, filterParameters, page, page_size);
    }

    private void putParameter(String k, String v){
        if (v != null && !v.isEmpty()){
            filterParameters.put(k, v);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoute(@Valid RouteDto routeDto){
        return routeService.addRoute(routeDto);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/")
    public Response deleteRoute(@PathParam("id") Integer id) {
        return routeService.deleteRoute(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/")
    public Response updateRoute(@PathParam("id") Integer id, @Valid RouteDto routeDto){
        return routeService.updateRoute(id, routeDto);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/count/distance/equals/{distance}")
    public Response countRoutesDistanceEq(@PathParam("distance") int distance){
        return routeService.countDistanceEquals(distance);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/count/distance/greater/{distance}")
    public Response countRoutesDistanceGt(@PathParam("distance") int distance){
        return routeService.countDistanceGreater(distance);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/name/contains/{substrInName}")
    public Response countRoutesDistanceGt(@PathParam("substrInName") String substr){
        return routeService.findNameContainsSubstr(substr);
    }
}