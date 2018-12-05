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
			AVGV_VALUE = "≤4.02", AE_VALUE = "≤0.25";
	private static final String YHLND_TITLE = "氧化铝浓度(%) ", DJWD_TITLE = "电解温度(°C) ", FZB_TITLE = "分子比(%) ",
			AVGV_TITLE = "平均电压(V) ", AE_TITLE = "效应系数 ";
	protected static final String TAG = "HomeFragment Error";
	private SharedPreferences sp;
	// private GridView gridView;
	private Button btnMore;
	private List<String> dateRecord = null; // 记录日期
	private List<String> dateTable = null; // 报表日期
	private List<Map<String, Object>> JXList = null; // 记录号名

	private String getDateTableUrl = ":8000/scgy/android/odbcPhP/getDate.php";
	private String getJXNameUrl = ":8000/scgy/android/odbcPhP/getJXRecordName.php";

	private Context mContext;
	private TitlePopup titlePopup;
	private TextView tvTitle, tvAeTitle, tvAvgVTitle, tvDJWDTitle, tvFZBTitle, tvYHLNDTitle;
	private ImageView ivWifi, ivShare;
	// private SlideShowView bannerView;
	private io.github.ylbfdev.slideshowview.SlideShowView mSlideShowView;
	private int[] NormPotS = { 0, 0, 0, 0, 0, 0 }; // 各区正常槽数量
	private int[] AeCnt = { 0, 0, 0, 0, 0, 0 }; // 各区效应次数
	private double[] AvgVSum = { 0, 0, 0, 0, 0, 0 }; // 各区平均电压总和
	private int[] DJWDSum = { 0, 0, 0, 0, 0, 0 }; // 各区电解温度总和
	private double[] FZBAvg = { 0, 0, 0, 0, 0, 0 }; // 各区分子比平均值
	private double[] YHLNDAvg = { 0, 0, 0, 0, 0, 0 }; // 各区氧化铝浓度平均值

	private String getAeCntUrl = ":8000/scgy/android/odbcPhP/AeCnt_area_date.php"; // 效应次数
	protected String getAvgVAreaUrl = ":8000/scgy/android/odbcPhP/GetAvgV_dayTable.php";
	protected String getDJWDUrl = ":8000/scgy/android/odbcPhP/GetDJWD_MeasueTable.php";
	protected String getFZBUrl = ":8000/scgy/android/odbcPhP/GetFZB_MeasueTable_plus.php"; // 获取分子比数据地址
	protected String getYHLNDUrl = ":8000/scgy/android/odbcPhP/GetYHLND_MeasueTable_plus.php"; // 获取氧化铝浓度数据地址
	private List<AeRecord> listBeanAeCnt = null; // 效应次数列表
	private List<AvgV> listBeanAvgV = null; // 日报数据列表
	private List<DJWD> listBeanDJWD = null; // 电解温度列表
	private List<HY_item> listBeanFZB = null; // 初始化分子比数据列表
	private List<HY_item> listBeanYHLND = null; // 氧化铝浓度数据列表

	private String getNormPots1Url = ":8000/scgy/android/odbcPhP/GetNormPots1.php";
	private String getNormPots2Url = ":8000/scgy/android/odbcPhP/GetNormPots2.php";

	private List<PotCtrl> NormPotsList1 = null; // 一厂房槽状态 列表
	private List<PotCtrl> NormPotsList2 = null; // 二厂房槽状态 列表

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
		init(); // 初始化各控件
		NetDetector netDetector = new NetDetector(mContext, false);
		if (netDetector.isConnectingToInternetNoShow() == 1) {
			ivWifi.setVisibility(View.GONE);
			mSlideShowView.setVisibility(View.VISIBLE);// wifi
		} else {
			ivWifi.setVisibility(View.VISIBLE);
			mSlideShowView.setVisibility(View.GONE);// no wifi
		}
		if (NetStatus() != 0) {
			if (!initdate(mContext)) { // 取远程服务器地址和端口
				Intent intent = new Intent(getActivity(), SettingActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);// 没有设置远程服务器ip和端口

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
				// init_GetDate(); // 获取日期
				// init_GetJXRecord(); // 获取解析记录
				GetAllData_ShowChart(true); // 获取运行槽数据后，才能执行‘四低一高‘工艺数据，且显示5个图表

				List<String> imageUris = new ArrayList<>();
				String IP = "http://" + ip;
				imageUris.add(IP + MyConst.PIC_ADDRESS[0]);
				imageUris.add(IP + MyConst.PIC_ADDRESS[1]);
				imageUris.add(IP + MyConst.PIC_ADDRESS[2]);
				imageUris.add(IP + MyConst.PIC_ADDRESS[3]);
				imageUris.add(IP + MyConst.PIC_ADDRESS[4]);
				// mSlideShowView.setVisibility(View.VISIBLE);// wifi
				/* 为控件设置图片 */
				mSlideShowView.setImageUris(imageUris);
				/* 开始播放 默认4秒切换 */
				mSlideShowView.startPlay();
				checkUpDate(); // 检测版本升级
			}

		} else {
			Toast.makeText(mContext, "网络异常！", Toast.LENGTH_LONG).show();
		}

	}

	// 获取各区氧化铝浓度数据，再显示图表
	private void initDATA_YHLND() {
		listBeanYHLND = new ArrayList<HY_item>(); // 初始化分子比适配器
		ExecutorService exec_YHLND = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(6, new Runnable() {
			@Override
			public void run() {
				Log.i("氧化铝浓度数据", "获取各区氧化铝浓度数据OK，开始显示柱形图表啦，happy去");
				YHLNDSum_Clear();
				ShowBar_YHLND(CalcYHLNDSUM(listBeanYHLND));// 显示各区氧化铝浓度柱状图
			}
		});

		// 获取一厂房一区氧化铝浓度数据
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
						tvYHLNDTitle.setText(YHLND_TITLE + "  参考值:" + YHLND_VALUE);
					}
				});

				HY_item room11 = GetRoomAvgYHLND("11", BeginDateValue, EndDateValue);
				if (room11 != null) {
					listBeanYHLND.add(room11);
				}
				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "一厂房一区氧化铝浓度数据CyclicBarrier有误");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "一厂房一区氧化铝浓度数据CyclicBarrier有误");
				}
			}
		});

		// 获取一厂房二区氧化铝浓度数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {

					Log.e(TAG, "一厂房二区氧化铝浓度数据CyclicBarrier有误");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "一厂房二区氧化铝浓度数据CyclicBarrier有误");
				}
			}
		});

		// 获取一厂房三区氧化铝浓度数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "一厂房三区氧化铝浓度数据CyclicBarrier1有误");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "一厂房三区氧化铝浓度数据CyclicBarrier2有误");
				}
			}
		});
		// 获取二厂房一区氧化铝浓度数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "二厂房一区氧化铝浓度数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "二厂房一区氧化铝浓度数据CyclicBarrier有误2");
				}
			}
		});
		// 获取二厂房二区氧化铝浓度数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "二厂房二区氧化铝浓度数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "二厂房二区氧化铝浓度数据CyclicBarrier有误2");
				}
			}
		});
		// 获取二厂房三区氧化铝浓度数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "二厂房三区氧化铝浓度数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "二厂房三区氧化铝浓度数据CyclicBarrier有误2");
				}
			}
		});
		exec_YHLND.shutdown();
	}

	protected void ShowBar_YHLND(String[] mDate) {
		// 图表数据设置
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y轴方向厂房氧化铝浓度数据
		ArrayList<String> xVals = new ArrayList<>();// X轴数据
		double YHLNDTotal1 = 0, YHLNDTotal2 = 0, YHLNDTotal = 0; // 一厂房，二厂房，厂房氧化铝浓度总和
		int PotS1, PotS2, PotS;
		int Cnt1 = 0, Cnt2 = 0, TotalCnt = 0;
		for (int i = 0; i < 3; i++) {
			if (YHLNDAvg[i] > 0.1 && YHLNDAvg[i] < 5.0) {
				YHLNDTotal1 = YHLNDTotal1 + YHLNDAvg[i];// 一厂房氧化铝浓度
				Cnt1++;
			}
		}
		for (int j = 3; j < 6; j++) {
			if (YHLNDAvg[j] > 0.1 && YHLNDAvg[j] < 5.0) {
				YHLNDTotal2 = YHLNDTotal2 + YHLNDAvg[j];// 二厂房氧化铝浓度
				Cnt2++;
			}
		}
		if (!MyConst.isEqual(YHLNDTotal1, 0.0)) {
			YHLNDTotal = YHLNDTotal + YHLNDTotal1;
			TotalCnt = TotalCnt + Cnt1;
		}
		if (!MyConst.isEqual(YHLNDTotal2, 0.0)) {
			YHLNDTotal = YHLNDTotal + YHLNDTotal2;// 厂房氧化铝浓度总和
			TotalCnt = TotalCnt + Cnt2;
		}
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // 一厂房正常槽总数
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // 二厂房正常槽总数
		PotS = PotS1 + PotS2; // 厂房正常槽总数
		// 添加数据源
		if (NormPotS[0] != 0) {
			xVals.add("一厂1区" + mDate[0]);
			yVals.add(new BarEntry((float) YHLNDAvg[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("一厂2区" + mDate[1]);
			yVals.add(new BarEntry((float) YHLNDAvg[1], 1));
		}else {
			xVals.add("一厂2区" + mDate[1]);
		}
		if (NormPotS[2] != 0) {
			xVals.add("一厂3区" + mDate[2]);
			yVals.add(new BarEntry((float) YHLNDAvg[2], 2));
		}else{
			xVals.add("一厂3区" + mDate[2]);
		}
		if (PotS1 != 0 && Cnt1 != 0) {
			xVals.add("一厂");
			yVals.add(new BarEntry((float) YHLNDTotal1 / Cnt1, 3));
		} else if (Cnt1 == 0) {
			xVals.add("一厂");
			yVals.add(new BarEntry((float) 0, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("二厂1区" + mDate[3]);
			yVals.add(new BarEntry((float) YHLNDAvg[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("二厂2区" + mDate[4]);
			yVals.add(new BarEntry((float) YHLNDAvg[4], 5));
		}else{
			xVals.add("二厂2区" + mDate[4]);
		}
		if (NormPotS[5] != 0) {
			xVals.add("二厂3区" + mDate[5]);
			yVals.add(new BarEntry((float) YHLNDAvg[5], 6));
		}else{
			xVals.add("二厂3区" + mDate[5]);
		}

		if (PotS2 != 0 && Cnt2 != 0) {
			xVals.add("二厂");
			yVals.add(new BarEntry((float) YHLNDTotal2 / Cnt2, 7));
		} else if (Cnt2 == 0) {
			xVals.add("二厂");
			yVals.add(new BarEntry((float) 0, 7));
		}
		if (PotS != 0 && TotalCnt != 0) {
			xVals.add("厂房");
			yVals.add(new BarEntry((float) YHLNDTotal / TotalCnt, 8));
		} else if (TotalCnt == 0) {
			xVals.add("厂房");
			yVals.add(new BarEntry((float) 0, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "区氧化铝浓度");
		barDataSet.setColor(Color.rgb(56, 161, 219));// 设置数据露草色颜色
		barDataSet.setDrawValues(true); // 显示数值
		barDataSet.setValueTextSize(11f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.000");// 构造方法的字符格式这里如果小数不足2位,会以0补足.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChartYHLND.setData(bardata); // 设置数据
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
						potno = Integer.parseInt(mTable_yhlnd.getPotNo()); // 槽号
					}
					if (!(mYHLND == null || mYHLND.length() <= 0)) {
						YHLND = Double.parseDouble(mYHLND); // 氧化铝浓度
					}
					if (potno == 11) {
						YHLNDAvg[0] = YHLND; // 一厂房一区氧化铝浓度平均值
						YHLNDDate[0] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 12) {
						YHLNDAvg[1] = YHLND; // 一厂房二区氧化铝浓度平均值
						YHLNDDate[1] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 13) {
						YHLNDAvg[2] = YHLND; // 一厂房三区氧化铝浓度平均值
						YHLNDDate[2] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 21) {
						YHLNDAvg[3] = YHLND; // 二厂房一区氧化铝浓度平均值
						YHLNDDate[3] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 22) {
						YHLNDAvg[4] = YHLND; // 二厂房二区氧化铝浓度平均值
						YHLNDDate[4] = mTable_yhlnd.getDdate().substring(5, 10);
					}
					if (potno == 23) {
						YHLNDAvg[5] = YHLND; // 二厂房三区氧化铝浓度平均值
						YHLNDDate[5] = mTable_yhlnd.getDdate().substring(5, 10);
					}

				} else {
					Log.i("氧化铝浓度项:", "第 " + i + " 氧化铝浓度项 ，为空！");
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

	// 获取分子比数据，再显示图表
	private void initDATA_FZB() {
		listBeanFZB = new ArrayList<HY_item>(); // 初始化分子比适配器
		ExecutorService exec_FZB = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(6, new Runnable() {
			@Override
			public void run() {
				Log.i("获取各区分子比数据OK", "获取各区分子比数据OK，开始显示柱形图表啦，happy去");
				FZBSum_Clear();
				ShowBar_FZB(CalcFZBSUM(listBeanFZB));// 显示各区分子比柱状图
			}
		});

		// 获取一厂房一区分子比数据
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
						tvFZBTitle.setText(FZB_TITLE + "  参考值:" + FZB_VALUE);
					}
				});
				// listBean_FZB = new ArrayList<HY_item>(); // 初始化分子比适配器
				HY_item room11 = GetRoomAvgFzb("11", BeginDateValue, EndDateValue);
				if (room11 != null) {
					listBeanFZB.add(room11);
				}

				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "一厂房一区分子比数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "一厂房一区分子比数据CyclicBarrier有误2");
				}
			}
		});
		// 获取一厂房二区分子比数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "一厂房二区分子比数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "一厂房二区分子比数据CyclicBarrier有误2");
				}
			}
		});

		// 获取一厂房三区分子比数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "一厂房三区分子比数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "一厂房三区分子比数据CyclicBarrier有误2");
				}
			}
		});
		// 获取二厂房一区分子比数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "二厂房一区分子比数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "二厂房一区分子比数据CyclicBarrier有误2");
				}
			}
		});
		// 获取二厂房二区分子比数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "二厂房二区分子比数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "二厂房二区分子比数据CyclicBarrier有误2");
				}
			}
		});
		// 获取二厂房三区分子比数据
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
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "二厂房三区分子比数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {
					Log.e(TAG, "二厂房三区分子比数据CyclicBarrier有误2");
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
			Log.i("厂房：分子比" + areaID, "获取厂房分子比OK，其他数据呢");
			return JsonToBean_Area_Date.JsonArrayToFZBItem(areaID, json.toString());
		} else {
			Log.i("厂房：分子比" + areaID, "从PHP服务器无数据返回！");
			handler.post(new Runnable() {
				@Override
				public void run() {
					// "未获取分子比,请检查远程服务器IP和端口是否正确！");
					Toast.makeText(mContext, "未获取到分子比信息，可能还没有输入！区号：" + areaID, Toast.LENGTH_SHORT).show();
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
			// Log.d("厂房：氧化铝浓度", json.toString());// 从服务器返回有数据
			// System.out.println("获取厂房氧化铝浓度OK，其他数据呢");
			Log.i("厂房：氧化铝浓度" + areaID, "获取厂房氧化铝浓度OK，其他数据呢");
			return JsonToBean_Area_Date.JsonArrayToYHLNDItem(areaID, json.toString());
		} else {
			Log.i("厂房：氧化铝浓度" + areaID, "从PHP服务器无数据返回！");
			handler.post(new Runnable() {
				@Override
				public void run() {
					// "未获取氧化铝浓度,请检查远程服务器IP和端口是否正确！");
					Toast.makeText(mContext, "未获取到氧化铝浓度信息，可能还没有输入！区号：" + areaID, Toast.LENGTH_SHORT).show();
				}
			});
			return null;
		}
	}

	protected void ShowBar_FZB(String[] mDate) {
		// 图表数据设置
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y轴方向厂房分子比
		ArrayList<String> xVals = new ArrayList<>();// X轴数据
		double FZBTotal1 = 0, FZBTotal2 = 0, FZBTotal = 0; // 一厂房，二厂房，厂房分子比总和
		int PotS1, PotS2, PotS;
		int Cnt1 = 0, Cnt2 = 0, TotalCnt = 0;
		for (int i = 0; i < 3; i++) {
			if (FZBAvg[i] > 2.0 && FZBAvg[i] < 4.0) {
				FZBTotal1 = FZBTotal1 + FZBAvg[i];// 一厂房分子比平均值
				Cnt1++;
			}
		}
		for (int j = 3; j < 6; j++) {
			if (FZBAvg[j] > 2.0 && FZBAvg[j] < 4.0) {
				FZBTotal2 = FZBTotal2 + FZBAvg[j];// 二厂房分子比平均值
				Cnt2++;
			}
		}
		if (!MyConst.isEqual(FZBTotal1, 0.0)) {
			FZBTotal = FZBTotal + FZBTotal1;
			TotalCnt = TotalCnt + Cnt1;
		}
		if (!MyConst.isEqual(FZBTotal2, 0.0)) {
			FZBTotal = FZBTotal + FZBTotal2;// 厂房分子比总和
			TotalCnt = TotalCnt + Cnt2;
		}
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // 一厂房正常槽总数
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // 二厂房正常槽总数
		PotS = PotS1 + PotS2; // 厂房正常槽总数
		// 添加数据源

		xVals.add("一厂1区" + mDate[0]);
		yVals.add(new BarEntry((float) FZBAvg[0], 0));

		xVals.add("一厂2区" + mDate[1]);
		yVals.add(new BarEntry((float) FZBAvg[1], 1));

		xVals.add("一厂3区" + mDate[2]);
		yVals.add(new BarEntry((float) FZBAvg[2], 2));

		if (PotS1 != 0 && Cnt1 != 0) {
			xVals.add("一厂");
			yVals.add(new BarEntry((float) FZBTotal1 / Cnt1, 3));
		} else if (Cnt1 == 0) {
			xVals.add("一厂");
			yVals.add(new BarEntry((float) 0, 3));
		}
		xVals.add("二厂1区" + mDate[3]);
		yVals.add(new BarEntry((float) FZBAvg[3], 4));

		xVals.add("二厂2区" + mDate[4]);
		yVals.add(new BarEntry((float) FZBAvg[4], 5));

		xVals.add("二厂3区" + mDate[5]);
		yVals.add(new BarEntry((float) FZBAvg[5], 6));

		if (PotS2 != 0 && Cnt2 != 0) {
			xVals.add("二厂");
			yVals.add(new BarEntry((float) FZBTotal2 / Cnt2, 7));
		} else if (Cnt2 == 0) {
			xVals.add("二厂");
			yVals.add(new BarEntry((float) 0, 7));
		}
		if (PotS != 0 && TotalCnt != 0) {
			xVals.add("厂房");
			yVals.add(new BarEntry((float) FZBTotal / TotalCnt, 8));
		} else if (TotalCnt == 0) {
			xVals.add("厂房");
			yVals.add(new BarEntry((float) 0, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "区分子比");
		barDataSet.setColor(Color.rgb(66, 80, 102));// 设置数据戴兰颜色
		barDataSet.setDrawValues(true); // 显示数值
		barDataSet.setValueTextSize(11f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.000");// 构造方法的字符格式这里如果小数不足2位,会以0补足.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChartFZB.setData(bardata); // 设置数据
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
						potno = Integer.parseInt(mTable.getPotNo()); // 槽号

					}
					if (!(mFzb == null || mFzb.length() <= 0)) {
						Fzb = Double.parseDouble(mFzb); // 分子比

					}
					if (potno == 11) {
						FZBAvg[0] = Fzb; // 一厂房一区分子比平均值
						FzbDate[0] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 12) {
						FZBAvg[1] = Fzb; // 一厂房二区分子比平均值
						FzbDate[1] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 13) {
						FZBAvg[2] = Fzb; // 一厂房三区分子比平均值
						FzbDate[2] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 21) {
						FZBAvg[3] = Fzb; // 二厂房一区分子比平均值
						FzbDate[3] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 22) {
						FZBAvg[4] = Fzb; // 二厂房二区分子比平均值
						FzbDate[4] = mTable.getDdate().substring(5, 10);
					}
					if (potno == 23) {
						FZBAvg[5] = Fzb; // 二厂房三区分子比平均值
						FzbDate[5] = mTable.getDdate().substring(5, 10);
					}

				} else {
					Log.i("分子比平均值项", "第 " + i + " 分子比平均值项 ，为空！");
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
				Log.i("各区电解温度数据OK", "获取各区电解温度数据OK，开始显示柱形图表啦，happy去");
				DJWDSum_Clear();
				CalcDJWDSUM(listBeanDJWD);
				ShowBar_DJWD();// 显示各区电解温度柱状图
			}
		});

		// 获取电解温度
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
						tvDJWDTitle.setText(DJWD_TITLE + todayValue + " 参考值:" + DJWD_VALUE);
					}
				});
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // 全部槽号
				mparams.add(new BasicNameValuePair("BeginDate", todayValue));
				mparams.add(new BasicNameValuePair("EndDate", todayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(getDJWDUrl, "POST", mparams);
				if (json != null) {
					Log.i("厂房电解温度", "获取厂房电解温度OK，其他数据呢");
					listBeanDJWD = new ArrayList<DJWD>(); // 初始化电解温度适配器
					listBeanDJWD = JsonToBean_Area_Date.JsonArrayToDJWDBean(json.toString());

				} else {
					// 再次get电解温度数据
					json = jsonParser.makeHttpRequest(getDJWDUrl, "POST", mparams);
					if (json != null) {
						// Log.d("厂房：电解温度", json.toString());// 从服务器返回有数据
						listBeanDJWD = new ArrayList<DJWD>(); // 初始化电解温度适配器
						listBeanDJWD = JsonToBean_Area_Date.JsonArrayToDJWDBean(json.toString());
					} else {
						Log.i("厂房：电解温度", "从PHP服务器无数据返回！");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// "未获取效应次数,请检查远程服务器IP和端口是否正确！");
								Toast.makeText(mContext, "未获取到电解温度信息，可能还没有输入！", Toast.LENGTH_SHORT).show();

							}
						});
					}
				}
				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "厂房电解温度数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "厂房电解温度数据CyclicBarrier有误2");
				}
			}
		});

		exec_DJWD.shutdown();
	}

	// 显示电解温度图表
	protected void ShowBar_DJWD() {
		// 图表数据设置
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y轴方向厂房电解温度
		ArrayList<String> xVals = new ArrayList<>();// X轴数据
		int DJWDTotal1, DJWDTotal2, DJWDTotal; // 一厂房，二厂房，厂房总电解温度和
		int PotS1, PotS2, PotS;
		DJWDTotal1 = DJWDSum[0] + DJWDSum[1] + DJWDSum[2];// 一厂房电解温度总和
		DJWDTotal2 = DJWDSum[3] + DJWDSum[4] + DJWDSum[5];// 二厂房电解温度总和
		DJWDTotal = DJWDTotal1 + DJWDTotal2;// 电解温度总和
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // 一厂房正常槽总数
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // 二厂房正常槽总数
		PotS = PotS1 + PotS2; // 厂房正常槽总数
		// 添加数据源
		if (NormPotS[0] != 0) {
			xVals.add("一厂1区");
			yVals.add(new BarEntry((float) DJWDSum[0] / NormPotS[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("一厂2区");
			yVals.add(new BarEntry((float) DJWDSum[1] / NormPotS[1], 1));
		}else{
			xVals.add("一厂2区");
		}
		if (NormPotS[2] != 0) {
			xVals.add("一厂3区");
			yVals.add(new BarEntry((float) DJWDSum[2] / NormPotS[2], 2));
		}else {
			xVals.add("一厂3区");
		}
		if (PotS1 != 0) {
			xVals.add("一厂");
			yVals.add(new BarEntry((float) DJWDTotal1 / PotS1, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("二厂1区");
			yVals.add(new BarEntry((float) DJWDSum[3] / NormPotS[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("二厂2区");
			yVals.add(new BarEntry((float) DJWDSum[4] / NormPotS[4], 5));
		}else{
			xVals.add("二厂2区");
		}
		if (NormPotS[5] != 0) {
			xVals.add("二厂3区");
			yVals.add(new BarEntry((float) DJWDSum[5] / NormPotS[5], 6));
		}else{
			xVals.add("二厂3区");
		}
		if (PotS2 != 0) {
			xVals.add("二厂");
			yVals.add(new BarEntry((float) DJWDTotal2 / PotS2, 7));
		}
		if (PotS != 0) {
			xVals.add("厂房");
			yVals.add(new BarEntry((float) DJWDTotal / PotS, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "电解温度");
		barDataSet.setColor(Color.rgb(255, 215, 0));// 设置浅黄红色颜色
		barDataSet.setDrawValues(true); // 显示数值
		barDataSet.setValueTextSize(10f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.0");// 构造方法的字符格式这里如果小数不足2位,会以0补足.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChartDJWD.setData(bardata); // 设置数据
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
						potno = Integer.parseInt(mTable.getPotNo()); // 槽号
					}
					if (!(mDjwd == null || mDjwd.length() <= 0)) {
						Djwd = Integer.parseInt(mDjwd); // 电解温度
					}
					if (potno >= 1101 && potno <= 1136) {
						DJWDSum[0] = DJWDSum[0] + Djwd; // 一厂房一区电解温度总和
					} else if (potno >= 1201 && potno <= 1237) {
						DJWDSum[1] = DJWDSum[1] + Djwd;
					} else if (potno >= 1301 && potno <= 1337) {
						DJWDSum[2] = DJWDSum[2] + Djwd;
					} else if (potno >= 2101 && potno <= 2136) {
						DJWDSum[3] = DJWDSum[3] + Djwd; // 二厂房一区电解温度总和
					} else if (potno >= 2201 && potno <= 2237) {
						DJWDSum[4] = DJWDSum[4] + Djwd;
					} else if (potno >= 2301 && potno <= 2337) {
						DJWDSum[5] = DJWDSum[5] + Djwd;
					}

				} else {
					Log.i("电解温度", "第 " + i + " 项 电解温度，为空！");
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
				Log.i("区平均电压数据", "获取各区平均电压数据OK，开始显示柱形图表啦，happy去");
				AvgVSum_Clear();
				CalcAvgVSUM(listBeanAvgV);
				ShowBar_AvgV();// 显示各区平均电压柱状图
			}
		});

		// 获取平均电压
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
						tvAvgVTitle.setText(AVGV_TITLE + yesterdayValue + "  参考值:" + AVGV_VALUE);
					}
				});
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // 全部槽号
				mparams.add(new BasicNameValuePair("BeginDate", yesterdayValue));
				mparams.add(new BasicNameValuePair("EndDate", yesterdayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(getAvgVAreaUrl, "POST", mparams);
				if (json != null) {
					Log.i("厂房平均电压", "获取厂房平均电压OK，其他数据呢");
					listBeanAvgV = new ArrayList<AvgV>(); // 初始化效应次数适配器
					listBeanAvgV = JsonToBean_Area_Date.JsonArrayToAvgVDayTableBean(json.toString());

				} else {
					// 再次get平均电压数据
					json = jsonParser.makeHttpRequest(getAvgVAreaUrl, "POST", mparams);
					if (json != null) {
						Log.i("厂房：平均电压", "ok");// 从服务器返回有数据
						listBeanAvgV = new ArrayList<AvgV>(); // 初始化效应次数适配器
						listBeanAvgV = JsonToBean_Area_Date.JsonArrayToAvgVDayTableBean(json.toString());
					} else {
						Log.w("厂房：平均电压 ---", "从PHP服务器无数据返回！");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// "未获取效应次数,请检查远程服务器IP和端口是否正确！");
								Toast.makeText(mContext, "未获取到厂房平均电压，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_SHORT).show();

							}
						});
					}
				}
				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "厂房平均电压数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "厂房平均电压数据CyclicBarrier有误2");
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
		// 图表数据设置
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y轴方向厂房效应系数
		ArrayList<String> xVals = new ArrayList<>();// X轴数据
		double AvgVTotal1, AvgVTotal2, AvgVTotal; // 一厂房，二厂房，厂房总平均电压和
		int PotS1, PotS2, PotS;
		AvgVTotal1 = AvgVSum[0] + AvgVSum[1] + AvgVSum[2];// 一厂房平均电压总和
		AvgVTotal2 = AvgVSum[3] + AvgVSum[4] + AvgVSum[5];// 二厂房平均电压总和
		AvgVTotal = AvgVTotal1 + AvgVTotal2;// 厂房平均电压总和
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // 一厂房正常槽总数
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // 二厂房正常槽总数
		PotS = PotS1 + PotS2; // 厂房正常槽总数
		// 添加数据源
		if (NormPotS[0] != 0) {
			xVals.add("一厂1区");
			yVals.add(new BarEntry((float) AvgVSum[0] / NormPotS[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("一厂2区");
			yVals.add(new BarEntry((float) AvgVSum[1] / NormPotS[1], 1));
		}else{
			xVals.add("一厂2区");//停槽
		}

		if (NormPotS[2] != 0) {
			xVals.add("一厂3区");
			yVals.add(new BarEntry((float) AvgVSum[2] / NormPotS[2], 2));
		}else{
			xVals.add("一厂3区");//停槽
		}
		if (PotS1 != 0) {
			xVals.add("一厂");
			yVals.add(new BarEntry((float) AvgVTotal1 / PotS1, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("二厂1区");
			yVals.add(new BarEntry((float) AvgVSum[3] / NormPotS[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("二厂2区");
			yVals.add(new BarEntry((float) AvgVSum[4] / NormPotS[4], 5));
		}else {
			xVals.add("二厂2区");//stop
		}
		if (NormPotS[5] != 0) {
			xVals.add("二厂3区");
			yVals.add(new BarEntry((float) AvgVSum[5] / NormPotS[5], 6));
		}else {
			xVals.add("二厂3区");//stop
		}
		if (PotS2 != 0) {
			xVals.add("二厂");
			yVals.add(new BarEntry((float) AvgVTotal2 / PotS2, 7));
		}
		if (PotS != 0) {
			xVals.add("厂房");
			yVals.add(new BarEntry((float) AvgVTotal / PotS, 8));
		}

		BarDataSet barDataSet = new BarDataSet(yVals, "区平均电压");
		barDataSet.setColor(Color.rgb(75, 92, 196));// 设置数据宝蓝颜色
		barDataSet.setDrawValues(true); // 显示数值
		barDataSet.setValueTextSize(11f);
		barDataSet.setBarSpacePercent(60f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.000");// 构造方法的字符格式这里如果小数不足2位,会以0补足.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		bardata.setHighlightEnabled(true);

		mBarChartV.setData(bardata); // 设置数据
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
					int potno = mTable.getPotNo(); // 槽号
					double avgV = mTable.getAverageV();// 平均电压
					if (potno >= 1101 && potno <= 1136) {
						AvgVSum[0] = AvgVSum[0] + avgV; // 一厂房一区平均电压总和
					} else if (potno >= 1201 && potno <= 1237) {
						AvgVSum[1] = AvgVSum[1] + avgV;
					} else if (potno >= 1301 && potno <= 1337) {
						AvgVSum[2] = AvgVSum[2] + avgV;
					} else if (potno >= 2101 && potno <= 2136) {
						AvgVSum[3] = AvgVSum[3] + avgV; // 二厂房一区平均电压总和
					} else if (potno >= 2201 && potno <= 2237) {
						AvgVSum[4] = AvgVSum[4] + avgV;
					} else if (potno >= 2301 && potno <= 2337) {
						AvgVSum[5] = AvgVSum[5] + avgV;
					}

				} else {
					Log.w("平均电压为空", "第 " + i + " 项 平均电压，为空！");
				}
			}
		}

	}

	/*
	 * private void init_GetCommData() { // 执行从远程获得日期数据 if (date_table == null)
	 * { mhttpgetdata_date = (AsyTask_HttpGetDate) new
	 * AsyTask_HttpGetDate(get_dateTable_url, this, mContext) .execute(); } if
	 * (JXList == null) { mHttpGetData_JXRecord = (AsyTask_HttpGetJXRecord) new
	 * AsyTask_HttpGetJXRecord(get_JXName_url, this, mContext).execute(); //
	 * 执行从远程获得解析记录数据 } }
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
		tvPotTotal.setText(NormPotS[0] + NormPotS[1] + NormPotS[2] + NormPotS[3] + NormPotS[4] + NormPotS[5] + "台");
	}

	private void initDATA_AE() {
		ExecutorService exec = Executors.newCachedThreadPool();
		final CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
			@Override
			public void run() {
				Log.i("效应系数数据", "获取各区效应系数数据OK，开始显示柱形图表啦，happy去");
				AeCnt_Clear();
				CalcAeCnt(listBeanAeCnt);
				ShowBar_AE();// 显示各区效应柱状图
			}
		});
		// 获取效应次数
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
						tvAeTitle.setText(AE_TITLE + mToday + " 参考值：" + AE_VALUE);
					}
				});
				List<NameValuePair> mparams = new ArrayList<NameValuePair>();
				mparams.clear();
				mparams.add(new BasicNameValuePair("areaID", "66")); // 全部槽号
				mparams.add(new BasicNameValuePair("BeginDate", todayValue));
				mparams.add(new BasicNameValuePair("EndDate", todayValue));
				JSONArrayParser jsonParser = new JSONArrayParser();
				JSONArray json = jsonParser.makeHttpRequest(getAeCntUrl, "POST", mparams);
				if (json != null) {
					Log.i("效应次数", "获取厂房效应次数OK，其他数据呢");
					listBeanAeCnt = new ArrayList<AeRecord>(); // 初始化效应次数适配器
					listBeanAeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(json.toString());

				} else {
					// 再次get效应次数数据
					json = jsonParser.makeHttpRequest(getAeCntUrl, "POST", mparams);
					if (json != null) {
						Log.i("效应次数", "厂房：效应次数ok");// 从服务器返回有数据
						listBeanAeCnt = new ArrayList<AeRecord>(); // 初始化效应次数适配器
						listBeanAeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(json.toString());
					} else {
						Log.i("厂房：效应次数 ---", "从PHP服务器无数据返回！");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// tv_title.setTextSize(14);
								// tv_title.setText("工作站:" +
								// "未获取效应次数,请检查远程服务器IP和端口是否正确！");
								Toast.makeText(mContext, "没有获取到厂房效应次数，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_SHORT).show();

							}
						});
					}
				}
				try {
					barrier.await();// 等待其他哥们
				} catch (InterruptedException e) {
					Log.e(TAG, "厂房 效应次数 数据CyclicBarrier有误1");
					Thread.currentThread().interrupt();
				} catch (BrokenBarrierException e) {

					Log.e(TAG, "厂房 效应次数 数据CyclicBarrier有误2");
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

	// 获取二厂房正常槽数量
	protected Boolean GetNormalPots2() {
		HttpPost_JsonArray jsonParser2 = new HttpPost_JsonArray();
		JSONArray json2 = jsonParser2.makeHttpRequest(getNormPots2Url, "POST");
		if (json2 != null) {
			// Log.d("二厂房：正常槽数量", json2.toString());// 从服务器返回有数据
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

	// 获取一厂房正常槽数量
	protected Boolean GetNormalPots1() {
		HttpPost_JsonArray jsonParser1 = new HttpPost_JsonArray();
		JSONArray json1 = jsonParser1.makeHttpRequest(getNormPots1Url, "POST");
		if (json1 != null) {
			// Log.d("一厂房：正常槽数量", json1.toString());// 从服务器返回有数据
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
		// 图表数据设置
		ArrayList<BarEntry> yVals = new ArrayList<>();// Y轴方向厂房效应系数
		ArrayList<String> xVals = new ArrayList<>();// X轴数据
		int AeTotal1, AeTotal2, AeTotal, PotS1, PotS2, PotS;
		AeTotal1 = AeCnt[0] + AeCnt[1] + AeCnt[2];// 一厂房效应总数
		AeTotal2 = AeCnt[3] + AeCnt[4] + AeCnt[5];// 二厂房效应总数
		AeTotal = AeTotal1 + AeTotal2;// 厂房效应总数
		PotS1 = NormPotS[0] + NormPotS[1] + NormPotS[2]; // 一厂房正常槽总数
		PotS2 = NormPotS[3] + NormPotS[4] + NormPotS[5]; // 二厂房正常槽总数
		PotS = PotS1 + PotS2; // 厂房正常槽总数
		// 添加数据源

		if (NormPotS[0] != 0) {
			xVals.add("一厂1区");
			yVals.add(new BarEntry((float) AeCnt[0] / NormPotS[0], 0));
		}
		if (NormPotS[1] != 0) {
			xVals.add("一厂2区");
			yVals.add(new BarEntry((float) AeCnt[1] / NormPotS[1], 1));
		}else{
            xVals.add("一厂2区");//停槽
        }
		if (NormPotS[2] != 0) {
			xVals.add("一厂3区");
			yVals.add(new BarEntry((float) AeCnt[2] / NormPotS[2], 2));
		}else{
			xVals.add("一厂3区");//停槽
			//yVals.add(new BarEntry((float) 0, 2));
		}
		if (PotS1 != 0) {
			xVals.add("一厂");
			yVals.add(new BarEntry((float) AeTotal1 / PotS1, 3));
		}
		if (NormPotS[3] != 0) {
			xVals.add("二厂1区");
			yVals.add(new BarEntry((float) AeCnt[3] / NormPotS[3], 4));
		}
		if (NormPotS[4] != 0) {
			xVals.add("二厂2区");
			yVals.add(new BarEntry((float) AeCnt[4] / NormPotS[4], 5));
		}else{
            xVals.add("二厂2区");//停槽
        }
		if (NormPotS[5] != 0) {
			xVals.add("二厂3区");
			yVals.add(new BarEntry((float) AeCnt[5] / NormPotS[5], 6));
		}else{
			xVals.add("二厂3区"); //停槽
			//yVals.add(new BarEntry((float) 0, 6));
		}
		if (PotS2 != 0) {
			xVals.add("二厂");
			yVals.add(new BarEntry((float) AeTotal2 / PotS2, 7));
		}
		if (PotS != 0) {
			xVals.add("厂房");
			yVals.add(new BarEntry((float) AeTotal / PotS, 8));
		}
		String lable = "正常槽  " + "<一厂1区" + NormPotS[0] + "> <一厂2区" + NormPotS[1] + "> <一厂3区" + NormPotS[2];
		lable = lable + "> <一厂 " + PotS1 + "> <二厂1区" + NormPotS[3] + "> <二厂2区" + NormPotS[4] + "> <二厂3区" + NormPotS[5]
				+ "> <二厂 " + PotS2 + ">  <厂房 " + PotS + ">";
		BarDataSet barDataSet = new BarDataSet(yVals, lable);
		barDataSet.setColor(Color.rgb(190, 0, 47));// 设置数据颜色
		barDataSet.setDrawValues(true); // 显示数值
		barDataSet.setValueTextSize(13f);
		barDataSet.setBarSpacePercent(55f);
		// barDataSet.setValueTextColor(Color.RED);
		barDataSet.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float value, Entry entry, int dataSetIndex,
					ViewPortHandler viewPortHandler) {
				DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.

				return decimalFormat.format(value);
			}
		});

		BarData bardata = new BarData(xVals, barDataSet);
		mBarChartAE.setData(bardata); // 设置数据
		handler.post(new Runnable() {
			@Override
			public void run() {
				mBarChartAE.invalidate();
			}
		});

	}

	// 计算效应次数总和
	protected void CalcAeCnt(List<AeRecord> listBean_AeCnt) {
		if (listBean_AeCnt != null && listBean_AeCnt.size() != 0) {
			AeRecord aeCnt = new AeRecord();
			for (int i = 0; i < listBean_AeCnt.size(); i++) {
				aeCnt = listBean_AeCnt.get(i);
				if (aeCnt != null) {
					int potno = aeCnt.getPotNo(); // 槽号
					int aeS = aeCnt.getWaitTime();// 效应次数

					if (potno >= 1101 && potno <= 1136) {
						AeCnt[0] = AeCnt[0] + aeS; // 一厂房一区效应次数总和
					} else if (potno >= 1201 && potno <= 1237) {
						AeCnt[1] = AeCnt[1] + aeS;
					} else if (potno >= 1301 && potno <= 1337) {
						AeCnt[2] = AeCnt[2] + aeS;
					} else if (potno >= 2101 && potno <= 2136) {
						AeCnt[3] = AeCnt[3] + aeS; // 二厂房一区效应次数总和
					} else if (potno >= 2201 && potno <= 2237) {
						AeCnt[4] = AeCnt[4] + aeS;
					} else if (potno >= 2301 && potno <= 2337) {
						AeCnt[5] = AeCnt[5] + aeS;
					}

				} else {
					Log.w("效应次数", "第 " + i + " 项 效应次数，为空！");
				}
			}
		}
	}

	// 统计正常槽数量
	protected void CalcPotsNorm(List<PotCtrl> normPotsList, int Room) {
		if (normPotsList != null && (normPotsList.size() != 0)) {
			for (int i = 0; i < normPotsList.size(); i++) {
				PotCtrl mPotCtrl = new PotCtrl();
				mPotCtrl = normPotsList.get(i);
				if (mPotCtrl != null) {
					int potno = mPotCtrl.getPotNo(); // 槽号
					int mCtrl = mPotCtrl.getCtrls(); // 槽状态字
					// 最后两位（二进制）00代表 正常，01 代表预热，10代表启动，11代表停槽
					if ((mCtrl & 0x03) == 0) {
						if (potno >= 1101 && potno <= 1136) {
							NormPotS[0]++;
						} else if (potno >= 1201 && potno <= 1237) {
							NormPotS[1]++;
						} else if (potno >= 1301 && potno <= 1337) {
							NormPotS[2]++;
						} else if (potno >= 2101 && potno <= 2136) {
							NormPotS[3]++; // 二厂房一区
						} else if (potno >= 2201 && potno <= 2237) {
							NormPotS[4]++;
						} else if (potno >= 2301 && potno <= 2337) {
							NormPotS[5]++;
						}
					}

				} else {
					Log.w("槽控制字", "槽控制字数据为NULL!");
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
			// 执行从远程获得日期数据
			AsyTask_HttpGetDate mhttpgetdata_date = (AsyTask_HttpGetDate) new AsyTask_HttpGetDate(getDateTableUrl, this,
					mContext).execute();
		}

	}

	private void init_GetJXRecord() {
		GetJXCnt++;
		if (JXList == null) {
			AsyTask_HttpGetJXRecord mHttpGetData_JXRecord = (AsyTask_HttpGetJXRecord) new AsyTask_HttpGetJXRecord(
					getJXNameUrl, this, mContext).execute(); // 执行从远程获得解析记录数据
		}

	}

	private void init() {
		Homedialog = MyProgressDialog.createDialog(mContext);
		init_normalPot(); // 初始化正常槽数量控件
		init_button();// 初始化按钮控件
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
		// 5个图表初始化
		mBarChartAE = (BarChart) mView.findViewById(R.id.Ae_chart);
		mBarChartV = (BarChart) mView.findViewById(R.id.AvgV_chart);
		mBarChartDJWD = (BarChart) mView.findViewById(R.id.DJWD_chart);
		mBarChartFZB = (BarChart) mView.findViewById(R.id.FZB_chart);
		mBarChartYHLND = (BarChart) mView.findViewById(R.id.YHLND_chart);
		// 图表标题初始化
		tvAeTitle = (TextView) mView.findViewById(R.id.tv_AeTitle);
		tvAvgVTitle = (TextView) mView.findViewById(R.id.tv_AvgV);
		tvDJWDTitle = (TextView) mView.findViewById(R.id.tv_DJWD);
		tvFZBTitle = (TextView) mView.findViewById(R.id.tv_FZB);
		tvYHLNDTitle = (TextView) mView.findViewById(R.id.tv_YHLND);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String todayValue = sdf.format(dt);
		tvAeTitle.setText(AE_TITLE + todayValue + " 参考值：" + AE_VALUE);
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
		// 获取正常槽数据
		layoutNormal.setOnClickListener(this);

	}

	// 获取正常槽数量后，才能进行获取四低一高工艺参数数据，且以图表形式显示
	private void GetAllData_ShowChart(boolean All_Charts) {
		NormPots_clear(); //
		final CountDownLatch latch = new CountDownLatch(2);// 两个工人的协作
		// 获取一厂房正常槽数据
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
				} while (i < 2);// 取正常槽数据失败，1次重试机会
				if (!FoundData1) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mContext, "未获取到一厂房正常槽数量，网络不给力或请检查远程服务器IP和端口是否正确！", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
				latch.countDown();
			}
		}).start();
		// 获取二厂房正常槽数据
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
				} while (j < 2); // 取正常槽数据失败，2次重试机会
				if (!FoundData2) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mContext, "未获取到二厂房正常槽数量，网络不给力或请检查远程服务器IP和端口是否正确！", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
				latch.countDown();
			}
		}).start();

		try {
			latch.await();// 等待所有工人完成工作

			CalcPotsNorm(NormPotsList1, 1); // 分别统计一房正常槽数量
			CalcPotsNorm(NormPotsList2, 2);// 分别统计二房正常槽数量
			if (CalcPots1 && CalcPots2) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						SetNormalPot(); // 显示正常槽数量
						// iv_Fresh_Pots.setVisibility(View.GONE);
					}
				});
			}
			if (All_Charts) {
				initDATA_AE();// 显示当前各区效应系数
				initDATA_AvgV();// 显示当前各区平均电压
				// initDATA_DJWD();// 显示当前各区电解温度
				// initDATA_FZB();// 显示当前各区分子比
				// initDATA_YHLND();// 显示当前各区氧化铝浓度
			}

		} catch (InterruptedException e) {
			Log.e(TAG, "厂房正常槽数量 latch 有误");
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
		// 图表显示设置
		// mBarChart.setTouchEnabled();
		mChart.setScaleEnabled(false); // 不放大

		mChart.getLegend().setEnabled(false);
		mChart.getLegend().setPosition(LegendPosition.BELOW_CHART_LEFT);// 设置注解的位置在左上方
		mChart.getLegend().setForm(LegendForm.CIRCLE);// 这是左边显示小图标的形状
		mChart.getLegend().setWordWrapEnabled(true);
		mChart.getLegend().setTextSize(2.8f);

		mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);// 设置X轴的位置
		mChart.getXAxis().setDrawGridLines(false);// 不显示网格
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

		mChart.getAxisRight().setEnabled(false);// 右侧不显示Y轴
		mChart.getAxisLeft().setEnabled(false);
		mChart.getAxisLeft().setDrawLabels(false); // 左侧Y坐标不显示数据刻度
		mChart.getAxisLeft().setAxisMinValue(0.0f);// 设置Y轴显示最小值，不然0下面会有空隙
		// mBarChart.getAxisLeft().setAxisMaxValue(2.0f);// 设置Y轴显示最大值
		mChart.getAxisLeft().setDrawGridLines(true);// 不设置Y轴网格

		mChart.setNoDataTextDescription("没有获取到相关图表数据");
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
							// 效应槽
						}
					}
					return true;
				}
			});
		}
		// 区平均电压，长点击
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
			tvTitle.setText("工作站:" + "网络太卡或请检查远程服务器IP和端口是否正确！");
			return false;
		} else {
			Toast.makeText(mContext, "第" + GetJXCnt + " 次尝试获取解析记录数据，请稍后再试！", Toast.LENGTH_SHORT).show();
			init_GetJXRecord();
		}

		if (GetDateCnt > 2) {
			tvTitle.setTextSize(14);
			tvTitle.setText("工作站:" + "网络太卡或请检查远程服务器IP和端口是否正确！");
			return false;
		} else {
			Toast.makeText(mContext, "第" + GetDateCnt + " 次尝试获取日期数据，请稍后再试！", Toast.LENGTH_SHORT).show();
			init_GetDate();
		}
		return true;
	}

	private void Popup_initData() {
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(mContext, "设置远程服务器", R.drawable.settings));
		titlePopup.addAction(new ActionItem(mContext, "关于", R.drawable.mm_title_btn_keyboard_normal));
	}

	public boolean initdate(Context ctx) {
		sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		if (sp != null) {
			ip = sp.getString("ipstr", ip);
			if (ip != null) {
				if (sp.getString("port", String.valueOf(port)) != null) {
					port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
				} else {
					Toast.makeText(ctx, "请设置远程服务器端口", Toast.LENGTH_SHORT).show();
					return false;
				}
			} else {
				Toast.makeText(ctx, "请设置远程服务器IP", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void GetALLDayUrl(String data) {
		// 得到日期
		if (data.equals("")) {
			// Toast.makeText(getApplicationContext(),
			// "没有获取到[日期]初始数据，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_LONG).show();
			// tv_title.setTextSize(14);
			// tv_title.setText("工作站:" + "请检查远程服务器IP和端口是否正确！");

		} else {
			dateTable = new ArrayList<String>();
			dateTable = JsonToBean_GetPublicData.JsonArrayToDate(data);
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String todayValue = sdf.format(dt);
			dateRecord = new ArrayList<String>(dateTable); // 记录日期
			dateRecord.add(0, todayValue);
		}

	}

	@Override
	public void GetJXRecordUrl(String data) {
		if (data.equals("")) {
			// tv_title.setTextSize(14);
			// tv_title.setText("工作站:" + "请检查远程服务器IP和端口是否正确！");
			// Toast.makeText(getApplicationContext(),
			// "没有获取到[解析号]初始数据，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_LONG).show();

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
			MyConst.showShare(mContext); // 一键分享
			break;
		case R.id.iv_refresh_pots:
			// 获取各区正常槽数据,效应数据，平均电压
			Homedialog.setMessage("玩命加载区槽数、效应系数、平均电压...");
			if (!Homedialog.isShowing()) {
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			GetAllData_ShowChart(false);
			break;
		case R.id.iv_refresh_ae:
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("玩命加载各区效应系数...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}

			initDATA_AE();// 显示当前各区效应系数
			break;
		case R.id.iv_refresh_avgv:
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("玩命加载各区平均电压...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_AvgV();// 显示当前各区平均电压
			break;
		case R.id.iv_refresh_djwd:
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("玩命加载各区电解温度...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_DJWD();// 显示当前各区电解温度
			break;
		case R.id.imgbtn_show_DJWDChart:
			if (mBarChartDJWD.getVisibility() == View.GONE) {
				mBarChartDJWD.setVisibility(View.VISIBLE);
				ivFreshDJWD.setVisibility(View.VISIBLE);
				imgbtnShowDJWD.setImageDrawable(getResources().getDrawable(R.drawable.up_gray));
				if (!Homedialog.isShowing()) {
					Homedialog.setMessage("玩命加载...");
					Homedialog.show();
					mHandler.sendEmptyMessageDelayed(0, 1500);
				}
				initDATA_DJWD();// 显示当前各区电解温度
			} else {
				mBarChartDJWD.setVisibility(View.GONE);
				ivFreshDJWD.setVisibility(View.INVISIBLE);
				imgbtnShowDJWD.setImageDrawable(getResources().getDrawable(R.drawable.down_gray));
			}
			break;
		case R.id.iv_refresh_fzb:
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("玩命加载各区分子比...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_FZB();// 显示当前各区分子比
			break;
		case R.id.imgbtn_show_FZBChart:
			if (mBarChartFZB.getVisibility() == View.GONE) {
				mBarChartFZB.setVisibility(View.VISIBLE);
				ivFreshFZB.setVisibility(View.VISIBLE);
				imgbtnShowFZB.setImageDrawable(getResources().getDrawable(R.drawable.up_gray));
				if (!Homedialog.isShowing()) {
					Homedialog.setMessage("玩命加载...");
					Homedialog.show();
					mHandler.sendEmptyMessageDelayed(0, 1500);
				}
				initDATA_FZB();// 显示当前各区分子比
			} else {
				mBarChartFZB.setVisibility(View.GONE);
				ivFreshFZB.setVisibility(View.INVISIBLE);
				imgbtnShowFZB.setImageDrawable(getResources().getDrawable(R.drawable.down_gray));
			}
			break;
		case R.id.iv_refresh_yhlnd:
			// 刷新氧化铝浓度
			if (!Homedialog.isShowing()) {
				Homedialog.setMessage("玩命加载各区氧化铝浓度...");
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			initDATA_YHLND();// 显示当前各区氧化铝浓度
			break;
		case R.id.imgbtn_show_YHLNDChart:
			// 显示氧化铝浓度
			if (mBarChartYHLND.getVisibility() == View.GONE) {
				mBarChartYHLND.setVisibility(View.VISIBLE);
				ivFreshYHLND.setVisibility(View.VISIBLE);
				imgbtnShowYHLND.setImageDrawable(getResources().getDrawable(R.drawable.up_gray));
				if (!Homedialog.isShowing()) {
					Homedialog.setMessage("玩命加载...");
					Homedialog.show();
					mHandler.sendEmptyMessageDelayed(0, 1500);
				}
				initDATA_YHLND();// 显示当前各区氧化铝浓度
			} else {
				mBarChartYHLND.setVisibility(View.GONE);
				ivFreshYHLND.setVisibility(View.INVISIBLE);
				imgbtnShowYHLND.setImageDrawable(getResources().getDrawable(R.drawable.down_gray));
			}
			break;
		case R.id.Normal_Layout:
			// 获取各区正常槽数据,效应数据，平均电压
			Homedialog.setMessage("玩命加载区槽数、效应系数、平均电压...");
			if (!Homedialog.isShowing()) {
				Homedialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			GetAllData_ShowChart(false);
			break;
		}
	}

}
