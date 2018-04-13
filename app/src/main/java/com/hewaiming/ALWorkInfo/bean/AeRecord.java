package com.hewaiming.ALWorkInfo.bean;

public class AeRecord {	
	private int PotNo;
	private String Status;	
	private double AverageV;		
	private int ContinueTime;
	private int WaitTime;	
	private double MaxV;	
	private String Ddate;	
	
	public AeRecord() {
		super();
	}

	public AeRecord(int potNo, String status, double averageV, int continueTime, int waitTime, double maxV,
			String ddate) {
		super();
		PotNo = potNo;
		Status = status;
		AverageV = averageV;
		ContinueTime = continueTime;
		WaitTime = waitTime;
		MaxV = maxV;
		Ddate = ddate;
	}

	public AeRecord(int potNo, int waitTime) {
		super();
		PotNo = potNo;
		WaitTime = waitTime;
	}

	public AeRecord(int potNo, int continueTime, String ddate) {
		super();
		PotNo = potNo;
		ContinueTime = continueTime;
		Ddate = ddate;
	}

	public int getPotNo() {
		return PotNo;
	}

	public void setPotNo(int potNo) {
		PotNo = potNo;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public double getAverageV() {
		return AverageV;
	}

	public void setAverageV(double averageV) {
		AverageV = averageV;
	}

	public int getContinueTime() {
		return ContinueTime;
	}

	public void setContinueTime(int continueTime) {
		ContinueTime = continueTime;
	}

	public int getWaitTime() {
		return WaitTime;
	}

	public void setWaitTime(int waitTime) {
		WaitTime = waitTime;
	}

	public double getMaxV() {
		return MaxV;
	}

	public void setMaxV(double maxV) {
		MaxV = maxV;
	}

	public String getDdate() {
		return Ddate;
	}

	public void setDdate(String recTime) {		
		String temp=recTime.substring(0, recTime.length()-4);
		Ddate = temp;
	}

	@Override
	public String toString() {
		return "AeRecord [PotNo=" + PotNo + ", Status=" + Status + ", AverageV=" + AverageV + ", ContinueTime="
				+ ContinueTime + ", WaitTime=" + WaitTime + ", MaxV=" + MaxV + ", Ddate=" + Ddate + "]";
	}
	
}
