package com.vino.lecture.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vino.lecture.entity.Lecture;
import com.vino.lecture.exception.LectureDuplicateException;
import com.vino.lecture.repository.LectureRepository;
import com.vino.scaffold.service.base.AbstractBaseServiceImpl;
import com.vino.scaffold.shiro.entity.User;

@Service("lectureService")
public class LectureServiceImpl extends AbstractBaseServiceImpl<Lecture, Long>  implements LectureService{
	@Autowired
	private LectureRepository lectureRepository;
	@Override
	public void saveWithCheckDuplicate(Lecture Lecture, User user) throws LectureDuplicateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<Lecture> findLectureByCondition(Map<String, Object> searchParams, Pageable pageable) {
		
		return null;
	}

}
