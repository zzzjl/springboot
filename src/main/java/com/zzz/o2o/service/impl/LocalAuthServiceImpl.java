package com.zzz.o2o.service.impl;

import com.zzz.o2o.dao.LocalAuthDao;
import com.zzz.o2o.dto.LocalAuthExcution;
import com.zzz.o2o.entity.LocalAuth;
import com.zzz.o2o.enums.LocalAuthStateEnum;
import com.zzz.o2o.exceptions.LocalAuthOperationException;
import com.zzz.o2o.service.LocalAuthService;
import com.zzz.o2o.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    @Autowired
    private LocalAuthDao localAuthDao;
    /*通过账号和密码获取平台信息
   * */
    @Override
     public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password){
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username,MD5.getEncoderByMd5(password) );

        return localAuth;
    }

    /*通过userId获取平台账号信息
    * */
    @Override
    public LocalAuth getLocalAuthByUserId(long userId){
        return localAuthDao.queryLocalByUserId(userId);
    }
    /*
    *修改平台账号登录密码
    * */
    @Override
    @Transactional
    public LocalAuthExcution modifyLocalAuth(Long userId , String username, String password, String newPassWord )throws LocalAuthOperationException{
        //空值判断，传入的LocalAuth账号密码，用户信息特别时UserId不能为空，否则直接返回错误
        if(userId !=null&&username!=null&&password !=null &&newPassWord!=null&&!password.equals(newPassWord)){
            try{
                //更新密码，并对新密码进行MD5加密
                int effectNum = localAuthDao.updateLocalAuth(userId,username,MD5.getEncoderByMd5(password),MD5.getEncoderByMd5(newPassWord),new Date());
                //判断是否成功
                if(effectNum<=0){
                    throw new LocalAuthOperationException("更新密码失败！");
                }
                return new LocalAuthExcution(LocalAuthStateEnum.SUCCESS);
            }catch(Exception e){
                throw new LocalAuthOperationException("更新密码失败！"+e.getMessage());
            }
        }else{
            return new LocalAuthExcution(LocalAuthStateEnum.NULL_USERID);
        }

    }

    @Transactional
    public LocalAuthExcution bindLocalAuth(LocalAuth localAuth)throws LocalAuthOperationException{
        //
        return new LocalAuthExcution(LocalAuthStateEnum.INNER_ERROR);
    }
}
