package com.hewaiming.ALWorkInfo.bean;

import java.io.Serializable;

public class MeasueTable implements Serializable {
	private String PotNo;
	private String Ddate;
	private String ALCnt;
	private String LSP;
	private String DJZSP;
	private String DJWD;
	private String FZB;
	private String FeCnt;
	private String SiCnt;
	private String ALOCnt;
	private String CaFCnt;
	private String MgCnt;
	private String MLSP;
	private String LDYJ;  
	private String JHCL;    //计划出铝量	
	public MeasueTable(String potNo, String ddate, String aLCnt, String lSP, String dJZSP, String dJWD, String fZB,
			String feCnt, String siCnt, String aLOCnt, String caFCnt, String mgCnt, String mLSP, String lDYJ,
			String jHCL) {
		super();
		PotNo = potNo;
		Ddate = ddate;
		ALCnt = aLCnt;
		LSP = lSP;
		DJZSP = dJZSP;
		DJWD = dJWD;
		FZB = fZB;
		FeCnt = feCnt;
		SiCnt = siCnt;
		ALOCnt = aLOCnt;
		CaFCnt = caFCnt;
		MgCnt = mgCnt;
		MLSP = mLSP;
		LDYJ = lDYJ;
		JHCL = jHCL;
	}
	public MeasueTable() {
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
		
		if (ddate.length() > 10) {
			String temp = ddate.substring(0, 10);
			Ddate = temp;
		}
	}
	public String getALCnt() {
		return ALCnt;
	}
	public void setALCnt(String aLCnt) {
		ALCnt = aLCnt;
	}
	public String getLSP() {
		return LSP;
	}
	public void setLSP(String lSP) {
		LSP = lSP;
	}
	public String getDJZSP() {
		return DJZSP;
	}
	public void setDJZSP(String dJZSP) {
		DJZSP = dJZSP;
	}
	public String getDJWD() {
		return DJWD;
	}
	public void setDJWD(String dJWD) {
		DJWD = dJWD;
	}
	public String getFZB() {
		return FZB;
	}
	public void setFZB(String fZB) {
		FZB = fZB;
	}
	public String getFeCnt() {
		return FeCnt;
	}
	public void setFeCnt(String feCnt) {
		FeCnt = feCnt;
	}
	public String getSiCnt() {
		return SiCnt;
	}
	public void setSiCnt(String siCnt) {
		SiCnt = siCnt;
	}
	public String getALOCnt() {
		return ALOCnt;
	}
	public void setALOCnt(String aLOCnt) {
		ALOCnt = aLOCnt;
	}
	public String getCaFCnt() {
		return CaFCnt;
	}
	public void setCaFCnt(String caFCnt) {
		CaFCnt = caFCnt;
	}
	public String getMgCnt() {
		return MgCnt;
	}
	public void setMgCnt(String mgCnt) {
		MgCnt = mgCnt;
	}
	public String getMLSP() {
		return MLSP;
	}
	public void setMLSP(String mLSP) {
		MLSP = mLSP;
	}
	public String getLDYJ() {
		return LDYJ;
	}
	public void setLDYJ(String lDYJ) {
		LDYJ = lDYJ;
	}
	public String getJHCL() {
		return JHCL;
	}
	public void setJHCL(String jHCL) {
		JHCL = jHCL;
	}
	@Override
	public String toString() {
		return "MeasueTable [PotNo=" + PotNo + ", Ddate=" + Ddate + ", ALCnt=" + ALCnt + ", LSP=" + LSP + ", DJZSP="
				+ DJZSP + ", DJWD=" + DJWD + ", FZB=" + FZB + ", FeCnt=" + FeCnt + ", SiCnt=" + SiCnt + ", ALOCnt="
				+ ALOCnt + ", CaFCnt=" + CaFCnt + ", MgCnt=" + MgCnt + ", MLSP=" + MLSP + ", LDYJ=" + LDYJ + ", JHCL="
				+ JHCL + "]";
	}

	
}
