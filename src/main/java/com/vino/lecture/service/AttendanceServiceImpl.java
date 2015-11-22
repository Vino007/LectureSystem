package com.vino.lecture.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vino.lecture.entity.Attendance;
import com.vino.lecture.exception.AttendanceDuplicateException;
import com.vino.lecture.exception.AttendanceNotExistException;
import com.vino.lecture.repository.AttendanceRepository;
import com.vino.scaffold.service.base.AbstractBaseServiceImpl;
@Service("attendanceService")
public class AttendanceServiceImpl extends AbstractBaseServiceImpl<Attendance, Long> implements AttendanceService{
	@Autowired
	private AttendanceRepository attendanceRepository;
	@PersistenceContext
	private EntityManager em;
	@Override
	public List<Attendance> findAttendanceByLectureId(Long lectureId) {
		// TODO Auto-generated method stub
		return attendanceRepository.findAttendanceByLectureId(lectureId);
	}
	@Override
	public void saveWithCheckDuplicate(List<Attendance> attendances) throws AttendanceDuplicateException {
		if(attendances==null||attendances.size()==0)
			return;
		for(Attendance a:attendances){
			//使用lectureId与studentId来确定是否重复
			Attendance temp=attendanceRepository.findAttendanceByLectureIdAndStudentId(a.getLectureId(), a.getStudentId());
			if(temp!=null){
				throw new AttendanceDuplicateException();//发现重复，抛出异常
				
			}
		}
		//没有重复才保存
		attendanceRepository.save(attendances);
		
	}
	@Override
	public void saveWithCheckDuplicate(Attendance attendance) throws AttendanceDuplicateException {
		if(attendance==null)
			return;
		Attendance temp=attendanceRepository.findAttendanceByLectureIdAndStudentId(attendance.getLectureId(), attendance.getStudentId());
		if(temp!=null)
			throw new AttendanceDuplicateException();
		else{
			attendanceRepository.save(attendance);
		}
		
	}
	@Override
	public void deleteAttendance(Long lectureId) {
		attendanceRepository.deleteAttendanceByLectureId(lectureId);
		
	}
	
	@Override
	public void cancelReservation(Long lectureId, Long studentId) throws AttendanceNotExistException {
		// TODO Auto-generated method stub
		Attendance attendance=attendanceRepository.findAttendanceByLectureIdAndStudentId(lectureId, studentId);
		if(attendance==null){
			throw new AttendanceNotExistException();
		}else{
			attendanceRepository.deleteAttendanceByLectureIdAndStudentId(lectureId, studentId);
		}
	}
		
}
