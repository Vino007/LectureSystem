<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
   <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">编辑讲座</h4>
			</div>
				<form id="updateForm" action="lecture/update" method="post">
			<div class="modal-body">

					<input name="id" value="${lecture.id}" hidden="true"/>
					<div class="form-group">
						<label for="title" class="control-label">讲座:</label> <input
							type="text" class="form-control required" id="title" name="title"  value="${lecture.title}" >
					</div>					
					<div class="form-group">
						<label for="lecturer" class="control-label">主讲人:</label> <input
							type="text" class="form-control required" id="lecturer" name="lecturer" value="${lecture.lecturer}">
					</div>
					<div class="form-group">
						<label for="time" class="control-label">时间:</label> <input
							type="text" class="form-control required" id="time" name="time" value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.time}"/>">
					</div>
					<div class="form-group">
						<label for="address" class="control-label">地点:</label> <input
							type="text" class="form-control required" id="address" name="address" value="${lecture.address}">
					</div>
					<div class="form-group">
						<label for="maxPeopleNum" class="control-label">允许人数:</label> <input
							type="text" class="form-control required" id="maxPeopleNum" name="maxPeopleNum" value="${lecture.maxPeopleNum}">
					</div>
					<div class="form-group">
						<label for="reserveStartTime" class="control-label">预约开始时间:</label> <input
							type="text" class="form-control required" id="reserveStartTime" name="reserveStartTime" value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.reserveStartTime}"/>">
					</div>
					<div class="form-group">
						<label for="description" class="control-label">详情描述:</label> <input
							type="text" class="form-control required" id="description" name="description" value="${lecture.description}">
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="submit" class="btn btn-primary" id="updateSubmitBtn">提交</button>
			</div>
			</form>
<script>
/* https://github.com/RobinHerbots/jquery.inputmask/blob/3.x/README_date.md */
$("#time").inputmask("datetime");
$("#reserveStartTime").inputmask("datetime");
/* 异步提交表单及更新content */
$('#updateModal').on('shown.bs.modal', function(event) {
	$("#updateForm").validate({
		 submitHandler : function(form){			
			 $.ajax({
					async : false,
					cache : false,
					type : 'POST',
					data : $("#updateForm").serialize(),				
					url : "lecture/update",//请求的action路径  
					error : function() {//请求失败处理函数  
						alert('失败');
					},
					success : function(data) { //请求成功后处理函数。    
						alert("success");						
						$('#updateModal').on('hidden.bs.modal',function(event){//当modal框完全隐藏后再刷新页面content，要不然有bug
							$("#content-wrapper").html(data);//刷新content页面
						});
					}
				});
			 }
		});		
	});

</script>