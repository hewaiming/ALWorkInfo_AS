package com.hewaiming.ALWorkInfo.bean;

public class PotV {
	private int PotV;
	private int Cur;
	private String Ddate;

	public PotV(int potV, int cur, String ddate) {

		PotV = potV;
		Cur = cur;
		Ddate = ddate;
	}

	public PotV() {

	}

	public int getPotV() {
		return PotV;
	}

	public void setPotV(int potV) {
		PotV = potV;
	}

	public int getCur() {
		return Cur;
	}

	public void setCur(int cur) {
		Cur = cur;
	}

	@Override
	public String toString() {
		return "PotV [PotV=" + PotV + ", Cur=" + Cur + ", Ddate=" + Ddate + "]";
	}

	public String getDdate() {
		return Ddate;
	}

	public void setDdate(String ddate) {
		if (ddate.length() > 10) {
			String temp = ddate.substring(5, ddate.length() - 4);
			Ddate = temp;
		}
	}

}