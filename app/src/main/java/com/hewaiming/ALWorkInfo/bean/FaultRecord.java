package com.hewaiming.ALWorkInfo.bean;

public class FaultRecord {		
	private int PotNo;		
	private String RecordNo;
	private String RecTime;
	public FaultRecord() {
	
	}	
public FaultRecord(int potNo, String recordNo, String recTime) {
		this.PotNo = potNo;
		this.RecordNo = recordNo;
		this.RecTime = recTime;
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
public String getRecTime() {
	return RecTime;
}
public void setRecTime(String recTime) {
	String temp=recTime.substring(0, recTime.length()-4);
	RecTime = temp;
}
@Override
public String toString() {
	return "FaultRecord [PotNo=" + PotNo + ", RecordNo=" + RecordNo + ", RecTime=" + RecTime + "]";
}
}
