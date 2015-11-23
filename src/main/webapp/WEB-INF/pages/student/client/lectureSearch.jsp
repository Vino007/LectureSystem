<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="vino" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>讲座查询</h1>	
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
					
					<table class="table table-hover center">
						<tr>
						
							<th style="width: 10px">#</th>
							<th>标题</th>
							<th>主讲人</th>
							<th>时间</th>
							<th>地点</th>
							<th>允许人数</th>
							<th>操作</th>
						</tr>
						<c:forEach items="${lectures}" var="lecture" varStatus="status">
							<tr>							
								<td>${status.count}</td>
								<td>${lecture.title}</td>
								<td>${lecture.lecturer}</td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.time}"/></td>
								<td>${lecture.address}</td>
								<td>${lecture.maxPeopleNum}</td>
								<td><a  class="btn btn-primary btn-xs " onclick="detailItem(${lecture.id})">详情</a></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<!-- /.box-body -->
				<!-- 分页 -->
				<vino:pagination paginationSize="10" page="${page}"
					action="student/lecture/search" contentSelector="#content-wrapper"></vino:pagination>
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
	
	
	$("#searchBtn").click(function() {
		$('#pageNumber').val(1);
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			data : $("#searchForm").serialize(),		 
			url : "student/lecture/search",//请求的action路径  
			error : function() {//请求失败处理函数  
				alert('失败');
			},
			success : function(data) { //请求成功后处理函数。    
				$("#content-wrapper").html(data);//刷新content页面
			
			}
		});
	});	
	function detailItem(id){
		$('#modal .modal-content').load('student/lecture/detail/'+id,function(){
			$("#modal").modal();
		});			
	}
	

</script>