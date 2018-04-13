package com.hewaiming.ALWorkInfo.bean;

public class PotCtrl {
	private int PotNo;		
	private int Ctrls;
	public PotCtrl(int potNo, int ctrls) {
		super();
		PotNo = potNo;
		Ctrls = ctrls;
	}
	public PotCtrl() {
		super();
	}
	public int getPotNo() {
		return PotNo;
	}
	public void setPotNo(int potNo) {
		PotNo = potNo;
	}
	public int getCtrls() {
		return Ctrls;
	}
	public void setCtrls(int ctrls) {
		Ctrls = ctrls;
	}
	@Override
	public String toString() {
		return "PotCtrl [PotNo=" + PotNo + ", Ctrls=" + Ctrls + "]";
	}	
	
}
