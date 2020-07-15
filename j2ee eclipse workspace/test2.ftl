<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>test2</title>
</head>
<body>
	<form action="/test/draw" method="post">
	userId：<input type="text" name="userId"/><br/>
	hisDataId：<input type="text" name="hisDataId"/><br/>	
	<input type="radio" name="hasLabeled" value="on" checked="checked"/> 是
	<input type="radio" name="hasLabeled" value="off"/> 否
	<input type="submit" value="提交"/>
	</form>
</body>
</html>