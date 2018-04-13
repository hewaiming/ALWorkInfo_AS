package com.hewaiming.ALWorkInfo.view;


import com.hewaiming.ALWorkInfo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;

public class HeaderListView_AlarmRecord extends LinearLayout {
	private Context context;
	private TextView tvPotNo, tvRecordNo,tvRecTime;

	public void setTvPotNo(String tvPotNo) {
		this.tvPotNo.setText(tvPotNo);
	}
	public void setTvRecordNo(String recordno) {
		this.tvRecordNo.setText(recordno);
	}
	public void setTvRecTime(String recTime) {
		this.tvRecTime.setText(recTime);
	}	

	public HeaderListView_AlarmRecord(Context context) {
		super(context);
		this.context = context;
		View view = LayoutInflater.from(this.context).inflate(R.layout.header_alarm_record, null);
		// 以下两句的顺序不能调换，要先addView，然后才能通过findViewById找到该TextView
		addView(view);
		tvPotNo=(TextView) view.findViewById(R.id.tv_PotNo_head);
		tvRecordNo=(TextView) view.findViewById(R.id.tv_RecordNo_head);
		tvRecTime=(TextView) view.findViewById(R.id.tv_RecTime_head);		
		
	}
}
