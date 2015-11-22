package com.vino.lecture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vino.lecture.entity.Attendance;
import com.vino.scaffold.repository.base.BaseRepository;

public interface AttendanceRepository extends BaseRepository<Attendance, Long> {
	@Query("from Attendance a where a.lectureId=:lectureId")
	public List<Attendance> findAttendanceByLectureId(@Param("lectureId")Long lectureId);
	@Query("from Attendance a where a.lectureId=:lectureId and a.studentId=:studentId")
	public Attendance findAttendanceByLectureIdAndStudentId(@Param("lectureId")Long lectureId,@Param("studentId")Long studentId);
	@Modifying
	@Query("delete from Attendance a where a.lectureId=:lectureId")
	public void deleteAttendanceByLectureId(@Param("lectureId")long lectureId);
	@Query("from Attendance a where a.studentId=:studentId")
	public List<Attendance> findAttendanceByStudentId(@Param("studentId")long studentId);
	
	@Modifying
	@Query("delete from Attendance a where a.lectureId=:lectureId and a.studentId=:studentId")
	public void deleteAttendanceByLectureIdAndStudentId(@Param("lectureId")long lectureId,@Param("studentId")long studentId);
	
	
	
}
