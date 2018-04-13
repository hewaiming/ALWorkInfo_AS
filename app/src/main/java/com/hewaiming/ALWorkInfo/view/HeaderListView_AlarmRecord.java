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
		// ���������˳���ܵ�����Ҫ��addView��Ȼ�����ͨ��findViewById�ҵ���TextView
		addView(view);
		tvPotNo=(TextView) view.findViewById(R.id.tv_PotNo_head);
		tvRecordNo=(TextView) view.findViewById(R.id.tv_RecordNo_head);
		tvRecTime=(TextView) view.findViewById(R.id.tv_RecTime_head);		
		
	}
}
