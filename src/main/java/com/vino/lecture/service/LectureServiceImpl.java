package com.vino.lecture.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vino.lecture.entity.Lecture;
import com.vino.lecture.entity.Student;
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
		
		
	}

	

	@Override
	public void save(Lecture obj) {
		User curUser=getCurrentUser();
		obj.setCreatorName(curUser.getUsername());
		obj.setCreateTime(new Date());
		obj.setCreatorId(curUser.getId());
		super.save(obj);
	}



	@Override
	public void save(List<Lecture> objs) {
		User curUser=getCurrentUser();
		for(Lecture obj:objs){
			obj.setCreatorName(curUser.getUsername());
			obj.setCreateTime(new Date());
			obj.setCreatorId(curUser.getId());
		}
		super.save(objs);
	}

	 /**
     * 创建动态查询条件组合.
     */
    private Specification<Lecture> buildSpecification(final Map<String,Object> searchParams) {		
        Specification<Lecture> spec = new Specification<Lecture>(){           
			@Override
			public Predicate toPredicate(Root<Lecture> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = null;
				String title=(String) searchParams.get("title");
				String lecturer=(String) searchParams.get("lecturer");
				String address=(String) searchParams.get("address");
				String lectureTimeRange=(String) searchParams.get("lectureTimeRange");
				String createTimeRange=(String) searchParams.get("createTimeRange");
				if(title!=null&&!"".equals(title)){
					Predicate condition=cb.like(root.get("title").as(String.class),"%"+searchParams.get("title")+"%");
					if(null==allCondition)
						allCondition=cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
					else
						allCondition=cb.and(allCondition,condition);
					}
				if(lecturer!=null&&!"".equals(lecturer)){
					Predicate condition=cb.like(root.get("lecturer").as(String.class),"%"+searchParams.get("lecturer")+"%");
					if(null==allCondition)
						allCondition=cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
					else
						allCondition=cb.and(allCondition,condition);
					}
				if(address!=null&&!"".equals(address)){
					Predicate condition=cb.like(root.get("address").as(String.class),"%"+searchParams.get("address")+"%");
					if(null==allCondition)
						allCondition=cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
					else
						allCondition=cb.and(allCondition,condition);
					}
				if(lectureTimeRange!=null&&!"".equals(lectureTimeRange)){			
					String lectureTimeStartStr=lectureTimeRange.split(" - ")[0]+":00:00:00";
					String lectureTimeEndStr=lectureTimeRange.split(" - ")[1]+":23:59:59";
					SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy:hh:mm:ss");
					//System.out.println(createTimeStartStr);
					try {
						Date lectureTimeStart = format.parse(lectureTimeStartStr);
						Date lectureTimeEnd=format.parse(lectureTimeEndStr);
						Predicate condition=cb.between(root.get("time").as(Date.class),lectureTimeStart, lectureTimeEnd);
						if(null==allCondition)
							allCondition=cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
						else
							allCondition=cb.and(allCondition,condition);
						
					} catch (ParseException e) {
						e.printStackTrace();
						Logger log =LoggerFactory.getLogger(this.getClass());
						log.error("lectureTime 转换时间出错");
					}				
				}
				
				if(createTimeRange!=null&&!"".equals(createTimeRange)){			
					String createTimeStartStr=createTimeRange.split(" - ")[0]+":00:00:00";
					String createTimeEndStr=createTimeRange.split(" - ")[1]+":23:59:59";
					SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy:hh:mm:ss");
					//System.out.println(createTimeStartStr);
					try {
						Date createTimeStart = format.parse(createTimeStartStr);
						Date createTimeEnd=format.parse(createTimeEndStr);
						Predicate condition=cb.between(root.get("createTime").as(Date.class),createTimeStart, createTimeEnd);
						if(null==allCondition)
							allCondition=cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
						else
							allCondition=cb.and(allCondition,condition);
						
					} catch (ParseException e) {
						e.printStackTrace();
						Logger log =LoggerFactory.getLogger(this.getClass());
						log.error("createTime 转换时间出错");
					}				
				}					
				return allCondition;
			}
        };
        return spec;
    }

	@Override
	public Page<Lecture> findLectureByCondition(Map<String, Object> searchParams, Pageable pageable) {
		
		return lectureRepository.findAll(buildSpecification(searchParams), pageable);
	}



	@Override
	public void update(Lecture lecture) {
		Lecture lecture2 = lectureRepository.findOne(lecture.getId());
		if (lecture.getTitle() != null)
			lecture2.setTitle(lecture.getTitle());
		if (lecture.getLecturer() != null)
			lecture2.setLecturer(lecture.getLecturer());
		if (lecture.getTime() != null)
			lecture2.setTime(lecture.getTime());
		if (lecture.getAddress() != null)
			lecture2.setAddress(lecture.getAddress());
		if (lecture.getMaxPeopleNum()!=0)
			lecture2.setMaxPeopleNum(lecture.getMaxPeopleNum());
		if (lecture.getReserveStartTime() != null)
			lecture2.setReserveStartTime(lecture.getReserveStartTime());
		if (lecture.getDescription() != null)
			lecture2.setDescription(lecture.getDescription());
		if (lecture.getCreateTime() != null)
			lecture2.setCreateTime(lecture.getCreateTime());
		if (lecture.getCreatorName() != null)
			lecture2.setCreatorName(lecture.getCreatorName());
		
		
	}
	
}
