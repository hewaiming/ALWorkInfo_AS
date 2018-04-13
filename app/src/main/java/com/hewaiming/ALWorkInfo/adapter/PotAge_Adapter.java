package com.hewaiming.ALWorkInfo.adapter;

import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.bean.PotAge;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PotAge_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<PotAge> mList;

	public PotAge_Adapter(Context mContext, List<PotAge> mList) {
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
		PotAge entity = mList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_potage, null);
			holder.PotNo_tv = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.BeginTime_tv = (TextView) convertView.findViewById(R.id.tv_BeginTime);
			holder.EndTime_tv = (TextView) convertView.findViewById(R.id.tv_EndTime);
			holder.Age_tv = (TextView) convertView.findViewById(R.id.tv_Age);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TextPaint tPaint = holder.PotNo_tv.getPaint();
		tPaint.setFakeBoldText(true);
		holder.PotNo_tv.setText(entity.getPotNo() + "");
		holder.BeginTime_tv.setText(entity.getBeginTime());
		holder.EndTime_tv.setText(entity.getEndTime());
		holder.Age_tv.setText(entity.getAge() + "");
		return convertView;
	}

	class ViewHolder {
		TextView PotNo_tv;
		TextView BeginTime_tv;
		TextView EndTime_tv;
		TextView Age_tv;
	}

	public void onDateChange(List<PotAge> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
