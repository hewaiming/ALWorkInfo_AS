package com.hewaiming.ALWorkInfo.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener_other;
import com.hewaiming.ALWorkInfo.Popup.ActionItem;
import com.hewaiming.ALWorkInfo.Popup.TitlePopup;
import com.hewaiming.ALWorkInfo.Popup.TitlePopup_More;
import com.hewaiming.ALWorkInfo.SlideBottomPanel.SlideBottomPanel;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_RealRecordAdapter;
import com.hewaiming.ALWorkInfo.bean.PotV;
import com.hewaiming.ALWorkInfo.bean.PotV_plus;
import com.hewaiming.ALWorkInfo.bean.RealRecord;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.floatButton.ALWorkInfoApplication;
import com.hewaiming.ALWorkInfo.floatButton.FloatView;
import com.hewaiming.ALWorkInfo.floatButton.FloatingActionButton;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate_other;
import com.hewaiming.ALWorkInfo.view.MyMarkerView;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
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
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PotVLineActivity extends Activity
		implements HttpGetListener, HttpGetListener_other, OnClickListener, OnScrollListener {
	private static final float BaseV = 3635;// X�ֶ�ֵΪ100 // y�᲻��Ƿ
	private static final float BaseV_LESS = 3520; // X�ֶ�ֵΪ50
	private static final float BaseV_MORE = 3745;// X�ֶ�ֵΪ150
	private Spinner spinner_area, spinner_potno, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn, moreBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	private ArrayAdapter<String> PotNo_adapter;
	private HttpPost_BeginDate_EndDate http_post;
	private String potno_url = ":8000/scgy/android/odbcPhP/PotVoltage_plus.php";
	private String RealRec_URL = ":8000/scgy/android/odbcPhP/RealRecordTable_potno_date.php";
	private String PotNo="1101", BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();
	private List<String> PotNoList = null;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private List<PotV_plus> listBean = null;
	private CombinedChart mCombinedChart;
	private ImageButton isShowingBtn;
	private LinearLayout showArea = null;

	private FloatView floatView = null; // ������FLOAT BUTTON
	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowManagerParams = null;
	private FloatingActionButton show_RealRec_btn;
	private ListView lv_realrec;
	private RelativeLayout mHead;
	private HttpPost_BeginDate_EndDate_other http_post_getRealRec;
	private Context mContext;
	private List<RealRecord> listBean_RealRec = null;
	private HSView_RealRecordAdapter realRec_Adapter;
	private com.hewaiming.ALWorkInfo.SlideBottomPanel.SlideBottomPanel sbv;
	private String ip="125.64.59.11";
	private int port=1234;
	private View Layout_select;
	private View Layout_ok;
	private TitlePopup_More titlePopupMore;
	private List<String> xValues = null;
	private float MaxValueLeft = 6000;
	private float MinValueLeft = 2500;
	private float barHight = 50;
	private float barHight_Norm = 300;
	private float MaxActionLeft = 100;
	private float MinActionLeft = 0;
	private CandleStickChart mCandleChart;
	private int[] colors = { Color.RED, Color.rgb(128, 0, 128), Color.GREEN, Color.YELLOW, Color.BLACK,
			Color.rgb(0, 128, 128), Color.rgb(128, 0, 0), Color.BLUE, Color.GRAY, Color.rgb(238, 17, 61),
			Color.YELLOW };
	private String[] labels = { "AEB", "AC", "TAP", "IRF", "AEPB", "RRK", "AEW", "MAN", "T(TMT)", "NB", "ALF" };
    private boolean IsHide=false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_potv_line);
		MyApplication.getInstance().addActivity(this);
		mContext = this;
		GetDataFromIntent();
		init_title();
		init_HSView();	
		if (!(PotNo.equals(""))) {
			hide_Layout(); // ����Ǵ�ѡ��ۺŽ��룬������
			IsHide=true;
			init_GetRemoteData();			
			moreBtn.setVisibility(View.VISIBLE);

		} else {
			init_area(); // �Ӳ�ѹ���߽������
			init_potNo();
			init_date();
			moreBtn.setVisibility(View.GONE);
			PotNo=PotNoList.get(0).toString();
		}
		// ��ʼ�����൯������
		titlePopupMore = new TitlePopup_More(mContext, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, PotNo, ip,
				port, dateBean,IsHide);
		Popup_initData();
		show_RealRec_btn = (FloatingActionButton) findViewById(R.id.floatBtn_show_realRec); // ����������ť
		show_RealRec_btn.setOnClickListener(this);
		show_RealRec_btn.setAlpha(200);
		sbv = (SlideBottomPanel) findViewById(R.id.sbv);// ����������ť
		// createView(); // ����������ť
	}
	

	private void init_GetRemoteData() {
		mCombinedChart.setVisibility(View.VISIBLE);
		http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url, 2, PotNo, BeginDate, EndDate,
				this, this).execute();

	}

	private void hide_Layout() {
		Layout_select = findViewById(R.id.Layout_selection);
		Layout_ok = findViewById(R.id.Layout_OK);
		Layout_select.setVisibility(View.GONE);
		Layout_ok.setVisibility(View.GONE);

	}

	private void GetDataFromIntent() {
		BeginDate = getIntent().getStringExtra("Begin_Date");
		EndDate = getIntent().getStringExtra("End_Date");
		PotNo = getIntent().getStringExtra("PotNo");
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
		potno_url = "http://" + ip + potno_url;
		RealRec_URL = "http://" + ip + RealRec_URL;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// windowManager.removeView(floatView); // �ڳ����˳�(Activity���٣�ʱ������������
	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#ffffff"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_realrec = (ListView) findViewById(R.id.lv_realrec_potno);
		lv_realrec.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_realrec.setCacheColorHint(0);
		lv_realrec.setOnScrollListener(this);

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
				show_RealRec_btn.setVisibility(View.GONE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(getApplicationContext(), "û��ѡ��ۺ�", 1).show();
			}

		});
	}

	private void init_date() {
		spinner_beginDate = (Spinner) findViewById(R.id.spinner_Begindate);
		spinner_endDate = (Spinner) findViewById(R.id.spinner_Enddate);
		Date_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateBean);
		// ���������б�ķ��
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
				show_RealRec_btn.setVisibility(View.GONE);
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
				show_RealRec_btn.setVisibility(View.GONE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void init_title() {

		mCombinedChart = (CombinedChart) findViewById(R.id.chart_PotV);
		mCombinedChart.setVisibility(View.GONE);	
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(PotNo + "��ѹ����ͼ");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);
		moreBtn = (Button) findViewById(R.id.btn_more);
		//moreBtn.setVisibility(View.GONE);
		moreBtn.setOnClickListener(this);
		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		isShowingBtn.setOnClickListener(this);

	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);

		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
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
		spinner_potno.setSelection(0);
		PotNo = PotNoList.get(0).toString();
		PotNo_adapter.notifyDataSetChanged();// ֪ͨ���ݸı�
	}

	@Override
	public void GetDataUrl(String data) {

		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[��ѹ]���ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // ���LISTVIEW ��ǰ������
				}
			}
		} else {
			listBean = new ArrayList<PotV_plus>();
			listBean.clear();
			listBean = JsonToBean_Area_Date.JsonArrayToPotV_plusBean(data);
			CombinedData mCombinedData = getCombinedData(listBean.size(), 1);
			showChart(mCombinedChart, mCombinedData, Color.rgb(255, 255, 255));
			// NB,AC,RRK�ȶ���ͼ
			// CandleData mCandleData=generateCandleData(listBean.size());
			// showActionChart(mCandleChart,mCandleData,Color.rgb(255, 255,
			// 255));
			show_RealRec_btn.setVisibility(View.VISIBLE);
		}
	}

	private CombinedData getCombinedData(int count, float range) {
		// xValues = new ArrayList<String>();
		if (xValues == null) {
			xValues = new ArrayList<String>();
		} else {
			xValues.clear();
		}
		ArrayList<Entry> yValues = new ArrayList<Entry>(); // y��Ĳ�ѹ��������
		ArrayList<Entry> yValuesTarget = new ArrayList<Entry>(); // y��Ĳ�Ŀ��ѹ����
		ArrayList<Entry> yValuesCur = new ArrayList<Entry>();
		ArrayList<Entry> yValues_MoreLess = new ArrayList<Entry>();
		for (int i = 0; i < count; i++) {
			String mDate = listBean.get(i).getDdate().toString();
			xValues.add(mDate);// x����ʾ�����ݣ�����Ĭ��ʹ�������±���ʾ
			float value = (float) (listBean.get(i).getPotV() * range);
			float valueTarget = (float) (listBean.get(i).getTargetV() * range);
			float temp = (float) (listBean.get(i).getCur() * range);
			yValues.add(new Entry(value, i)); // y��Ĳ�ѹ����
			yValuesTarget.add(new Entry(valueTarget * range, i)); // y��Ĳ�Ŀ��ѹ����
			yValuesCur.add(new Entry(temp, i));// y���ϵ�е���
			int More_Less = listBean.get(i).getMoreLess();
			if (More_Less == 100) {
				yValues_MoreLess.add(new Entry(BaseV * range, i)); // y�᲻��Ƿ
			} else if (More_Less == 50) {
				yValues_MoreLess.add(new Entry(BaseV_LESS * range, i)); // y��Ƿ�ӹ�
			} else if (More_Less == 150) {
				yValues_MoreLess.add(new Entry(BaseV_MORE * range, i)); // y����ӹ�
			}

		}
		LineData lineData = generateMultiLineData(generateLineDataSet(yValues, Color.GREEN, PotNo + "�۵�ѹ: mV ", "LEFT"),
				generateLineDataSet(yValuesTarget, Color.RED, "", "LEFT_Target"),
				generateLineDataSet(yValues_MoreLess, Color.DKGRAY, "", "LEFT_Base"),
				generateLineDataSet(yValuesCur, Color.RED, "ϵ�е���: 100A", "RIGHT"));

		CombinedData combinedData = new CombinedData(xValues);
		combinedData.setData(lineData);// ���� ��ѹ���ߺ�ϵ�е�������ͼ

		ScatterData scatterData = generateScatterData(count);
		combinedData.setData(scatterData); // ����������������TMTͼ

		// CandleData candleData = generateCandleData(count);
		// combinedData.setData(candleData); // �ӹ���AC,RKK��
		/*
		 * combinedChart.setData(combinedData);// ��ǰ��Ļ����ʾ���е�����
		 * combinedChart.invalidate();
		 */

		return combinedData;
	}

	private ScatterData generateScatterData(int count) {
		ArrayList<Entry> UP_yVals = new ArrayList<>();
		ArrayList<Entry> DOWN_yVals = new ArrayList<>();
		ArrayList<Entry> TMT_yVals = new ArrayList<>();
		ArrayList<Entry> AC_yVals = new ArrayList<>();
		ArrayList<Entry> TAP_yVals = new ArrayList<>();
		ArrayList<Entry> AEPB_yVals = new ArrayList<>();
		ArrayList<Entry> RRK_yVals = new ArrayList<>();
		ArrayList<Entry> MAN_yVals = new ArrayList<>();
		ArrayList<Entry> NB_yVals = new ArrayList<>();
		ArrayList<Entry> ALF_yVals = new ArrayList<>();
		ArrayList<Entry> AEB_yVals = new ArrayList<>();
		ArrayList<Entry> AEW_yVals = new ArrayList<>();
		ArrayList<Entry> IRF_yVals = new ArrayList<>();
		ArrayList<Entry> BASE_yVals = new ArrayList<>();
		ArrayList<Entry> IntervalHight_yVals = new ArrayList<>();
		ArrayList<Entry> IntervalLow_yVals = new ArrayList<>();
		int valueBefore;
		for (int i = 0; i < count; i++) {// �������Դ
			if (i > 0) {
				valueBefore = listBean.get(i - 1).getAction();
			} else {
				valueBefore = listBean.get(0).getAction();
			}
			int value = listBean.get(i).getAction();
			float postion = listBean.get(i).getPotV();

			if ((value & 0x02) == 0x02) {
				AEB_yVals.add(new Entry(MinValueLeft + 460, i));
			}

			if ((value & 0x04) == 0x04) {
				if ((valueBefore & 0x04) != 0x04) {
					UP_yVals.add(new Entry(postion + 10, i));
				}
			}
			if ((value & 0x08) == 0x08) {
				if ((valueBefore & 0x08) != 0x08) {
					DOWN_yVals.add(new Entry(postion + 10, i));
				}
			}
			if ((value & 0x200) == 0x200) {
				if ((valueBefore & 0x200) != 0x200) {
					TMT_yVals.add(new Entry(postion + 10, i));
				}
			}
			if ((value & 0x10) == 0x10) {
				AC_yVals.add(new Entry(MinValueLeft + 460, i));
			}
			if ((value & 0x20) == 0x20) {
				TAP_yVals.add(new Entry(MinValueLeft + 460, i));
			}
			if ((value & 0x40) == 0x40) {
				IRF_yVals.add(new Entry(MinValueLeft + 460, i));
			}
			if ((value & 0x100) == 0x100) {
				if ((valueBefore & 0x100) == 0x100) {
					AEPB_yVals.add(new Entry(MinValueLeft + 460, i));
				}
			}
			if ((value & 0x400) == 0x400) {
				RRK_yVals.add(new Entry(MinValueLeft + 460, i));
			}
			if ((value & 0x1000) == 0x1000) {
				MAN_yVals.add(new Entry(MinValueLeft + 420, i));
			}
			if ((value & 0x2000) == 0x2000) {
				AEW_yVals.add(new Entry(MinValueLeft + 460, i));
			}

			if ((value & 0x01) == 0x01) {
				NB_yVals.add(new Entry(MinValueLeft + 420, i));
			}
			if ((value & 0x80) == 0x80) {
				ALF_yVals.add(new Entry(MinValueLeft + 420, i));
			}

			if ((i % 26) == 0) {
				BASE_yVals.add(new Entry(BaseV, i));
			}

			float targetV = listBean.get(i).getTargetV();
			if ((i % 30) == 0) {
				IntervalHight_yVals.add(new Entry(targetV + 50, i));
			}
			if ((i % 30) == 0) {
				IntervalLow_yVals.add(new Entry(targetV - 40, i));
			}

		}

		ScatterDataSet Set_UP = new ScatterDataSet(UP_yVals, "��");
		Set_UP.setColor(Color.rgb(153, 153, 0));
		Set_UP.setScatterShapeSize(2f);
		Set_UP.setDrawValues(true);
		Set_UP.setValueTextColor(Color.RED);
		Set_UP.setValueTextSize(14f);
		Set_UP.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				if (entry != null) {
					return "��";
				} else {
					return null;
				}
				// return "��";
			}
		});

		ScatterDataSet Set_DOWN = new ScatterDataSet(DOWN_yVals, "��");
		Set_DOWN.setColor(Color.rgb(102, 153, 255));
		Set_DOWN.setScatterShapeSize(2f);
		Set_DOWN.setHighlightEnabled(true);
		Set_DOWN.setDrawValues(true);
		Set_DOWN.setValueTextColor(Color.BLACK);
		Set_DOWN.setValueTextSize(14f);
		Set_DOWN.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				if (entry != null) {
					return "��";
				} else {
					return null;
				}
				// return "��";
			}
		});

		ScatterDataSet Set_TMT = new ScatterDataSet(TMT_yVals, "TMT");
		Set_TMT.setColor(Color.GRAY, 100);
		Set_TMT.setScatterShapeSize(6f);
		Set_TMT.setDrawValues(true);
		Set_TMT.setValueTextColor(Color.BLACK);
		Set_TMT.setValueTextSize(14f);

		Set_TMT.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				if (entry != null) {
					return "T";
				} else {
					return null;
				}
			}
		});

		ScatterDataSet Set_AC = new ScatterDataSet(AC_yVals, "AC");
		Set_AC.setScatterShape(ScatterShape.SQUARE);
		Set_AC.setColor(Color.rgb(128, 0, 128));
		Set_AC.setScatterShapeSize(3f);
		Set_AC.setDrawValues(false);
		Set_AC.setValueTextColor(Color.rgb(128, 0, 128));
		Set_AC.setValueTextSize(6f);
		Set_AC.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				if (entry != null) {
					return "AC";
				} else {
					return null;
				}

			}
		});

		ScatterDataSet Set_TAP = new ScatterDataSet(TAP_yVals, "TAP");
		Set_TAP.setColor(Color.GREEN);
		Set_TAP.setScatterShape(ScatterShape.SQUARE);
		Set_TAP.setScatterShapeSize(3f);
		Set_TAP.setDrawValues(false);
		Set_TAP.setValueTextColor(Color.GREEN);
		Set_TAP.setValueTextSize(6f);

		ScatterDataSet Set_AEPB = new ScatterDataSet(AEPB_yVals, "AEPB");
		Set_AEPB.setColor(Color.BLACK);
		Set_AEPB.setScatterShape(ScatterShape.SQUARE);
		Set_AEPB.setScatterShapeSize(3f);
		Set_AEPB.setDrawValues(false);
		Set_AEPB.setValueTextColor(Color.BLACK);
		Set_AEPB.setValueTextSize(6f);

		ScatterDataSet Set_MAN = new ScatterDataSet(MAN_yVals, "MAN");
		Set_MAN.setColor(Color.BLUE);
		Set_MAN.setScatterShape(ScatterShape.SQUARE);
		Set_MAN.setScatterShapeSize(3f);
		Set_MAN.setDrawValues(false);
		Set_MAN.setValueTextColor(Color.BLUE);
		Set_MAN.setValueTextSize(6f);

		ScatterDataSet Set_RRK = new ScatterDataSet(RRK_yVals, "RRK");
		Set_RRK.setColor(Color.rgb(0, 128, 128));
		Set_RRK.setScatterShape(ScatterShape.SQUARE);
		Set_RRK.setScatterShapeSize(3f);
		Set_RRK.setDrawValues(false);
		Set_RRK.setValueTextColor(Color.rgb(0, 128, 128));
		Set_RRK.setValueTextSize(6f);

		ScatterDataSet Set_AEB = new ScatterDataSet(AEB_yVals, "AEB");
		Set_AEB.setScatterShape(ScatterShape.SQUARE);
		Set_AEB.setColor(Color.RED);
		Set_AEB.setScatterShapeSize(3f);
		Set_AEB.setDrawValues(false);
		Set_AEB.setValueTextColor(Color.RED);
		Set_AEB.setValueTextSize(6f);

		ScatterDataSet Set_AEW = new ScatterDataSet(AEW_yVals, "AEW");
		Set_AEW.setColor(Color.rgb(128, 0, 0));
		Set_AEW.setScatterShapeSize(3f);
		Set_AEW.setDrawValues(false);
		Set_AEW.setValueTextColor(Color.rgb(128, 0, 0));
		Set_AEW.setValueTextSize(6f);

		ScatterDataSet Set_IRF = new ScatterDataSet(IRF_yVals, "IRF");
		Set_IRF.setColor(Color.YELLOW);
		Set_IRF.setScatterShapeSize(3f);
		Set_IRF.setDrawValues(false);
		Set_IRF.setValueTextColor(Color.YELLOW);
		Set_IRF.setValueTextSize(6f);

		ScatterDataSet Set_NB = new ScatterDataSet(NB_yVals, "NB");
		Set_NB.setScatterShape(ScatterShape.X);
		Set_NB.setColor(Color.rgb(238, 17, 61));
		Set_NB.setScatterShapeSize(1.2f);
		Set_NB.setDrawValues(false);
		Set_NB.setValueTextColor(Color.rgb(238, 17, 61));
		Set_NB.setValueTextSize(8f);
		Set_NB.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				if (entry != null) {
					return "�O";
				} else {
					return null;
				}
			}
		});

		ScatterDataSet Set_ALF = new ScatterDataSet(ALF_yVals, "ALF");
		Set_ALF.setScatterShape(ScatterShape.X);
		Set_ALF.setColor(Color.YELLOW);
		Set_ALF.setScatterShapeSize(1.2f);
		Set_ALF.setDrawValues(false);
		Set_ALF.setValueTextColor(Color.YELLOW);
		Set_ALF.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				if (entry != null) {
					return "�O";
				} else {
					return null;
				}
			}
		});

		ScatterDataSet Set_BASE = new ScatterDataSet(BASE_yVals, "");
		Set_BASE.setScatterShape(ScatterShape.X);
		Set_BASE.setColor(Color.RED);
		Set_BASE.setScatterShapeSize(0.3f);
		Set_BASE.setDrawValues(false);
		Set_BASE.setValueTextColor(Color.RED);
		// Set_BASE.setValueTextSize(8f);

		ScatterDataSet Set_HIGHT = new ScatterDataSet(IntervalHight_yVals, "");
		Set_HIGHT.setScatterShape(ScatterShape.X);
		Set_HIGHT.setColor(Color.DKGRAY);
		Set_HIGHT.setScatterShapeSize(0.3f);
		Set_HIGHT.setDrawValues(false);
		Set_HIGHT.setValueTextColor(Color.DKGRAY);

		ScatterDataSet Set_LOW = new ScatterDataSet(IntervalLow_yVals, "");
		Set_LOW.setScatterShape(ScatterShape.X);
		Set_LOW.setColor(Color.DKGRAY);
		Set_LOW.setScatterShapeSize(0.3f);
		Set_LOW.setDrawValues(false);
		Set_LOW.setValueTextColor(Color.DKGRAY);

		ArrayList<IScatterDataSet> mScatterData = new ArrayList<IScatterDataSet>();// IScatterData
		// �ӿںܹؼ�������Ӷ������ݵĹؼ��ṹ��LineChartҲ�ǿ��Բ��ö�Ӧ�Ľӿ��࣬Ҳ������Ӷ�������
		mScatterData.add(Set_DOWN);
		mScatterData.add(Set_TMT);
		mScatterData.add(Set_UP);
		mScatterData.add(Set_AC);
		mScatterData.add(Set_TAP);
		mScatterData.add(Set_AEPB);
		mScatterData.add(Set_MAN);
		mScatterData.add(Set_NB);
		mScatterData.add(Set_ALF);
		mScatterData.add(Set_RRK);
		mScatterData.add(Set_AEB);
		mScatterData.add(Set_AEW);
		mScatterData.add(Set_IRF);
		mScatterData.add(Set_BASE);
		mScatterData.add(Set_HIGHT);
		mScatterData.add(Set_LOW);
		ScatterData multi_scatterData = new ScatterData(xValues, mScatterData);
		multi_scatterData.setHighlightEnabled(true);

		return multi_scatterData;
	}

	private CandleData generateCandleData(int count) {
		ArrayList<String> xVals = new ArrayList<String>();
		ArrayList<CandleEntry> NB_yVals = new ArrayList<CandleEntry>();// Y�᷽���һ������
		ArrayList<CandleEntry> AEB_yVals = new ArrayList<CandleEntry>();// Y�᷽��ڶ�������
		ArrayList<CandleEntry> MAN_yVals = new ArrayList<CandleEntry>();// Y�᷽�����������
		ArrayList<CandleEntry> AC_yVals = new ArrayList<CandleEntry>();
		ArrayList<CandleEntry> TAP_yVals = new ArrayList<CandleEntry>();
		ArrayList<CandleEntry> IRF_yVals = new ArrayList<CandleEntry>();
		ArrayList<CandleEntry> ALF_yVals = new ArrayList<CandleEntry>();
		ArrayList<CandleEntry> AEPB_yVals = new ArrayList<CandleEntry>();
		ArrayList<CandleEntry> RRK_yVals = new ArrayList<CandleEntry>();
		ArrayList<CandleEntry> AEW_yVals = new ArrayList<CandleEntry>();
		ArrayList<CandleEntry> No_yVals = new ArrayList<CandleEntry>();
		int valueBefore;
		for (int i = 0; i < count; i++) {// �������Դ
			String mDate = listBean.get(i).getDdate().toString();
			xVals.add(mDate);// x����ʾ�����ݣ�����Ĭ��ʹ�������±���ʾ
			if (i > 0) {
				valueBefore = listBean.get(i - 1).getAction();
			} else {
				valueBefore = listBean.get(0).getAction();
			}
			int value = listBean.get(i).getAction();
			// float postion = listBean.get(i).getPotV();
			if ((value & 0x01) == 0x01) {
				NB_yVals.add(
						new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft, MinActionLeft + 50));
			}
			if ((value & 0x80) == 0x80) {
				ALF_yVals.add(
						new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft, MinActionLeft + 50));
			}
			if ((value & 0x02) == 0x02) {
				AEB_yVals.add(
						new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft, MinActionLeft + 50));
			}
			if ((value & 0x10) == 0x10) {
				AC_yVals.add(new CandleEntry(i, 10, 10, 10, 50));
			}
			if ((value & 0x20) == 0x20) {
				TAP_yVals.add(
						new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft, MinActionLeft + 50));
			}
			if ((value & 0x40) == 0x40) {
				IRF_yVals.add(
						new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft, MinActionLeft + 50));
			}
			if ((value & 0x100) == 0x100) {
				if ((valueBefore & 0x100) == 0x100) {
					AEPB_yVals.add(new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft,
							MinActionLeft + 50));
				}
			}
			if ((value & 0x400) == 0x400) {
				RRK_yVals.add(
						new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft, MinActionLeft + 50));
			}
			if ((value & 0x2000) == 0x2000) {
				AEW_yVals.add(
						new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft, MinActionLeft + 50));
			}
			if ((value & 0x1000) == 0x1000) {
				MAN_yVals.add(
						new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft, MinActionLeft + 50));
			}
			if (value == 0) {
				No_yVals.add(
						new CandleEntry(i, MinActionLeft + 50, MinActionLeft + 0, MinActionLeft, MinActionLeft + 50));
			}

		}

		CandleDataSet Set_NB = new CandleDataSet(NB_yVals, "NB");
		Set_NB.setAxisDependency(AxisDependency.LEFT);
		Set_NB.setColor(Color.rgb(238, 17, 61));// ���õ�һ��������ɫ
		Set_NB.setDrawValues(false); // ����ʾ��ֵ
		Set_NB.setShadowWidth(1f);

		CandleDataSet Set_ALF = new CandleDataSet(ALF_yVals, "ALF");
		Set_ALF.setAxisDependency(AxisDependency.LEFT);
		Set_ALF.setColor(Color.YELLOW);// ���õ�һ��������ɫ
		Set_ALF.setShadowWidth(1f);
		Set_ALF.setDrawValues(false); // ����ʾ��ֵ

		CandleDataSet Set_AEB = new CandleDataSet(AEB_yVals, "AEB");
		Set_AEB.setAxisDependency(AxisDependency.LEFT);

		Set_AEB.setColor(Color.RED);// ���õڶ���������ɫ
		Set_AEB.setShadowWidth(2f);
		Set_AEB.setDrawValues(false); // ����ʾ��ֵ

		CandleDataSet Set_AEW = new CandleDataSet(AEW_yVals, "AEW");
		Set_AEW.setAxisDependency(AxisDependency.LEFT);
		Set_AEW.setColor(Color.rgb(128, 0, 0));
		Set_AEW.setShadowWidth(2f);
		Set_AEW.setDrawValues(false); // ����ʾ��ֵ

		CandleDataSet Set_AEPB = new CandleDataSet(AEPB_yVals, "AEPB");
		Set_AEPB.setAxisDependency(AxisDependency.LEFT);
		Set_AEPB.setColor(Color.BLACK);
		Set_AEPB.setShadowWidth(2f);
		Set_AEPB.setDrawValues(false); // ����ʾ��ֵ

		CandleDataSet Set_MAN = new CandleDataSet(MAN_yVals, "MAN");
		Set_MAN.setAxisDependency(AxisDependency.LEFT);
		Set_MAN.setColor(Color.BLUE);// ���õ�����������ɫ
		Set_MAN.setDrawValues(false); // ����ʾ��ֵ
		Set_MAN.setShadowWidth(2f);

		CandleDataSet Set_AC = new CandleDataSet(AC_yVals, "AC");
		Set_AC.setAxisDependency(AxisDependency.LEFT);
		// Set_AC.setColor(Color.rgb(128, 0, 128));// ���õڶ���������ɫ
		Set_AC.setColor(Color.BLUE);// ���õڶ���������ɫ
		// Set_AC.setDrawValues(true); // ����ʾ��ֵ
		Set_AC.setShadowWidth(50f);

		CandleDataSet Set_TAP = new CandleDataSet(TAP_yVals, "TAP");
		Set_TAP.setAxisDependency(AxisDependency.LEFT);
		Set_TAP.setColor(Color.GREEN);// ���õڶ���������ɫ
		Set_TAP.setShadowWidth(2f);
		Set_TAP.setDrawValues(false); // ����ʾ��ֵ

		CandleDataSet Set_RRK = new CandleDataSet(RRK_yVals, "RRK");
		Set_RRK.setAxisDependency(AxisDependency.LEFT);
		Set_RRK.setColor(Color.rgb(0, 128, 128));// ���õڶ���������ɫ
		Set_RRK.setShadowWidth(2f);
		Set_RRK.setDrawValues(false); // ����ʾ��ֵ

		CandleDataSet Set_IRF = new CandleDataSet(IRF_yVals, "IRF");
		Set_IRF.setAxisDependency(AxisDependency.LEFT);
		Set_IRF.setColor(Color.YELLOW);// ���õڶ���������ɫ
		Set_IRF.setShadowWidth(2f);
		Set_IRF.setDrawValues(false); // ����ʾ��ֵ

		CandleDataSet Set_NO = new CandleDataSet(No_yVals, "NO");
		Set_NO.setAxisDependency(AxisDependency.LEFT);
		Set_NO.setColor(Color.WHITE);// ���õڶ���������ɫ
		Set_NO.setShadowWidth(0.7f);
		Set_NO.setDrawValues(false); // ����ʾ��ֵ

		ArrayList<ICandleDataSet> mData = new ArrayList<ICandleDataSet>();// ICandleDataSet
		// �ӿںܹؼ�������Ӷ������ݵĹؼ��ṹ��LineChartҲ�ǿ��Բ��ö�Ӧ�Ľӿ��࣬Ҳ������Ӷ�������
		mData.add(Set_AC);
		mData.add(Set_AEB);
		mData.add(Set_AEW);
		mData.add(Set_AEPB);
		mData.add(Set_MAN);
		mData.add(Set_NB);
		mData.add(Set_ALF);
		mData.add(Set_TAP);
		mData.add(Set_IRF);
		mData.add(Set_RRK);
		mData.add(Set_NO);
		CandleData multi_CandleData = new CandleData(xVals, mData);
		return multi_CandleData;
	}

	private LineData generateMultiLineData(LineDataSet... lineDataSets) {
		List<ILineDataSet> dataSets = new ArrayList<>();
		for (int i = 0; i < lineDataSets.length; i++) {
			dataSets.add(lineDataSets[i]);
		}
		LineData data = new LineData(xValues, dataSets);
		return data;
	}

	private LineDataSet generateLineDataSet(ArrayList<Entry> entries, int color, String label, String LEFT_RIGHT) {
		LineDataSet set = new LineDataSet(entries, label);
		if (LEFT_RIGHT.equals("LEFT")) {
			// ��ѹ����
			set.setAxisDependency(AxisDependency.LEFT);
			// set.setDrawCubic(true);// Բ������
			set.setLineWidth(0.7f); // �߿�
			set.setCircleSize(0.5f);// ��ʾ��Բ�δ�С
			set.setColor(Color.BLUE);// ��ʾ��ɫ
			set.setCircleColor(Color.BLUE);// Բ�ε���ɫ
			set.setHighLightColor(Color.BLUE); // �������ߵ���ɫ
			set.setDrawValues(true);
			set.setLabel("��ѹ(mV)");
			set.setAxisDependency(YAxis.AxisDependency.LEFT);

		} else if (LEFT_RIGHT.equals("RIGHT")) {
			// ϵ�е�������
			set.setLineWidth(0.7f); // �߿�
			set.setCircleSize(0.5f);// ��ʾ��Բ�δ�С
			set.setColor(Color.RED);// ��ʾ��ɫ
			set.setDrawValues(false);
			set.setCircleColor(Color.RED);// Բ�ε���ɫ
			set.setHighLightColor(Color.RED); // �������ߵ���ɫ
			set.setAxisDependency(YAxis.AxisDependency.RIGHT);
			set.setLabel("����(100A)");
		} else if (LEFT_RIGHT.equals("LEFT_Target")) {
			// ��Ŀ���ѹ����
			set.setAxisDependency(AxisDependency.LEFT);
			// set.setDrawCubic(true);// Բ������
			set.setLineWidth(0.4f); // �߿�
			set.setCircleSize(0.3f);// ��ʾ��Բ�δ�С
			set.setColor(Color.RED);// ��ʾ��ɫ
			set.setCircleColor(Color.RED);// Բ�ε���ɫ
			set.setHighLightColor(Color.RED); // �������ߵ���ɫ
			set.setDrawValues(false);
			set.setAxisDependency(YAxis.AxisDependency.LEFT);
		} else if (LEFT_RIGHT.equals("LEFT_Base")) {
			// �۹�Ƿ�ӹ���
			set.setAxisDependency(AxisDependency.LEFT);
			set.setDrawCubic(false);// Բ������
			set.setLineWidth(0.4f); // �߿�
			set.setCircleSize(0.2f);// ��ʾ��Բ�δ�С
			set.setColor(Color.DKGRAY);// ��ʾ��ɫ
			set.setCircleColor(Color.DKGRAY);// Բ�ε���ɫ
			set.setDrawValues(false);
			set.setAxisDependency(YAxis.AxisDependency.LEFT);
		}

		return set;
	}

	private void showChart(CombinedChart mChart, CombinedData mData, int color) {
		mChart.setDrawBorders(false); // �Ƿ�������ͼ����ӱ߿�
		mChart.fitScreen();
		mChart.setDescription("");// ��������
		// ���û�����ݵ�ʱ�򣬻���ʾ���������listview��emtpyview
		mChart.setNoDataTextDescription("����ҪΪ����ͼ�ṩ����.");

		mChart.setDrawGridBackground(false); // �Ƿ���ʾ�����ɫ
		mChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // ���ĵ���ɫ�����������Ǹ���ɫ����һ��͸����

		mChart.setTouchEnabled(true); // �����Ƿ���Դ���

		mChart.setDragEnabled(true);// �Ƿ������ק
		mChart.setScaleEnabled(true);// �Ƿ��������
		mChart.setDrawOrder(new CombinedChart.DrawOrder[] { CombinedChart.DrawOrder.CANDLE,
				CombinedChart.DrawOrder.SCATTER, CombinedChart.DrawOrder.LINE });

		mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);// ���ú������ڵײ�
		mChart.getXAxis().setGridColor(Color.TRANSPARENT);// ȥ�����������ߵ���ʾ
		mChart.getXAxis().setTextSize(4f);
		// if disabled, scaling can be done on x- and y-axis separately
		mChart.setPinchZoom(false);//

		mChart.setBackgroundColor(color);// ���ñ���

		MarkerView mv = new MyMarkerView(this, R.layout.content_marker_view);
		mChart.setMarkerView(mv); // �������ͼ�ϵĵ�ʱ���ᵯ��һ��View

		Legend mLegend = mChart.getLegend(); // ���ñ���ͼ��ʾ�������Ǹ�һ��y��value��
		mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
		mLegend.setWordWrapEnabled(true);
		mLegend.setForm(LegendForm.SQUARE);// ��ʽ
		mLegend.setFormSize(10f);// ����
		mLegend.setTextColor(Color.BLACK);// ��ɫ
		mLegend.setCustom(colors, labels);
		// mLegend.setTypeface(mTf);// ����

		// ���Y�� ��ѹ
		YAxis yAxis_potv = mChart.getAxisLeft();
		yAxis_potv.setDrawLabels(true);
		yAxis_potv.setDrawGridLines(false);
		yAxis_potv.setEnabled(true);
		yAxis_potv.setDrawAxisLine(true);
		yAxis_potv.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		yAxis_potv.setTextSize(3f);
		yAxis_potv.setTextColor(Color.BLUE);
		yAxis_potv.setAxisMaxValue(MaxValueLeft);
		yAxis_potv.setAxisMinValue(MinValueLeft);
		yAxis_potv.setValueFormatter(new YAxisValueFormatter() {

			@Override
			public String getFormattedValue(float value, YAxis yAxis) {
				return value / 1000 + "V";
			}
		});

		// yAxis.setLabelRotationAngle(90f);;

		// �ұ�Y�� ϵ�е���
		YAxis yAxis_cur = mChart.getAxisRight();
		yAxis_cur.setDrawLabels(true);
		yAxis_cur.setEnabled(true);
		yAxis_cur.setDrawAxisLine(true);
		yAxis_cur.setDrawGridLines(false);
		yAxis_cur.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
		yAxis_cur.setTextSize(5f);
		yAxis_cur.setAxisMaxValue(2300);
		yAxis_cur.setAxisMinValue(0);
		yAxis_cur.setTextColor(Color.RED);

		mChart.setData(mData); // �������� һ��Ҫ����CHART�趨����֮��
		mChart.invalidate();
		// mChart.animateX(5); // ����ִ�еĶ���,x��
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_isSHOW:
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
				Toast.makeText(getApplicationContext(), "����ѡ�񲻶ԣ���ֹ����С�ڿ�ʼ����", 1).show();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date bdate = df.parse(BeginDate);
					Date edate = df.parse(EndDate);
					long TIME_DAY_MILLISECOND = 86400000;
					Long days = (edate.getTime() - bdate.getTime()) / (TIME_DAY_MILLISECOND);
					if (days >= 3) {
						Toast.makeText(getApplicationContext(), "������̫�󣺽�ֹ����-��ʼ����>2,������ѡ������", 1).show();
					} else {
						moreBtn.setVisibility(View.VISIBLE);
						mCombinedChart.setVisibility(View.VISIBLE);
						tv_title.setText(PotNo + "��ѹ����ͼ");
						// mCandleChart.setVisibility(View.VISIBLE);
						http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(potno_url, 2, PotNo,
								BeginDate, EndDate, this, this).execute();
					}
				} catch (ParseException e) {
					Log.e("PotVLineActivity", "ParseException");
				}
			}
			break;
		case R.id.floatBtn_show_realRec:
			// Toast.makeText(getApplicationContext(), "ok", 1).show();
			sbv.displayPanel();
			http_post_getRealRec = (HttpPost_BeginDate_EndDate_other) new HttpPost_BeginDate_EndDate_other(RealRec_URL,
					2, PotNo, BeginDate, EndDate, this, this).execute();
			break;
		case R.id.btn_more:
			titlePopupMore.show(v);
			break;
		}
	}

	private void Popup_initData() {
		// �������������������
		titlePopupMore.addAction(new ActionItem(mContext, "��������", R.drawable.gongyi_info));
		titlePopupMore.addAction(new ActionItem(mContext, "ʵʱ����", R.drawable.realtime_info));
	}

	@Override
	public void onBackPressed() {
		if (sbv.isPanelShowing()) {
			sbv.hide();
			return;
		} else {
			sbv.displayPanel();
		}
		super.onBackPressed();
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
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	@Override
	public void GetOtherDataUrl(String Data) {
		if (Data.equals("")) {
			Toast.makeText(getApplicationContext(), "û�л�ȡ��[ʵʱ��¼]���ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();
			if (listBean_RealRec != null) {
				if (listBean_RealRec.size() > 0) {
					listBean_RealRec.clear(); // ���LISTVIEW ��ǰ������
					realRec_Adapter.onDateChange(listBean_RealRec);
				}
			}
		} else {
			listBean_RealRec = new ArrayList<RealRecord>();
			listBean_RealRec.clear();
			listBean_RealRec = JsonToBean_Area_Date.JsonArrayToRealRecordBean(Data, JXList);
			realRec_Adapter = new HSView_RealRecordAdapter(mContext, R.layout.item_hsview_real_record, listBean_RealRec,
					mHead);
			lv_realrec.setAdapter(realRec_Adapter);
		}
	}
}
