package com.hewaiming.ALWorkInfo.bean;

public class SetParams {
	private int PotNo;	
	private double SetV;
	private int NBTime;
	private int AETime;
	public int getPotNo() {
		return PotNo;
	}
	public void setPotNo(int potNo) {
		PotNo = potNo;
	}
	public double getSetV() {
		return SetV;
	}
	public SetParams() {
		super();
	}
	public SetParams(int potNo, double setV, int nBTime, int aETime, int aLF) {		
		this.PotNo = potNo;
		this.SetV = setV;
		this.NBTime = nBTime;
		this.AETime = aETime;
		this.ALF = aLF;
	}
	public void setSetV(double setV) {
		SetV = setV;
	}
	public int getNBTime() {
		return NBTime;
	}
	public void setNBTime(int nBTime) {
		NBTime = nBTime;
	}
	public int getAETime() {
		return AETime;
	}
	public void setAETime(int aETime) {
		AETime = aETime;
	}
	public int getALF() {
		return ALF;
	}
	public void setALF(int aLF) {
		ALF = aLF;
	}
	private int ALF;
	@Override
	public String toString() {
		return "SetParams [PotNo=" + PotNo + ", SetV=" + SetV + ", NBTime=" + NBTime + ", AETime=" + AETime + ", ALF="
				+ ALF + "]";
	}	
	
}
