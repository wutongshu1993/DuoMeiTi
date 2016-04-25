package admin;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import common.BuildingsInfo;
import model.AdminProfile;
import model.Repertory;
import model.Rules;
import model.StudentProfile;
import model.User;
import util.Const;
import utility.DatabaseOperation;

import model.DutyPiece;
import model.ExamStuScore;
import model.DutySchedule;
//import common.DutyInfo;
import model.ChooseClassSwitch;
import model.User;
import common.StudentInfo;

public class StudentManageAction extends ActionSupport{
	
	private String collegeSelect[];
	private static String sexSelect[];

	
	private String username;
	private String idCard;
	private String sex;
	private String rtID;
	private String studentId;
	private String bankCard;
	private String phoneNumber;
	public java.sql.Date entryTime;
	private String fullName;
	private String college;
	private String passwordAgain;
	private String strValue;
	private String name_id;
	private String search_select;
	private String isEmpty;
	private String test1;
	private int isPassed;
	private int userid;
	private int student_profile_id;
	private int isUpgradePrivilege;
	private int score;
	private String status;
	private List score_list;
	private String studenttable_jsp;
	private String isRepeat;//标记学号是否重复


	public List getScore_list() {
		return score_list;
	}


	public void setScore_list(List score_list) {
		this.score_list = score_list;
	}


	private static List<StudentProfile> student_list;
	private static List<User> user_list;
	private static StudentProfile edit_student;
	private static User edit_user;
	private String isUpgradePrivilegelist[];
	
	private String ruleText;//规章制度的内容,jsp页面传过来的内容
	private String textShow;//规章制度的内容，显示给jsp页面的内容
	private Date time;//规章制度的修改时间
	
	
	
	
	
	private List<BuildingsInfo> teahBuildings;
//	private List<DutyInfo> dutySchedule; 
	private List<StudentInfo> searchResult;
	private int teachBuildingId;
	private int student_Id;
	private int dtime;//值班时间
	private boolean chooseClassSwitch;
	private String log;
	private String studentName;
	private String isException;
	
	



	
	
	
	
	
	
	public String searchStudent() throws Exception{
		

		String conditions;
		if(studentName.length()>0&&studentId.length()>0)
			conditions="where s.studentId="+studentId+" and "+"s.user.fullName='"+studentName+"'";
		else if(studentName.length()>0)
			conditions="where s.user.fullName='"+studentName+"'";
		else 
			conditions="where s.studentId="+studentId;
		
		String hql="select new common.StudentInfo(s.id,s.user.fullName,s.studentId) from StudentProfile s "+conditions;
		Session session = model.Util.sessionFactory.openSession();
		searchResult=session.createQuery(hql).list();
		session.close();
		return SUCCESS;
	}

	
	
	/*
	 * 学生查找方法
	 * 如果姓名和学号都为空，返回null
	 * 如果姓名和学号都不为空，优先按学号搜索
	 * 结果通过list返回，如果isEmpty，说明没有找到
	 */
	public static List<StudentProfile> studentSearch(String name, String studentID){
		if(name.length()==0&&studentID.length()==0){
			return null;
		}
		List<StudentProfile> reList=new ArrayList<StudentProfile>();
		Session session=model.Util.sessionFactory.openSession();
		if(studentID.length()!=0){//按学号查找
			Criteria q = session.createCriteria(StudentProfile.class).add(Restrictions.eq("studentId", studentID))
					.add(Restrictions.eq("isPassed", model.StudentProfile.Passed));
			reList=q.list();
		}else{//按姓名查找
			Criteria q1 = session.createCriteria(User.class).add(Restrictions.eq("fullName", name));
			List<User> userList=q1.list();
			List<Integer> idList=new ArrayList<Integer>();
			if(!userList.isEmpty()){
				for(User user: userList){
					idList.add(user.getId());
				}
				Criteria q2 = session.createCriteria(StudentProfile.class)
						.add(Restrictions.in("user.id", idList))//查找所有符合要求d
						.add(Restrictions.eq("isPassed", model.StudentProfile.Passed));			
				reList=q2.list();
			}
		}
		session.close();
		return reList;
	}

