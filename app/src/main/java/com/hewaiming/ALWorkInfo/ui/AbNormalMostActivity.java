package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
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
import android.util.DisplayMetrics;
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

public class AbNormalMostActivity extends Activity implements HttpGetListener, OnClickListener, OnScrollListener {
	private Spinner spinner_area, spinner_PotNo, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title, tv_Total;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private HttpPost_BeginDate_EndDate http_post;
	private String AbNormal_url = ":8000/scgy/android/odbcPhP/AbNormalMost_area_date.php";
	private String BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();

	private List<FaultMost> listBean = null;
	private HSView_FaultMostAdapter AbNormalMost_Adapter = null;

	private LinearLayout showArea = null;
	private View layout_AbNormalmost;
	private ImageButton isShowingBtn;
	private RelativeLayout mHead;
	private ListView lv_AbNormalMost;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private String ip;
	private int port;
	private Context mContext;
	private FooterListView footView;
	private BarChart mChart_AbNormal;
	private BarData chartData;
	private int[] Area_AbNormalCnt = { 0, 0, 0, 0, 0, 0 }; // 区异常加工次数总和
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abnormal_most);
		MyApplication.getInstance().addActivity(this);
		layout_AbNormalmost = findViewById(R.id.Layout_FaultMost);
		GetDataFromIntent();
		mContext = this;
		init_area();
		init_date();
		init_HSView();
		init_footer();
		init_title();
		if (!MyConst.GetDataFromSharePre(mContext, "AbNormalMost_Show")) {
			MyConst.GuideDialog_show(mContext, "AbNormalMost_Show"); // 第一次显示
		}
	}

	private void init_footer() {
		footView = new FooterListView(getApplicationContext());// 添加表end
		lv_AbNormalMost.addFooterView(footView);
		tv_Total = (TextView) findViewById(R.id.tv_footTotal);
	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
		AbNormal_url = "http://" + ip + AbNormal_url;

	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_AbNormalMost = (ListView) findViewById(R.id.lv_FaultMost);
		lv_AbNormalMost.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_AbNormalMost.setCacheColorHint(0);
		lv_AbNormalMost.setOnScrollListener(this);
		lv_AbNormalMost.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position != listBean.size()) {
					Intent intent = new Intent(AbNormalMostActivity.this, PotVLineActivity.class);
					Bundle bundle = new Bundle();
					bundle.putStringArrayList("date_record", (ArrayList<String>) dateBean);
					bundle.putBoolean("Hide_Action", true);
					bundle.putString("PotNo", String.valueOf(listBean.get(position).getPotNo()));
					bundle.putString("Begin_Date", EndDate);
					bundle.putString("End_Date", EndDate);
					bundle.putSerializable("JXList", (Serializable) JXList);
					bundle.putString("ip", ip);
					bundle.putInt("port", port);
					intent.putExtras(bundle);
					startActivity(intent); // 槽压曲线图
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
		tv_title.setText("下料异常频发");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		isShowingBtn.setOnClickListener(this);
		mChart_AbNormal = (BarChart) findViewById(R.id.AbNormal_chart);

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
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.up_red_high));
			} else {
				showArea.setVisibility(View.GONE);
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.down_red_high));
			}
			break;
		case R.id.btn_ok:
			if (EndDate.compareTo(BeginDate) < 0) {
				Toast.makeText(getApplicationContext(), "日期选择不对：截止日期小于开始日期", 1).show();
			} else {
				http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(AbNormal_url, 1,
						Integer.toString(areaId), BeginDate, EndDate, this, this).execute();

				layout_AbNormalmost.setVisibility(View.VISIBLE);
				// 选择厂房显示图表
				if (areaId == 66) {
					mChart_AbNormal.setVisibility(View.VISIBLE);
				} else {
					mChart_AbNormal.setVisibility(View.GONE);
				}
			}
			break;
		}
	}

	private BarData getChartData(int[] AbNormalCnt) {
		ArrayList<String> xVals = new ArrayList<String>(); // xVals用来表示区名
		for(int i=0;i<AbNormalCnt.length;i++){
			if(i<3){
				xVals.add("一厂"+(i+1)+"区");	
			}else{
				xVals.add("二厂"+(i-2)+"区");
			}			
		}
		List<BarEntry> yValues = new ArrayList<BarEntry>(); // yVals用来表示封装每个饼块的实际数据

		/** 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38 所以 14代表的百分比就是14% */
		for(int i=0;i<AbNormalCnt.length;i++){
			yValues.add(new BarEntry((float)AbNormalCnt[i], i)); 
		}		
		// y轴的集合
		BarDataSet barDataSet = new BarDataSet(yValues, "各区下料异常总次数对比图");/* 显示在比例图上 */
		barDataSet.setColor(Color.rgb(190, 0, 47));// 设置数据露草色颜色
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
		
		/*barDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离
		barDataSet.setValueTextSize(10f);
		barDataSet.setValueFormatter(new ValueFormatter() {

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
		pieDataSet.setSelectionShift(px); // 选中态多出的长度*/
		BarData mData = new BarData(xVals, barDataSet);
		return mData;

	}
	private void showChart(BarChart mchart, BarData mdata) {		
		//mchart.setUsePercentValues(false);
		mchart.setScaleEnabled(false); // 不放大
		mchart.getLegend().setPosition(LegendPosition.ABOVE_CHART_CENTER);
		mchart.getLegend().setForm(LegendForm.SQUARE);// 设置注解的位置和形状		
		mchart.getLegend().setTextColor(Color.rgb(190, 0, 47));
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

		mchart.setNoDataTextDescription("没有获取到相关下料异常数据");
		mchart.setDescription("");		
		
		mchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
				Toast.makeText(getApplicationContext(), "下料异常总次数:" + new DecimalFormat("#").format(e.getVal()),
						Toast.LENGTH_SHORT).show();// 单纯地显示一个Toast
			}

			@Override
			public void onNothingSelected() {
				// TODO Auto-generated method stub

			}
		});

		/*mchart.setsetCenterText("下料异常总次数");// 中间写的文字
		mchart.setCenterTextColor(Color.RED);// 设置中间文字的颜色
		mchart.setCenterTextRadiusPercent(0.5f);// 设置文字显示的角度，180横着，默认是竖着
		mchart.setCenterTextSize(7f);// 设置中心文字的字体大小
		mchart.setCenterTextTypeface(null);// 设置字体
		mchart.setDrawCenterText(true);// 中心字使能开关，false时中间无法显示文字

		mchart.setTransparentCircleAlpha(100);// 透明圈的透明度，分3圈，一个是外面的值，然后是这个，然后就是下面的那个Hole
		mchart.setTransparentCircleColor(Color.RED); // 设置颜色
		mchart.setTransparentCircleRadius(50f);// 设置半径

		mchart.setDrawHoleEnabled(true);// 基本同上
		mchart.setHoleColor(Color.GREEN);
		mchart.setHoleRadius(30f);*/

		//mchart.setDescription("各区下料异常总次数对比图");// 设置描述文字	
		mchart.setDescriptionColor(Color.BLUE);
		mchart.setDescriptionTextSize(10.f);// 设置描述文字的字体
		mchart.animateXY(300, 300);
		mchart.setData(mdata);// 给Chart填充数据

	}
	
	@Override
	public void GetDataUrl(String data) {
		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "没有获取到[下料异常频发]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			tv_Total.setText("0次");
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // 清除LISTVIEW 以前的内容
					AbNormalMost_Adapter.onDateChange(listBean);
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
			AbNormalMost_Adapter = new HSView_FaultMostAdapter(this, R.layout.item_hsview_faultmost, listBean, mHead);
			lv_AbNormalMost.setAdapter(AbNormalMost_Adapter);
			tv_Total.setText(total + "次");
			//显示各区下料异常图表
			for (int i = 0; i < Area_AbNormalCnt.length; i++) {
				 Area_AbNormalCnt[i] = 0;
			}
			//有下料异常记录
			if (listBean != null) {
				// 分别统计各区下料异常总次数
				for (FaultMost tmp : listBean) {
					if (tmp.getPotNo() >= 1101 && tmp.getPotNo() <= 1136) {
						 Area_AbNormalCnt[0] =  Area_AbNormalCnt[0] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 1201 && tmp.getPotNo() <= 1237) {
						 Area_AbNormalCnt[1] =  Area_AbNormalCnt[1] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 1301 && tmp.getPotNo() <= 1337) {
						 Area_AbNormalCnt[2] =  Area_AbNormalCnt[2] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 2101 && tmp.getPotNo() <= 2136) {
						 Area_AbNormalCnt[3] =  Area_AbNormalCnt[3] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 2201 && tmp.getPotNo() <= 2237) {
						 Area_AbNormalCnt[4] =  Area_AbNormalCnt[4] + tmp.getFaultCnt();
					} else if (tmp.getPotNo() >= 2301 && tmp.getPotNo() <= 2337) {
						 Area_AbNormalCnt[5] =  Area_AbNormalCnt[5] + tmp.getFaultCnt();
					}
				}
			}
			chartData = getChartData( Area_AbNormalCnt);
			showChart(mChart_AbNormal, chartData);
		}

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
