package common;


import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import model.AdminProfile;
import model.CheckRecord;
import model.Classroom;
import model.RepairRecord;
import model.Repertory;
import model.RoomPicture;
import model.TeachBuilding;
import model.User;
import util.FileUploadBaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import Repair.RepairDAO;
import RepairImpl.RepairDAOImpl;
import db.MyHibernateSessionFactory;

public class ClassroomInformationAction extends FileUploadBaseAction{
//	public String build_name;
	public String remark;
	
//	public int picID;
//	public static int classroomId;
	
	public int classroomId;
	
//	public TeachBuilding building;
	public Classroom classroom;
//	public String schedulePath;
	public List<CheckRecord> checkrecords;
	public List<RepairRecord> repairrecords;
	public List<Repertory> repertory_list;
//	public List<RoomPicture>picture_list;
//	public List classroom_repertory_list;
	
	public String repairrecord_jsp;
	public String checkdetail;
	public String checkrecord_jsp;
//	public String classroomid;
	
	
	public String savestatus;
	public int deviceId;
	public String rtID;
	public String repairdetail;
	public String move_device_id;
//	public String move_class_id;
	public String device_jsp;
	public String alterdevice_jsp;

	
	public List<Repertory> rtClass;
	

	
	
	
/*	获取页面所需要的所有信息
 * 0是classroom
 * 1是rtClass 此教室设备列表
 * 2是checkrecords 此教室检查记录列表
 * 3是repairrecords 此教室维修记录列表
 * 
 */
//	
	public static Object[] obtainAllInfo(Session s, int classroomId)
	{
		Object[] ans = new Object[4];
		
//		System.out.println("admin.classroomaction:");
		Session session = model.Util.sessionFactory.openSession();
		
		//query current select classroom
		Criteria classroom_criteria = session.createCriteria(Classroom.class);
		Criteria building_criteria = session.createCriteria(TeachBuilding.class);
		classroom_criteria.add(Restrictions.eq("id", classroomId));
		Classroom classroom = (Classroom) classroom_criteria.uniqueResult();
//		building_criteria.add(Restrictions.eq("build_id", classroom.teachbuilding.build_id));
//		building = (TeachBuilding) building_criteria.uniqueResult();
		
//		ActionContext.getContext().getSession().remove("classroom_id");
//
//		ActionContext.getContext().getSession().put("classroom_id", classroom.id);

		
		
		List rtClass = session.createCriteria(model.Repertory.class)
						 .add(Restrictions.eq("rtClassroom.id", classroomId))
						 .list();



		//query at most 5 checkrecord
		Criteria checkrecord_criteria = session.
				createCriteria(CheckRecord.class);		
		checkrecord_criteria.add(Restrictions.eq("classroom.id", classroomId));
		checkrecord_criteria.addOrder(Order.desc("id"));
		checkrecord_criteria.setMaxResults(5);
		List checkrecords = checkrecord_criteria.list();

		
		

		//query at most 5 repairrecords
		List repairrecords= session.createCriteria(model.RepairRecord.class)
							  .add(Restrictions.eq("classroom.id", classroomId))
							  .addOrder(Order.desc("id"))
							  .setMaxResults(5)
							  .list();

		session.close();
		
		
		ans[0] = classroom;
		ans[1] = rtClass;
		ans[2] = checkrecords;
		ans[3] = repairrecords;
		return ans;
	}
	
	
	
	
	
	
	
	
	//检查记录删除
	public int checkRecordId;
	public String deleteCheckRecord() 
	{		
		System.out.println("deleteCheckRecord:" + checkRecordId);
		try
		{
			Session s = model.Util.sessionFactory.openSession();
			CheckRecord cr = (CheckRecord)
					s.createCriteria(model.CheckRecord.class)
					 .add(Restrictions.eq("id", checkRecordId))
					 .uniqueResult();
			
			s.beginTransaction();
			s.delete(cr);
			s.getTransaction().commit();
			s.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		
		}
		
		return ActionSupport.SUCCESS;
	}
	
	
	//备用设备
	public String alterdevice() 
	{		
		System.out.println("ENTER 备用设备gggggggggggggggg---------");
		Session session = model.Util.sessionFactory.openSession();
		repertory_list = session.createCriteria(model.Repertory.class)
				.add(Restrictions.eq("rtDeviceStatus", util.Util.DeviceBackupStatus ))
				.list();
		
		alterdevice_jsp = util.Util.getJspOutput("/jsp/classroom/alterdevice.jsp");
		session.close();
		return ActionSupport.SUCCESS;
	}
	
	
//	按照资产编号加入教室
	String rtNumber;
	public String move2classByRtNumber()
	{
		System.out.println("JJJJJ=========");
		
		int user_id = (int) ActionContext.getContext().getSession().get("user_id");
		Session s = model.Util.sessionFactory.openSession();
		
		
		List<Repertory> repertoryList = s.createCriteria(model.Repertory.class)
										.add(Restrictions.eq("rtNumber", rtNumber))
										.list();
		for(int i = 0; i < repertoryList.size(); ++ i)
		{
					
			util.Util.modifyDeviceStatus(
					s,
					repertoryList.get(i).rtId, 
					user_id, 
					util.Util.DeviceClassroomStatus, 
					classroomId);

		}

		
		return this.SUCCESS;
	}
	//加入教室
	public String move2class(){

		
		int user_id = (int) ActionContext.getContext().getSession().get("user_id");
		
		Session session = model.Util.sessionFactory.openSession();
		util.Util.modifyDeviceStatus(
									session,
									Integer.parseInt(rtID), 
									user_id, 
									util.Util.DeviceClassroomStatus, 
									classroomId);
		
		
		rtClass = session.createCriteria(model.Repertory.class)
				 .add(Restrictions.eq("rtClassroom.id", classroomId ))
				 .list();
		
		repertory_list = session.createCriteria(model.Repertory.class)
						.add(Restrictions.eq("rtDeviceStatus", util.Util.DeviceBackupStatus ))
						.list();

		
		device_jsp = util.Util.getJspOutput("/jsp/classroom/device.jsp");
		alterdevice_jsp = util.Util.getJspOutput("/jsp/classroom/alterdevice.jsp");
		
		return ActionSupport.SUCCESS;
	}
	
	
	//移入维修
	public String move2repair()
	{

		System.out.println("移入维修！！！！！！！！！");
		System.out.println(classroomId);
		int user_id = (int) ActionContext.getContext().getSession().get("user_id");
		Session session = model.Util.sessionFactory.openSession();
		util.Util.modifyDeviceStatus(
									session,
									Integer.parseInt(move_device_id), 
//									classroomId,
									user_id, 
									util.Util.DeviceRepairStatus, 
									-1);

	
		rtClass = session.createCriteria(model.Repertory.class)
				 .add(Restrictions.eq("rtClassroom.id", classroomId ))
				 .list();
		
		device_jsp = util.Util.getJspOutput("/jsp/classroom/device.jsp");
		
		
		return ActionSupport.SUCCESS;
	}
	
