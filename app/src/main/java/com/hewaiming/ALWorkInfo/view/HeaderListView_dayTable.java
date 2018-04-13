package com.hewaiming.ALWorkInfo.view;


import com.hewaiming.ALWorkInfo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;

public class HeaderListView_dayTable extends LinearLayout {
	private Context context;
	private TextView PotNo, PotSt,RunTime,HylCnt,JLCnt,SetV,RealSetV,WorkV,AverageV,AeV,AeTime,AeCnt,DybTime,Ddate;

	public void setTvPotNo(String tvPotNo) {
		this.PotNo.setText(tvPotNo);
	}
	
	public void setTvPotSt(String potst) {
		this.PotSt.setText(potst);
	}
	

	public void setHylCnt(String tvHylCnt) {
		this.HylCnt.setText(tvHylCnt);
	}	

	public void setJLCnt(String tvJLCnt) {
		this.JLCnt.setText(tvJLCnt);
	}

	public void setTvRunTime(String runtime) {
		this.RunTime.setText(runtime);
	}
	
	public void setTvSetV(String setv) {
		this.SetV.setText(setv);
	}
	
	public void setTvRealSetV(String realsetv) {
		this.RealSetV.setText(realsetv);
	}
	
	public void setTvWorkV(String workv) {
		this.WorkV.setText(workv);
	}
	
	public void setTvAverageV(String averagev) {
		this.AverageV.setText(averagev);
	}
	
	public void setTvAeV(String aev) {
		this.AeV.setText(aev);
	}

	public void setTvAeTime(String aetime) {
		this.AeTime.setText(aetime);
	}
	
	public void setTvAeCnt(String aecnt) {
		this.AeCnt.setText(aecnt);
	}
	
	public void setTvDybTime(String dybtime) {
		this.DybTime.setText(dybtime);
	}
	public void setTvDdate(String ddate) {
		this.Ddate.setText(ddate);
	}
	
	public HeaderListView_dayTable(Context context) {
		super(context);
		this.context = context;		
//		PotNo, PotSt,RunTime,HylCnt,JLCnt,SetV,RealSetV,WorkV,AverageV,AeV,AeTime,AeCnt,DybTime,Ddate
		
		View view = LayoutInflater.from(this.context).inflate(R.layout.header_daytable, null);
		// 以下两句的顺序不能调换，要先addView，然后才能通过findViewById找到该TextView
		addView(view);
		PotNo=(TextView) view.findViewById(R.id.tv_PotNo_head);
		PotSt=(TextView) view.findViewById(R.id.tv_PotSt_head);
		RunTime=(TextView) view.findViewById(R.id.tv_RunTime_head);
		HylCnt=(TextView) view.findViewById(R.id.tv_HylCnt_head); //氧化铝下料量
		JLCnt=(TextView) view.findViewById(R.id.tv_JLCnt_head);   //加工次数
		SetV=(TextView) view.findViewById(R.id.tv_SetV_head);
		RealSetV=(TextView) view.findViewById(R.id.tv_RealSetV_head);
		WorkV=(TextView) view.findViewById(R.id.tv_WorkV_head);
		AverageV=(TextView) view.findViewById(R.id.tv_AverageV_head);
		AeV=(TextView) view.findViewById(R.id.tv_AeV_head);
		AeTime=(TextView) view.findViewById(R.id.tv_AeTime_head);
		AeCnt=(TextView) view.findViewById(R.id.tv_AeCnt_head);
		DybTime=(TextView) view.findViewById(R.id.tv_DybTime_head);
		Ddate=(TextView) view.findViewById(R.id.tv_Ddate_head);
		
	}
}
