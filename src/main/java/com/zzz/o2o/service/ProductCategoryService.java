package com.zzz.o2o.service;


import com.zzz.o2o.dto.ProductCategoryExecution;
import com.zzz.o2o.entity.ProductCategory;
import com.zzz.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList(Long shopId);
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;
    ProductCategoryExecution deletePorductCategory(Long productCategoryId, Long shopId) throws ProductCategoryOperationException;
}
