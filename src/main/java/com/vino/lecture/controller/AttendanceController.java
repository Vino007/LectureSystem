package com.vino.lecture.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import com.vino.lecture.service.AttendanceExcelService;
import com.vino.lecture.service.AttendanceService;
import com.vino.lecture.service.LectureService;
import com.vino.lecture.service.StudentService;
import com.vino.scaffold.controller.base.BaseController;
import com.vino.scaffold.entity.Constants;
import com.vino.scaffold.utils.Servlets;


import jxl.read.biff.BiffException;

@Controller
@RequestMapping("/attendance")
public class AttendanceController extends BaseController {
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private LectureService lectureService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private AttendanceExcelService attendanceExcelService;
	@RequiresPermissions("attendance:menu")
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAllLectures(Model model,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE+"") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(pageNumber));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		return "attendance/list";
	}
	
	@RequiresPermissions("attendance:create")
	@RequestMapping(value="/prepareAdd",method=RequestMethod.GET)
	public String prepareAddAttendance(Model model,@RequestParam("lectureId")Long lectureId ){
		model.addAttribute("lectureId", lectureId);
		return "attendance/add";
	}
	@RequiresPermissions("attendance:create")
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String addAttendance(Model model,long lectureId,String username){
		Student student=studentService.findByUsername(username);
		if(student==null){
			model.addAttribute("requestResult", "entityNotExist");	
			
		}else{
			Attendance attendance=new Attendance();
			attendance.setAttended(true);
			attendance.setLectureId(lectureId);
			attendance.setStudentId(student.getId());
			try {
				attendanceService.saveWithCheckDuplicate(attendance);
			} catch (AttendanceDuplicateException e) {
				e.printStackTrace();			
				model.addAttribute("requestResult", "entityDuplicate");	
			}
		}
		
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(1));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		return "attendance/list";	
	}
	@RequiresPermissions("attendance:delete")
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public  String deleteAttendances(Model model,long lectureId){
		attendanceService.deleteAttendance(lectureId);//删除与lecture相关的所有考勤
		
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(1));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		return "attendance/list";
		
	}
	@RequiresPermissions("attendance:update")
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String prepareUpdateAttendance(Model model,@PathVariable("id") Long id){
		model.addAttribute("attendance", attendanceService.findOne(id));
		return "attendance/edit";
		
	}
	@RequiresPermissions("attendance:update")
	@RequestMapping(value="/update",method=RequestMethod.POST)	
	public String updateAttendance(Model model,Attendance attendance){
		attendanceService.update(attendance);
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(1));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		return "attendance/list";
		
	}

	@RequiresPermissions("attendance:view")
	@RequestMapping(value="/detail/{id}",method=RequestMethod.GET)
	public String findAttendance(Model model,@PathVariable("id") Long id){
		model.addAttribute("attendance", attendanceService.findOne(id));
		return "attendance/detail";
		
	}
	
	@RequiresPermissions("attendance:upload")
	@RequestMapping(value="/prepareUpload",method=RequestMethod.GET)
	public String prepareUpload(Model model,Long lectureId){
		model.addAttribute("lectureId", lectureId);
		return "attendance/upload";
	}
	/**
	 * 上传文件
	 * @param model
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresPermissions("attendance:upload")
	@ResponseBody
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public String upload(Model model,@RequestParam("file")MultipartFile file,HttpServletRequest request,Long lectureId){
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(1));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		if(!file.isEmpty()){
			 //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中  
            String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");  
            //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
            try {
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, file.getOriginalFilename()));
				List<Attendance> uploadAttendances=attendanceExcelService.getFromExcel(new File(realPath+"\\"+file.getOriginalFilename()),lectureId);		
				attendanceService.saveWithCheckDuplicate(uploadAttendances);
				log.info("上传用户:"+Arrays.toString(uploadAttendances.toArray()));
			} catch (IOException e) {
				log.error("保存或读取文件出错");
				e.printStackTrace();
				return "saveFileError";
			} catch (BiffException e) {
				
				e.printStackTrace();
				return "fileStreamError";
			} catch (AttendanceDuplicateException e) {
				e.printStackTrace();
				log.warn("上传文件包含与数据库重复的对象");
				return "entityDuplicate";				
			} 
		}else{
			return "fileEmpty";
		}
		return "uploadSuccess";
	}

	
	@RequiresPermissions("attendance:downloadAttendance")
	@RequestMapping(value="/downloadAttendance",method=RequestMethod.GET)
	public ResponseEntity<byte[]> downloadAttendance(Long lectureId,HttpSession session) throws IOException{
		String realPath=session.getServletContext().getRealPath("/WEB-INF/upload");
		String fileName="attendanceList"+".xls";
		attendanceExcelService.saveAttendanceToExcel(realPath+"\\"+fileName, lectureId);
		HttpHeaders headers = new HttpHeaders();    
		headers.setContentDispositionFormData("attachment", fileName); 
	
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	  //  FileInputStream fin=new FileInputStream(new File(realPath+"\\"+fileName));
	    
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(realPath+"\\"+fileName)),    
				                                  headers, HttpStatus.CREATED);
			
	}
	@RequiresPermissions("attendance:downloadReserve")
	@RequestMapping(value="/downloadReserve",method=RequestMethod.GET)
	public ResponseEntity<byte[]> downloadReserve(Long lectureId,HttpSession session) throws IOException{
		String realPath=session.getServletContext().getRealPath("/WEB-INF/upload");
		String fileName="reserveList"+".xls";
		attendanceExcelService.saveReserveToExcel(realPath+"\\"+fileName, lectureId);
		HttpHeaders headers = new HttpHeaders();    
		headers.setContentDispositionFormData("attachment", fileName); 
	
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	   // FileInputStream fin=new FileInputStream(new File(realPath+"\\"+fileName));
	    
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(realPath+"\\"+fileName)),    
				                                  headers, HttpStatus.CREATED);
			
	}
}