	public String searchStudentInformation() throws Exception
	{
		System.out.println("searchStudentInformation():");
		if(search_select.equals("2")){//按学号查找
			student_list=studentSearch("", name_id);
		}
		else{//按姓名查找
			student_list=studentSearch(name_id, "");
		}
		if(student_list.isEmpty()){
			isEmpty = "0";
		}else{
			isEmpty = "1";
			score_list = new ArrayList<ExamStuScore>();
			Session session=model.Util.sessionFactory.openSession();
			for(int i = 0; i<student_list.size(); i++)
			{
				model.StudentProfile cnt_stu = (model.StudentProfile)student_list.get(i);
				Criteria sc = session.createCriteria(model.ExamStuScore.class)
							   .add(Restrictions.eq("stuPro.id",cnt_stu.id ))
							   .addOrder(Order.desc("id"));
				List<ExamStuScore> temp = sc.list();
				if(temp.size()>0){
					score_list.add(temp.get(0));
				}else{
					ExamStuScore s = new ExamStuScore();
					s.setScore(-1);
					score_list.add(s);
				}
				
			}
			session.close();
		}
		studenttable_jsp = util.Util.getJspOutput("/jsp/admin/student_manage/studenttable.jsp");
		System.out.println(score_list);
		return SUCCESS;
	}
	
	
 public int getStudent_profile_id() {
		return student_profile_id;
	}


	public void setStudent_profile_id(int student_profile_id) {
		this.student_profile_id = student_profile_id;
	}


public String saveStudentInformation() throws Exception
	{
		
		System.out.println("saveStudentInformation():");		
		System.out.println("id:"+edit_student.getId());
		isRepeat="0";
		if(!studentId.equals(edit_student.getStudentId())){
			System.out.println("修改学号");
			if(homepage.StudentAction.isRepeat(studentId)){
				System.out.println("chongfu");
				isRepeat="1";
				return SUCCESS;
			}
		}
		
		
		//更新学生数据,hql只更新部分字段
		Session session = model.Util.sessionFactory.openSession();
		session.beginTransaction();
		String hql = "update StudentProfile t set t.studentId = '"+studentId
				+ "', t.college = '"+college
				+ "', t.isUpgradePrivilege = '"+isUpgradePrivilege
				+ "', t.bankCard = '"+bankCard
				+ "', t.idCard = '"+idCard
				+ "' where id = "+edit_student.getId();
		System.out.println("hql:"+hql);
		Query query = session.createQuery(hql);
		query.executeUpdate(); 
		
		
		String hql2 = "update User t set t.fullName = '"+fullName
				+ "', t.sex = '"+sex
				+ "', t.phoneNumber = '"+phoneNumber
				+ "' where id = "+edit_student.getUser().getId();
		System.out.println("hql:"+hql2);
		Query query2 = session.createQuery(hql2);
		query2.executeUpdate(); 
		
		Transaction t = session.getTransaction();
		t.commit();
		session.close();
		return SUCCESS;
	}
	
	
	
	public String getStudentInformation() throws Exception
	{
		
		System.out.println("getStudentInformation():");
		System.out.println("edit_student_id:"+rtID);
		
		
		Session session=model.Util.sessionFactory.openSession();
		String hql = "SELECT rt FROM StudentProfile rt WHERE rt.id = " + rtID;
		System.out.println(hql);
		Query query = session.createQuery(hql);
		student_list = query.list();
		edit_student=student_list.get(0);
		
		
		
		String hql2 = "SELECT rt FROM User rt WHERE rt.id = " + edit_student.getUser().getId();
		Query query2 = session.createQuery(hql2);
		System.out.println(hql2);
		user_list=query2.list();
		edit_user = user_list.get(0);
		session.close();
		
		
		fullName = edit_user.getFullName();
		sex = edit_user.getSex();
		phoneNumber = edit_user.getPhoneNumber();
		college = edit_student.getCollege();
		studentId = edit_student.getStudentId();
		isUpgradePrivilege = edit_student.getIsUpgradePrivilege();
		bankCard = edit_student.getBankCard();
		idCard = edit_student.getIdCard();
		
		return SUCCESS;
	}
	
	
	
	
	
	
	
	public String studentInformationDelete() throws Exception
	{
		
		System.out.println("studentInformationDelete():");
		System.out.println(rtID);
		isException="0";
		for(StudentProfile student : student_list){
			if(student.getId()==Integer.parseInt(rtID)){
				edit_student = student;
				break;
			}
		}
		Session session = model.Util.sessionFactory.openSession();	
		try{
			
			//查找student对应的user
			Criteria q = session.createCriteria(User.class).add(Restrictions.eq("username",edit_student.getUser().getUsername())); //hibernate session创建查询
			user_list=q.list();
			Collections.reverse(user_list);
			//要删除的user
			edit_user = user_list.get(0);
			//必须同时删除student和user
			session.beginTransaction();
			session.delete(edit_student);
			session.delete(edit_user);
			Transaction t = session.getTransaction();
			t.commit();
		}catch(Exception e){
			e.printStackTrace();
            session.getTransaction().rollback();
            System.out.println("删除失败");
            isException="1";
		}
		finally{
			session.close();
		}
		return SUCCESS;
	}
	
	
	public String studentInformationEdit() throws Exception
	{
		
		System.out.println("studentInformationEdit():");
		System.out.println(rtID);
		return SUCCESS;

	}
	
	
	
