package com.vino.lecture.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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

import com.vino.lecture.entity.Student;
import com.vino.lecture.exception.StudentDuplicateException;
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
	private StudentExcelService studentExcelService;
	@RequiresPermissions("student:menu")
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAllStudents(Model model,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE+"") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		Page<Student> studentPage=studentService.findAll(buildPageRequest(pageNumber));
		model.addAttribute("students", studentPage.getContent());
		model.addAttribute("page", studentPage);
		//model.addAttribute("searchParams", "");
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
		System.out.println("���ص�ҳ�����������"+Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		System.out.println(searchParams.toString());
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
	public String addStudent(Model model ,Student student,HttpSession session){
		try {
			studentService.saveWithCheckDuplicate(student);
		} catch (StudentDuplicateException e) {	
			model.addAttribute("studentDuplicate", "true");
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
		System.out.println(downloadIds);
		String realPath=session.getServletContext().getRealPath("/WEB-INF/upload");
		String fileName="studentExport"+System.currentTimeMillis()+".xls";
		studentExcelService.saveToExcel(realPath+"\\"+fileName, downloadIds);
		HttpHeaders headers = new HttpHeaders();    
		headers.setContentDispositionFormData("attachment", fileName); 
	
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	    FileInputStream fin=new FileInputStream(new File(realPath+"\\"+fileName));
	    
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(realPath+"\\"+fileName)),    
				                                  headers, HttpStatus.CREATED);
			
	}
}
