package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.List;

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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.bean.MeasueTable;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.MyApplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowCraftLineActivity extends Activity implements OnClickListener {

	private Button backBtn;
	private TextView tv_title;
	private String PotNo;
	private String SelDate;
	private List<dayTable> list_daytable = new ArrayList<dayTable>();
	private XAxis xAxis;
	private YAxis yAxis_left, yAxis_right;
	private LineChart[] mCharts = new LineChart[16];
	private String selector;
	private List<MeasueTable> list_measuetable = new ArrayList<MeasueTable>();
	public static final String[] TitleName = { "�趨��ѹ", "������ѹ", "ƽ����ѹ", "����", "��ѹ��ʱ��", "����������", "ЧӦ����", "����������",
			"����ָʾ��", "ʵ�ʳ�����", "������", "�躬��", "���ӱ�", "����¶�", "��ˮƽ", "�����ˮƽ" };

	public static final int[] COLORS = { 0xFF00FF00, 0xFFFF0000, 0xFFFF0000, 0xFF0000FF };
	public static final int[] COLORS_bg = {0x19002800, 0x2d001400};
	

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_craft_line);
		MyApplication.getInstance().addActivity(this);
		PotNo = getIntent().getStringExtra("PotNo");
		SelDate = getIntent().getStringExtra("Begin_End_Date");
		selector = getIntent().getStringExtra("SELITEMS");	
		list_daytable = new ArrayList<dayTable>();
		list_daytable = (List<dayTable>) getIntent().getSerializableExtra("list_daytable");

		if (list_daytable==null) {
			Toast.makeText(getApplicationContext(), "���ձ������д��͹����Ĳ���Ϊ�գ�", 1).show();
		}
		list_measuetable = (List<MeasueTable>) getIntent().getSerializableExtra("list_measuetable");
		if (list_measuetable==null) {
			Toast.makeText(getApplicationContext(), "�Ӳ������ݱ����д��͹����Ĳ���Ϊ�գ�", 1).show();
		}
		init_title();
		mCharts[0] = (LineChart) findViewById(R.id.chart_SetV);
		mCharts[1] = (LineChart) findViewById(R.id.chart_WorkV);
		mCharts[2] = (LineChart) findViewById(R.id.chart_AvgV);
		mCharts[3] = (LineChart) findViewById(R.id.chart_Noise);
		mCharts[4] = (LineChart) findViewById(R.id.chart_DYBTime);
		mCharts[5] = (LineChart) findViewById(R.id.chart_ALF);
		mCharts[6] = (LineChart) findViewById(R.id.chart_AeCnt);
		mCharts[7] = (LineChart) findViewById(R.id.chart_YhlCnt);
		mCharts[8] = (LineChart) findViewById(R.id.chart_ALCntZSL);
		mCharts[9] = (LineChart) findViewById(R.id.chart_ALCnt);
		mCharts[10] = (LineChart) findViewById(R.id.chart_FeCnt);
		mCharts[11] = (LineChart) findViewById(R.id.chart_SiCnt);
		mCharts[12] = (LineChart) findViewById(R.id.chart_FZB);
		mCharts[13] = (LineChart) findViewById(R.id.chart_DJWD);
		mCharts[14] = (LineChart) findViewById(R.id.chart_LSP);
		mCharts[15] = (LineChart) findViewById(R.id.chart_DJZSP);
		ISshow_Chart();

		for (int i = 0; i < mCharts.length; i++) {
			if (i < 9) {
				if (list_daytable!=null) {
					LineData mLineData = getLineDatafromDayTable(i, list_daytable.size(), 1); // �Ӳ��ձ�ȥ����
					showChart(mCharts[i], mLineData,COLORS_bg[i%COLORS_bg.length] );
				}
			} else {
				if (list_measuetable!=null) {
					LineData mLineData = getLineDatafromMeasueTable(i, list_measuetable.size(), 1); // �Ӳ�������ȡ����
					showChart(mCharts[i], mLineData, COLORS_bg[i%COLORS_bg.length]);
				}
			}
		}
	}

	private void ISshow_Chart() {
		for (int i = 0; i < mCharts.length; i++) {
			if (selector.contains(TitleName[i])) {
				mCharts[i].setVisibility(View.VISIBLE);
			} else {
				mCharts[i].setVisibility(View.GONE); // ѡ������ʾ����������
			}
		}

	}

	private void init_title() {
		// mLineChart = (LineChart) findViewById(R.id.chart_Craft);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setTextSize(12);
		tv_title.setText(PotNo + "�۹�������  " + SelDate);
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

	}

	private void showChart(LineChart lineChart, LineData mLineData, int color) {
		lineChart.setDrawBorders(true); // �Ƿ�������ͼ����ӱ߿�
		lineChart.setMinimumHeight(400);
		lineChart.setDescription("");// ��������
		// ���û�����ݵ�ʱ�򣬻���ʾ���������listview��emtpyview
		lineChart.setNoDataTextDescription("����ҪΪ����ͼ�ṩ����.");
		lineChart.setDrawGridBackground(false); // �Ƿ���ʾ�����ɫ
//		lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // ���ĵ���ɫ�����������Ǹ���ɫ����һ��͸����
		lineChart.setBackgroundColor(Color.WHITE);

		lineChart.setTouchEnabled(true); // �����Ƿ���Դ���
		lineChart.setDragEnabled(true);// �Ƿ������ק
		lineChart.setScaleEnabled(true);// �Ƿ��������
		
		xAxis = lineChart.getXAxis();
		yAxis_left = lineChart.getAxisLeft();
		yAxis_right = lineChart.getAxisRight();
		
		xAxis.setTextColor(Color.DKGRAY);
		xAxis.setDrawAxisLine(true);
		xAxis.setTextSize(5f);

		yAxis_left.setEnabled(false);
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
		mLegend.setTextColor(Color.BLACK);// ��ɫ
		// mLegend.setTypeface(mTf);// ����		

		lineChart.setData(mLineData); // �������� һ��Ҫ����CHART�趨����֮��
		lineChart.animateX(200); // ����ִ�еĶ���,x��
	}

	private LineData getLineDatafromDayTable(int id, int count, float range) {

		List<String> xValues = new ArrayList<String>();
		List<Entry> yValues = new ArrayList<Entry>(); // y�������

		for (int i = 0; i < count; i++) {
			xValues.add(list_daytable.get(i).getDdate().toString());// x����ʾ�����ݣ�����Ĭ��ʹ�������±���ʾ
			switch (id) {
			case 0:
				float yvalue_SetV = (float) (list_daytable.get(i).getSetV() * range);
				yValues.add(new Entry(yvalue_SetV, i)); // y��� �趨��ѹ
				break;
			case 1:
				float yvalue_WorkV = (float) (list_daytable.get(i).getWorkV() * range);
				yValues.add(new Entry(yvalue_WorkV, i)); // y��� ������ѹ
				break;
			case 2:
				float value_AvgV = (float) (list_daytable.get(i).getAverageV() * range);
				yValues.add(new Entry(value_AvgV, i)); // y��� ƽ����ѹ
				break;
			case 3:
				float value_Noise = (float) (list_daytable.get(i).getZF() * range);
				yValues.add(new Entry(value_Noise, i)); // y��� ����
				break;
			case 4:
				 float value_DYBTime = (float) (list_daytable.get(i).getDybTime() * range);
				 yValues.add(new Entry(value_DYBTime, i)); // y��� ��ѹ��ʱ��
				break;
			case 5:
				float value_ALF = (float) (list_daytable.get(i).getFhlCnt() * range);
				yValues.add(new Entry(value_ALF, i)); // y��� ����������
				break;
			case 6:
				float value_AeCnt = (float) (list_daytable.get(i).getAeCnt() * range);
				yValues.add(new Entry(value_AeCnt, i)); // y��� ЧӦ����
				break;
			case 7:
				float value_YhlCnt = (float) (list_daytable.get(i).getYhlCnt() * range);
				yValues.add(new Entry(value_YhlCnt, i)); // y��� ��������
				break;

			case 8:
				float value_AlZSL = (float) (list_daytable.get(i).getAlCntZSL() * range);
				yValues.add(new Entry(value_AlZSL, i)); // y��� ����ָʾ��		
			}

		}

		// y������ݼ���
		LineDataSet mlineDataSet = new LineDataSet(yValues, TitleName[id]);/* ��ʾ�ڱ���ͼ�� */
		// mLineDataSet.setFillAlpha(110);
		// mLineDataSet.setFillColor(Color.RED);
		// ��y��ļ��������ò���
		// lineDataSet_SetV.setAxisDependency(AxisDependency.LEFT);
		mlineDataSet.setLineWidth(1.6f); // �߿�
		mlineDataSet.setCircleSize(1f);// ��ʾ��Բ�δ�С
		mlineDataSet.setColor(COLORS[id%COLORS.length]);// ��ʾ��ɫ
		mlineDataSet.setCircleColor(COLORS[id%COLORS.length] );// Բ�ε���ɫ
		mlineDataSet.setHighLightColor(COLORS[id%COLORS.length]); // �������ߵ���ɫ
		
		mlineDataSet.setDrawCircles(true);  
		mlineDataSet.setDrawCircleHole(true);
		mlineDataSet.setCircleSize(3.6f); //������Ȧ�Ĵ�С���뾶��
		mlineDataSet.setCircleColor(Color.GREEN); //������Ȧ����ɫ
		mlineDataSet.setCircleColorHole(Color.RED);//������Ȧ����Բ���ף�����ɫ

		List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
		lineDataSets.add(mlineDataSet); // add 

		// create a data object with the datasets
		LineData lineData = new LineData(xValues, lineDataSets);

		return lineData;
	}

	private LineData getLineDatafromMeasueTable(int id, int count, float range) {

		List<String> xValues = new ArrayList<String>();
		List<Entry> yValues = new ArrayList<Entry>(); // y�������

		for (int i = 0; i < count; i++) {
			xValues.add(list_measuetable.get(i).getDdate().toString());// x����ʾ�����ݣ�����Ĭ��ʹ�������±���ʾ
			switch (id) {	
			case 9:
				if (list_measuetable.get(i).getALCnt().equals("")) {
					//float value_ALCnt = (float) (0 * range);
					//yValues.add(new Entry(value_ALCnt, i)); // y��� �ƻ������� 0
				} else {
					float value_ALCnt = (float) (Integer.parseInt(list_measuetable.get(i).getALCnt()) * range);
					yValues.add(new Entry(value_ALCnt, i)); // y��� �ƻ�������
				}
				break;
			case 10:
				if (list_measuetable.get(i).getFeCnt().equals("")) {
					//float value_FeCnt = (float) (0 * range);
					//yValues.add(new Entry(value_FeCnt, i)); // y��� ������ 0
				} else {
					float value_FeCnt = (float) (Float.parseFloat(list_measuetable.get(i).getFeCnt()) * range);
					yValues.add(new Entry(value_FeCnt, i)); // y��� ������
				}

				break;
			case 11:
				if (list_measuetable.get(i).getSiCnt().equals("")) {
					//float value_SiCnt = (float) (0 * range);
					//yValues.add(new Entry(value_SiCnt, i)); // y��� �躬�� 0
				} else {
					float value_SiCnt = (float) (Float.parseFloat(list_measuetable.get(i).getSiCnt()) * range);
					yValues.add(new Entry(value_SiCnt, i)); // y��� �躬��
				}
				break;

			case 12:
				if (list_measuetable.get(i).getFZB().equals("")) {
					//float value_FZB = (float) (0 * range);
					//yValues.add(new Entry(value_FZB, i)); // y��� ���ӱ�
				} else {
					float value_FZB = (float) (Float.parseFloat(list_measuetable.get(i).getFZB()) * range);
					yValues.add(new Entry(value_FZB, i)); // y��� ���ӱ�
				}
				break;

			case 13:
				if (list_measuetable.get(i).getDJWD().equals("")) {
					//float value_DJWD = (float) (0 * range);
					//yValues.add(new Entry(value_DJWD, i)); // y��� ����¶�
				} else {
					float value_DJWD = (float) (Integer.parseInt(list_measuetable.get(i).getDJWD()) * range);
					yValues.add(new Entry(value_DJWD, i)); // y��� ����¶�
				}

				break;

			case 14:
				if (list_measuetable.get(i).getLSP().equals("")) {
					//float value_LSP = (float) (0 * range);
					//yValues.add(new Entry(value_LSP, i));
				} else {
					float value_LSP = (float) (Integer.parseInt(list_measuetable.get(i).getLSP()) * range);
					yValues.add(new Entry(value_LSP, i)); // y��� ��ˮƽ
				}
				break;
			case 15:
				if (list_measuetable.get(i).getDJZSP().equals("")) {
					//float value_DJZSP = (float) (0 * range);
					//yValues.add(new Entry(value_DJZSP, i));
				} else {
					float value_DJZSP = (float) (Integer.parseInt(list_measuetable.get(i).getDJZSP()) * range);
					yValues.add(new Entry(value_DJZSP, i)); // y��� �����ˮƽ
				}
				break;
			}

		}

		// y������ݼ���
		LineDataSet mlineDataSet = new LineDataSet(yValues, TitleName[id]);/* ��ʾ�ڱ���ͼ�� */
		// mLineDataSet.setFillAlpha(110);
		// mLineDataSet.setFillColor(Color.RED);
		// ��y��ļ��������ò���
		// lineDataSet_SetV.setAxisDependency(AxisDependency.LEFT);
		mlineDataSet.setLineWidth(1.6f); // �߿�
		mlineDataSet.setCircleSize(1f);// ��ʾ��Բ�δ�С
		mlineDataSet.setColor(COLORS[id%COLORS.length] );// ��ʾ��ɫ
		mlineDataSet.setCircleColor(COLORS[id%COLORS.length] );// Բ�ε���ɫ
		mlineDataSet.setHighLightColor(COLORS[id%COLORS.length]); // �������ߵ���ɫ
		
		mlineDataSet.setDrawCircles(true);  
		mlineDataSet.setDrawCircleHole(true);
		mlineDataSet.setCircleSize(3.8f); //������Ȧ�Ĵ�С���뾶��
		mlineDataSet.setCircleColor(Color.BLUE); //������Ȧ����ɫ
		mlineDataSet.setCircleColorHole(Color.RED);//������Ȧ����Բ���ף�����ɫ

		List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
		lineDataSets.add(mlineDataSet); // add 

		// create a data object with the datasets
		LineData lineData = new LineData(xValues, lineDataSets);

		return lineData;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}

}
