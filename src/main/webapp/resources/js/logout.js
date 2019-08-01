$(function(){
    $('#log-out').click(function () {
        //清除SESSION
        $.ajax({
            url:"/local/logout",
            type:"post",
            async:false,
            cache:false,
            dataType:'json',
            success:function (data) {
                if(data.success){
                    var userType = $("#logout").attr("userType");
                    //清除成功后退出登录页面
                    window.location.href = "/local/login?userType="+userType;
                    return false;
                }
            },
            error:function (data,error) {
                alert(error);
            }
        });
    });
});