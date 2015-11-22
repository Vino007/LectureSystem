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

		<li class="active">用户管理</li>
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

						</tr>
						<c:forEach items="${lectures}" var="lecture" varStatus="status">
							<tr>
								<td>${status.count}</td>
								<td>${lecture.title}</td>
								<td>${lecture.lecturer}</td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.time}" /></td>
								<td>${lecture.address}</td>
								<td>${lecture.maxPeopleNum}</td>

							</tr>
						</c:forEach>
					</table>
				</div>
				<!-- /.box-body -->
				<!-- 分页 -->
				<%-- 	<vino:pagination paginationSize="10" page="${page}"
					action="student/lecture/search" contentSelector="#content-wrapper"></vino:pagination> --%>
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
		<div class="modal-content"></div>
	</div>
</div>
<!-- ./新增页面 modal框 -->



<script>

	//Date range picker
	$('.dateRangePicker').daterangepicker();
	
	
	
	
	function detailItem(id){
		$('#modal .modal-content').load('lecture/detail/'+id,function(){
			$("#modal").modal();
		});			
	}
	
</script>