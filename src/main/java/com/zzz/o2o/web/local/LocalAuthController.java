package com.zzz.o2o.web.local;

import com.zzz.o2o.dto.LocalAuthExcution;
import com.zzz.o2o.entity.LocalAuth;
import com.zzz.o2o.entity.PersonInfo;
import com.zzz.o2o.enums.LocalAuthStateEnum;
import com.zzz.o2o.exceptions.LocalAuthOperationException;
import com.zzz.o2o.service.LocalAuthService;
import com.zzz.o2o.util.CodeUtil;
import com.zzz.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping( value ="local" ,method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {
    @Autowired
    private LocalAuthService localAuthService;
    @RequestMapping(value ="/changelocalpwd" ,method = RequestMethod.POST)
    @ResponseBody
    private Map<String ,Object> changeLocalPwd(HttpServletRequest request){
        Map<String ,Object>modelMap = new HashMap<String ,Object>();
        //验证吗校验
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码！");
        }
        //获取账号
        String userName = HttpServletRequestUtil.getString(request,"userName");
        //获取原密码
        String password = HttpServletRequestUtil.getString(request,"password");
        //获取新密码
        String newPassword = HttpServletRequestUtil.getString(request,"newPassword");
        //从session中获取当前用户信息，（一旦通过微信登录就能获取到用户信息）
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //非空判断，要求账号新旧密码以及当前用户session非空，且新旧密码不相同
        if(userName!=null&&password!=null&&newPassword!=null&&!password.equals(newPassword)&&user !=null){
            try{
                //查看原先账号，看看与输入账号是否一致，不一致认为非法
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                if(localAuth==null||!localAuth.getUsername().equals(userName)){
                    //不一致则推出
                    modelMap.put("success",false);
                    modelMap.put("errMsg","输入账号非本次登录账号");
                }
                //修改平台账号密码
                LocalAuthExcution le = localAuthService.modifyLocalAuth(user.getUserId(),userName,password,newPassword);
                if(le.getState()== LocalAuthStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }
                else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",le.getStateInfo());
                }
            }catch (LocalAuthOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap ;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","修改密码失败，新密码为空");
        }
        return modelMap ;
    }
    @RequestMapping(value ="/logincheck",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object>loginCheck(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        //获取是否需要校验验证码的标识符
        boolean needVerify = HttpServletRequestUtil.getBoolean(request,"needVerify");
        if(needVerify&&!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return  modelMap;
        }
        //获取输入账号

        String userName = HttpServletRequestUtil.getString(request,"userName");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request,"password");
        //非空校验
        if(userName!=null&&password!=null){
            LocalAuth localAuth =  localAuthService.getLocalAuthByUsernameAndPwd(userName,password);
            if(localAuth !=null){
                modelMap.put("success",true);
                //同时设置session
                PersonInfo user =localAuth.getPersonInfo();
                request.getSession().setAttribute("user",user);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg","用户名或密码错误！");
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名密码均不能为空！");
        }
        return modelMap ;
    }
    @RequestMapping(value ="/loginout",method = RequestMethod.POST)
    @ResponseBody
    private Map<String ,Object>loginOut(HttpServletRequest request){
        Map<String,Object>modelMap = new HashMap<String,Object>();
        //将用户SESSION置为空
        request.getSession().setAttribute("user",null);
        modelMap.put("success",true);
        return modelMap ;
    }
}
