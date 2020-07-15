<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<link rel="shortcut icon" href="/static/images/favicon.ico" />
<title>管理页面测试</title>
</head>
<body>
<p>测试的management页面，测试与其他ftl的包含与ajax响应</p>
<br><br>
<#if aaa??>
	主页传参：${aaa}
</#if>
<br><br>
<p>分隔一哈</p>
<br><br>
<p>------------------------------------------------------------------------</p>
<br>
<p>下面是ajaxTestSlot.ftl</p>
<#include "ajaxTestSlot.ftl">

</body>
</html>