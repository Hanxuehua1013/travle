package cn.itcast.travel.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 收藏实体类
 */
public class Favorite implements Serializable {
    private  int rid;//旅游线路对象
    private Date date;//收藏时间
    private int uid;//所属用户

    /**
     * 无参构造方法
     */
    public Favorite() {
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Favorite(int rid, Date date, int uid) {

        this.rid = rid;
        this.date = date;
        this.uid = uid;
    }
}
