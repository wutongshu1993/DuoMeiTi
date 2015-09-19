<%@ include file="/jsp/base/taglib.jsp"%>

<layout:override name="main_content">
	<div class="mycontent">

		<div><button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#rtModal" id="rtInsert" name="rtInsert">添加设备信息</button></div>
		<div class="modal fade" id="rtModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h2 class="modal-title" id="modal-title">添加设备信息</h2>
					</div>
					<form class="form-inline well" id="repertory_form" method="post">
						<div class="modal-body">
						
							<div class="row">
							<div class="col-lg-6">
								<div class="input-group">
									<span class="input-group-btn">
										<button type="button" class="btn btn-default">设备类型</button>
									</span>
									<select class="form-control" name="rtType" id="rtType" placeholder="请选择" value="<s:property value="rtType"/>">
										<option value="" selected="selected"></option>
										<option value="中控">中控</option>
										<option value="功放">功放</option>
										<option value="计算机主机">计算机主机</option>
										<option value="投影机">投影机</option>
										<option value="显示器">显示器</option>
										<option value="机柜">机柜</option>
										<option value="幕布">幕布</option>
										<option value="麦克">麦克</option>
										<option value="数字处理器">数字处理器</option>
									</select>
								</div>
								<div id="alert-bar" style="color: red"></div>
							</div>
							<div class="col-lg-6">
								<div class="input-group">
									<span class="input-group-btn">
										<button type="button" class="btn btn-default">资产编号</button>
									</span>
									<input type="text" class="form-control" name="rtNumber" value="<s:property value="rtNumber"/>">
								</div>
								<div id="alert-bar" style="color: red"></div>
							</div>
							</div>
							
							<div class="row">
							<div class="col-lg-6">
								<div class="input-group">
									<span class="input-group-btn">
										<button type="button" class="btn btn-default">型号</button>
									</span>
									<input type="text" class="form-control" name="rtVersion" value="<s:property value="rtVersion"/>">
								</div>
								<div id="alert-bar" style="color: red"></div>
							</div>
							<div class="col-lg-6">
								<div class="input-group">
									<span class="input-group-btn">
										<button type="button" class="btn btn-default">出厂号</button>
									</span>
									<input type="text" class="form-control" name="rtFactorynum" value="<s:property value="rtFactorynum"/>">
								</div>
								<div id="alert-bar" style="color: red"></div>
							</div>
							</div>
						
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" id="rtSave">保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal" id="rtClose">关闭</button>
						</div>
					</form>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->

		<!-- <div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				全部设备<span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li>主要设备</li>
				<li>耗材设备</li>
			</ul>
		</div> -->
		<div class="form-group">
			<form id="repertory_search" name="repertory_search" method="post">
				<div class="col-lg-3">
					<select class="form-control" name="rtDevice" id="rtDevice">
						<option value="">全部设备</option>
						<option value="main">主要设备</option>
						<option value="cost">耗材设备</option>
					</select>
				</div>
				<input type="text" class="" name="rtMainDevice" id="rtMainDevice" placeholder="设备">
				<button type="button" class="btn btn-success" id="rtSearch">查询</button>
			</form>
		</div>

		<table class="table table-bordered table-hover" id="repertory_table">
			<tr class="active">
				<th>设备类型</th>
				<th>资产编号</th>
				<th>型号</th>
				<th>出厂号</th>
					<!-- <th>型号</th>
					<th>出厂日期</th>
					<th>审批日期</th>
					<th>出厂号</th>
					<th>使用状态</th>
					<th>编辑</th>
					 -->
				<th>删除</th>
			</tr>
			
			<s:iterator value="repertory_list" var="i" status="index">
				<tr class="success" rt_id="<s:property value="#i.rtId"/>">
					<td> <s:property value="#i.rtType"/> </td>
					<td> <s:property value="#i.rtNumber"/> </td>
					<td> <s:property value="#i.rtVersion"/> </td>
					<td> <s:property value="#i.rtFactorynum"/> </td>
					<td> <button type="button" class="btn btn-danger delete" >删除</button> </td>
				</tr>
			</s:iterator>
			
			
		</table>
		
		
		<script>
		$(document).on("click","#rtInsert",function() {
			$("[name=rtType]").val("");
			$("[name=rtNumber]").val("");
			$("[name=rtVersion]").val("");
			$("[name=rtFactorynum]").val("");
		})
		$(document).on("click","#rtSave", function(){
			if($("#rtType").val() == ""){
				alert("输入不能为空！");
				return ;
			}
			
			var params = $("#repertory_form").serialize();//序列化表单值→ Json；
			//ajax方法通过HTTP请求加载远程数据； 
			$.ajax({
		    	url: 'repertory_insert',
		        type: 'post',
		        dataType: 'json',
		        data: params,
		        success: repertoryCallback
		    });
			
		})
		
		function repertoryCallback(data){
			if(data.status == "1"){
				$("#repertory_table tr:first").after(data.add_repertory_html);
	        	
	        	var cnt = $(document).find("#repertory_table tr:eq(1)");
	        	$(cnt).children().eq(0).text(data.rtType);
	        	$(cnt).children().eq(1).text(data.rtNumber);
	        	$(cnt).children().eq(2).text(data.rtVersion);
	        	$(cnt).children().eq(3).text(data.rtFactorynum);
	        	//alert(data.rtId+",  "+data.rtType+",  "+data.rtNumber);
	        	$(cnt).attr("rt_id", data.rtId);
	        	$('#rtModal').modal('hide');
	        	alert("保存成功！ ");
	      
			}
		}
		
		var delete_rtId;
		$(document).on("click",".delete", function(){
			var temp = confirm("删除不可恢复！");
			if(temp == true) {
				delete_rtId = $(this).parents("tr").attr("rt_id");//attr所选元素属性值 
				$.ajax({
					url: 'repertory_delete',
				    type: 'post',
				    dataType: 'json',
				    data: {"rtId": delete_rtId,},//{"后台",""}
					success: deleteCallback
				});
			}
		})
		
		function deleteCallback(data)
		{
			if(data.status == "0")
			{
				alert("删除数据不存在！ ");
			}
			else if(data.status == "1")
			{
				$(document).find("tr[rt_id=" + delete_rtId + "]").remove();
			}
		}
		
		$(document).on("click","#rtSearch", function()
		{
			//alert($("#rtDevice option:selected").attr("value"));
			if($("#rtDevice option:selected").attr("value") == ""){
				return;
			}
			var keyword = $("#repertory_search").serialize();
			$.ajax({
			    url: 'repertory_search',
			    type: 'post',
			    dataType: 'json',
			    data: keyword,
			    success: searchCallback
			});
		})
		
		function searchCallback(data) {
			if(data.status == "1"){
				$("#repertory_table tr:not(:first)").remove();
				//$("#repertory_table tr:first").after(data.add_repertory_html);
				var everylist = data.rtSearch_list;
				$(everylist).each(function(i) {
					$("#repertory_table").append(data.add_repertory_html);
					var row = $(document).find("#repertory_table tr:eq("+ (i + 1) +")");
					//alert(everylist[i].rtType + "," + everylist[i].rtVersion);
		        	$(row).find("td:eq(0)").text(everylist[i].rtType);
		        	$(row).find("td:eq(1)").text(everylist[i].rtNumber);
		        	$(row).find("td:eq(2)").text(everylist[i].rtVersion);
		        	$(row).find("td:eq(3)").text(everylist[i].rtFactorynum);
		        	$(row).attr("rt_id", everylist[i].rtId);
				})
				
			}else if(data.status == "0") {
				$("#repertory_table tr:not(:first)").remove();
				$("#repertory_table tr:first").after("无查询结果");
			}
		}
		
		/* $("#repertory_table tr:not(:first)").dblclick(alert("Hello!")) */
		
		
		
		
		</script>
		<s:debug></s:debug>
	</div>
</layout:override>

<%@ include file="/jsp/admin/base.jsp"%>