	//移入报废
	public String move2bad(){
		

		int user_id = (int) ActionContext.getContext().getSession().get("user_id");
		Session session = model.Util.sessionFactory.openSession();
		util.Util.modifyDeviceStatus(
									session,
									Integer.parseInt(move_device_id), 
									user_id, 
									util.Util.DeviceScrappedStatus, 
									-1);
		
		rtClass = session.createCriteria(model.Repertory.class)
				 .add(Restrictions.eq("rtClassroom.id", classroomId ))
				 .list();
		device_jsp = util.Util.getJspOutput("/jsp/classroom/device.jsp");
		
		
		return ActionSupport.SUCCESS;
	}
	
	
	
	//维修记录
	public String repairrecord_save() {
		System.out.println("admin.repairrecord:");
		Session session = null;
		try	{
			int user_id = (int) ActionContext.getContext().getSession().get("user_id");
			session = model.Util.sessionFactory.openSession();
			
			User repairman = (User) session.createCriteria(User.class)
								.add(Restrictions.eq("id", user_id))
								.uniqueResult();

			Repertory device = (Repertory)session.createCriteria(Repertory.class)
								.add(Restrictions.eq("rtId", deviceId))
								.uniqueResult();
			
			
			RepairRecord repairrecord = new RepairRecord();
			repairrecord.setDevice(device);
			repairrecord.setRepairdate(new Timestamp(new java.util.Date().getTime()));
			repairrecord.setRepairdetail(repairdetail);
			repairrecord.setRepairman(repairman);
			

			Classroom cl = (Classroom) 
					session.createCriteria(model.Classroom.class)
					.add(Restrictions.eq("id", classroomId)).uniqueResult();
			

			repairrecord.setClassroom(cl);
			
			
			
			
			session.beginTransaction();
			session.save(repairrecord);
			session.getTransaction().commit();
			

			
//			repairrecords = (List) session.createQuery("select rd "
//					+ "from RepairRecord as rd "
//					+ "left join rd.device as ry "
//					+ "left join ry.rtClassroom as cm  "
//					+ "where cm.id=" + classroomId + " order by rd.id desc")
//						.setMaxResults(5).list();
			repairrecords= session.createCriteria(model.RepairRecord.class)
					  .add(Restrictions.eq("classroom.id", classroomId))
					  .addOrder(Order.desc("id"))
					  .setMaxResults(5)
					  .list();

			
			
			repairrecord_jsp = util.Util.getJspOutput("/jsp/classroom/repairrecord.jsp");
						
			
			this.savestatus = "success";
		} catch(Exception e)	{
			this.savestatus = "fail";
			e.printStackTrace();
		} finally {
			if(session != null) session.close();
		}
		return SUCCESS;
	}
	
