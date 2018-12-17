package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;

import java.util.List;

public interface RouteDao {
    /**
     * 根据分类id查询该分类下线路总数
     * @param cid
     * @param rname
     * @return
     */
    public int findTotalCount(int cid, String rname);

    /**
     * 根据分类id分页查询该分类下路线信息
     * @param cid
     * @param begin
     * @param pageSize
     * @param rname
     * @return
     */
    public List<Route> findRoutList(int cid, int begin, int pageSize, String rname);

    /**
     * 根据线路id查询该线路详细信息
     * @param rid
     * @return
     */
    Route findRoutDetail(String rid);

    /**
     * 根据线路id查询该线路的图片连接
     * @param rid
     * @return
     */
    List<RouteImg> findRoutImg(String rid);

    /**
     * 查询线路商家信息
     * @param sid
     * @return
     */
    Seller findRoutSeller(int sid);

    /**
     * 查询线路分类信息
     * @param cid
     * @return
     */
    Category findCategory(int cid);

    /**
     * 分页查询线路收藏排行榜
     * @param start
     * @param pageSize
     * @param name
     * @param samllPrice
     * @param bigPrice
     * @return
     */
    List<Route> findFavoriteRank(int start,int pageSize,String name,int samllPrice,int bigPrice);
}
