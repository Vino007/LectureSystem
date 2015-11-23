<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="vino" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!-- Content Header (Page header) -->
<section class="content-header">
	<h1>讲座预约 </h1>
	
</section>
<!-- Main content -->
<section class="content">
	<div class="row">
		<div class="col-md-12">
			<!-- The time line -->
			<ul class="timeline">
				
				<c:forEach items="${lectures}" var="lecture">
				<!-- timeline time label -->
				
				<li class="time-label">
					<span class="bg-red">
						<fmt:formatDate pattern="yyyy-MM-dd"
										value="${lecture.time}"/>					
					</span>
				</li>
				<!-- /.timeline-label -->
				<!-- timeline item -->
				<li>
					<i class="fa fa-envelope bg-blue"></i>
					<div class="timeline-item">
						<span class="time"><i class="fa fa-clock-o"></i>发布时间:<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.createTime}"/></span> <!-- 右上角显示时间 -->
						<h3 class="timeline-header">${lecture.title}</h3><!-- 标题 -->
						<div class="timeline-body"><!-- 内容 -->
							<table class="table table-hover center">
								<tr><td>主讲人</td><td>${lecture.lecturer}</td></tr>
								<tr><td>时间</td><td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.time}"/></td></tr>
								<tr><td>预约开始时间</td><td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.reserveStartTime}"/></td></tr>
								<tr><td>地点</td><td>${lecture.address}</td>	</tr>						
								<tr><td>允许人数</td><td>${lecture.maxPeopleNum}</td></tr>
								<tr><td>现有人数</td><td id="currentPeopleNum">${lecture.currentPeopleNum}</td></tr>
								<tr><td>详情</td><td>${lecture.description}</td></tr>
							</table>
						</div>
						<div class="timeline-footer"><!-- footer，用来放置button -->
							<a id="reserveBtn"  class="btn btn-primary " onclick="reserveItem(${lecture.id})">预约</a>
							<a id="cancelReservationBtn" class="btn btn-danger " onclick="cancelReservationItem(${lecture.id})">取消</a>
						</div>
					</div>
				</li>
				</c:forEach>
				<!-- END timeline item -->				
				<li>
					<i class="fa fa-clock-o bg-gray"></i>
				</li>
			</ul>
		</div><!-- /.col -->
	</div><!-- /.row -->
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

	function reserveItem(lectureId){
		
		var requestData={lectureId,lectureId};
		$.post("student/lecture/reserve",requestData,function(data,status){
			var result=data.result;		
			if(result=="reserveAlready"){
				alert("已经预约过了");
			}else if(result=="success"){
				alert("预约成功");
				$('#currentPeopleNum').text(data.currentPeopleNum);
			}else if(result=="timeNotArrived"){
				alert("预约时间未到");
			}else if(result=="fullPeople"){
				alert("人数已满");
			}
			else{
				alert("失败");
			}
		},"json");
	}
	function cancelReservationItem(lectureId){
		var requestData={lectureId,lectureId};
		$.post("student/lecture/cancelReservation",requestData,function(data,status){
			var result=data.result;		
			if(result=="success"){
				alert("取消成功");
				$('#currentPeopleNum').text(data.currentPeopleNum);
			}else if(result=="attendanceNotExist"){
				alert("您没有预约该讲座,无法取消");
			}
			else{
				alert("失败");
			}
		},"json");
	}
</script>