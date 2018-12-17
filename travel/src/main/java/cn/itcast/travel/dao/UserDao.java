package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;

import java.util.List;

public interface UserDao {
    /**
     *注册
     * @param user
     * @param uuid
     * @return
     */
    int registerUser(User user, String uuid);

    /**
     *激活
     * @param code
     * @return
     */
    int activeUser(String code);

    /**
     *判断用户名是否存在
     * @param name
     * @return
     */
    User findUserByName(String name);

    /**
     * 用户登陆
     * @param user
     * @return
     */
    User userLogin(User user);

    /**
     * 获取用户激活状态
     * @param username
     * @return
     */
    User getUserByName(String username);

    /**
     *用户收藏线路
     * @param rid
     * @param uid
     * @return
     */
    int updateFavovite(String rid, int uid);

    int updateCollectCount(String rid, int count);

    /**
     * 查询收藏数量
     * @param rid
     * @return
     */
    int findFavorite(String rid);

    /**
     * 消除收藏
     * @param rid
     * @param uid
     * @return
     */
    int reduceFavovite(String rid, int uid);

    /**
     * 查找用户是否收藏过次线路
     * @param rid
     * @param uid
     * @return
     */
    Favorite findFavoriteByUser(String rid, int uid);

    /**
     * 查询用户所用收藏线路信息
     * @param uid
     * @param start
     * @param pageSize
     * @param rname
     * @return
     */
    List<Route> findFavoriteRoute(int uid, int start, int pageSize, String rname);

    /**
     * 查询用户收藏线路总数
     * @param uid
     * @param rname
     * @return
     */
    int finFavoriteRouteCount(int uid, String rname);
}
