package com.hewaiming.ALWorkInfo.adapter.HScrollView;

import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView.OnScrollChangedListener;
import com.hewaiming.ALWorkInfo.bean.AeRecord;

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

public class HSView_AeRecAdapter extends BaseAdapter {
	private static final String TAG = "HolderAdapter";

	private List<AeRecord> mList;
	/**
	 * ListView???
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

	public HSView_AeRecAdapter(Context context, int id_row_layout, List<AeRecord> currentData,
			RelativeLayout mHead) {
		//Log.v(TAG + ".HolderAdapter", " ?????");

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
	 * ??List?????????
	 * 
	 * @param items
	 */
	public void addItem(List<AeRecord> items) {
		for (AeRecord item : items) {
			mList.add(item);
		}
	}

	/**
	 * ????List?§Ö?????
	 */
	public void cleanAll() {
		this.mList.clear();
	}

	public View getView(int position, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;
		AeRecord entity = mList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(id_row_layout, null);
			holder = new ViewHolder();

			MyHScrollView scrollView1 = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView1);

			holder.scrollView = scrollView1;
			holder.tvPotNo = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.tvDdate = (TextView) convertView.findViewById(R.id.tv_DDate);
			holder.tvAvgV = (TextView) convertView.findViewById(R.id.tv_AvgV);
			holder.tvContinueTime = (TextView) convertView.findViewById(R.id.tv_ContinueTime);
			holder.tvWaitTime = (TextView) convertView.findViewById(R.id.tv_WaitTime);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_Status);
			holder.tvMaxV = (TextView) convertView.findViewById(R.id.tv_MaxV);

			MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));

			convertView.setTag(holder);
			// ???§Ò??
			//convertView.setBackgroundColor(colors[position % 2]);
			// mHolderList.add(holder);
		} else {
			// ???§Ò??
			//convertView.setBackgroundColor(colors[position % 2]);
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvPotNo.setText(entity.getPotNo()+"");
		holder.tvDdate.setText(entity.getDdate());
		holder.tvAvgV.setText(entity.getAverageV()+"");
		holder.tvContinueTime.setText(entity.getContinueTime()+"");
		holder.tvWaitTime.setText(entity.getWaitTime()+"");
		holder.tvStatus.setText(entity.getStatus());
		holder.tvMaxV.setText(entity.getMaxV()+"");

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
		TextView tvPotNo;
		TextView tvDdate;
		TextView tvAvgV;
		TextView tvContinueTime;
		TextView tvWaitTime;
		TextView tvStatus;
		TextView tvMaxV;
		HorizontalScrollView scrollView;
	}

	public void onDateChange(List<AeRecord> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