	//检查记录
	public String checkrecord_save() {
		System.out.println("admin.checkrecord:");
		Session session = null;
		try	{
			int user_id = (int) ActionContext.getContext().getSession().get("user_id");
			session = model.Util.sessionFactory.openSession();
			
			User user = (User)session.createCriteria(User.class)
						.add(Restrictions.eq("id", user_id))
						.uniqueResult();




	
			Classroom classroom = (Classroom)session.createCriteria(Classroom.class)
								 .add(Restrictions.eq("id", classroomId  ))
								 .uniqueResult();
			
			
			
			CheckRecord checkrecord = new CheckRecord();
			checkrecord.setCheckdate(new Timestamp(new java.util.Date().getTime()));
			checkrecord.setCheckdetail(checkdetail);
			checkrecord.setCheckman(user);
			checkrecord.setClassroom(classroom);
			
			session.beginTransaction();
			session.save(checkrecord);
			session.getTransaction().commit();
			
			checkrecords = session.createCriteria(CheckRecord.class)
					.add(Restrictions.eq("classroom.id", classroomId))
					.addOrder(Order.desc("id"))
					.setMaxResults(5)
					.list();
//			checkrecord_criteria.add(Restrictions.eq("classroom.id", classroomId));
//			checkrecord_criteria.addOrder(Order.desc("id"));
//			checkrecord_criteria.setMaxResults(5);
//			checkrecords = checkrecord_criteria.list();
			
			checkrecord_jsp = util.Util.getJspOutput("/jsp/classroom/checkrecord.jsp");
			
			this.savestatus = "success";
		} catch(Exception e)	
		{
			this.savestatus = "fail";
			e.printStackTrace();
		} finally {
			if(session != null) session.close();
		}
		return SUCCESS;
	}
	
	
	
//	public void ClassroomPicture(){
//		System.out.println("ClassroomPicture");
//		System.out.println(classroomId);
//		
//		
//		Session session = model.Util.sessionFactory.openSession();
//		Criteria c1 = session.createCriteria(RoomPicture.class).add(Restrictions.eq("class_id",classroomId)); //hibernate session创建查询
//		picture_list = c1.list();
//		
//		Criteria q = session.createCriteria(Classroom.class).add(Restrictions.eq("id",classroomId)); //hibernate session创建查询
//		List<Classroom> class_list = q.list();
//		Collections.reverse(class_list);
//		schedulePath = class_list.get(0).getClass_schedule_path();
//		session.close();
//		
//		
//		System.out.println(picture_list);
//		System.out.println(schedulePath);
//	}
	
	
	
//	public String PictureUpload() {
//		System.out.println("PictureUpload!");
//		System.out.println("calssid" + classroomId);
//		System.out.println(remark);
//		
//		Session session = model.Util.sessionFactory.openSession();
//		RoomPicture nPicture = new RoomPicture();	
//		
//		nPicture.setClass_id(classroomId);
//		nPicture.setRemark(remark);
//		
//		//获取当前时间，命名照片，防止照片重复
//		java.util.Date date = new java.util.Date();
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddHHmmss");
//		String curdate = simpleDateFormat.format(date);
//		String fileName = curdate+fileFileName.substring(fileFileName.length()-5, fileFileName.length());
//		if (file != null)//file没接收到的原因可能是jsp页面里面的input file的属性名不是file 
//        {
//			util.Util.saveFile(file, fileName, util.Util.RootPath + util.Util.ClassroomInfoFilePath);
//			String inserted_file_path = util.Util.ClassroomInfoFilePath +fileName;
//			nPicture.setPath(inserted_file_path);
//        }
//		
//		session.beginTransaction();
//		session.save(nPicture);
//		Transaction t = session.getTransaction();
//		t.commit();
//		
//		return ActionSupport.SUCCESS;
//	}
//	
//	
//	
//	public String PictureDelete() {
//		System.out.println("PictureDelete:");
//		System.out.println(picID);
//		
//		//从数据库中删除
//		Session session = model.Util.sessionFactory.openSession();		
//		Criteria q = session.createCriteria(RoomPicture.class).add(Restrictions.eq("id",picID)); //hibernate session创建查询
//		picture_list=q.list();
//		Collections.reverse(picture_list);
//		RoomPicture nPicture = new RoomPicture();
//		nPicture = picture_list.get(0);
//		//System.out.println(nPicture);
//		session.beginTransaction();
//		session.delete(nPicture);
//		Transaction t = session.getTransaction();
//		t.commit();
//		session.close();
//		//删除本地文件
//		String filePath = util.Util.RootPath + nPicture.getPath();
//		System.out.println(filePath);
//		util.Util.deleteFile(filePath);
//		
//		return ActionSupport.SUCCESS;
//	}
//	
//	public String ScheduleUpload(){
//		System.out.println("ScheduleUpload:");
//		System.out.println("calssid" + classroomId);
//		System.out.println("fileFileName" + fileFileName);
//		
//		Session session = model.Util.sessionFactory.openSession();		
//		Criteria q = session.createCriteria(Classroom.class).add(Restrictions.eq("id",classroomId)); //hibernate session创建查询
//		List<Classroom> class_list = q.list();
//		Collections.reverse(class_list);
//		Classroom nClass = class_list.get(0);
//
//		if (file != null)//file没接收到的原因可能是jsp页面里面的input file的属性名不是file 
//        {
//			File old_class_schedule = new File(util.Util.RootPath + nClass.getClass_schedule_path());
//	    	if(!old_class_schedule.delete()) // 删除旧课表
//	    	{
////	    		this.status = "1";
////				this.message = "系统出现致命错误！！！！！！";
//	    		System.out.println("系统出现致命错误！！！！！！");
////				return SUCCESS;
//	    	}
//	    	System.out.println("**"+nClass.getClassroom_num()+"**");
//			String newFileName = nClass.getTeachbuilding().getBuild_name() + "-" + nClass.getClassroom_num() 
//								 + fileFileName.substring(fileFileName.lastIndexOf("."));
//			
//			util.Util.saveFile(file, newFileName,  util.Util.RootPath + util.Util.ClassroomSchedulePath);
//			String inserted_file_path = util.Util.ClassroomSchedulePath +newFileName;
//			nClass.setClass_schedule_path(inserted_file_path);
//			System.out.println("inserted_file_path" + inserted_file_path);
//        }
//	
//		session.beginTransaction();
//		session.update(nClass);
//		Transaction t = session.getTransaction();
//		t.commit();
//		session.close();
//		return ActionSupport.SUCCESS;
//	}

	
	public String getRemark() {
		return remark;
	}




