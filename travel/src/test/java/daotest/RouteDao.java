package daotest;

import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class RouteDao {

    public static void main(String[] args) throws JsonProcessingException {
      /*  RouteDaoImpl routeDao = new RouteDaoImpl();
        int totalCount = routeDao.findTotalCount(5);
        System.out.println(totalCount);
        List<Route> routList = routeDao.findRoutList(5, 5, 5);
        ObjectMapper om = new ObjectMapper();
        try {
            String s = om.writeValueAsString(routList);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
/*        RouteServiceImpl routeService = new RouteServiceImpl();
        PageBean pageBean = routeService.searchRoutInfo(0, 1, "西安");
        ObjectMapper om = new ObjectMapper();
        try {
            String s = om.writeValueAsString(pageBean);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
     /*   RouteServiceImpl routeService = new RouteServiceImpl();
        Route route = routeService.searchRoutDetail("262");
        ObjectMapper om = new ObjectMapper();
        try {
            String rou = om.writeValueAsString(route);
            System.out.println(rou);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
        RouteServiceImpl routeService = new RouteServiceImpl();
        PageBean<Route> favoriteRank = routeService.findFavoriteRank(1, null, 0, 0);
        ObjectMapper om = new ObjectMapper();
        System.out.println(om.writeValueAsString(favoriteRank));

    }

}
