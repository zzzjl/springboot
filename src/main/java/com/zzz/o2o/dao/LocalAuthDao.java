package com.zzz.o2o.dao;

import com.zzz.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LocalAuthDao {

    /*通过账号和密码查询对应信息，登录用*/
    LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username, @Param("password") String password);

    /*
    * 通过用户Id查询对应的localAuth*/
    LocalAuth queryLocalByUserId(@Param("userId") long userId);

    /*添加平台账号*/
    int insertLocalAuth(@Param("localAuth") LocalAuth localAuth);

    /*通过UserId，UserName 更改密码*/
    int updateLocalAuth(@Param("userId") long userId, @Param("username") String userName, @Param("password") String password, @Param("newPassword") String newPassword, @Param("lastEditTime") Date lastEditTime);

    /**/


}
