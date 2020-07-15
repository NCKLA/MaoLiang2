<!DOCTYPE html>
<html>
<head>	
    <meta charset="utf-8">
<!--     <meta content="ie=edge" http-equiv="x-ua-compatible">
    <meta content="template language" name="keywords">
    <meta content="Tamerlan Soziev" name="author">
    <meta content="Admin dashboard html template" name="description">
    <meta content="width=device-width, initial-scale=1" name="viewport"> -->

			<title>China Mangement Company</title>	

    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css">
    <link href="/static/css/main.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.css">
    <link rel="stylesheet" type="text/css" href="/static/css/styles.css">	
	<link rel="shortcut icon" href="/static/images/favicon.ico" />
	
	<script src="/static/js/jquery-3.3.1.min.js"></script>
<!-- 	<script src="/static/js/popper.min.js"></script> -->
	<script src="/static/js/bootstrap.min.js"></script>
	
	<#if unsigned??>
	<script type="text/javascript">
	$(document).ready(function(){
	alert("您还未登录，请不要乱逛");
	})
	</script>
	</#if>
</head>
<body>
<div class="container-fluid main-page h-100 col-sm-12 col-md-12 col-lg-12 ">
	<div class="container pb-5 ">
		<div class="row">
			<div class="col-xl-4  mx-auto main-page-containers  ">
				<div class="col-xl-2 mx-auto pt-5 images ">
					<img src="/static/images/images.png"  class="w-100  ">

				</div>
				<div class="pt-5 pl-3 text-center">
					<h3>LOGIN FORM</h3>
					<hr>
				</div>
				
<!-- 				<div class=" pt-5 text-center">
					<input type="radio" checked="checked" class=" mr-1">User
					<input type="radio" class="ml-3 mr-1">Admin
				
			</div> -->
			
			<form class="form-group pt-4  " action="/login/loginConfirm" method="post">
				<div class="row">
					<div class="col-12 form-inline ">
						<label class="ml-auto mr-3" for="userId" >UserID:</label>
						<input type="text" class="form-control w-50 mr-auto inputs" name="userId">
				</div>

				<div class="col-12 form-inline pt-4">
				<label class="ml-auto mr-1 " for="passWord">Password:</label>
				<input type="password" class="form-control w-50 mr-auto inputs" name="passWord">
				</div>
				<#if wrong??>
				<span style="color: red;align-content: center;" >学号或密码错误，请重试</span>
				</#if>
				
				<div class="ml-auto pt-4 buttons">
					<!-- <input type="submit" value="Confirm" class=" mr-3 submit " > -->
					<button class="btn btn-success btn-sm mr-3 submit">CONFIRM</button>
					<button class="btn btn-danger btn-sm ml-3 clear">CLEAR</button>
				<!-- <input type="submit" value="Clear" class=" ml-3 clear" > -->
				</div>
			
				<!-- 验证失败的提示 -->

				</div>
					<div class="text-right pt-3 NewUser">
					<a href="../webapp/signup.html">Register a new user</a>
				</div>
			</form>
		</div>
	</div>
</div>
</div>



</body>
</html>