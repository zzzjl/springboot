package com.zzz.o2o.web.shopadmin;

//import com.zzz.log.LogShowParams;

import com.zzz.o2o.log.LogShowParams;
import com.zzz.o2o.service.AreaService;
import com.zzz.o2o.service.ShopCategoryService;
import com.zzz.o2o.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/hello")

public class TestController {

   // @LogShowParams(requestUrl = "/index")
   @Autowired
    private ShopService shopService ;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    String  s = "hello!";
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @LogShowParams(requestUrl = "/test")

    public void  getShopInitInfo() {
        System.out.println(s);


        //return new HashMap<String,Object>();


    }


   /* @RequestMapping(value = "/getshopinitinfo" ,method =RequestMethod.GET)
    @LogShowParams(requestUrl = "/lll")
    private void getShopInitInfo(){
        List<Area> list = areaService.getAreaList();
        System.out.println("hello");

    }*/
        /*Map<String ,Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();
        try{
            ShopCategory shopCategoryCondition =new ShopCategory();
            shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList" ,shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success", true);
        }catch(Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap ;
    }*/
    }


