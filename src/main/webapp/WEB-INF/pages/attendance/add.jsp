<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">新增考勤</h4>
			</div>
			<form id="addForm" action="attendance/add" method="post">
			<div class="modal-body">
					
					<div class="form-group">
						<label for="username" class="control-label"><font color="red">*</font>学号:</label> <input
							type="text" class="form-control" id="username"
							name="username">
					</div>	
					<input type="text" name="lectureId" value="${lectureId}" hidden="true">							
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
					 username:{ //格式：domId: 规则
						 minlength:14,//无效
						 maxlength:14,
						 required:true,	
						 digits:true
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
							url : "attendance/add",//请求的action路径  
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

</script>