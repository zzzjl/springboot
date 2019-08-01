package com.zzz.o2o.web.shopadmin;

import com.zzz.o2o.dto.ProductCategoryExecution;
import com.zzz.o2o.dto.Result;
import com.zzz.o2o.entity.ProductCategory;
import com.zzz.o2o.entity.Shop;
import com.zzz.o2o.enums.ProductCategoryStateEnum;
import com.zzz.o2o.exceptions.ProductCategoryOperationException;
import com.zzz.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopadmin" )
@ResponseBody
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @RequestMapping(value = "/getproductcategorylist",method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
        //TO BE REMOVED
        Shop shop = new Shop();
        shop.setShopId(1L);
        request.getSession().setAttribute("currentShop",shop);    //记得注释

        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        List<ProductCategory> list = null;
        if(currentShop != null&&currentShop.getShopId()>0){
            list = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true ,list);

        }else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false,ps.getStateInfo(),ps.getState());
        }
    }
    @RequestMapping(value ="/addproductcategorys", method= RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory>productCategoryList, HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory pc :productCategoryList){
            pc.setShopId(currentShop.getShopId());
        }
        if(productCategoryList!=null&&productCategoryList.size()>0){
            try {
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
                if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少输入一个店铺类别");
        }
        return modelMap;
    }
    @RequestMapping(value="/removeproductcategory",method = RequestMethod.POST)
    @ResponseBody
    private Map<String ,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
        Map<String ,Object>modelMap = new HashMap<String,Object>();
        if(productCategoryId!=null&&productCategoryId>0){
            try{
                Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
                ProductCategoryExecution pe =productCategoryService.deletePorductCategory(productCategoryId,currentShop.getShopId());
                if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch(ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少选择一个类别");
        }
        return modelMap;
    }

}