	public String studentInformation() throws Exception{
		
		System.out.println("JJJJJJJJJJ");
		collegeSelect = Const.collegeSelect;
		sexSelect = Const.sexSelect;
		
		Session session = model.Util.sessionFactory.openSession();
		Criteria q = session.createCriteria(model.StudentProfile.class)
				.add(Restrictions.eq("isPassed", model.StudentProfile.Passed));
		
		
		student_list = q.list();
		score_list = new ArrayList<ExamStuScore>();
		for(int i = 0; i<student_list.size(); i++)
		{
			model.StudentProfile cnt_stu = (model.StudentProfile)student_list.get(i);
			Criteria sc = session.createCriteria(model.ExamStuScore.class)
						   .add(Restrictions.eq("stuPro.id",cnt_stu.id ))
						   .addOrder(Order.desc("id"));
			List<ExamStuScore> temp = sc.list();
			if(temp.size()>0)
			{
				score_list.add(temp.get(0));
			}
			else
			{
				ExamStuScore s = new ExamStuScore();
				s.setScore(-1);
				score_list.add(s);
			}
		}
		Collections.reverse(student_list);
		Collections.reverse(score_list);
		session.close();
		
		return ActionSupport.SUCCESS;
	
	}
	//编辑规章制度
	public String editRules() throws Exception{
		System.out.println("StudentManageAction.editRules()");
		
		Session session = model.Util.sessionFactory.openSession();
		//将前台传过来的文字 更新到数据库
		Criteria q = session.createCriteria(Rules.class);
		Rules rules = new Rules();
		
		if (q.list().size() == 0 ) {//如果现在表是空的，就插入到数据库中
			rules.setText(ruleText);
			rules.setTime(new Date(new java.util.Date().getTime()));
			session.beginTransaction();
			session.save(rules);
			session.getTransaction().commit();
			session.close();
			
		}
		else {//如果不是空的，就更新数据库
			q.add(Restrictions.eq("id",1));//默认就一条数据，所以id设为1
			rules = (Rules)q.uniqueResult();
			rules.setText(ruleText);
			rules.setTime(new Date(new java.util.Date().getTime()));
			session.beginTransaction();
			session.save(rules);
			session.getTransaction().commit();
			session.close();
		}
//		Criteria q = session.createCriteria(Rules.class).add(Restrictions.eq("id",
//					 1));//默认只有一条数据
		
		return ActionSupport.SUCCESS;
	}
	//显示规章制度
	public String showRules() throws Exception{
		System.out.println("StudentManageAction.showRules()");
		
		Session session = model.Util.sessionFactory.openSession();
		Criteria q = session.createCriteria(Rules.class);
		Rules temp;
		if (q.list().size() > 0) {
			temp = (Rules)q.list().get(0);//
			textShow = temp.getText();
		}
		else {
			textShow =" ";
		}
		session.close();
		return SUCCESS;
	}
	
	
	
	
	
	public String getName_id() {
		return name_id;
	}



	public String getSearch_select() {
		return search_select;
	}


	public void setSearch_select(String search_select) {
		this.search_select = search_select;
	}


	public String getIsEmpty() {
		return isEmpty;
	}


	public void setIsEmpty(String isEmpty) {
		this.isEmpty = isEmpty;
	}


	public void setName_id(String name_id) {
		this.name_id = name_id;
	}




	public String getTest1() {
		return test1;
	}





	public void setTest1(String test1) {
		this.test1 = test1;
	}





	public String[] getIsUpgradePrivilegelist() {
		return isUpgradePrivilegelist;
	}



	public void setIsUpgradePrivilegelist(String[] isUpgradePrivilegelist) {
		this.isUpgradePrivilegelist = isUpgradePrivilegelist;
	}



	public int getIsUpgradePrivilege() {
		return isUpgradePrivilege;
	}



	public void setIsUpgradePrivilege(int isUpgradePrivilege) {
		this.isUpgradePrivilege = isUpgradePrivilege;
	}



	public static List<User> getUser_list() {
		return user_list;
	}



