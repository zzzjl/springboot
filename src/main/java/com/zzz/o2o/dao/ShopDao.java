package com.zzz.o2o.dao;

import com.zzz.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {
    /*查询总数*/
    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    /*
    * 新增店铺
    * */
    int insertShop(Shop shop);
    /*根据id查询店铺
    * */
    Shop  quaryShopById(long shopId);
    /*更新店铺*/
    int updateShop(Shop shop);
    /*分页查询店铺  可输入条件：店铺名称、店铺状态、店铺类别、区域ID、owener
    shopCondition查询条件
    rowIndex从第几条开始取数据
    pageSize返回的条数

    * */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
}
