require.config({
	paths: {
		'jquery': 'jquery-1.8.0'
	}
});
require(['jquery','common','jqueryExtend'], function($,common) {
	function initOrder(obj){
		$.each(obj,function(key){
			var tr = $('<tr></tr>');
			tr.append('<td><a href="detail.html?id='+obj[key].item.id+'"><img src="'+obj[key].item.images+'"></a></td>')
			tr.append('<td>'+JSON.parse(window.sessionStorage["wh9527"]).name+'</td>');
			tr.append('<td>￥<span>'+obj[key].price+'</span>');
			tr.append('<td><span>'+pasreDate(obj[key].createTime)+'</span>');
			tr.append('<td>已完成</td>');

			$(".personContentRight table").append(tr);
		})
	}

	function pasreDate(date){
		date =  new Date(date);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"  "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}

	function getOrder(){
		var user = JSON.parse(window.sessionStorage.getItem("wh9527"));
		$.ajax({
			type:"POST",
			daraType:"json",
			url:common("getOrders"),
			data:{
				"userId":user.id
			},
			success:function(data){
				if(data.success){
					initOrder(data.data);
				}else{
					alert(data.errMsg);
				}
			},
			error:function(data){
				alert("发生错误");
			}
		})
	}

	getOrder();
})