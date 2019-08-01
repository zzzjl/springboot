$(function(){
  var loading = false;
  //分页允许的最大条数
  var maxItems = 999;
  //一页返回最大条数
    var pageSize = 3;
    //获取店铺列表URL
    var listUrl = '/frontend/listshops';
    //获取店铺类别列表以及区域列表的URL
    var searchDivUrl = '/frontend/listshopspageinfo';
    //页码
    var pageNum = 1;
    //从地址栏URL里尝试获取parent  shop  category id
    var parentId = getQueryString('parentId');
    var areaId = '';
    var shopCategoryId = '';
    var shopName = '';
    //渲染出店铺类别列表以及区域列表以供搜索
    getSearchDivData();
    //预先加载10条店铺信息
    addItems(pageSize,pageNum);
    /*
    * 获取店铺类别列表以及区域列表信息
    *
    * */
    function getSearchDivData() {
        //如果传入parentId，则取出此一级类别下面的所有二级类别
        var url = searchDivUrl+'?'+'parentId='+parentId;
        $.getJSON(url,function (data) {
            if(data.success){
                //获取后台返回的店铺类别列表
                var shopCategoryList = data.shopCategoryList;
                var html = '';
                html +='<a href ="#" class ="button" data-category-id ="">全部类别</a>';
                //遍历店铺类别列表，拼接出a标签集
                shopCategoryList.map(function (item,index) {
                    html+='<a href = "#" class = "button " data-category-id='
                    +item.shopCategoryId
                    +'>'
                    +item.shopCategoryName
                    +'</a>'+'</div>'
                });
                //将拼接好的类别标签嵌入前台HTML
                $('#shoplist-search-div').html(html);
                var selectOptions = '<option  value="">全部街道</option>';
                //获取后台返回的区域信息列表
                var areaList = data.areaList;
                //遍历区域列表，拼接option标签
                areaList.map(function (item,index) {
                    selectOptions+='<option  value ="'
                    +item.areaId+'">'
                    +item.areaName+'</option>';
                });
                //将标签集添加进area列表里
                $('#area-search').html(selectOptions);
            }
        });
    }
    /*
    *
    * 获取分页展示的店铺列表信息
    *
    * */
    function addItems(pageSize,pageIndex) {
        //拼接出查询的URL，赋空值默认去掉这个条件限制，有值代表这个条件查询
        var url = listUrl+'?'+'pageIndex='+pageIndex+'&pageSize='+pageSize+'&parentId='+parentId+'&areaId='+areaId
            +'&shopCategoryId='+shopCategoryId+'&shopName='+shopName;
        //设定加载符，若还在取数据则不能再次访问后台，避免多次加载
        loading = true;
        //访问后台获取相应条件下的店铺列表
        $.getJSON(url,function (data) {
            if(data.success){
                //获取当前条件下的店铺总数
                maxItems = data.count;
                var html='';
                //遍历店铺列表，拼接出卡片集合
                data.shopList.map(function (item,index) {
                    html+=''+'<div class ="card" data-shop-id ="'
                    +item.shopId+'">'+'<div class ="card-header">'
                    +item.shopName+'</div>'
                    +'<div class = "card-content">'
                    +'<div class = "list-block- media-list">'+'<ul>'
                    +'<li class ="item-content">'
                    +'<div class="item-media">'+'<img src="'
                    +item.shopImg+'" width="44">'+'</div>'
                    +'<div class="item-inner">'
                    +'<div class="item-subtitle">'+item.shopDesc
                    +'</div>'+'</div>'+'</li>'+'</ul>'
                    +'</div>'+'</div>'+'<div class ="card-footer">'
                    +'<p class="color-gray">'
                        +item.lastEditTime
                        +'更新</p> '+'<span>点击查看</span>'+'</div>'
                    +'</div>';
                });
                //将卡片添加进HTML里
                $('.list-div').append(html);
                //获取目前为止以显示的卡片总数，包含之前已经加载的
                var total = $('.list-div.card').length;
                //若总数超过MAXITEMS，则停止加载
                if(total>=maxItems){
                    //加载完毕，注销无限加载事件，以防不必要的加载
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    $('.infinite-scroll-preloader').remove();
                }
                //否则页码加一，继续load出新店铺
                pageNum+=1;
                //加载结束，可以再次加载
                loading=false;
                //刷新页面，显示新的店铺
                $.refreshScroller();
            }
        });
    }
    //下滑屏幕自动进行分页搜索
    $(document).on('infinite','.infinite-scroll-bottom',function () {
        if(loading)
          return;
        addItems(pageSize,pageNum);
    });
    //点击店铺的卡片进入该店铺的详情页
    $('.shop-list').on('click','.card',function (e) {
       var shopId = e.currentTarget.dataset.shopId;
       window.location.href = '/frontend/shopdetail?shopId='+shopId;
    });
    // 选择新的店铺类别之后，重置页码，清空原先的店铺列表，按照新的类别去查询
    $('#shoplist-search-div').on('click','.button',function (e) {
       if(parentId){//如果传过来的是一个父类下的子类
           shopCategoryId = e.target.dataset.categoryId;
           if($(e.target).hasClass('button-fill')){
               $(e.target).removeClass('button-fill');
               shopCategoryId ='';
           }else{
               $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
           }
           //清空店铺列表查询
           $('.list-div').empty();
           //重置页码
           pageNum = 1;
           addItems(pageSize,pageNum);
       }else{//如果传递过来的父类为空，则按照父类查询
           parentId = e.target.dataset.categoryId;
           if($(e.target).hasClass('button-fill')){
               $(e.target).removeClass('button-fill');
               parentId = '';
           }else{
            $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
           }
           //清空店铺列表查询
           $('.list-div').empty();
           //重置页码
           pageNum = 1;
           addItems(pageSize,pageNum);
           parentId = '';
       }
    });
    //需要查询的店铺名字变化后 ，重置页码，清空原先的店铺列表，按照新的名字查询
    $('#search').on('input',function (e) {
        shopName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize,pageNum);
    });
    //区域信息变化后，重置页码，清空原先的店铺列表，按照新的区域名字查询
    $('#area-search').on('change',function (e) {
        areaId = $('#area-search').val();
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
});