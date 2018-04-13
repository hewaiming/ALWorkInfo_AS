package com.hewaiming.ALWorkInfo.bean;

import java.io.Serializable;

public class AvgV_Area implements Serializable {	
	private double AverageV;	
	private String Ddate;	
	

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
			String temp = ddate.substring(5, 10);
			Ddate = temp;
		}
	}

	public AvgV_Area() {

	}

	public AvgV_Area(double averageV, String ddate) {
		super();
		AverageV = averageV;
		Ddate = ddate;
	}

	@Override
	public String toString() {
		return "AvgV_Area [AverageV=" + AverageV + ", Ddate=" + Ddate + "]";
	}
	

}
