<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">讲座详情</h4>
			</div>
			<div class="modal-body">
				<table  class="table table-striped">
				<tr><td>标题:</td><td>${lecture.title}	</td></tr>
				<tr><td>主讲人:</td><td>${lecture.lecturer}</td></tr>
				<tr><td>时间:</td><td><fmt:formatDate pattern="yyyy-MM-dd HH:mm"
										value="${lecture.time}"/></td></tr>
				<tr><td>地点:</td><td>${lecture.address}</td></tr>
				<tr><td>允许人数:</td><td>${lecture.maxPeopleNum}	</td></tr>
				<tr><td>当前人数:</td><td>${lecture.currentPeopleNum}	</td></tr>
				<tr><td>预约开始时间:</td><td><fmt:formatDate pattern="yyyy-MM-dd HH:mm"
										value="${lecture.reserveStartTime}"/></td></tr>
				<tr><td>详情描述:</td><td>${lecture.description}	</td></tr>
				<c:choose>
					<c:when test="${lecture.available}">
						<td>状态:</td><td><span class="badge bg-green">可预约</span></td>
					</c:when>
					<c:otherwise>
						<td>状态:</td><td><span class="badge bg-red">不可预约</span></td>
					</c:otherwise>
				</c:choose>
				<tr><td>创建时间:</td><td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${lecture.createTime}"/></td></tr>
				<tr><td>创建人:</td><td>${lecture.creatorName}	</td></tr>
				</table>																										
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>			
			</div>