package com.vino.lecture.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.vino.scaffold.entity.base.BaseEntity;
import com.vino.scaffold.shiro.entity.Role;
@Entity
@Table(name="t_lecture")
public class Lecture extends BaseEntity<Long>{
	@Column(name="title",length=100)
	private String title;
	@Column(name="lecturer",length=30)
	private String lecturer;
	@Column(name="term",length=30)
	private String term;//讲座所处的学期
	@Column(name="address",length=30)
	private String address;
	@Column(name="description",length=2000)
	private String description;
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")//将String转换成date
	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time")
	private Date time;
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "reserve_start_time")
	private Date reserveStartTime;//开始预约时间
	@Column(name = "max_people_num")
	private int maxPeopleNum;
	@Column(name = "current_people_num")
	private int currentPeopleNum;
	
	@Column(name = "available")
	private boolean available=Boolean.FALSE;//是否可以预约,超过人数或者时间未到不可以预约
	/*@ManyToMany(targetEntity=Student.class)
	@JoinTable(name="t_lecture_student",joinColumns=@JoinColumn(name="lecture_id"),inverseJoinColumns=@JoinColumn(name="student_id"))
	private Set<Student> students=new HashSet<Student>();
	*/
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLecturer() {
		return lecturer;
	}
	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Date getReserveStartTime() {
		return reserveStartTime;
	}
	public void setReserveStartTime(Date reserveStartTime) {
		this.reserveStartTime = reserveStartTime;
	}
	public int getMaxPeopleNum() {
		return maxPeopleNum;
	}
	public void setMaxPeopleNum(int maxPeopleNum) {
		this.maxPeopleNum = maxPeopleNum;
	}
	public int getCurrentPeopleNum() {
		return currentPeopleNum;
	}
	public void setCurrentPeopleNum(int currentPeopleNum) {
		this.currentPeopleNum = currentPeopleNum;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
/*	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	*/
	
	
}
