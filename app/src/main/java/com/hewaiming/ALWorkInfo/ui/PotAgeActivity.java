package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.PotAge_Adapter;
import com.hewaiming.ALWorkInfo.bean.PotAge;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpPost_Area;
import com.hewaiming.ALWorkInfo.view.HeaderListView_PotAge;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PotAgeActivity extends Activity implements HttpGetListener, OnClickListener {
	private Spinner spinner;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ListView lv_potage;
	private ArrayAdapter<String> Area_adapter;
	private AsyTask_HttpPost_Area http_post;
	private HeaderListView_PotAge headerView;
	private String url = ":8000/scgy/android/odbcPhP/PotAgeTable.php";
	private List<PotAge> listBean=null;
	private PotAge_Adapter potage_Adapter=null;
	private String ip;
	private int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_potage);
		MyApplication.getInstance().addActivity(this);
		GetDataFromIntent();
		init();
		init_title();
		DoGetDataFromNet();
	}

	private void GetDataFromIntent() {
		ip=getIntent().getStringExtra("ip");
		port=getIntent().getIntExtra("port", 1234);
		url="http://"+ip+url;
		
	}

	private void init_title() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("槽龄表");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

	}

	private void init() {
		lv_potage = (ListView) findViewById(R.id.lv_potage);
		spinner = (Spinner) findViewById(R.id.PotAge_spinner_area);
		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// 设置下拉列表的风格
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner.setAdapter(Area_adapter);
		// 添加事件Spinner事件监听		
		spinner.setVisibility(View.VISIBLE);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
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
				areaId = 11;
			}

		});
		findBtn = (Button) findViewById(R.id.btn_potage);
		findBtn.setOnClickListener(this);
	}

	@Override
	public void GetDataUrl(String data) {
		if (data==null) {
			Toast.makeText(getApplicationContext(), "没有获取到[槽龄表]数据！", Toast.LENGTH_LONG).show();
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // 清除LISTVIEW 以前的内容
					potage_Adapter.onDateChange(listBean);
				}
			}
		} else if(!data.isEmpty()) {
			if (lv_potage.getHeaderViewsCount() > 0) {
				lv_potage.removeHeaderView(headerView);
			}
			headerView = new HeaderListView_PotAge(this);// 添加表头
			headerView.setTvPotNo("槽号");
			headerView.setTvBeginTime("启槽时间");
			headerView.setTvEndTime("停槽时间");
			headerView.setTvAge("槽年代");			
			lv_potage.addHeaderView(headerView);
			
			listBean=new ArrayList<PotAge>();
			listBean = JsonToBean.JsonArrayToPotAgeBean(data);		
			potage_Adapter=new PotAge_Adapter(this,listBean);
			lv_potage.setAdapter(potage_Adapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_potage:
			DoGetDataFromNet();		
			break;
		}
	}

	private void DoGetDataFromNet() {
		http_post = (AsyTask_HttpPost_Area) new AsyTask_HttpPost_Area(url, this, this, Integer.toString(areaId)).execute();
		
	}

}
