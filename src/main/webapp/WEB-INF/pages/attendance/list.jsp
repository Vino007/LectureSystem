<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="vino" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>
		考勤管理 <small></small>
	</h1>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-dashboard"></i>系统管理</a></li>
		
		<li class="active">考勤管理</li>
	</ol>
</section>
<!-- Main content -->
<section class="content">
	<div class="row">
		<!-- <div class="col-md-6"> -->
		<div class="box">
			<!-- /.box-header -->
			<div class="box-body">
				<div class="row">
					<div class="col-md-12">
						<div class="box box-primary">
							<div class="box-header with-border">
								<h3 class="box-title">数据查询</h3>
							</div>
							<div class="box-body">
								<!-- form start -->
								<form id="searchForm" action="lecture/search" method="get">
									<div class="box-body">
										<div class="row">
											<input hidden="true" name="pageNumber" id="pageNumber">
											<div class="form-group col-md-2">
												<label for="titleLabel">标题:</label> <input type="text"
													class="form-control" id="titleLabel"
													name="search_title" value="${searchParamsMap.title }">
											</div>
											<div class="form-group col-md-2">
												<label for="lecturerLabel">主讲人:</label> <input type="text"
													class="form-control" id="lecturerLabel"
													name="search_lecturer"
													value="${searchParamsMap.lecturer }">
											</div>
											<div class="form-group col-md-2">
												<label for="addressLabel">地点:</label> <input type="text"
													class="form-control" id="addressLabel"
													name="search_address"
													value="${searchParamsMap.address }">
											</div>
											
											<!-- Date range -->
											<div class="form-group  col-md-3">
												<label>讲座时间:</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input type="text" class="form-control pull-right dateRangePicker"
														 name="search_lectureTimeRange"
														value="${searchParamsMap.lectureTimeRange}">
												</div>
												<!-- /.input group -->
											</div>
											<div class="form-group  col-md-3">
												<label>创建时间:</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input type="text" class="form-control pull-right dateRangePicker"
														 name="search_createTimeRange"
														value="${searchParamsMap.createTimeRange}">
												</div>
												<!-- /.input group -->
											</div>
											<!-- <div class="form-group col-md-2">
												<label for="isLockedLabel" >是否锁定: </label><br>
												<input id="isLockedLabel" type="checkbox" name="search_locked">
											</div> -->

											<!-- /.form group -->
										</div>
										<!-- other rows -->
									</div>
									<!-- /.box-body -->
									<div class="box-footer">
										<button id="searchBtn" type="submit"
											class="btn btn-info pull-right">查询</button>
									</div>
									<!-- /.box-footer -->
								</form>
							</div>
							<!-- /.box-body -->
						</div>
						<!-- /.box -->
					</div>
					<!-- /.col (right) -->
				</div>
				<!-- /.row -->
				<div class="box box-primary">
					<div class="box-header with-border">
						<h3 class="box-title">讲座列表</h3>
					</div>
					<div class="btn-group">
						<!-- 注意，为了设置正确的内补（padding），务必在图标和文本之间添加一个空格。 -->										
																
					</div>
					<table class="table table-hover center">
						<tr>
							<th style="width: 10px"><label> <input id="allCheck"
									type="checkbox" class="minimal" value="0">
							</label></th>
							<th style="width: 10px">#</th>
							<th>标题</th>
							<th>主讲人</th>
							<th>时间</th>
							<th>地点</th>
							<th>操作</th><!-- 水平居中 -->

						</tr>
						<c:forEach items="${lectures}" var="lecture" varStatus="status">
							<tr>
								<td><label><input type="checkbox"
										class="minimal deleteCheckbox" value="${lecture.id}"></label></td>
								<td>${status.count}</td>
								<td>${lecture.title}</td>
								<td>${lecture.lecturer}</td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.time}"/></td>
								<td>${lecture.address}</td>
								<td>
									<shiro:hasPermission name="attendance:create">
										<button id="addBtn" type="button"
											class="btn  btn-xs btn-danger btn-flat margin" onclick="addItem(${lecture.id})">
											添加考勤
										</button>
									</shiro:hasPermission>
									<shiro:hasPermission name="attendance:delete">
										<button id="deleteBtn" type="button"
											class="btn  btn-xs btn-danger btn-flat margin" onclick="deleteItem(${lecture.id})">
											删除考勤
										</button>
									</shiro:hasPermission>		
									<shiro:hasPermission name="attendance:upload">
										<button  type="button"
											class="btn btn-xs btn-primary btn-flat margin " 
											onclick="uploadAttendanceItem(${lecture.id})">
											 上传考勤
										</button>
									</shiro:hasPermission>
										<shiro:hasPermission name="attendance:downloadAttendance">
										<form style="display:inline;" id="downloadAttendanceForm" action="attendance/downloadAttendance" method="get">
										<input type="text" name="lectureId" value="${lecture.id}" hidden="true">
										<button  type="submit"
											class="btn btn-xs btn-primary btn-flat margin " 
											 onclick="downloadAttendanceItem()">
											 下载考勤
										</button>
										</form>
									</shiro:hasPermission>
									
									</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<!-- /.box-body -->
				<!-- 分页 -->
				<vino:pagination paginationSize="10" page="${page}"
					action="lecture/search" contentSelector="#content-wrapper"></vino:pagination>
			</div>
			<!-- /.box -->
		</div>
	</div>
