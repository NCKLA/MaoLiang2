<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ajax test slot for manage</title>
<script src="/static/js/jquery.js"></script>
<script type="text/javascript">
	$(document).ready(function(){		
/* 		$("#btn1").click(function(){
			alert("wdnmd");
		}); */
/* 	    var obj = window.document.location;
	    var BASE_PATH = obj.href.substring(0, obj.href.indexOf(obj.pathname)); */
		$("#btn1").click(function(){			
			$.ajax({
					type:"POST",
					url: "/slot/ajaxToManage",
					data:{},
					dataType:'json',
			        xhrFields: {
			            withCredentials: true
			        },
			        crossDomain: true,
					success:function(data){
						$("#div1").empty();						
						$("#p1").val(data.filename);
						$("#p2").val(data.maxRemain);
						
						/* 先打印看看这些信息 （打完了 没问题 记录在有道云笔记）*/
/*  						var tab ='<p>'+ "我试一哈" +'</p>';
						$("#div1").append(tab);
						
						var tab ='<p>第一个dataid：'+ data.dataDistributor.linkList[0].data.dataId +'</p>';						
						$("#div1").append(tab);
						
						var tab ='<p>第三个data的remain：'+ data.dataDistributor.linkList[2].remain +'</p>';
						$("#div1").append(tab);
						
						var tab ='<p>currentDataId：'+ data.dataDistributor.currentDataId +'</p>';
						$("#div1").append(tab);
						
						var tab ='<p>order的第一个info，有丶多：'+ data.dataDistributor.orderMap[2338].orderInfo +'</p>';
						$("#div1").append(tab);
						
						var tab ='<p>order的第三个的时间：'+ data.dataDistributor.orderMap[2341].orderTimes +'</p>';
						$("#div1").append(tab); */
						
/* 						var tab ='<p>第二个上下文：'+ data.dataDistributor.contextMap[24105].splitChatRecord +'</p>';
						$("#div1").append(tab); */
						
/*  						var tab ='<p>'+  +'</p>';
						$("#div1").append(tab); */
						
  						var tab = '<h2>题库中待标注数据信息</h2>'+
						'<table><tr>'+
							'<th>index</th>'+
							'<th>id</th>'+
							'<th>聊天记录</th>'+
							'<th>分词聊天记录</th>'+
							'<th>speaker</th>'+
							'<th>slotValueLabel</th>'+
							'<th>备注</th>'+
							'<th>订单id</th>'+
							'<th>remian</th>'+
						'</tr>';
						$("#div1").append(tab);


						//既然题库传不过来信息，那我专门开一个linklist分矿
  						for (var i=0;i<data.dataDistributor.useSize;i++) {
	                        var tab = '<tr>' +
	                            '<td>' + i + '</td>' +
	                            '<td>' + data.dataDistributor.linkList[i].data.dataId + '</td>' +
	                            '<td>' + data.dataDistributor.linkList[i].data.chatRecord + '</td>' +
	                            '<td>' + data.dataDistributor.linkList[i].data.splitChatRecord + '</td>' +
	                            '<td>' + data.dataDistributor.linkList[i].data.speaker + '</td>' +
	                            '<td>' + data.dataDistributor.linkList[i].data.slotValueLabel + '</td>' +
	                            '<td>' + data.dataDistributor.linkList[i].data.remark + '</td>' +
	                            '<td>' + data.dataDistributor.linkList[i].data.orderId + '</td>' +
	                            '<td>' + data.dataDistributor.linkList[i].remain + '</td>' +
	                            '</tr>';
	                        $("#div1").append(tab);
						}
						$("#div1").append('</table>'); 
						
 						var tab = '<h2>题库中订单信息</h2>'+
						'<table><tr>'+
						'<th>订单编号</th>'+
						'<th>客户昵称</th>'+
						'<th>订单时间</th>'+
						'<th>订单内容</th>'+ 
						'</tr>';
						$("#div1").append(tab); 
						
						 for (var i in data.dataDistributor.orderMap){
							var tab = '<tr>' +
							'<td>' + i + '</td>' +
							'<td>' + data.dataDistributor.orderMap[i].buyerNick + '</td>' +
							'<td>' + data.dataDistributor.orderMap[i].orderTimes + '</td>' +
							'<td>' + data.dataDistributor.orderMap[i].orderInfo + '</td>' +
							'</tr>';
	                        $("#div1").append(tab);
						}
						$("#div1").append('</table>'); 
						
						/* var tab = '<h2>题库中上文信息</h2>'+
						'<table><tr>'+
						'<th>id</th>'+
						'<th>对话</th>'+
						'<th>speaker</th>'+
						'<th>orderId</th>'+
						'<th>slot预标注</th>'+
						'</tr>';
						$("#div1").append(tab);
						
						for (var i in data.dataDistributor.contextMap){
							var tab = '<tr>' +
							'<td>' + i + '</td>' +
							'<td>' + data.dataDistributor.contextMap[i].chatRecord + '</td>' +
							'<td>' + data.dataDistributor.contextMap[i].speaker + '</td>' +
							'<td>' + data.dataDistributor.contextMap[i].orderId + '</td>' +
							'<td>' + data.dataDistributor.contextMap[i].slotValueLabel + '</td>' + 
							'</tr>';
	                        $("#div1").append(tab);
						}
						$("#div1").append('</table>'); */
						
 						var tab = '<h2>其他信息</h2><p>'+
						"first："+  data.dataDistributor.first + "<br/>" +
						"last："+  data.dataDistributor.last + "<br/>" +
						"useSize："+  data.dataDistributor.useSize + "<br/>" +
						"outSider："+  data.dataDistributor.outSiderMap + "<br/>" +
						"maxRemain："+  data.dataDistributor.maxRemain + "<br/>" +
						"contextLength："+  data.dataDistributor.contextLength + "<br/>" +
						"currentDataId："+  data.dataDistributor.currentDataId + "<br/>" +
						"default_SIZE："+  data.dataDistributor.default_SIZE + "<br/></p>"
						;
						$("#div1").append(tab);
					},
					error:function(){
						alert("wdnmd,error");
					}										
				});
		});
		
	});
</script>
</head>
<body>

<button id="btn1" type="button">向slotController发出ajax请求</button>
<br>
<form action="/slot/dataFromManage" method="post">
	当前文件名：<input type="text" id="p1" name="p1" /><br/><br/>
	当前最大标注人数：<input type="text" id="p2" name="p2" /><br/><br/>
	<input type="submit" value="提交"/>
</form>
<br/>

保存slotvalue的题库和下拉菜单到文件：&nbsp;&nbsp;&nbsp;<a href="/slot/savefile">
    保存
</a>

<h1>显示dataDistributor：</h1><br/>
<div id="div1">
<p>点按钮之后这句话会消失</p>
</div>
<#if dataDistributor??>
<p>dataDistributor：${dataDistributor}</p>
</#if>

<#if inta??>
<p>inta：${inta}</p>
</#if>


</body>
</html>