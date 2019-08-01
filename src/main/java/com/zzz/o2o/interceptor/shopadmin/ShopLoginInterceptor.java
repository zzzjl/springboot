package com.zzz.o2o.interceptor.shopadmin;

import com.zzz.o2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ShopLoginInterceptor extends HandlerInterceptorAdapter {
    /*
    * 登录验证拦截器
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handler) throws Exception{
        //从SESSION中取出数据来
        Object userObj = request.getSession().getAttribute("user");
        if(userObj!=null){
            //若用户信息不为空。则将用户信息转换成PersonInfo
            PersonInfo user = (PersonInfo)userObj;
            //做空值判断，确保userId不为空且可用状态为1，用户类型为 店家
            if(user!=null&&user.getUserId()!=null&&user.getUserId()>0&&user.getEnableStatus()==1){
                //若通过验证则返回true，返回true，用户接下来的操作才能执行
                return true;
            }
        }
        //若不满足验证，则直接跳转账号登录
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open('"+request.getContextPath()+"/local/login?usertype=2','_self')");
        out.println("</script>");
        out.println("</html>");
        return false;
    }

}