	public void setRemark(String remark) {
		this.remark = remark;
	}




//	public int getPicID() {
//		return picID;
//	}
//
//
//
//
//	public void setPicID(int picID) {
//		this.picID = picID;
//	}



//
//	public TeachBuilding getBuilding() {
//		return building;
//	}
//
//
//
//
//	public void setBuilding(TeachBuilding building) {
//		this.building = building;
//	}
//



//	public Classroom getClassroom() {
//		return classroom;
//	}
//
//
//
//
//	public void setClassroom(Classroom classroom) {
//		this.classroom = classroom;
//	}




//	public String getSchedulePath() {
//		return schedulePath;
//	}
//
//
//
//
//	public void setSchedulePath(String schedulePath) {
//		this.schedulePath = schedulePath;
//	}




	public List<CheckRecord> getCheckrecords() {
		return checkrecords;
	}




	public void setCheckrecords(List<CheckRecord> checkrecords) {
		this.checkrecords = checkrecords;
	}




	public List<RepairRecord> getRepairrecords() {
		return repairrecords;
	}




	public void setRepairrecords(List<RepairRecord> repairrecords) {
		this.repairrecords = repairrecords;
	}



//
//	public List<RoomPicture> getPicture_list() {
//		return picture_list;
//	}
//
//
//
//
//	public void setPicture_list(List<RoomPicture> picture_list) {
//		this.picture_list = picture_list;
//	}
//



//	public List getClassroom_repertory_list() {
//		return classroom_repertory_list;
//	}
//
//
//
//
//	public void setClassroom_repertory_list(List classroom_repertory_list) {
//		this.classroom_repertory_list = classroom_repertory_list;
//	}




