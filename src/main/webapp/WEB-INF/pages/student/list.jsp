<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="vino" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>
		学生管理<small></small>
	</h1>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-dashboard"></i>系统管理</a></li>

		<li class="active">学生管理</li>
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
								<h3 class="box-username">数据查询</h3>
							</div>
							<div class="box-body">
								<!-- form start -->
								<form id="searchForm" action="student/search" method="get">
									<div class="box-body">
										<div class="row">
											<input hidden="true" name="pageNumber" id="pageNumber">
											<div class="form-group col-md-2">
												<label for="usernameLabel">学号:</label> <input type="text"
													class="form-control" id="usernameLabel"
													name="search_username" value="${searchParamsMap.username }">
											</div>
											<div class="form-group col-md-2">
												<label for="nameLabel">姓名:</label> <input type="text"
													class="form-control" id="nameLabel" name="search_name"
													value="${searchParamsMap.name }">
											</div>
											<div class="form-group col-md-2">
												<label for="majorLabel">专业:</label> <input type="text"
													class="form-control" id="majorLabel" name="search_major"
													value="${searchParamsMap.major }">
											</div>
											<div class="form-group col-md-2">
												<label for="gradeLabel">年级:</label> <input type="text"
													class="form-control" id="gradeLabel" name="search_grade"
													value="${searchParamsMap.grade }">
											</div>


											<div class="form-group  col-md-3">
												<label>创建时间:</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input type="text"
														class="form-control pull-right dateRangePicker"
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
						<h3 class="box-username">讲座列表</h3>
					</div>
					<div class="btn-group">
						<!-- 注意，为了设置正确的内补（padding），务必在图标和文本之间添加一个空格。 -->
						<form id="downloadForm" action="student/download" method="post">
							<shiro:hasPermission name="student:create">
								<button id="addBtn" type="button"
									class="btn  btn-primary btn-flat margin" onclick="addItem()">
									<span class="fa fa-fw  fa-plus" aria-hidden="true"></span> 新增
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="student:delete">
								<button id="deleteBtn" type="button"
									class="btn  btn-danger btn-flat margin">
									<span class="fa fa-fw fa-remove" aria-hidden="true"></span> 删除
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="student:upload">
								<button id="uploadBtn" type="button"
									class="btn  btn-primary btn-flat margin" onclick="uploadItem()">
									<span class="fa fa-fw fa-cloud-upload" aria-hidden="true"></span>
									上传
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="student:download">
								<button id="downloadBtn" type="submit"
									class="btn  btn-primary btn-flat margin"
									onclick="downloadItem()">
									<span class="fa fa-fw fa-cloud-download" aria-hidden="true"></span>
									下载
								</button>
							</shiro:hasPermission>
							<input id="downloadIds" type="hidden" name="downloadIds[]">
						</form>
					</div>
					<table class="table table-hover center">
						<tr>
							<th style="width: 10px"><label> <input id="allCheck"
									type="checkbox" class="minimal" value="0">
							</label></th>
							<th style="width: 10px">#</th>
							<th>学号</th>
							<th>姓名</th>
							<th>专业</th>
							<th>年级</th>
							<th>创建时间</th>
							<th>创建人</th>
							<th >操作</th>

						</tr>
						<c:forEach items="${students}" var="student" varStatus="status">
							<tr>
								<td><label><input type="checkbox"
										class="minimal deleteCheckbox" value="${student.id}"></label></td>
								<td>${status.count}</td>
								<td>${student.username}</td>
								<td>${student.name}</td>
								<td>${student.major}</td>
								<td>${student.grade}</td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${student.createTime}" /></td>
								<td>${student.creatorName}</td>

								<td><shiro:hasPermission name="student:update">
										<button id="updateBtn" type="button"
											class="btn btn-xs btn-primary btn-flat"
											onclick='updateItem(${student.id})'>编辑</button>
									</shiro:hasPermission> <shiro:hasPermission name="student:view">
										<button id="detailBtn" type="button"
											class="btn  btn-xs btn-primary btn-flat"
											onclick='detailItem(${student.id})'>详情</button>
									</shiro:hasPermission></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<!-- /.box-body -->
				<!-- 分页 -->
				<vino:pagination paginationSize="10" page="${page}"
					action="student/search" contentSelector="#content-wrapper"></vino:pagination>
			</div>
			<!-- /.box -->
		</div>
	</div>
</section>
<!-- /.content -->

<!--  modal框 -->
<div class="modal fade" id="modal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>

<script>
	//Date range picker
	$('.dateRangePicker').daterangepicker();
	//Date range picker with time picker
	$('#reservationtime').daterangepicker({timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A'});
	
	/* icheck 初始化 详情：https://github.com/fronteed/icheck */
   	iCheckInit();
 	/* iCheck事件监听 详情：https://github.com/fronteed/icheck */
 	/* 全选和取消全选 */
	$(document).ready(function(){
		$('#allCheck').on('ifToggled', function(event){		
			$('input[class*="deleteCheckbox"]').iCheck('toggle');			
		});
		
	});
	/* button监听事件 */
	$(document).ready(function(){
		$("#deleteBtn").click(function(){
			deleteItems("input[class*='deleteCheckbox']","student/delete");
		});
		
	});		
	$("#searchBtn").click(function() {
		$('#pageNumber').val(1);
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			data : $("#searchForm").serialize(),		 
			url : "student/search",//请求的action路径  
			error : function() {//请求失败处理函数  
				alert('失败');
			},
			success : function(data) { //请求成功后处理函数。    
				$("#content-wrapper").html(data);//刷新content页面
			
			}
		});
	});
	
	function modalLoadAndDisplay(url){	
		$('#modal .modal-content').load(url,function(){
			$("#modal").modal();
		});		
	}
	function addItem(){
		modalLoadAndDisplay('student/prepareAdd');
	}
	
	function updateItem(id){	
		modalLoadAndDisplay('student/'+id);
	}
	
	function detailItem(id){
	
		modalLoadAndDisplay('student/detail/'+id);
	}
	
	function uploadItem(){	
		modalLoadAndDisplay('student/prepareUpload');
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