<%@ include file="/jsp/base/taglib.jsp" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta name="renderer" content="webkit"/>
        <title>多媒体管理系统</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <meta name="keywords" content="" />
        <meta name="author" content="" />
        <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
		<link href="/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" />
		<link rel="stylesheet" type="text/css" media="screen" href="/css/base/table.css" >		
		<link rel="stylesheet" type="text/css" media="screen" href="/css/base/base.css"/> 
		<link rel="stylesheet" type="text/css" media="screen" href="/css/admin/classroom_manage.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="/css/admin/repertory.css"/>
		<link href="/datepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" />
        <script type='text/javascript' src="/js/base/jquery-2.1.4.min.js"></script>
        <script type='text/javascript' src="/datepicker/js/bootstrap-datetimepicker.min.js"></script>
		<script type="text/javascript" src="/datepicker/js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
		<style>
				td,tr,th{
				text-align:center;
				vertical-align:middle;
				}
		</style>
    </head>   

<body>
	<div class="mycontent">
		<div id="maintainRecords_table">
			<table class="table table-bordered table-striped"
				id="Records_table">
				<thead>
					<tr>
						<th>维修人</th>
						<th>教室</th>
						<th>时间</th>
						<th>设备名称</th>
						<th>维修情况</th>
					</tr>
				</thead>
				
				<%-- <s:iterator value="maintainRecords_list" var="item">
					<tr class="success" id='<s:property value="#item.id"/>'>
						<td><s:property value="#item.person" /></td>
						<td><s:property value="#item.classroom" /></td>
						<td><s:property value="#item.time" /></td>
						<td><s:property value="#item.equipmentName" /></td>
						<td><s:property value="#item.detail" /></td>
						</td>
					</tr>
				</s:iterator> --%>


			</table>
		</div>
	</div>

</body>
</html>>