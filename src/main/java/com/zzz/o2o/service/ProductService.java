package com.zzz.o2o.service;

import com.zzz.o2o.dto.ImageHolder;
import com.zzz.o2o.dto.ProductExecution;
import com.zzz.o2o.entity.Product;
import com.zzz.o2o.exceptions.ProductOperationException;

import java.util.List;

public interface ProductService {
    /*添加商品信息及图片处理
    * */
    ProductExecution addProduct(Product product, ImageHolder
            thumbnailName, List<ImageHolder> productImgList) throws ProductOperationException;
    /*通过Id查询商品信息
    * */
    Product getProductById(long productId);
    /*
    * 修改商品信息以及图片处理
    */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException;

    /*查询商品列表（分页）
    * */
    ProductExecution getProductList(Product product, int rowIndex, int pageSize);


}
