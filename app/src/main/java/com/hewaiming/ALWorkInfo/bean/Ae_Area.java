package com.hewaiming.ALWorkInfo.bean;

import java.io.Serializable;

public class Ae_Area implements Serializable {		
	private String Ddate;	
	private int AeCnt; //效应数量
	private int Pots;  //槽子数量
	private double AeXS;	 //效应系数	

	public String getDdate() {
		return Ddate;
	}

	public void setDdate(String ddate) {
		if (ddate.length() > 10) {
			String temp = ddate.substring(5, 10);
			Ddate = temp;
		}
	}

	
	public int getAeCnt() {
		return AeCnt;
	}

	public void setAeCnt(int aeCnt) {
		AeCnt = aeCnt;
	}

	public int getPots() {
		return Pots;
	}

	public void setPots(int pots) {
		Pots = pots;
	}

	public double getAeXS() {
		return AeXS;
	}

	public void setAeXS(double aeXS) {
		AeXS = aeXS;
	}

	public Ae_Area() {

	}

	public Ae_Area(String ddate, int aeCnt, int pots, double aeXS) {
		super();
		Ddate = ddate;
		AeCnt = aeCnt;
		Pots = pots;
		AeXS = aeXS;
	}

	@Override
	public String toString() {
		return "Ae_Area [Ddate=" + Ddate + ", AeCnt=" + AeCnt + ", Pots=" + Pots + ", AeXS=" + AeXS + "]";
	}	

}
