package com.hewaiming.ALWorkInfo.bean;

public class PotAge {
	public PotAge() {
		super();
	}
	public PotAge(int potNo, String beginTime, String endTime, int age) {
		super();
		PotNo = potNo;
		BeginTime = beginTime;
		EndTime = endTime;
		Age = age;
	}
	private int PotNo;	
	private String BeginTime;
	private String EndTime;
	private int Age;
	public int getPotNo() {
		return PotNo;
	}
	public void setPotNo(int potNo) {
		PotNo = potNo;
	}
	public String getBeginTime() {
		return BeginTime;
	}
	public void setBeginTime(String beginTime) {
		BeginTime = beginTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	public int getAge() {
		return Age;
	}
	public void setAge(int age) {
		Age = age;
	}
	@Override
	public String toString() {
		return "PotAge [PotNo=" + PotNo + ", BeginTime=" + BeginTime + ", EndTime=" + EndTime + ", Age=" + Age + "]";
	}
	
}
