<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="vino" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>
		讲座管理 <small></small>
	</h1>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-dashboard"></i>系统管理</a></li>
		
		<li class="active">讲座管理</li>
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
						<form id="downloadForm" action="lecture/download" method="post" >
						<shiro:hasPermission name="lecture:create">
							<button id="addBtn" type="button"
								class="btn  btn-primary btn-flat margin" 
								 onclick="addItem()">
								<span class="fa fa-fw  fa-plus" aria-hidden="true"></span> 新增
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="lecture:delete">
							<button id="deleteBtn" type="button"
								class="btn  btn-danger btn-flat margin">
								<span class="fa fa-fw fa-remove" aria-hidden="true"></span> 删除
							</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="lecture:upload">
							<button id="uploadBtn" type="button"
								class="btn  btn-primary btn-flat margin" 
								 onclick="uploadItem()">
								<span class="fa fa-fw fa-cloud-upload" aria-hidden="true"></span> 上传
							</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="lecture:download">
							<button id="downloadBtn" type="submit"
								class="btn  btn-primary btn-flat margin" 
								 onclick="downloadItem()">
								<span class="fa fa-fw fa-cloud-download" aria-hidden="true"></span> 下载
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
							<th>标题</th>
							<th>主讲人</th>
							<th>时间</th>
							<th>地点</th>
							<th>允许人数</th>
							<th>当前人数</th>
						<!-- 	<th>创建时间</th>
							<th>创建人</th> -->
						
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
								<td>${lecture.maxPeopleNum}</td>
								<td>${lecture.currentPeopleNum}</td>
							<%-- 	<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.createTime}" /></td>
								<td>${lecture.creatorName}</td> --%>
								<c:choose>
									<c:when test="${lecture.available}">
										<td><span class="badge bg-red">可预约</span></td>
									</c:when>
									<c:otherwise>
										<td><span class="badge bg-green">不可预约</span></td>
									</c:otherwise>
								</c:choose>
								<td><shiro:hasPermission name="lecture:update">
										<button id="updateBtn" type="button"
											class="btn btn-xs btn-primary btn-flat"
											 onclick='updateItem(${lecture.id})'>编辑</button>
									</shiro:hasPermission> 
									<shiro:hasPermission name="lecture:view">
										<button id="detailBtn" type="button"
											class="btn  btn-xs btn-primary btn-flat " 
											 onclick='detailItem(${lecture.id})'>详情</button>
									</shiro:hasPermission> 
									
									<shiro:hasPermission name="attendance:downloadReserve">
										<form  style="display:inline;"  id="downloadReserveForm" action="attendance/downloadReserve" method="get">
										<input type="text" name="lectureId" value="${lecture.id}" hidden="true">
										<button  type="submit"
											class="btn  btn-xs btn-primary btn-flat " 
											 onclick="downloadReserveItem()">
											 下载预约清单
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

<!-- 删除确认页面 modal框 -->
<div class="modal fade" id="deleteConfirmModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">删除讲座</h4>
			</div>
			<div class="modal-body">
				<div>确定要删除吗？</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="deleteConfirmBtn">提交</button>
			</div>
		</div>
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
	//删除确认modal事件处理
	$('#deleteConfirmModal').on('shown.bs.modal', function(event) {
		$('#deleteConfirmBtn').click(function(){
			deleteItemsUseModal("input[class*='deleteCheckbox']","lecture/delete");
		});
	});
	/* button监听事件 */
	$(document).ready(function(){
		$("#deleteBtn").click(function(){
			$("#deleteConfirmModal").modal();	
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
	function addItem(){
		$('#modal .modal-content').load('lecture/prepareAdd',function(){
			$("#modal").modal();
		});
	}
	function updateItem(id){
		$('#modal .modal-content').load('lecture/'+id,function(){
			$("#modal").modal();
		});
		
	}
	
	function detailItem(id){
		$('#modal .modal-content').load('lecture/detail/'+id,function(){
			$("#modal").modal();
		});			
	}
	
	function uploadItem(){
		$('#modal .modal-content').load('lecture/prepareUpload',function(){
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