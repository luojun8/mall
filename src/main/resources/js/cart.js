require.config({
	paths: {
		'jquery': 'jquery-1.8.0'
	}
});
require(['jquery','common','jqueryExtend'], function($,common) {

	function addGoods(obj){
		if(!obj){
			return;
		}
		$.each(obj,function(key){
			var tr = $('<tr></tr>');
			tr.attr("dataId",key.substring(4));
			tr.addClass("addTrStyle");
			var td1 = $('<td></td>');
			td1.addClass("tdCheckboxs");
			var str = '<input type="checkbox" id="goods_1" class="checkboxs checkboxsOne" checked><label for="goods_1" class="labelCheck labelCheckOne"></label>&nbsp;&nbsp;&nbsp;&nbsp;';
			td1.append(str);
			tr.append(td1);

			var td2 = $('<td></td>');
			td2.addClass("formDescription");
			td2.append('<a href="#" target="_blank" >'+obj[key].name+'</a>');
			tr.append(td2);

			var td3 = $('<td></td>');
			td3.addClass("formPicture");
			td3.append('<a href="#" target="_blank" ><img src="'+obj[key].url+'"></a>');
			tr.append(td3);

			var td4 = $('<td></td>');
			td4.addClass("formPrice");
			td4.append('<span>'+obj[key].price+'</span>');
			tr.append(td4);

			var td5 = $('<td></td>');
			td5.addClass("formNumber");
			str = '<span class="spanDescrise">-</span><input type="text" value="'+obj[key].number+'" class="goodsNumber"><span class="spanPlus">+</span><div class="stock">库存<strong>'+obj[key].stock+'</strong>个</div>';
			td5.append(str);
			tr.append(td5);

			var td6 = $('<td></td>');
			td6.addClass("formAlls").addClass("spanPrice");
			td6.append('<span id="alls">'+obj[key].number*obj[key].price+'</span>')
			tr.append(td6);

			var td7 = $('<td></td>');
			td7.addClass("formOperation");
			td7.append('&nbsp;&nbsp;&nbsp;&nbsp;<span>删除</span>');
			tr.append(td7);

			$('#tbody').append(tr);
		});
	}

	addGoods(JSON.parse(window.sessionStorage.getItem('wh9528')));

	//全选判断
	//页面载入的时候所有复选框默认全选
	//flag为判断是否可结算
	var flag = true;
	//商品的总数量
	$('.footerAllNum em').text(checkedNum());
	//商品的总价钱
	$('.footerAllPrice span:nth-of-type(2)').text(checkedNumAllPrice());
	$('.labelCheckAlls').on('click', function(){
		if($('.checkboxs').prop('checked')){
			$('.checkboxs').prop('checked', false).each(function(index){
				var num = $('.checkboxs').length;
				if(!$(this).prop('checked') && index != 0 && index != (num - 1)){
					$(this).parent().parent().removeClass('addTrStyle');
				}
			});
			$('.labelCheck').css('backgroundPosition', '0 0');
			$('#cartSubmit').css({
				'cursor':'not-allowed',
				'backgroundColor':'#ddd',
				'color':'#bbb'
			}).on('click', function(){
				return false;
			});
			//商品的总数量
			$('.footerAllNum em').text('0');
			//商品的总价钱
			$('.footerAllPrice span:nth-of-type(2)').text('0');
			flag = false;
		}else{
			$('.checkboxs').prop('checked', true).each(function(index){
				var num = $('.checkboxs').length;
				if($(this).prop('checked') && index != 0 && index != (num - 1)){
					$(this).parent().parent().addClass('addTrStyle');
				}
			});
			$('.labelCheck').css('backgroundPosition', '0 -20px');
			$('#cartSubmit').css({
				'cursor':'pointer',
				'backgroundColor':'#E54346',
				'color':'#fff'
			}).on('click', function(){
				return true;
			});	
			//商品的总数量
			$('.footerAllNum em').text(checkedNum());
			//商品的总价钱
			$('.footerAllPrice span:nth-of-type(2)').text(checkedNumAllPrice());

			flag = true;
		}		
		return false;
	})
	//单击商品单项选择
	$('.labelCheckOne').on('click', function(){
		if($(this).prev().prop('checked')){
			//如果选中，则取反
			$(this).prev().prop('checked', false);
			$(this).parent().parent().removeClass('addTrStyle');
			$(this).css('backgroundPosition', '0 0');
		}else{
			//若没有选中，则取其反
			$(this).prev().prop('checked', true);
			$(this).parent().parent().addClass('addTrStyle');
			$(this).css('backgroundPosition', '0 -20px');
		}
		var isChecked = isCheckAllGoods();
		var isSomeChecked = isCheckSomeGoods();
		//判断商品是否全部选中
		if(!isChecked){
			$('.allCheckBoxs').prop('checked', false);
			$('.labelCheckAlls').css('backgroundPosition', '0 0');
			flag = false;
		}else{
			$('.allCheckBoxs').prop('checked', true);
			$('.labelCheckAlls').css('backgroundPosition', '0 -20px');
			flag = true;
		}	
		//判断商品是否部分选中
		if(!isSomeChecked){
			$('#cartSubmit').css({
				'cursor':'not-allowed',
				'backgroundColor':'#ddd',
				'color':'#bbb'
			}).on('click', function(){
				return false;
			});
		}else{
			$('#cartSubmit').css({
				'cursor':'pointer',
				'backgroundColor':'#E54346',
				'color':'#fff'
			}).on('click', function(){
				return true;
			});	
		}
		//商品的总数量
		$('.footerAllNum em').text(checkedNum());
		//商品的总价钱
		$('.footerAllPrice span:nth-of-type(2)').text(checkedNumAllPrice());
		//阻止默认事件
		return false;
	})
	//判断单项商品是否全部选中
	function isCheckAllGoods(){
		var flag = true;
		$('.checkboxsOne').each(function(){
			if(!$(this).prop('checked')){
				flag = false;
			}
		});
		return flag;
	}
	//判断页面是否有商品选中
	function isCheckSomeGoods(){
		var flag = false;
		$('.checkboxsOne').each(function(){
			if($(this).prop('checked')){
				flag = true;
			}
		});
		return flag;
	}	
	//单个商品的数量自定义范围限制不小于1
	$('.goodsNumber').blur(function(){
		var price = $(this).parent().parent().find(".formPrice").text();
		var stockNum = parseInt($(this).nextAll('.stock').find('strong').text());
		var goodsInfo = postGoodsInfo(this);
		if($(this).val() <= 0){
			$(this).attr('value', 1);
			changeCart(goodsInfo.id,1);
		}else if($(this).val() >= stockNum){
			$(this).attr('value', stockNum);
			changeCart(goodsInfo.id,stockNum);
		}else{
			changeCart(goodsInfo.id,$(this).attr('value'));
		}
		$(this).parent().next().find('span').text($(this).val()*price);
		//商品的总数量
		$('.footerAllNum em').text(checkedNum());
		//商品的总价钱
		$('.footerAllPrice span:nth-of-type(2)').text(checkedNumAllPrice());
		return false;
	});
	//计算选中商品的数量
	function checkedNum(){
		var allNum = 0;
		$('.addTrStyle .goodsNumber').each(function(){
			allNum += parseInt($(this).val());
		});
		return allNum;
	}
	//计算选中商品的价钱的总数
	function checkedNumAllPrice(){
		var allPrice = 0;
		$('.addTrStyle .spanPrice span').each(function(){
			allPrice += parseInt($(this).text());
		});
		return allPrice;
	}
	//获取商品的数量和商品ID
	function postGoodsInfo(obj){
		var goodsInfo = {};
		goodsInfo.id  = $(obj).parents(".addTrStyle").attr("dataId");
		goodsInfo.num = $(obj).parents(".addTrStyle").find('.goodsNumber').val();
		return goodsInfo;
	}
	function changeCart(id,num){
		var item = "item"+id;
		var cart = JSON.parse(window.sessionStorage.getItem('wh9528'));
		cart[item].number = num;
		window.sessionStorage.setItem('wh9528',JSON.stringify(cart));
	}
	//点击数量减号，后面的价格变化
	$('.spanDescrise').on('click', function(){
		var num = $(this).next().val();
		var price = $(this).parent().parent().find(".formPrice").text();
		var goodsInfo = postGoodsInfo(this);
		if(num <= 1){
			return false;
		}else{
			num--;
			$(this).next().attr('value', num);
			$(this).parent().next().find('span').text(num*price);
			changeCart(goodsInfo.id,num);
		}
		//商品的总数量
		$('.footerAllNum em').text(checkedNum());
		//商品的总价钱
		$('.footerAllPrice span:nth-of-type(2)').text(checkedNumAllPrice());

	});
	//点击数量加号，后面的价格变化
	$('.spanPlus').on('click', function(){
		var stockNum = parseInt($(this).nextAll('.stock').find('strong').text());
		var num = $(this).prev().val();
		var price = $(this).parent().parent().find(".formPrice").text();
		var goodsInfo = postGoodsInfo(this);
		if(num >= stockNum){
			return false;
		}else{
			num++;
			$(this).prev().attr('value', num);
			$(this).parent().next().find('span').text(num*price);
			changeCart(goodsInfo.id,num);
		}			
		//商品的总数量
		$('.footerAllNum em').text(checkedNum());
		//商品的总价钱
		$('.footerAllPrice span:nth-of-type(2)').text(checkedNumAllPrice());
	});
	//点击删除按钮，删除该商品的信息，同时向后台提交删除商品的信息
	$('.formOperation span:nth-of-type(1)').on('click', function(){
		var goodsId = postGoodsInfo(this).id;
		if(confirm("是否从购物车中移除该商品？")){
			$.mask();
			$.loading('../image/icon/loading_1.gif', '删除');
			var cart = window.sessionStorage.getItem('wh9528');
			cart = JSON.parse(cart);
			delete cart["item"+goodsId];
			window.sessionStorage.setItem('wh9528',JSON.stringify(cart));
			$(this).parent().parent().remove();
			setTimeout(function(){
				$.unMask();
				$.unloading();
			},500)
		}
		//商品的总数量
		$('.footerAllNum em').text(checkedNum());
		//商品的总价钱
		$('.footerAllPrice span:nth-of-type(2)').text(checkedNumAllPrice());
	});

	//点击去结算提交后台数据
	$('#cartSubmit').on('click', function(e){

		var goodsID = [];
		if($(this).css('cursor') == 'pointer'){
			$('.addTrStyle .formName span').each(function(){
				goodsID.push($(this).attr('data-id'));
			});
			$.mask();
			$.loading('../image/icon/loading_1.gif', '跳转');
			var cart = JSON.parse(window.sessionStorage.getItem('wh9528'));
			var user = JSON.parse(window.sessionStorage.getItem('wh9527'));
			var data = {};
			$.each(cart,function(key){
				var i = key.substring(4);
				var j = cart[key].number;
				data[i]=j;
			})
			var d = {};
			d["jsonOrders"] = JSON.stringify(data);
			d["price"] = $('.footerAllPrice span:nth-of-type(2)').text();
			d["userId"] = user.id;
			console.log(d);
			$.ajax({
					type:'POST',
					url:common("submitOrder"),
					data:d,
					success:function(data){
						$.unMask();
						$.unloading();
						if(data.success){
							window.sessionStorage.removeItem("wh9528");
							alert("提交成功");
							window.location.href="index.html";
						}else{
							alert(data.errMsg);
						}
						
					},
					error:function(){
						$.unMask();
						$.unloading();
						alert("提交出错");
					}
			});
		}else{
			e.stopPropagation();	
		}
	});
	


});