	public static void setUser_list(List<User> user_list) {
		StudentManageAction.user_list = user_list;
	}



	public static StudentProfile getEdit_student() {
		return edit_student;
	}



	public static void setEdit_student(StudentProfile edit_student) {
		StudentManageAction.edit_student = edit_student;
	}



	public static User getEdit_user() {
		return edit_user;
	}



	public static void setEdit_user(User edit_user) {
		StudentManageAction.edit_user = edit_user;
	}



	public String[] getCollegeSelect() {
		return collegeSelect;
	}



	public void setCollegeSelect(String[] collegeSelect) {
		this.collegeSelect = collegeSelect;
	}



	public String[] getSexSelect() {
		return sexSelect;
	}



	public void setSexSelect(String[] sexSelect) {
		this.sexSelect = sexSelect;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getIdCard() {
		return idCard;
	}



	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}



	public String getSex() {
		return sex;
	}



	public void setSex(String sex) {
		this.sex = sex;
	}



	public String getRtID() {
		return rtID;
	}



	public void setRtID(String rtID) {
		this.rtID = rtID;
	}



	public String getStudentId() {
		return studentId;
	}



	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}



	public String getBankCard() {
		return bankCard;
	}



	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}



	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public java.sql.Date getEntryTime() {
		return entryTime;
	}



	public void setEntryTime(java.sql.Date entryTime) {
		this.entryTime = entryTime;
	}



	public String getFullName() {
		return fullName;
	}



	public void setFullName(String fullName) {
		this.fullName = fullName;
	}



	public String getCollege() {
		return college;
	}



	public void setCollege(String college) {
		this.college = college;
	}



	public String getPasswordAgain() {
		return passwordAgain;
	}



	public void setPasswordAgain(String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}



	public List<StudentProfile> getStudent_list() {
		return student_list;
	}



	public void setStudent_list(List<StudentProfile> student_list) {
		this.student_list = student_list;
	}



	public String getStrValue() {
		return strValue;
	}



	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}



	public int getIsPassed() {
		return isPassed;
	}



	public void setIsPassed(int isPassed) {
		this.isPassed = isPassed;
	}



	public int getUserid() {
		return userid;
	}



	public void setUserid(int userid) {
		this.userid = userid;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}
	
	



	
	

	public String getRuleText() {
		return ruleText;
	}


	public void setRuleText(String ruleText) {
		this.ruleText = ruleText;
	}


	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}


	public String getTextShow() {
		return textShow;
	}


	public void setTextShow(String textShow) {
		this.textShow = textShow;
	}
	
	
	
	
	
	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public List<StudentInfo> getSearchResult() {
		return searchResult;
	}


	public void setSearchResult(List<StudentInfo> searchResult) {
		this.searchResult = searchResult;
	}


	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}


	public int getDtime() {
		return dtime;
	}


	public void setDtime(int dtime) {
		this.dtime = dtime;
	}


	public int getStudent_Id() {
		return student_Id;
	}


	public void setStudent_Id(int student_Id) {
		this.student_Id = student_Id;
	}


	public String getLog() {
		return log;
	}


	public void setLog(String log) {
		this.log = log;
	}


	public boolean isChooseClassSwitch() {
		return chooseClassSwitch;
	}


	public void setChooseClassSwitch(boolean chooseClassSwitch) {
		this.chooseClassSwitch = chooseClassSwitch;
	}

//
//	public List<DutyInfo> getDutySchedule() {
//		return dutySchedule;
//	}
//
//
//	public void setDutySchedule(List<DutyInfo> dutySchedule) {
//		this.dutySchedule = dutySchedule;
//	}


	public int getTeachBuildingId() {
		return teachBuildingId;
	}


	public void setTeachBuildingId(int teachBuildingId) {
		this.teachBuildingId = teachBuildingId;
	}


	public List<BuildingsInfo> getTeahBuildings() {
		return teahBuildings;
	}


	public void setTeahBuildings(List<BuildingsInfo> teahBuildings) {
		this.teahBuildings = teahBuildings;
	}


	public String getStudenttable_jsp() {
		return studenttable_jsp;
	}


	public void setStudenttable_jsp(String studenttable_jsp) {
		this.studenttable_jsp = studenttable_jsp;
	}


	public String getIsException() {
		return isException;
	}


	public void setIsException(String isException) {
		this.isException = isException;
	}


	public String getIsRepeat() {
		return isRepeat;
	}


	public void setIsRepeat(String isRepeat) {
		this.isRepeat = isRepeat;
	}





	
}
