package com.hewaiming.ALWorkInfo.bean;

import java.io.Serializable;

public class dayTable implements Serializable {
	private int PotNo;
	private String PotSt;
	private int SetNB;
	private int RunTime;
	private int JLCnt;//加工次数
	private double AverageV;	
	private double RealSetV;
	private double SetV;
	private double WorkV;
	private double AeV;
	private int AeTime;
	private int AeCnt;
	private int DybTime;
	private int YhlCnt;//氧化铝下料量
	private int FhlCnt;//氟化铝下料量
	private int AlCntZSL;  //指示出铝量
	private int ZF;    //噪音
	private String Ddate;		


	public dayTable(int potNo, String potSt, int setNB, int runTime, int jLCnt, double averageV, double realSetV,
			double setV, double workV, double aeV, int aeTime, int aeCnt, int dybTime, int yhlCnt, int fhlCnt,
			int alCntZSL, int zF, String ddate) {
		super();
		PotNo = potNo;
		PotSt = potSt;
		SetNB = setNB;
		RunTime = runTime;
		JLCnt = jLCnt;
		AverageV = averageV;
		RealSetV = realSetV;
		SetV = setV;
		WorkV = workV;
		AeV = aeV;
		AeTime = aeTime;
		AeCnt = aeCnt;
		DybTime = dybTime;
		YhlCnt = yhlCnt;
		FhlCnt = fhlCnt;
		AlCntZSL = alCntZSL;
		ZF = zF;
		Ddate = ddate;
	}

	public int getPotNo() {
		return PotNo;
	}

	public int getYhlCnt() {
		return YhlCnt;
	}

	
	public int getJLCnt() {
		return JLCnt;
	}


	public void setJLCnt(int jLCnt) {
		JLCnt = jLCnt;
	}


	public void setYhlCnt(int yhlCnt) {
		YhlCnt = yhlCnt;
	}

	public int getFhlCnt() {
		return FhlCnt;
	}

	public void setFhlCnt(int fhlCnt) {
		FhlCnt = fhlCnt;
	}

	public void setPotNo(int potNo) {
		PotNo = potNo;
	}

	public String getPotSt() {
		return PotSt;
	}

	public void setPotSt(String potSt) {
		PotSt = potSt;
	}

	public double getAverageV() {
		return AverageV;
	}

	public void setAverageV(double averageV) {
		AverageV = averageV;
	}

	public double getRealSetV() {
		return RealSetV;
	}

	public void setRealSetV(double realSetV) {
		RealSetV = realSetV;
	}

	public double getSetV() {
		return SetV;
	}

	public void setSetV(double setV) {
		SetV = setV;
	}

	public double getWorkV() {
		return WorkV;
	}

	public void setWorkV(double workV) {
		WorkV = workV;
	}

	public double getAeV() {
		return AeV;
	}

	public void setAeV(double aeV) {
		AeV = aeV;
	}

	public int getAeTime() {
		return AeTime;
	}

	public void setAeTime(int aeTime) {
		AeTime = aeTime;
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

	public dayTable() {

	}

	public int getSetNB() {
		return SetNB;
	}

	public void setSetNB(int setNB) {
		SetNB = setNB;
	}
	public int getRunTime() {
		return RunTime;
	}

	public void setRunTime(int runTime) {
		RunTime = runTime;
	}

	public int getAeCnt() {
		return AeCnt;
	}

	public void setAeCnt(int aeCnt) {
		AeCnt = aeCnt;
	}

	public int getDybTime() {
		return DybTime;
	}

	public void setDybTime(int dybTime) {
		DybTime = dybTime;
	}

	public int getAlCntZSL() {
		return AlCntZSL;
	}

	public void setAlCntZSL(int alCntZSL) {
		AlCntZSL = alCntZSL;
	}

	public int getZF() {
		return ZF;
	}

	public void setZF(int zF) {
		ZF = zF;
	}

	@Override
	public String toString() {
		return "dayTable [PotNo=" + PotNo + ", PotSt=" + PotSt + ", SetNB=" + SetNB + ", RunTime=" + RunTime
				+ ", JLCnt=" + JLCnt + ", AverageV=" + AverageV + ", RealSetV=" + RealSetV + ", SetV=" + SetV
				+ ", WorkV=" + WorkV + ", AeV=" + AeV + ", AeTime=" + AeTime + ", AeCnt=" + AeCnt + ", DybTime="
				+ DybTime + ", YhlCnt=" + YhlCnt + ", FhlCnt=" + FhlCnt + ", AlCntZSL=" + AlCntZSL + ", ZF=" + ZF
				+ ", Ddate=" + Ddate + "]";
	}


}
