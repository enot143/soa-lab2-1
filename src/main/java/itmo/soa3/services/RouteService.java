package itmo.soa3.services;

import itmo.soa3.dao.CoordinateDAO;
import itmo.soa3.dao.LocationDAO;
import itmo.soa3.dao.RouteDAO;
import itmo.soa3.dto.CoordinateDto;
import itmo.soa3.dto.LocationDto;
import itmo.soa3.dto.RouteDto;
import itmo.soa3.entities.Coordinate;
import itmo.soa3.entities.Location;
import itmo.soa3.entities.Route;
import itmo.soa3.utils.FilterSortUtil;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
public class RouteService {
    @EJB
    RouteDAO routeDAO;
    @EJB
    CoordinateDAO coordinateDAO;
    @EJB
    LocationDAO locationDAO;

    private Coordinate findCoordinates(CoordinateDto coordinateDto){
        Coordinate c = coordinateDAO.getByXY(coordinateDto.getX(), coordinateDto.getY());
        if (c == null){
            Coordinate newCoordinate = new Coordinate();
            newCoordinate.setX(coordinateDto.getX());
            newCoordinate.setY(coordinateDto.getY());
            coordinateDAO.insert(newCoordinate);
            return newCoordinate;
        }else{
            return c;
        }
    }

    private Location findLocation(LocationDto locationDto){
        if (locationDto.getId() != null){
            Location l = locationDAO.findById(locationDto.getId());
            if (l == null) throw new NotFoundException("location with id = " + locationDto.getId() + " is not found");
            else return l;
        }
        Location l = locationDAO.getByXYZ(locationDto.getX(), locationDto.getY(), locationDto.getZ());
        if (l == null){
            Location newLocation = new Location();
            newLocation.setX(locationDto.getX());
            newLocation.setY(locationDto.getY());
            newLocation.setZ(locationDto.getZ());
            locationDAO.insert(newLocation);
            return newLocation;
        }else{
            return l;
        }
    }

    private void addOrUpdateRoute(Route route, RouteDto routeDto){
        route.setName(routeDto.getName());
        route.setCoordinates(findCoordinates(routeDto.getCoordinates()));
        LocalDate d = LocalDate.now();
        route.setCreationDate(d);
        route.setFrom(findLocation(routeDto.getFrom()));
        route.setTo(findLocation(routeDto.getTo()));
        route.setDistance(routeDto.getDistance());
        routeDAO.save(route);
    }


    public Response addRoute(RouteDto routeDto){
        Route route = new Route();
        addOrUpdateRoute(route, routeDto);
        return Response.ok().build();
    }

    public Response deleteRoute(Integer id){
        if (routeDAO.findById(id) != null){
            routeDAO.remove(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        throw new NotFoundException("route is not found");
    }

    public Response getRoute(Integer id){
        Route r = routeDAO.findById(id);
        if (r == null){
            throw new NotFoundException("route is not found");
        }else{
            return Response.ok().entity(r).build();
        }
    }



    public Response getAllRoutes(String sortingCriteria,
                                 HashMap<String, String> filterParameters,
                                 Integer page,
                                 Integer page_size){
        List<Route> routes = routeDAO.sortAndFilter(FilterSortUtil.getOrdersList(sortingCriteria),
                FilterSortUtil.getFiltersList(filterParameters));
        return Response.ok().entity(executePageRequest(page, page_size, routes)).build();
    }


    private List<Route> executePageRequest(Integer page, Integer page_size, List<Route> routes){
        List<Route> routeList = new ArrayList<>();
        int left = (page - 1) * page_size;
        if (left >= routes.size()){
            return routeList;
        }
        int right = page * page_size;
        if (right > routes.size()){
            right = routes.size();
        }
        for (int i = left; i < right; i++){
            routeList.add(routes.get(i));
        }
        return routeList;
    }

    public Response updateRoute(Integer id, RouteDto routeDto){
        Route route = routeDAO.findById(id);
        if (route != null){
            addOrUpdateRoute(route, routeDto);
            return Response.ok().build();
        }else{
            throw new NotFoundException("route is not found");
        }
    }

    public Response countDistanceEquals(int distance){
        long c = routeDAO.countDistanceEquals(distance);
        return Response.ok().entity(c).build();
    }

    public Response countDistanceGreater(int distance) {
        long c = routeDAO.countDistanceGreater(distance);
        return Response.ok().entity(c).build();
    }

    public Response findNameContainsSubstr(String substr) {
        HashMap<String, String> h = new HashMap<>();
        h.put("name", "like:" + substr);
        List<Route> routes = routeDAO.sortAndFilter(FilterSortUtil.getOrdersList("+id"),
                FilterSortUtil.getFiltersList(h));
        return Response.ok().entity(routes).build();
    }
}
