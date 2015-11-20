package com.vino.lecture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vino.lecture.entity.Attendance;
import com.vino.scaffold.repository.base.BaseRepository;

public interface AttendanceRepository extends BaseRepository<Attendance, Long> {
	@Query("from Attendance a where a.lectureId=:lectureId")
	public List<Attendance> findAttendanceByLectureId(@Param("lectureId")Long lectureId);
}
