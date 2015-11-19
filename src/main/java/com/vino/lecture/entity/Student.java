package com.vino.lecture.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.vino.scaffold.entity.base.BaseEntity;
import com.vino.scaffold.shiro.entity.User;
@Entity
@Table(name="t_student")
public class Student extends BaseEntity<Long> {
	@Column(name = "username",length=40)
	private String username;
	@Column(name = "password",length=40)
	private String password;
	@Column(name = "name",length=30)
	private String name;
	@Column(name = "gender")
	private Integer gender;
	@Column(name = "major",length=30)
	private String major;
	@Column(name = "grade",length=20)
	private String grade;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "birthday")
	private Date birthday;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login_time")
	private Date lastLoginTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "login_time")
	private Date loginTime;
	@Column(name = "salt",length=50)
	private String salt;
	@Column(name = "locked")
	private Boolean locked = Boolean.FALSE;
	
	/*@ManyToMany(targetEntity=Lecture.class,mappedBy="students")
	private Set<Lecture> lectures=new HashSet<Lecture>();
*/
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

/*	public Set<Lecture> getLectures() {
		return lectures;
	}

	public void setLectures(Set<Lecture> lectures) {
		this.lectures = lectures;
	}
	
	*/
}
