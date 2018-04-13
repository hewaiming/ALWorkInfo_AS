package com.hewaiming.ALWorkInfo.bean;

public class PotV_plus {
	private int PotV;
	private int TargetV;
	private int MoreLess;
	private int Interval;
	private int Cur;
	private int Action;
	private String Ddate;	
	
	public PotV_plus() {
		super();
	}
	

	public PotV_plus(int potV, int targetV, int moreLess, int interval, int cur, int action, String ddate) {
		super();
		PotV = potV;
		TargetV = targetV;
		MoreLess = moreLess;
		Interval = interval;
		Cur = cur;
		Action = action;
		Ddate = ddate;
	}


	public int getInterval() {
		return Interval;
	}

	public void setInterval(int interval) {
		Interval = interval;
	}

	public int getMoreLess() {
		return MoreLess;
	}



	public void setMoreLess(int moreLess) {
		MoreLess = moreLess;
	}



	public int getTargetV() {
		return TargetV;
	}

	public void setTargetV(int targetV) {
		TargetV = targetV;
	}

	public int getAction() {
		return Action;
	}

	public void setAction(int action) {
		Action = action;
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
	

	public String getDdate() {
		return Ddate;
	}

	public void setDdate(String ddate) {
		if (ddate.length() > 10) {
			String temp = ddate.substring(8, ddate.length() - 7);
			Ddate = temp;
		}
	}


	@Override
	public String toString() {
		return "PotV_plus [PotV=" + PotV + ", TargetV=" + TargetV + ", MoreLess=" + MoreLess + ", Interval=" + Interval
				+ ", Cur=" + Cur + ", Action=" + Action + ", Ddate=" + Ddate + "]";
	}	
	

}