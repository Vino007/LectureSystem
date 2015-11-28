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
     * user�Ż�ȡȨ�ޣ�student����ȡȨ��
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	    	
        String username = (String)principals.getPrimaryPrincipal();
        Student student=studentService.findByUsername(username);
        User user = userService.findByUsername(username);
        SimpleAuthorizationInfo authorizationInfo= new SimpleAuthorizationInfo();
        if(user!=null){    
        authorizationInfo.setRoles(userService.findAllRoleNamesByUsername(username));//��ѯ�û��Ľ�ɫ����ƾ֤��
        authorizationInfo.setStringPermissions(userService.findAllPermissionsByUsername(username));//��ѯ�û�Ȩ�޷���ƾ֤��
        }
        
		
        return authorizationInfo;
    }
    //Ҫ��֤student��user�û������ظ�
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	SimpleAuthenticationInfo authenticationInfo=null;
        String username = (String)token.getPrincipal();
        Student student=studentService.findByUsername(username);
     
        User user = userService.findByUsername(username);
        if(user == null&&student == null) {
            throw new UnknownAccountException();//û�ҵ��ʺ�
        }
        if(user!=null&&Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //�ʺ�����
        }
        if(student!=null&&Boolean.TRUE.equals(student.getLocked())) {
            throw new LockedAccountException(); //�ʺ�����
        }
        
        if(student!=null){
        	Constants.isStudent=true;        	
        	authenticationInfo = new SimpleAuthenticationInfo(
                    student.getUsername(), //�û���
                    student.getPassword(), //����
                    ByteSource.Util.bytes(student.getSalt()),//salt=username+salt
                    getName()  //realm name
            );
            return authenticationInfo;
        }
        if(user!=null){
        	Constants.isStudent=false;       
            //����AuthenticatingRealmʹ��CredentialsMatcher��������ƥ�䣬��������˼ҵĲ��ÿ����Զ���ʵ��
            authenticationInfo = new SimpleAuthenticationInfo(
                    user.getUsername(), //�û���
                    user.getPassword(), //����
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
