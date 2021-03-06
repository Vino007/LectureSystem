package com.vino.lecture.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vino.lecture.entity.Lecture;
import com.vino.scaffold.repository.base.BaseRepository;
import com.vino.scaffold.shiro.entity.Resource;
import com.vino.scaffold.shiro.entity.User;

public interface LectureRepository extends BaseRepository<Lecture, Long>{
	
	List<Lecture> findLectureByAvailable(boolean available);
}
