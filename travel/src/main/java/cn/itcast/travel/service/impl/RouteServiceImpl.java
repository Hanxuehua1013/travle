package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.util.BeanFactory;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    //获取操作线路数据库对象
    private RouteDao routeDao;
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setRouteDao(RouteDao routeDao) {

        this.routeDao = routeDao;
    }

    /**
     * 查询线路信息
     * @param cid
     * @param currentPage
     * @param rname
     * @return
     */
    @Override
    public PageBean searchRoutInfo(int cid, int currentPage, String rname) {
        //创建一个页面信息对象
        PageBean<Route> routePageBean = new PageBean<>();
        if (currentPage<0){
            currentPage=1;
        }
        routePageBean.setCurrentPage(currentPage);
        //1.获取总信息数
        int totalCount = routeDao.findTotalCount(cid,rname);
        routePageBean.setTotalCount(totalCount);
        //2.定义每页显示条数
        int pageSize = 8;
        routePageBean.setPageSize(pageSize);
        //3.获取分页起始位置
        int begin = (currentPage-1)*pageSize;
        if (begin<0){
            begin=0;
        }
        List<Route> routList = routeDao.findRoutList(cid, begin, pageSize,rname);
        routePageBean.setList(routList);
        //4.获取总页数
        int totalPage = totalCount%pageSize == 0 ? totalCount/pageSize : (totalCount/pageSize)+1;
        routePageBean.setTotalPage(totalPage);
        return routePageBean;
    }

    /**
     * 查询线路详细信息
     * @param rid
     * @return
     */
    @Override
    public Route searchRoutDetail(String rid) {
        //1.获取线路基本信息
        Route route = routeDao.findRoutDetail(rid);
        //2.获取线路图片
        List<RouteImg> routeImg =routeDao.findRoutImg(rid);
        //3.获取线路商家信息
        Seller routeSeller = routeDao.findRoutSeller(route.getSid());
        //4.获取线路所属分类
        Category category = routeDao.findCategory(route.getCid());
        route.setSeller(routeSeller);
        route.setRouteImgList(routeImg);
        route.setCname(category.getCname());
        return route;
    }
    /**
     * 根据线路id查询该线路的收藏数量
     * @param rid
     * @return
     */
    @Override
    public int findCollectCount(String rid){
        int count =  userDao.findFavorite(rid);
        return count;
    }

    /**
     * 查询所有线路排行榜
     * @param
     * @return
     */
    @Override
    public PageBean<Route> findFavoriteRank(int currentPage,String name,int samllPrice,int bigPrice) {
        int pageSize = 8;//定义每页显示条数
        int start =(currentPage -1)*pageSize;
        int totalCount = routeDao.findTotalCount(0,name);
        int totalPage = totalCount%pageSize == 0 ? totalCount/pageSize : totalCount/pageSize+1;
        List<Route> favoriteRank = routeDao.findFavoriteRank(start,pageSize,name,samllPrice,bigPrice);
        PageBean<Route> pageBean = new PageBean<>();
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(favoriteRank);
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }
}
