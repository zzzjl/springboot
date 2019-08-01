package com.zzz.o2o.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.security.PrivateKey;

@Configuration
@EnableTransactionManagement
/*
* 首先使用注解@EnableTransactionManagemen注解开启事务后，再在service方法使用@service即可
* */
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {
    @Autowired
    //注入DataSourceConfiguraction里面的datasource，通过createDataSource获取
    private DataSource dataSource;
    @Override
    /*
    * 关于事务管理，需要返回PlatformTransactionManagment实现
    *
    * */
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
