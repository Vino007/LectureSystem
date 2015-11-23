package com.vino.scaffold.shiro.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vino.lecture.entity.Student;
import com.vino.lecture.service.StudentService;
import com.vino.scaffold.controller.base.BaseController;
import com.vino.scaffold.entity.Constants;
import com.vino.scaffold.shiro.entity.Resource;
import com.vino.scaffold.shiro.entity.User;
import com.vino.scaffold.shiro.service.ResourceService;
import com.vino.scaffold.shiro.service.UserService;

@Controller
public class HomeController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private StudentService studentService;

	@RequestMapping("/")
	public String home(Model model, HttpServletRequest request) {
		if (!Constants.isStudent) {
			Subject curUser = SecurityUtils.getSubject();
			Session session = curUser.getSession();
			String username = (String) curUser.getPrincipal();
			User currentUser = userService.findByUsername(username);
			session.setAttribute(Constants.CURRENT_USER, currentUser);// 将当前用户放入session
			session.setAttribute(Constants.CURRENT_USERNAME, username);
			List<Resource> resources = resourceService.findAll();// 用于前端页面生成侧边栏
			request.setAttribute("resources", resources);
			
			if (currentUser.getLoginTime() != null) {
				currentUser.setLastLoginTime(currentUser.getLoginTime());
			}
			currentUser.setLoginTime(new Date());
			userService.update(currentUser);
			
			return "index";
		} else {
			Subject curUser = SecurityUtils.getSubject();
			Session session = curUser.getSession();
			String username = (String) curUser.getPrincipal();
			Student currentUser = studentService.findByUsername(username);
			session.setAttribute(Constants.CURRENT_USER, currentUser);// 将当前用户放入session
			session.setAttribute(Constants.CURRENT_USERNAME, username);
			if (currentUser.getLoginTime() != null) {
				currentUser.setLastLoginTime(currentUser.getLoginTime());
			}
			currentUser.setLoginTime(new Date());
			studentService.update(currentUser);

			return "student/index";
		}

	}
}
