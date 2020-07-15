<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>核弹试验田</title>
</head>
<body>
	<#if test1??>
	<h1>Parameter test1</h1>
	<p>${test1}</p>
	<br>
	</#if>	
	<#if test2??>
	<h1>Parameter test2</h1>
	<p>${test2.toString()}</p>
	<br>
	</#if>	
	<#if test3??>
	<h1>Parameter test3</h1>
	<p>${test3.toString()}</p>
	<br>
	</#if>	
	
	<#if test4??>
	<h1>Parameter test4</h1>
	<#list test4?keys as key>
	<font color="blue">${key}</font><br>
		<#list test4[key] as str>
		<font color="red">${str}</font><br>
		</#list>		
	<br>
	</#list>
	</#if>

	
</body>
</html>