package com.hewaiming.ALWorkInfo.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.BackHandlerInterface;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.InterFace.LoadAeCntInterface;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener_other;
import com.hewaiming.ALWorkInfo.adapter.MyPageAdapter;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.fragment.BackHandledFragment;
import com.hewaiming.ALWorkInfo.fragment.Fragment_AeCnt;
import com.hewaiming.ALWorkInfo.fragment.Fragment_AeTime;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate_other;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.EventLogTags.Description;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AeMostActivity extends FragmentActivity
		implements BackHandlerInterface, HttpGetListener, HttpGetListener_other, OnClickListener {
	private Spinner spinner_area, spinner_PotNo, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;

	private HttpPost_BeginDate_EndDate http_post;
	private HttpPost_BeginDate_EndDate_other http_post_other;
	private String AeCnt_url = ":8000/scgy/android/odbcPhP/AeCnt_area_date.php";
	private String AeTime_url = ":8000/scgy/android/odbcPhP/AeTime_area_date.php";

	private String BeginDate, EndDate;

	private ArrayList<Fragment> fragments;

	private ViewPager pager;
	private MyPageAdapter adapter;
	private RadioGroup group;
	private RadioButton button0;
	private RadioButton button1;
	private int AE_CNT_TIME = 88;
	// 声明接口
	private LoadAeCntInterface listener_AeCnt = null;
	private HttpGetListener_other listener_AeTime = null;
	private Handler mHandler, mHandler_AeTime;
	private View layout_Ae;
	private ImageButton isShowingBtn;
	private LinearLayout showArea = null;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private List<String> dateBean = new ArrayList<String>();
	private Context mContext;

	private BackHandledFragment selectedFragment;
	private String ip;
	private int port;
	private PieChart mPieChart_AECnt;
	private PieData piedata;
	private int[] Area_AeCnt = { 0, 0, 0, 0, 0, 0 }; // 区效应次数总和

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ae_most);
		MyApplication.getInstance().addActivity(this);
		mContext = this;
		layout_Ae = findViewById(R.id.AeMost);
		GetDataFromIntent();
		init_area();
		init_date();
		init_title();
		init_Tab();
		if (!MyConst.GetDataFromSharePre(mContext, "AeMost_Show")) {
			MyConst.GuideDialog_show(mContext, "AeMost_Show"); // 第一次显示
		}
	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
		AeCnt_url = "http://" + ip + AeCnt_url;
		AeTime_url = "http://" + ip + AeTime_url;
	}

	private void init_Tab() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new Fragment_AeCnt(mContext, dateBean, JXList, ip, port));
		fragments.add(new Fragment_AeTime(JXList, dateBean, ip, port));

		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(fragments.size() - 1);// 缓存页面,显示第一个缓存最后一个
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				Log.v("asdf", "onPageSelected");
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
		button0 = (RadioButton) findViewById(R.id.radio_AeCnt);
		button1 = (RadioButton) findViewById(R.id.radio_AeTime);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_AeCnt:
					pager.setCurrentItem(0);
					break;
				case R.id.radio_AeTime:
					pager.setCurrentItem(1);
					break;
				default:
					break;
				}

			}
		});

	}

	protected void getTabState(int index) {

		button0.setChecked(false);
		button1.setChecked(false);

		switch (index) {
		case 0:
			button0.setChecked(true);
			break;
		case 1:
			button1.setChecked(true);
			break;
		default:
			break;
		}

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
		tv_title.setText("效应槽");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		isShowingBtn.setOnClickListener(this);

		mPieChart_AECnt = (PieChart) findViewById(R.id.AreaAeCnt_chart);

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
					areaId = 66; // 所有厂房
					break;
				case 1:
					areaId = 61; // 一厂房
					break;
				case 2:
					areaId = 62; // 二厂房
					break;
				case 3:
					areaId = 11; // 一厂房一区
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
					areaId = 23; // 二厂房三区
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
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.up_red));
			} else {
				showArea.setVisibility(View.GONE);
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.down_red));
			}
			break;
		case R.id.btn_ok:
			if (EndDate.compareTo(BeginDate) < 0) {
				Toast.makeText(getApplicationContext(), "日期选择不对：截止日期小于开始日期", 1).show();
			} else {
				http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(AeCnt_url, 1,
						Integer.toString(areaId), BeginDate, EndDate, this, this).execute();
				http_post_other = (HttpPost_BeginDate_EndDate_other) new HttpPost_BeginDate_EndDate_other(AeTime_url, 1,
						Integer.toString(areaId), BeginDate, EndDate, this, this).execute();
				layout_Ae.setVisibility(View.VISIBLE);
				if (areaId == 66) {
					mPieChart_AECnt.setVisibility(View.VISIBLE);
				} else {
					mPieChart_AECnt.setVisibility(View.GONE);
				}
			}
			break;
		}
	}

	@Override
	public void GetDataUrl(String data) {
		if (data != null) {
			Message msg = new Message();
			msg.obj = data;
			msg.what = 1;
			mHandler.sendMessage(msg);
			Message msg_date1 = new Message();
			msg_date1.obj = BeginDate;
			msg_date1.what = 2;
			mHandler.sendMessage(msg_date1);
			Message msg_date2 = new Message();
			msg_date2.obj = EndDate;
			msg_date2.what = 3;
			mHandler.sendMessage(msg_date2);
			// 显示各区效应次数饼形图
			List<AeRecord> listAeCnt = new ArrayList<AeRecord>(); // 初始化适配器
			listAeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(data);

			for (int i = 0; i < Area_AeCnt.length; i++) {
				Area_AeCnt[i] = 0;
			}
			//有效应记录
			if (listAeCnt != null) {
				// 分别统计各区效应次数总和
				for (AeRecord tmp : listAeCnt) {
					if (tmp.getPotNo() >= 1101 && tmp.getPotNo() <= 1136) {
						Area_AeCnt[0] = Area_AeCnt[0] + tmp.getWaitTime();
					} else if (tmp.getPotNo() >= 1201 && tmp.getPotNo() <= 1237) {
						Area_AeCnt[1] = Area_AeCnt[1] + tmp.getWaitTime();
					} else if (tmp.getPotNo() >= 1301 && tmp.getPotNo() <= 1337) {
						Area_AeCnt[2] = Area_AeCnt[2] + tmp.getWaitTime();
					} else if (tmp.getPotNo() >= 2101 && tmp.getPotNo() <= 2136) {
						Area_AeCnt[3] = Area_AeCnt[3] + tmp.getWaitTime();
					} else if (tmp.getPotNo() >= 2201 && tmp.getPotNo() <= 2237) {
						Area_AeCnt[4] = Area_AeCnt[4] + tmp.getWaitTime();
					} else if (tmp.getPotNo() >= 2301 && tmp.getPotNo() <= 2337) {
						Area_AeCnt[5] = Area_AeCnt[5] + tmp.getWaitTime();
					}
				}
			}
			piedata = getPieData(Area_AeCnt);
			showChart(mPieChart_AECnt, piedata);
		}

	}

	private void showChart(PieChart piechart, PieData piedata) {
		piechart.setData(piedata);// 给PieChart填充数据
		piechart.setUsePercentValues(false);
		piechart.getLegend().setPosition(LegendPosition.ABOVE_CHART_CENTER);
		piechart.getLegend().setForm(LegendForm.CIRCLE);// 设置注解的位置和形状
		piechart.getLegend().setEnabled(false);	
		piechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
				Toast.makeText(getApplicationContext(), "效应次数:" + new DecimalFormat("#").format(e.getVal()),
						Toast.LENGTH_SHORT).show();// 单纯地显示一个Toast
			}

			@Override
			public void onNothingSelected() {
				// TODO Auto-generated method stub

			}
		});

		piechart.setCenterText("效应次数");// 中间写的文字
		piechart.setCenterTextColor(Color.RED);// 设置中间文字的颜色
		piechart.setCenterTextRadiusPercent(0.5f);// 设置文字显示的角度，180横着，默认是竖着
		piechart.setCenterTextSize(7f);// 设置中心文字的字体大小
		piechart.setCenterTextTypeface(null);// 设置字体
		piechart.setDrawCenterText(true);// 中心字使能开关，false时中间无法显示文字

		piechart.setTransparentCircleAlpha(100);// 透明圈的透明度，分3圈，一个是外面的值，然后是这个，然后就是下面的那个Hole
		piechart.setTransparentCircleColor(Color.RED); // 设置颜色
		piechart.setTransparentCircleRadius(50f);// 设置半径

		piechart.setDrawHoleEnabled(true);// 基本同上
		piechart.setHoleColor(Color.GREEN);
		piechart.setHoleRadius(30f);

		piechart.setDescription("各区效应次数总比图");// 设置描述文字
		piechart.setDescriptionColor(Color.BLUE);
		piechart.setDescriptionTextSize(10.f);// 设置描述文字的字体
		piechart.animateXY(300, 300);

	}

	private PieData getPieData(int[] AeCnt) {
		ArrayList<String> xVals = new ArrayList<String>(); // xVals用来表示区名
		xVals.add("一厂1区");
		xVals.add("一厂2区");
		xVals.add("一厂3区");
		xVals.add("二厂1区");
		xVals.add("二厂2区");
		xVals.add("二厂3区");

		ArrayList<Entry> yValues = new ArrayList<Entry>(); // yVals用来表示封装每个饼块的实际数据

		/** 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38 所以 14代表的百分比就是14% */

		yValues.add(new Entry(AeCnt[0], 0));
		yValues.add(new Entry(AeCnt[1], 1));
		yValues.add(new Entry(AeCnt[2], 2));
		yValues.add(new Entry(AeCnt[3], 3));
		yValues.add(new Entry(AeCnt[4], 4));
		yValues.add(new Entry(AeCnt[5], 5));
		// y轴的集合
		PieDataSet pieDataSet = new PieDataSet(yValues, "各区效应次数总比图");/* 显示在比例图上 */
		pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离
		pieDataSet.setValueTextSize(10f);
		pieDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("#");// 构造方法的字符格式这里如果小数不足2位,会以0补足.

				return decimalFormat.format(value);
			}
		});

		ArrayList<Integer> colors = new ArrayList<Integer>();

		// 饼图颜色
		colors.add(Color.rgb(205, 205, 205));
		colors.add(Color.rgb(114, 188, 223));
		colors.add(Color.rgb(255, 123, 124));
		colors.add(Color.rgb(57, 135, 200));
		colors.add(Color.rgb(255, 215, 0));
		colors.add(Color.rgb(56, 161, 219));
		pieDataSet.setColors(colors);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = 5 * (metrics.densityDpi / 160f);
		pieDataSet.setSelectionShift(px); // 选中态多出的长度

		PieData pieData = new PieData(xVals, pieDataSet);
		return pieData;

	}

	public void setHandler(Handler handler) {
		mHandler = handler;

	}

	@Override
	public void GetOtherDataUrl(String data) {
		if (data != null) {
			Message msg = new Message();
			msg.obj = data;
			msg.what = 2;
			mHandler_AeTime.sendMessage(msg);
			// listener_AeTime.GetAeTimeDataUrl(data);
		}
	}

	public void setHandler_AeTime(Handler mHandler_AeTime2) {
		mHandler_AeTime = mHandler_AeTime2;

	}

	@Override
	public void setSelectedFragment(BackHandledFragment backHandledFragment) {
		this.selectedFragment = backHandledFragment;

	}

	@Override
	public void onBackPressed() {
		if (getFragmentManager().findFragmentByTag("Fragment_AeCnt") != null
				&& getFragmentManager().findFragmentByTag("Fragment_AeCnt").isVisible()) {

		}

		super.onBackPressed();
	}
}
