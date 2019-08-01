package com.zzz.o2o.interceptor.shopadmin;

import com.zzz.o2o.entity.Shop;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handler) throws Exception {
        //从session中获取当前选择地店铺
        Shop currentshop = (Shop)request.getSession().getAttribute("currentShop");
        @SuppressWarnings("unchecked")
        //从session中获取可操作店铺列表
        List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
        //非空判断
        if(currentshop!=null&&shopList !=null){
            //遍历可操作的店铺列表
            for(Shop shop :shopList){
                //如果当前店铺在列表里则返回true
                if(shop.getShopId() == currentshop.getShopId()){
                    return true;
                }
            }
        }
    //若不满足验证 返回false 终止用户执
    return false;
    }
}
