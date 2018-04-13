package com.hewaiming.ALWorkInfo.bean;

import java.io.Serializable;

public class DJWD implements Serializable {
	private String PotNo;
	private String Ddate;	
	private String DJWD;
	public DJWD(String potNo, String ddate, String dJWD) {
		super();
		PotNo = potNo;
		Ddate = ddate;
		DJWD = dJWD;
	}
	public DJWD() {
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
	public String getDJWD() {
		return DJWD;
	}
	public void setDJWD(String dJWD) {
		DJWD = dJWD;
	}
	@Override
	public String toString() {
		return "DJWD [PotNo=" + PotNo + ", Ddate=" + Ddate + ", DJWD=" + DJWD + "]";
	}	
	
}
