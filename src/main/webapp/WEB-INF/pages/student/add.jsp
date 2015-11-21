<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- <div id="requestResult" class="alert alert-danger"></div> -->
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="exampleModalLabel">新增学生</h4>
</div>
<form id="addForm" action="student/add" method="post">
	<div class="modal-body">
		<div class="form-group">
			<label for="username" class="control-label"><font color="red">*</font>学号:</label>
			<input type="text" class="form-control required " id="username"
				name="username">
		</div>
		<div class="form-group">
			<label for="name" class="control-label"><font color="red">*</font>姓名:</label>
			<input type="text" class="form-control required " id="name"
				name="name">
		</div>
		<div class="form-group">
			<label for="major" class="control-label"><font color="red">*</font>专业:</label>
			<input type="text" class="form-control required " id="major"
				name="major">
		</div>
		<div class="form-group">
			<label for="grade" class="control-label"><font color="red">*</font>年级:</label>
			<input type="text" class="form-control required " id="grade"
				name="grade">
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button type="submit" class="btn btn-primary" id="addSubmitBtn">提交</button>
	</div>
</form>
<script>
/*modal框事件监听 详情：http://v3.bootcss.com/javascript/#modals-events */
$('#modal').on('shown.bs.modal', function(event) {	
			$("#title").focus();
			 $("#addForm").validate({
				 submitHandler : function(form){
			           	$.ajax({
							async : false,
							cache : false,
							type : 'POST',
							data :  $("#addForm").serialize(),
						   // contentType : 'application/json',    //发送信息至服务器时内容编码类型
							//dataType : "json",
							url : "student/add",//请求的action路径  
							error : function() {//请求失败处理函数  
								alert('失败');
							},
							success : function(data) { //请求成功后处理函数。
								
								alert("success");
								$('#modal').on('hidden.bs.modal',function(event){//当modal框完全隐藏后再刷新页面content，要不然有bug
									$("#content-wrapper").html(data);//刷新content页面
								});
							}
						});
			        }    
			    });
	});
	
//Datemask dd/mm/yyyy
/* https://github.com/RobinHerbots/jquery.inputmask/blob/3.x/README_date.md */

	/* js校验 */
	/* 
	$().ready(function() {
 $("#signupForm").validate({
        rules: {
   firstname: "required",
   email: {
    required: true,
    email: true
   },
   password: {
    required: true,
    minlength: 5
   },
   confirm_password: {
    required: true,
    minlength: 5,
    equalTo: "#password"
   }
  },
        messages: {
   firstname: "请输入姓名",
   email: {
    required: "请输入Email地址",
    email: "请输入正确的email地址"
   },
   password: {
    required: "请输入密码",
    minlength: jQuery.format("密码不能小于{0}个字 符")
   },
   confirm_password: {
    required: "请输入确认密码",
    minlength: "确认密码不能小于5个字符",
    equalTo: "两次输入密码不一致不一致"
   }
  }
    });
});
	
	*/
</script>