package com.zzz.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopadmin" , method={RequestMethod.GET})
public class ShopAdminController {
    @RequestMapping(value = "/shopoperation"  )
    public String shopOperation(){
        return "shop/shopoperation";
    }

    @RequestMapping(value = "/shoplist"  )
    public String shopList(){
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopmanagement"  )
    public String shopManagement(){
        return "shop/shopmanagement";
    }
    @RequestMapping(value = "/productcategorymanagement"  )
    public String porductCategoryManagement(){
        return "shop/productcategorymanagement";
    }

    @RequestMapping(value = "/productoperation")
    //商品添加编辑
    public String productOperation(){
        return "product/productoperation";
    }

    @RequestMapping(value = "/productmanagement")
    //商品管理页面
    public String productManagement(){
        return "product/productmanagement";
    }
}
