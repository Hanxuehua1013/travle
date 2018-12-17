package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    //获取template对象
    private JdbcTemplate template;


    public void setTemplate(JdbcTemplate template) {
        System.out.println("routDao===="+template);
        this.template = template;

    }

    /**
     * 查询总记录数
     * @return
     */
    @Override
    public int findTotalCount(int cid, String rname) {
//        String sql = "select count(*) from tab_route WHERE cid = ?";
        //1.定义模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> parm = new ArrayList<>();
        if(cid!=0){
            sb.append(" and cid = ? ");
            parm.add(cid);
        }
        if (rname!=""&&rname!=null&&!"null".equals(rname)){
            sb.append(" and rname like ?");
            parm.add("%"+rname+"%");
        }
        sql = sb.toString();
        Integer totalCount = template.queryForObject(sql, Integer.class,parm.toArray());
        return totalCount;
    }

    /**
     * 查询线路信息
     * @param cid
     * @param begin
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public List<Route> findRoutList(int cid, int begin, int pageSize, String rname) {
//        String sql = "select * from tab_route where cid = ? limit ?,?";
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> parm = new ArrayList<>();
        if(cid!=0){
            sb.append(" and cid = ? ");
            parm.add(cid);
        }
        if (rname!=""&&rname!=null&!"null".equals(rname)){
            sb.append(" and rname like ?");
            parm.add("%"+rname+"%");
        }
        sb.append(" limit ?,?");
        sql = sb.toString();
        parm.add(begin);
        parm.add(pageSize);
        List<Route> query = null;
        try {
            query = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class),parm.toArray());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return query;
    }

    /**
     * 获取线路基本信息
     * @param rid
     * @return
     */
    @Override
    public Route findRoutDetail(String rid) {
        String sql = "select * from tab_route where rid = ?";
        Route route = null;
        try {
            route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), Integer.parseInt(rid));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return route;
    }

    /**
     * 获取线路图片
     * @param rid
     * @return
     */
    @Override
    public List<RouteImg> findRoutImg(String rid) {
        String sql = "select * from tab_route_img where rid = ?";
        List<RouteImg> routeImg = null;
        try {
            routeImg = template.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), Integer.parseInt(rid));
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return routeImg;
    }

    /**
     * 获取线路商家信息
     * @param sid
     * @return
     */
    @Override
    public Seller findRoutSeller(int sid) {
        String sql = "select * from tab_seller where sid = ?";
        Seller seller = null;
        try {
            seller = template.queryForObject(sql, new BeanPropertyRowMapper<Seller>(Seller.class), sid);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return seller;
    }

    /**
     * 查询线路分类
     * @param cid
     * @return
     */
    @Override
    public Category findCategory(int cid) {
        String sql = "select * from tab_category where cid = ?";
        Category category = null;
        try {
            category = template.queryForObject(sql, new BeanPropertyRowMapper<Category>(Category.class), cid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return category;
    }
    @Override
    public List<Route> findFavoriteRank(int start,int pageSize,String name,int samllPrice,int bigPrice) {
//        String sql = "select * from tab_route where cid = ? limit ?,?";
//        String sql = "SELECT * FROM tab_route WHERE rname LIKE ? HAVING price BETWEEN ? AND ? ORDER BY COUNT DESC LIMIT ?,?";
        String sql = "select * from tab_route where 1=1";
        StringBuilder sb = new StringBuilder(sql);
        ArrayList<Object> parm = new ArrayList<>();
        if (name!=""&&name!=null&!"null".equals(name)){
            sb.append(" and rname like ?");
            parm.add("%"+name+"%");
        }
        if(samllPrice>=0&&bigPrice>0){
            sb.append(" having price between ? and ? ");
            parm.add(samllPrice);
            parm.add(bigPrice);
        }
        sb.append(" order by count desc ");
        sb.append(" limit ?,?");
        sql = sb.toString();
        parm.add(start);
        parm.add(pageSize);
        List<Route> query = null;
        try {
            query = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class),parm.toArray());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return query;
    }
}
