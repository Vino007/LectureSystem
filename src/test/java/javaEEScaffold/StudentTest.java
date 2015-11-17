package javaEEScaffold;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vino.lecture.entity.Student;
import com.vino.lecture.exception.StudentDuplicateException;
import com.vino.lecture.service.StudentService;
import com.vino.scaffold.shiro.service.PasswordHelper;
import com.vino.scaffold.shiro.service.ResourceService;
import com.vino.scaffold.shiro.service.RoleService;

public class StudentTest {
	private EntityManager em;
	private  ClassPathXmlApplicationContext ctx ;
	private StudentService studentService;
	@Before
	public void setUp() throws Exception {
		 ctx =  new ClassPathXmlApplicationContext("applicationContext.xml"); 
		 studentService=ctx.getBean("studentService",StudentService.class);
	}

	@After
	public void tearDown() throws Exception {
		ctx.close();
	}
	@Test
	public void saveStudent() throws StudentDuplicateException{
		Student student=new Student();
		student.setUsername("23220141153384");
		student.setName("׿Խ");
		student.setPassword("1111");
		student.setLocked(false);
		PasswordHelper helper=new PasswordHelper();
		helper.encryptStudentPassword(student);
		studentService.save(student);
	}
}
