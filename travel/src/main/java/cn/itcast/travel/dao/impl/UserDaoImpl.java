package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    /**
     * 获取template
     */
    private JdbcTemplate template;


    public void setTemplate(JdbcTemplate template) {
        this.template = template;
        System.out.println("userDao===="+template);
    }

    /**
     * 注册存储用户
     *
     * @param user
     * @param uuid
     * @return
     */
    @Override
    public int registerUser(User user, String uuid) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        int update = 0;
        try {
            update = template.update(sql,
                    user.getUsername(),
                    user.getPassword(),
                    user.getName(),
                    user.getBirthday(),
                    user.getSex(),
                    user.getTelephone(),
                    user.getEmail(),
                    "N",
                    uuid
            );
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return update;
    }

    /**
     * 根据状态码更新激活状态
     *
     * @param code
     * @return
     */
    @Override
    public int activeUser(String code) {
        String sql = "update tab_user set status=? where code=?";
        int y = 0;
        try {
            y = template.update(sql, "Y", code);
        } catch (DataAccessException e) {
            template.update("delete from tab_user WHERE code = ?", code);
            System.out.println(e.getMessage());
        }
        return y;
    }

    /**
     * 根据姓名查找用户
     *
     * @param name
     * @return
     */
    @Override
    public User findUserByName(String name) {
        String sql = "select * from tab_user where username = ?";
        User user = null;
        try {
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), name);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    /**
     * 用户登陆，根据用户名和密码判断用户是否存在
     *
     * @param user
     * @return
     */
    @Override
    public User userLogin(User user) {
        String sql = "select * from tab_user where username=? and password=?";
        User user1 = null;
        try {
            user1 = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername(), user.getPassword());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return user1;
    }

    /**
     * 查询用户名是否存在
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByName(String username) {
        String sql = "select status from tab_user where username = ?";
        User user = null;
        try {
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    /**
     * 更新用户收藏
     *
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public int updateFavovite(String rid, int uid) {
        String sql = "insert into tab_favorite(rid,date,uid) values(?,?,?)";
        int update = template.update(sql, Integer.parseInt(rid), null, uid);
        return update;
    }

    /**
     * 更新线路，并返回
     *
     * @param rid
     * @param count
     * @return
     */
    @Override
    public int updateCollectCount(String rid, int count) {
        int i = Integer.parseInt(rid);
        String sql = "update tab_route set count = ? where rid = ?";
        //收藏数加一
        int update = template.update(sql, count, i);
        return update;
    }

    /**
     * 查询线路收藏数量
     *
     * @param rid
     * @return
     */
    @Override
    public int findFavorite(String rid) {
        String sql = "select count from tab_route where rid = ?";
        Integer integer = template.queryForObject(sql, Integer.class, Integer.parseInt(rid));
        return integer;
    }

    /**
     * 删除用户收藏
     *
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public int reduceFavovite(String rid, int uid) {
        String sql = "delete from tab_favorite where rid =? and uid = ? ";
        int update = template.update(sql, Integer.parseInt(rid), uid);
        return update;
    }

    /**
     * 查询用户是否收藏本线路
     *
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public Favorite findFavoriteByUser(String rid, int uid) {
        String sql = "select * from tab_favorite where rid = ? and uid = ?";
        Favorite favorite = null;
        try {
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), Integer.parseInt(rid), uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favorite;
    }

    /**
     * 通过用户id查询用户收藏的所有线路
     *
     * @param uid
     * @param start
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public List<Route> findFavoriteRoute(int uid, int start, int pageSize, String rname) {

        ArrayList<Object> list = new ArrayList<>();
        String sql = "SELECT * FROM tab_route t1 ,(SELECT * FROM tab_favorite WHERE uid = ?) t2 WHERE 1=1";
        StringBuilder sb = new StringBuilder(sql);
        list.add(uid);
        if (rname != null && !rname.equals("null") &&!rname.equals("")) {
            sb.append(" and t1.rid = t2.rid and rname like ? limit ?,?");
            list.add("%" + rname + "%");
            list.add(start);
            list.add(pageSize);
        } else {
            sb.append(" and t1.rid = t2.rid limit ?,?");
            list.add(start);
            list.add(pageSize);
        }

        sql = sb.toString();
        List<Route> query = null;
        try {
            query = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), list.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return query;
    }

    /**
     * 查询用户收藏线路总数-->根据用户id
     *
     * @param uid
     * @param rname
     * @return
     */
    @Override
    public int finFavoriteRouteCount(int uid, String rname) {
        String sql;
        Integer integer;
        if (rname != null && !rname.equals("null") && !rname.equals("")) {
            sql = "SELECT COUNT(*) FROM tab_route t1 ,(SELECT * FROM tab_favorite WHERE uid = ?) t2 WHERE t1.rid = t2.rid AND rname LIKE ? ";
            String rna =  "%" + rname + "%";
            integer = template.queryForObject(sql, Integer.class, uid,rna);
        } else {
            sql = "select count(*) FROM tab_route t1 ,(SELECT * FROM tab_favorite WHERE uid = ?) t2 where t1.rid = t2.rid";
            integer = template.queryForObject(sql, Integer.class, uid);

        }
        return integer;
    }

}
