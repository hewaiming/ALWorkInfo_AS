package com.hewaiming.ALWorkInfo.bean;

public class FaultMost {
	private int PotNo;
	private int FaultCnt;
	public FaultMost(int potNo, int faultCnt) {
		super();
		PotNo = potNo;
		FaultCnt = faultCnt;
	}
	public FaultMost() {
		super();
	}
	public int getPotNo() {
		return PotNo;
	}
	public void setPotNo(int potNo) {
		PotNo = potNo;
	}
	public int getFaultCnt() {
		return FaultCnt;
	}
	public void setFaultCnt(int faultCnt) {
		FaultCnt = faultCnt;
	}
	@Override
	public String toString() {
		return "FaultMost [PotNo=" + PotNo + ", FaultCnt=" + FaultCnt + "]";
	}

}
