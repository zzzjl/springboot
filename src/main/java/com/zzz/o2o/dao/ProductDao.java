package com.zzz.o2o.dao;

import com.zzz.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    /*添加商品
    * */
    int insertProduct(Product product);

    /*根据id获取商品信息
    * */
    Product queryProductById(long productId);
    /*更新商品信息
    * */
    int updateProduct(Product product);
    /*
    * 查询商品列表*/
    List<Product> queryProductList(@Param("product") Product product, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    int queryProductCount(@Param("product") Product product);
    /*当productCategory被删除时，应更新productCategory为null
     * */
    int updateProductCategoryToNull(long productCategoryId);

}
