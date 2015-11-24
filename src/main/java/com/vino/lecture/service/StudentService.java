package com.vino.lecture.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vino.lecture.entity.Student;
import com.vino.lecture.exception.StudentDuplicateException;
import com.vino.scaffold.service.base.BaseService;


public interface StudentService extends  BaseService<Student, Long>{
	
	Student findByUsername(String username);
	public void saveWithCheckDuplicate(Student Student) throws StudentDuplicateException;
	public void saveWithCheckDuplicate(List<Student> Students)throws StudentDuplicateException;
	Page<Student> findStudentByCondition(Map<String, Object> searchParams,
			Pageable pageable);
	void update(Student student);
	String alterPassword(long studentId, String oldPassword, String newPassword, String newPassword2);
}
