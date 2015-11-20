package com.vino.lecture.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vino.lecture.entity.Attendance;
import com.vino.scaffold.service.base.BaseService;

public interface AttendanceService extends BaseService<Attendance, Long>{
	public void update(Attendance attendance);

	public List<Attendance> findAttendanceByLectureId(Long lectureId);

		
}
