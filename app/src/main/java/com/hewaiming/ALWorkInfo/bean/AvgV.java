package com.hewaiming.ALWorkInfo.bean;

import java.io.Serializable;

public class AvgV implements Serializable {
	private int PotNo;
	private String PotST;
	private double AverageV;	
	private String Ddate;	

	
	public int getPotNo() {
		return PotNo;
	}

	public void setPotNo(int potNo) {
		PotNo = potNo;
	}


	public String getPotST() {
		return PotST;
	}

	public void setPotST(String potST) {
		PotST = potST;
	}

	public double getAverageV() {
		return AverageV;
	}

	public void setAverageV(double averageV) {
		AverageV = averageV;
	}

	public String getDdate() {
		return Ddate;
	}

	public void setDdate(String ddate) {
		if (ddate.length() > 10) {
			String temp = ddate.substring(0, 10);
			Ddate = temp;
		}
	}

	public AvgV() {

	}

	public AvgV(int potNo, String potST, double averageV, String ddate) {
		super();
		PotNo = potNo;
		PotST = potST;
		AverageV = averageV;
		Ddate = ddate;
	}
	

}
