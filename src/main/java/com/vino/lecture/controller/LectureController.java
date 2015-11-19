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
import com.vino.lecture.entity.Lecture;
import com.vino.lecture.service.LectureExcelService;
import com.vino.lecture.service.LectureService;
import com.vino.scaffold.controller.base.BaseController;
import com.vino.scaffold.entity.Constants;
import com.vino.scaffold.utils.Servlets;


import jxl.read.biff.BiffException;

@Controller
@RequestMapping("/lecture")
public class LectureController extends BaseController {
	@Autowired
	private LectureService lectureService;
	@Autowired
	private LectureExcelService lectureExcelService;
	@RequiresPermissions("lecture:menu")
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAllLectures(Model model,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,
			@RequestParam(value = "page.size", defaultValue = Constants.PAGE_SIZE+"") int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(pageNumber));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		//model.addAttribute("searchParams", "");
		return "lecture/list";
	}
	@RequiresPermissions("lecture:view")
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public String getLecturesByCondition(Model model,Lecture lecture,@RequestParam(value="pageNumber",defaultValue="1")int pageNumber,ServletRequest request){
		Map<String,Object> searchParams=Servlets.getParametersStartingWith(request, "search_");
		log.info("搜索参数="+searchParams.toString());				
		Page<Lecture> lecturePage=lectureService.findLectureByCondition(searchParams, buildPageRequest(pageNumber));
		model.addAttribute("lectures",lecturePage.getContent());
		model.addAttribute("page", lecturePage);	
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		System.out.println("返回到页面的搜索参数"+Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		System.out.println(searchParams.toString());
		model.addAttribute("searchParamsMap", searchParams);
		return "lecture/list";
	}
	@RequiresPermissions("lecture:create")
	@RequestMapping(value="/prepareAdd",method=RequestMethod.GET)
	public String prepareAddLecture(Model model ){
		return "lecture/add";
	}
	@RequiresPermissions("lecture:create")
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String addLecture(Model model ,Lecture lecture,HttpSession session){
		lectureService.save(lecture);					
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(1));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		return "lecture/list";	
	}
	@RequiresPermissions("lecture:delete")
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public  String deleteLectures(Model model,@RequestParam("deleteIds[]")Long[] deleteIds){
		lectureService.delete(deleteIds);
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(1));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		return "lecture/list";
		
	}
	@RequiresPermissions("lecture:update")
	@RequestMapping(value="/update",method=RequestMethod.POST)	
	public String updateLecture(Model model,Lecture lecture){
		lectureService.update(lecture);
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(1));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		return "lecture/list";
		
	}
	@RequiresPermissions("lecture:update")
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String prepareUpdateLecture(Model model,@PathVariable("id") Long id){
		model.addAttribute("lecture", lectureService.findOne(id));
		return "lecture/edit";
		
	}
	@RequiresPermissions("lecture:view")
	@RequestMapping(value="/detail/{id}",method=RequestMethod.GET)
	public String findLecture(Model model,@PathVariable("id") Long id){
		model.addAttribute("lecture", lectureService.findOne(id));
		return "lecture/detail";
		
	}
	
	@RequiresPermissions("lecture:upload")
	@RequestMapping(value="/prepareUpload",method=RequestMethod.GET)
	public String prepareUpload(){
		return "lecture/upload";
	}
	/**
	 * 上传文件
	 * @param model
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresPermissions("lecture:upload")
	@ResponseBody
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public String upload(Model model,@RequestParam("file")MultipartFile file,HttpServletRequest request){
		Page<Lecture> lecturePage=lectureService.findAll(buildPageRequest(1));
		model.addAttribute("lectures", lecturePage.getContent());
		model.addAttribute("page", lecturePage);
		
		if(!file.isEmpty()){
			 //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中  
            String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");  
            //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
            try {
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, file.getOriginalFilename()));
				List<Lecture> uploadLectures=lectureExcelService.getFromExcel(new File(realPath+"\\"+file.getOriginalFilename()));		
				lectureService.save(uploadLectures);
				log.info("上传用户:"+Arrays.toString(uploadLectures.toArray()));
			} catch (IOException e) {
				log.error("保存或读取文件出错");
				e.printStackTrace();
				return "saveFileError";
			} catch (BiffException e) {
				
				e.printStackTrace();
				return "fileStreamError";
			} /*catch (LectureDuplicateException e) {
				e.printStackTrace();
				log.warn("上传文件包含与数据库重复的对象");
				return "entityDuplicate";				
			} */
		}else{
			return "fileEmpty";
		}
		
		return "uploadSuccess";
	}
	/**
	 * 选取讲座后下载
	 * @param downloadIds
	 * @param session
	 * @return
	 * @throws IOException
	 */
	
	@RequiresPermissions("lecture:download")
	@RequestMapping(value="/download",method=RequestMethod.POST)
	public ResponseEntity<byte[]> download(@RequestParam(value="downloadIds[]",required=false)Long[] downloadIds,HttpSession session) throws IOException{
		System.out.println(downloadIds);
		String realPath=session.getServletContext().getRealPath("/WEB-INF/upload");
		String fileName="lectureExport"+System.currentTimeMillis()+".xls";
		lectureExcelService.saveToExcel(realPath+"\\"+fileName, downloadIds);
		HttpHeaders headers = new HttpHeaders();    
		headers.setContentDispositionFormData("attachment", fileName); 
	
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	    FileInputStream fin=new FileInputStream(new File(realPath+"\\"+fileName));
	    
	    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(realPath+"\\"+fileName)),    
				                                  headers, HttpStatus.CREATED);
			
	}
}
