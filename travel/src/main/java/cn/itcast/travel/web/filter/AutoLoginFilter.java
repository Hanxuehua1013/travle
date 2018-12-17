package cn.itcast.travel.web.filter;

import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import cn.itcast.travel.util.CheckCookieUtil;
import cn.itcast.travel.web.servlet.BaseServlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 过滤器-->为携带登陆信息cookie的请求进行自动登陆
* */
@WebFilter("/*")
public class AutoLoginFilter implements Filter {
    public void destroy() {
    }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        //获取请求头中的cookie数组
        Cookie[] cookies = request.getCookies();
        User user = new User();
        //判断cookie中是否存在username
        if (CheckCookieUtil.findCookie(cookies,"username")!=null){
            //存在的话，将username、password他们的值存入user中
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("username")){
                    user.setUsername(cookie.getValue());
                }
                if(cookie.getName().equals("password")){
                    user.setPassword(cookie.getValue());
                }
            }
            //使用获得的user进行登陆验证，查看数据库中是否有对应的数据
            UserService userService = new BaseServlet().getService("userService",UserService.class);
            //UserService userService = new UserServiceImpl();
            System.out.println("autoLoginFilter===="+userService);
            User User1 = userService.userLogin(user);
            //有对应数据
            if (User1!=null){
                //将user设置到session域中，证明已登陆过
                request.getSession().setAttribute("user",User1);
            }
        }
        //放行
        chain.doFilter(request, resp);
    }
    public void init(FilterConfig config) throws ServletException {
    }

}
