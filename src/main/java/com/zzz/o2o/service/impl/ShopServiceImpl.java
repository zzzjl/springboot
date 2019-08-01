package com.zzz.o2o.service.impl;

import com.zzz.o2o.dao.ShopDao;
import com.zzz.o2o.dto.ImageHolder;
import com.zzz.o2o.dto.ShopExecution;
import com.zzz.o2o.entity.Shop;
import com.zzz.o2o.enums.ShopStateEnum;
import com.zzz.o2o.exceptions.ShopOperationExceptions;
import com.zzz.o2o.service.ShopService;
import com.zzz.o2o.util.ImageUtil;
import com.zzz.o2o.util.PageCalculator;
import com.zzz.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;
    @Override
    public ShopExecution getShopList(Shop shopCondition ,int pageIndex ,int pageSize){
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex,pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition,rowIndex,pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution(ShopStateEnum.SUCCESS);
        if(shopList!=null){
            se.setShopList(shopList);
            se.setCount(count);
        }else{
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se ;

    }
    @Override
    public Shop getShopById(long shopId){
        return shopDao.quaryShopById(shopId);
    }
    @Override
   public ShopExecution modifyShop(Shop shop,ImageHolder imageHolder)throws ShopOperationExceptions {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            //1.判断是否需要处理图片
            try {
                if (imageHolder.getImage() != null) {
                    Shop tempShop = shopDao.quaryShopById(shop.getShopId());
                    if (tempShop.getShopImg() != null && imageHolder.getImageName() != null && "".equals(imageHolder.getImageName())) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, imageHolder);
                }
                //2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.quaryShopById(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ShopOperationExceptions("modifyShop error" + e.getMessage());
            }


        }
    }
  //  @Override
    @Transactional
   public ShopExecution addShop(Shop shop, ImageHolder imageHolder) {
        //空值判断
        if(shop == null ){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try{
            //给店铺信息赋初始值
        shop.setEnableStatus(0);
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        //添加店铺信息
        int effectedNum= shopDao.insertShop(shop);
        if(effectedNum<=0){
            throw new ShopOperationExceptions("创建店铺失败！");  //当且仅当抛出RuntimeException或其子类时事务才会中止
        }
        if(imageHolder.getImage()!=null){
            //存储图片
            try{
                addShopImg(shop,imageHolder);
            }catch(Exception e){
                throw new ShopOperationExceptions ("addShopImg error:"+e.getMessage());
            }
            //跟新店铺信息的图片地址
            effectedNum = shopDao.updateShop(shop);
            if(effectedNum<=0){
                throw new ShopOperationExceptions("更新店铺图片地址失败！");
            }


        }
        }catch(Exception  e){
            throw new ShopOperationExceptions("addShop error:"+e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }
    private void addShopImg(Shop shop , ImageHolder imageHolder){
        //获取图片目录的相对地址
        String dest = PathUtil.getShopImgPath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(imageHolder,dest);
        shop.setShopImg(shopImgAddr);
        System.out.println(shopImgAddr);
    }
}
