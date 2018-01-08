require.config({
    paths: {
        'jquery': 'jquery-1.8.0'
    }
});
require(['jquery'], function($) {
	$(".loginBar").append(getUrl());
	$(".login").on("click",function(){
		window.parent.location.href = "login.html";
	});
	$(".register").on("click",function(){
		window.parent.location.href = "register.html";
	});
	$(".myOrder").on("click",function(){
		window.parent.location.href = "order.html";
	});
	$(".addGood").on("click",function(){
		window.parent.location.href = "addGood.html";
	})
	$(".logout").on("click",function(){
		window.sessionStorage.removeItem("wh9527");
		window.location.reload();
	});
	function getUrl(){
		var user = window.sessionStorage["wh9527"];
		if(user){
			user = JSON.parse(user);
			if(user.role == 2){
				return '<a href="javascript:void(0)" class="user">'+user.name+'</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="myOrder">订单查看</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="addGood">添加商品</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="logout">退出</a>';
			}else{
				return '<a href="javascript:void(0)" class="user">'+user.name+'</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="myOrder">我的订单</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="logout">退出</a>';
			}
			
		}else{
			return '<span>请</span><a href="javascript:void(0)" class="login">登录</a><span class="dividing"></span><a href="javascript:void(0)" class="register">注册</a>';
		}
	}
})