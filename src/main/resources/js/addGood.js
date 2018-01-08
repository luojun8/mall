require.config({
    paths: {
        'jquery': 'jquery-1.8.0'
    }
});
require(['jquery','common','wangEditor'], function($,common) {
	var base64 = "";
	$("#file").on("change", function(event){
		 var file = this.files[0];
        //判断是否是图片类型
        if (!/image\/\w+/.test(file.type)) {
            alert("只能选择图片");
            return false;
        }
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function (e) { 
        	base64 = this.result;
        	$(".upload").attr("src",this.result); 
        	$(".upload").css("display","block"); 
        }
	});

	function getBase64Image(img) {
	      var canvas = document.createElement("canvas");
	      canvas.width = img.width();
	      canvas.height = img.height();
	      var ctx = canvas.getContext("2d");
	      ctx.drawImage(img, 0, 0, img.width(), img.height());
	      var dataURL = canvas.toDataURL("image/png");
	      return dataURL
	      // return dataURL.replace("data:image/png;base64,", "");
	}


    var E = window.wangEditor;
    var editor = new E('#editor');
    editor.customConfig.uploadImgShowBase64 = true;
    editor.create()

    $("#addGood").on("click",function(){
    	var name = $("#name").val();
    	var store = $("#store").val();
    	var price = $("#price").val();
    	var detail = editor.txt.html();
    	$.ajax({
    		type:"POST",
    		dataType:"json",
    		data:{
    			"name":name,
    			"stock":store,
    			"images":base64,
    			"price":parseFloat(price),
    			"desc":detail,
    		},
    		url:common('addGood'),
    		success:function(data){
    			if(data.success){
    				alert("添加成功");
    				window.location.href="index.html";
    			}else{
    				alert(data.errMsg);
    			}
    		},
    		error:function(data){
    			alert("请求错误");
    		}
    	})
    })
})