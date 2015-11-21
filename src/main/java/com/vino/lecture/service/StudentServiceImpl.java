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

import com.vino.lecture.entity.Student;
import com.vino.lecture.exception.StudentDuplicateException;
import com.vino.lecture.repository.StudentRepository;
import com.vino.scaffold.entity.Constants;
import com.vino.scaffold.service.base.AbstractBaseServiceImpl;
import com.vino.scaffold.shiro.entity.User;
import com.vino.scaffold.shiro.service.PasswordHelper;

@Service("studentService")
public class StudentServiceImpl extends AbstractBaseServiceImpl<Student, Long> implements StudentService {
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private PasswordHelper passwordHelper;

	@Override
	public Student findByUsername(String studentname) {

		return studentRepository.findByUsername(studentname);
	}

	// 只能管理端调用
	@Override
	public void saveWithCheckDuplicate(Student student) throws StudentDuplicateException {
		// 校验是否用户重复
		if (studentRepository.findByUsername(student.getUsername()) != null)
			throw new StudentDuplicateException();
		if (student.getPassword() == null) {
			student.setPassword(Constants.DEFAULT_PASSWORD);
		}
		// 加密密码
		passwordHelper.encryptStudentPassword(student);
		student.setCreateTime(new Date());

		if (getCurrentUser() != null)
			student.setCreatorName(getCurrentUser().getUsername());
		studentRepository.save(student);

	}

	@Override
	public void saveWithCheckDuplicate(List<Student> students) throws StudentDuplicateException {
		if (null == students || 0 == students.size())
			return;
		// 校验是否用户重复
		for (Student student : students) {
			if (studentRepository.findByUsername(student.getUsername()) != null)
				throw new StudentDuplicateException();
			if (student.getPassword() == null) {
				student.setPassword(Constants.DEFAULT_PASSWORD);
			}
			// 加密密码
			passwordHelper.encryptStudentPassword(student);
			student.setCreateTime(new Date());
			student.setCreatorName(getCurrentUser().getUsername());
		}
		studentRepository.save(students);

	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Student> buildSpecification(final Map<String, Object> searchParams) {
		Specification<Student> spec = new Specification<Student>() {
			@Override
			public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = null;
				String username = (String) searchParams.get("username");
				String name = (String) searchParams.get("name");
				String major = (String) searchParams.get("major");
				String grade = (String) searchParams.get("grade");
				String createTimeRange = (String) searchParams.get("createTimeRange");
				if (username != null && !"".equals(username)) {
					Predicate condition = cb.like(root.get("username").as(String.class),
							"%" + searchParams.get("username") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);// 此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
					else
						allCondition = cb.and(allCondition, condition);
				}
				if (major != null && !"".equals(major)) {
					Predicate condition = cb.like(root.get("major").as(String.class),
							"%" + searchParams.get("major") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);// 此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
					else
						allCondition = cb.and(allCondition, condition);
				}
				if (grade != null && !"".equals(grade)) {
					Predicate condition = cb.like(root.get("grade").as(String.class),
							"%" + searchParams.get("grade") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);// 此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
					else
						allCondition = cb.and(allCondition, condition);
				}
				if (name != null && !"".equals(name)) {
					Predicate condition = cb.like(root.get("name").as(String.class),
							"%" + searchParams.get("name") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);// 此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
					else
						allCondition = cb.and(allCondition, condition);
				}
				if (createTimeRange != null && !"".equals(createTimeRange)) {
					String createTimeStartStr = createTimeRange.split(" - ")[0] + ":00:00:00";
					String createTimeEndStr = createTimeRange.split(" - ")[1] + ":23:59:59";
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy:hh:mm:ss");
					System.out.println(createTimeStartStr);
					try {
						Date createTimeStart = format.parse(createTimeStartStr);
						Date createTimeEnd = format.parse(createTimeEndStr);
						Predicate condition = cb.between(root.get("createTime").as(Date.class), createTimeStart,
								createTimeEnd);
						if (null == allCondition)
							allCondition = cb.and(condition);// 此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
						else
							allCondition = cb.and(allCondition, condition);

					} catch (ParseException e) {
						e.printStackTrace();
						Logger log = LoggerFactory.getLogger(this.getClass());
						log.error("createTime 转换时间出错");
					}
				}
				return allCondition;
			}
		};
		return spec;
	}

	@Override
	public Page<Student> findStudentByCondition(Map<String, Object> searchParams, Pageable pageable) {

		return studentRepository.findAll(buildSpecification(searchParams), pageable);
	}
	/**
	 * 编辑框中有哪些属性就添加哪些属性
	 */
	@Override
	public void update(Student student) {
		Student student2 = studentRepository.findOne(student.getId());
		if (student.getBirthday() != null)
			student2.setBirthday(student.getBirthday());
		if (student.getName() != null)
			student2.setName(student.getName());
		if (student.getMajor() != null)
			student2.setMajor(student.getMajor());
		if (student.getGrade() != null)
			student2.setGrade(student.getGrade());
		if (student.getCreateTime() != null)
			student2.setCreateTime(student.getCreateTime());
		if (student.getCreatorName() != null)
			student2.setCreatorName(student.getCreatorName());

	}

}
