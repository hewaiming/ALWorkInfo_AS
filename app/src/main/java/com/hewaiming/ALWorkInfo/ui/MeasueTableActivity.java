package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.SortDayTable.PotNoComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.PotNoComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortMeasue.ALComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.ALComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.CBWDComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.CBWDComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.DJWDComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.DJWDComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.DJZSPComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.DJZSPComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.DateComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.DateComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.FHGComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.FHGComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.FZBComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.FZBComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.FeComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.FeComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.JHALComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.JHALComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.LSPComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.LSPComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.MgComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.MgComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.PotNoComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.PotNoComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.SiComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.SiComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.YHLComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.YHLComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.YJComparatorASC_Measue;
import com.hewaiming.ALWorkInfo.SortMeasue.YJComparatorDESC_Measue;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_MeasueTableAdapter;
import com.hewaiming.ALWorkInfo.bean.MeasueTable;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
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

public class MeasueTableActivity extends Activity implements HttpGetListener, OnScrollListener, OnClickListener {
	private Spinner spinner_area, spinner_potno, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private ArrayAdapter<String> PotNo_adapter;

	private HttpPost_BeginDate_EndDate http_post;
	private String potno_url = ":8000/scgy/android/odbcPhP/MeasueTable_potno_date.php";
	private String area_url = ":8000/scgy/android/odbcPhP/MeasueTable_area_date.php";

