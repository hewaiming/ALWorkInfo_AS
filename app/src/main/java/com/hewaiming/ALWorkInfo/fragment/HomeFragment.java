package com.hewaiming.ALWorkInfo.fragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hewaiming.ALWorkInfo.R;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetJXRecord_Listener;

import com.hewaiming.ALWorkInfo.Popup.ActionItem;
import com.hewaiming.ALWorkInfo.Popup.MyProgressDialog;
import com.hewaiming.ALWorkInfo.Popup.TitlePopup;

import com.hewaiming.ALWorkInfo.Update.UpdateManager;
//import com.hewaiming.ALWorkInfo.banner.SlideShowView;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.bean.AvgV;
import com.hewaiming.ALWorkInfo.bean.DJWD;
import com.hewaiming.ALWorkInfo.bean.HY_item;
import com.hewaiming.ALWorkInfo.bean.PotCtrl;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.config.PermisionUtils;
import com.hewaiming.ALWorkInfo.json.JSONArrayParser;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.json.JsonToBean_GetPublicData;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpGetJXRecord;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpGetDate;
import com.hewaiming.ALWorkInfo.net.HttpPost_JsonArray;
import com.hewaiming.ALWorkInfo.net.NetDetector;
import com.hewaiming.ALWorkInfo.ui.AeMostActivity;
import com.hewaiming.ALWorkInfo.ui.AreaAvgVActivity;
import com.hewaiming.ALWorkInfo.ui.MainActivity;
import com.hewaiming.ALWorkInfo.ui.SettingActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class HomeFragment extends Fragment implements OnClickListener, HttpGetJXRecord_Listener, HttpGetDate_Listener {

	private String ip;
	private int port;
	private static final String YHLND_VALUE = "1.5~3.2", DJWD_VALUE = "930~940", FZB_VALUE = "2.4~2.55",
			AVGV_VALUE = "��4.02", AE_VALUE = "��0.25";
	private static final String YHLND_TITLE = "������Ũ��(%) ", DJWD_TITLE = "����¶�(��C) ", FZB_TITLE = "���ӱ�(%) ",
			AVGV_TITLE = "ƽ����ѹ(V) ", AE_TITLE = "ЧӦϵ�� ";
	protected static final String TAG = "HomeFragment Error";
	private SharedPreferences sp;
	// private GridView gridView;
	private Button btnMore;
	private List<String> dateRecord = null; // ��¼����
	private List<String> dateTable = null; // ��������
	private List<Map<String, Object>> JXList = null; // ��¼����

	private String getDateTableUrl = ":8000/scgy/android/odbcPhP/getDate.php";
	private String getJXNameUrl = ":8000/scgy/android/odbcPhP/getJXRecordName.php";

	private Context mContext;
	private TitlePopup titlePopup;
	private TextView tvTitle, tvAeTitle, tvAvgVTitle, tvDJWDTitle, tvFZBTitle, tvYHLNDTitle;
	private ImageView ivWifi, ivShare;
	// private SlideShowView bannerView;
	private io.github.ylbfdev.slideshowview.SlideShowView mSlideShowView;
	private int[] NormPotS = { 0, 0, 0, 0, 0, 0 }; // ��������������
	private int[] AeCnt = { 0, 0, 0, 0, 0, 0 }; // ����ЧӦ����
	private double[] AvgVSum = { 0, 0, 0, 0, 0, 0 }; // ����ƽ����ѹ�ܺ�
	private int[] DJWDSum = { 0, 0, 0, 0, 0, 0 }; // ��������¶��ܺ�
	private double[] FZBAvg = { 0, 0, 0, 0, 0, 0 }; // �������ӱ�ƽ��ֵ
	private double[] YHLNDAvg = { 0, 0, 0, 0, 0, 0 }; // ����������Ũ��ƽ��ֵ

	private String getAeCntUrl = ":8000/scgy/android/odbcPhP/AeCnt_area_date.php"; // ЧӦ����
	protected String getAvgVAreaUrl = ":8000/scgy/android/odbcPhP/GetAvgV_dayTable.php";
	protected String getDJWDUrl = ":8000/scgy/android/odbcPhP/GetDJWD_MeasueTable.php";
	protected String getFZBUrl = ":8000/scgy/android/odbcPhP/GetFZB_MeasueTable_plus.php"; // ��ȡ���ӱ����ݵ�ַ
	protected String getYHLNDUrl = ":8000/scgy/android/odbcPhP/GetYHLND_MeasueTable_plus.php"; // ��ȡ������Ũ�����ݵ�ַ
	private List<AeRecord> listBeanAeCnt = null; // ЧӦ�����б�
	private List<AvgV> listBeanAvgV = null; // �ձ������б�
	private List<DJWD> listBeanDJWD = null; // ����¶��б�
	private List<HY_item> listBeanFZB = null; // ��ʼ�����ӱ������б�
	private List<HY_item> listBeanYHLND = null; // ������Ũ�������б�

	private String getNormPots1Url = ":8000/scgy/android/odbcPhP/GetNormPots1.php";
	private String getNormPots2Url = ":8000/scgy/android/odbcPhP/GetNormPots2.php";

	private List<PotCtrl> NormPotsList1 = null; // һ������״̬ �б�
	private List<PotCtrl> NormPotsList2 = null; // ��������״̬ �б�

	private BarChart mBarChartAE, mBarChartV, mBarChartDJWD, mBarChartFZB, mBarChartYHLND;
	private Handler handler = new Handler(Looper.getMainLooper());
	private int GetDateCnt = 0, GetJXCnt = 0;
	private View mView;

	private boolean CalcPots1 = false, CalcPots2 = false;
	private TextView tvPotTotal, tvPot1, tvPot11, tvPot12, tvPot13, tvPot2, tvPot21, tvPot22, tvPot23;

	private ImageView ivFreshPots, ivFreshAe, ivFreshAvgV, ivFreshDJWD, ivFreshFZB, ivFreshYHLND;
	private ImageButton imgbtnShowDJWD, imgbtnShowFZB, imgbtnShowYHLND;
	protected MyProgressDialog Homedialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Homedialog.dismiss();
		};
	};
	private RelativeLayout layoutNormal;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_home, container, false);
		Log.i("fragment", "create home fragment view");
		return mView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		init(); // ��ʼ�����ؼ�
		NetDetector netDetector = new NetDetector(mContext, false);
		if (netDetector.isConnectingToInternetNoShow() == 1) {
			ivWifi.setVisibility(View.GONE);
			mSlideShowView.setVisibility(View.VISIBLE);// wifi
		} else {
			ivWifi.setVisibility(View.VISIBLE);
			mSlideShowView.setVisibility(View.GONE);// no wifi
		}
		if (NetStatus() != 0) {
			if (!initdate(mContext)) { // ȡԶ�̷�������ַ�Ͷ˿�
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);// û������Զ�̷�����ip�Ͷ˿�

			} else {
				getDateTableUrl = "http://" + ip + getDateTableUrl;
				getJXNameUrl = "http://" + ip + getJXNameUrl;
				getNormPots1Url = "http://" + ip + getNormPots1Url;
				getNormPots2Url = "http://" + ip + getNormPots2Url;
				getAeCntUrl = "http://" + ip + getAeCntUrl;
				getAvgVAreaUrl = "http://" + ip + getAvgVAreaUrl;
				getDJWDUrl = "http://" + ip + getDJWDUrl;
				getFZBUrl = "http://" + ip + getFZBUrl;
				getYHLNDUrl = "http://" + ip + getYHLNDUrl;
				// init_GetDate(); // ��ȡ����
				// init_GetJXRecord(); // ��ȡ������¼
				GetAllData_ShowChart(true); // ��ȡ���в����ݺ󣬲���ִ�С��ĵ�һ�ߡ��������ݣ�����ʾ5��ͼ��

				List<String> imageUris = new ArrayList<>();
				String IP = "http://" + ip;
				imageUris.add(IP + MyConst.PIC_ADDRESS[0]);
				imageUris.add(IP + MyConst.PIC_ADDRESS[1]);
				imageUris.add(IP + MyConst.PIC_ADDRESS[2]);
				imageUris.add(IP + MyConst.PIC_ADDRESS[3]);
				imageUris.add(IP + MyConst.PIC_ADDRESS[4]);
				// mSlideShowView.setVisibility(View.VISIBLE);// wifi
				/* Ϊ�ؼ�����ͼƬ */
				mSlideShowView.setImageUris(imageUris);
				/* ��ʼ���� Ĭ��4���л� */
				mSlideShowView.startPlay();
				checkUpDate(); // ���汾����
			}

		} else {
			Toast.makeText(mContext, "�����쳣��", Toast.LENGTH_LONG).show();
		}

	}

	// ��ȡ����������Ũ�����ݣ�����ʾͼ��
	private void initDATA_YHLND() {
		listBeanYHLND = new ArrayList<HY_item>(); // ��ʼ�����ӱ�������
		ExecutorService exec_YHLND = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(6, new Runnable() {
			@Override
			public void run() {
				Log.i("������Ũ������", "��ȡ����������Ũ������OK����ʼ��ʾ����ͼ������happyȥ");
				YHLNDSum_Clear();
				ShowBar_YHLND(CalcYHLNDSUM(listBeanYHLND));// ��ʾ����������Ũ����״ͼ
			}
		});

		// ��ȡһ����һ��������Ũ������
		exec_YHLND.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				handler.post(new Runnable() {
					@Override
					public void run() {
						tvYHLNDTitle.setText(YHLND_TITLE + "  �ο�ֵ:" + YHLND_VALUE);
					}
				});

				HY_item room11 = GetRoomAvgYHLND("11", BeginDateValue, EndDateValue);
				if (room11 != null) {
					listBeanYHLND.add(room11);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "һ����һ��������Ũ������CyclicBarrier����");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "һ����һ��������Ũ������CyclicBarrier����");
				}
			}
		});

		// ��ȡһ��������������Ũ������
		exec_YHLND.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());

				HY_item room12 = GetRoomAvgYHLND("12", BeginDateValue, EndDateValue);
				if (room12 != null) {
					listBeanYHLND.add(room12);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {

					Log.e(TAG, "һ��������������Ũ������CyclicBarrier����");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "һ��������������Ũ������CyclicBarrier����");
				}
			}
		});

		// ��ȡһ��������������Ũ������
		exec_YHLND.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				HY_item room13 = GetRoomAvgYHLND("13", BeginDateValue, EndDateValue);
				if (room13 != null) {
					listBeanYHLND.add(room13);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "һ��������������Ũ������CyclicBarrier1����");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "һ��������������Ũ������CyclicBarrier2����");
				}
			}
		});
		// ��ȡ������һ��������Ũ������
		exec_YHLND.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				HY_item room21 = GetRoomAvgYHLND("21", BeginDateValue, EndDateValue);
				if (room21 != null) {
					listBeanYHLND.add(room21);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "������һ��������Ũ������CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "������һ��������Ũ������CyclicBarrier����2");
				}
			}
		});
		// ��ȡ����������������Ũ������
		exec_YHLND.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				HY_item room22 = GetRoomAvgYHLND("22", BeginDateValue, EndDateValue);
				if (room22 != null) {
					listBeanYHLND.add(room22);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "����������������Ũ������CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "����������������Ũ������CyclicBarrier����2");
				}
			}
		});
		// ��ȡ����������������Ũ������
		exec_YHLND.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				HY_item room23 = GetRoomAvgYHLND("23", BeginDateValue, EndDateValue);
				if (room23 != null) {
					listBeanYHLND.add(room23);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "����������������Ũ������CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "����������������Ũ������CyclicBarrier����2");
				}
			}
		});
		exec_YHLND.shutdown();
	}

	protected void ShowBar_YHLND(String[] mDate) {
		// ͼ����������
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y�᷽�򳧷�������Ũ������
		ArrayList<String> xVals = new ArrayList<>();// X������
		double YHLNDTotal1 = 0, YHLNDTotal2 = 0, YHLNDTotal = 0; // һ������������������������Ũ���ܺ�
		int PotS1, PotS2, PotS;
		int Cnt1 = 0, Cnt2 = 0, TotalCnt = 0;
		for (int i = 0; i < 3; i++) {
			if (YHLNDAvg[i] > 0.1 && YHLNDAvg[i] < 5.0) {
				YHLNDTotal1 = YHLNDTotal1 + YHLNDAvg[i];// һ����������Ũ��
				Cnt1++;
			}
		}
		for (int j = 3; j < 6; j++) {
			if (YHLNDAvg[j] > 0.1 && YHLNDAvg[j] < 5.0) {
				YHLNDTotal2 = YHLNDTotal2 + YHLNDAvg[j];// ������������Ũ��
				Cnt2++;
			}
		}
		if (!MyConst.isEqual(YHLNDTotal1, 0.0)) {
			YHLNDTotal = YHLNDTotal + YHLNDTotal1;
			TotalCnt = TotalCnt + Cnt1;
		}
		if (!MyConst.isEqual(YHLNDTotal2, 0.0)) {
			YHLNDTotal = YHLNDTotal + YHLNDTotal2;// ����������Ũ���ܺ�
			TotalCnt = TotalCnt + Cnt2;
		}
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // һ��������������
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // ����������������
		PotS = PotS1 + PotS2; // ��������������
		// �������Դ
		if (NormPotS[0] != 0) {
			xVals.add("һ��1��" + mDate[0]);
			yVals.add(new BarEntry((float) YHLNDAvg[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("һ��2��" + mDate[1]);
			yVals.add(new BarEntry((float) YHLNDAvg[1], 1));
		}else {
			xVals.add("һ��2��" + mDate[1]);
		}
		if (NormPotS[2] != 0) {
			xVals.add("һ��3��" + mDate[2]);
			yVals.add(new BarEntry((float) YHLNDAvg[2], 2));
		}else{
			xVals.add("һ��3��" + mDate[2]);
		}
		if (PotS1 != 0 && Cnt1 != 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) YHLNDTotal1 / Cnt1, 3));
		} else if (Cnt1 == 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) 0, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("����1��" + mDate[3]);
			yVals.add(new BarEntry((float) YHLNDAvg[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("����2��" + mDate[4]);
			yVals.add(new BarEntry((float) YHLNDAvg[4], 5));
		}else{
			xVals.add("����2��" + mDate[4]);
		}
		if (NormPotS[5] != 0) {
			xVals.add("����3��" + mDate[5]);
			yVals.add(new BarEntry((float) YHLNDAvg[5], 6));
		}else{
			xVals.add("����3��" + mDate[5]);
		}

		if (PotS2 != 0 && Cnt2 != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) YHLNDTotal2 / Cnt2, 7));
		} else if (Cnt2 == 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) 0, 7));
		}
		if (PotS != 0 && TotalCnt != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) YHLNDTotal / TotalCnt, 8));
		} else if (TotalCnt == 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) 0, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "��������Ũ��");
		barDataSet.setColor(Color.rgb(56, 161, 219));// ��������¶��ɫ��ɫ
		barDataSet.setDrawValues(true); // ��ʾ��ֵ
		barDataSet.setValueTextSize(11f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.000");// ���췽�����ַ���ʽ�������С������2λ,����0����.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChartYHLND.setData(bardata); // ��������
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChartYHLND.invalidate();
			}
		});

	}

	protected String[] CalcYHLNDSUM(List<HY_item> listBean) {
		String[] YHLNDDate = { "", "", "", "", "", "" };
		if (listBean != null && listBean.size() != 0) {
			HY_item mTable_yhlnd = new HY_item();
			for (int i = 0; i < listBean.size(); i++) {
				mTable_yhlnd = listBean.get(i);
				if (mTable_yhlnd != null) {
					String mPot = mTable_yhlnd.getPotNo();
					String mYHLND = mTable_yhlnd.getFZB();
					int potno = 0;
					double YHLND = 0;
					if (!(mPot == null || mPot.length() <= 0)) {
						potno = Integer.parseInt(mTable_yhlnd.getPotNo()); // �ۺ�
					}
					if (!(mYHLND == null || mYHLND.length() <= 0)) {
						YHLND = Double.parseDouble(mYHLND); // ������Ũ��
					}
					if (potno == 11) {
						YHLNDAvg[0] = YHLND; // һ����һ��������Ũ��ƽ��ֵ
						YHLNDDate[0] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 12) {
						YHLNDAvg[1] = YHLND; // һ��������������Ũ��ƽ��ֵ
						YHLNDDate[1] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 13) {
						YHLNDAvg[2] = YHLND; // һ��������������Ũ��ƽ��ֵ
						YHLNDDate[2] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 21) {
						YHLNDAvg[3] = YHLND; // ������һ��������Ũ��ƽ��ֵ
						YHLNDDate[3] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 22) {
						YHLNDAvg[4] = YHLND; // ����������������Ũ��ƽ��ֵ
						YHLNDDate[4] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 23) {
						YHLNDAvg[5] = YHLND; // ����������������Ũ��ƽ��ֵ
						YHLNDDate[5] = mTable_yhlnd.getDdate().substring(5, 10);
					}

				} else {
					Log.i("������Ũ����:", "�� " + i + " ������Ũ���� ��Ϊ�գ�");
				}
			}
		}
		return YHLNDDate;
	}

	protected void YHLNDSum_Clear() {
		for (int i = 0; i < YHLNDAvg.length; i++) {
			YHLNDAvg[i] = 0;
		}

	}

	// ��ȡ���ӱ����ݣ�����ʾͼ��
	private void initDATA_FZB() {
		listBeanFZB = new ArrayList<HY_item>(); // ��ʼ�����ӱ�������
		ExecutorService exec_FZB = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(6, new Runnable() {
			@Override
			public void run() {
				Log.i("��ȡ�������ӱ�����OK", "��ȡ�������ӱ�����OK����ʼ��ʾ����ͼ������happyȥ");
				FZBSum_Clear();
				ShowBar_FZB(CalcFZBSUM(listBeanFZB));// ��ʾ�������ӱ���״ͼ
			}
		});

		// ��ȡһ����һ�����ӱ�����
		exec_FZB.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd
				// hh:mm");
				final String EndDateValue = sdf.format(dt);

				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				handler.post(new Runnable() {
					@Override
					public void run() {
						tvFZBTitle.setText(FZB_TITLE + "  �ο�ֵ:" + FZB_VALUE);
					}
				});
				// listBean_FZB = new ArrayList<HY_item>(); // ��ʼ�����ӱ�������
				HY_item room11 = GetRoomAvgFzb("11", BeginDateValue, EndDateValue);
				if (room11 != null) {
					listBeanFZB.add(room11);
				}

				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "һ����һ�����ӱ�����CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "һ����һ�����ӱ�����CyclicBarrier����2");
				}
			}
		});
		// ��ȡһ�����������ӱ�����
		exec_FZB.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());

				HY_item room12 = GetRoomAvgFzb("12", BeginDateValue, EndDateValue);
				if (room12 != null) {
					listBeanFZB.add(room12);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "һ�����������ӱ�����CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "һ�����������ӱ�����CyclicBarrier����2");
				}
			}
		});

		// ��ȡһ�����������ӱ�����
		exec_FZB.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				HY_item room13 = GetRoomAvgFzb("13", BeginDateValue, EndDateValue);
				if (room13 != null) {
					listBeanFZB.add(room13);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "һ�����������ӱ�����CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "һ�����������ӱ�����CyclicBarrier����2");
				}
			}
		});
		// ��ȡ������һ�����ӱ�����
		exec_FZB.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				HY_item room21 = GetRoomAvgFzb("21", BeginDateValue, EndDateValue);
				if (room21 != null) {
					listBeanFZB.add(room21);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "������һ�����ӱ�����CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "������һ�����ӱ�����CyclicBarrier����2");
				}
			}
		});
		// ��ȡ�������������ӱ�����
		exec_FZB.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				HY_item room22 = GetRoomAvgFzb("22", BeginDateValue, EndDateValue);
				if (room22 != null) {
					listBeanFZB.add(room22);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "�������������ӱ�����CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "�������������ӱ�����CyclicBarrier����2");
				}
			}
		});
		// ��ȡ�������������ӱ�����
		exec_FZB.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String EndDateValue = sdf.format(dt);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -14);
				final String BeginDateValue = sdf.format(cal.getTime());
				HY_item room23 = GetRoomAvgFzb("23", BeginDateValue, EndDateValue);
				if (room23 != null) {
					listBeanFZB.add(room23);
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "�������������ӱ�����CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "�������������ӱ�����CyclicBarrier����2");
				}
			}
		});
		exec_FZB.shutdown();

	}

	protected HY_item GetRoomAvgFzb(final String areaID, String beginDateValue, String endDateValue) {
		List<NameValuePair> mparams = new ArrayList<NameValuePair>();
		mparams.clear();
		mparams.add(new BasicNameValuePair("BeginDate", beginDateValue));
		mparams.add(new BasicNameValuePair("EndDate", endDateValue));
		mparams.add(new BasicNameValuePair("areaID", areaID));
		JSONArrayParser jsonParser = new JSONArrayParser();
		JSONArray json = jsonParser.makeHttpRequest(getFZBUrl, "POST", mparams);
		if (json != null) {
			Log.i("���������ӱ�" + areaID, "��ȡ�������ӱ�OK������������");
			return JsonToBean_Area_Date.JsonArrayToFZBItem(areaID, json.toString());
		} else {
			Log.i("���������ӱ�" + areaID, "��PHP�����������ݷ��أ�");
			handler.post(new Runnable() {
				@Override
				public void run() {
					// "δ��ȡ���ӱ�,����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
					Toast.makeText(mContext, "δ��ȡ�����ӱ���Ϣ�����ܻ�û�����룡���ţ�" + areaID, Toast.LENGTH_SHORT).show();
				}
			});
			return null;
		}
	}

	protected HY_item GetRoomAvgYHLND(final String areaID, String beginDateValue, String endDateValue) {
		List<NameValuePair> mparams = new ArrayList<NameValuePair>();
		mparams.clear();
		mparams.add(new BasicNameValuePair("BeginDate", beginDateValue));
		mparams.add(new BasicNameValuePair("EndDate", endDateValue));
		mparams.add(new BasicNameValuePair("areaID", areaID));
		JSONArrayParser jsonParser = new JSONArrayParser();
		JSONArray json = jsonParser.makeHttpRequest(getYHLNDUrl, "POST", mparams);
		if (json != null) {
			// Log.d("������������Ũ��", json.toString());// �ӷ���������������
			// System.out.println("��ȡ����������Ũ��OK������������");
			Log.i("������������Ũ��" + areaID, "��ȡ����������Ũ��OK������������");
			return JsonToBean_Area_Date.JsonArrayToYHLNDItem(areaID, json.toString());
		} else {
			Log.i("������������Ũ��" + areaID, "��PHP�����������ݷ��أ�");
			handler.post(new Runnable() {
				@Override
				public void run() {
					// "δ��ȡ������Ũ��,����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
					Toast.makeText(mContext, "δ��ȡ��������Ũ����Ϣ�����ܻ�û�����룡���ţ�" + areaID, Toast.LENGTH_SHORT).show();
				}
			});
			return null;
		}
	}

	protected void ShowBar_FZB(String[] mDate) {
		// ͼ����������
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y�᷽�򳧷����ӱ�
		ArrayList<String> xVals = new ArrayList<>();// X������
		double FZBTotal1 = 0, FZBTotal2 = 0, FZBTotal = 0; // һ���������������������ӱ��ܺ�
		int PotS1, PotS2, PotS;
		int Cnt1 = 0, Cnt2 = 0, TotalCnt = 0;
		for (int i = 0; i < 3; i++) {
			if (FZBAvg[i] > 2.0 && FZBAvg[i] < 4.0) {
				FZBTotal1 = FZBTotal1 + FZBAvg[i];// һ�������ӱ�ƽ��ֵ
				Cnt1++;
			}
		}
		for (int j = 3; j < 6; j++) {
			if (FZBAvg[j] > 2.0 && FZBAvg[j] < 4.0) {
				FZBTotal2 = FZBTotal2 + FZBAvg[j];// ���������ӱ�ƽ��ֵ
				Cnt2++;
			}
		}
		if (!MyConst.isEqual(FZBTotal1, 0.0)) {
			FZBTotal = FZBTotal + FZBTotal1;
			TotalCnt = TotalCnt + Cnt1;
		}
		if (!MyConst.isEqual(FZBTotal2, 0.0)) {
			FZBTotal = FZBTotal + FZBTotal2;// �������ӱ��ܺ�
			TotalCnt = TotalCnt + Cnt2;
		}
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // һ��������������
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // ����������������
		PotS = PotS1 + PotS2; // ��������������
		// �������Դ

		xVals.add("һ��1��" + mDate[0]);
		yVals.add(new BarEntry((float) FZBAvg[0], 0));

		xVals.add("һ��2��" + mDate[1]);
		yVals.add(new BarEntry((float) FZBAvg[1], 1));

		xVals.add("һ��3��" + mDate[2]);
		yVals.add(new BarEntry((float) FZBAvg[2], 2));

		if (PotS1 != 0 && Cnt1 != 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) FZBTotal1 / Cnt1, 3));
		} else if (Cnt1 == 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) 0, 3));
		}
		xVals.add("����1��" + mDate[3]);
		yVals.add(new BarEntry((float) FZBAvg[3], 4));

		xVals.add("����2��" + mDate[4]);
		yVals.add(new BarEntry((float) FZBAvg[4], 5));

		xVals.add("����3��" + mDate[5]);
		yVals.add(new BarEntry((float) FZBAvg[5], 6));

		if (PotS2 != 0 && Cnt2 != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) FZBTotal2 / Cnt2, 7));
		} else if (Cnt2 == 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) 0, 7));
		}
		if (PotS != 0 && TotalCnt != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) FZBTotal / TotalCnt, 8));
		} else if (TotalCnt == 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) 0, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "�����ӱ�");
		barDataSet.setColor(Color.rgb(66, 80, 102));// �������ݴ�����ɫ
		barDataSet.setDrawValues(true); // ��ʾ��ֵ
		barDataSet.setValueTextSize(11f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.000");// ���췽�����ַ���ʽ�������С������2λ,����0����.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChartFZB.setData(bardata); // ��������
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChartFZB.invalidate();
			}
		});

	}

	protected String[] CalcFZBSUM(List<HY_item> listBean) {
		String[] FzbDate = { "", "", "", "", "", "" };
		if (listBean != null && listBean.size() != 0) {
			HY_item mTable = new HY_item();
			for (int i = 0; i < listBean.size(); i++) {
				mTable = listBean.get(i);
				if (mTable != null) {
					String mPot = mTable.getPotNo();
					String mFzb = mTable.getFZB();
					int potno = 0;
					double Fzb = 0;
					if (!(mPot == null || mPot.length() <= 0)) {
						potno = Integer.parseInt(mTable.getPotNo()); // �ۺ�

					}
					if (!(mFzb == null || mFzb.length() <= 0)) {
						Fzb = Double.parseDouble(mFzb); // ���ӱ�

					}
					if (potno == 11) {
						FZBAvg[0] = Fzb; // һ����һ�����ӱ�ƽ��ֵ
						FzbDate[0] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 12) {
						FZBAvg[1] = Fzb; // һ�����������ӱ�ƽ��ֵ
						FzbDate[1] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 13) {
						FZBAvg[2] = Fzb; // һ�����������ӱ�ƽ��ֵ
						FzbDate[2] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 21) {
						FZBAvg[3] = Fzb; // ������һ�����ӱ�ƽ��ֵ
						FzbDate[3] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 22) {
						FZBAvg[4] = Fzb; // �������������ӱ�ƽ��ֵ
						FzbDate[4] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 23) {
						FZBAvg[5] = Fzb; // �������������ӱ�ƽ��ֵ
						FzbDate[5] = mTable.getDdate().substring(5, 10);
					}

				} else {
					Log.i("���ӱ�ƽ��ֵ��", "�� " + i + " ���ӱ�ƽ��ֵ�� ��Ϊ�գ�");
				}
			}
		}
		return FzbDate;

	}

	protected void FZBSum_Clear() {
		for (int i = 0; i < FZBAvg.length; i++) {
			FZBAvg[i] = 0;
		}

	}

	private void initDATA_DJWD() {
		ExecutorService exec_DJWD = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
			@Override
			public void run() {
				Log.i("��������¶�����OK", "��ȡ��������¶�����OK����ʼ��ʾ����ͼ������happyȥ");
				DJWDSum_Clear();
				CalcDJWDSUM(listBeanDJWD);
				ShowBar_DJWD();// ��ʾ��������¶���״ͼ
			}
		});

		// ��ȡ����¶�
		exec_DJWD.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				final String todayValue = sdf.format(dt);
				final String today = sdfLong.format(dt);
				/*
				 * TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				 * Calendar cal = Calendar.getInstance(); cal.add(Calendar.DATE,
				 * -1); SimpleDateFormat sdf = new
				 * SimpleDateFormat("yyyy-MM-dd"); final String yesterdayValue =
				 * sdf.format(cal.getTime());
				 */
				handler.post(new Runnable() {
					@Override
					public void run() {
						tvDJWDTitle.setText(DJWD_TITLE + todayValue + " �ο�ֵ:" + DJWD_VALUE);
					}
				});
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // ȫ���ۺ�
				mparams.add(new BasicNameValuePair("BeginDate", todayValue));
				mparams.add(new BasicNameValuePair("EndDate", todayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(getDJWDUrl, "POST", mparams);
				if (json != null) {
					Log.i("��������¶�", "��ȡ��������¶�OK������������");
					listBeanDJWD = new ArrayList<DJWD>(); // ��ʼ������¶�������
					listBeanDJWD = JsonToBean_Area_Date.JsonArrayToDJWDBean(json.toString());

				} else {
					// �ٴ�get����¶�����
					json = jsonParser.makeHttpRequest(getDJWDUrl, "POST", mparams);
					if (json != null) {
						// Log.d("����������¶�", json.toString());// �ӷ���������������
						listBeanDJWD = new ArrayList<DJWD>(); // ��ʼ������¶�������
						listBeanDJWD = JsonToBean_Area_Date.JsonArrayToDJWDBean(json.toString());
					} else {
						Log.i("����������¶�", "��PHP�����������ݷ��أ�");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// "δ��ȡЧӦ����,����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
								Toast.makeText(mContext, "δ��ȡ������¶���Ϣ�����ܻ�û�����룡", Toast.LENGTH_SHORT).show();

							}
						});
					}
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "��������¶�����CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "��������¶�����CyclicBarrier����2");
				}
			}
		});

		exec_DJWD.shutdown();
	}

	// ��ʾ����¶�ͼ��
	protected void ShowBar_DJWD() {
		// ͼ����������
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y�᷽�򳧷�����¶�
		ArrayList<String> xVals = new ArrayList<>();// X������
		int DJWDTotal1, DJWDTotal2, DJWDTotal; // һ�������������������ܵ���¶Ⱥ�
		int PotS1, PotS2, PotS;
		DJWDTotal1 = DJWDSum[0] + DJWDSum[1] + DJWDSum[2];// һ��������¶��ܺ�
		DJWDTotal2 = DJWDSum[3] + DJWDSum[4] + DJWDSum[5];// ����������¶��ܺ�
		DJWDTotal = DJWDTotal1 + DJWDTotal2;// ����¶��ܺ�
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // һ��������������
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // ����������������
		PotS = PotS1 + PotS2; // ��������������
		// �������Դ
		if (NormPotS[0] != 0) {
			xVals.add("һ��1��");
			yVals.add(new BarEntry((float) DJWDSum[0] / NormPotS[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("һ��2��");
			yVals.add(new BarEntry((float) DJWDSum[1] / NormPotS[1], 1));
		}else{
			xVals.add("һ��2��");
		}
		if (NormPotS[2] != 0) {
			xVals.add("һ��3��");
			yVals.add(new BarEntry((float) DJWDSum[2] / NormPotS[2], 2));
		}else {
			xVals.add("һ��3��");
		}
		if (PotS1 != 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) DJWDTotal1 / PotS1, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("����1��");
			yVals.add(new BarEntry((float) DJWDSum[3] / NormPotS[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("����2��");
			yVals.add(new BarEntry((float) DJWDSum[4] / NormPotS[4], 5));
		}else{
			xVals.add("����2��");
		}
		if (NormPotS[5] != 0) {
			xVals.add("����3��");
			yVals.add(new BarEntry((float) DJWDSum[5] / NormPotS[5], 6));
		}else{
			xVals.add("����3��");
		}
		if (PotS2 != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) DJWDTotal2 / PotS2, 7));
		}
		if (PotS != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) DJWDTotal / PotS, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "����¶�");
		barDataSet.setColor(Color.rgb(255, 215, 0));// ����ǳ�ƺ�ɫ��ɫ
		barDataSet.setDrawValues(true); // ��ʾ��ֵ
		barDataSet.setValueTextSize(10f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.0");// ���췽�����ַ���ʽ�������С������2λ,����0����.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChartDJWD.setData(bardata); // ��������
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChartDJWD.invalidate();
			}
		});

	}

	protected void CalcDJWDSUM(List<DJWD> listBean) {
		if (listBean != null && listBean.size() != 0) {
			DJWD mTable = new DJWD();
			for (int i = 0; i < listBean.size(); i++) {
				mTable = listBean.get(i);
				if (mTable != null) {
					String mPot = mTable.getPotNo();
					String mDjwd = mTable.getDJWD();
					int potno = 0;
					int Djwd = 0;
					if (!(mPot == null || mPot.length() <= 0)) {
						potno = Integer.parseInt(mTable.getPotNo()); // �ۺ�
					}
					if (!(mDjwd == null || mDjwd.length() <= 0)) {
						Djwd = Integer.parseInt(mDjwd); // ����¶�
					}
					if (potno >= 1101 && potno <= 1136) {
						DJWDSum[0] = DJWDSum[0] + Djwd; // һ����һ������¶��ܺ�
					} else if (potno >= 1201 && potno <= 1237) {
						DJWDSum[1] = DJWDSum[1] + Djwd;
					} else if (potno >= 1301 && potno <= 1337) {
						DJWDSum[2] = DJWDSum[2] + Djwd;
					} else if (potno >= 2101 && potno <= 2136) {
						DJWDSum[3] = DJWDSum[3] + Djwd; // ������һ������¶��ܺ�
					} else if (potno >= 2201 && potno <= 2237) {
						DJWDSum[4] = DJWDSum[4] + Djwd;
					} else if (potno >= 2301 && potno <= 2337) {
						DJWDSum[5] = DJWDSum[5] + Djwd;
					}

				} else {
					Log.i("����¶�", "�� " + i + " �� ����¶ȣ�Ϊ�գ�");
				}
			}
		}

	}

	protected void DJWDSum_Clear() {
		for (int i = 0; i < DJWDSum.length; i++) {
			DJWDSum[i] = 0;
		}

	}

	private void initDATA_AvgV() {
		ExecutorService exec_AvgV = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
			@Override
			public void run() {
				Log.i("��ƽ����ѹ����", "��ȡ����ƽ����ѹ����OK����ʼ��ʾ����ͼ������happyȥ");
				AvgVSum_Clear();
				CalcAvgVSUM(listBeanAvgV);
				ShowBar_AvgV();// ��ʾ����ƽ����ѹ��״ͼ
			}
		});

		// ��ȡƽ����ѹ
		exec_AvgV.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final String yesterdayValue = sdf.format(cal.getTime());
				handler.post(new Runnable() {
					@Override
					public void run() {
						tvAvgVTitle.setText(AVGV_TITLE + yesterdayValue + "  �ο�ֵ:" + AVGV_VALUE);
					}
				});
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // ȫ���ۺ�
				mparams.add(new BasicNameValuePair("BeginDate", yesterdayValue));
				mparams.add(new BasicNameValuePair("EndDate", yesterdayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(getAvgVAreaUrl, "POST", mparams);
				if (json != null) {
					Log.i("����ƽ����ѹ", "��ȡ����ƽ����ѹOK������������");
					listBeanAvgV = new ArrayList<AvgV>(); // ��ʼ��ЧӦ����������
					listBeanAvgV = JsonToBean_Area_Date.JsonArrayToAvgVDayTableBean(json.toString());

				} else {
					// �ٴ�getƽ����ѹ����
					json = jsonParser.makeHttpRequest(getAvgVAreaUrl, "POST", mparams);
					if (json != null) {
						Log.i("������ƽ����ѹ", "ok");// �ӷ���������������
						listBeanAvgV = new ArrayList<AvgV>(); // ��ʼ��ЧӦ����������
						listBeanAvgV = JsonToBean_Area_Date.JsonArrayToAvgVDayTableBean(json.toString());
					} else {
						Log.w("������ƽ����ѹ ---", "��PHP�����������ݷ��أ�");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// "δ��ȡЧӦ����,����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
								Toast.makeText(mContext, "δ��ȡ������ƽ����ѹ������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_SHORT).show();

							}
						});
					}
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "����ƽ����ѹ����CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "����ƽ����ѹ����CyclicBarrier����2");
				}
			}
		});

		exec_AvgV.shutdown();
	}

	protected void AvgVSum_Clear() {
		for (int i = 0; i < AvgVSum.length; i++) {
			AvgVSum[i] = 0;
		}

	}

	protected void ShowBar_AvgV() {
		// ͼ����������
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y�᷽�򳧷�ЧӦϵ��
		ArrayList<String> xVals = new ArrayList<>();// X������
		double AvgVTotal1, AvgVTotal2, AvgVTotal; // һ��������������������ƽ����ѹ��
		int PotS1, PotS2, PotS;
		AvgVTotal1 = AvgVSum[0] + AvgVSum[1] + AvgVSum[2];// һ����ƽ����ѹ�ܺ�
		AvgVTotal2 = AvgVSum[3] + AvgVSum[4] + AvgVSum[5];// ������ƽ����ѹ�ܺ�
		AvgVTotal = AvgVTotal1 + AvgVTotal2;// ����ƽ����ѹ�ܺ�
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // һ��������������
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // ����������������
		PotS = PotS1 + PotS2; // ��������������
		// �������Դ
		if (NormPotS[0] != 0) {
			xVals.add("һ��1��");
			yVals.add(new BarEntry((float) AvgVSum[0] / NormPotS[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("һ��2��");
			yVals.add(new BarEntry((float) AvgVSum[1] / NormPotS[1], 1));
		}else{
			xVals.add("һ��2��");//ͣ��
		}

		if (NormPotS[2] != 0) {
			xVals.add("һ��3��");
			yVals.add(new BarEntry((float) AvgVSum[2] / NormPotS[2], 2));
		}else{
			xVals.add("һ��3��");//ͣ��
		}
		if (PotS1 != 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) AvgVTotal1 / PotS1, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("����1��");
			yVals.add(new BarEntry((float) AvgVSum[3] / NormPotS[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("����2��");
			yVals.add(new BarEntry((float) AvgVSum[4] / NormPotS[4], 5));
		}else {
			xVals.add("����2��");//stop
		}
		if (NormPotS[5] != 0) {
			xVals.add("����3��");
			yVals.add(new BarEntry((float) AvgVSum[5] / NormPotS[5], 6));
		}else {
			xVals.add("����3��");//stop
		}
		if (PotS2 != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) AvgVTotal2 / PotS2, 7));
		}
		if (PotS != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) AvgVTotal / PotS, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "��ƽ����ѹ");
		barDataSet.setColor(Color.rgb(75, 92, 196));// �������ݱ�����ɫ
		barDataSet.setDrawValues(true); // ��ʾ��ֵ
		barDataSet.setValueTextSize(11f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.000");// ���췽�����ַ���ʽ�������С������2λ,����0����.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChartV.setData(bardata); // ��������
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChartV.invalidate();
			}
		});

	}

	protected void CalcAvgVSUM(List<AvgV> listBean) {
		if (listBean != null && listBean.size() != 0) {
			AvgV mTable = new AvgV();
			for (int i = 0; i < listBean.size(); i++) {
				mTable = listBean.get(i);
				if (mTable != null) {
					int potno = mTable.getPotNo(); // �ۺ�
					double avgV = mTable.getAverageV();// ƽ����ѹ
					if (potno >= 1101 && potno <= 1136) {
						AvgVSum[0] = AvgVSum[0] + avgV; // һ����һ��ƽ����ѹ�ܺ�
					} else if (potno >= 1201 && potno <= 1237) {
						AvgVSum[1] = AvgVSum[1] + avgV;
					} else if (potno >= 1301 && potno <= 1337) {
						AvgVSum[2] = AvgVSum[2] + avgV;
					} else if (potno >= 2101 && potno <= 2136) {
						AvgVSum[3] = AvgVSum[3] + avgV; // ������һ��ƽ����ѹ�ܺ�
					} else if (potno >= 2201 && potno <= 2237) {
						AvgVSum[4] = AvgVSum[4] + avgV;
					} else if (potno >= 2301 && potno <= 2337) {
						AvgVSum[5] = AvgVSum[5] + avgV;
					}

				} else {
					Log.w("ƽ����ѹΪ��", "�� " + i + " �� ƽ����ѹ��Ϊ�գ�");
				}
			}
		}

	}

	/*
	 * private void init_GetCommData() { // ִ�д�Զ�̻���������� if (date_table == null)
	 * { mhttpgetdata_date = (AsyTask_HttpGetDate) new
	 * AsyTask_HttpGetDate(get_dateTable_url, this, mContext) .execute(); } if
	 * (JXList == null) { mHttpGetData_JXRecord = (AsyTask_HttpGetJXRecord) new
	 * AsyTask_HttpGetJXRecord(get_JXName_url, this, mContext).execute(); //
	 * ִ�д�Զ�̻�ý�����¼���� } }
	 */

	private void SetNormalPot() {
		tvPot11.setText(NormPotS[0] + "");
		tvPot12.setText(NormPotS[1] + "");
		tvPot13.setText(NormPotS[2] + "");
		tvPot1.setText(NormPotS[0] + NormPotS[1] + NormPotS[2] + "");
		tvPot21.setText(NormPotS[3] + "");
		tvPot22.setText(NormPotS[4] + "");
		tvPot23.setText(NormPotS[5] + "");
		tvPot2.setText(NormPotS[3] + NormPotS[4] + NormPotS[5] + "");
		tvPotTotal.setText(NormPotS[0] + NormPotS[1] + NormPotS[2] + NormPotS[3] + NormPotS[4] + NormPotS[5] + "̨");
	}

	private void initDATA_AE() {
		ExecutorService exec = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
			@Override
			public void run() {
				Log.i("ЧӦϵ������", "��ȡ����ЧӦϵ������OK����ʼ��ʾ����ͼ������happyȥ");
				AeCnt_Clear();
				CalcAeCnt(listBeanAeCnt);
				ShowBar_AE();// ��ʾ����ЧӦ��״ͼ
			}
		});
		// ��ȡЧӦ����
		exec.execute(new Runnable() {
			@Override
			public void run() {
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
				String todayValue = sdf.format(dt);
				final String mToday = sdfLong.format(dt);
				handler.post(new Runnable() {
					@Override
					public void run() {
						tvAeTitle.setText(AE_TITLE + mToday + " �ο�ֵ��" + AE_VALUE);
					}
				});
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // ȫ���ۺ�
				mparams.add(new BasicNameValuePair("BeginDate", todayValue));
				mparams.add(new BasicNameValuePair("EndDate", todayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(getAeCntUrl, "POST", mparams);
				if (json != null) {
					Log.i("ЧӦ����", "��ȡ����ЧӦ����OK������������");
					listBeanAeCnt = new ArrayList<AeRecord>(); // ��ʼ��ЧӦ����������
					listBeanAeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(json.toString());

				} else {
					// �ٴ�getЧӦ��������
					json = jsonParser.makeHttpRequest(getAeCntUrl, "POST", mparams);
					if (json != null) {
						Log.i("ЧӦ����", "������ЧӦ����ok");// �ӷ���������������
						listBeanAeCnt = new ArrayList<AeRecord>(); // ��ʼ��ЧӦ����������
						listBeanAeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(json.toString());
					} else {
						Log.i("������ЧӦ���� ---", "��PHP�����������ݷ��أ�");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// tv_title.setTextSize(14);
								// tv_title.setText("����վ:" +
								// "δ��ȡЧӦ����,����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
								Toast.makeText(mContext, "û�л�ȡ������ЧӦ����������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_SHORT).show();

							}
						});
					}
				}
				try {
					barrier.await();// �ȴ���������
				} catch (InterruptedException e) {
					Log.e(TAG, "���� ЧӦ���� ����CyclicBarrier����1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "���� ЧӦ���� ����CyclicBarrier����2");
				}
			}
		});

		exec.shutdown();

	}

	protected void AeCnt_Clear() {
		for (int i = 0; i < AeCnt.length; i++) {
			AeCnt[i] = 0;
		}

	}

	// ��ȡ����������������
	protected Boolean GetNormalPots2() {
		HttpPost_JsonArray jsonParser2 = new HttpPost_JsonArray();
		JSONArray json2 = jsonParser2.makeHttpRequest(getNormPots2Url, "POST");
		if (json2 != null) {
			// Log.d("������������������", json2.toString());// �ӷ���������������
			NormPotsList2 = new ArrayList<PotCtrl>();
			NormPotsList2 = JsonToBean_GetPublicData.JsonArrayToNormPots(json2.toString());
			if (NormPotsList2.size() == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	// ��ȡһ��������������
	protected Boolean GetNormalPots1() {
		HttpPost_JsonArray jsonParser1 = new HttpPost_JsonArray();
		JSONArray json1 = jsonParser1.makeHttpRequest(getNormPots1Url, "POST");
		if (json1 != null) {
			// Log.d("һ����������������", json1.toString());// �ӷ���������������
			NormPotsList1 = new ArrayList<PotCtrl>();
			NormPotsList1 = JsonToBean_GetPublicData.JsonArrayToNormPots(json1.toString());
			if (NormPotsList1.size() == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	protected void ShowBar_AE() {
		// ͼ����������
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y�᷽�򳧷�ЧӦϵ��
		ArrayList<String> xVals = new ArrayList<>();// X������
		int AeTotal1, AeTotal2, AeTotal, PotS1, PotS2, PotS;
		AeTotal1 = AeCnt[0] + AeCnt[1] + AeCnt[2];// һ����ЧӦ����
		AeTotal2 = AeCnt[3] + AeCnt[4] + AeCnt[5];// ������ЧӦ����
		AeTotal = AeTotal1 + AeTotal2;// ����ЧӦ����
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // һ��������������
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // ����������������
		PotS = PotS1 + PotS2; // ��������������
		// �������Դ

		if (NormPotS[0] != 0) {
			xVals.add("һ��1��");
			yVals.add(new BarEntry((float) AeCnt[0] / NormPotS[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("һ��2��");
			yVals.add(new BarEntry((float) AeCnt[1] / NormPotS[1], 1));
		}else{
            xVals.add("һ��2��");//ͣ��
        }
		if (NormPotS[2] != 0) {
			xVals.add("һ��3��");
			yVals.add(new BarEntry((float) AeCnt[2] / NormPotS[2], 2));
		}else{
			xVals.add("һ��3��");//ͣ��
			//yVals.add(new BarEntry((float) 0, 2));
		}
		if (PotS1 != 0) {
			xVals.add("һ��");
			yVals.add(new BarEntry((float) AeTotal1 / PotS1, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("����1��");
			yVals.add(new BarEntry((float) AeCnt[3] / NormPotS[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("����2��");
			yVals.add(new BarEntry((float) AeCnt[4] / NormPotS[4], 5));
		}else{
            xVals.add("����2��");//ͣ��
        }
		if (NormPotS[5] != 0) {
			xVals.add("����3��");
			yVals.add(new BarEntry((float) AeCnt[5] / NormPotS[5], 6));
		}else{
			xVals.add("����3��"); //ͣ��
			//yVals.add(new BarEntry((float) 0, 6));
		}
		if (PotS2 != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) AeTotal2 / PotS2, 7));
		}
		if (PotS != 0) {
			xVals.add("����");
			yVals.add(new BarEntry((float) AeTotal / PotS, 8));
		}
		String lable = "������  " + "<һ��1��" + NormPotS[0] + "> <һ��2��" + NormPotS[1] + "> <һ��3��" + NormPotS[2];
		lable = lable + "> <һ�� " + PotS1 + "> <����1��" + NormPotS[3] + "> <����2��" + NormPotS[4] + "> <����3��" + NormPotS[5]
				+ "> <���� " + PotS2 + ">  <���� " + PotS + ">";
		BarDataSet barDataSet = new BarDataSet(yVals, lable);
		barDataSet.setColor(Color.rgb(190, 0, 47));// ����������ɫ
		barDataSet.setDrawValues(true); // ��ʾ��ֵ
		barDataSet.setValueTextSize(13f);
		barDataSet.setBarSpacePercent(55f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// ���췽�����ַ���ʽ�������С������2λ,����0����.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		mBarChartAE.setData(bardata); // ��������
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChartAE.invalidate();
			}
		});

	}

	// ����ЧӦ�����ܺ�
	protected void CalcAeCnt(List<AeRecord> listBean_AeCnt) {
		if (listBean_AeCnt != null && listBean_AeCnt.size() != 0) {
			AeRecord aeCnt = new AeRecord();
			for (int i = 0; i < listBean_AeCnt.size(); i++) {
				aeCnt = listBean_AeCnt.get(i);
				if (aeCnt != null) {
					int potno = aeCnt.getPotNo(); // �ۺ�
					int aeS = aeCnt.getWaitTime();// ЧӦ����

					if (potno >= 1101 && potno <= 1136) {
						AeCnt[0] = AeCnt[0] + aeS; // һ����һ��ЧӦ�����ܺ�
					} else if (potno >= 1201 && potno <= 1237) {
						AeCnt[1] = AeCnt[1] + aeS;
					} else if (potno >= 1301 && potno <= 1337) {
						AeCnt[2] = AeCnt[2] + aeS;
					} else if (potno >= 2101 && potno <= 2136) {
						AeCnt[3] = AeCnt[3] + aeS; // ������һ��ЧӦ�����ܺ�
					} else if (potno >= 2201 && potno <= 2237) {
						AeCnt[4] = AeCnt[4] + aeS;
					} else if (potno >= 2301 && potno <= 2337) {
						AeCnt[5] = AeCnt[5] + aeS;
					}

				} else {
					Log.w("ЧӦ����", "�� " + i + " �� ЧӦ������Ϊ�գ�");
				}
			}
		}
	}

	// ͳ������������
	protected void CalcPotsNorm(List<PotCtrl> normPotsList, int Room) {
		if (normPotsList != null && (normPotsList.size() != 0)) {
			for (int i = 0; i < normPotsList.size(); i++) {
				PotCtrl mPotCtrl = new PotCtrl();
				mPotCtrl = normPotsList.get(i);
				if (mPotCtrl != null) {
					int potno = mPotCtrl.getPotNo(); // �ۺ�
					int mCtrl = mPotCtrl.getCtrls(); // ��״̬��
					// �����λ�������ƣ�00���� ������01 ����Ԥ�ȣ�10����������11����ͣ��
					if ((mCtrl & 0x03) == 0) {
						if (potno >= 1101 && potno <= 1136) {
							NormPotS[0]++;
						} else if (potno >= 1201 && potno <= 1237) {
							NormPotS[1]++;
						} else if (potno >= 1301 && potno <= 1337) {
							NormPotS[2]++;
						} else if (potno >= 2101 && potno <= 2136) {
							NormPotS[3]++; // ������һ��
						} else if (potno >= 2201 && potno <= 2237) {
							NormPotS[4]++;
						} else if (potno >= 2301 && potno <= 2337) {
							NormPotS[5]++;
						}
					}

				} else {
					Log.w("�ۿ�����", "�ۿ���������ΪNULL!");
				}
			}
			if (Room == 1) {
				CalcPots1 = true;
			}
			if (Room == 2) {
				CalcPots2 = true;
			}
		}

	}

	private void NormPots_clear() {
		for (int i = 0; i < NormPotS.length; i++) {
			NormPotS[i] = 0;
		}
	}

	private void checkUpDate() {
		UpdateManager manager = new UpdateManager(mContext, false);
		manager.checkUpdate();
	}

	private int NetStatus() {
		NetDetector netDetector = new NetDetector(mContext, false);
		return netDetector.isConnectingToInternet();

	}

	private void init_GetDate() {
		GetDateCnt++;
		if (dateTable == null) {
			// ִ�д�Զ�̻����������
			AsyTask_HttpGetDate mhttpgetdata_date = (AsyTask_HttpGetDate) new AsyTask_HttpGetDate(getDateTableUrl, this,
					mContext).execute();
		}

	}

	private void init_GetJXRecord() {
		GetJXCnt++;
		if (JXList == null) {
			AsyTask_HttpGetJXRecord mHttpGetData_JXRecord = (AsyTask_HttpGetJXRecord) new AsyTask_HttpGetJXRecord(
					getJXNameUrl, this, mContext).execute(); // ִ�д�Զ�̻�ý�����¼����
		}

	}

	private void init() {
		Homedialog = MyProgressDialog.createDialog(mContext);
		init_normalPot(); // ��ʼ�������������ؼ�
		init_button();// ��ʼ����ť�ؼ�
		ivShare = (ImageView) mView.findViewById(R.id.iv_share);
		ivShare.setOnClickListener(this);
		titlePopup = new TitlePopup(mContext, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Popup_initData();
		btnMore = (Button) mView.findViewById(R.id.btn_more);
		btnMore.setVisibility(View.VISIBLE);
		btnMore.setOnClickListener(this);
		tvTitle = (TextView) mView.findViewById(R.id.tv_title);
		ivWifi = (ImageView) mView.findViewById(R.id.iv_NoWiFi);
		// bannerView = (com.hewaiming.ALWorkInfo.banner.SlideShowView)
		// mView.findViewById(R.id.bannerwView);
		mSlideShowView = (io.github.ylbfdev.slideshowview.SlideShowView) mView.findViewById(R.id.MySlideShowView);
		// 5��ͼ���ʼ��
		mBarChartAE = (BarChart) mView.findViewById(R.id.Ae_chart);
		mBarChartV = (BarChart) mView.findViewById(R.id.AvgV_chart);
		mBarChartDJWD = (BarChart) mView.findViewById(R.id.DJWD_chart);
		mBarChartFZB = (BarChart) mView.findViewById(R.id.FZB_chart);
		mBarChartYHLND = (BarChart) mView.findViewById(R.id.YHLND_chart);
		// ͼ������ʼ��
		tvAeTitle = (TextView) mView.findViewById(R.id.tv_AeTitle);
		tvAvgVTitle = (TextView) mView.findViewById(R.id.tv_AvgV);
		tvDJWDTitle = (TextView) mView.findViewById(R.id.tv_DJWD);
		tvFZBTitle = (TextView) mView.findViewById(R.id.tv_FZB);
		tvYHLNDTitle = (TextView) mView.findViewById(R.id.tv_YHLND);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String todayValue = sdf.format(dt);
		tvAeTitle.setText(AE_TITLE + todayValue + " �ο�ֵ��" + AE_VALUE);
		init_BarCHART(mBarChartAE, 0);
		init_BarCHART(mBarChartV, 1);
		init_BarCHART(mBarChartDJWD, 2);
		init_BarCHART(mBarChartFZB, 3);
		init_BarCHART(mBarChartYHLND, 4);
	}

	private void init_button() {
		ivFreshPots = (ImageView) mView.findViewById(R.id.iv_refresh_pots);
		ivFreshPots.setOnClickListener(this);
		// iv_Fresh_Pots.setVisibility(View.GONE);
		ivFreshAe = (ImageView) mView.findViewById(R.id.iv_refresh_ae);
		ivFreshAe.setOnClickListener(this);
		ivFreshAvgV = (ImageView) mView.findViewById(R.id.iv_refresh_avgv);
		ivFreshAvgV.setOnClickListener(this);
		ivFreshDJWD = (ImageView) mView.findViewById(R.id.iv_refresh_djwd);
		ivFreshDJWD.setOnClickListener(this);
		ivFreshFZB = (ImageView) mView.findViewById(R.id.iv_refresh_fzb);
		ivFreshFZB.setOnClickListener(this);
		ivFreshYHLND = (ImageView) mView.findViewById(R.id.iv_refresh_yhlnd);
		ivFreshYHLND.setOnClickListener(this);
		imgbtnShowDJWD = (ImageButton) mView.findViewById(R.id.imgbtn_show_DJWDChart);
		imgbtnShowDJWD.setOnClickListener(this);
		imgbtnShowFZB = (ImageButton) mView.findViewById(R.id.imgbtn_show_FZBChart);
		imgbtnShowFZB.setOnClickListener(this);
		imgbtnShowYHLND = (ImageButton) mView.findViewById(R.id.imgbtn_show_YHLNDChart);
		imgbtnShowYHLND.setOnClickListener(this);
		// ��ȡ����������
		layoutNormal.setOnClickListener(this);

	}

	// ��ȡ�����������󣬲��ܽ��л�ȡ�ĵ�һ�߹��ղ������ݣ�����ͼ����ʽ��ʾ
	private void GetAllData_ShowChart(boolean All_Charts) {
		NormPots_clear(); //
		final CountDownLatch latch = new CountDownLatch(2);// �������˵�Э��
		// ��ȡһ��������������
		new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				boolean FoundData1 = false;
				do {
					if (GetNormalPots1()) {
						FoundData1 = true;
						break;
					}
					i++;
				} while (i < 2);// ȡ����������ʧ�ܣ�1�����Ի���
				if (!FoundData1) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mContext, "δ��ȡ��һ�������������������粻����������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
				latch.countDown();
			}
		}).start();
		// ��ȡ����������������
		new Thread(new Runnable() {
			public void run() {
				int j = 0;
				boolean FoundData2 = false;
				do {
					if (GetNormalPots2()) {
						FoundData2 = true;
						break;
					}
					j++;
				} while (j < 2); // ȡ����������ʧ�ܣ�2�����Ի���
				if (!FoundData2) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mContext, "δ��ȡ�����������������������粻����������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
				latch.countDown();
			}
		}).start();

		try {
			latch.await();// �ȴ����й�����ɹ���

			CalcPotsNorm(NormPotsList1, 1); // �ֱ�ͳ��һ������������
			CalcPotsNorm(NormPotsList2, 2);// �ֱ�ͳ�ƶ�������������
			if (CalcPots1 && CalcPots2) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						SetNormalPot(); // ��ʾ����������
						// iv_Fresh_Pots.setVisibility(View.GONE);
					}
				});
			}
			if (All_Charts) {
				initDATA_AE();// ��ʾ��ǰ����ЧӦϵ��
				initDATA_AvgV();// ��ʾ��ǰ����ƽ����ѹ
				// initDATA_DJWD();// ��ʾ��ǰ��������¶�
				// initDATA_FZB();// ��ʾ��ǰ�������ӱ�
				// initDATA_YHLND();// ��ʾ��ǰ����������Ũ��
			}

		} catch (InterruptedException e) {
			Log.e(TAG, "�������������� latch ����");
			Thread.currentThread().interrupt();
		}

	}

	private void init_normalPot() {
		layoutNormal = (RelativeLayout) mView.findViewById(R.id.Normal_Layout);
		tvPotTotal = (TextView) mView.findViewById(R.id.tv_potTotal);
		tvPot1 = (TextView) mView.findViewById(R.id.tv_Room1_sum);
		tvPot11 = (TextView) mView.findViewById(R.id.tv_Room11_sum);
		tvPot12 = (TextView) mView.findViewById(R.id.tv_Room12_sum);
		tvPot13 = (TextView) mView.findViewById(R.id.tv_Room13_sum);
		tvPot2 = (TextView) mView.findViewById(R.id.tv_Room2_sum);
		tvPot21 = (TextView) mView.findViewById(R.id.tv_Room21_sum);
		tvPot22 = (TextView) mView.findViewById(R.id.tv_Room22_sum);
		tvPot23 = (TextView) mView.findViewById(R.id.tv_Room23_sum);

	}

	private void init_BarCHART(BarChart mChart, int TYPE) {
		// ͼ����ʾ����
		// mBarChart.setTouchEnabled();
		mChart.setScaleEnabled(false); // ���Ŵ�

		mChart.getLegend().setEnabled(false);
		mChart.getLegend().setPosition(LegendPosition.BELOW_CHART_LEFT);// ����ע���λ�������Ϸ�
		mChart.getLegend().setForm(LegendForm.CIRCLE);// ���������ʾСͼ�����״
		mChart.getLegend().setWordWrapEnabled(true);
		mChart.getLegend().setTextSize(2.8f);

		mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);// ����X���λ��
		mChart.getXAxis().setDrawGridLines(false);// ����ʾ����
		mChart.getXAxis().setDrawAxisLine(false);
		if (TYPE == 3 || TYPE == 4) {
			mChart.getXAxis().setTextSize(5f);
			mChart.getXAxis().setLabelRotationAngle(6f);
			mChart.getXAxis().setLabelsToSkip(0);
			mChart.getXAxis().setSpaceBetweenLabels(20);
		} else {
			mChart.getXAxis().setTextSize(8f);
		}
		// mChart.getXAxis().setLabelRotationAngle(90f);
		mChart.getXAxis().setTextColor(Color.DKGRAY);

		mChart.getAxisRight().setEnabled(false);// �Ҳ಻��ʾY��
		mChart.getAxisLeft().setEnabled(false);
		mChart.getAxisLeft().setDrawLabels(false); // ���Y���겻��ʾ���ݿ̶�
		mChart.getAxisLeft().setAxisMinValue(0.0f);// ����Y����ʾ��Сֵ����Ȼ0������п�϶
		// mBarChart.getAxisLeft().setAxisMaxValue(2.0f);// ����Y����ʾ���ֵ
		mChart.getAxisLeft().setDrawGridLines(true);// ������Y������

		mChart.setNoDataTextDescription("û�л�ȡ�����ͼ������");
		mChart.setDescription("");

		if (TYPE == 0) {
			mChart.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					if (checkGlobalData()) {
						Intent aemost_intent = new Intent(getActivity(), AeMostActivity.class);
						Bundle aemostBundle = new Bundle();
						if (dateRecord != null) {
							aemostBundle.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
						}
						if (JXList != null) {
							aemostBundle.putSerializable("JXList", (Serializable) JXList);
						}
						if (dateRecord != null && JXList != null) {
							aemostBundle.putString("ip", ip);
							aemostBundle.putInt("port", port);
							aemost_intent.putExtras(aemostBundle);
							startActivity(aemost_intent);
							// ЧӦ��
						}
					}
					return true;
				}
			});
		}
		// ��ƽ����ѹ�������
		if (TYPE == 1) {
			mChart.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {

					if (checkGlobalData()) {
						Intent avgV_intent = new Intent(getActivity(), AreaAvgVActivity.class);
						Bundle avgVBundle = new Bundle();
						if (dateRecord != null) {
							avgVBundle.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
						}
						if (JXList != null) {
							avgVBundle.putSerializable("JXList", (Serializable) JXList);
						}
						if (dateRecord != null && JXList != null) {
							avgVBundle.putString("ip", ip);
							avgVBundle.putInt("port", port);
							avgV_intent.putExtras(avgVBundle);
							startActivity(avgV_intent);

						}
					}
					return true;
				}
			});
		}
		if (TYPE == 2) {

		}
		if (TYPE == 3) {

		}
		if (TYPE == 4) {

		}
	}

	protected Boolean checkGlobalData() {
		if (GetJXCnt > 2) {
			tvTitle.setTextSize(14);
			tvTitle.setText("����վ:" + "����̫��������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
			return false;
		} else {
			Toast.makeText(mContext, "��" + GetJXCnt + " �γ��Ի�ȡ������¼���ݣ����Ժ����ԣ�", Toast.LENGTH_SHORT).show();
			init_GetJXRecord();
		}

		if (GetDateCnt > 2) {
			tvTitle.setTextSize(14);
			tvTitle.setText("����վ:" + "����̫��������Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
			return false;
		} else {
			Toast.makeText(mContext, "��" + GetDateCnt + " �γ��Ի�ȡ�������ݣ����Ժ����ԣ�", Toast.LENGTH_SHORT).show();
			init_GetDate();
		}
		return true;
	}

	private void Popup_initData() {
		// �������������������
		titlePopup.addAction(new ActionItem(mContext, "����Զ�̷�����", R.drawable.settings));
		titlePopup.addAction(new ActionItem(mContext, "����", R.drawable.mm_title_btn_keyboard_normal));
	}

	public boolean initdate(Context ctx) {
		sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		if (sp != null) {
			ip = sp.getString("ipstr", ip);
			if (ip != null) {
				if (sp.getString("port", String.valueOf(port)) != null) {
					port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
				} else {
					Toast.makeText(ctx, "������Զ�̷������˿�", Toast.LENGTH_SHORT).show();
					return false;
				}
			} else {
				Toast.makeText(ctx, "������Զ�̷�����IP", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void GetALLDayUrl(String data) {
		// �õ�����
		if (data.equals("")) {
			// Toast.makeText(getApplicationContext(),
			// "û�л�ȡ��[����]��ʼ���ݣ�����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG).show();
			// tv_title.setTextSize(14);
			// tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");

		} else {
			dateTable = new ArrayList<String>();
			dateTable = JsonToBean_GetPublicData.JsonArrayToDate(data);
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String todayValue = sdf.format(dt);
			dateRecord = new ArrayList<String>(dateTable); // ��¼����
			dateRecord.add(0, todayValue);
		}

	}

	@Override
	public void GetJXRecordUrl(String data) {
		if (data.equals("")) {
			// tv_title.setTextSize(14);
			// tv_title.setText("����վ:" + "����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��");
			// Toast.makeText(getApplicationContext(),
			// "û�л�ȡ��[������]��ʼ���ݣ�����Զ�̷�����IP�Ͷ˿��Ƿ���ȷ��", Toast.LENGTH_LONG).show();

		} else {
			JXList = new ArrayList<Map<String, Object>>();
			JXList = JsonToBean_GetPublicData.JsonArrayToJXRecord(data);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_more:
			titlePopup.show(v);
			break;
		case R.id.iv_share:
			MyConst.showShare(mContext); // һ������
			break;
		case R.id.iv_refresh_pots:
			// ��ȡ��������������,ЧӦ���ݣ�ƽ����ѹ
			Homedialog.setMessage("����������������ЧӦϵ����ƽ����ѹ...");
			if (!Homedialog.isShowing()) {
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			GetAllData_ShowChart(false);
			break;
		case R.id.iv_refresh_ae:
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("�������ظ���ЧӦϵ��...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}

			initDATA_AE();// ��ʾ��ǰ����ЧӦϵ��
			break;
		case R.id.iv_refresh_avgv:
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("�������ظ���ƽ����ѹ...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_AvgV();// ��ʾ��ǰ����ƽ����ѹ
			break;
		case R.id.iv_refresh_djwd:
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("�������ظ�������¶�...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_DJWD();// ��ʾ��ǰ��������¶�
			break;
		case R.id.imgbtn_show_DJWDChart:
			if (mBarChartDJWD.getVisibility() == View.GONE) {
				mBarChartDJWD.setVisibility(View.VISIBLE);
				ivFreshDJWD.setVisibility(View.VISIBLE);
				imgbtnShowDJWD.setImageDrawable(getResources().getDrawable(R.drawable.up_gray));
				if (!Homedialog.isShowing()) {
					Homedialog.setMessage("��������...");
					Homedialog.show();
					mHandler.sendEmptyMessageDelayed(0, 1500);
				}
				initDATA_DJWD();// ��ʾ��ǰ��������¶�
			} else {
				mBarChartDJWD.setVisibility(View.GONE);
				ivFreshDJWD.setVisibility(View.INVISIBLE);
				imgbtnShowDJWD.setImageDrawable(getResources().getDrawable(R.drawable.down_gray));
			}
			break;
		case R.id.iv_refresh_fzb:
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("�������ظ������ӱ�...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_FZB();// ��ʾ��ǰ�������ӱ�
			break;
		case R.id.imgbtn_show_FZBChart:
			if (mBarChartFZB.getVisibility() == View.GONE) {
				mBarChartFZB.setVisibility(View.VISIBLE);
				ivFreshFZB.setVisibility(View.VISIBLE);
				imgbtnShowFZB.setImageDrawable(getResources().getDrawable(R.drawable.up_gray));
				if (!Homedialog.isShowing()) {
					Homedialog.setMessage("��������...");
					Homedialog.show();
					mHandler.sendEmptyMessageDelayed(0, 1500);
				}
				initDATA_FZB();// ��ʾ��ǰ�������ӱ�
			} else {
				mBarChartFZB.setVisibility(View.GONE);
				ivFreshFZB.setVisibility(View.INVISIBLE);
				imgbtnShowFZB.setImageDrawable(getResources().getDrawable(R.drawable.down_gray));
			}
			break;
		case R.id.iv_refresh_yhlnd:
			// ˢ��������Ũ��
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("�������ظ���������Ũ��...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_YHLND();// ��ʾ��ǰ����������Ũ��
			break;
		case R.id.imgbtn_show_YHLNDChart:
			// ��ʾ������Ũ��
			if (mBarChartYHLND.getVisibility() == View.GONE) {
				mBarChartYHLND.setVisibility(View.VISIBLE);
				ivFreshYHLND.setVisibility(View.VISIBLE);
				imgbtnShowYHLND.setImageDrawable(getResources().getDrawable(R.drawable.up_gray));
				if (!Homedialog.isShowing()) {
					Homedialog.setMessage("��������...");
					Homedialog.show();
					mHandler.sendEmptyMessageDelayed(0, 1500);
				}
				initDATA_YHLND();// ��ʾ��ǰ����������Ũ��
			} else {
				mBarChartYHLND.setVisibility(View.GONE);
				ivFreshYHLND.setVisibility(View.INVISIBLE);
				imgbtnShowYHLND.setImageDrawable(getResources().getDrawable(R.drawable.down_gray));
			}
			break;
		case R.id.Normal_Layout:
			// ��ȡ��������������,ЧӦ���ݣ�ƽ����ѹ
			Homedialog.setMessage("����������������ЧӦϵ����ƽ����ѹ...");
			if (!Homedialog.isShowing()) {
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			GetAllData_ShowChart(false);
			break;
		}
	}

}
