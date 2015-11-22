package com.vino.lecture.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vino.lecture.entity.Attendance;
import com.vino.lecture.exception.AttendanceDuplicateException;
import com.vino.lecture.exception.AttendanceNotExistException;
import com.vino.scaffold.service.base.BaseService;

public interface AttendanceService extends BaseService<Attendance, Long>{
	public void update(Attendance attendance);

	public List<Attendance> findAttendanceByLectureId(Long lectureId);
	public void saveWithCheckDuplicate(List<Attendance> attendances) throws AttendanceDuplicateException;
	public void saveWithCheckDuplicate(Attendance attendance) throws AttendanceDuplicateException;
	public void deleteAttendance(Long lectureId);	
//	public void deleteAttendanceByLectureIdAndStudentId(Long lectureId,Long studentId);
	public void cancelReservation(Long lectureId,Long studentId) throws AttendanceNotExistException;
}
