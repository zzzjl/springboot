package com.zzz.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzz.o2o.dto.ImageHolder;
import com.zzz.o2o.dto.ShopExecution;
import com.zzz.o2o.entity.Area;
import com.zzz.o2o.entity.PersonInfo;
import com.zzz.o2o.entity.Shop;
import com.zzz.o2o.entity.ShopCategory;
import com.zzz.o2o.enums.ShopStateEnum;
import com.zzz.o2o.log.LogShowParams;
import com.zzz.o2o.service.AreaService;
import com.zzz.o2o.service.ShopCategoryService;
import com.zzz.o2o.service.ShopService;
import com.zzz.o2o.util.CodeUtil;
import com.zzz.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService ;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    @RequestMapping(value = "/getshopmanagementinfo" , method = RequestMethod.GET )
    @ResponseBody
    public Map<String,Object>getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopid");
        if(shopId<=0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if(currentShopObj==null){
                modelMap.put("redirct",true);
                modelMap.put("url","/shopadmin/shoplist");
            }else {
                Shop currentShop = (Shop)currentShopObj;
                modelMap.put("redirct",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirct",false);
        }
        return modelMap ;
    }
    @RequestMapping(value = "/getshoplist" , method = RequestMethod.GET )
    @ResponseBody
    public Map<String,Object>getShopList (HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        PersonInfo user = new PersonInfo();
       // user.setUserId(1L);
      //  request.getSession().setAttribute("user",user);
        user = (PersonInfo)request.getSession().getAttribute("user");
        try {
            Shop shopCondition =new Shop();
            //user.setName("WOOOO");
            shopCondition.setOwner(user);
            ShopExecution se =shopService.getShopList(shopCondition,0,100);
            modelMap.put("shopList",se.getShopList());
            //列出店铺成功后，将店铺放入SESSION中作为权限验证的依据，即该账号只操作自己的店铺
            request.getSession().setAttribute("shopList",se.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap ;

    }
    @RequestMapping(value = "/getshopbyid" ,method = RequestMethod.GET)
    @ResponseBody
    public Map<String ,Object>getShopById(HttpServletRequest request){
        Map<String,Object>modelMap = new HashMap<String,Object>();
        Long shopId = HttpServletRequestUtil.getLong(request,"shopid");
        if(shopId>-1){
            try {
                Shop shop = shopService.getShopById(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop",shop);
                modelMap.put("areaList",areaList);
                modelMap.put("success",true);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap ;

    }

    @RequestMapping(value = "/getshopinitinfo" ,method = RequestMethod.GET)
    @ResponseBody
    @LogShowParams(requestUrl = "/getshopinitinfo")
    public  Map<String , Object>getShopInitInfo(){
        Map<String ,Object> modelMap = new HashMap<String, Object>();
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
    }

    //修改店铺信息
    @RequestMapping(value = "/modifyshop" ,method = RequestMethod.POST)
    @ResponseBody
    public   Map<String ,Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码！");
            return modelMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        //修改店铺
        if (shop != null && shop.getShopId() != null) {
            PersonInfo owner = new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
            try {
                //File FileStream = new File(PathUtil.getImgBasePath()+ ImageUtil.getRandomFileName());
            //*FileStream.createNewFile();*//*
                // inputStreamToFile(shopImg.getInputStream(),FileStream);
                ShopExecution se;
                if (shopImg == null) {
                    se = shopService.modifyShop(shop, null);
                } else {
                    ImageHolder imageHolder =new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                    se = shopService.modifyShop(shop,imageHolder);
                }
                if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
                return modelMap;
            } catch (Exception e) {
                throw new RuntimeException("文件流转换发生异常:" + e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺Id");
            return modelMap;
        }
    }

    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    @LogShowParams(requestUrl = "/registershop")
    //接受并转换相应的参数，店铺信息和图片信息A
    public  Map<String ,Object> registerShop(HttpServletRequest request){
        Map<String ,Object> modelMap = new HashMap<String, Object>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码！");
            return modelMap ;
        }
        String shopStr= HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper= new ObjectMapper();
        Shop shop = null ;
        try{
            shop = mapper.readValue(shopStr,Shop.class);
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap ;
        }
        CommonsMultipartFile shopImg= null ;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;
            shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            return modelMap ;
        }

     //注册店铺
        if(shop!=null&&shopImg!=null){
            PersonInfo owner =(PersonInfo)request.getSession().getAttribute("user");   //暂时先自己new
            shop.setOwner(owner);
            try {
            //File FileStream = new File(PathUtil.getImgBasePath()+ ImageUtil.getRandomFileName());
            /*FileStream.createNewFile();*/
           // inputStreamToFile(shopImg.getInputStream(),FileStream);
                ImageHolder imageHolder =new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
            ShopExecution se =shopService.addShop(shop,imageHolder);
            if(se.getState()==ShopStateEnum.CHECK.getState()) {
                modelMap.put("success",true);
                //该用户可以操作的列表
                List<Shop> shopList =(List<Shop>)request.getSession().getAttribute("shopList");
                if(shopList==null||shopList.size()==0){
                    shopList = new ArrayList<Shop>();
                }
                shopList.add(se.getShop());
                request.getSession().setAttribute("shopList",shopList);
              }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",se.getStateInfo());
            }
            return modelMap ;
            }catch(Exception e){
                throw new RuntimeException("文件流转换发生异常:"+e.getMessage());
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap ;
        }
     //返回结果

    }
   /* private static void inputStreamToFile(InputStream ins , File file){
        OutputStream os =null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer)) != -1 ) {
                os.write(buffer, 0, bytesRead);
            }
        }catch(Exception e){
                throw new RuntimeException("调用inputStreamToFile异常:"+e.getMessage());
            }finally{
                try{
                    if(os != null){
                        os.close();
                    }
                    if(ins != null)
                        ins.close();
                }catch(IOException e){
                    throw new RuntimeException("调用inputStreamToFile关闭IO异常:"+e.getMessage());
                }
            }


        }*/

    }


