package com.vino.lecture.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vino.lecture.entity.Attendance;
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
		
}
