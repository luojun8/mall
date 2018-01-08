require.config({
	paths: {
		'jquery': 'jquery-1.8.0'
	}
});
require(['jquery', 'common'], function($,common) {
	//轮播图初始化
	var $imgBtnLi = $('.imgBtn li');
	var $sliderAImg = $('#slider a img');
	$sliderAImg.eq(0).css({
		opacity: 1
	});
	$imgBtnLi.eq(0).addClass('switch');
	var iPrev = 0;
	var iNow = 0;
	//手动轮播
	$imgBtnLi.on('click', function() {
		clearInterval(timer);
		iNow = $(this).index();
		slider(autoSlider);
	});
	//自动轮播
	var timer = autoSlider();

	function slider(autoSlider) {
		if (iNow != iPrev) {
			
			$imgBtnLi.eq(iNow).addClass('switch');
			$imgBtnLi.eq(iPrev).removeClass('switch');
			$sliderAImg.eq(iNow).stop().animate({
				opacity: 1,
			}, 1000, function() {
				autoSlider ? (timer = autoSlider()) : '';
			});
			$sliderAImg.eq(iPrev).stop().animate({
				opacity: 0
			}, 500);
			iPrev = iNow;
		}

	}

	function autoSlider() {
		return setInterval(function() {
			if (iNow++ >= 2) {
				iNow = 0;
			}
			slider();
		}, 5000);
	}
	//ajax获取后台数据
	function menu(){
		var page = $('.pagerCur').text();
		var hidden2 = $('.arrow.dsc').css("display");
		var asc = true;
		if(hidden2!="none"){
			asc = false;
		}
		var name = $('.searchText').val();
		$.ajax({
			url : common("getGoods"),
			type : 'post',
			data : {
				"page":page,
				"order":asc,
				"name":name
			},
			dataType : 'json',
			success : function(data) {
				if(data.success){
					getMenu(data);
				}else{
					alert(data.errMsg);
				}
			},
			error : function(data) {
				alert("请求错误");
			}
		});
	}


//json为自定义假数据，正式用时时ajax中的data
//var json = {"goods":[{"id":2,"name":"黄瓜","price":15,"detail":"富含维生素","url":"../image/goods/fruits_1.jpg"},{"id":3,"name":"香菜","price":30,"detail":"美容养颜","url":"../image/goods/fruits_1.jpg"},{"id":10,"name":"小头菜","price":16,"detail":"健康无毒药","url":"../image/goods/fruits_1.jpg"},{"id":11,"name":"番茄","price":12,"detail":"很好的营养","url":"../image/goods/fruits_1.jpg"},{"id":2,"name":"黄瓜","price":15,"detail":"富含维生素","url":"../image/goods/fruits_1.jpg"},{"id":2,"name":"黄瓜","price":15,"detail":"富含维生素","url":"../image/goods/fruits_1.jpg"},{"id":2,"name":"黄瓜","price":15,"detail":"富含维生素","url":"../image/goods/fruits_1.jpg"},{"id":2,"name":"黄瓜","price":15,"detail":"富含维生素","url":"../image/goods/fruits_1.jpg"},{"id":2,"name":"黄瓜","price":15,"detail":"富含维生素","url":"../image/goods/fruits_1.jpg"},{"id":2,"name":"黄瓜","price":15,"detail":"富含维生素","url":"../image/goods/fruits_1.jpg"}],"total":100,"page":2,"num":10};
 	//拼接字符串后添加到页面内

 	function setPage(obj){
 		$('.totalNum b').text(obj.total);
 		$('.pagerCur').text(obj.page);
 		$('.pagerTotal').text(obj.num);
 	}

 	function setCarts(){
 		var carts = window.sessionStorage.getItem('wh9528');
 		var num = 0;
 		if(carts){
 			carts = JSON.parse(carts);
	 		for(var n in carts){
	 			num +=parseFloat(carts[n].number);
	 		}
 		}
 		$('.cartNumber').text(num);
 	}

	function getMenu(obj){
		setPage(obj);
		var choice = $('<div></div>');
		choice.attr("id","searchResultsList");
		var ul = $('<ul></ul>');
		//商品具体内容拼接插入页面
		$.each(obj.data, function(num, el) {
			var a = $('<a></a>');
			//a.attr("href","detail.html?id="+obj.goods[num].id);
			a.attr("dataID",obj.data[num].id);
			var li = $('<li></li>');
			li.addClass("fl");

			var imageDiv = $('<div></div>');
			imageDiv.addClass("wrapImgs");
			var image = $('<img></img>');
			image.attr("onclick","window.top.location.href='detail.html?id="+obj.data[num].id+"'");
			image.attr("src",obj.data[num].images);
			imageDiv.append(image);
			li.append(imageDiv);

			var p1 = $('<p></p>');
			p1.addClass('productdescription');
			p1.text(obj.data[num].name);
			li.append(p1);

			var p2 = $('<p></p>');
			p2.addClass('productPrice');
			var span = $('<span></span>');
			span.addClass('nowPrice').addClass('fl');
			span.text('￥'+obj.data[num].price);
			p2.append(span);

			var cart = $('<span></span>');
			cart.addClass('btnCart');
			cart.text('加入购物车');
			p2.append(cart);
			
			li.append(p2);
			var stock = obj.data[num].stock;
			cart.on('click',function(){
				var parent = $(this).parents('a');
				var item = parent.attr("dataID");
				item = "item"+item;
				var cart = window.sessionStorage.getItem('wh9528');
				if(!cart){
					cart = "{}";
				}
				cart = JSON.parse(cart);
				if(cart[item]){
					cart[item].number = cart[item].number+1;
				}else{
					var name = parent.find(".productdescription").text();
					var url = parent.find("img").attr("src");
					var price = parent.find(".nowPrice").text();
					
					var obj = {
						"name":name,
						"url":url,
						"price":price.replace("￥","").trim(),
						"number":1,
						"stock":stock
					}
					cart[item] = obj;
				}
				var num = 0;
				if($('.cartNumber').text()){
					num = parseInt($('.cartNumber').text())+1
				}
				$('.cartNumber').text(num);
				window.sessionStorage.setItem('wh9528',JSON.stringify(cart));
			});

			a.append(li);
			ul.append(a);
		});

		choice.append(ul);
		$('#searchResultsList').remove();

		$('#priceSales').after(choice);
	}

	$('.searchBtn').on("click",function(){
		menu();
	})
	menu();

	//价格排序
	$(".priceBtn").on('click',function(){
		var hidden1 = $('.arrow.asc').css("display");
		var hidden2 = $('.arrow.dsc').css("display");
		var asc = true;
		if((hidden1 == 'none' && hidden2 == 'none')){
			$('.arrow.asc').css("display","inline-block");
		}
		if((hidden1 == 'none' && hidden2 != 'none')){
			$('.arrow.asc').css("display","inline-block");
			$('.arrow.dsc').css("display","none");
		}
		if((hidden1 != 'none' && hidden2 == 'none')){
			$('.arrow.asc').css("display","none");
			$('.arrow.dsc').css("display","inline-block");
			asc = false
		}
		menu();
	});

	//上一页
	$('.pagerPrev').on('click',function(){
		var pagerCur = $('.pagerCur').text();
		if(pagerCur == 1 || !pagerCur){
			return;
		}
		$('.pagerCur').text(parseInt(pagerCur)-1);
		menu();
	});

	//下一页
	$('.pagerNext').on('click',function(){
		var pagerCur = $('.pagerCur').text();
		var pagerTotal = $('.pagerTotal').text();
		if(pagerCur == pagerTotal || !pagerCur){
			return;
		}
		$('.pagerCur').text(parseInt(pagerCur)+1);
		menu();
	});
	
	//getMenu(json);
	setCarts();

	//鼠标放在购物车上面出现详细商品列表
	$('#hoverCart').hoverDelay({
			hoverDuring: 100,
            outDuring: 100,
            hoverEvent: function(){
            	var carts = window.sessionStorage.getItem('wh9528');
            	if(!carts){
            		carts = "{}";
            	}
            	carts = JSON.parse(carts);
				$('#cartGoods').show();
				var str_1 = '';
				var str_2 = '';
				var totalPrice = 0;
				var totalNumber = 0;
				//for(var i=0; i<cartsGoods.length; i++){
				for(var key in carts){
					var cartsGoods = carts[key];
					str_1 += '<li><div class="goodsImg fl"><img src="'+ cartsGoods.url +'"></div><div class="goodsDisc fl"><a href="javascript:void(0)">'+ cartsGoods.name  +'</a></div><div class="fr goodsOperation">￥<span class="unitPrice">'+ cartsGoods.price +'</span>×<span class="theNumber">'+ cartsGoods.number +'</span><br /></div></li>';
					totalPrice += parseFloat(cartsGoods.price);
					totalNumber +=parseFloat(cartsGoods.number);
				}
				$('#cartGoods ul').html(str_1);
				str_2 = '<div class="total"><div class="totalLeft fl">共<span class="totalNumber">'+ totalNumber +'</span>件商品&nbsp;&nbsp;&nbsp;共计￥<span class="totalPrice">'+ totalPrice +'</span>元</div><div class="totalRight fr">去结算</div></div>';
				$('#cartGoods ul').append(str_2);
				$('.totalRight').on('click',function(){
					window.top.location.href = "cart.html";
				});
            },
            outEvent: function(){
                $('#cartGoods').hide();
            }
		
	});



	//页面初始化

	//图片延迟加载
	var fn = function(){
	    $(".choice_right img").each(function() {//遍历所有图片
	        var othis = $(this),//当前图片对象
	            top = othis.offset().top - $(window).scrollTop();//计算图片top - 滚动条top
	        if (top > $(window).height()) {//如果该图片不可见
	            return;//不管
	        } else {
	        	var img = new Image();
	        	$(img).attr('src', othis.attr('data-src'));
	    		//可见的时候把占位值替换 并删除占位属性
	        	setTimeout(function(){	
	        		othis.css('background', 'none');
	        		othis.attr('src', othis.attr('data-src')).removeAttr('data-src');
	        	}, 1000);
	            
	        }
	    });
	}
	$(window).scroll(function(){
		fn();
	}).resize(function(){
		fn();
	});
	fn();

	
	



});