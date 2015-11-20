package com.vino.lecture.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vino.scaffold.entity.base.BaseEntity;

@Entity
@Table(name="t_attendance")
public class Attendance  extends BaseEntity<Long>{
	@Column(name="student_id")
	private long studentId;
	@Column(name="lecture_id")
	private long lectureId;	
	@Column(name="is_attended")
	private boolean isAttended;//ÊÇ·ñ³öÏ¯½²×ù
	public long getStudentId() {
		return studentId;
	}
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	public long getLectureId() {
		return lectureId;
	}
	public void setLectureId(long lectureId) {
		this.lectureId = lectureId;
	}
	public boolean isAttended() {
		return isAttended;
	}
	public void setAttended(boolean isAttended) {
		this.isAttended = isAttended;
	}

}
