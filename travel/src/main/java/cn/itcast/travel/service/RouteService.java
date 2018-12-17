package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {
    /**
     * 查询线路分页信息
     * @param cid
     * @param currentPage
     * @param rname
     * @return
     */
    public PageBean searchRoutInfo(int cid, int currentPage, String rname);

    /**
     * 查询具体某条线路具体信息
     * @param rid
     * @return
     */
    public Route searchRoutDetail(String rid);

    /**
     * 查询线路收藏数量
     * @param rid
     * @return
     */
    public int findCollectCount(String rid);

    /**
     * 分页查询线路排行榜信息
     * @param currentPage
     * @param name
     * @param samllPrice
     * @param bigPrice
     * @return
     */
    public PageBean<Route> findFavoriteRank(int currentPage,String name,int samllPrice,int bigPrice);
}
