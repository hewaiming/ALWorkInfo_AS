package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.SortDayTable.ALCntComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.ALCntComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.AeCntComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.AeCntComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.AeTimeComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.AeTimeComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.AeVComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.AeVComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.AvgVComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.AvgVComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.DYBComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.DYBComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.DateComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.DateComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.FHLCntComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.FHLCntComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.JLCntComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.JLCntComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.PotNoComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.PotNoComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.PotStComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.PotStComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.RealSetVComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.RealSetVComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.RunTimeComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.RunTimeComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.SetVComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.SetVComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.WorkVComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.WorkVComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.YHLCntComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.YHLCntComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_DayTableAdapter;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DayTableActivity extends Activity implements HttpGetListener, OnScrollListener, OnClickListener {
	private Spinner spinner_area, spinner_potno, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private ImageButton isShowingBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private ArrayAdapter<String> PotNo_adapter;

	private HttpPost_BeginDate_EndDate http_post;
	private String potno_url = ":8000/scgy/android/odbcPhP/dayTable_potno_date.php";
	private String area_url = ":8000/scgy/android/odbcPhP/dayTable_area_date.php";

	private String PotNo, BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();
	private List<String> PotNoList = null;
	private List<dayTable> listBean = null;
	private HSView_DayTableAdapter daytable_Adapter = null;
	private RelativeLayout mHead;
	private ListView lv_daytable;
	private LinearLayout showArea = null;
	private View layout_daytable;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private String ip;
	private int port;
	protected Context mContext;
	private TextView PotNo_h, PotSt_h, RunTime_h, YHLCnt_h, JLCnt_h, Setv_h, RealSetv_h, Workv_h, Avgv_h, Aev_h,
			AeTime_h, AeCnt_h, DYB_h, FHL_h, AL_h, Date_h;
	private boolean[] SortFlag = { false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false };
	private ImageView PotNo_iv, PotSt_iv, YHLCnt_iv, JLCnt_iv, RunTime_iv, Setv_iv, RealSetv_iv, Workv_iv, Avgv_iv,
			Aev_iv, AeTime_iv, AeCnt_iv, DYB_iv, FHL_iv, AL_iv, Date_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daytable);
		MyApplication.getInstance().addActivity(this);
		mContext = this;
		GetDataFromIntent();
		init_area();
		init_potNo();
		init_date();
		init_title();
		init_HSView();
		init_sort();
		init_listview();
		if (!MyConst.GetDataFromSharePre(mContext, "DayTable_Show")) {
			MyConst.GuideDialog_show(mContext, "DayTable_Show"); // 第一次显示
		}
	}

	private void init_sort() {
		PotNo_h = (TextView) findViewById(R.id.tv_PotNo_head);
		PotNo_h.setOnClickListener(this);
		PotSt_h = (TextView) findViewById(R.id.tv_PotSt_head);
		PotSt_h.setOnClickListener(this);
		RunTime_h = (TextView) findViewById(R.id.tv_RunTime_head);
		RunTime_h.setOnClickListener(this);
		YHLCnt_h = (TextView) findViewById(R.id.tv_YHLCnt_head);
		YHLCnt_h.setOnClickListener(this); // 加料量
		JLCnt_h = (TextView) findViewById(R.id.tv_JLCnt_head);
		JLCnt_h.setOnClickListener(this); // 加量次数
		Setv_h = (TextView) findViewById(R.id.tv_SetV_head);
		Setv_h.setOnClickListener(this);
		RealSetv_h = (TextView) findViewById(R.id.tv_RealSetV_head);
		RealSetv_h.setOnClickListener(this);
		Workv_h = (TextView) findViewById(R.id.tv_WorkV_head);
		Workv_h.setOnClickListener(this);
		Avgv_h = (TextView) findViewById(R.id.tv_AverageV_head);
		Avgv_h.setOnClickListener(this);
		Aev_h = (TextView) findViewById(R.id.tv_AeV_head);
		Aev_h.setOnClickListener(this);
		AeTime_h = (TextView) findViewById(R.id.tv_AeTime_head);
		AeTime_h.setOnClickListener(this);
		AeCnt_h = (TextView) findViewById(R.id.tv_AeCnt_head);
		AeCnt_h.setOnClickListener(this);
		DYB_h = (TextView) findViewById(R.id.tv_DybTime_head);
		DYB_h.setOnClickListener(this);
		FHL_h = (TextView) findViewById(R.id.tv_ALFCnt_head);
		FHL_h.setOnClickListener(this);
		AL_h = (TextView) findViewById(R.id.tv_ALCntZSL_head);
		AL_h.setOnClickListener(this);
		Date_h = (TextView) findViewById(R.id.tv_Ddate_head);
		Date_h.setOnClickListener(this);

		PotNo_iv = (ImageView) findViewById(R.id.iv_PotNo);
		PotSt_iv = (ImageView) findViewById(R.id.iv_PotSt);
		RunTime_iv = (ImageView) findViewById(R.id.iv_RunTime);
		YHLCnt_iv = (ImageView) findViewById(R.id.iv_YHLCnt);// 加料量 排序
		JLCnt_iv = (ImageView) findViewById(R.id.iv_JLCnt); // 加料次数 排序
		Setv_iv = (ImageView) findViewById(R.id.iv_SetV);
		RealSetv_iv = (ImageView) findViewById(R.id.iv_RealSetV);
		Workv_iv = (ImageView) findViewById(R.id.iv_WorkV);
		Avgv_iv = (ImageView) findViewById(R.id.iv_AverageV);

		Aev_iv = (ImageView) findViewById(R.id.iv_AeV);
		AeTime_iv = (ImageView) findViewById(R.id.iv_AeTime);
		AeCnt_iv = (ImageView) findViewById(R.id.iv_AeCnt);
		DYB_iv = (ImageView) findViewById(R.id.iv_DybTime);
		FHL_iv = (ImageView) findViewById(R.id.iv_ALFCnt);
		AL_iv = (ImageView) findViewById(R.id.iv_ALCntZSL);
		Date_iv = (ImageView) findViewById(R.id.iv_Ddate);
	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_table");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
		potno_url = "http://" + ip + potno_url;
		area_url = "http://" + ip + area_url;
	}

	private void init_listview() {
		lv_daytable = (ListView) findViewById(R.id.lv_daytable);
		lv_daytable.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_daytable.setCacheColorHint(0);
		lv_daytable.setOnScrollListener(this);
		/*
		 * lv_daytable.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { PotNo =
		 * String.valueOf(listBean.get(position).getPotNo()); //
		 * Toast.makeText(getApplicationContext(), PotNo, 1).show(); Intent
		 * potv_intent = new Intent(DayTableActivity.this,
		 * ShowPotVLineActivity.class); Bundle potv_bundle = new Bundle();
		 * potv_bundle.putString("PotNo", PotNo);
		 * potv_bundle.putString("Begin_Date", BeginDate);
		 * potv_bundle.putString("End_Date", EndDate);
		 * potv_bundle.putSerializable("JXList", (Serializable) JXList);
		 * potv_bundle.putString("ip", ip); potv_bundle.putInt("port", port);
		 * potv_intent.putExtras(potv_bundle); startActivity(potv_intent); //
		 * 槽压曲线图
		 * 
		 * } });
		 */
		lv_daytable.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				PotNo = String.valueOf(listBean.get(position).getPotNo());
				// Toast.makeText(getApplicationContext(), PotNo, 1).show();
				Intent potv_intent = new Intent(DayTableActivity.this, PotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putString("PotNo", PotNo);
				potv_bundle.putString("Begin_Date", BeginDate);
				potv_bundle.putString("End_Date", EndDate);
				potv_bundle.putStringArrayList("date_record", (ArrayList<String>) dateBean);
				potv_bundle.putSerializable("JXList", (Serializable) JXList);
				potv_bundle.putString("ip", ip);
				potv_bundle.putInt("port", port);
				potv_intent.putExtras(potv_bundle);
				startActivity(potv_intent); // 槽压曲线图
				return true;
			}
		});

	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head); // 表头处理
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#fffff7"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

	}

	private void init_potNo() {
		spinner_potno = (Spinner) findViewById(R.id.spinner_PotNo);
		PotNoList = new ArrayList<String>();
		for (int i = 1101; i <= 1136; i++) {
			PotNoList.add(i + "");
		}
		PotNoList.add(0, "全部槽号");
		PotNo_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PotNoList);
		PotNo_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_potno.setAdapter(PotNo_adapter);
		spinner_potno.setVisibility(View.VISIBLE);
		spinner_potno.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PotNo = PotNoList.get(position).toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

	}

	private void init_date() {
		spinner_beginDate = (Spinner) findViewById(R.id.spinner_Begindate);
		spinner_endDate = (Spinner) findViewById(R.id.spinner_Enddate);
		Date_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateBean);
		// 设置下拉列表的风格
		Date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_beginDate.setAdapter(Date_adapter);
		spinner_endDate.setAdapter(Date_adapter);
		spinner_beginDate.setVisibility(View.VISIBLE);
		spinner_endDate.setVisibility(View.VISIBLE);
		BeginDate = spinner_beginDate.getItemAtPosition(0).toString();
		EndDate = spinner_endDate.getItemAtPosition(0).toString();

		spinner_beginDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				BeginDate = spinner_beginDate.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// 截止时间
		spinner_endDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				EndDate = spinner_endDate.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void init_title() {
		layout_daytable = findViewById(R.id.Layout_daytable);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("槽日报");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);
		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		isShowingBtn.setOnClickListener(this);

	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);

		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// 设置下拉列表的风格
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_area.setAdapter(Area_adapter);
		spinner_area.setVisibility(View.VISIBLE);
		spinner_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					areaId = 11;
					break;
				case 1:
					areaId = 12;
					break;
				case 2:
					areaId = 13;
					break;
				case 3:
					areaId = 21;
					break;
				case 4:
					areaId = 22;
					break;
				case 5:
					areaId = 23;
					break;
				}

				PotNoChanged(areaId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		findBtn = (Button) findViewById(R.id.btn_ok);
		findBtn.setOnClickListener(this);
	}

	protected void PotNoChanged(int areaId2) {
		switch (areaId2) {
		case 11:
			PotNoList.clear();
			for (int i = 1101; i <= 1136; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 12:
			PotNoList.clear();
			for (int i = 1201; i <= 1237; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 13:
			PotNoList.clear();
			for (int i = 1301; i <= 1337; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 21:
			PotNoList.clear();
			for (int i = 2101; i <= 2136; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 22:
			PotNoList.clear();
			for (int i = 2201; i <= 2237; i++) {
				PotNoList.add(i + "");
			}
			break;
		case 23:
			PotNoList.clear();
			for (int i = 2301; i <= 2337; i++) {
				PotNoList.add(i + "");
			}
			break;
		}
		PotNoList.add(0, "全部槽号");
		spinner_potno.setSelection(0);
		PotNo = PotNoList.get(0).toString();
		PotNo_adapter.notifyDataSetChanged();// 通知数据改变

	}

	@Override
	public void GetDataUrl(String data) {

		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "没有获取到[日报]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // 清除LISTVIEW 以前的内容
					daytable_Adapter.onDateChange(listBean);
				}
			}
		} else {

			listBean = new ArrayList<dayTable>();
			listBean.clear();
			listBean = JsonToBean_Area_Date.JsonArrayToDayTableBean(data);
			daytable_Adapter = new HSView_DayTableAdapter(this, R.layout.item_hsview_daytable, listBean, mHead);

			lv_daytable.setAdapter(daytable_Adapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_isSHOW:
			if (showArea.getVisibility() == View.GONE) {
				showArea.setVisibility(View.VISIBLE);
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.up_green));
			} else {
				showArea.setVisibility(View.GONE);
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.down_green));
			}
			break;
		case R.id.btn_ok:
			if (EndDate.compareTo(BeginDate) < 0) {
				Toast.makeText(getApplicationContext(), "日期选择不对：截止日期小于开始日期", 1).show();
			} else {
				Reset_SortImage();
				if (PotNo == "全部槽号") {
					http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(area_url, 1,
							Integer.toString(areaId), BeginDate, EndDate, this, this).execute();
				} else {

					http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url, 2, PotNo,
							BeginDate, EndDate, this, this).execute();
				}
			}
			layout_daytable.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_PotNo_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[0]) {
					SortFlag[0] = false;
					Collections.sort(listBean, new PotNoComparatorDESC_DayTable());
					PotNo_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[0] = true;
					Collections.sort(listBean, new PotNoComparatorASC_DayTable());
					PotNo_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_PotSt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[1]) {
					SortFlag[1] = false;
					Collections.sort(listBean, new PotStComparatorDESC_DayTable());
					PotSt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[1] = true;
					Collections.sort(listBean, new PotStComparatorASC_DayTable());
					PotSt_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_RunTime_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[2]) {
					SortFlag[2] = false;
					Collections.sort(listBean, new RunTimeComparatorDESC_DayTable());
					RunTime_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[2] = true;
					Collections.sort(listBean, new RunTimeComparatorASC_DayTable());
					RunTime_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		// 加料量排序
		case R.id.tv_YHLCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[3]) {
					SortFlag[3] = false;
					Collections.sort(listBean, new YHLCntComparatorDESC_DayTable());
					YHLCnt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[3] = true;
					Collections.sort(listBean, new YHLCntComparatorASC_DayTable());
					YHLCnt_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		// 加料次数排序
		case R.id.tv_JLCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[4]) {
					SortFlag[4] = false;
					Collections.sort(listBean, new JLCntComparatorDESC_DayTable());
					JLCnt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[4] = true;
					Collections.sort(listBean, new JLCntComparatorASC_DayTable());
					JLCnt_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_SetV_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[5]) {
					SortFlag[5] = false;
					Collections.sort(listBean, new SetVComparatorDESC_DayTable());
					Setv_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[5] = true;
					Collections.sort(listBean, new SetVComparatorASC_DayTable());
					Setv_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_RealSetV_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[6]) {
					SortFlag[6] = false;
					Collections.sort(listBean, new RealSetVComparatorDESC_DayTable());
					RealSetv_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[6] = true;
					Collections.sort(listBean, new RealSetVComparatorASC_DayTable());
					RealSetv_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_WorkV_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[7]) {
					SortFlag[7] = false;
					Collections.sort(listBean, new WorkVComparatorDESC_DayTable());
					Workv_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[7] = true;
					Collections.sort(listBean, new WorkVComparatorASC_DayTable());
					Workv_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_AverageV_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[8]) {
					SortFlag[8] = false;
					Collections.sort(listBean, new AvgVComparatorDESC_DayTable());
					Avgv_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[8] = true;
					Collections.sort(listBean, new AvgVComparatorASC_DayTable());
					Avgv_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_AeV_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[9]) {
					SortFlag[9] = false;
					Collections.sort(listBean, new AeVComparatorDESC_DayTable());
					Aev_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[9] = true;
					Collections.sort(listBean, new AeVComparatorASC_DayTable());
					Aev_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_AeTime_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[10]) {
					SortFlag[10] = false;
					Collections.sort(listBean, new AeTimeComparatorDESC_DayTable());
					AeTime_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[10] = true;
					Collections.sort(listBean, new AeTimeComparatorASC_DayTable());
					AeTime_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_AeCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[11]) {
					SortFlag[11] = false;
					Collections.sort(listBean, new AeCntComparatorDESC_DayTable());
					AeCnt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[11] = true;
					Collections.sort(listBean, new AeCntComparatorASC_DayTable());
					AeCnt_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_DybTime_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[12]) {
					SortFlag[12] = false;
					Collections.sort(listBean, new DYBComparatorDESC_DayTable());
					DYB_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[12] = true;
					Collections.sort(listBean, new DYBComparatorASC_DayTable());
					DYB_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_ALFCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[13]) {
					SortFlag[13] = false;
					Collections.sort(listBean, new FHLCntComparatorDESC_DayTable());
					FHL_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[13] = true;
					Collections.sort(listBean, new FHLCntComparatorASC_DayTable());
					FHL_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_ALCntZSL_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[14]) {
					SortFlag[14] = false;
					Collections.sort(listBean, new ALCntComparatorDESC_DayTable());
					AL_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[14] = true;
					Collections.sort(listBean, new ALCntComparatorASC_DayTable());
					AL_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_Ddate_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[15]) {
					SortFlag[15] = false;
					Collections.sort(listBean, new DateComparatorDESC_DayTable());
					Date_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[15] = true;
					Collections.sort(listBean, new DateComparatorASC_DayTable());
					Date_iv.setBackgroundResource(R.drawable.asc);
				}
				daytable_Adapter.onDateChange(listBean);
				// daytable_Adapter = new HSView_DayTableAdapter(this,
				// R.layout.item_hsview_daytable, listBean, mHead);
				// lv_daytable.setAdapter(daytable_Adapter);
			}
			break;
		}
	}

	private void Reset_SortImage() {
		PotNo_iv.setBackgroundResource(R.drawable.sort);
		PotSt_iv.setBackgroundResource(R.drawable.sort);
		RunTime_iv.setBackgroundResource(R.drawable.sort);
		YHLCnt_iv.setBackgroundResource(R.drawable.sort);
		JLCnt_iv.setBackgroundResource(R.drawable.sort);
		Setv_iv.setBackgroundResource(R.drawable.sort);
		RealSetv_iv.setBackgroundResource(R.drawable.sort);
		Workv_iv.setBackgroundResource(R.drawable.sort);
		Avgv_iv.setBackgroundResource(R.drawable.sort);

		Aev_iv.setBackgroundResource(R.drawable.sort);
		AeTime_iv.setBackgroundResource(R.drawable.sort);
		AeCnt_iv.setBackgroundResource(R.drawable.sort);
		DYB_iv.setBackgroundResource(R.drawable.sort);
		FHL_iv.setBackgroundResource(R.drawable.sort);
		AL_iv.setBackgroundResource(R.drawable.sort);
		Date_iv.setBackgroundResource(R.drawable.sort);
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
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1_head);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

}