</section>
<!-- /.content -->

<!-- 新增页面 modal框 -->
<div class="modal fade" id="modal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			
		</div>
	</div>
</div>
<!-- ./新增页面 modal框 -->

<script>

	//Date range picker
	$('.dateRangePicker').daterangepicker();
	/* icheck 初始化 详情：https://github.com/fronteed/icheck */
   	iCheckInit();
 	/* iCheck事件监听 详情：https://github.com/fronteed/icheck */
 	/* 全选和取消全选 */
	$(document).ready(function(){
		$('#allCheck').on('ifToggled', function(event){		
			$('input[class*="deleteCheckbox"]').iCheck('toggle');			
		});		
	});
	$("#searchBtn").click(function() {
		$('#pageNumber').val(1);
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			data : $("#searchForm").serialize(),		 
			url : "lecture/search",//请求的action路径  
			error : function() {//请求失败处理函数  
				alert('失败');
			},
			success : function(data) { //请求成功后处理函数。    
				$("#content-wrapper").html(data);//刷新content页面
			
			}
		});
	});
	/* button监听事件 */
	function deleteItem(lectureId){
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			data : $.param({
				lectureId : lectureId
			}),		 
			url : "attendance/delete",//请求的action路径  
			error : function() {//请求失败处理函数  
				alert('失败');
			},
			success : function(data) { //请求成功后处理函数。    
				$("#content-wrapper").html(data);//刷新content页面
			}
		});
	}
	$("#searchBtn").click(function() {
		//$('#pageNumber').val(1);
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			data : $("#searchForm").serialize(),		 
			url : "lecture/search",//请求的action路径  
			error : function() {//请求失败处理函数  
				alert('失败');
			},
			success : function(data) { //请求成功后处理函数。    
				$("#content-wrapper").html(data);//刷新content页面
			}
		});
	});
	function addItem(lectureId){
		$('#modal .modal-content').load('attendance/prepareAdd?lectureId='+lectureId,function(){
			$("#modal").modal();
		});
	}	
	function detailItem(id){
		$('#modal .modal-content').load('attendance/detail/'+id,function(){
			$("#modal").modal();
		});			
	}

	 function uploadAttendanceItem(lectureId){
		 $('#modal .modal-content').load('attendance/prepareUpload?lectureId='+lectureId,function(){
				$("#modal").modal();
			});			
	} 
	/**
	AJAX不能下载文件，用表单来实现
	*/
	function downloadItem(){	
		var downloadIds = [];
		var i = 0;
		$("input[class*='deleteCheckbox']").each(function(index, item) {
			var isChecked = item.checked;
			if (isChecked == true) {
				downloadIds[i++] = item.value;
			}
		});
		$('#downloadIds').val(downloadIds)
		$('#downloadForm').submit(function(){
			
		});
	}		
</script>