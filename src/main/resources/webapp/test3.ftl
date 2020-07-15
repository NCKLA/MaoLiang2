<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>test3-打印抽题信息</title>
</head>
<body>
<#if noSuitDataId="true">
	<h1>no suit data for you,您太能肝了！</h1>
	<br>
</#if>
<#if noSuitDataId="false">
	<h1>data:</h1>
	<p>${data.toString()}</p>
	<br>
	<h1>order:</h1>
	<p>${order.toString()}</p>
	<br>
	<h1>contexts：</h1>
	<#list contexts as context>
	<p>${context}</p>
	<br>
	</#list>
</#if>
<!-- 	mav.addObject("noSuitDataId", "false");
				mav.addObject("data", data);
				
				/* 取得当前数组下标对应的data的order */
				mav.addObject("order", (Order)dataDistributor.getOrderMap().get(data.getOrderId()));			
				
				/* 给出上下文，必要时db */
				mav.addObject("contexts",initContextAboveAndBelow(data.getDataId(),data.getOrderId())); -->
</body>
</html>