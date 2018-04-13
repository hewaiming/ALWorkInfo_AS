package com.hewaiming.ALWorkInfo.adapter.HScrollView;

import java.util.List;

import org.w3c.dom.Text;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView.OnScrollChangedListener;
import com.hewaiming.ALWorkInfo.bean.dayTable;

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

public class HSView_DayTableAdapter extends BaseAdapter {
	private static final String TAG = "HolderAdapter";

	private List<dayTable> mList;
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

	public HSView_DayTableAdapter(Context context, int id_row_layout, List<dayTable> currentData,
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
	public void addItem(List<dayTable> items) {
		for (dayTable item : items) {
			mList.add(item);
		}
	}

	/**
	 * ????List?��?????
	 */
	public void cleanAll() {
		this.mList.clear();
	}

	public View getView(int position, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;
		dayTable entity = mList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(id_row_layout, null);
			holder = new ViewHolder();

			MyHScrollView scrollView1 = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView1);

			holder.scrollView = scrollView1;
			holder.tvPotNo = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.tvPotSt = (TextView) convertView.findViewById(R.id.tv_PotSt);			
			holder.tvRunTime = (TextView) convertView.findViewById(R.id.tv_RunTime);
			holder.tvYHLCnt=(TextView) convertView.findViewById(R.id.tv_YHLCnt);
			holder.tvJLCnt=(TextView) convertView.findViewById(R.id.tv_JLCnt);
			holder.tvSetV = (TextView) convertView.findViewById(R.id.tv_SetV);
			holder.tvRealSetV = (TextView) convertView.findViewById(R.id.tv_RealSetV);
			holder.tvWorkV = (TextView) convertView.findViewById(R.id.tv_WorkV);
			holder.tvAvgV = (TextView) convertView.findViewById(R.id.tv_AverageV);
			holder.tvAeV = (TextView) convertView.findViewById(R.id.tv_AeV);
			holder.tvAeTime = (TextView) convertView.findViewById(R.id.tv_AeTime);
			holder.tvAeCnt = (TextView) convertView.findViewById(R.id.tv_AeCnt);
			holder.tvDybTime = (TextView) convertView.findViewById(R.id.tv_DybTime);
			holder.tvALFCnt = (TextView) convertView.findViewById(R.id.tv_ALFCnt);
			holder.tvALCntZSL = (TextView) convertView.findViewById(R.id.tv_ALCntZSL);
			holder.tvDdate = (TextView) convertView.findViewById(R.id.tv_Ddate);
			
			

			MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView1_head);
			headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));

			convertView.setTag(holder);
			// ???��??
			//convertView.setBackgroundColor(colors[position % 2]);
			// mHolderList.add(holder);
		} else {
			// ???��??
			//convertView.setBackgroundColor(colors[position % 2]);
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvPotNo.setText(entity.getPotNo()+"");
		holder.tvPotSt.setText(entity.getPotSt());
		holder.tvRunTime.setText(entity.getRunTime()+"");
		holder.tvYHLCnt.setText(entity.getYhlCnt()+"");
		holder.tvJLCnt.setText(entity.getJLCnt()+"");
		holder.tvSetV.setText(entity.getSetV()+"");
		holder.tvRealSetV.setText(entity.getRealSetV()+"");
		holder.tvWorkV.setText(entity.getWorkV()+"");
		holder.tvAvgV.setText(entity.getAverageV()+"");
		holder.tvAeV.setText(entity.getAeV()+"");
		holder.tvAeTime.setText(entity.getAeTime()+"");
		holder.tvAeCnt.setText(entity.getAeCnt()+"");
		holder.tvDybTime.setText(entity.getDybTime()+"");
		holder.tvALFCnt.setText(entity.getFhlCnt()+"");
		holder.tvALCntZSL.setText(entity.getAlCntZSL()+"");
		holder.tvDdate.setText(entity.getDdate());		

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
		TextView tvPotSt;
		TextView tvRunTime;
		TextView tvYHLCnt; //??????
		TextView tvJLCnt; //???????
		TextView tvSetV;
		TextView tvRealSetV;
		TextView tvWorkV;
		TextView tvAvgV;
		TextView tvAeV;
		TextView tvAeTime;
		TextView tvAeCnt;
		TextView tvDybTime;
		TextView tvALFCnt;  //????????????
		TextView tvALCntZSL; //????????
		TextView tvDdate;
		HorizontalScrollView scrollView;
	}

	public void onDateChange(List<dayTable> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
