package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AreaAeAdapter;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AreaAvgVAdapter;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_FaultMostAdapter;
import com.hewaiming.ALWorkInfo.bean.Ae_Area;
import com.hewaiming.ALWorkInfo.bean.AvgV_Area;
import com.hewaiming.ALWorkInfo.bean.FaultMost;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.view.FooterListView;
import com.hewaiming.ALWorkInfo.view.FooterListView_plus;

import android.R.color;
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

public class AreaAeActivity extends Activity implements HttpGetListener, OnClickListener, OnScrollListener {
	private Spinner spinner_area, spinner_PotNo, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title, tv_Total, tv_FootTitle,tv_FootTitle1,tv_Total1;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private HttpPost_BeginDate_EndDate http_post;
	private String url = ":8000/scgy/android/odbcPhP/GetAreaAE_DayTable.php";
	private String BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();

	private List<Ae_Area> listBean = null;
	private HSView_AreaAeAdapter mAdapter = null;

	private LinearLayout showArea = null;
	private View layout_areaAe;
	private ImageButton isShowingBtn;
	private RelativeLayout mHead;
	private ListView lv_areaAe;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private String ip,mChartTitle="";
	private int port;
	private Context mContext;
	private FooterListView_plus footView;
	private LineChart[] mCharts = new LineChart[1];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_area_ae);
		MyApplication.getInstance().addActivity(this);
		layout_areaAe = findViewById(R.id.Layout_AreaAe);
		GetDataFromIntent();
		mContext = this;
		init_area();
		init_date();	
		init_title();
		init_HSView();
		init_footer();
		/*if (!MyConst.GetDataFromSharePre(mContext, "AreaAvgV_Show")) {
			MyConst.GuideDialog_show(mContext, "AreaAvgV_Show"); // ��һ����ʾ
		}*/
	}

	private void init_footer() {
		footView = new FooterListView_plus(getApplicationContext());// ��ӱ�end
		lv_areaAe.addFooterView(footView);
		tv_Total = (TextView) findViewById(R.id.tv_footTotal);
		tv_FootTitle = (TextView) findViewById(R.id.tv_footTitle);
		tv_Total1 = (TextView) findViewById(R.id.tv_footTotal1);
		tv_FootTitle1 = (TextView) findViewById(R.id.tv_footTitle1);
	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
		url = "http://" + ip + url;

	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_areaAe = (ListView) findViewById(R.id.lv_AreaAe);
		lv_areaAe.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_areaAe.setCacheColorHint(0);
		lv_areaAe.setOnScrollListener(this);
		/*
		 * lv_areaAvgV.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { if (position != listBean.size()) { Intent
		 * intent = new Intent(AreaAvgVActivity.this, PotVLineActivity.class);
		 * Bundle bundle = new Bundle();
		 * bundle.putStringArrayList("date_record", (ArrayList<String>)
		 * dateBean); bundle.putBoolean("Hide_Action", true);
		 * bundle.putString("PotNo",
		 * String.valueOf(listBean.get(position).getPotNo()));
		 * bundle.putString("Begin_Date", BeginDate);
		 * bundle.putString("End_Date", EndDate);
		 * bundle.putSerializable("JXList", (Serializable) JXList);
		 * bundle.putString("ip", ip); bundle.putInt("port", port);
		 * intent.putExtras(bundle); startActivity(intent); // ��ѹ����ͼ }
		 * 
		 * } });
		 */
	}

	private void init_date() {
		spinner_PotNo = (Spinner) findViewById(R.id.spinner_PotNo);
		spinner_PotNo.setVisibility(View.GONE);
		spinner_beginDate = (Spinner) findViewById(R.id.spinner_Begindate);
		spinner_endDate = (Spinner) findViewById(R.id.spinner_Enddate);
		Date_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateBean);
		// ���������б�ķ��
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

		// ��ֹʱ��
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
		tv_title.setText("��ЧӦϵ��");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		isShowingBtn.setOnClickListener(this);
		mCharts[0] = (LineChart) findViewById(R.id.chart_AreaAe);
	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);
		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas_ALL);
		// ���������б�ķ��
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_area.setAdapter(Area_adapter);
		spinner_area.setVisibility(View.VISIBLE);
		spinner_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					areaId = 66;
					mChartTitle="ȫ��";
					break;
				case 1:
					areaId = 61;
					mChartTitle="һ��";
					break;
				case 2:
					areaId = 62;
					mChartTitle="����";
					break;
				case 3:
					areaId = 11;
					mChartTitle="һ��һ��";
					break;
				case 4:
					areaId = 12;
					mChartTitle="һ������";
					break;
				case 5:
					areaId = 13;
					mChartTitle="һ������";
					break;
				case 6:
					areaId = 21;
					mChartTitle="����һ��";
					break;
				case 7:
					areaId = 22;
					mChartTitle="��������";
					break;
				case 8:
					areaId = 23;
					mChartTitle="��������";
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
		case R.id.btn_isSHOW: // ��ʾ������
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
				Toast.makeText(getApplicationContext(), "����ѡ�񲻶ԣ���ֹ����С�ڿ�ʼ����", 1).show();
			} else {
				http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(url, 1,
						Integer.toString(areaId), BeginDate, EndDate, this, this).execute();

				layout_areaAe.setVisibility(View.VISIBLE);
				tv_title.setText(mChartTitle+"ЧӦϵ��");
			}
			break;
		}
	}

	@Override
	public void GetDataUrl(String data) {
		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[ЧӦ]���ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();
			tv_Total.setText("δ��ȡ��ЧӦϵ������ ");
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // ���LISTVIEW ��ǰ������
					mAdapter.onDateChange(listBean);
				}
			}
		} else {
			listBean = new ArrayList<Ae_Area>();
			listBean.clear();
			listBean = JsonToBean_Area_Date.JsonArrayToAreaAeBean(data);
			Double total = 0.0;
			int total_cnt=0;
			float avgAE=0;
			for (Ae_Area tmp : listBean) {
				total = total + tmp.getAeXS(); // ͳ��ЧӦϵ����
				total_cnt=total_cnt+tmp.getAeCnt();// ͳ��ЧӦ������
			}
			mAdapter = new HSView_AreaAeAdapter(this, R.layout.item_hsview_area_ae, listBean, mHead);
			lv_areaAe.setAdapter(mAdapter);
			if (listBean.size() != 0) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				tv_Total1.setTextColor(Color.RED);
				avgAE=(float) (total / listBean.size());
				tv_Total1.setText(decimalFormat.format(avgAE) + "");
				tv_Total.setTextColor(Color.RED);
				tv_Total.setText(total_cnt+"");
			} else {				
				tv_Total.setText("ЧӦϵ����������");
			}
			tv_FootTitle.setTextColor(Color.RED);
			tv_FootTitle.setText("ЧӦ�ܴ���:");
			tv_FootTitle1.setTextColor(Color.RED);
			tv_FootTitle1.setText("ЧӦϵ����ֵ:");
			String chartTitle="ЧӦϵ�����߱�";
			ShowAreaAeChart(listBean,mChartTitle+chartTitle,avgAE);  //��ʾ��ЧӦϵ������ͼ

		}

	}

	private void ShowAreaAeChart(List<Ae_Area> list,String mTitle,float AeAvg) {
		if (list!=null) {
			LineData mLineData = getLineDatafromDayTable(list.size(),1.0, mTitle,AeAvg); // �Ӳ��ձ�ȥЧӦ����
			showChart(mCharts[0], mLineData);
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
			// ������ͷ �� listView�ؼ���touchʱ�������touch���¼��ַ��� ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView_AreaAe);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	private LineData getLineDatafromDayTable(int count,Double range, String Area_Ae,float avg) {
       //count ��ʾ�б�����������  Area_V ͼ����
		List<String> xValues = new ArrayList<String>();
		List<Entry> yValues = new ArrayList<Entry>(); // y�������
		List<Entry> avgValues=new ArrayList<Entry>();  //��ֵ����
		for (int i =0; i<count; i++) {
			xValues.add(listBean.get(i).getDdate().toString());// x����ʾ�����ݣ�����Ĭ��ʹ�������±���ʾ
			float value_Ae = (float) (listBean.get(i).getAeXS() * range);
			yValues.add(new Entry(value_Ae, i)); // y��� ��ЧӦϵ��
			avgValues.add(new Entry(avg, i));   //y��� ЧӦϵ����ֵ
		}

		// y������ݼ���
		LineDataSet mlineDataSet = new LineDataSet(yValues, Area_Ae);/* ��ʾ�ڱ���ͼ�� */
		// mLineDataSet.setFillAlpha(110);
		// mLineDataSet.setFillColor(Color.RED);
		// ��y��ļ��������ò���
		// lineDataSet_SetV.setAxisDependency(AxisDependency.LEFT);
		mlineDataSet.setLineWidth(1.6f); // �߿�
		mlineDataSet.setCircleSize(1f);// ��ʾ��Բ�δ�С
		mlineDataSet.setColor(Color.RED);// ��ʾ��ɫ
		mlineDataSet.setCircleColor(Color.GRAY);// Բ�ε���ɫ
		mlineDataSet.setHighLightColor(Color.GREEN); // �������ߵ���ɫ

		mlineDataSet.setDrawCircles(true);
		mlineDataSet.setDrawCircleHole(true);
		mlineDataSet.setCircleSize(3.6f); // ������Ȧ�Ĵ�С���뾶��
		mlineDataSet.setCircleColor(Color.GREEN); // ������Ȧ����ɫ
		mlineDataSet.setCircleColorHole(Color.RED);// ������Ȧ����Բ���ף�����ɫ
		mlineDataSet.setValueTextSize(8.8f);
		//������ЧӦϵ����ʾ��ʽ
        mlineDataSet.setValueFormatter(new ValueFormatter() {
			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.

				return decimalFormat.format(value);
			}
		});
		
      //ЧӦϵ����ֵ��
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        LineDataSet avgLineDataSet=new LineDataSet(avgValues, "ЧӦϵ����ֵ:"+decimalFormat.format(avg));
        avgLineDataSet.setLineWidth(1.2f);
        avgLineDataSet.setColor(Color.rgb(75, 92, 196));
        avgLineDataSet.setValueTextSize(6.5f);
        avgLineDataSet.setDrawCircles(false);
        avgLineDataSet.setDrawCircleHole(false);
        avgLineDataSet.setDrawValues(false);
        avgLineDataSet.setValueFormatter(new ValueFormatter() {			
			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.
				return decimalFormat.format(value);
			}
		});
		
		List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
		lineDataSets.add(mlineDataSet); // add
		lineDataSets.add(avgLineDataSet);
		// create a data object with the datasets
		LineData lineData = new LineData(xValues, lineDataSets);

		return lineData;
	}
	
	private void showChart(LineChart lineChart, LineData mLineData) {
		lineChart.fitScreen(); //����
		lineChart.setDrawBorders(false); // �Ƿ�������ͼ����ӱ߿�
		//lineChart.setMinimumHeight(400);
		lineChart.setDescription("");// ��������
		// ���û�����ݵ�ʱ�򣬻���ʾ���������listview��emtpyview
		lineChart.setNoDataTextDescription("����ҪΪ����ͼ�ṩ����.");
		lineChart.setDrawGridBackground(false); // �Ƿ���ʾ�����ɫ
//		lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // ���ĵ���ɫ�����������Ǹ���ɫ����һ��͸����
		lineChart.setBackgroundColor(Color.WHITE);

		lineChart.setTouchEnabled(true); // �����Ƿ���Դ���
		lineChart.setDragEnabled(true);// �Ƿ������ק
		lineChart.setScaleEnabled(true);// �Ƿ��������
		
		XAxis xAxis = lineChart.getXAxis();
		YAxis yAxis_left = lineChart.getAxisLeft();
		YAxis yAxis_right = lineChart.getAxisRight();
		
		xAxis.setTextColor(Color.DKGRAY);
		xAxis.setDrawAxisLine(true);
		xAxis.setTextSize(4.6f);
		xAxis.setLabelRotationAngle(6f);
		xAxis.setLabelsToSkip(1);

		yAxis_left.setEnabled(true);
		yAxis_left.setDrawAxisLine(false);
		yAxis_left.setDrawLimitLinesBehindData(true);

		yAxis_right.setEnabled(false);
		yAxis_right.setDrawAxisLine(false);
		// lineChart.getAxisRight().setEnabled(true); // �ұ� ��������
		// lineChart.getAxisLeft().setEnabled(true);
		lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);// ���ú������ڵײ�
		lineChart.getXAxis().setGridColor(Color.TRANSPARENT);// ȥ�����������ߵ���ʾ
		// if disabled, scaling can be done on x- and y-axis separately
		lineChart.setPinchZoom(false);//		

		// get the legend (only possible after setting data)
		Legend mLegend = lineChart.getLegend(); // ���ñ���ͼ��ʾ�������Ǹ�һ��y��value��
		mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
		mLegend.setForm(LegendForm.SQUARE);// ��ʽ
		mLegend.setFormSize(10f);// ����
		mLegend.setTextColor(Color.BLUE);// ��ɫ
		// mLegend.setTypeface(mTf);// ����		

		lineChart.setData(mLineData); // �������� һ��Ҫ����CHART�趨����֮��
		lineChart.animateX(200); // ����ִ�еĶ���,x��
	}
}
