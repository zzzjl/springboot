package com.zzz.o2o.service;


import com.zzz.o2o.dto.ImageHolder;
import com.zzz.o2o.dto.ShopExecution;
import com.zzz.o2o.entity.Shop;
import com.zzz.o2o.exceptions.ShopOperationExceptions;

public interface ShopService {
   /*添加商铺*/
   ShopExecution addShop(Shop shop, ImageHolder imageHolder);
   /*通过Id获取商铺信息*/
   Shop getShopById(long shopId);
   /*更新店铺信息*/
   ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) throws ShopOperationExceptions;
   /*分页查询*/
   public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

}