	public String getCheckdetail() {
		return checkdetail;
	}




	public void setCheckdetail(String checkdetail) {
		this.checkdetail = checkdetail;
	}









	public String getCheckrecord_jsp() {
		return checkrecord_jsp;
	}

	public void setCheckrecord_jsp(String checkrecord_jsp) {
		this.checkrecord_jsp = checkrecord_jsp;
	}

//	public String getClassroomid() {
//		return classroomid;
//	}
//
//
//
//
//	public void setClassroomid(String classroomid) {
//		this.classroomid = classroomid;
//	}




	public String getSavestatus() {
		return savestatus;
	}




	public void setSavestatus(String savestatus) {
		this.savestatus = savestatus;
	}




	public List<Repertory> getRtClass() {
		return rtClass;
	}




	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getRepairdetail() {
		return repairdetail;
	}

	public void setRepairdetail(String repairdetail) {
		this.repairdetail = repairdetail;
	}

	public void setRtClass(List<Repertory> rtClass) {
		this.rtClass = rtClass;
	}




//	public String getBuild_name() {
//		return build_name;
//	}
//
//	public void setBuild_name(String build_name) {
//		this.build_name = build_name;
//	}

	public int getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(int classroomId) {
		this.classroomId = classroomId;
	}

	public String getRepairrecord_jsp() {
		return repairrecord_jsp;
	}

	public void setRepairrecord_jsp(String repairrecord_jsp) {
		this.repairrecord_jsp = repairrecord_jsp;
	}


	public String getMove_device_id() {
		return move_device_id;
	}


	public void setMove_device_id(String move_device_id) {
		this.move_device_id = move_device_id;
	}


//	public String getMove_class_id() {
//		return move_class_id;
//	}
//
//
//	public void setMove_class_id(String move_class_id) {
//		this.move_class_id = move_class_id;
//	}


	public String getDevice_jsp() {
		return device_jsp;
	}


	public void setDevice_jsp(String device_jsp) {
		this.device_jsp = device_jsp;
	}

	public String getAlterdevice_jsp() {
		return alterdevice_jsp;
	}

	public void setAlterdevice_jsp(String alterdevice_jsp) {
		this.alterdevice_jsp = alterdevice_jsp;
	}


	public List<Repertory> getRepertory_list() {
		return repertory_list;
	}


	public void setRepertory_list(List<Repertory> repertory_list) {
		this.repertory_list = repertory_list;
	}


	public String getRtID() {
		return rtID;
	}


	public void setRtID(String rtID) {
		this.rtID = rtID;
	}


	public String getRtNumber() {
		return rtNumber;
	}


	public void setRtNumber(String rtNumber) {
		this.rtNumber = rtNumber;
	}


	public Classroom getClassroom() {
		return classroom;
	}


	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}


	public int getCheckRecordId() {
		return checkRecordId;
	}


	public void setCheckRecordId(int checkRecordId) {
		this.checkRecordId = checkRecordId;
	}
	
	

}