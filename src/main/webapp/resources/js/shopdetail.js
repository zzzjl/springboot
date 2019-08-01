$(function(){
    var loading = false;
    //分页允许返回的最大条数，
    var maxItem = 20;
    //列出商品列表的URL
    var pageSize = 10;
    var listUrl = '/frontend/listproductsbyshop';
    //默认页码
    var pageNum = 1;
    //从地址栏获取ShopId
    var shopId = getQueryString('shopId');
    var productCategoryId = '';
    var productName = '';
    //获取本店信息，以及商品类别信息列表URL
    var searchDivUrl = '/frontend/listshopdetailinfo?shopId='+shopId;
    //渲染出店铺基本信息以及商品类别列表以供搜索
    getSearchDivData();
    //预先加载10条商品信息
    addItems(pageSize,pageNum);
    //给兑换礼品的a标签赋值兑换礼品的URL
    $('#exchangelist').attr('herf','/frontend/listshopdetailpageinfo?shopId='+shopId);
    //获取本店店铺信息以及商品类别信息列表
    function getSearchDivData() {
        var url = searchDivUrl;
        $.getJSON(url,function (data) {
            if(data.success){
                var shop = data.shop;
                $('#shop-cover-pic').attr('src',shop.shopImg);
                $('#shop-update-time').html(new Date(shop.lastEditTime).Format('yyyy-MM-dd'));   /*.Format('yyyy-MM-dd')*/
                $('#shop-name').html(shop.shopName);
                $('#shop-desc').html(shop.shopDesc);
                $('#shop-addr').html(shop.shopAddr);
                $('#shop-phone').html(shop.phone);
                //从后台返回该店铺的商品列表
                var productCategoryList = data.productCategoryList;
                var html = '';
                //遍历商品列表，生成相应商品a标签
                productCategoryList.map(function (item,index) {
                    html+='<a herf="#" class ="button" data-product-search-id='
                    +item.productCategoryId
                    + '>'
                    +item.productCategoryName
                    +'</a>';
                });
                //将商品类别a标签绑定到相应的html组建中
                $('#shopdetail-button-div').html(html);
            }
        });
    }
    /*\
    * 获取分页展示商品列表信息
    *
    * */
    function addItems(pageSize,pageIndex) {
        //拼接出查询的URL，赋空值默认就去掉这个条件限制，有值就代表按这个条件查询
        var url = listUrl+'?'+'pageIndex='+pageIndex+'&pageSize='+pageSize
        +'&productCategoryId='+productCategoryId+'&productName='+productName
        +'&shopId='+shopId;
        //设定加载符,若为true则不让加载
        loading = true;
        $.getJSON(url,function (data) {
            if(data.success){
                //获取当前条件下的总数
                maxItem = data.count;
                var html = '';
                //遍历列表，拼出卡片集合
                data.productList.map(function (item,index) {
                    html+=''+'<div class="card" data-product-id='
                    +item.productId+'>'
                    +'<div class="card-header">'+item.productName
                    +'</div>'+'<div class ="card-content">'
                    +'<div class="list-block media-list">' +'<ul>'
                    +'<li class="item-content">'
                    +'<div class="item-media">'+'<img src="'
                    +item.imgAddr+'" width ="44">'+'</div>'
                    +'<div class="item-inner">'
                    +'<div class="item-subtitle">'+item.productDesc
                    +'</div>'+'</div>'+'</li>'+'</ul>'
                    +'</div>'+ '</div>'
                    +'<div class="card-footer">'
                    +'<p class="color-gray">'
                    +new Date(item.lastEditTime).Format("yyyy-MM-dd")
                    +'更新</p>'+'<span>点击查看</span>'+
                    '</div>' +'</div>';


                });
                //将卡片集合添加到目标HTML
                $('.list-div').append(html);
                //获取目前为止以显示的卡片数，包含之前已经加载的
                var total = $('.list-div.card').length;
                //若总数达到与按照此查询条件列出来的一致，则停止后台查询
                if(total>=maxItem){

                    //删除加载提示符
                    $('.infinite-scroll-preloader').hide();
                }else{
                    $('.infinite-scroll-preloader').show();
                }
                //否则页码加一，继续load新的店铺
                pageNum +=1;
                //加载结束，可以继续加载
                loading = false;
                //刷新页面，显示新加载的店铺
                $.refreshScroller();
            }
        });
    }

    //下滑自动分页搜索
    $(document).on('infinite','infinite-scroll-bottom',function () {
        if(loading){
            return;
        }
        addItems(pageSize,pageNum);
    });


    //选择新的商品类别后，重置页码，清空原先的商品列表，按照新的类别去查询
    $('#shopdetail-button-div').on('click','.button',function (e) {
        //获取商品类别Id
        productCategoryId = e.target.dataset.productSearchId;
        //alert(productCategoryId);
        if(productCategoryId){
            //若之前已选定别的CateforyId，则移除其选定效果，改成新的
            if($(e.target).hasClass('button-fill')){
                $(e.target).removeClass('button-fill');
                productCategoryId='';
            }else{
                $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
            }
            $('.list-div').empty();
             pageNum = 1;
            addItems(pageSize,pageNum);
        }
    });
    //点击查看进入商品详情页
    $('.list-div').on('click','.card',function (e) {
        var productId = e.currentTarget.dataset.productId;
        window.location.href = '/frontend/productdetail?productId='+productId;
    });
    //需要查询的商品名字发生变化后，重置页码，清空原先的商品列表，按照新的名字查询
    $('#search').on('input',function (e) {
        productName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize,pageNum);
    });
    //点击后打开右侧栏
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });
    //初始化页面$
    $.init();

})