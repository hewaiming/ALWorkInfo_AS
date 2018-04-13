package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.bean.MeasueTable;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JSONArrayParser;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.json.JsonToMultiList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CraftLineActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	protected static final String TAG = "CraftLineActivity CyclicBarrier";
	private Spinner spinner_area, spinner_potno, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private ArrayAdapter<String> PotNo_adapter;
	private String potno_url = ":8000/scgy/android/odbcPhP/dayTable_Craft.php";
	private String measue_potno_url = ":8000/scgy/android/odbcPhP/MeasueTable_Craft.php";
	private String PotNo, BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();
	private List<String> PotNoList = null;
	private List<dayTable> listBean_daytable = null;
	private List<CheckBox> list_cb = new ArrayList<CheckBox>();
	private CheckBox cb0, cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14, cb15,cb_NoneAll;
	private String selitems = "";
	private List<MeasueTable> listBean_measuetable = null;
	private ProgressDialog m_ProgressDialog = null;
	private ImageButton isShowingBtn;
	private LinearLayout showArea = null;
	private String ip;
	private int port;
	private int default_PotNo = 1, default_Area = 11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_craft_line);
		MyApplication.getInstance().addActivity(this);
		GetDataFromIntent();
		init_title();
		init_potNo();
		init_area();
		init_date();
		init_items();
	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_table");
		PotNo = getIntent().getStringExtra("PotNo_Selected");
		GetPotNo_Area(PotNo);
		// JXList = (List<Map<String, Object>>)
		// getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
		potno_url = "http://" + ip + potno_url;
		measue_potno_url = "http://" + ip + measue_potno_url;
	}

	private void GetPotNo_Area(String potNo) {
		if(potNo!=null && !potNo.equals("")) {		
			int tmpNo = Integer.parseInt(potNo);
			default_Area = tmpNo / 100;
			default_PotNo = tmpNo % 100;
		}

	}

	private void init_items() {
		cb0 = (CheckBox) findViewById(R.id.chkbox_SetV);
		cb1 = (CheckBox) findViewById(R.id.chkbox_WorkV);
		cb2 = (CheckBox) findViewById(R.id.chkbox_AvgV);
		cb3 = (CheckBox) findViewById(R.id.chkbox_Noise);
		cb4 = (CheckBox) findViewById(R.id.chkbox_DYBTime);
		cb5 = (CheckBox) findViewById(R.id.chkbox_ALF);
		cb6 = (CheckBox) findViewById(R.id.chkbox_AeCnt);
		cb7 = (CheckBox) findViewById(R.id.chkbox_ALO);
		cb8 = (CheckBox) findViewById(R.id.chkbox_ALzs);
		cb9 = (CheckBox) findViewById(R.id.chkbox_ALCnt);
		cb10 = (CheckBox) findViewById(R.id.chkbox_FeCnt);
		cb11 = (CheckBox) findViewById(R.id.chkbox_SiCnt);
		cb12 = (CheckBox) findViewById(R.id.chkbox_FZB);
		cb13 = (CheckBox) findViewById(R.id.chkbox_DJWD);
		cb14 = (CheckBox) findViewById(R.id.chkbox_LSP);
		cb15 = (CheckBox) findViewById(R.id.chkbox_DJZSP);
		cb_NoneAll=(CheckBox) findViewById(R.id.chkbox_ALL);
		list_cb.add(cb0);
		list_cb.add(cb1);
		list_cb.add(cb2);
		list_cb.add(cb3);
		list_cb.add(cb4);
		list_cb.add(cb5);
		list_cb.add(cb6);
		list_cb.add(cb7);
		list_cb.add(cb8);
		list_cb.add(cb9);
		list_cb.add(cb10);
		list_cb.add(cb11);
		list_cb.add(cb12);
		list_cb.add(cb13);
		list_cb.add(cb14);
		list_cb.add(cb15);
		list_cb.add(cb_NoneAll);
		for (CheckBox cb : list_cb) {
			cb.setOnCheckedChangeListener(this);
		}

	}

	private void init_potNo() {
		spinner_potno = (Spinner) findViewById(R.id.spinner_PotNo);
		PotNoList = new ArrayList<String>();
		for (int i = 1101; i <= 1136; i++) {
			PotNoList.add(i + "");
		}
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
				Toast.makeText(getApplicationContext(), "没有选择槽号", 1).show();
			}

		});
		spinner_potno.setSelection(default_PotNo - 1);
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
		if(dateBean.size()>25)
		{
			spinner_beginDate.setSelection(25);
		}else{
			spinner_beginDate.setSelection(dateBean.size()-1);
		}		
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
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("工艺曲线");
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
		switch (default_Area) {
		case 11:
			spinner_area.setSelection(0);
			break;
		case 12:
			spinner_area.setSelection(1);
			break;
		case 13:
			spinner_area.setSelection(2);
			break;
		case 21:
			spinner_area.setSelection(3);
			break;
		case 22:
			spinner_area.setSelection(4);
			break;
		case 23:
			spinner_area.setSelection(5);
			break;
		}
		PotNoChanged(default_Area);
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
			if ((PotNoList != null) && (!PotNoList.isEmpty())) {
				PotNoList.clear();
				for (int i = 1101; i <= 1136; i++) {
					PotNoList.add(i + "");
				}
			}
			break;
		case 12:
			if ((PotNoList != null) && (!PotNoList.isEmpty())) {
				PotNoList.clear();
				for (int i = 1201; i <= 1237; i++) {
					PotNoList.add(i + "");
				}
			}
			break;
		case 13:
			if ((PotNoList != null) && (!PotNoList.isEmpty())) {
				PotNoList.clear();
				for (int i = 1301; i <= 1337; i++) {
					PotNoList.add(i + "");
				}
			}
			break;
		case 21:
			if ((PotNoList != null) && (!PotNoList.isEmpty())) {
				PotNoList.clear();
				for (int i = 2101; i <= 2136; i++) {
					PotNoList.add(i + "");
				}
			}
			break;
		case 22:
			if ((PotNoList != null) && (!PotNoList.isEmpty())) {
				PotNoList.clear();
				for (int i = 2201; i <= 2237; i++) {
					PotNoList.add(i + "");
				}
			}
			break;
		case 23:
			if ((PotNoList != null) && (!PotNoList.isEmpty())) {
				PotNoList.clear();
				for (int i = 2301; i <= 2337; i++) {
					PotNoList.add(i + "");
				}
			}
			break;
		}
		if (default_PotNo == 1) {
			spinner_potno.setSelection(0);
			if (PotNoList!=null && !PotNoList.isEmpty()){
				PotNo = PotNoList.get(0).toString();
				PotNo_adapter.notifyDataSetChanged();// 通知数据改变
			}			
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
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date bdate = df.parse(BeginDate);
					Date edate = df.parse(EndDate);
					long TIME_DAY_MILLISECOND = 86400000;
					Long days = (edate.getTime() - bdate.getTime()) / (TIME_DAY_MILLISECOND);
					if (days > 31) {
						Toast.makeText(getApplicationContext(), "数据量太大：截止日期-开始日期>31,请重新选择日期", 1).show();
					} else {
						// 选定各项曲线部位
						selitems = "";
						for (CheckBox cBox : list_cb) {
							if (cBox.isChecked()) {
								selitems += cBox.getText() + ",";
							}
						}
						if (selitems.equals("")) {
							Toast.makeText(getApplicationContext(), "没有选中任何一项工艺参数，请选择工艺参数项！", 1).show();
							break;
						} else {
							m_ProgressDialog = ProgressDialog.show(CraftLineActivity.this, "请等待...", "正在获取工艺参数数据 ...", true);
							showCraft(); // 并发进程方式取数据
							// m_ProgressDialog.dismiss();
						}
					}
				} catch (ParseException e) {
					Log.i("工艺参数：测量数据 ---", e.getMessage());					
				}
			}
			break;
		}
	}

	private void showCraft() {

		ExecutorService exec = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {
			@Override
			public void run() {				
				Log.i("工艺参数：","所有工艺参数都OK，开始happy去");
				Intent show_intent = new Intent(CraftLineActivity.this, ShowCraftLineActivity.class);
				Bundle mbundle = new Bundle();
				mbundle.putString("PotNo", PotNo);
				mbundle.putString("Begin_End_Date", BeginDate + " 至 " + EndDate);
				mbundle.putSerializable("list_daytable", (Serializable) listBean_daytable);
				mbundle.putSerializable("list_measuetable", (Serializable) listBean_measuetable);
				mbundle.putString("SELITEMS", selitems);
				show_intent.putExtras(mbundle);
				m_ProgressDialog.dismiss();
				startActivity(show_intent); // 显示工艺曲线图
			}
		});

		exec.execute(new Runnable() {
			@Override
			public void run() {			
				Log.i("工艺参数：","测量数据OK，其他哥们呢");
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("PotNo", PotNo)); // 槽号
				mparams.add(new BasicNameValuePair("BeginDate", BeginDate));
				mparams.add(new BasicNameValuePair("EndDate", EndDate));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(measue_potno_url, "POST", mparams);
				if (json != null) {
					//Log.d("工艺参数：测量数据", json.toString());// 从服务器返回有数据
					listBean_measuetable = new ArrayList<MeasueTable>();
					listBean_measuetable = JsonToBean_Area_Date.JsonArrayToMeasueTableBean(json.toString());
				} else {
					Log.i("工艺参数：测量数据 ---", "从PHP服务器无数据返回！");
				}

				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG,"工艺参数：测量数据 出错1 InterruptedException");	
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG,"工艺参数：测量数据 出错2 ---");					
				}
			}
		});
		exec.execute(new Runnable() {
			@Override
			public void run() {
				//System.out.println("日报数据OK，其他哥们呢");
				Log.i("工艺参数：","日报数据OK，其他哥们呢");
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("PotNo", PotNo)); // 槽号
				mparams.add(new BasicNameValuePair("BeginDate", BeginDate));
				mparams.add(new BasicNameValuePair("EndDate", EndDate));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(potno_url, "POST", mparams);
				if (json != null) {
					//Log.d("工艺参数：日报数据", json.toString());// 从服务器返回有数据
					listBean_daytable = new ArrayList<dayTable>();
					listBean_daytable = JsonToMultiList.JsonArrayToDayTableBean(json.toString());
				} else {
					Log.i("工艺参数：日报数据 ---", "从PHP服务器无数据返回！");
				}

				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {					
					Log.e(TAG,"工艺参数：日报数据 错误1---");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG,"工艺参数：日报数据 错误2---");					
				}
			}
		});
		exec.shutdown();
	}

	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.getId()==R.id.chkbox_ALL){
			if(isChecked){
			 //全选	
				chkbox_ALL();
			}else{
			  //全不选
				chkbox_None();
			}
		}
		Log.i("chkbox", buttonView.getText().toString() + isChecked);
	}

	private void chkbox_None() {
		for(int i=0;i<list_cb.size()-1;i++){
			list_cb.get(i).setChecked(false);
		}
		
	}

	private void chkbox_ALL() {
		for(int i=0;i<list_cb.size()-1;i++){
			list_cb.get(i).setChecked(true);
		}
		
	}

}
