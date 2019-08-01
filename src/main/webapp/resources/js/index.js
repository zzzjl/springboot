$(function(){
  //定义访问后台，获取头条列表 、 一级类别列表的URL
  var url = '/frontend/listmainpageinfo';
  $.getJSON(url,function (data) {
      if(data.success){
          //获取后台传递过来的头条列表
          var headLineList = data.headLineList;
          //alert(data.shopCategoryList);
          var swiperHtml = '';
          //遍历头条列表，拼接出轮播图组
          headLineList.map(function (item,index) {
              //alert(item.lineImg);
              swiperHtml+=''+'<div class ="swiper-slide img-wrap" align="center" >'
                  +'<a herf ="'+item.lineLink
                  +'"exteral><img class ="banner-img" src="'+item.lineImg
                  +'"alt = "'+item.lineName+'" height="400px" width="1000px" ></a>'+'</div>';
          });
          //将轮播图组赋值传给前端HTML控件
          /*alert(swiperHtml);*/
          $('.swiper-wrapper').html(swiperHtml);
          //设定轮播图转换时间为3S
          $('.swiper-container').swiper({
              autoplay:3000,
              //用户操作时停止轮换
              autoplayDisableOnInteraction:false
          });
          //获取后台的大类列表
          var shopCategoryList = data.shopCategoryList;
          var categoryHtml = '';
          //拼接列表
          shopCategoryList.map(function (item,index) {
              categoryHtml +=''
              +'<div class = "col-40 shop-classify" data-category='
              +item.shopCategoryId+'>'+'<div class = "word">'
              +'<p class = "shop-title">'+item.shopCategoryName
              +'</p>'+'<div class = "shop-desc">'
              +item.shopCategoryDesc+'</div>'+'</div>'
              +'<div class = "shop-classify-img-warp">'
              +'<img class = "shop-img" src = "'+item.shopCategoryImg
              +'" height="60px" width="60px">'+'</div>'+'</div>';
          });
          //将拼接好的类别赋值给前端HTML
          $('.row').html(categoryHtml);
      }
  });
  //点击“我的”显示侧栏
  $('#me').click(function () {
      $.openPanel('#panel-right-demo');
  });
  $('.row').on('click','.shop-classify',function (e) {
      var shopCategoryId = e.currentTarget.dataset.category;
      var newUrl = '/frontend/shopList?parentId='+shopCategoryId;
      window.location.href= newUrl;
  })
})