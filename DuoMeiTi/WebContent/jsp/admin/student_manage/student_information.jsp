<%@ include file="/jsp/base/taglib.jsp" %>



<layout:override name="mycontent">



	<div class="mycontent">
	
	
	
	<br>
	<br>
	<form class="form-horizontal" id="student_search_form">
	<div class="form-horizontal">
        <div class="form-group">
        
            <label class="col-lg-2 control-label" style="margin:2px">学生查询：</label>
            <div class="col-lg-2">
                <select class="col-lg-2 form-control" style="margin:3px;"name="search_select" id="search_select">
                <option value="1">姓名</option>
                <option value="2">学号</option>
            </select>
            </div>
            
			<div class="col-lg-2">
				<input type="text" class="col-lg-3 form-control" style="margin:3px;height:34px;" aria-describedby="basic-addon1" name="name_id" id="name_id">
			</div>

			<div class="col-lg-1">
                <button type="button" class="btn btn-primary" style="margin:2px;" id="student_search" >查&nbsp;&nbsp;询</button>
            </div>
        </div>
    </div>
    </form>
	
	
	<div class="student_table">
	
		<table class="table table-bordered table-hover" id="student_information_table">
			
			<tr class="row" id="search_infor">
				<th class="col-lg-1.5">姓名</th>
				<th class="col-lg-1.5">学号</th>
				<th class="col-lg-1.5">学院</th>
				<th class="col-lg-1.5">电话</th>
				<th class="col-lg-1">权限</th>
				<th class="col-lg-1">编辑</th>
				<th class="col-lg-1">删除</th>
			</tr>
		
			<tr class="row" id="search_information" style="display: none;">
				<td class="col-lg-1.5" id="search_name"></td>
				<td class="col-lg-1.5" id="search_studentid"></td>
				<td class="col-lg-1.5" id="search_college"></td>
				<td class="col-lg-1.5" id="search_number"></td>
				<td class="col-lg-1" id="search_isUpgradePrivilege"></td>
				<td class="col-lg-1 ">
						<button type="button" class="btn btn-primary btn-sm edit" data-toggle="modal" data-target="#student_edit" id="edit-button" name="edit-button" >编辑</button>
					</td>
					<td class="col-lg-1 ">
						<button type="button" class="btn btn-primary btn-sm delete" id="delede-button">删除</button>	
					</td>
			</tr>
			
			
			<s:iterator value="student_list" var="i" status="index">
				<tr class="row" id=<s:property value="#i.id"/>>
					<td class="col-lg-1.5"> <s:property value="#i.user.fullName"/> </td>
					<td class="col-lg-1"> <s:property value="#i.studentId"/> </td>
					<td class="col-lg-1.5"> <s:property value="#i.college "/> </td>
					<td class="col-lg-1.5"> <s:property value="#i.user.phoneNumber"/> </td>
					<td class="col-lg-1.5"> 
						<s:if test="#i.isUpgradePrivilege == 0">在职学生</s:if>
						<s:else>管理教师</s:else>
					</td>
						
					<td class="col-lg-1 ">
						<button type="button" class="btn btn-primary btn-sm edit" data-toggle="modal" data-target="#student_edit" id="edit-button" name="edit-button" >编辑</button>
					</td>
					<td class="col-lg-1 ">
						<button type="button" class="btn btn-primary btn-sm delete" id="delede-button">删除</button>	
					</td>
				</tr>
					
			</s:iterator>
		</table>
		
	</div>	
		
		<div class="modal fade" id="student_edit">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h2 class="modal-title" id="modal-title">编辑学生信息</h2>
									</div>
									<div class="modal-body">
										<form class="form-inline well" id="edit_student_form" method="post">
											<div class="modal-body">
												<div class="row">
													<div class="col-lg-6">
														<div class="input-group">
															<span class="input-group-btn">
																<button type="button" class="btn btn-default">姓名</button>
															</span>
															<input type="text" class="form-control" id="fullName"  name="fullName" value="">
														</div>
													</div>
													<div class="col-lg-6">
														<div class="input-group">
															<span class="input-group-btn">
																<button type="button" class="btn btn-default">学号</button>
															</span>
															<input type="text" class="form-control" id="studentId"  name="studentId" value="">
														</div>
													</div>
													<div class="col-lg-6">
														<div class="input-group">
															<span class="input-group-btn">
																<button type="button" class="btn btn-default">班级</button>
															</span>
															<input type="text" class="form-control" id="college"  name="college" value="">
														</div>
													</div>
													<div class="col-lg-6">
														<div class="input-group">
															<span class="input-group-btn">
																<button type="button" class="btn btn-default">电话</button>
															</span>
															<input type="text" class="form-control" id="phoneNumber"  name="phoneNumber" value="">
														</div>
													</div>
													<div class="col-lg-6">
														<div class="input-group">
															<span class="input-group-btn">
																<button type="button" class="btn btn-default">权限</button>
															</span>
															<!-- <input type="text" class="form-control" id="isUpgradePrivilege"  name="isUpgradePrivilege" value=""> -->
															<s:select list="#{'0':'在职学生','1':'管理教师'}" class="form-control" name="isUpgradePrivilege" id="isUpgradePrivilege"></s:select>
														</div>
													</div>
												</div>
											</div>
										</form>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-primary" id="editSave">保存</button>
										<button type="button" class="btn btn-default" data-dismiss="modal" id="editClose">关闭</button>
									</div>
								</div>
								<!-- /.modal-content -->
							</div>
							<!-- /.modal-dialog -->
						</div>
		
		<script type='text/javascript' src="/js/admin/student_information.js"></script>
		
		
		
	</div>
	
	
</layout:override>


<%@ include file="base.jsp" %>