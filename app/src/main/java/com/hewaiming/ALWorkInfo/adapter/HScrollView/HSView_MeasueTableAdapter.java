package com.hewaiming.ALWorkInfo.adapter.HScrollView;

import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView.OnScrollChangedListener;
import com.hewaiming.ALWorkInfo.bean.MeasueTable;

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

public class HSView_MeasueTableAdapter extends BaseAdapter {
	private static final String TAG = "HolderAdapter";

	private List<MeasueTable> mList;
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

	public HSView_MeasueTableAdapter(Context context, int id_row_layout, List<MeasueTable> currentData,
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
	public void addItem(List<MeasueTable> items) {
		for (MeasueTable item : items) {
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
		MeasueTable entity = mList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(id_row_layout, null);
			holder = new ViewHolder();

			MyHScrollView scrollView1 = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView1);

			holder.scrollView = scrollView1;
			holder.tvPotNo = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.tvDdate = (TextView) convertView.findViewById(R.id.tv_Ddate);
			holder.tvALCnt = (TextView) convertView.findViewById(R.id.tv_ALCnt);
			holder.tvLSP = (TextView) convertView.findViewById(R.id.tv_LSP);
			holder.tvDJZSP = (TextView) convertView.findViewById(R.id.tv_DJZSP);
			holder.tvDJWD = (TextView) convertView.findViewById(R.id.tv_DJWD);
			holder.tvFZB = (TextView) convertView.findViewById(R.id.tv_FZB);
			holder.tvFeCnt = (TextView) convertView.findViewById(R.id.tv_FeCnt);
			holder.tvSiCnt = (TextView) convertView.findViewById(R.id.tv_SiCnt);
			holder.tvALOCnt = (TextView) convertView.findViewById(R.id.tv_ALOCnt);
			holder.tvCaFCnt = (TextView) convertView.findViewById(R.id.tv_CaFCnt);
			holder.tvMgCnt = (TextView) convertView.findViewById(R.id.tv_MgCnt);
			holder.tvMLSP = (TextView) convertView.findViewById(R.id.tv_MLSP);
			holder.tvLDYJ = (TextView) convertView.findViewById(R.id.tv_LDYJ);
			holder.tvJHCL = (TextView) convertView.findViewById(R.id.tv_JHCL);			

			MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView1_head);
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
		holder.tvPotNo.setText(entity.getPotNo());
		holder.tvDdate.setText(entity.getDdate());
		holder.tvALCnt.setText(entity.getALCnt());
		holder.tvLSP.setText(entity.getLSP());
		holder.tvDJZSP.setText(entity.getDJZSP());
		holder.tvDJWD.setText(entity.getDJWD());
		holder.tvFZB.setText(entity.getFZB());
		holder.tvFeCnt.setText(entity.getFeCnt());
		holder.tvSiCnt.setText(entity.getSiCnt());
		holder.tvALOCnt.setText(entity.getALOCnt());
		holder.tvCaFCnt.setText(entity.getCaFCnt());
		holder.tvMgCnt.setText(entity.getMgCnt());
		holder.tvMLSP.setText(entity.getMLSP());
		holder.tvLDYJ.setText(entity.getLDYJ());	
		holder.tvJHCL.setText(entity.getJHCL());	

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

//	PotNo, Ddate,ALCnt, LSP, DJZSP;DJWD;FZB;FeCnt;SiCnt;ALOCnt;CaFCnt;MgCnt;MLSP;LDYJ;JHCL;
	class ViewHolder {
		TextView tvPotNo;
		TextView tvDdate;
		TextView tvALCnt;
		TextView tvLSP;
		TextView tvDJZSP;
		TextView tvDJWD;
		TextView tvFZB;
		TextView tvFeCnt;
		TextView tvSiCnt;
		TextView tvALOCnt;
		TextView tvCaFCnt;
		TextView tvMgCnt;
		TextView tvMLSP;
		TextView tvLDYJ;
		TextView tvJHCL;
		HorizontalScrollView scrollView;
	}

	public void onDateChange(List<MeasueTable> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
