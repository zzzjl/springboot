/*

* */
$(function(){

    var initUrl = '/shopadmin/getshopinitinfo';
    var registerShopUrl = '/shopadmin/registershop';
    var shopId = getQueryString('shopid');
    var isEdit = shopId?true:false;
    var shopInfoUrl = '/shopadmin/getshopbyid?shopid='+shopId;
    var editShopUrl = '/shopadmin/modifyshop';
    $('#backtom').attr('href','/shopadmin/shopmanagement?shopid='+shopId);
    if(!isEdit){

        getShopInitInfo();

    }else{

        getShopInfo(shopId);

    }
   // getShopInitInfo();
   // alert(initUrl);
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl ,function(data){
            if(data.success){
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);

                var tempAreaHtml = '';
                var shopCategory = '<option data-id="'
                    +shop.shopCategory.shopCategoryId+'"selected>'
                    +shop.shopCategory.shopCategoryName+'</option>'
                data.areaList.map(function (item,index) {
                    tempAreaHtml+='<option data-id="'+item.areaId+'">'
                    +item.areaName+'</option>'
                })
                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disabled','disable');
                $('#area').html(tempAreaHtml);
                $("#area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
            }
        });


    }



    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            if (data.success) {
                var tempHtml = '';
                var tempAreaHtml = '';

                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">'
                        + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);

            }
        });
    }
        $('#submit').click(function(){
            var shop = {};
            if(isEdit){
                shop.shopId=shopId;
            }
            shop.shopName = $('#shop-name').val();
            shop.shopAddr = $('#shop-addr').val();
            shop.phone = $('#shop-phone').val();
            shop.shopDesc = $('#shop-desc').val();
            shop.shopCategory = {
                shopCategoryId : $('#shop-category').find('option').not(function(){
                    return !this.selected;
                }).data('id')
            };
            shop.area = {
                areaId : $('#area').find('option').not(function(){
                    return !this.selected;
                }).data('id')
            };
            var shopImg = $('#shop-img')[0].files[0];
            var formData = new FormData();
            formData.append('shopImg',shopImg);
            formData.append('shopStr',JSON.stringify(shop));
            var verifyCodeActual = $('#j_captcha').val();
            if(!verifyCodeActual){
                $.toast('请输入验证码！');
                return ;
            }
            formData.append('verifyCodeActual',verifyCodeActual);
            $.ajax({
                url:(isEdit? editShopUrl:registerShopUrl),
                type:'POST',
                data:formData,
                contentType :false,
                processData : false,
                cache : false,
                success:function(data){
                    if(data.success){
                        $.toast('提交成功！');
                        setInterval("window.location.href = '/shopadmin/shoplist';",2000);
                        //window.location.href = '/shopadmin/shoplist';
                    }else{
                        $.toast('提交失败！'+ data.errMsg);
                    }
                    $('#captcha_img').click();
                }

                }

            )

        });

})