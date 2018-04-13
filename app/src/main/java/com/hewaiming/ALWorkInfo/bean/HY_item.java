package com.hewaiming.ALWorkInfo.bean;

import java.io.Serializable;

public class HY_item implements Serializable {
	private String PotNo;
	private String Ddate;	
	private String FZB;
	public HY_item(String potNo, String ddate, String fZB) {
		super();
		PotNo = potNo;
		Ddate = ddate;
		FZB = fZB;
	}
	public HY_item() {
		super();
	}
	public String getPotNo() {
		return PotNo;
	}
	public void setPotNo(String potNo) {
		PotNo = potNo;
	}
	public String getDdate() {
		return Ddate;
	}
	public void setDdate(String ddate) {
		Ddate = ddate;
	}
	public String getFZB() {
		return FZB;
	}
	public void setFZB(String fZB) {
		FZB = fZB;
	}
	@Override
	public String toString() {
		return "FZB [PotNo=" + PotNo + ", Ddate=" + Ddate + ", FZB=" + FZB + "]";
	}
	
	
}
