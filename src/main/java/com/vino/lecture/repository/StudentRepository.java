package com.vino.lecture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vino.lecture.entity.Attendance;
import com.vino.lecture.entity.Student;
import com.vino.scaffold.repository.base.BaseRepository;


public interface StudentRepository extends BaseRepository<Student, Long>{
	public Student findByUsername(String username);
	
}
