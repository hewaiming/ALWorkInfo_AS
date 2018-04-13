package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_FaultMostAdapter;
import com.hewaiming.ALWorkInfo.bean.FaultMost;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.view.FooterListView;

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
import android.widget.AdapterView.OnItemClickListener;
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

public class FaultMostActivity extends Activity implements HttpGetListener, OnClickListener, OnScrollListener {
	private Spinner spinner_area, spinner_PotNo, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title, tv_Total;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private HttpPost_BeginDate_EndDate http_post;
	private String fault_url = ":8000/scgy/android/odbcPhP/FaultMost_area_date.php";
	private String BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();

	private List<FaultMost> listBean = null;
	private HSView_FaultMostAdapter FaultMost_Adapter = null;

	private LinearLayout showArea = null;
	private View layout_faultmost;
	private ImageButton isShowingBtn;
	private RelativeLayout mHead;
	private ListView lv_FaultMost;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private String ip;
	private int port;
	private Context mContext;
	private FooterListView footView;
	private BarChart mChart_FaultMost;
	private BarData chartData;
	private int[] Area_FaultCnt = { 0, 0, 0, 0, 0, 0 }; // 区故障次数总和
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fault_most);
		MyApplication.getInstance().addActivity(this);
		layout_faultmost = findViewById(R.id.Layout_FaultMost);
		GetDataFromIntent();
		mContext = this;
		init_area();
		init_date();
		init_HSView();
		init_footer();
		init_title();
		if (!MyConst.GetDataFromSharePre(mContext, "FaultMost_Show")) {
			MyConst.GuideDialog_show(mContext, "FaultMost_Show"); // 第一次显示
		}
	}

	private void init_footer() {
		footView = new FooterListView(getApplicationContext());// 添加表end
		lv_FaultMost.addFooterView(footView);
		tv_Total = (TextView) findViewById(R.id.tv_footTotal);
	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
		fault_url = "http://" + ip + fault_url;

	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_FaultMost = (ListView) findViewById(R.id.lv_FaultMost);
		lv_FaultMost.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_FaultMost.setCacheColorHint(0);
		lv_FaultMost.setOnScrollListener(this);
		lv_FaultMost.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position != listBean.size()) {
					Intent faultRec_intent = new Intent(FaultMostActivity.this, FaultRecActivity.class);
					Bundle bundle_faultRec = new Bundle();
					bundle_faultRec.putStringArrayList("date_record", (ArrayList<String>) dateBean);
					bundle_faultRec.putBoolean("Hide_Action", true);
					bundle_faultRec.putString("PotNo", String.valueOf(listBean.get(position).getPotNo()));
					bundle_faultRec.putString("Begin_Date", BeginDate);
					bundle_faultRec.putString("End_Date", EndDate);
					bundle_faultRec.putSerializable("JXList", (Serializable) JXList);
					bundle_faultRec.putString("ip", ip);
					bundle_faultRec.putInt("port", port);
					faultRec_intent.putExtras(bundle_faultRec);
					startActivity(faultRec_intent); // 故障记录
				}

			}
		});
	}

	private void init_date() {
		spinner_PotNo = (Spinner) findViewById(R.id.spinner_PotNo);
		spinner_PotNo.setVisibility(View.GONE);
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
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("故障最多");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		isShowingBtn.setOnClickListener(this);
		mChart_FaultMost = (BarChart) findViewById(R.id.FaultMost_chart);
	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);
		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas_ALL);
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
					areaId = 66;
					break;
				case 1:
					areaId = 61;
					break;
				case 2:
					areaId = 62;
					break;
				case 3:
					areaId = 11;
					break;
				case 4:
					areaId = 12;
					break;
				case 5:
					areaId = 13;
					break;
				case 6:
					areaId = 21;
					break;
				case 7:
					areaId = 22;
					break;
				case 8:
					areaId = 23;
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		findBtn = (Button) findViewById(R.id.btn_ok);
		findBtn.setOnClickListener(this);
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
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.up_yellow));
			} else {
				showArea.setVisibility(View.GONE);
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.down_yellow));
			}
			break;
		case R.id.btn_ok:
			if (EndDate.compareTo(BeginDate) < 0) {
				Toast.makeText(getApplicationContext(), "日期选择不对：截止日期小于开始日期", 1).show();
			} else {
				http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(fault_url, 1,
						Integer.toString(areaId), BeginDate, EndDate, this, this).execute();

				layout_faultmost.setVisibility(View.VISIBLE);
				// 选择厂房显示图表
				if (areaId == 66) {
					mChart_FaultMost.setVisibility(View.VISIBLE);
				} else {
					mChart_FaultMost.setVisibility(View.GONE);
				}
			}
			break;
		}
	}

	@Override
	public void GetDataUrl(String data) {
		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "没有获取到[故障率最多]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			tv_Total.setText("0次");
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // 清除LISTVIEW 以前的内容
					FaultMost_Adapter.onDateChange(listBean);
				}
			}
		} else {
			listBean = new ArrayList<FaultMost>();
			listBean.clear();
			listBean = JsonToBean_Area_Date.JsonArrayToFaultCntBean(data);
			int total = 0;
			for (FaultMost tmp : listBean) {
				total = total + tmp.getFaultCnt(); // 统计总数
			}
			FaultMost_Adapter = new HSView_FaultMostAdapter(this, R.layout.item_hsview_faultmost, listBean, mHead);
			lv_FaultMost.setAdapter(FaultMost_Adapter);
			tv_Total.setText(total + "次");
			//各区柱形图显示
			for (int i = 0; i < Area_FaultCnt.length; i++) {
				 Area_FaultCnt[i] = 0;
			}
			//有记录
			if (listBean != null) {
				// 分别统计各区下料异常总次数
				for (FaultMost tmp : listBean) {
					if (tmp.getPotNo() >= 1101 && tmp.getPotNo() <= 1136) {
						 Area_FaultCnt[0] =  Area_FaultCnt[0] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 1201 && tmp.getPotNo() <= 1237) {
						 Area_FaultCnt[1] =  Area_FaultCnt[1] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 1301 && tmp.getPotNo() <= 1337) {
						 Area_FaultCnt[2] =  Area_FaultCnt[2] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 2101 && tmp.getPotNo() <= 2136) {
						 Area_FaultCnt[3] =  Area_FaultCnt[3] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 2201 && tmp.getPotNo() <= 2237) {
						 Area_FaultCnt[4] =  Area_FaultCnt[4] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 2301 && tmp.getPotNo() <= 2337) {
						 Area_FaultCnt[5] =  Area_FaultCnt[5] + tmp.getFaultCnt();
					}
				}
			}
			chartData = getChartData(Area_FaultCnt);
			showChart(mChart_FaultMost, chartData);
		}
	}
	//获取图表数据
	private BarData getChartData(int[] faultCnt) {
		ArrayList<String> xVals = new ArrayList<String>(); // xVals用来表示区名
		for(int i=0;i<faultCnt.length;i++){
			if(i<3){
				xVals.add("一厂"+(i+1)+"区");	
			}else{
				xVals.add("二厂"+(i-2)+"区");
			}			
		}		

		List<BarEntry> yValues = new ArrayList<BarEntry>(); // yVals用来表示封装每个饼块的实际数据
		/** 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38 所以 14代表的百分比就是14% */
		for(int i=0;i<faultCnt.length;i++){
			yValues.add(new BarEntry((float)faultCnt[i], i)); 
		}		
		// y轴的集合
		BarDataSet barDataSet = new BarDataSet(yValues, "各区故障总数对比图");/* 显示在比例图上 */
		barDataSet.setColor(Color.RED);// 设置数据露草色颜色
		barDataSet.setDrawValues(true); // 显示数值
		barDataSet.setValueTextSize(11f);
		barDataSet.setBarSpacePercent(60f);
		//barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("#");// 构造方法的字符格式这里如果小数不足2位,会以0补足.

				return decimalFormat.format(value);
			}
		});			
		
		BarData mData = new BarData(xVals, barDataSet);
		return mData;

	}
	private void showChart(BarChart mchart, BarData mdata) {		
		//mchart.setUsePercentValues(false);
		mchart.setScaleEnabled(false); // 不放大
		mchart.getLegend().setPosition(LegendPosition.ABOVE_CHART_CENTER);
		mchart.getLegend().setForm(LegendForm.SQUARE);// 设置注解的位置和形状		
		mchart.getLegend().setTextColor(Color.RED);
		mchart.getLegend().setTextSize(12f);
		mchart.getLegend().setEnabled(true);
		mchart.getXAxis().setPosition(XAxisPosition.BOTTOM);// 设置X轴的位置
		mchart.getXAxis().setDrawGridLines(false);// 不显示网格
		mchart.getXAxis().setDrawAxisLine(false);
		mchart.getXAxis().setTextSize(7f);
		mchart.getXAxis().setTextColor(Color.DKGRAY);
		
		mchart.getAxisRight().setEnabled(false);// 右侧不显示Y轴
		mchart.getAxisLeft().setEnabled(false);
		mchart.getAxisLeft().setDrawLabels(false); // 左侧Y坐标不显示数据刻度
		mchart.getAxisLeft().setAxisMinValue(0.0f);// 设置Y轴显示最小值，不然0下面会有空隙
		// mBarChart.getAxisLeft().setAxisMaxValue(2.0f);// 设置Y轴显示最大值
		mchart.getAxisLeft().setDrawGridLines(true);// 不设置Y轴网格

		mchart.setNoDataTextDescription("没有获取到相关故障数据");
		mchart.setDescription("");			
		mchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
				Toast.makeText(getApplicationContext(), "故障总数:" + new DecimalFormat("#").format(e.getVal()),
						Toast.LENGTH_SHORT).show();// 单纯地显示一个Toast
			}

			@Override
			public void onNothingSelected() {
				// TODO Auto-generated method stub

			}
		});
		mchart.setDescriptionColor(Color.BLUE);
		mchart.setDescriptionTextSize(10.f);// 设置描述文字的字体
		mchart.animateXY(300, 300);
		mchart.setData(mdata);// 给Chart填充数据
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
					.findViewById(R.id.horizontalScrollView_FaultMost);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}
}
