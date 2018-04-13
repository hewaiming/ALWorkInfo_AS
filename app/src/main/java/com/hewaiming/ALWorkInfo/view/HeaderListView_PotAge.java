package com.hewaiming.ALWorkInfo.view;


import com.hewaiming.ALWorkInfo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;

public class HeaderListView_PotAge extends LinearLayout {
	private Context context;
	private TextView tvPotNo, tvBeginTime,tvEndTime,tvAge;

	public void setTvPotNo(String tvPotNo) {
		this.tvPotNo.setText(tvPotNo);
	}
	public void setTvBeginTime(String BeginTime) {
		this.tvBeginTime.setText(BeginTime);
	}
	public void setTvEndTime(String EndTime) {
		this.tvEndTime.setText(EndTime);
	}
	public void setTvAge(String Age) {
		this.tvAge.setText(Age);
	}	

	public HeaderListView_PotAge(Context context) {
		super(context);
		this.context = context;
		View view = LayoutInflater.from(this.context).inflate(R.layout.header_potage, null);
		// 以下两句的顺序不能调换，要先addView，然后才能通过findViewById找到该TextView
		addView(view);
		tvPotNo=(TextView) view.findViewById(R.id.tv_PotNo_head);
		tvBeginTime=(TextView) view.findViewById(R.id.tv_BeginTime_head);
		tvEndTime=(TextView) view.findViewById(R.id.tv_EndTime_head);
		tvAge=(TextView) view.findViewById(R.id.tv_Age_head);	
		
	}
}
