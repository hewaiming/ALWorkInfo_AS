package com.hewaiming.ALWorkInfo.adapter;

import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.bean.OperateRecord;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OperateRecord_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<OperateRecord> mList;

	public OperateRecord_Adapter(Context mContext, List<OperateRecord> mList) {
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
		OperateRecord entity = mList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_real_record, null);
			holder.ObjectName_tv = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.ParamNameCH_tv = (TextView) convertView.findViewById(R.id.tv_RecordNo);
			holder.Description_tv = (TextView) convertView.findViewById(R.id.tv_param1);
			holder.UserName_tv = (TextView) convertView.findViewById(R.id.tv_param2);
			holder.RecTime_tv = (TextView) convertView.findViewById(R.id.tv_RecTime);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TextPaint tPaint = holder.ObjectName_tv.getPaint();
		tPaint.setFakeBoldText(true);
		holder.ObjectName_tv.setText(entity.getObjectName());
		holder.ParamNameCH_tv.setText(entity.getParamNameCH());
		holder.Description_tv.setText(entity.getDescription());
		holder.UserName_tv.setText(entity.getUserName());
		holder.RecTime_tv.setText(entity.getRecTime());
		return convertView;
	}

	class ViewHolder {
		TextView ObjectName_tv;
		TextView ParamNameCH_tv;
		TextView Description_tv;
		TextView UserName_tv;
		TextView RecTime_tv;

	}

	public void onDateChange(List<OperateRecord> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
