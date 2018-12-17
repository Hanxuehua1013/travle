package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //创建Service层的userService对象
    private UserService userService = getService("userService",UserService.class);
/*    @Autowired
    private UserService userService;*/


    /**
     * 用户注册
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取用户输入的验证码
        String check = request.getParameter("check");
        //获取生成的验证码
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        //清除session域
        request.getSession().invalidate();
        ResultInfo info = new ResultInfo();
        //判断用户输入验证码是否正确
        if (checkcode_server != null&&checkcode_server.equalsIgnoreCase(check)){
            Map<String, String[]> map= request.getParameterMap();
            User user = new User();
            try {
                //用户输入的信息封装成Bean对象
                BeanUtils.populate(user,map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            //进行注册，返回一个传递消息的对象
            info = userService.registerUser(user);
        }else {
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
        }
        //创建jackson核心对象
        ObjectMapper om = new ObjectMapper();
        //将消息对象以json格式相应回前台界面
        om.writeValue(response.getWriter(),info);
    }

    /**
     * 用户登陆
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String checkcode = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        request.getSession().invalidate();
        String check = request.getParameter("check");
        ResultInfo resultInfo = new ResultInfo();
        //判断验证码是否正确
        if (checkcode!=null&&checkcode.equalsIgnoreCase(check)){
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            try {
                //将页面传递的数据进行封装
                BeanUtils.populate(user,map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //查询数据库是否有用户登陆的信息
            //判断用户是否存在
            Boolean statu = userService.getUserByName(user.getUsername());
            //存在
            if(statu){
                //判断用户是否激活
                boolean act = userService.isActive(user.getUsername());
                //已激活
                if(act){
                    //判断用户输入信息是否有误
                    User user1 = userService.userLogin(user);
                    //用户输入信息正确
                    if (user1!=null){
                        //判断是否点击自动登陆
                        String checkbox = request.getParameter("checkbox");
                        if(checkbox!=null&&checkbox.equals("on")){
                            //自动登陆，将用户名和密码存入cookie中
                            Cookie psCookie = new Cookie("password", user.getPassword());
                            Cookie unCookie = new Cookie("username", user.getUsername());
                            //保存密码账号到cookie两天
                            psCookie.setMaxAge(60*60*48);
                            unCookie.setMaxAge(60*60*48);
                            response.addCookie(psCookie);
                            response.addCookie(unCookie);
                        }
                        request.getSession().setAttribute("user",user);
                        resultInfo.setFlag(true);
                    }
                    //用户输入信息错误
                    else {
                        resultInfo.setFlag(false);
                        resultInfo.setErrorMsg("用户名或密码错误");
                    }
                }
                //未激活
                else {
                    resultInfo.setFlag(false);
                    resultInfo.setErrorMsg("用户未激活,<a href='active.html'>点击激活</a>");
                }
            }
            //用户不存在
            else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("用户不存在");
            }
        }
        //验证码错误
        else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
        }
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getWriter(),resultInfo);
    }

    /**
     * 通过用户名查询一个用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session域中取user
        User user = (User) request.getSession().getAttribute("user");
        ResultInfo info = new ResultInfo();
        //判断是user是否存在-->是否登陆
        if (user!=null){
            //处于登陆状态，设置返回信息
            info.setFlag(true);
            info.setData(user.getUsername());

        }else {
            //不处于登陆状态，设置返回信息
            info.setFlag(false);
        }
        //创建jackson对象
        ObjectMapper om = new ObjectMapper();
        //将消息对象以json格式回写到前台页面
        om.writeValue(response.getWriter(),info);
    }

    /**
     * 退出
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //清除session-->登陆状态
        request.getSession().invalidate();
        //清除cookie
        Cookie username = new Cookie("username", "");
        Cookie password = new Cookie("password", "");
        username.setMaxAge(0);
        password.setMaxAge(0);
        response.addCookie(username);
        response.addCookie(password);
        //重定向值登陆界面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    /**
     * 激活用户
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        //根据用户带回的激活码更改用户激活状态
        boolean a = userService.activeUser(code);
        //激活成功，重定向至登陆界面
        if (a){
            response.sendRedirect(request.getContextPath()+"/login.html");
        }
        //激活失败，跳转激活失败页面
        else {
            response.getWriter().write("激活失败，请联系管理员！电话：123456789");
            response.sendRedirect(request.getContextPath()+"/active_fail.html");
        }
    }

    /**
     * 收藏线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void collectRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid = user.getUid();
        int count = userService.collectRoute(rid,uid);
        Route route = new Route();
        route.setCount(count);
        writeValue(route,response);
    }

    /**
     * 取消收藏线路-->用户已登陆
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void dicollectRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid = user.getUid();//获取用户id
        int count = userService.dicollectRoute(rid,uid);
        Route route = new Route();
        route.setCount(count);
        writeValue(route,response);
    }

    /**
     * 查询用户是否收藏本线路-->用户已登陆
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid = user.getUid();
        System.out.println("用户id："+uid);
        boolean tag = userService.findFavorite(rid,uid);
        System.out.println("tag"+tag);
        ResultInfo resultInfo = new ResultInfo();
        if (tag){
            //收藏过，设置标识为true
            resultInfo.setFlag(true);
        }else {
            //未收藏过，设置标识为false
            resultInfo.setFlag(false);
        }
        writeValue(resultInfo,response);
    }

    /**
     * 查询用户的收藏线路信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findUserFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        User user = (User) request.getSession().getAttribute("user");
        String rname = request.getParameter("rname");
        String currentPage = request.getParameter("currentPage");
        int uid = user.getUid();
        PageBean<Route> routePageBean = userService.findUserFavoriteRoute(uid,Integer.parseInt(currentPage),rname);
        writeValue(routePageBean,response);

    }
}
