package com.zzz.o2o.web.frontend;

import com.zzz.o2o.dto.ShopExecution;
import com.zzz.o2o.entity.Area;
import com.zzz.o2o.entity.Shop;
import com.zzz.o2o.entity.ShopCategory;
import com.zzz.o2o.service.AreaService;
import com.zzz.o2o.service.ShopCategoryService;
import com.zzz.o2o.service.ShopService;
import com.zzz.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;
    /*
    *
    * 返回商品列表页里的ShopCategory列表，以及区域信息列表
    *
    * */
    @RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String ,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object>modelMap = new HashMap<String,Object>();
        //试着从前端请求中获取parentId
        long parentId = HttpServletRequestUtil.getLong(request,"parentId");
        List<ShopCategory>shopCategoryList = null;
        if(parentId!=-1){
            //如果parentId存在，则取出该商品类别下的二级商品类别
            try{
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            }catch(Exception e ){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }else{
            try{
                //如果parentId不存在，则取出所有一级的商品类别列表
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            }catch(Exception e ){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area>areaList = null;
        try{
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return  modelMap ;
    }
    /*

    获取指定条件下的店铺列表

    * */
    @RequestMapping(value = "/listshops" ,method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object>listShops(HttpServletRequest request){
        Map<String,Object>modelMap =  new HashMap<String,Object>();
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取一页要展示的数据条数
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //非空判断
        if(pageIndex>-1&&pageSize>-1){
            //试着获取一级店铺类别的Id
            long parentId = HttpServletRequestUtil.getLong(request,"parentId");
            //试着获取二级类别的Id
            long shopCategoryId = HttpServletRequestUtil.getLong(request,"shopCategoryId");
            //试着获取区域Id
            int areaId = HttpServletRequestUtil.getInt(request,"areaId");
            //试着获取模糊查询的名字
            String shopName = HttpServletRequestUtil.getString(request,"shopName");
            //获取组合后的查询条件
            Shop shopCondition = compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
            //根据上面的条件获取查询结果，并返回总数
            ShopExecution shopExecution = shopService.getShopList(shopCondition,pageIndex,pageSize);
            modelMap.put("shopList",shopExecution.getShopList());
            modelMap.put("count",shopExecution.getCount());
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","页数或者起始数据数量非法！");
        }
        return modelMap ;
    }
    private Shop compactShopCondition4Search(long parentId,long shopCategoryId,int areaId,String shopName){
        Shop shopCondition = new Shop();
        if(parentId!=-1L){
            //查询某个一级商店类别下的所有二级类别的店铺列表
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if(shopCategoryId!=-1L){
            //查询某个二级店铺类别下的列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if(areaId!=-1){
            //查询某个区域下的店铺列表
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if(shopName!=null){
            //查询名字包含SHOPNAME的店铺列表
            shopCondition.setShopName(shopName);
        }
        //而且要是审核成功的店铺
        shopCondition.setEnableStatus(1);
        return shopCondition ;
    }



}
