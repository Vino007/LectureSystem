package com.vino.lecture.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vino.lecture.entity.Lecture;
import com.vino.scaffold.entity.Constants;
import com.vino.scaffold.shiro.entity.User;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
@Service("lectureExcelService")
public class LectureExcelService {
	@Autowired  
	private HttpSession session; 
	@Autowired
	private LectureService lectureService;

	public void setLectureService(LectureService lectureService) {
		this.lectureService = lectureService;
	}
	public void saveToExcel(String path,Long...ids) throws FileNotFoundException{
		List<Lecture> lectures=lectureService.find(ids);
		File file=new File(path);
		createExcel(new FileOutputStream(file), lectures);
	}
	public List<Lecture> getFromExcel(File file) throws BiffException, IOException{
		return readExcel(file);
	}
	/**
	 * index���㿪ʼ
	 * excel��ʽ����һ��Ϊ���⣬��2�п�ʼΪ���ݣ���һ�У��û������ڶ��У��û�����
	 * ��Ҫ�жϷǿյ�У��,�ļ���ĳЩΪ�յ�ʱ��Ҳ���Ե��룬�������쳣
	 * @param file
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws Exception
	 */
	
	public List<Lecture> readExcel(File file) throws BiffException, IOException   {
		List<Lecture> lecturelist = new ArrayList<Lecture>();
		Workbook rwb = null;
		String cellStr = null;
		Lecture lecture = null;
		// ����������
		// ��ȡExcel�ļ�����
		if (file == null || !file.exists()) {
			System.out.println(file.getName() + "�����ڣ�");
		}
		InputStream stream = new FileInputStream(file);
		rwb = Workbook.getWorkbook(stream);
		// ��ȡ�ļ���ָ�������� Ĭ�ϵĵ�һ��
		Sheet sheet = rwb.getSheet(0);
		// ����(��ͷ��Ŀ¼����Ҫ����1��ʼ)
		for (int i = 1; i < sheet.getRows(); i++) {//�ڶ��п�ʼ
			// ����һ��lecture�����Ӧһ�У� �����洢ÿһ�е�ֵ
			lecture = new Lecture();
			// ����			
			cellStr = sheet.getCell(0, i).getContents().trim();//��һ��
			lecture.setTitle(cellStr);
			cellStr = sheet.getCell(1, i).getContents().trim();//��һ��
			lecture.setLecturer(cellStr);
			cellStr = sheet.getCell(2, i).getContents().trim();//�ڶ���
			SimpleDateFormat sf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
			Date time;
			try {
				time = sf.parse(cellStr);
				System.out.println("time"+time);
				lecture.setTime(time);
			} catch (ParseException e) {				
				e.printStackTrace();
			}
			
			cellStr = sheet.getCell(3, i).getContents().trim();//�ڶ���
			lecture.setAddress(cellStr);
			cellStr = sheet.getCell(4, i).getContents().trim();//�ڶ���
			lecture.setMaxPeopleNum(Integer.parseInt(cellStr));
			cellStr = sheet.getCell(5, i).getContents().trim();//�ڶ���
			lecture.setDescription(cellStr);
			
			lecture.setCreateTime(new Date());
			lecture.setCreatorName(getCurrentUser().getUsername());
			
			// �Ѹջ�ȡ���д���lecturelist
			lecturelist.add(lecture);
		}
		return lecturelist;
	}
	private void createExcel(OutputStream os,List<Lecture> list){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String[] heads={"��������","������","ʱ��","�ص�","��������","����"};
		// ����������
		WritableWorkbook workbook=null;
		try {
			workbook = Workbook.createWorkbook(os);
		// �����µ�һҳ��sheetֻ���ڹ�������ʹ��
		WritableSheet sheet = workbook.createSheet("lecture sheet1", 0);
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
			sheet.addCell(new Label(0, i+1, list.get(i).getTitle()));
			sheet.addCell(new Label(1, i+1, list.get(i).getLecturer()));
			sheet.addCell(new Label(2, i+1, sf.format(list.get(i).getTime())));
			sheet.addCell(new Label(3, i+1, list.get(i).getAddress()));
			sheet.addCell(new Label(4, i+1, list.get(i).getMaxPeopleNum()+""));
			sheet.addCell(new Label(5, i+1, list.get(i).getDescription()));
			
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
	public LectureService getLectureService() {
		return lectureService;
	}
	public User getCurrentUser(){
		return (User) session.getAttribute(Constants.CURRENT_USER);
		
	}
}
