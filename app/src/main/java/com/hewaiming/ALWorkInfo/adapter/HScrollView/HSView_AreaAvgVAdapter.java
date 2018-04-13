package com.hewaiming.ALWorkInfo.adapter.HScrollView;

import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView.OnScrollChangedListener;
import com.hewaiming.ALWorkInfo.bean.AvgV_Area;
import com.hewaiming.ALWorkInfo.bean.FaultMost;

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

public class HSView_AreaAvgVAdapter extends BaseAdapter {
	private static final String TAG = "HolderAdapter";

	private List<AvgV_Area> mList;
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

	public HSView_AreaAvgVAdapter(Context context, int id_row_layout, List<AvgV_Area> currentData,
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
	public void addItem(List<AvgV_Area> items) {
		for (AvgV_Area item : items) {
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
		AvgV_Area entity = mList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(id_row_layout, null);
			holder = new ViewHolder();

			MyHScrollView scrollView1 = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView_AreaAvgV);

			holder.scrollView = scrollView1;
			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_Date);
			holder.tvV = (TextView) convertView.findViewById(R.id.tv_AreaAvgV);			

			MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView_AreaAvgV);
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
		holder.tvDate.setText(entity.getDdate()+"");
		holder.tvV.setText(entity.getAverageV()+"");	

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
		TextView tvDate;
		TextView tvV;		
		HorizontalScrollView scrollView;
	}

	public void onDateChange(List<AvgV_Area> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