	private String PotNo, BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();
	private List<String> PotNoList = null;
	private List<MeasueTable> listBean = null;
	private HSView_MeasueTableAdapter measue_Adapter = null;
	private RelativeLayout mHead;
	private ListView lv_MeasueTable;
	private ImageButton isShowingBtn;
	private LinearLayout showArea = null;
	private View layout_list;
	private String ip;
	private int port;
	private TextView PotNo_h, Ddate_h, ALCnt_h, LSP_h, DJZSP_h, DJWD_h, FZB_h, FeCnt_h, SiCnt_h, ALOCnt_h, CaFCnt_h,
			MgCnt_h, MLSP_h, LDYJ_h, JHCL_h;
	private boolean[] SortFlag = { false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false };
	private ImageView PotNo_iv, Ddate_iv, ALCnt_iv, LSP_iv, DJZSP_iv, DJWD_iv, FZB_iv, FeCnt_iv, SiCnt_iv, ALOCnt_iv,
			CaFCnt_iv, MgCnt_iv, MLSP_iv, LDYJ_iv, JHCL_iv;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measue_table);
		MyApplication.getInstance().addActivity(this);
		mContext=this;
		GetDataFromIntent();
		init_area();
		init_potNo();
		init_date();
		init_title();
		init_HSView();
		init_sort();
		if (!MyConst.GetDataFromSharePre(mContext, "MeasueTable_Show")) {
			MyConst.GuideDialog_show(mContext, "MeasueTable_Show"); // 第一次显示
		}
	}

	private void init_sort() {
		PotNo_h = (TextView) findViewById(R.id.tv_PotNo_head);
		PotNo_h.setOnClickListener(this);
		Ddate_h = (TextView) findViewById(R.id.tv_Ddate_head);
		Ddate_h.setOnClickListener(this);
		ALCnt_h = (TextView) findViewById(R.id.tv_ALCnt_head);
		ALCnt_h.setOnClickListener(this);
		LSP_h = (TextView) findViewById(R.id.tv_LSP_head);
		LSP_h.setOnClickListener(this);
		DJZSP_h = (TextView) findViewById(R.id.tv_DJZSP_head);
		DJZSP_h.setOnClickListener(this);
		DJWD_h = (TextView) findViewById(R.id.tv_DJWD_head);
		DJWD_h.setOnClickListener(this);
		FZB_h = (TextView) findViewById(R.id.tv_FZB_head);
		FZB_h.setOnClickListener(this);
		FeCnt_h = (TextView) findViewById(R.id.tv_FeCnt_head);
		FeCnt_h.setOnClickListener(this);
		SiCnt_h = (TextView) findViewById(R.id.tv_SiCnt_head);
		SiCnt_h.setOnClickListener(this);
		ALOCnt_h = (TextView) findViewById(R.id.tv_ALOCnt_head);
		ALOCnt_h.setOnClickListener(this);
		CaFCnt_h = (TextView) findViewById(R.id.tv_CaFCnt_head);
		CaFCnt_h.setOnClickListener(this);
		MgCnt_h = (TextView) findViewById(R.id.tv_MgCnt_head);
		MgCnt_h.setOnClickListener(this);
		MLSP_h = (TextView) findViewById(R.id.tv_MLSP_head);
		MLSP_h.setOnClickListener(this);
		LDYJ_h = (TextView) findViewById(R.id.tv_LDYJ_head);
		LDYJ_h.setOnClickListener(this);
		JHCL_h = (TextView) findViewById(R.id.tv_JHCL_head);
		JHCL_h.setOnClickListener(this);

		PotNo_iv = (ImageView) findViewById(R.id.iv_PotNo);
		Ddate_iv = (ImageView) findViewById(R.id.iv_Ddate);
		ALCnt_iv = (ImageView) findViewById(R.id.iv_ALCnt);
		LSP_iv = (ImageView) findViewById(R.id.iv_LSP);
		DJZSP_iv = (ImageView) findViewById(R.id.iv_DJZSP);
		DJWD_iv = (ImageView) findViewById(R.id.iv_DJWD);
		FZB_iv = (ImageView) findViewById(R.id.iv_FZB);

		FeCnt_iv = (ImageView) findViewById(R.id.iv_FeCnt);
		SiCnt_iv = (ImageView) findViewById(R.id.iv_SiCnt);
		ALOCnt_iv = (ImageView) findViewById(R.id.iv_ALOCnt);
		CaFCnt_iv = (ImageView) findViewById(R.id.iv_CaFCnt);
		MgCnt_iv = (ImageView) findViewById(R.id.iv_MgCnt);
		MLSP_iv = (ImageView) findViewById(R.id.iv_MLSP);
		LDYJ_iv = (ImageView) findViewById(R.id.iv_LDYJ);
		JHCL_iv = (ImageView) findViewById(R.id.iv_JHCL);

	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_record");
		// JXList = (List<Map<String, Object>>)
		// getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
		potno_url = "http://" + ip + potno_url;
		area_url = "http://" + ip + area_url;
	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head); // 表头处理
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#fffff7"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_MeasueTable = (ListView) findViewById(R.id.lv_MeasueTable);
		lv_MeasueTable.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_MeasueTable.setCacheColorHint(0);
		lv_MeasueTable.setOnScrollListener(this);
		lv_MeasueTable.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
				PotNo= String.valueOf(listBean.get(position).getPotNo());
				Intent craft_intent = new Intent(MeasueTableActivity.this, CraftLineActivity.class);
				Bundle craftBundle = new Bundle();
				craftBundle.putStringArrayList("date_table", (ArrayList<String>)dateBean );
				craftBundle.putString("PotNo_Selected", PotNo);
				craftBundle.putString("ip", ip);
				craftBundle.putInt("port", port);
				craft_intent.putExtras(craftBundle);
				startActivity(craft_intent); // 工艺曲线
				return true;
			}
		});
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
		layout_list = findViewById(R.id.Layout_Measue);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("测量数据");
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
			Toast.makeText(getApplicationContext(), "没有获取到[测量数据]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // 清除LISTVIEW 以前的内容
					measue_Adapter.onDateChange(listBean);
				}
			}
		} else {

			listBean = new ArrayList<MeasueTable>();
			listBean.clear();
			listBean = JsonToBean_Area_Date.JsonArrayToMeasueTableBean(data);
			measue_Adapter = new HSView_MeasueTableAdapter(this, R.layout.item_hsview_measuetable, listBean, mHead);

			lv_MeasueTable.setAdapter(measue_Adapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_isSHOW: // 显示或隐藏
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
				if (PotNo == "全部槽号") {
					http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(area_url, 1,
							Integer.toString(areaId), BeginDate, EndDate, this, this).execute();
				} else {

					http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url, 2, PotNo,
							BeginDate, EndDate, this, this).execute();
				}
			}
			layout_list.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_PotNo_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[0]) {
					SortFlag[0] = false;					
					Collections.sort(listBean,new PotNoComparatorDESC_Measue());
					PotNo_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[0] = true;
					Collections.sort(listBean, new PotNoComparatorASC_Measue());
					PotNo_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_Ddate_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[1]) {
					SortFlag[1] = false;					
					Collections.sort(listBean,new DateComparatorDESC_Measue());
					Ddate_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[1] = true;
					Collections.sort(listBean, new DateComparatorASC_Measue());
					Ddate_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_ALCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[2]) {
					SortFlag[2] = false;					
					Collections.sort(listBean,new ALComparatorDESC_Measue());
					ALCnt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[2] = true;
					Collections.sort(listBean, new ALComparatorASC_Measue());
					ALCnt_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_LSP_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[3]) {
					SortFlag[3] = false;					
					Collections.sort(listBean,new LSPComparatorDESC_Measue());
					LSP_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[3] = true;
					Collections.sort(listBean, new LSPComparatorASC_Measue());
					LSP_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_DJZSP_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[4]) {
					SortFlag[4] = false;					
					Collections.sort(listBean,new DJZSPComparatorDESC_Measue());
					DJZSP_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[4] = true;
					Collections.sort(listBean, new DJZSPComparatorASC_Measue());
					DJZSP_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_DJWD_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[5]) {
					SortFlag[5] = false;					
					Collections.sort(listBean,new DJWDComparatorDESC_Measue());
					DJWD_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[5] = true;
					Collections.sort(listBean, new DJWDComparatorASC_Measue());
					DJWD_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_FZB_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[6]) {
					SortFlag[6] = false;					
					Collections.sort(listBean,new FZBComparatorDESC_Measue());
					FZB_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[6] = true;
					Collections.sort(listBean, new FZBComparatorASC_Measue());
					FZB_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_FeCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[7]) {
					SortFlag[7] = false;					
					Collections.sort(listBean,new FeComparatorDESC_Measue());
					FeCnt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[7] = true;
					Collections.sort(listBean, new FeComparatorASC_Measue());
					FeCnt_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_SiCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[8]) {
					SortFlag[8] = false;					
					Collections.sort(listBean,new SiComparatorDESC_Measue());
					SiCnt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[8] = true;
					Collections.sort(listBean, new SiComparatorASC_Measue());
					SiCnt_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_ALOCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[9]) {
					SortFlag[9] = false;					
					Collections.sort(listBean,new YHLComparatorDESC_Measue());
					ALOCnt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[9] = true;
					Collections.sort(listBean, new YHLComparatorASC_Measue());
					ALOCnt_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_CaFCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[10]) {
					SortFlag[10] = false;					
					Collections.sort(listBean,new FHGComparatorDESC_Measue());
					CaFCnt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[10] = true;
					Collections.sort(listBean, new FHGComparatorASC_Measue());
					CaFCnt_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_MgCnt_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[11]) {
					SortFlag[11] = false;					
					Collections.sort(listBean,new MgComparatorDESC_Measue());
					MgCnt_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[11] = true;
					Collections.sort(listBean, new MgComparatorASC_Measue());
					MgCnt_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_MLSP_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[12]) {
					SortFlag[12] = false;					
					Collections.sort(listBean,new CBWDComparatorDESC_Measue());
					MLSP_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[12] = true;
					Collections.sort(listBean, new CBWDComparatorASC_Measue());
					MLSP_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_LDYJ_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[13]) {
					SortFlag[13] = false;					
					Collections.sort(listBean,new YJComparatorDESC_Measue());
					LDYJ_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[13] = true;
					Collections.sort(listBean, new YJComparatorASC_Measue());
					LDYJ_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		case R.id.tv_JHCL_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[14]) {
					SortFlag[14] = false;					
					Collections.sort(listBean,new JHALComparatorDESC_Measue());
					JHCL_iv.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[14] = true;
					Collections.sort(listBean, new JHALComparatorASC_Measue());
					JHCL_iv.setBackgroundResource(R.drawable.asc);
				}
				measue_Adapter.onDateChange(listBean);			
			}
			break;
		}
	}

	private void Reset_SortImage() {
		PotNo_iv.setBackgroundResource(R.drawable.sort);
		Ddate_iv.setBackgroundResource(R.drawable.sort);
		ALCnt_iv.setBackgroundResource(R.drawable.sort);
		LSP_iv.setBackgroundResource(R.drawable.sort);
		DJZSP_iv.setBackgroundResource(R.drawable.sort);
		DJWD_iv.setBackgroundResource(R.drawable.sort);
		FZB_iv.setBackgroundResource(R.drawable.sort);

		FeCnt_iv.setBackgroundResource(R.drawable.sort);
		SiCnt_iv.setBackgroundResource(R.drawable.sort);
		ALOCnt_iv.setBackgroundResource(R.drawable.sort);
		CaFCnt_iv.setBackgroundResource(R.drawable.sort);
		MgCnt_iv.setBackgroundResource(R.drawable.sort);
		MLSP_iv.setBackgroundResource(R.drawable.sort);
		LDYJ_iv.setBackgroundResource(R.drawable.sort);
		JHCL_iv.setBackgroundResource(R.drawable.sort);
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
