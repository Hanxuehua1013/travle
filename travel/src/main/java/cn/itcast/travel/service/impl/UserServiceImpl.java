package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.BeanFactory;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 注册，判断注册信息是否正确，若全部正确则注册成功，并给用户发送一封激活邮件
     * @param user
     * @return
     */
    @Override
    public ResultInfo registerUser(User user) {
        ResultInfo resultInfo = new ResultInfo();
        //判断用户名格式是否正确 格式：6-12个字符
        if(!user.getUsername().matches("^\\w{6,12}$")){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("账户格式错误");
            return resultInfo;
        }
        //判断密码格式是否正确 格式：6-12个字符
        else if (!user.getPassword().matches("^\\w{6,12}$")){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("密码格式错误");
            return resultInfo;
        }
        //判断邮箱格式是否正确 格式：aaa@aa.aa
        else if (!user.getEmail().matches("^\\w+@\\w+\\.\\w+$")){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("邮箱格式错误");
            return resultInfo;
        }
        //判断姓名格式是否正确 格式：不能有数字
        else if (user.getName().matches(" \\d*.*\\d+.*\\d*")){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("姓名格式错误");
            return resultInfo;
        }
        //判断手机号格式是否正确 格式：开头不能为0的11位数字
        else if (user.getTelephone().length()!=11&&!user.getPassword().matches(" ^[1-9]\\d{10}")){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("手机号格式错误");
            return resultInfo;
        }
        //判断出生日期格式是否正确 格式：有数字
        else if (!user.getBirthday().matches("\\d+.*")){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("出生日期格式错误");
            return resultInfo;
        }
        //若格式都正确，进行注册
        else {
            //查询注册用户名是否存在
            User userByName = userDao.findUserByName(user.getName());
            //注册用户名不存在在进行注册
            if (userByName == null){
                //进行注册
                String uuid = UuidUtil.getUuid();
                int sum =  userDao.registerUser(user,uuid);
                //判断是否注册成功
                if(sum>0){
                    //注册成功给用户发送一封激活邮件
                    MailUtils.sendMail(user.getEmail(),"您的验证码为:"+uuid+"<a href='http://localhost/user/active?code="+uuid+"'>点击激活</a>","激活");
                    resultInfo.setFlag(true);
                    return resultInfo;
                }else {
                    //注册失败返回错误信息
                    resultInfo.setFlag(false);
                    resultInfo.setErrorMsg("注册失败，请检查注册信息！");
                    return resultInfo;
                }
            }else {
                //用户存在，返回错误信息
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("用户已存在");
                return resultInfo;
            }
        }

    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean activeUser(String code) {
        //根据激活码更改用户激活状态
       if(userDao.activeUser(code)>0){
            return true;
       }else {
           return false;
       }
    }

    /**
     * 登陆判断
     * @param user
     * @return
     */
    @Override
    public User userLogin(User user) {
        User u = userDao.userLogin(user);
        return u;

    }

    /**
     * 查询用户是否存在
     * @param username
     * @return
     */
    @Override
    public Boolean getUserByName(String username) {
        User user = userDao.getUserByName(username);
        if(user!=null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断用户是否处于激活状态
     * @param username
     * @return
     */
    @Override
    public boolean isActive(String username) {
        User user = userDao.getUserByName(username);
        if(user.getStatus().equals("Y")){
            return true;
        }else {
            return false;
        }

    }

    /**
     * 收藏线路
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public int collectRoute(String rid, int uid) {
        //更新用户收藏表
       int a = userDao.updateFavovite(rid,uid);
       //查询收藏数量
       int count =  userDao.findFavorite(rid);
       if(a>0){
           //更新成功，更新线路表收藏数量
           int tag = userDao.updateCollectCount(rid,count+1);
           if(tag>0){
               //更新成功，收藏数量+1
               count++;
           }
       }
       return count;
    }

    /**
     * 取消收藏
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public int dicollectRoute(String rid, int uid) {
        //更新用户收藏表
        int a = userDao.reduceFavovite(rid,uid);
        //查询收藏数量
        int count =  userDao.findFavorite(rid);
        if(a>0){
            //更新成功，更新线路表收藏数量
            int tag = userDao.updateCollectCount(rid,count-1);
            if(tag>0){
                //更新成功，收藏数量-1
                count--;
            }
        }
        return count;
    }

    /**
     * 查找用户是否收藏过本线路
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean findFavorite(String rid, int uid) {
        Favorite favorite = userDao.findFavoriteByUser(rid,uid);
        if(favorite!=null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 用户收藏-->根据用户id查询用户收藏的线路信息
     * @param uid
     * @param currentPage
     * @param rname
     * @return
     */
    @Override
    public PageBean<Route>  findUserFavoriteRoute(int uid, int currentPage, String rname) {
        int pageSize = 12;//设置每页显示数目
        int start = (currentPage-1)*pageSize;
        //查询收藏的线路集合
        List<Route> routeList =userDao.findFavoriteRoute(uid,start,pageSize,rname);
        int totalCount = userDao.finFavoriteRouteCount(uid,rname);//用户收藏线路总数
        PageBean<Route> routePageBean = new PageBean<>();
        routePageBean.setTotalCount(totalCount);
        routePageBean.setCurrentPage(currentPage);
        routePageBean.setList(routeList);
        routePageBean.setPageSize(pageSize);
        int totalPage = totalCount%pageSize == 0 ? totalCount/pageSize : (totalCount/pageSize)+1;//总页数
        routePageBean.setTotalPage(totalPage);
        return routePageBean;
    }
}
