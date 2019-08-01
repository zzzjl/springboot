$(function(){
    var shopId = getQueryString('shopid');
    var shopInfoUrl = '/shopadmin/getshopmanagementinfo?shopid='+shopId;
        $.getJSON(shopInfoUrl ,function(data){
        if(data.redirct){
            window.location.href = data.url;
        }else{
            if(data.shopId !=undefined && data.shopId != null){
                shopId=data.shopId;
            }
            $('#shopinfo').attr('href','/shopadmin/shopoperation?shopid='+shopId);
        }
    });
});