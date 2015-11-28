<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
   
	<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">编辑资源</h4>
			</div>
			<div class="modal-body">

				
				
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="submit" class="btn btn-primary" id="updateSubmitBtn">提交</button>
			</div>
			
<script>
/* 异步提交表单及更新content */
$('#modal').on('shown.bs.modal', function(event) {
	$('#confirmBtn').click(function(){
		deleteItems("input[class*='deleteCheckbox']","lecture/delete");
	});
});

			

</script>