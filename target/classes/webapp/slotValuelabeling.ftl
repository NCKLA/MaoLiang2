<!DOCTYPE html>
<#setting number_format="#">
<html>
<head>	
    <meta charset="utf-8">

	<title>Slot Value标注</title>		
			
	<script src="/static/js/jquery-3.3.1.min.js"></script>
<!-- 	<script src="/static/js/popper.min.js"></script> -->
	<script src="/static/js/bootstrap.min.js"></script>
	
	<script src="/static/js/jquery.js"></script>	

    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css">
    <link href="/static/css/main.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/css/styles.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
	<link rel="shortcut icon" href="/static/images/favicon.ico" />
<script type="text/javascript">
var s1,s2,map;
var extraSlotNumber = 1;
$(document).ready(function(){
	 map = {};
	 var i = 1;
	map[0]=["NULL"];
	<#list slotMenu?keys as key>
		
		map[i]=[];
		<#list slotMenu["${key}"] as value>
			map[i].push("${value}");
		</#list>		
		i++;
	</#list>
});
function cg1(SA,SB){
	s1 = document.getElementById(SA);
	s2 = document.getElementById(SB);	
	$("#"+SB).empty();
	
	var index = s1.selectedIndex;
	
	for(var i = 0 ; i < map[index].length ; i++) {
		if(index==0){
			s2.add(new Option("----空----","NULL"));			
		}else {
			s2.add(new Option(map[index][i],map[index][i])); 
		}		
	}
}
function addSlot(){
	
	//这段代码的20.4.5时候的版本存到有道云了  万一出错拿出来用   036.log
	var temp = 
	"<div class='col-lg-13 d-flex pr-2' id='divSelect"+ extraSlotNumber +"'><div class='col-5'>" +
	"<div class='form-group' >"+
	"slot:<select name='selectA"+ extraSlotNumber +"' id='selectA"+ extraSlotNumber +"' onchange='" +

	'cg1("selectA'+ extraSlotNumber +'","selectB'+ extraSlotNumber +'")'+
	
	"' class='py-1 'style='border-radius: 4px; width: 125px;' >" +
	"<option value='NULL'>----空----</option>" +
	"<#if slotMenu??><#list slotMenu?keys as key><option value='${key}'>${key}</option></#list></#if>" +
	"</select></div></div>" +
	"<div class='col-5 '>"+
	"<div class='form-group' >value:" +
	"<select name='selectB"+ extraSlotNumber +"' id='selectB"+ extraSlotNumber +"' class='py-1 ' style='border-radius: 4px; width: 125px;'>" +
	"<option value='NULL'>----空----</option></select></div></div>" +
	"<div class='col-2 '><button onclick='" +
	
	'deleteSlot("divSelect'+ extraSlotNumber +'")' +
	
	"'class='btn btn-danger btn-sm' type='button'>删除此行</button></div></div>";
	
	
	$("#mainSelectDiv").append(temp);
	
	extraSlotNumber++;
}
function deleteSlot(divId){
	var elem=document.getElementById(divId); // 按 id 获取要删除的元素
	elem.parentNode.removeChild(elem);
}
function updateType(){
	var e = document.getElementById("submitType");
	e.value = "S";
	
	document.getElementById('infos').submit();
}
function updateType2(){
	var e = document.getElementById("submitType");
	e.value = "L";
	
	document.getElementById('infos').submit();
}


</script>

</head>
<body>
<div class="container-fluid main-page h-100 ">
	<div class="container pb-5  main-page-containers ">
		<div class="row">
				<div class="col-lg-6 col-sm-12 col-md-12">
				<div class="col-12 my-4 " style="overflow-y: scroll; height: 600px; border: 2px solid #eee;">
					<#if noSuitDataId="false">
						<h1>order:</h1>
						<p>${order.toString()}</p>
						<br>
					</#if>
					<#if noSuitDataId="true">
						<h1>no suitable data for you,您太能肝了！</h1>
						<br>
					</#if>
				</div>
				</div>
			<div class="col-lg-6">

				<div class="col-12">	
					<div class="col-12 my-4 " style="overflow-y: scroll; height: 300px; border: 2px solid #eee;">
					<#if noSuitDataId="false">
						<h1>data:</h1>
						<p>${data.toString()}</p>
						<br>						
						<h1>contexts：</h1>
						<#list contexts as context>
						<p>${context}</p>
						<br>
						</#list>
					</#if>
					</div>
				
				
		<form id="infos" class="form-horizontal" role="form" action="/slot/receive" method="POST">
		<!-- <form id="infos" class="form-horizontal" action="/slot/receive" method="POST"> -->
		<!-- 下拉菜单的那一行从这里开始 -->
		<div id="mainSelectDiv" style="overflow-y: scroll; height: 250px; border: 2px solid #eee;">
			
				<div class="col-lg-13 d-flex pr-2" >
				
					<div class="col-5">
					<div class="form-group" >
						slot:<select  name="selectA" id="selectA" onchange="cg1('selectA','selectB')" class="py-1 " style="border-radius: 4px; width: 125px;" >
						<option value="NULL">----空----</option>
						<#if slotMenu??>
		    	            <#list slotMenu?keys as key>
				                <option value="${key}">${key}</option>
				            </#list>
				        </#if>
						</select>						
					</div>
				</div>
			
		
		<div class="col-5">
			<div class="form-group">
				value:<select name="selectB" id="selectB" class="py-1 " style="border-radius: 4px; width: 125px;">
						<option value="NULL">----空----</option>
						</select>					
				</div>
		</div>
				<div class="col-2">	
						<button id="addslot" onclick="addSlot()"  type="button" class="btn btn-primary btn-sm">添加标注</button>
				</div>
				</div>
				</div>
				
				
				
				
		<!-- 下拉菜单的那一行从这里结束 -->
		<div class="col-12 d-flex pt-5 text-center">
			<div class="col-lg-4 ">	
						
				<button class="btn btn-primary btn-sm btn-btn" type="button">新增SlotValue</button>					
					
			</div>
				<div class="col-lg-5 ">	
						
				<button class="btn btn-warning px-4 btn-sm btn-btn" type="button" onclick="updateType();" >跳过</button>					
					
			</div>
		<div class="form-group">

				<input type="hidden"  class="form-control" name="dataId" value="${data.getDataId()}">
				<input type="hidden"  class="form-control" name="userId" value="${userId}">
				
				<input type="hidden" id="submitType" class="form-control" name="submitType" value="L">
				
<!-- 				<input type="hidden" class="form-control"  id="dataId" value="${data.getDataId()}"> -->
		</div>
	     <div class="form-group">
			<div class="col-lg-4 ">								
				<button  class="btn btn-success btn-sm px-4  btn-btn" type="button" onclick="updateType2();" >提交</button>		
			</div>
		</div>
		</div>
		
		</form>
		
				
			</div></div>
	   </div>
   </div>
</div>


</body>
</html>
