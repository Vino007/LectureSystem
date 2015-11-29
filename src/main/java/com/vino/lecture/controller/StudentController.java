package com.vino.lecture.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.event.ListSelectionEvent;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.vino.lecture.entity.Attendance;
import com.vino.lecture.entity.Lecture;
import com.vino.lecture.entity.Student;
import com.vino.lecture.exception.AttendanceDuplicateException;
import com.vino.lecture.exception.AttendanceNotExistException;
import com.vino.lecture.exception.StudentDuplicateException;
import com.vino.lecture.service.AttendanceService;
import com.vino.lecture.service.LectureService;
import com.vino.lecture.service.StudentExcelService;
import com.vino.lecture.service.StudentService;
import com.vino.scaffold.controller.base.BaseController;
import com.vino.scaffold.entity.Constants;
import com.vino.scaffold.utils.Servlets;

import jxl.read.biff.BiffException;

@Controller
@RequestMapping("/student")
public class StudentController extends BaseController{
	@Autowired
	private StudentService studentService;
	@Autowired
	private LectureService lectureService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private StudentExcelService studentExcelService;
	@RequiresPermissions("student:menu")
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAllStudents(Model model,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE+"") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		Page<Student> studentPage=studentService.findAll(buildPageRequest(pageNumber));
		model.addAttribute("students", studentPage.getContent());
		model.addAttribute("page", studentPage);
		return "student/list";
	}
	@RequiresPermissions("student:view")
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public String getStudentsByCondition(Model model,Student student,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,ServletRequest request){
		Map<String,Object> searchParams=Servlets.getParametersStartingWith(request, "search_");
		log.info("��������="+searchParams.toString());				
		Page<Student> studentPage=studentService.findStudentByCondition(searchParams, buildPageRequest(pageNumber));
		model.addAttribute("students",studentPage.getContent());
		model.addAttribute("page", studentPage);	
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		model.addAttribute("searchParamsMap", searchParams);
		return "student/list";
	}
	@RequiresPermissions("student:create")
	@RequestMapping(value="/prepareAdd",method=RequestMethod.GET)
	public String prepareAddStudent(Model model ){
		return "student/add";
	}
	@RequiresPermissions("student:create")
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String addStudent(Model model ,Student student) {
		try {
			studentService.saveWithCheckDuplicate(student);
		} catch (StudentDuplicateException e) {	
			model.addAttribute("requestResult", "entityDuplicate");			
			e.printStackTrace();
		}					
		Page<Student> studentPage=studentService.findAll(buildPageRequest(1));
		model.addAttribute("students", studentPage.getContent());
		model.addAttribute("page", studentPage);
		return "student/list";	
	}
	@RequiresPermissions("student:delete")
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public  String deleteStudents(Model model,@RequestParam("deleteIds[]")Long[] deleteIds){
		studentService.delete(deleteIds);
		Page<Student> studentPage=studentService.findAll(buildPageRequest(1));
		model.addAttribute("students", studentPage.getContent());
		model.addAttribute("page", studentPage);
		return "student/list";
		
	}
	@RequiresPermissions("student:update")
	@RequestMapping(value="/update",method=RequestMethod.POST)	
	public String updateStudent(Model model,Student student){
		studentService.update(student);
		Page<Student> studentPage=studentService.findAll(buildPageRequest(1));
		model.addAttribute("students", studentPage.getContent());
		model.addAttribute("page", studentPage);
		return "student/list";
		
	}
	@RequiresPermissions("student:update")
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String prepareUpdateStudent(Model model,@PathVariable("id") Long id){
		model.addAttribute("student", studentService.findOne(id));
		return "student/edit";
		
	}
	@RequiresPermissions("student:view")
	@RequestMapping(value="/detail/{id}",method=RequestMethod.GET)
	public String findStudent(Model model,@PathVariable("id") Long id){
		model.addAttribute("student", studentService.findOne(id));
		return "student/detail";
		
	}
	
	@RequiresPermissions("student:upload")
	@RequestMapping(value="/prepareUpload",method=RequestMethod.GET)
	public String prepareUpload(){
		return "student/upload";
	}
	/**
	 * �ϴ��ļ�
	 * @param model
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresPermissions("student:upload")
	@ResponseBody
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public String upload(Model model,@RequestParam("file")MultipartFile file,HttpServletRequest request){
		Page<Student> studentPage=studentService.findAll(buildPageRequest(1));
		model.addAttribute("students", studentPage.getContent());
		model.addAttribute("page", studentPage);
		
		if(!file.isEmpty()){
			 //����õ���Tomcat�����������ļ����ϴ���\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\�ļ�����  
            String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");  
            //���ﲻ�ش���IO���رյ����⣬��ΪFileUtils.copyInputStreamToFile()�����ڲ����Զ����õ���IO���ص�
            try {
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, file.getOriginalFilename()));
				List<Student> uploadStudents=studentExcelService.getFromExcel(new File(realPath+"\\"+file.getOriginalFilename()));		
				studentService.saveWithCheckDuplicate(uploadStudents);
				log.info("�ϴ��û�:"+Arrays.toString(uploadStudents.toArray()));
			} catch (IOException e) {
				log.error("������ȡ�ļ�����");
				e.printStackTrace();
				return "saveFileError";
			} catch (BiffException e) {
				
				e.printStackTrace();
				return "fileStreamError";
			} catch (StudentDuplicateException e) {
				e.printStackTrace();
				log.warn("�ϴ��ļ����������ݿ��ظ��Ķ���");
				return "entityDuplicate";				
			} 
		}else{
			return "fileEmpty";
		}
		
		return "uploadSuccess";
	}
	/**
	 * ѡȡ����������
	 * @param downloadIds
	 * @param session
	 * @return
	 * @throws IOException
	 */
	
	@RequiresPermissions("student:download")
	@RequestMapping(value="/download",method=RequestMethod.POST)
	public ResponseEntity<byte[]> download(@RequestParam(value="downloadIds[]",required=false)Long[] downloadIds,HttpSession session) throws IOException{
		String realPath=session.getServletContext().getRealPath("/WEB-INF/upload");
		String fileName="studentExport"+System.currentTimeMillis()+".xls";
		studentExcelService.saveToExcel(realPath+"\\"+fileName, downloadIds);
		HttpHeaders headers = new HttpHeaders();    
		headers.setContentDispositionFormData("attachment", fileName); 
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	   // FileInputStream fin=new FileInputStream(new File(realPath+"\\"+fileName));
	    
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(realPath+"\\"+fileName)),    
				                                  headers, HttpStatus.CREATED);
			
	}
	/****************************************************************************************************************
	 ****************************************************************************************************************
	 **************************************************������ѧ���˵Ĺ���***********************************************
	 ****************************************************************************************************************
	 **************************************************************************************************************** 
	 */
	@RequestMapping(value="/profile",method=RequestMethod.GET)
	public String getProfile(Model model,long studentId){
		Student student=studentService.findOne(studentId);
		model.addAttribute("currentUser",student);
		return "student/client/profile";
	}
	@RequestMapping(value="/lecture/prepareReserve",method=RequestMethod.GET)
	public String prepareLectureReserve(Model model){
		List<Lecture> lectures=lectureService.findLectureByAvailable(true);
		//ʹ�ý���ʱ�����Խ�����������ʱ��Խ��������Խǰ��
		
		model.addAttribute("lectures", lectures);
		return "student/client/lectureReserve";
	}
	/**
	 * ԤԼ����
	 * @param model
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/lecture/reserve",method=RequestMethod.POST)
	public Map<String,Object> lectureReserve(Model model,Long lectureId,HttpSession session){
		Map<String,Object> result=new HashMap<>();
		Lecture lecture=lectureService.findOne(lectureId);
		Date now=new Date();
		//log.info("��ǰʱ��"+now);
		if(lecture.getCurrentPeopleNum()>=lecture.getMaxPeopleNum()){
			result.put("result", "fullPeople");
			return result;
		}else if(now.before(lecture.getReserveStartTime())){
			result.put("result", "timeNotArrived");
			return result;
		}
		Student curUser=(Student) session.getAttribute(Constants.CURRENT_USER);
		Long studentId=curUser.getId();
		Attendance attendance=new Attendance();
		attendance.setAttended(false);
		attendance.setStudentId(studentId);
		attendance.setLectureId(lectureId);
		
		try {
			attendanceService.saveWithCheckDuplicate(attendance); //���ϳ�һ��service
			lecture.setCurrentPeopleNum((lecture.getCurrentPeopleNum())+1);
			lectureService.update(lecture);
			result.put("result","success");	
			result.put("currentPeopleNum",lectureService.findOne(lectureId).getCurrentPeopleNum());
			
		} catch (AttendanceDuplicateException e) {		
			e.printStackTrace();
			//return "reserveAlready";//�Ѿ�ԤԼ��
			result.put("result","reserveAlready");
			
		}
		return result;
		
	}
	@ResponseBody
	@RequestMapping(value="/lecture/cancelReservation",method=RequestMethod.POST)
	public Map<String,Object> lectureCancelReservation(Model model,Long lectureId,HttpSession session){
		Lecture lecture=lectureService.findOne(lectureId);
		Student curUser=(Student) session.getAttribute(Constants.CURRENT_USER);
		Long studentId=curUser.getId();	
		Map<String,Object> result=new HashMap<>();	
		try {
			attendanceService.cancelReservation(lectureId, studentId);//���ϳ�һ��service
			lecture.setCurrentPeopleNum((lecture.getCurrentPeopleNum())-1);
			lectureService.update(lecture);
			result.put("result","success");	
			result.put("currentPeopleNum",lectureService.findOne(lectureId).getCurrentPeopleNum());
		} catch (AttendanceNotExistException e) {
			result.put("result","attendanceNotExist");		
			e.printStackTrace();
		}					
		return result;		
	}
	/**
	 * ��ȡ��ǰѧ���Ľ���
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/myLecture",method=RequestMethod.GET)
	public String getLecturesByStudent(HttpSession session,Model model){
		Student curUser=(Student) session.getAttribute(Constants.CURRENT_USER);
		List<Lecture> lectures=lectureService.findLecturesByStudentId(curUser.getId(), true);//��ȡ��ǩ����
		model.addAttribute("lectures", lectures);
		return "student/client/myLecture";
	}
	@RequestMapping(value="lecture/detail/{id}",method=RequestMethod.GET)
	public String findLecture(Model model,@PathVariable("id") Long id){
		model.addAttribute("lecture", lectureService.findOne(id));
		return "lecture/detail";
		
	}
	/**
	 * ��ȡ���н���
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return
	 */
	@RequestMapping(value="/lecture/all",method=RequestMethod.GET)
	public String getAllLectures(Model model,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE+"") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(pageNumber));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		return "student/client/lectureSearch";
	}
	/**
	 * ��ѯ����
	 * @param model
	 * @param lecture
	 * @param pageNumber
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/lecture/search",method=RequestMethod.GET)
	public String getLecturesByCondition(Model model,Lecture lecture,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,ServletRequest request){
		Map<String,Object> searchParams=Servlets.getParametersStartingWith(request, "search_");
		log.info("��������="+searchParams.toString());				
		Page<Lecture> lecturePage=lectureService.findLectureByCondition(searchParams, buildPageRequest(1));
		model.addAttribute("lectures",lecturePage.getContent());
		model.addAttribute("page", lecturePage);	
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		model.addAttribute("searchParamsMap", searchParams);
		return "student/client/lectureSearch";
	}
	
	@RequestMapping(value="/updateInfo",method=RequestMethod.POST)	
	public String updateStudentWithoutAuth(Model model,Student student,HttpSession session){
		studentService.update(student);//session�е�user���Զ�����
	//	session.setAttribute(Constants.CURRENT_USER, studentService.findOne(student.getId()));
		return "student/client/edit";
		
	}
	@RequestMapping(value="/prepareUpdate/{id}",method=RequestMethod.GET)
	public String prepareUpdateStudentWithoutAuth(Model model,@PathVariable("id") Long id){
		model.addAttribute("student", studentService.findOne(id));
		return "student/client/edit";
		
	}
	@RequestMapping(value="/prepareAlterPassword/{id}",method=RequestMethod.GET)
	public String prepareAlterPassword(Model model,@PathVariable("id") Long id){
		model.addAttribute("student", studentService.findOne(id));
		return "student/client/alterPassword";
		
	}
	@ResponseBody
	@RequestMapping(value="/alterPassword",method=RequestMethod.POST)	
	public Map<String,Object> alterPassword(Model model,long studentId,String oldPassword,String newPassword,String newPassword2,HttpSession session){
		String result=studentService.alterPassword(studentId, oldPassword, newPassword, newPassword2);
		Map<String,Object> resultMap=new HashMap<>();
		resultMap.put("result", result);
		return resultMap;
		
	}
}
