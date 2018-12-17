package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.BeanFactory;
import cn.itcast.travel.util.JedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao;
    private Jedis jedis;

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * 查询旅游分类列表
     * @return
     */
    @Override
    public String findCategory() {
        //1.通过redis连接池获取redis

        //2.查询缓存是否有旅游分类字段
        String category = jedis.get("category");
        if (category == null|| category.length() ==0){
            //2.1 没有缓存取数据库数据
            System.out.println("数据库");
            List<Category> category1 = categoryDao.findCategory();
            //2.1.1 将取出的数据存入缓存
            ObjectMapper om = new ObjectMapper();
            String s = "";
            try {
                s = om.writeValueAsString(category1);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            jedis.set("category",s);
            //返回数据
            return s;
        }else {
            System.out.println("缓存");
            //2.2 有缓存，直接返回
            return category;
        }
    }
}
