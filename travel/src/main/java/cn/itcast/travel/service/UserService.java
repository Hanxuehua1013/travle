package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;

/**
 * 用户业务层接口
 */
public interface UserService {
    /**
     * 注册
     * @param user
     * @return
     */
    ResultInfo registerUser(User user);

    /**
     * 激活用户
     * @param code
     * @return
     */
    boolean activeUser(String code);

    /**
     * 登陆
     * @param user
     * @return
     */
    User userLogin(User user);

    /**
     * 查询用户是否存在
     * @param username
     * @return
     */
    Boolean getUserByName(String username);

    /**
     * 判断用户是否激活
     * @param username
     * @return
     */
    boolean isActive(String username);

    /**
     * 收藏线路
     * @param rid
     * @param uid
     * @return
     */
    int collectRoute(String rid, int uid);

    /**
     * 取消收藏
     * @param rid
     * @param uid
     * @return
     */
    int dicollectRoute(String rid, int uid);

    /**
     * 查询用户是否收藏7
     * @param rid
     * @param uid
     * @return
     */
    boolean findFavorite(String rid, int uid);

    /**
     * 查询用户收藏线路信息
     * @param uid
     * @param i
     * @param rname
     * @return
     */
    PageBean<Route> findUserFavoriteRoute(int uid, int i, String rname);
}
