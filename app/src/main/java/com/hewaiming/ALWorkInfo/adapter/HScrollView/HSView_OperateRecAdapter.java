package com.hewaiming.ALWorkInfo.adapter.HScrollView;

import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView.OnScrollChangedListener;
import com.hewaiming.ALWorkInfo.bean.OperateRecord;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HSView_OperateRecAdapter extends BaseAdapter {
	private static final String TAG = "HolderAdapter";

	private List<OperateRecord> mList;
	/**
	 * ListView头部
	 */
	private RelativeLayout mHead;
	/**
	 * layout ID
	 */
	private int id_row_layout;
	private LayoutInflater mInflater;
    //e0f1f2  b3e5fc
	int[] colors = { Color.rgb(224, 241, 242), Color.rgb(179, 213, 252) };
	// int[] colors = { Color.BLACK, Color.BLACK };

	public HSView_OperateRecAdapter(Context context, int id_row_layout, List<OperateRecord> currentData,
			RelativeLayout mHead) {
		Log.v(TAG + ".HolderAdapter", " 初始化");

		this.id_row_layout = id_row_layout;
		this.mInflater = LayoutInflater.from(context);
		this.mList = currentData;
		this.mHead = mHead;

	}

	public int getCount() {
		return this.mList.size();
	}

	public Object getItem(int position) {

		return mList.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	/**
	 * 向List中添加数据
	 * 
	 * @param items
	 */
	public void addItem(List<OperateRecord> items) {
		for (OperateRecord item : items) {
			mList.add(item);
		}
	}

	/**
	 * 清空当List中的数据
	 */
	public void cleanAll() {
		this.mList.clear();
	}

	public View getView(int position, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;
		OperateRecord entity = mList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(id_row_layout, null);
			holder = new ViewHolder();

			MyHScrollView scrollView1 = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView1);

			holder.scrollView = scrollView1;
			holder.tvObjectName = (TextView) convertView.findViewById(R.id.tv_ObjectName);
			holder.tvParamName = (TextView) convertView.findViewById(R.id.tv_ParamName);
			holder.tvDescription = (TextView) convertView.findViewById(R.id.tv_Description);
			holder.tvRecTime = (TextView) convertView.findViewById(R.id.tv_RecTime);
			holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_UserName);

			MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));

			convertView.setTag(holder);
			// 隔行变色
			//convertView.setBackgroundColor(colors[position % 2]);
			// mHolderList.add(holder);
		} else {
			// 隔行变色
			//convertView.setBackgroundColor(colors[position % 2]);
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvObjectName.setText(entity.getObjectName());
		holder.tvParamName.setText(entity.getParamNameCH());
		holder.tvDescription.setText(entity.getDescription());
		holder.tvRecTime.setText(entity.getRecTime());
		holder.tvUserName.setText(entity.getUserName());

		return convertView;
	}

	class OnScrollChangedListenerImp implements OnScrollChangedListener {
		MyHScrollView mScrollViewArg;

		public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			mScrollViewArg.smoothScrollTo(l, t);
		}
	};

	class ViewHolder {
		TextView tvObjectName;
		TextView tvParamName;
		TextView tvDescription;
		TextView tvRecTime;
		TextView tvUserName;
		HorizontalScrollView scrollView;
	}

	public void onDateChange(List<OperateRecord> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
