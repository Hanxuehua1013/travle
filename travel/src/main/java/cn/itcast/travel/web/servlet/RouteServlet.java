package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    //获取线路Service
    private RouteService routeService = getService("routeService", RouteService.class);;
/*    @Autowired
    private RouteService routeService;*/


    /**
     * 查询路线信息  1.总页数 2.总信息数 3.当前页 4.线路对象集合
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void searchRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        String currentPage = request.getParameter("currentPage");
        String rname = request.getParameter("name");
      /*  System.out.println("cid="+cid);
        System.out.println("cid="+"null".equals(cid));
        System.out.println("currentPage="+currentPage);
        System.out.println("rname="+rname);*/
        if (cid == "" || cid ==null||"null".equals(cid)){
            cid = "0";
        }
        PageBean pageBean = routeService.searchRoutInfo(Integer.valueOf(cid), Integer.valueOf(currentPage),rname);
//        System.out.println(pageBean);
        writeValue(pageBean,response);
    }

    /**
     * 查询线路详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void searchRoutDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        Route route = routeService.searchRoutDetail(rid);
        writeValue(route,response);
    }

    /**
     * 更新线路收藏数量
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updateCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String rid = request.getParameter("rid");
        int collectCount = routeService.findCollectCount(rid);
        Route route = new Route();
        route.setCount(collectCount);
        writeValue(route,response);
    }

    /**
     * 查询线路收藏排行榜
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void favoriteRank(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        String currentPage = request.getParameter("currentPage");
        String name = request.getParameter("name");//搜索线路名称
        String smallPrice = request.getParameter("smallPrice");
        String bigPrice = request.getParameter("bigPrice");
        if(smallPrice==null||smallPrice.equals("")){
            smallPrice="-1";
        }
        if(bigPrice==null||bigPrice.equals("")){
            bigPrice="-1";
        }
        PageBean<Route> pageBean =routeService.findFavoriteRank(Integer.parseInt(currentPage),name,Integer.parseInt(smallPrice),Integer.parseInt(bigPrice));
        writeValue(pageBean,response);
    }
}
