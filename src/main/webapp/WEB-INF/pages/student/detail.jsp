<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">学生详情</h4>
			</div>
			<div class="modal-body">
				<table  class="table table-striped">
				<tr><td>学号:</td><td>${student.username}	</td></tr>
				<tr><td>姓名:</td><td>${student.name}</td></tr>
				<tr><td>专业:</td><td>${student.major}</td></tr>
				<tr><td>年级:</td><td>${student.grade}</td></tr>
				<tr><td>性别:</td><td>${student.gender}	</td></tr>
				<tr><td>生日:</td><td><fmt:formatDate pattern="yyyy-MM-dd"
										value="${student.birthday}"/>	</td></tr>
				<tr><td>创建时间:</td><td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
										value="${student.createTime}"/></td></tr>
				<tr><td>创建人:</td><td>${student.creatorName}	</td></tr>
				</table>																										
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>			
			</div>