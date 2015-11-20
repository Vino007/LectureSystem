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

import com.vino.lecture.entity.Attendance;
import com.vino.lecture.entity.Lecture;
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

@Service("attendanceExcelService")
public class AttendanceExcelService {
	@Autowired
	private HttpSession session;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private LectureService lectureService;
	@Autowired
	private StudentService studentService;
	/**
	 * ���ؿ���excel
	 * @param path
	 * @param lectureId
	 * @throws FileNotFoundException
	 */
	public void saveAttendanceToExcel(String path, Long lectureId) throws FileNotFoundException {
		List<Attendance> attendances = attendanceService.findAttendanceByLectureId(lectureId);
		File file = new File(path);
		createAttendanceExcel(new FileOutputStream(file), attendances, lectureId);
	}
	/**
	 * ����ԤԼ�嵥excel
	 * @param path
	 * @param lectureId
	 * @throws FileNotFoundException
	 */
	public void saveReserveToExcel(String path, Long lectureId) throws FileNotFoundException {
		List<Attendance> attendances = attendanceService.findAttendanceByLectureId(lectureId);
		File file = new File(path);
		createReserveExcel(new FileOutputStream(file), attendances, lectureId);
	}

	public List<Attendance> getFromExcel(File file,long lectureId) throws BiffException, IOException {
		return readExcel(file,lectureId);
	}

	/**
	 * index���㿪ʼ excel��ʽ����һ��Ϊ���⣬��2�п�ʼΪ���ݣ���һ�У��û������ڶ��У��û�����
	 * ��Ҫ�жϷǿյ�У��,�ļ���ĳЩΪ�յ�ʱ��Ҳ���Ե��룬�������쳣
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws BiffException
	 * @throws Exception
	 */

	public List<Attendance> readExcel(File file,Long lectureId) throws BiffException, IOException {
		List<Attendance> attendances = new ArrayList<Attendance>();
		Workbook rwb = null;
		String cellStr = null;
		Attendance attendance = null;
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
		for (int i = 2; i < sheet.getRows(); i++) {// ��3�п�ʼ
			// ����һ��attendance�����Ӧһ�У� �����洢ÿһ�е�ֵ
			attendance = new Attendance();
			// ����
			cellStr = sheet.getCell(0, i).getContents().trim();// ѧ��
			attendance.setLectureId(lectureId);
			attendance.setStudentId(studentService.findByUsername(cellStr).getId());
			cellStr = sheet.getCell(4, i).getContents().trim();// isAttended
			if(cellStr.equals("��")){
				attendance.setAttended(true);
			}else{
				attendance.setAttended(false);
			}
			attendance.setCreateTime(new Date());
			attendance.setCreatorName(getCurrentUser().getUsername());

			// �Ѹջ�ȡ���д���attendance
			attendances.add(attendance);
		}
		return attendances;
	}

	private void createReserveExcel(OutputStream os, List<Attendance> attendances, Long lectureId) {
		String[] heads = { "ѧ��", "����", "רҵ", "�꼶" };
		// ����������
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(os);
			// �����µ�һҳ��sheetֻ���ڹ�������ʹ��
			WritableSheet sheet = workbook.createSheet("attendance sheet1", 0);
			// ������Ԫ�񼴾���Ҫ��ʾ�����ݣ�new Label(0,0,"�û�") ��һ��������column �ڶ���������row
			// ������������content�����ĸ������ǿ�ѡ��,ΪLabel���������ʽ
			// ͨ��sheet��addCell�������Label��ע��һ��cell/labelֻ��ʹ��һ��addCell
			// ��һ������Ϊ�У��ڶ���Ϊ�У��������ı�����
			Lecture lecture = lectureService.findOne(lectureId);
			String[] lectureHeads = { lecture.getTitle(), lecture.getLecturer() };
			for (int i = 0; i < lectureHeads.length; i++) {// ��һ��
				sheet.addCell(new Label(i, 0, lectureHeads[i]));
			}
			// ����ֶ���
			for (int i = 0; i < heads.length; i++) {// �ڶ���
				sheet.addCell(new Label(i, 1, heads[i]));
			}
			// ����ֶ�����,�ӵ����п�ʼ
			for (int i = 0; i < attendances.size(); i++) {
				Attendance attendance = attendances.get(i);
				Student student = studentService.findOne(attendance.getStudentId());
				sheet.addCell(new Label(0, i + 2, student.getUsername()));
				sheet.addCell(new Label(1, i + 2, student.getName()));
				sheet.addCell(new Label(2, i + 2, student.getMajor()));
				sheet.addCell(new Label(3, i + 2, student.getGrade()));
			}
			workbook.write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// ������д��������У�Ȼ��رչ����������ر������

			try {
				if (workbook != null)
					workbook.close();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void createAttendanceExcel(OutputStream os, List<Attendance> attendances, Long lectureId) {
		String[] heads = { "ѧ��", "����", "רҵ", "�꼶", "�Ƿ�ǩ��" };
		// ����������
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(os);
			// �����µ�һҳ��sheetֻ���ڹ�������ʹ��
			WritableSheet sheet = workbook.createSheet("attendance sheet1", 0);
			// ������Ԫ�񼴾���Ҫ��ʾ�����ݣ�new Label(0,0,"�û�") ��һ��������column �ڶ���������row
			// ������������content�����ĸ������ǿ�ѡ��,ΪLabel���������ʽ
			// ͨ��sheet��addCell�������Label��ע��һ��cell/labelֻ��ʹ��һ��addCell
			// ��һ������Ϊ�У��ڶ���Ϊ�У��������ı�����
			Lecture lecture = lectureService.findOne(lectureId);
			String[] lectureHeads = { lecture.getTitle(), lecture.getLecturer() };
			for (int i = 0; i < lectureHeads.length; i++) {// ��һ��
				sheet.addCell(new Label(i, 0, lectureHeads[i]));
			}
			// ����ֶ���
			for (int i = 0; i < heads.length; i++) {// �ڶ���
				sheet.addCell(new Label(i, 1, heads[i]));
			}
			// ����ֶ�����,�ӵ����п�ʼ
			for (int i = 0; i < attendances.size(); i++) {
				Attendance attendance = attendances.get(i);
				Student student = studentService.findOne(attendance.getStudentId());
				sheet.addCell(new Label(0, i + 2, student.getUsername()));
				sheet.addCell(new Label(1, i + 2, student.getName()));
				sheet.addCell(new Label(2, i + 2, student.getMajor()));
				sheet.addCell(new Label(3, i + 2, student.getGrade()));
				if (attendance.isAttended()) {
					sheet.addCell(new Label(4, i + 2, "��"));
				} else {
					sheet.addCell(new Label(4, i + 2, "��"));
				}
			}
			workbook.write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// ������д��������У�Ȼ��رչ����������ر������

			try {
				if (workbook != null)
					workbook.close();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (os != null)
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

	public AttendanceService getAttendanceService() {
		return attendanceService;
	}

	public User getCurrentUser() {
		return (User) session.getAttribute(Constants.CURRENT_USER);

	}
}
