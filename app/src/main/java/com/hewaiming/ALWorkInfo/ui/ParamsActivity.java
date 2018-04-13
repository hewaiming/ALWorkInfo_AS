package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.SortDayTable.ALCntComparatorASC_DayTable;
import com.hewaiming.ALWorkInfo.SortDayTable.ALCntComparatorDESC_DayTable;
import com.hewaiming.ALWorkInfo.SortParams.AEComparatorASC_Params;
import com.hewaiming.ALWorkInfo.SortParams.AEComparatorDESC_Params;
import com.hewaiming.ALWorkInfo.SortParams.FHLComparatorASC_Params;
import com.hewaiming.ALWorkInfo.SortParams.FHLComparatorDESC_Params;
import com.hewaiming.ALWorkInfo.SortParams.NBComparatorASC_Params;
import com.hewaiming.ALWorkInfo.SortParams.NBComparatorDESC_Params;
import com.hewaiming.ALWorkInfo.SortParams.PotNoComparatorASC_Params;
import com.hewaiming.ALWorkInfo.SortParams.PotNoComparatorDESC_Params;
import com.hewaiming.ALWorkInfo.SortParams.SetVComparatorASC_Params;
import com.hewaiming.ALWorkInfo.SortParams.SetVComparatorDESC_Params;
import com.hewaiming.ALWorkInfo.adapter.Params_Adapter;
import com.hewaiming.ALWorkInfo.bean.SetParams;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpPost_Area;
import com.hewaiming.ALWorkInfo.view.HeaderListView_Params;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ParamsActivity extends Activity implements HttpGetListener, OnClickListener {
	private Spinner spinner;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ListView lv_params;
	private ArrayAdapter<String> Area_adapter;
	private AsyTask_HttpPost_Area http_post;
	private HeaderListView_Params headerView;
	private String url = ":8000/scgy/android/odbcPhP/PotSetValueTable.php";
	private List<SetParams> listBean = null;
	private Params_Adapter mAdapter = null;
	private String ip;
	private int port;
	private LinearLayout mHead;
	private boolean[] SortFlag={false,false,false,false,false};
	private ImageView iv_PotNo,iv_SetV,iv_NB,iv_Ae,iv_FHL;
	private TextView PotNo_head,SetV_head,Nb_head,Ae_head,FHL_head;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_params);
		MyApplication.getInstance().addActivity(this);
		GetDataFromIntent();
		init();
		init_title();
		init_sort();
		DoGetDataFromNet();
	}

	private void GetDataFromIntent() {
		ip=	getIntent().getStringExtra("ip");
		port=getIntent().getIntExtra("port", 1234);
		url="http://"+ip+url;		
	}

	private void init_title() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("常用参数");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

	}

	private void init() {
		mHead = (LinearLayout) findViewById(R.id.head); // 表头处理
		mHead.setFocusable(true);
		mHead.setClickable(true);	
		mHead.setBackgroundColor(Color.parseColor("#fffff7"));
		
		lv_params = (ListView) findViewById(R.id.lv_params);
		spinner = (Spinner) findViewById(R.id.spinner_area);
		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// 设置下拉列表的风格
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner.setAdapter(Area_adapter);
		// 添加事件Spinner事件监听
		// 设置默认值
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
		findBtn = (Button) findViewById(R.id.btn_params);
		findBtn.setOnClickListener(this);
	}

	protected void DoGetDataFromNet() {
		http_post = (AsyTask_HttpPost_Area) new AsyTask_HttpPost_Area(url, this, this, Integer.toString(areaId)).execute();

	}

	@Override
	public void GetDataUrl(String data) {
		if (data==null) {
			Toast.makeText(getApplicationContext(), "没有获取到【常用参数】数据！", Toast.LENGTH_LONG).show();
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // 清除LISTVIEW 以前的内容
					mAdapter.onDateChange(listBean);
				}
			}
		} else if(!data.isEmpty()){
			/*if (lv_params.getHeaderViewsCount() > 0) {
				lv_params.removeHeaderView(headerView);
			}
			headerView = new HeaderListView_Params(this);// 添加表头
			headerView.setTvPotNo("槽号");
			headerView.setTvSetV("设定电压");
			headerView.setTvNBTime("NB时间");
			headerView.setTvAETime("AE间隔");
			headerView.setTvALF("氟化铝下料量");
			lv_params.addHeaderView(headerView);*/
			listBean = new ArrayList<SetParams>();
			listBean = JsonToBean.JsonArrayToSetParamsBean(data);
			mAdapter = new Params_Adapter(this, listBean);
			lv_params.setAdapter(mAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_params:
			DoGetDataFromNet();
			break;
		case R.id.tv_PotNo_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[0]) {
					SortFlag[0] = false;					
					Collections.sort(listBean,new PotNoComparatorDESC_Params());
					iv_PotNo.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[0] = true;
					Collections.sort(listBean, new PotNoComparatorASC_Params());
					iv_PotNo.setBackgroundResource(R.drawable.asc);
				}			
				mAdapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_SetV_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[1]) {
					SortFlag[1] = false;					
					Collections.sort(listBean,new SetVComparatorDESC_Params());
					iv_SetV.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[1] = true;
					Collections.sort(listBean, new SetVComparatorASC_Params());
					iv_SetV.setBackgroundResource(R.drawable.asc);
				}			
				mAdapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_NbTime_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[2]) {
					SortFlag[2] = false;					
					Collections.sort(listBean,new NBComparatorDESC_Params());
					iv_NB.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[2] = true;
					Collections.sort(listBean, new NBComparatorASC_Params());
					iv_NB.setBackgroundResource(R.drawable.asc);
				}			
				mAdapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_AeTime_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[3]) {
					SortFlag[3] = false;					
					Collections.sort(listBean,new AEComparatorDESC_Params());
					iv_Ae.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[3] = true;
					Collections.sort(listBean, new AEComparatorASC_Params());
					iv_Ae.setBackgroundResource(R.drawable.asc);
				}			
				mAdapter.onDateChange(listBean);
			}
			break;
		case R.id.tv_ALF_head:
			if ((listBean != null) && (!listBean.isEmpty())) {
				Reset_SortImage();
				if (SortFlag[4]) {
					SortFlag[4] = false;					
					Collections.sort(listBean,new FHLComparatorDESC_Params());
					iv_FHL.setBackgroundResource(R.drawable.desc);
				} else {
					SortFlag[4] = true;
					Collections.sort(listBean, new FHLComparatorASC_Params());
					iv_FHL.setBackgroundResource(R.drawable.asc);
				}			
				mAdapter.onDateChange(listBean);
			}
			break;
		}
	}

	private void Reset_SortImage() {
		iv_PotNo.setBackgroundResource(R.drawable.sort);
		iv_SetV.setBackgroundResource(R.drawable.sort);
		iv_NB.setBackgroundResource(R.drawable.sort);
		iv_Ae.setBackgroundResource(R.drawable.sort);
		iv_FHL.setBackgroundResource(R.drawable.sort);	
		
	}

	private void init_sort() {
		PotNo_head = (TextView) findViewById(R.id.tv_PotNo_head);
		PotNo_head.setOnClickListener(this);
		
		SetV_head = (TextView) findViewById(R.id.tv_SetV_head);
		SetV_head.setOnClickListener(this);
		Nb_head = (TextView) findViewById(R.id.tv_NbTime_head);
		Nb_head.setOnClickListener(this);
		
		Ae_head = (TextView) findViewById(R.id.tv_AeTime_head);
		Ae_head.setOnClickListener(this);
		
		FHL_head = (TextView) findViewById(R.id.tv_ALF_head);
		FHL_head.setOnClickListener(this);		

		iv_PotNo = (ImageView) findViewById(R.id.iv_PotNo);
		iv_SetV = (ImageView) findViewById(R.id.iv_SetV);
		iv_NB = (ImageView) findViewById(R.id.iv_NbTime);
		iv_Ae = (ImageView) findViewById(R.id.iv_AeTime);
		iv_FHL = (ImageView) findViewById(R.id.iv_ALF);		
	}

}
