package com.hewaiming.ALWorkInfo.bean;

public class RealRecord {
	private int PotNo;
	private String RecordNo;
	private String Param1;
	private String Param2;
	private String RecTime;

	public RealRecord() {

	}

	public RealRecord(int potNo, String recordNo, String param1, String param2, String recTime) {

		PotNo = potNo;
		RecordNo = recordNo;
		Param1 = param1;
		Param2 = param2;
		RecTime = recTime;
	}

	public int getPotNo() {
		return PotNo;
	}

	public void setPotNo(int potNo) {
		PotNo = potNo;
	}

	public String getRecordNo() {
		return RecordNo;
	}

	public void setRecordNo(String recordNo) {
		RecordNo = recordNo;
	}

	public String getParam1() {
		return Param1;
	}

	public void setParam1(String param1) {
		Param1 = param1;
	}

	public String getParam2() {
		return Param2;
	}

	public void setParam2(String param2) {
		Param2 = param2;
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
		return "RealRecord [PotNo=" + PotNo + ", RecordNo=" + RecordNo + ", Param1=" + Param1 + ", Param2=" + Param2
				+ ", RecTime=" + RecTime + "]";
	}

}
