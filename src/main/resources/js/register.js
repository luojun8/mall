require.config({
    paths: {
        'jquery': 'jquery-1.8.0'
    }
});
require(['jquery','common'], function($,common) {
	//定义标识符，判断提交之前表单是否都正确
	var allsFlagUser = false,
		allsFlagPass = false,
		allsFlagConfirmPass = false,
		allsFlagMail = false,
		allsFlagPhone = false,
		allsFlagVerificationCode = false;
	//用户名检验
	var loadingFlag_1 = true;
	var $user = $('#user');
	$user.on('focus', function(){
		if(loadingFlag_1){
    		$(this).css({
    			borderColor:"#7ABD54"
    		});
    		var additionalInfo = $(this).nextAll('.additionalInfo').css({
    				borderColor:'#7ABD54',
    				backgroundColor:'#F0FAEA',
    				color:'#2b2b2b'
    			}).removeClass('beforeRed').show().find('p').html('4-20位字符，支持汉字、字母、数字及"-"、"_"组合');
    		$(this).next('span.successIcon').hide();    			
		}
	});
	$user.on('blur', function(){
		var value = $(this).val();
		var pattern = /^[a-zA-Z0-9\u4E00-\u9FA5_-]{4,20}$/;
		var numPattern = /^[0-9\s]{2,8}$/;
		var _this = $(this);
		if(value.length < 4 || value.length > 20){
			$(_this).css('borderColor','#f00');
			$(_this).nextAll('.additionalInfo').css({
				borderColor:'#f00',
				backgroundColor:'#FFEBEB',
				color:'#f00'
			}).addClass('beforeRed').show().find('p').html('用户长度只能在4到20位字符之间');
			allsFlagUser = false;
		}else if(numPattern.test(value)){
			$(_this).css('borderColor','#f00');
			$(_this).nextAll('.additionalInfo').css({
				borderColor:'#f00',
				backgroundColor:'#FFEBEB',
				color:'#f00'
			}).addClass('beforeRed').show().find('p').html('用户名不能是纯数字');
			allsFlagUser = false;	
		}else if(!pattern.test(value)){
			$(_this).css('borderColor','#f00');
			$(_this).nextAll('.additionalInfo').css({
				borderColor:'#f00',
				backgroundColor:'#FFEBEB',
				color:'#f00'
			}).addClass('beforeRed').show().find('p').html('用户名只能由中文、英文、数字及"-"、"_"组成');
			allsFlagUser = false;
		}else{
			loadingFlag_1 = false;
			$(_this).css('borderColor','#ccc');
			$(_this).nextAll('.additionalInfo').css({
				borderColor:'#7ABD54',
				backgroundColor:'#F0FAEA',
				color:'#2b2b2b'
			}).removeClass('beforeRed').hide().find('p').html('4-20位字符，支持汉字、字母、数字及"-"、"_"组合');
			//用户名正确后ajax提交检验是否可用
				$user.nextAll('.loading').css('display','block');
/*      			$.ajax({
				type:'GET',
				url:'#',
				data:'{"username":"'+value+'"}',
				dataType:'json',
				success:function(data){
					$user.nextAll('.loading').css('display','none');
					
				},
				error:function(){
					console.log("提交出错");
				}
			});*/
			setTimeout(function(){
				loadingFlag_1 = true;
				$(_this).css('borderColor','#ccc');
    			$(_this).nextAll('.additionalInfo').css({
    				borderColor:'#7ABD54',
    				backgroundColor:'#F0FAEA',
    				color:'#2b2b2b'
    			}).removeClass('beforeRed').hide().find('p').html('请输入正确邮箱');
    			$(_this).nextAll('span.successIcon').show();
    			$user.nextAll('.loading').css('display','none');
    			allsFlagUser = true;
			}, 2000);
		}
	});
	
	//密码验证
	var $pass = $('#pass');
	$pass.on('focus', function(){
		$(this).css({
			borderColor:"#7ABD54"
		});
		var additionalInfo = $(this).nextAll('.additionalInfo').css({
				borderColor:'#7ABD54',
				backgroundColor:'#F0FAEA',
				color:'#2b2b2b'
			}).removeClass('beforeRed').show().find('p').html('6到20位字符，建议由字母，数字和符号两种以上的组合');
		$(this).next('span.successIcon').hide();
	});
	var passValue='', secondPass='';
	$pass.keyup(function(){
		passValue = $(this).val();
		if(passValue.length >=6){
			//验证密码强度
			$('.formInfo>div:nth-of-type(2)>p').css('display','block');
			if(/^[\S]{6,20}$/.test(passValue) && /[a-zA-Z]{1,20}/.test(passValue) && /[0-9]{1,20}/.test(passValue) && /[^a-zA-Z0-9\s]{1,20}/.test(passValue) ){
				$('.pwdstrength').css({
					backgroundPosition:'0 -26px'
				});	
			}else if(/^[\S]{6,20}$/.test(passValue) && ((/[a-zA-Z]{1,20}/.test(passValue) && /[0-9]{1,20}/.test(passValue)) ||  (/[0-9]{1,20}/.test(passValue) && /[^a-zA-Z0-9]{1,20}/.test(passValue)) || (/[a-zA-Z]{1,20}/.test(passValue) && /[^a-zA-Z0-9]{1,20}/.test(passValue)) )){
				$('.pwdstrength').css({
					backgroundPosition:'0 -13px'
				});	
			}else if(/^[\d]{6,20}$/.test(passValue) || /^[a-zA-Z]{6,20}$/.test(passValue) || /^[^a-zA-Z0-9]{6,20}$/.test(passValue)){
				$('.pwdstrength').css({
					backgroundPosition:'0 0'
				});
			}	
		}else{
			$('.formInfo>div:nth-of-type(2)>p').css('display','none');
			$(this).nextAll('span.successIcon').hide();
		}

	});
	var $confirmPass =  $('#confirmPass');
	$pass.on('blur', function(){
		var value = $(this).val();
		var pattern = /^[\S]{6,20}$/;
		//当第二次再次修改第一次输入的密码的时候
		if(secondPass.length > 0){
			if(secondPass != passValue){
				$confirmPass.css('borderColor','#f00');
    			$confirmPass.nextAll('.additionalInfo').css({
    				borderColor:'#f00',
    				backgroundColor:'#FFEBEB',
    				color:'#f00'
    			}).addClass('beforeRed').show().find('p').html('两次输入密码不一致');
    			$confirmPass.next('span.successIcon').hide();
    			allsFlagConfirmPass = false;
			}else{
				$confirmPass.css('borderColor','#ccc');
    			$confirmPass.nextAll('.additionalInfo').css({
    				borderColor:'#7ABD54',
    				backgroundColor:'#F0FAEA',
    				color:'#2b2b2b'
    			}).removeClass('beforeRed').hide().find('p').html('请再次输入密码');
    			$confirmPass.next('span.successIcon').show();
    			allsFlagConfirmPass = true;
			}
		}

		if(value.length < 6 || value.length >20){
			$(this).css('borderColor','#f00');
			$(this).nextAll('.additionalInfo').css({
				borderColor:'#f00',
				backgroundColor:'#FFEBEB',
				color:'#f00'
			}).addClass('beforeRed').show().find('p').html('密码长度只能在6到20位之间');
			allsFlagPass = false;
		}else if(/\s+/.test(value)){
			$(this).css('borderColor','#f00');
			$(this).nextAll('.additionalInfo').css({
				borderColor:'#f00',
				backgroundColor:'#FFEBEB',
				color:'#f00'
			}).addClass('beforeRed').show().find('p').html('密码不能有空格');
			allsFlagPass = false;
		}else if(pattern.test(value)){
			$(this).css('borderColor','#ccc');
			$(this).nextAll('.additionalInfo').css({
				borderColor:'#7ABD54',
				backgroundColor:'#F0FAEA',
				color:'#2b2b2b'
			}).removeClass('beforeRed').hide().find('p').html('6到20位字符，建议由字母，数字和符号两种以上的组合');
			$(this).nextAll('span.successIcon').show();
			allsFlagPass = true;
		}
	});
	//二次密码验证是否相等
	$confirmPass.on('focus', function(){
		$(this).css({
			borderColor:"#7ABD54"
		});
		var additionalInfo = $(this).nextAll('.additionalInfo').css({
				borderColor:'#7ABD54',
				backgroundColor:'#F0FAEA',
				color:'#2b2b2b'
			}).removeClass('beforeRed').show().find('p').html('请再次输入密码');
		$(this).next('span.successIcon').hide();
	});
	$confirmPass.blur(function(){
		secondPass = $(this).val();
			if(secondPass.length > 0 && passValue.length > 0 && secondPass == passValue){
				$(this).css('borderColor','#ccc');
    			$(this).nextAll('.additionalInfo').css({
    				borderColor:'#7ABD54',
    				backgroundColor:'#F0FAEA',
    				color:'#2b2b2b'
    			}).removeClass('beforeRed').hide().find('p').html('请再次输入密码');
    			$(this).next('span.successIcon').show();
    			allsFlagConfirmPass = true;
    			// checkIsSubmit();
			}else{
				$(this).css('borderColor','#f00');
    			$(this).nextAll('.additionalInfo').css({
    				borderColor:'#f00',
    				backgroundColor:'#FFEBEB',
    				color:'#f00'
    			}).addClass('beforeRed').show().find('p').html('两次输入密码不一致');	
    			allsFlagConfirmPass = false;
			}				

	});

	//点击提交按钮
	$('#btn').bind('click',function(){
		if(allsFlagUser && allsFlagConfirmPass){
			 var data = {
			 	"name":$("#user").val(),
			 	"password":$("#pass").val()
			 }
			 $.ajax({
                type: "POST",
                dataType: "json",
                url: common("register"),
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
		}	
	});

});
