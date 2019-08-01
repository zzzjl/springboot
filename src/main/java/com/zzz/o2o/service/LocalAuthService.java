package com.zzz.o2o.service;

import com.zzz.o2o.dto.LocalAuthExcution;
import com.zzz.o2o.entity.LocalAuth;
import com.zzz.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {
    /*通过账号和密码获取平台信息
    * */
    LocalAuth getLocalAuthByUsernameAndPwd(String username, String password);

    /*通过userId获取平台账号信息
    * */
    LocalAuth getLocalAuthByUserId(long userId);
    /*
    *修改平台账号登录密码
    * */
    LocalAuthExcution modifyLocalAuth(Long userId, String username, String password, String newPassWord)throws LocalAuthOperationException;
}
