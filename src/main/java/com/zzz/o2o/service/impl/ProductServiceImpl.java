package com.zzz.o2o.service.impl;

import com.zzz.o2o.dao.ProductDao;
import com.zzz.o2o.dao.ProductImgDao;
import com.zzz.o2o.dto.ImageHolder;
import com.zzz.o2o.dto.ProductExecution;
import com.zzz.o2o.entity.Product;
import com.zzz.o2o.entity.ProductImg;
import com.zzz.o2o.enums.ProductStateEnum;
import com.zzz.o2o.exceptions.ProductCategoryOperationException;
import com.zzz.o2o.exceptions.ProductOperationException;
import com.zzz.o2o.service.ProductService;
import com.zzz.o2o.util.ImageUtil;
import com.zzz.o2o.util.PageCalculator;
import com.zzz.o2o.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    @Override
    @Transactional
    //1.处理缩略图，获取缩略图相对路径并赋值给product
    //2.往tb_product写入商品信息，获取productId
    //3，结合productId批量处理商品详情图
    //4.将商品详情图列表批量插入tb_product_img中

    //        添加商品                             商品          缩略图                   详情图
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            //给商品设置上默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //默认为上架状态
            product.setEnableStatus(1);
            //若商品缩略图不为空，则添加
            if (thumbnail != null) {
                addThumbnail(product, thumbnail);
            }
            try {
                //创建商品信息
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建商品失败！");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败：" + e.toString());
            }
            //若商品详情图不为空则添加
            if (productImgList != null && productImgList.size() > 0) {
                addProductImgList(product, productImgList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            //传参为空则返回空值错误信息
            return new ProductExecution(ProductStateEnum.NO_PRODUCT);
        }

    }

    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
        //获取图片路径，这里直接存放在相应店铺文件夹下
        String dest = PathUtil.getShopImgPath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        //遍历图片一次处理，并添加进productImg实体类里
        for (ImageHolder productImgHolder : productImgHolderList) {
            String imgAddr = ImageUtil.generateThumbnail(productImgHolder, dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
            // 如果确实有图片有添加，就执行批量操作
            if (productImgList.size() > 0) {
                try {
                    int effectNum = productImgDao.batchInsertProductImg(productImgList);
                    if (effectNum <= 0) {
                        throw new ProductOperationException("创建商品详情图失败！");
                    }
                } catch (Exception e) {
                    throw new ProductOperationException("创建商品详情图失败" + e.toString());
                }
            }

    }

    public void deleteProductImgList(long productId) {
        //根据proudctId获取来图片
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        //干掉原来的图片
        for (ProductImg productImg : productImgList) {
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
            logger.info("图片或路径已删除"+productImg.getImgAddr());
        }
        //删除数据库里原有的图片信息
        productImgDao.deleteProductImgById(productId);
    }

    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImgPath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);

    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    @Override
    /*修改商品
    1.若缩略图有值，则处理缩略图，若原先存在缩略图则先删除再添加新图，之后获取缩略图并赋值给product
    2.若详情图有值，则作相同操作
    3.将tb_podudct_img下面的该商品原先商品详情图全部清除
    4.更新tb_productx信息
    * */
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException {
        //空值判断
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            //给商品设置默认属性
            product.setLastEditTime(new Date());
            //若商品缩略图不为空且原有缩略图不为空，则删除原有缩略并添加
            if (thumbnail != null) {
                //先获取一边原有信息，源信息里有原图片地址
                Product tempProduct = productDao.queryProductById(product.getProductId());
                if(tempProduct==null){
                    throw new ProductCategoryOperationException("无此商品！");
                }
                if (tempProduct.getImgAddr() != null) {
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                    logger.info("图片或路径已删除"+tempProduct.getImgAddr());

                }
                addThumbnail(product, thumbnail);
            }
            //如果有新的详情图，则将原先的删除，再添加新的图片
            if (productImgHolderList != null && productImgHolderList.size() > 0) {
                deleteProductImgList(product.getProductId());
                addProductImgList(product, productImgHolderList);
            }
            try {
                int effNum = productDao.updateProduct(product);
                if (effNum <= 0) {
                    throw new ProductCategoryOperationException("更新商品信息失败！");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new ProductOperationException("更新商品信息失败！" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.NO_PRODUCT);


        }
    }

    /*
    * 分页查询商品列表 条件商品名称（模糊）、商品状态、店铺Id、商品类别    * */
    public ProductExecution getProductList(Product product,int pageIndex,int pageSize){
        //页码转换成数据库的行码，并调用dao层取回制定页码的商品列表
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex,pageSize);
        List<Product> productList = productDao.queryProductList(product,rowIndex,pageSize);
        //基于同样查询条件下返回商品总数
        int count = productDao.queryProductCount(product);
        ProductExecution pe = new ProductExecution();
        pe.setCount(count);
        pe.setProductList(productList);
        return pe ;
    }
}