package com.zzz.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.zzz.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.zzz.o2o.interceptor.shopadmin.ShopPermissionInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.ServletException;

@Configuration
@EnableWebMvc   //等价于<mvc:annotation-driven/>
//WebMvcConfigurerAdapter定义视图解析器
//当实现ApplicationContextAware后这个类方便获得ApplicationContext中所有的bean
public class MvcConfiguraction extends WebMvcConfigurerAdapter implements ApplicationContextAware{
    //Spring容器
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
/*
* 静态资源配置
* */
   @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
       //registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
       registry.addResourceHandler("/pic/**").addResourceLocations("file:I:/zzz/pic/");
   }
   /*
   定义默认的请求处理器
   */
   @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
       configurer.enable();
   }
   /*
   * 创建viewResolver
   * */
   @Bean(name="viewResolver")
    public ViewResolver createViewResolver(){
       InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
       //设置Spring容器
       viewResolver.setApplicationContext(this.applicationContext);
       // 取消缓存
       viewResolver.setCache(false);
       //设置解析前缀
       viewResolver.setPrefix("/WEB-INF/html/");
       //设置解析后缀
       viewResolver.setSuffix(".html");
       return viewResolver;
   }
   //*
    // 文件上传解析器
    // */

    @Bean(name ="multipartResolver")
    public CommonsMultipartResolver createmultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        //1024*1024*20  =20M
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }

/*
* web.xml不生效，需要配置验证码servlet
* */
    @Value("${kaptcha.border}")
    private String border ;
    @Value("${kaptcha.textproducer.font.color}")
    private String fcolor ;
    @Value("${kaptcha.image.width}")
    private String width ;
    @Value("${kaptcha.textproducer.char.string}")
    private String cString ;
    @Value("${kaptcha.textproducer.font.size}")
    private String fsize ;
    @Value("${kaptcha.image.height}")
    private String height ;
    @Value("${kaptcha.noise.color}")
    private String nColor ;
    @Value("${kaptcha.textproducer.char.length}")
    private String clength ;
    @Value("${kaptcha.textproducer.font.name}")
    private String fname ;

    @Bean
    public ServletRegistrationBean servletRegistrationBean()throws ServletException{
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),"/kaptcha");
        servlet.addInitParameter("kaptcha.border",border);//无边框
        servlet.addInitParameter("kaptcha.textproducer.font.color",fcolor);//字体颜色
        servlet.addInitParameter("kaptcha.image.width",width);//图片宽度
        servlet.addInitParameter("kaptcha.textproducer.char.string",cString);//用那些字符生成验证码
        servlet.addInitParameter("kaptcha.image.height",height);//图片高度
        servlet.addInitParameter("kaptcha.textproducer.font.size",fsize);//字体大小
        servlet.addInitParameter("kaptcha.noise.color",nColor);//干扰线颜色
        servlet.addInitParameter("kaptcha.textproducer.char.length",clength);//字符个数
        servlet.addInitParameter("kaptcha.textproducer.font.name",fname);//字体
        return servlet;

    }
    /*
    拦截器
    * */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        String interceptorPath = "/shopadmin/**";
        // 注册拦截器
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
        //配置拦截路径
        loginIR.addPathPatterns(interceptorPath);
        //另一个拦截器
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        //配置拦截路径
        permissionIR.addPathPatterns(interceptorPath);
        //配置不拦截的路径
        permissionIR.excludePathPatterns("/shopadmin/shoplist");
        permissionIR.excludePathPatterns("/shopadmin/getshoplist");
        permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
        permissionIR.excludePathPatterns("/shopadmin/registershop");
        permissionIR.excludePathPatterns("/shopadmin/shopoperation");
        permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
        permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
    }

}
