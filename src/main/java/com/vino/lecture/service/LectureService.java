package com.vino.lecture.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vino.lecture.entity.Lecture;
import com.vino.lecture.exception.LectureDuplicateException;
import com.vino.scaffold.service.base.BaseService;
import com.vino.scaffold.shiro.entity.User;

public interface LectureService extends  BaseService<Lecture, Long>{
	void update(Lecture Lecture);
	Page<Lecture> findLectureByCondition(Map<String, Object> searchParams,
			Pageable pageable);
	//Lecture findByName(String name);
	
	List<Lecture> findLecturesByStudentId(long studentId, boolean isAttended);
	List<Lecture> findLectureByAvailable(boolean available);
	
}
