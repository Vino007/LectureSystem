package com.vino.lecture.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vino.lecture.entity.Student;
import com.vino.scaffold.entity.Constants;
import com.vino.scaffold.shiro.entity.User;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
@Service("studentExcelService")
public class StudentExcelService {
	@Autowired  
	private HttpSession session; 
	@Autowired
	private StudentService studentService;

	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}
	public void saveToExcel(String path,Long...ids) throws FileNotFoundException{
		List<Student> students=studentService.find(ids);
		File file=new File(path);
		createExcel(new FileOutputStream(file), students);
	}
	public List<Student> getFromExcel(File file) throws BiffException, IOException{
		return readExcel(file);
	}
	/**
	 * index���㿪ʼ
	 * excel��ʽ����һ��Ϊ���⣬��2�п�ʼΪ���ݣ���һ�У��û������ڶ��У��û�����
	 * @param file
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws Exception
	 */
	
	public List<Student> readExcel(File file) throws BiffException, IOException   {
		List<Student> studentlist = new ArrayList<Student>();
		Workbook rwb = null;
		String cellStr = null;
		Student student = null;
		// ����������
		// ��ȡExcel�ļ�����
		if (file == null || !file.exists()) {
			return null;
		}
		InputStream stream = new FileInputStream(file);
		rwb = Workbook.getWorkbook(stream);
		// ��ȡ�ļ���ָ�������� Ĭ�ϵĵ�һ��
		Sheet sheet = rwb.getSheet(0);
		// ����(��ͷ��Ŀ¼����Ҫ����1��ʼ)
		for (int i = 1; i < sheet.getRows(); i++) {
			// ����һ��student�����Ӧһ�У� �����洢ÿһ�е�ֵ
			student = new Student();
			// ����			
			cellStr = sheet.getCell(0, i).getContents().trim();// ��һ�У�ҵ������
			student.setUsername(cellStr);
			cellStr = sheet.getCell(1, i).getContents().trim();// ��Ԫ��
			student.setName(cellStr);
			cellStr = sheet.getCell(2, i).getContents().trim();// ��Ԫ��
			student.setMajor(cellStr);
			cellStr = sheet.getCell(3, i).getContents().trim();// ��Ԫ��
			student.setGrade(cellStr);
			//����Ĭ��ֵ
			student.setPassword(Constants.DEFAULT_PASSWORD);
			student.setCreateTime(new Date());
			student.setCreatorName(getCurrentUser().getUsername());
			
			// �Ѹջ�ȡ���д���studentlist
			studentlist.add(student);
		}
		return studentlist;
	}
	private void createExcel(OutputStream os,List<Student> list){
		String[] heads={"ѧ��","����","רҵ","�꼶"};
		// ����������
		WritableWorkbook workbook=null;
		try {
			workbook = Workbook.createWorkbook(os);
		// �����µ�һҳ��sheetֻ���ڹ�������ʹ��
		WritableSheet sheet = workbook.createSheet("student sheet1", 0);
		// ������Ԫ�񼴾���Ҫ��ʾ�����ݣ�new Label(0,0,"�û�") ��һ��������column �ڶ���������row
		// ������������content�����ĸ������ǿ�ѡ��,ΪLabel���������ʽ
		// ͨ��sheet��addCell�������Label��ע��һ��cell/labelֻ��ʹ��һ��addCell
		// ��һ������Ϊ�У��ڶ���Ϊ�У��������ı�����
		//����ֶ���
		for(int i=0;i<heads.length;i++){
			sheet.addCell(new Label(i,0,heads[i]));
		}
		//����ֶ�����
		for(int i=0;i<list.size();i++){
			sheet.addCell(new Label(0, i+1, list.get(i).getUsername()));
			sheet.addCell(new Label(1, i+1, list.get(i).getName()));
			sheet.addCell(new Label(2, i+1, list.get(i).getMajor()));
			sheet.addCell(new Label(3, i+1, list.get(i).getGrade()));
			//sheet.addCell(new Label(1, i+1, list.get(i).getGender()));
			
		}
		workbook.write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			// ������д��������У�Ȼ��رչ����������ر������
			
			try {
				if(workbook!=null)
				workbook.close();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(os!=null)
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public StudentService getStudentService() {
		return studentService;
	}
	public User getCurrentUser(){
		return (User) session.getAttribute(Constants.CURRENT_USER);
		
	}
}
