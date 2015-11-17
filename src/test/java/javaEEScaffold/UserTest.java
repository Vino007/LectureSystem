package javaEEScaffold;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vino.scaffold.shiro.entity.User;
import com.vino.scaffold.shiro.exception.UserDuplicateException;
import com.vino.scaffold.shiro.service.UserService;

public class UserTest {
	private UserService userService;
	private  ClassPathXmlApplicationContext ctx ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("����׼������");
		 ctx =  new ClassPathXmlApplicationContext("applicationContext.xml"); 
		 userService=ctx.getBean("userService",UserService.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSave() throws UserDuplicateException {
		User user=new User("admin","1111","����");
		User user2=new User();
		//userService.saveWithCheckDuplicate(user,user);
		userService.findByUsername("admin");
	}
	@Test
	public void testConnectUserAndRole(){
		userService.connectUserAndRole(1l, 1l,2l);
	}
	@Test public void testDisconnectUserAndRole(){
		userService.disconnectUserAndRole(1l, 1l);
	}

}
