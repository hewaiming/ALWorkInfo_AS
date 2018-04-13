package com.hewaiming.ALWorkInfo.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AeTimeAdapter;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.ui.AeMostActivity;
import com.hewaiming.ALWorkInfo.ui.DayTableActivity;
import com.hewaiming.ALWorkInfo.ui.PotVLineActivity;

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

public class Fragment_AeTime extends Fragment implements OnScrollListener {
	private View mView;
	private RelativeLayout mHeadAeTime;
	private ListView lvAeTime;
	private List<AeRecord> listBeanAeTime = null;
	private HSView_AeTimeAdapter AeTimeAdapter = null;
	private AeMostActivity mActivity;
	private String PotNo;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private List<String> dateBean = new ArrayList<String>();
	private String ip;
	private int port;

	public Fragment_AeTime(List<Map<String, Object>> jXList2,List<String> mDateBean,String mip,int mport) {
		this.JXList=jXList2;
		this.dateBean=mDateBean;
		this.ip=mip;
		this.port=mport;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_aetime, container, false);
		return mView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (AeMostActivity) activity;
		mActivity.setHandler_AeTime(mHandler_AeTime);		
		
	}

	public Handler mHandler_AeTime = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				init_adapter(msg.obj.toString());
				break;
			}
		}
	};

	private void init_adapter(String data) {
		if (data.equals("")) {
			Toast.makeText(this.getActivity(), "没有获取到[效应槽：持续时间最长]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			if (listBeanAeTime != null) {
				if (listBeanAeTime.size() > 0) {
					listBeanAeTime.clear(); // 清除LISTVIEW 以前的内容
					AeTimeAdapter.onDateChange(listBeanAeTime);
				}
			}
		} else {
			listBeanAeTime = new ArrayList<AeRecord>(); // 初始化效应时间长 适配器
			listBeanAeTime.clear();
			listBeanAeTime = JsonToBean_Area_Date.JsonArrayToAeTimeBean(data);
			AeTimeAdapter = new HSView_AeTimeAdapter(this.getActivity(), R.layout.item_hsview_aetime, listBeanAeTime,
					mHeadAeTime);
			lvAeTime.setAdapter(AeTimeAdapter);
		}

	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		mHeadAeTime = (RelativeLayout) mView.findViewById(R.id.head_AeTime); // 表头处理
		mHeadAeTime.setFocusable(true);
		mHeadAeTime.setClickable(true);
		mHeadAeTime.setBackgroundColor(Color.parseColor("#fffffb"));
		mHeadAeTime.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lvAeTime = (ListView) mView.findViewById(R.id.lv_AeTime);
		lvAeTime.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lvAeTime.setCacheColorHint(0);
		lvAeTime.setOnScrollListener(this);
		lvAeTime.setOnItemClickListener(new OnItemClickListener() {		
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PotNo = String.valueOf(listBeanAeTime.get(position).getPotNo());
//				Toast.makeText(getApplicationContext(), PotNo, 1).show();
				Intent potv_intent = new Intent( getActivity(), PotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putString("PotNo", PotNo);
				potv_bundle.putString("Begin_Date", listBeanAeTime.get(position).getDdate().substring(0, 10));
				potv_bundle.putString("End_Date", listBeanAeTime.get(position).getDdate().substring(0, 10));
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
			HorizontalScrollView headSrcrollView_AeTime = (HorizontalScrollView) mHeadAeTime
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView_AeTime.onTouchEvent(arg1);
			return false;
		}
	}
	
}
