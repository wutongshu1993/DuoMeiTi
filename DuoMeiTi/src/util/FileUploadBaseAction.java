package util;


import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Inject;

import model.EgFilePathSave;
import model.User;


public class FileUploadBaseAction extends ActionSupport{
    
    public File file; //上传的文件
    public String fileFileName; //文件名称
    public String fileContentType; //文件类型
}