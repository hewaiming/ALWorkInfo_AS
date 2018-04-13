package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.MyPageAdapter;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.fragment.Fragment_Ae1;
import com.hewaiming.ALWorkInfo.fragment.Fragment_Ae2;
import com.hewaiming.ALWorkInfo.fragment.Fragment_Ae3;
import com.hewaiming.ALWorkInfo.fragment.Fragment_Ae4;
import com.hewaiming.ALWorkInfo.fragment.Fragment_Ae5;
import com.hewaiming.ALWorkInfo.json.JsonToMultiList;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpPost_Area;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Ae5DayActivity extends FragmentActivity implements HttpGetListener, OnClickListener {
	private Spinner spinner_area;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter;
	private AsyTask_HttpPost_Area http_post;
	private String area_url = ":8000/scgy/android/odbcPhP/AeRecord5Day_area.php";	

	private ArrayList<Fragment> fragments;
	private ViewPager pager;
	private MyPageAdapter adapter;
	private RadioButton btn_ae1, btn_ae2, btn_ae3, btn_ae4, btn_ae5;
	private RadioGroup group;
	private Handler mHandler_Ae1,mHandler_Ae2,mHandler_Ae3,mHandler_Ae4,mHandler_Ae5;
	
	private Map<String,List<AeRecord>> map_5day=null;
	private View layout_Ae;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private String ip;
	private int port;
	private Context ctx;	
	private List<String> dateBean = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ae_5day);
		MyApplication.getInstance().addActivity(this);
		layout_Ae = findViewById(R.id.Ae5Day);
		layout_Ae.setVisibility(View.VISIBLE);
		ctx=this;
		GetDataFromIntent();
		init_area();
		init_title();
		init_Tab();
		DoGetDataFromNet();
		if (!MyConst.GetDataFromSharePre(ctx,"AE5Day_Show")){
			MyConst.GuideDialog_show(ctx,"AE5Day_Show");  //第一次显示
		}
	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip=getIntent().getStringExtra("ip");
		port=getIntent().getIntExtra("port", 1234);
		area_url="http://"+ip+area_url;
	}

	private void init_Tab() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new Fragment_Ae1(JXList,dateBean,ip,port));
		fragments.add(new Fragment_Ae2(JXList,dateBean,ip,port));
		fragments.add(new Fragment_Ae3(JXList,dateBean,ip,port));
		fragments.add(new Fragment_Ae4(JXList,dateBean,ip,port));
		fragments.add(new Fragment_Ae5(JXList,dateBean,ip,port));		

		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(fragments.size() - 1);// 缓存页面,显示第一个缓存最后一个
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				Log.v("onPageSelected", "onPageSelected:"+arg0);
				getTabState(arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		group = (RadioGroup) findViewById(R.id.radioGroup1);
		btn_ae1 = (RadioButton) findViewById(R.id.radio_Ae1);
		btn_ae2 = (RadioButton) findViewById(R.id.radio_Ae2);
		btn_ae3 = (RadioButton) findViewById(R.id.radio_Ae3);
		btn_ae4 = (RadioButton) findViewById(R.id.radio_Ae4);
		btn_ae5 = (RadioButton) findViewById(R.id.radio_Ae5);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_Ae1:
					pager.setCurrentItem(0);
					break;
				case R.id.radio_Ae2:
					pager.setCurrentItem(1);
					break;
				case R.id.radio_Ae3:
					pager.setCurrentItem(2);
					break;
				case R.id.radio_Ae4:
					pager.setCurrentItem(3);
					break;
				case R.id.radio_Ae5:
					pager.setCurrentItem(4);
					break;
				default:
					break;
				}

			}
		});

	}

	protected void getTabState(int index) {

		btn_ae1.setChecked(false);
		btn_ae2.setChecked(false);
		btn_ae3.setChecked(false);
		btn_ae4.setChecked(false);
		btn_ae5.setChecked(false);

		switch (index) {
		case 0:
			btn_ae1.setChecked(true);
			break;
		case 1:
			btn_ae2.setChecked(true);
			break;
		case 2:
			btn_ae3.setChecked(true);
			break;
		case 3:
			btn_ae4.setChecked(true);
			break;
		case 4:
			btn_ae5.setChecked(true);
			break;
		default:
			break;
		}

	}

	private void init_title() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("效应情报表");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);		
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
				DoGetDataFromNet();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		findBtn = (Button) findViewById(R.id.btn_ok);
		findBtn.setOnClickListener(this);
	}

	@Override
	public void GetDataUrl(String data) {
		
		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "没有获取到[效应情报表]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			
		} else {

			map_5day = new HashMap<String,List<AeRecord>>();
//			map_5day.clear();
			map_5day = JsonToMultiList.JsonArrayToAeRecord_5DayBean(data);
		
			Message msg1 = new Message();
			msg1.obj = map_5day.get("ae1");
			msg1.what = 1;
			mHandler_Ae1.sendMessage(msg1);
			
			Message msg2 = new Message();
			msg2.obj = map_5day.get("ae2");
			msg2.what = 2;
			mHandler_Ae2.sendMessage(msg2);
			
			Message msg3 = new Message();
			msg3.obj = map_5day.get("ae3");
			msg3.what =3;
			mHandler_Ae3.sendMessage(msg3);
			
			Message msg4 = new Message();
			msg4.obj = map_5day.get("ae4");
			msg4.what = 4;
			mHandler_Ae4.sendMessage(msg4);
			
			Message msg5 = new Message();
			msg5.obj = map_5day.get("ae5");
			msg5.what = 5;
			mHandler_Ae5.sendMessage(msg5);			
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_ok:	
			DoGetDataFromNet();				
			break;
		}
	}
	
	private void DoGetDataFromNet() {
		http_post = (AsyTask_HttpPost_Area) new AsyTask_HttpPost_Area(area_url, this, this, Integer.toString(areaId)).execute();
		
	}

	public void setHandler_Ae1(Handler mHandler_Ae) {
		mHandler_Ae1=mHandler_Ae;
		
	}
	public void setHandler_Ae2(Handler mHandler_Ae) {
		mHandler_Ae2=mHandler_Ae;
		
	}
	public void setHandler_Ae3(Handler mHandler_Ae) {
		mHandler_Ae3=mHandler_Ae;
		
	}
	public void setHandler_Ae4(Handler mHandler_Ae) {
		mHandler_Ae4=mHandler_Ae;
		
	}
	public void setHandler_Ae5(Handler mHandler_Ae) {
		mHandler_Ae5=mHandler_Ae;
		
	}
	
}
