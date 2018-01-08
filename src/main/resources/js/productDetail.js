require.config({
	paths: {
		'jquery': 'jquery-1.8.0'
	}
});
require(['jquery', 'common', 'jqueryExtend'], function($,common) {
	//照片查看器

	$('.bigImg').mouseover(function(){
		var $zhezhao = $('#zhezhao');
		var $outsideBigImg = $('#outsideBigImg');
		$zhezhao.show();
		$outsideBigImg.show();
		var _this = this;
		var l = $(_this).offset().left;
		var t = $(_this).offset().top;

		//求出小图与大图的宽高比率
		var ratioX = $outsideBigImg.find('img').width()/$(this).width();
		var ratioY = $outsideBigImg.find('img').height()/ $(this).height();
		$(document).mousemove(function(ev){
			if($(ev.target).attr('class') == $outsideBigImg.find('img').attr('class')){
				$outsideBigImg.hide();
			}
			var left = ev.pageX - l - $zhezhao.width()/2;
			var top = ev.pageY - t - $zhezhao.height()/2;
			if(left<0){
				left = 0;
			}else if(left >= ($(_this).width() - $zhezhao.width())){
				left = $(_this).width() - $zhezhao.width();
			}
			if(top<0){
				top = 0;
			}else if(top >= ($(_this).height() - $zhezhao.height())){
				top = $(_this).height() - $zhezhao.height();
			}
			$zhezhao.css({
				left:left,
				top:top
			});
			//右面的大图变化
			$outsideBigImg.find('img').css({
				left:-ratioX*$zhezhao.position().left,
				top:-ratioY*$zhezhao.position().top,
			});	
		});
		$(document).mouseout(function(){
			$(document).off();
			$zhezhao.hide();
			$outsideBigImg.hide();
		});
	});

	//图片延迟加载
	var fn = function(){
	    $(".descriptionTabBarCont img").each(function() {//遍历所有图片
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

	function GetQueryString(name){
	    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	    var r = window.location.search.substr(1).match(reg);
	    if(r!=null)return  unescape(r[2]); return null;
	}

	function getGoodDetail(){
		var id = GetQueryString("id");
		$.ajax({
			type:"POST",
			dataType:"json",
			url:common("getGood"),
			data:{
				"id":id
			},
			success:function(result){
				if(result.success){
					$(".detailTop h1").text(result.data.name);
					$(".hoverImg").attr("src",result.data.images);
					$(".outSideimg").attr("src",result.data.images);
					$(".detailNowPrice span").text(result.data.price);
					$(".spanCount").text(result.data.stock);
					$(".descriptionTabBarCont").html(result.data.desc);
				}else{
					alert(result.errMsg);
				}
			},
			error:function(data){
				alert("请求错误");
			}
		})
	}

	getGoodDetail();

	function addCart(){
		var id = GetQueryString("id");
		var item = "item"+id;
		var cart = window.sessionStorage.getItem('wh9528');
		if(!cart){
			cart = "{}";
		}
		cart = JSON.parse(cart);
		if(cart[item]){
			cart[item].number = parseInt(cart[item].number)+1;
		}else{
			var name = $(".detailTop h1").text();
			var url = $(".hoverImg").attr("src");
			var price = $(".detailNowPrice span").text();
			var num = $cartsNumberInput.val();
			var obj = {
				"name":name,
				"url":url,
				"price":price.replace("￥","").trim(),
				"stock":$(".spanCount").text(),
				"number":num
			}
			cart[item] = obj;
		}
		var num = 0;
		if($('.cartNumber').text()){
			num = parseInt($('.cartNumber').text())+1
		}
		$('.cartNumber').text(num);
		window.sessionStorage.setItem('wh9528',JSON.stringify(cart));
	}

	var stock = parseInt($('.stock').html());
	//商品数量变换
	//数量+
	var $cartsNumberInput = $('.cartsNumber input');
	$('.plus').on('click', function () {
	    var stock = parseInt($('.stock').html());
		var num = parseInt($cartsNumberInput.val());
		if(num >= stock){
			return;
		}else{
			$cartsNumberInput.attr('value', ++num);
		}
	});
	//数量-
	$('.decrease').on('click', function () {
	    var stock = parseInt($('.stock').html());
		var num = parseInt($cartsNumberInput.val());
		if(num <= 0){
			return;
		}else{
			$cartsNumberInput.attr('value', --num);
		}
	});
	//直接修改数量input，失去焦点时进行检查数量时检查是否超过库存
	$cartsNumberInput.on('blur', function() {
	    var stock = parseInt($('.stock').html());
		var num = parseInt($(this).val());
		if(num >= stock){
			$(this).attr('value', stock);
		}else{
			return;
		}
	});

	$('.atOnceBtn').on("click",function(){
		addCart();
		window.location.href = "cart.html";
	});

	$('.plusCart').on("click",function(){
		addCart();
		$(".info").css("display","");
	});


});