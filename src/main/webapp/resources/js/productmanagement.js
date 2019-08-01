$(function(){
    //获取此店铺下商品的url
    var listUrl = '/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
    //商品下架Url
    var statusUrl = '/shopadmin/modifyproduct';
    getList();
    function getList() {
        //从后台获取店铺商品的列表
        $.getJSON(listUrl,function (data) {
            if(data.success){
                var productList = data.productList;
                var tempHtml ='';
                //遍历每条商品信息，拼接成一行显示，列信息包括：
                //商品名称   优先级   上下架    编辑按钮
                //预览（含productId）
                productList.map(function (item,index) {
                    var textOp = '下架';
                    var contraryStatus = 0;
                    if(item.enableStatus==0){
                        // 若状态值为0，表明已经是下架商品，操作变为上架
                        textOp = '上架';
                        contraryStatus = 1;
                    }else{
                        contraryStatus = 0;
                    }
                    //拼接每个商品行信息
                    tempHtml +=''+'<div class ="row row-product">'
                       +'<div class ="col-40">'
                       +item.productName
                       +'</div>'
                       +'<div class = "col-40">'
                       +item.priority
                       +'</div>'
                       +'<div class = "col-20">'
                        +'<a href = "#" class ="edit" data-id="'
                        +item.productId
                        +'"data-status="'
                        +item.enableStatus
                        +'">编辑</a>'+'&emsp;'
                        +'<a href="#" class ="status" data-id="'
                        +item.productId
                        +'"data-status="'
                        +contraryStatus
                        +'">'
                        +textOp+'&emsp;'
                        +'</a>'
                        +'<a href="#" class ="preview" data-id = "'
                        +item.productId
                        +'"data-status="'
                        +item.enableStatus
                        +'">预览</a>'+'&emsp;'
                        +'</div>'
                        +'</div>';
                });
                // 将拼接好的信息赋值进Html中
                $('.product-wrap').html(tempHtml);
            }
        });
    }
    //将class为product-wrap里面的a标签绑定点击事件
    $('.product-wrap')
        .on(
            'click',
            'a',
            function (e) {
                var target = $(e.currentTarget);

                if(target.hasClass('edit')) {
                    alert('id:'+e.currentTarget.dataset.id+'status:'+e.currentTarget.dataset.status);
                    //如果有class edit 则点击就进入店铺的编辑页面，并带有productId参数
                    window.location.href = '/shopadmin/productoperation?productId='
                    +e.currentTarget.dataset.id;
                }else if(target.hasClass('status')){
                    //如果有class status则调用后台功能上下架相关商品，并带有productId参数
                    alert('id:'+e.currentTarget.dataset.id+'status:'+e.currentTarget.dataset.status);
                    changeItemStatus(e.currentTarget.dataset.id,
                        e.currentTarget.dataset.status);
                }else if(target.hasClass('preview')){
                    //如果有class preview则取前台展示系统该商品详情预览情况
                    window.location.href='frontend/productdetail?productId='
                    +e.currentTarget.dataset.id;
                }
            });
    function changeItemStatus(id, enableStatus) {
        //定义prouduct json对象并添加productId以及状态（上下架）
        var product ={};
        product.productId = id;
        product.enableStatus = enableStatus;
        $.confirm('ARE YOU SURE?',function () {
            //上下架相关商品
            $.ajax({
                url:statusUrl,
                type:'POST',
                data:{
                    productStr:JSON.stringify(product),
                    statusChange:true,
                },
                dataType:'json',
                success:function (data) {
                    if(data.success){
                        $toast('操作成功！');
                        getList();
                    }else{
                        $.toast('操作失败！');
                    }
                }
            });

        });
    }
})