package com.hewaiming.ALWorkInfo.adapter;

import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.bean.RealRecord;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RealRecord_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<RealRecord> mList;

	public RealRecord_Adapter(Context mContext, List<RealRecord> mList) {
		this.inflater = LayoutInflater.from(mContext);
		this.mList = mList;
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RealRecord entity = mList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_real_record, null);
			holder.PotNo_tv = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.RecordNo_tv = (TextView) convertView.findViewById(R.id.tv_RecordNo);
			holder.Param1_tv = (TextView) convertView.findViewById(R.id.tv_param1);
			holder.Param2_tv = (TextView) convertView.findViewById(R.id.tv_param2);
			holder.RecTime_tv = (TextView) convertView.findViewById(R.id.tv_RecTime);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TextPaint tPaint = holder.PotNo_tv.getPaint();
		tPaint.setFakeBoldText(true);
		holder.PotNo_tv.setText(entity.getPotNo() + "");
		holder.RecordNo_tv.setText(entity.getRecordNo());
		holder.Param1_tv.setText(entity.getParam1());
		holder.Param2_tv.setText(entity.getParam2());
		holder.RecTime_tv.setText(entity.getRecTime());
		return convertView;
	}

	class ViewHolder {
		TextView PotNo_tv;
		TextView RecordNo_tv;
		TextView Param1_tv;
		TextView Param2_tv;
		TextView RecTime_tv;

	}

	public void onDateChange(List<RealRecord> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
