package com.vino.lecture.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vino.lecture.entity.Lecture;
import com.vino.scaffold.repository.base.BaseRepository;
import com.vino.scaffold.shiro.entity.Resource;
import com.vino.scaffold.shiro.entity.User;

public interface LectureRepository extends BaseRepository<Lecture, Long>{
	//public Lecture findByUsername(String username);
	//public Page<Lecture> getLecturesByCondition(Lecture lecture,Pageable pageable);
}
