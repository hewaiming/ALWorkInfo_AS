package com.hewaiming.ALWorkInfo.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AeRecAdapter;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.ui.Ae5DayActivity;
import com.hewaiming.ALWorkInfo.ui.PotVLineActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Fragment_Ae2 extends Fragment implements OnScrollListener {
	private View mView;
	private RelativeLayout mHeadAe;
	private ListView lvAe;
	private List<AeRecord> listBeanAe = null;
	private HSView_AeRecAdapter aeAdapter = null;
	private Ae5DayActivity mActivity;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private List<String> dateBean = new ArrayList<String>();
	private String ip;
	private int port;
	
	public Fragment_Ae2(List<Map<String, Object>> jXList,List<String> mDateBean,String mip,int mport) {
		this.JXList=jXList;
		this.ip=mip;
		this.port=mport;
		this.dateBean=mDateBean;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_ae1, container, false);
		return mView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (Ae5DayActivity) activity;
		mActivity.setHandler_Ae2(mHandler_Ae);
	}

	public Handler mHandler_Ae = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				init_adapter(msg.obj);
				break;
			}
		}
	};

	private void init_adapter(Object data) {
		if (data.equals("")) {
			Toast.makeText(this.getActivity(), "没有获取到[效应情报表]前一次AE，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			if (listBeanAe != null) {
				if (listBeanAe.size() > 0) {
					listBeanAe.clear(); // 清除LISTVIEW 以前的内容
					aeAdapter.onDateChange(listBeanAe);
				}
			}
		} else {
			listBeanAe = new ArrayList<AeRecord>(); // 初始化效应记录 适配器
			listBeanAe.clear();
			listBeanAe = (List<AeRecord>) (data);
			aeAdapter = new HSView_AeRecAdapter(this.getActivity(), R.layout.item_hsview_ae_rec, listBeanAe,
					mHeadAe);
			lvAe.setAdapter(aeAdapter);
		}

	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		mHeadAe = (RelativeLayout) mView.findViewById(R.id.head_Ae1); // 表头处理
		mHeadAe.setFocusable(true);
		mHeadAe.setClickable(true);
		mHeadAe.setBackgroundColor(Color.parseColor("#fffffb"));
		mHeadAe.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lvAe = (ListView) mView.findViewById(R.id.lv_Ae1);
		lvAe.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lvAe.setCacheColorHint(0);
		lvAe.setOnScrollListener(this);
		lvAe.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent potv_intent = new Intent( getActivity(), PotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putString("PotNo", String.valueOf(listBeanAe.get(position).getPotNo()));
				potv_bundle.putString("Begin_Date", listBeanAe.get(position).getDdate().substring(0, 10));
				potv_bundle.putString("End_Date", listBeanAe.get(position).getDdate().substring(0, 10));
				potv_bundle.putSerializable("JXList", (Serializable) JXList);
				potv_bundle.putStringArrayList("date_record", (ArrayList<String>) dateBean);
				potv_bundle.putString("ip", ip);
				potv_bundle.putInt("port", port);
				potv_intent.putExtras(potv_bundle);
				startActivity(potv_intent); // 槽压曲线图
				
			}
		});

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		public boolean onTouch(View arg0, MotionEvent arg1) {
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView_Ae = (HorizontalScrollView) mHeadAe
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView_Ae.onTouchEvent(arg1);
			return false;
		}
	}
}
