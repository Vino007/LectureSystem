<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
   <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-username" id="exampleModalLabel">编辑学生</h4>
			</div>
				<form id="updateForm" action="student/update" method="post">
			<div class="modal-body">

					<input name="id" value="${student.id}" hidden="true"/>
					<div class="form-group">
						<label for="username" class="control-label">学号:</label> <input
							type="text" class="form-control" id="username" name="username" disabled="disabled"  value="${student.username}" >
					</div>					
					<div class="form-group">
						<label for="name" class="control-label">姓名:</label> <input
							type="text" class="form-control required" id="name" name="name" value="${student.name}">
					</div>
					
					<div class="form-group">
						<label for="major" class="control-label">专业:</label> <input
							type="text" class="form-control required" id="major" name="major" value="${student.major}">
					</div>
					<div class="form-group">
						<label for="grade" class="control-label">年级:</label> <input
							type="text" class="form-control required" id="grade" name="grade" value="${student.grade}">
					</div>
					<div class="form-group">
						<label for="birthday" class="control-label">生日:</label> <input
							type="text" class="form-control" id="birthday" name="birthday" value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${student.birthday}"/>">
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="submit" class="btn btn-primary" id="updateSubmitBtn">提交</button>
			</div>
			</form>
<script>
/* 异步提交表单及更新content */
$('#updateModal').on('shown.bs.modal', function(event) {
	$("#updateForm").validate({
		 submitHandler : function(form){			
			 $.ajax({
					async : false,
					cache : false,
					type : 'POST',
					data : $("#updateForm").serialize(),				
					url : "student/update",//请求的action路径  
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