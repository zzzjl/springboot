package com.zzz.o2o.service;


import com.zzz.o2o.dto.LocalAuthExcution;
import com.zzz.o2o.entity.LocalAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class LocalAuthServiceTest  {
    @Autowired
    private LocalAuthService localAuthService;
@Test
    public void TestgetLocalAuthByUsernameAndPwd() {
        String username = "zzzjl";
        String password = "123";
        LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, password);
        System.out.println("localAuth: "+localAuth.getLocalAuthId()+ localAuth.getUsername());
    }
    @Test
    public void TestmodifyLocalAuth(){
      Long userId =9527L;
        String username = "zzzjl";
        String password = "123";
        String newPassword ="1";
        LocalAuthExcution localAuthExcution =localAuthService.modifyLocalAuth(userId,username,password,newPassword);
        System.out.println(localAuthExcution.getStateInfo());

    }



}
