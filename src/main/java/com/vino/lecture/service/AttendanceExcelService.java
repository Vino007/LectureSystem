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
	 * 下载考勤excel
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
	 * 下载预约清单excel
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
	 * index从零开始 excel格式：第一行为标题，第2行开始为数据，第一列：用户名，第二列：用户别名
	 * 需要判断非空等校验,文件中某些为空的时候也可以导入，不会有异常
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
		// 创建输入流
		// 获取Excel文件对象
		if (file == null || !file.exists()) {
			System.out.println(file.getName() + "不存在！");
		}
		InputStream stream = new FileInputStream(file);
		rwb = Workbook.getWorkbook(stream);
		// 获取文件的指定工作表 默认的第一个
		Sheet sheet = rwb.getSheet(0);
		// 行数(表头的目录不需要，从1开始)
		for (int i = 2; i < sheet.getRows(); i++) {// 第3行开始
			// 创建一个attendance对象对应一行， 用来存储每一列的值
			attendance = new Attendance();
			// 列数
			cellStr = sheet.getCell(0, i).getContents().trim();// 学号
			attendance.setLectureId(lectureId);
			attendance.setStudentId(studentService.findByUsername(cellStr).getId());
			cellStr = sheet.getCell(4, i).getContents().trim();// isAttended
			if(cellStr.equals("是")){
				attendance.setAttended(true);
			}else{
				attendance.setAttended(false);
			}
			attendance.setCreateTime(new Date());
			attendance.setCreatorName(getCurrentUser().getUsername());

			// 把刚获取的列存入attendance
			attendances.add(attendance);
		}
		return attendances;
	}

	private void createReserveExcel(OutputStream os, List<Attendance> attendances, Long lectureId) {
		String[] heads = { "学号", "姓名", "专业", "年级" };
		// 创建工作区
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(os);
			// 创建新的一页，sheet只能在工作簿中使用
			WritableSheet sheet = workbook.createSheet("attendance sheet1", 0);
			// 创建单元格即具体要显示的内容，new Label(0,0,"用户") 第一个参数是column 第二个参数是row
			// 第三个参数是content，第四个参数是可选项,为Label添加字体样式
			// 通过sheet的addCell方法添加Label，注意一个cell/label只能使用一次addCell
			// 第一个参数为列，第二个为行，第三个文本内容
			Lecture lecture = lectureService.findOne(lectureId);
			String[] lectureHeads = { lecture.getTitle(), lecture.getLecturer() };
			for (int i = 0; i < lectureHeads.length; i++) {// 第一行
				sheet.addCell(new Label(i, 0, lectureHeads[i]));
			}
			// 添加字段名
			for (int i = 0; i < heads.length; i++) {// 第二行
				sheet.addCell(new Label(i, 1, heads[i]));
			}
			// 添加字段内容,从第三行开始
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
			// 将内容写到输出流中，然后关闭工作区，最后关闭输出流

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
		String[] heads = { "学号", "姓名", "专业", "年级", "是否签到" };
		// 创建工作区
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(os);
			// 创建新的一页，sheet只能在工作簿中使用
			WritableSheet sheet = workbook.createSheet("attendance sheet1", 0);
			// 创建单元格即具体要显示的内容，new Label(0,0,"用户") 第一个参数是column 第二个参数是row
			// 第三个参数是content，第四个参数是可选项,为Label添加字体样式
			// 通过sheet的addCell方法添加Label，注意一个cell/label只能使用一次addCell
			// 第一个参数为列，第二个为行，第三个文本内容
			Lecture lecture = lectureService.findOne(lectureId);
			String[] lectureHeads = { lecture.getTitle(), lecture.getLecturer() };
			for (int i = 0; i < lectureHeads.length; i++) {// 第一行
				sheet.addCell(new Label(i, 0, lectureHeads[i]));
			}
			// 添加字段名
			for (int i = 0; i < heads.length; i++) {// 第二行
				sheet.addCell(new Label(i, 1, heads[i]));
			}
			// 添加字段内容,从第三行开始
			for (int i = 0; i < attendances.size(); i++) {
				Attendance attendance = attendances.get(i);
				Student student = studentService.findOne(attendance.getStudentId());
				sheet.addCell(new Label(0, i + 2, student.getUsername()));
				sheet.addCell(new Label(1, i + 2, student.getName()));
				sheet.addCell(new Label(2, i + 2, student.getMajor()));
				sheet.addCell(new Label(3, i + 2, student.getGrade()));
				if (attendance.isAttended()) {
					sheet.addCell(new Label(4, i + 2, "是"));
				} else {
					sheet.addCell(new Label(4, i + 2, "否"));
				}
			}
			workbook.write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 将内容写到输出流中，然后关闭工作区，最后关闭输出流

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
