package com.vino.scaffold.shiro.realm;



import java.util.Date;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.vino.lecture.entity.Student;
import com.vino.lecture.service.StudentService;
import com.vino.scaffold.entity.Constants;
import com.vino.scaffold.shiro.entity.User;
import com.vino.scaffold.shiro.service.UserService;



/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
public class UserRealm extends AuthorizingRealm {
	@Autowired
    private UserService userService;
	@Autowired
	private StudentService studentService;
     
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}
	/**
     * user才获取权限，student不获取权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	    	
        String username = (String)principals.getPrimaryPrincipal();
        Student student=studentService.findByUsername(username);
        User user = userService.findByUsername(username);
        SimpleAuthorizationInfo authorizationInfo= new SimpleAuthorizationInfo();
        if(user!=null){    
        authorizationInfo.setRoles(userService.findAllRoleNamesByUsername(username));//查询用户的角色放入凭证中
        authorizationInfo.setStringPermissions(userService.findAllPermissionsByUsername(username));//查询用户权限放入凭证中
        System.out.println("dogetAuthorization 获取权限");   
        //更新登录时间
        User curUser=userService.findByUsername(username);
        if(curUser.getLoginTime()!=null){
        	curUser.setLastLoginTime(curUser.getLoginTime());
        }
		curUser.setLoginTime(new Date());		
		userService.update(curUser);
        }
        
		
        return authorizationInfo;
    }
    //要保证student与user用户名不重复
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	SimpleAuthenticationInfo authenticationInfo=null;
        String username = (String)token.getPrincipal();
        Student student=studentService.findByUsername(username);
     
        User user = userService.findByUsername(username);
        if(user == null&&student == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        if(user!=null&&Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }
        if(student!=null&&Boolean.TRUE.equals(student.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }
        
        if(student!=null){
        	Constants.isStudent=true;
        	if(student.getLoginTime()!=null){
        		student.setLastLoginTime(student.getLoginTime());
        	}
        	student.setLoginTime(new Date());
        	studentService.update(student);
        	
        	authenticationInfo = new SimpleAuthenticationInfo(
                    student.getUsername(), //用户名
                    student.getPassword(), //密码
                    ByteSource.Util.bytes(student.getSalt()),//salt=username+salt
                    getName()  //realm name
            );
            return authenticationInfo;
        }
        if(user!=null){
        	Constants.isStudent=false;
        	//更新登录时间
            User curUser=userService.findByUsername(username);
            if(curUser.getLoginTime()!=null){
            	curUser.setLastLoginTime(curUser.getLoginTime());
            }
    		curUser.setLoginTime(new Date());		
    		userService.update(curUser);
    		
            System.out.println("doGetAuthenticationInfo 登录");
           
            //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
            authenticationInfo = new SimpleAuthenticationInfo(
                    user.getUsername(), //用户名
                    user.getPassword(), //密码
                    ByteSource.Util.bytes(user.getSalt()),//salt=username+salt
                    getName()  //realm name
            );
           
        }
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
