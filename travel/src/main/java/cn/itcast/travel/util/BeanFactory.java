package cn.itcast.travel.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BeanFactory {

    public static Object getBean(String id){
        //获取并解析存有dao对象全路径的xml配置文件
        String path = BeanFactory.class.getClassLoader().getResource("beans.xml").getPath();
        Document parse = null;
        try {
            parse = Jsoup.parse(new File(path), "utf-8");
            Element element = parse.getElementById(id);
            String className = element.attr("className");
            Class<?> aClass = Class.forName(className);
            //获取反射对象
            Object instance = aClass.newInstance();
            return Proxy.newProxyInstance(BeanFactory.class.getClassLoader(), instance.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return method.invoke(instance,args);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
