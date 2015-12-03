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
				<thead><tr><th>学号</th><th>出席</th></tr></thead>
				<tbody>
					<c:forEach var="attendanceInfo" items="${attendanceInfos}">
					<tr><td>${attendanceInfo.username}</td><td>${attendanceInfo.attended}</td></tr>
					</c:forEach>
					
				</tbody>
				</table>																										
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>			
			</div>