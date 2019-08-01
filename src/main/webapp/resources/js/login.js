$(function () {

    //登录验证的controller url
    var loginUrl = '/local/logincheck';
    //从地址栏获取userType
    //userType=1为customer   其余为shopOwner
    var userType = getQueryString('userType');
    //登录次数，登录三次后要求验证码
    var logincount = 0;
    $('#submit').click(function(){
        //获取输入账号
        var userName = $('#username').val();
        //获取输入密码
        var password = $('#pwd').val();
        //获取验证信息
        var verifyCodeActual = $('#j_captcha').val();
        //是否需要验证  默认为false 不需要
        var needVerify = false;
        if(logincount >=3){
            if(!verifyCodeActual){
                $.toast('请输入验证码！');
                return;
            }else{
                needVerify = true;
            }
        }
        $.ajax({
            url:loginUrl,
            async:false,
            cache:false,
            type:"post",
            datatype:'json',
            data:{
                userName:userName,
                password:password,
                verifyCodeActual:verifyCodeActual,
                needVerify:needVerify
            },
            success:function (data) {
                if(data.success){
                    $.toast('登录成功！');
                    if(userType ==1){
                        //若用户在前端展示系统页面，则自动连接到前端展示首页
                        window.location.href='/frontend/index';
                    }else{
                        //若用户在店家管理页面则自动连接到店铺列表页
                        window.location.href='/shopadmin/shoplist';
                    }
                }else{
                    $.toast('登录失败！'+data.errMsg);
                    logincount++;
                    if(logincount>=3){
                        //登录三次，需要验证码
                        $('#verifyPart').show();
                    }
                }
            }

        })
    });

});