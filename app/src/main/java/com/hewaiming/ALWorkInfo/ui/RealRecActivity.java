package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_RealRecordAdapter;
import com.hewaiming.ALWorkInfo.bean.RealRecord;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;

import android.app.Activity;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RealRecActivity extends Activity implements HttpGetListener, OnScrollListener, OnClickListener {
	private Spinner spinner_area, spinner_potno, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ListView lv_realRec;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private ArrayAdapter<String> PotNo_adapter;

	private HttpPost_BeginDate_EndDate http_post;

	private String potno_url = ":8000/scgy/android/odbcPhP/RealRecordTable_potno_date.php";
	private String area_url = ":8000/scgy/android/odbcPhP/RealRecordTable_area_date.php";

	private String PotNo, BeginDate, EndDate;

	private List<String> dateBean = new ArrayList<String>();
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private List<String> PotNoList;
	private List<RealRecord> listBean = null;
	private HSView_RealRecordAdapter realRec_Adapter = null;
	private ImageButton isShowingBtn;
	private LinearLayout showArea = null;
	private RelativeLayout mHead;
	private ListView lv_RealRec;
	private View layout_list;
	private String ip;
	private int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_record);
		MyApplication.getInstance().addActivity(this);
		GetDataFromIntent();
		init_area();
		init_potNo();
		init_date();
		init_title();
		init_HSView();
	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip=getIntent().getStringExtra("ip");
		port=getIntent().getIntExtra("port", 1234);
		potno_url="http://"+ip+potno_url;
		area_url="http://"+ip+area_url;
	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_RealRec = (ListView) findViewById(R.id.lv_RealRec);
		lv_RealRec.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_RealRec.setCacheColorHint(0);
		lv_RealRec.setOnScrollListener(this);

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
		layout_list=findViewById(R.id.Layout_RealRec);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("实时记录");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		isShowingBtn.setOnClickListener(this);

	}

	private void init_area() {
		lv_realRec = (ListView) findViewById(R.id.lv_RealRec);
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
			Toast.makeText(getApplicationContext(), "没有获取到[实时记录]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // 清除LISTVIEW 以前的内容
					realRec_Adapter.onDateChange(listBean);
				}
			}
		} else {

			listBean = new ArrayList<RealRecord>();
			listBean.clear();
			listBean = JsonToBean_Area_Date.JsonArrayToRealRecordBean(data, JXList);

			realRec_Adapter = new HSView_RealRecordAdapter(this, R.layout.item_hsview_real_record, listBean, mHead);

			lv_realRec.setAdapter(realRec_Adapter);
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
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		public boolean onTouch(View arg0, MotionEvent arg1) {
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);			
			headSrcrollView.onTouchEvent(arg1);		
			return false;
		}
	}

}
