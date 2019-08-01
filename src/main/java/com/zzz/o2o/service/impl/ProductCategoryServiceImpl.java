package com.zzz.o2o.service.impl;

import com.zzz.o2o.dao.ProductCategoryDao;
import com.zzz.o2o.dto.ProductCategoryExecution;
import com.zzz.o2o.entity.ProductCategory;
import com.zzz.o2o.enums.ProductCategoryStateEnum;
import com.zzz.o2o.exceptions.ProductCategoryOperationException;
import com.zzz.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    ProductCategoryDao productCategoryDao;
    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId){
        return productCategoryDao.queryProductCategoryList(shopId);
    }
    @Override
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException{
        if(productCategoryList!=null&&productCategoryList.size()>0){
            try{
                int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if(effectNum<=0){
                    throw new ProductCategoryOperationException("店铺类别创建失败");
                }else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            }catch (Exception e){
                throw new ProductCategoryOperationException("batchAddProductCategory error"+e.getMessage());
            }
        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }
    @Override
    @Transactional
    public ProductCategoryExecution deletePorductCategory(Long productCategoryId,Long shopId)throws ProductCategoryOperationException{
        try{
            int effectNum =productCategoryDao.deleteProductCategory(productCategoryId,shopId);
            if(effectNum<=0){
                throw new ProductCategoryOperationException("商品类别删除失败！");
            }else{
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch(Exception e){
            throw new ProductCategoryOperationException("deleteProductCategoryError:"+e.getMessage());
        }
    }

}
