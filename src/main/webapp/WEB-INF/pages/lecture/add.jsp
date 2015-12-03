<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">新增讲座</h4>
			</div>
			<form id="addForm" action="lecture/add" method="post">
			<div class="modal-body">
					<div class="form-group">
						<label for="title" class="control-label"><font color="red">*</font>标题:</label> <input
							type="text" class="form-control " id="title"
							name="title">
					</div>
					<div class="form-group">
						<label for="lecturer" class="control-label"><font color="red">*</font>主讲人:</label> <input
							type="text" class="form-control " id="lecturer"
							name="lecturer">
					</div>
					<div class="form-group">
						<label for="time" class="control-label"><font color="red">*</font>时间:</label> <input
							type="text" class="form-control " id="time"
							name="time">
					</div>
					<div class="form-group">
						<label for="reserveStartTime" class="control-label"><font color="red">*</font>预约开始时间:</label> <input
							type="text" class="form-control" id="reserveStartTime"
							name="reserveStartTime">
					</div>
					<div class="form-group">
						<label for="address" class="control-label"><font color="red">*</font>地点:</label> <input
							type="text" class="form-control" id="address"
							name="address">
					</div>
					<div class="form-group">
						<label for="maxPeopleNum" class="control-label"><font color="red">*</font>允许人数:</label> <input
							type="text" class="form-control" id="maxPeopleNum"
							name="maxPeopleNum">
					</div>
					
					<div class="form-group">
						<label for="description" class="control-label"><font color="red">*</font>详情介绍:</label> <textarea
							class="form-control" id="description"
							name="description"></textarea>
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
				 rules:{
					 title:{ //格式：domId: 规则
						 minlength:2,//无效
						 maxlength:50,
						 required:true							 	
						 },
					 lecturer:{ //格式：domId: 规则
						 minlength:2,//无效
						 maxlength:30,
						 required:true						 	
							 },
					 time:"required",
					 address:"required",
					 reserveStartTime:"required",//预约开始时间要早于讲座时间
					 description:"required",
					 maxPeopleNum:{
						 min:1,//无效
						 max:999,
						 required:true,
						 digits:true	
						 }
				 },
				 messages:{					
					 maxPeopleNum:{
						 max: jQuery.validator.format("请输入一个最大为{0} 的数"),
						 min: jQuery.validator.format("请输入一个最小为{0} 的数"),						
						 digits: "只能输入整数"
					
					 }
				 },
				 submitHandler : function(form){
			           	$.ajax({
							async : false,
							cache : false,
							type : 'POST',
							data :  $("#addForm").serialize(),
						   // contentType : 'application/json',    //发送信息至服务器时内容编码类型
							//dataType : "json",
							url : "lecture/add",//请求的action路径  
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
/* $('#modal').on('hidden.bs.modal', function(event){
	$('#modal .modal-content').empty();
	console.log("hidden");
});	
$('#modal').on('hidden.bs.modal', function(event){
	console.log("hidde");
	$(this).removeData("bs.modal");
});	 */
//Datemask dd/mm/yyyy
/* https://github.com/RobinHerbots/jquery.inputmask/blob/3.x/README_date.md */
$("#time").inputmask("datetime");
$("#reserveStartTime").inputmask("datetime");
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