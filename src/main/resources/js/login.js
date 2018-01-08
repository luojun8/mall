require.config({
    paths: {
        'jquery': 'jquery-1.8.0'
    }
});
require(['jquery','common'], function($,common) {
	$("#submit").on("click",function(){
		var data = {
		 	"name":$("#account").val(),
		 	"password":$("#password").val()
		 }
		 $.ajax({
            type: "POST",
            dataType: "json",
            url: common("login"),
            data: data,
            success: function (result) {
                if(result.success){
                	window.sessionStorage["wh9527"] = JSON.stringify(result.data);
                	window.location.href = "index.html";
                }else{
                	alert(result.errMsg);
                }
            },
            error: function(data) {
                alert("error:请求错误");
             }
         });
	});
});