package com.vino.lecture.repository;

import com.vino.lecture.entity.Student;
import com.vino.scaffold.repository.base.BaseRepository;


public interface StudentRepository extends BaseRepository<Student, Long>{
	public Student findByUsername(String username);
}
