<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<section class="content-header">
	<h1>我的讲座</h1>

</section>
<section class="content">
	<div class="row">
		<div class="box box-primary">
			<div class="box-header with-border">
				<h3 class="box-title">修改信息</h3>
			</div>
			<!-- /.box-header -->
			<form id="updateForm" action="student/update" method="post">
				<div class="box-body">

					<input name="studentId" value="${student.id}" hidden="true" />
					
					<div class="form-group">
						<label for="oldPassword" class="control-label required">原密码:</label>
						<input type="password" class="form-control" id="oldPassword"
							name="oldPassword">
					</div>
					<div class="form-group">
						<label for="newPassword" class="control-label required">新密码:</label>
						<input type="password" class="form-control" id="newPassword"
							name="newPassword">
					</div>
					<div class="form-group">
						<label for="newPassword2" class="control-label required">新密码:</label>
						<input type="password" class="form-control" id="newPassword2"
							name="newPassword2">
					</div>


				</div>
				<!-- /.box-body -->
				<div class="box-footer">
					<button type="submit" class="btn btn-primary" id="updateSubmitBtn">提交</button>
				</div>
			</form>
		</div>
		<!-- /.box -->
	</div>
</section>
<script>
	/* 异步提交表单及更新content */
	$("#updateForm").validate({
		 rules:{
			 oldPassword:{
				 required:true,
				 minlength:4,
				 maxlength:30,				
			 },	
			 newPassword:{
				 required:true,
				 minlength:4,
				 maxlength:30,				
			 },			
			 newPassword2:{
				 required:true,
				 minlength:4,
				 maxlength:30,
				 equalTo:"#newPassword"
			 }
				 
		 },
		 messages:{		//没有设置messages的按照默认的提示	 			
		 	newPassword2:{
		 		 equalTo: "两次输入密码不一致"
		 	}
		 },
		submitHandler : function(form) {
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				data : $("#updateForm").serialize(),
				url : "student/alterPassword",//请求的action路径  
				dataType:'json',
				error : function() {//请求失败处理函数  
					alert('失败');
				},
				success : function(data) { //请求成功后处理函数。    				
					if(data.result=="oldPasswordError"){
						alert("旧密码错误");
					}else if(data.result=="newPasswordError"){
						alert("两次输入的新密码不一致");
					}else if(data.result=="success"){
						alert("修改密码成功");
					}
						
					
				}
			});
		}
	});
	
</script>