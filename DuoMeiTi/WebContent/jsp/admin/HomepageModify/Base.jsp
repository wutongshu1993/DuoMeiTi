<%@ include file="/jsp/base/taglib.jsp" %>



<layout:override name="main_content">
	

    <div class="mynavbar">
        <div class="container-fluid nopadding">
		    <div class="row nomargin">
		        <div class="col-lg-3 nopadding">
		           <a href="/admin/HomepageModify_FileUpload" class="navbar-button navbar-button-left">文件上传</a> 
		        </div>
		        <div class="col-lg-3 nopadding">
		           <a href="/admin/file_upload" class="navbar-button">课程成绩查询</a> 
		        </div>
		        <div class="col-lg-3 nopadding">
		           <a href="/admin/file_upload" class="navbar-button">学生成绩查询</a> 
		        </div>
		
		        <div class="col-lg-3 nopadding">
		           <a href="/admin/file_upload" class="navbar-button navbar-button-right">学生成绩搜索</a> 
		        </div>
		    </div>
		</div>

    </div>
    <div class="mycontent">

        	
        	<layout:block name="mycontent">
        		
        	</layout:block>
    </div>
	
	<script>
// 		alert("SBSB");
	</script>
	

</layout:override>








<%@ include file="/jsp/admin/base.jsp" %>