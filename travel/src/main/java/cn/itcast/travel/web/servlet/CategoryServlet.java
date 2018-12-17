package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/category/*")

public class CategoryServlet extends BaseServlet {
    //获取service
    private CategoryService categoryService = getService("categoryService", CategoryService.class);
/*    @Autowired
    private CategoryService categoryService;*/

    /**
     * 查询旅游分类列表
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //调用方法查询旅游分类
        String category = categoryService.findCategory();
        //回写数据
        response.getWriter().write(category);
    }
}
