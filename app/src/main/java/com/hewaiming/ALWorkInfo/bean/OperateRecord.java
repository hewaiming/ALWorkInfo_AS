package com.hewaiming.ALWorkInfo.bean;

public class OperateRecord {
	private String ObjectName;
	private String ParamNameCH;
	private String Description;
	private String UserName;
	private String RecTime;	
	public OperateRecord() {
		super();
	}

	public OperateRecord(String objectName, String paramNameCH, String description, String userName, String recTime) {
	
		ObjectName = objectName;
		ParamNameCH = paramNameCH;
		Description = description;
		UserName = userName;
		RecTime = recTime;
	}

	public String getObjectName() {
		return ObjectName;
	}

	public void setObjectName(String objectName) {
		ObjectName = objectName;
	}

	public String getParamNameCH() {
		return ParamNameCH;
	}

	public void setParamNameCH(String paramNameCH) {
		ParamNameCH = paramNameCH;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getRecTime() {
		return RecTime;
	}

	public void setRecTime(String recTime) {

		String temp = recTime.substring(0, recTime.length() - 4);	
		RecTime = temp;
	}

	@Override
	public String toString() {
		return "OperateRecord [ObjectName=" + ObjectName + ", ParamNameCH=" + ParamNameCH + ", Description="
				+ Description + ", UserName=" + UserName + ", RecTime=" + RecTime + "]";
	}	

	
}
