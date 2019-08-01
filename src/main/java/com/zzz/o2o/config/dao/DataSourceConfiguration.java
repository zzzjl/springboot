package com.zzz.o2o.config.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zzz.o2o.util.DESUtil;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.zzz.o2o.dao")
public class DataSourceConfiguration {
    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUserName;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    /* 生成DataSource   */
    @Bean(name="dataSource" )
    public ComboPooledDataSource createDataSource(){
        //生成dataSource实例
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        //生成之前配置文件的信息
        //驱动
        try {
            dataSource.setDriverClass(jdbcDriver);
        }catch (Exception e){
            e.printStackTrace();
        }
        //数据库连接URL
        dataSource.setJdbcUrl(jdbcUrl);
        //设置USER
        dataSource.setUser(DESUtil.getDecryptString(jdbcUserName));
        // 设置密码
        dataSource.setPassword(DESUtil.getDecryptString(jdbcPassword));
        //配置C3P0私有属性
        dataSource.setMaxPoolSize(30);
        dataSource.setMinPoolSize(10);
        //连接关闭后不自动commit
        dataSource.setAutoCommitOnClose(false);
        //设置超时时间
        dataSource.setCheckoutTimeout(10000);
        //连接失败重试次数
        dataSource.setAcquireRetryAttempts(2);
        return dataSource;

    }
}
