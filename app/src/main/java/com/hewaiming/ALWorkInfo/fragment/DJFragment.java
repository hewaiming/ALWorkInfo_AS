package com.hewaiming.ALWorkInfo.fragment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetJXRecord_Listener;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_GetPublicData;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpGetDate;
import com.hewaiming.ALWorkInfo.net.AsyTask_HttpGetJXRecord;
import com.hewaiming.ALWorkInfo.net.NetDetector;
import com.hewaiming.ALWorkInfo.ui.AbNormalMostActivity;
import com.hewaiming.ALWorkInfo.ui.Ae5DayActivity;
import com.hewaiming.ALWorkInfo.ui.AeMostActivity;
import com.hewaiming.ALWorkInfo.ui.AeRecActivity;
import com.hewaiming.ALWorkInfo.ui.AlarmRecActivity;
import com.hewaiming.ALWorkInfo.ui.AreaAeActivity;
import com.hewaiming.ALWorkInfo.ui.AreaAvgVActivity;
import com.hewaiming.ALWorkInfo.ui.CraftLineActivity;
import com.hewaiming.ALWorkInfo.ui.DayTableActivity;
import com.hewaiming.ALWorkInfo.ui.FaultMostActivity;
import com.hewaiming.ALWorkInfo.ui.FaultRecActivity;
import com.hewaiming.ALWorkInfo.ui.MeasueTableActivity;
import com.hewaiming.ALWorkInfo.ui.OperateRecActivity;
import com.hewaiming.ALWorkInfo.ui.ParamsActivity;
import com.hewaiming.ALWorkInfo.ui.PotAgeActivity;
import com.hewaiming.ALWorkInfo.ui.PotStatusActivity;
import com.hewaiming.ALWorkInfo.ui.PotVLineActivity;
import com.hewaiming.ALWorkInfo.ui.RealRecActivity;
import com.hewaiming.ALWorkInfo.ui.RealTimeLineActivity;
import com.hewaiming.ALWorkInfo.ui.SettingActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class DJFragment extends Fragment
		implements OnItemClickListener,HttpGetJXRecord_Listener, HttpGetDate_Listener {

	private String ip;
	private int port;
	private SharedPreferences sp;
	private GridView gridView;
	private Button btnMore;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> dataList;
	private List<String> dateRecord = null; // 记录日期
	private List<String> dateTable = null; // 报表日期
	private List<Map<String, Object>> JXList = null; // 记录号名

	private String getDateTableUrl = ":8000/scgy/android/odbcPhP/getDate.php";
	private String getJXNameUrl = ":8000/scgy/android/odbcPhP/getJXRecordName.php";
	private AsyTask_HttpGetDate mhttpgetdata_date;
	private AsyTask_HttpGetJXRecord mHttpGetData_JXRecord;
	private Context mContext;
	// private TitlePopup titlePopup;
	private TextView tvTitle;
	private ImageView ivShare;	

	private int GetDateCnt = 0, GetJXCnt = 0;
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_dj, container, false);
		Log.i("fragment", "create DJ fragment view");	
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		init(); // 初始化各控件		
		if (NetStatus() != 0) {
			if (!initdate(mContext)) { // 取远程服务器地址和端口
				Intent intent = new Intent(getActivity(), SettingActivity.class);			
				startActivity(intent);// 没有设置远程服务器ip和端口
			} else {
				getDateTableUrl = "http://" + ip + getDateTableUrl;
				getJXNameUrl = "http://" + ip + getJXNameUrl;				
				init_GetDate();
				init_GetJXRecord();				
			}

		} else {
			Toast.makeText(mContext, "网络异常！", Toast.LENGTH_LONG).show();
		}

	}


	
	private int NetStatus() {
		NetDetector netDetector = new NetDetector(getActivity(),false);
		return netDetector.isConnectingToInternet();

	}

	private void init_GetDate() {
		GetDateCnt++;
		if (dateTable == null) {
			// 执行从远程获得日期数据
			mhttpgetdata_date = (AsyTask_HttpGetDate) new AsyTask_HttpGetDate(getDateTableUrl, this, mContext)
					.execute();
		}

	}

	private void init_GetJXRecord() {
		GetJXCnt++;
		if (JXList == null) {
			mHttpGetData_JXRecord = (AsyTask_HttpGetJXRecord) new AsyTask_HttpGetJXRecord(getJXNameUrl, this,
					mContext).execute(); // 执行从远程获得解析记录数据
		}

	}

	private void init() {
		ivShare = (ImageView) mView.findViewById(R.id.iv_share);
		ivShare.setVisibility(View.GONE);
		// ivShare.setOnClickListener(this);
		gridView = (GridView) mView.findViewById(R.id.gridView);
		dataList = new ArrayList<Map<String, Object>>();
		adapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_action, new String[] { "pic", "name" },new int[] { R.id.pic, R.id.name });
		gridView.setAdapter(adapter);		
		gridView.setOnItemClickListener(this);
		// titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// Popup_initData();
		btnMore = (Button) mView.findViewById(R.id.btn_more);
		btnMore.setVisibility(View.GONE);
		// btnMore.setOnClickListener(this);
		tvTitle = (TextView) mView.findViewById(R.id.tv_title);
		tvTitle.setText("电解槽信息");
		// iv_wifi = (ImageView) findViewById(R.id.iv_NoWiFi);
		// bannerView = (com.hewaiming.ALWorkInfo.banner.SlideShowView)
		// findViewById(R.id.bannerwView);
		// mBarChart = (BarChart) findViewById(R.id.Ae_chart);
		// tv_aeTitle = (TextView) findViewById(R.id.tv_AeTitle);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String todayValue = sdf.format(dt);
		// tv_aeTitle.setText("工区日效应系数：" + todayValue);
		// init_BarCHART();

	}	

	private List<Map<String, Object>> getData() {

		for (int i = 0; i < MyConst.drawable.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pic", MyConst.drawable[i]);
			map.put("name", MyConst.iconName[i]);
			dataList.add(map);
		}
		Log.i("Main", "size=" + dataList.size());
		return dataList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		boolean DoRun = false;
		if (position == 0 || position == 1) {
			DoRun = true;
		}
		if ((JXList != null && dateRecord != null) || DoRun) {
			switch (position) {
			case 8:
				Intent Paramsintent = new Intent(getActivity(), ParamsActivity.class);
				Bundle paramBundle = new Bundle();
				paramBundle.putString("ip", ip);
				paramBundle.putInt("port", port);
				Paramsintent.putExtras(paramBundle);
				startActivity(Paramsintent); // 常用参数
				break;
			case 15:
				Intent DayTable_intent = new Intent(getActivity(), DayTableActivity.class);
				Bundle DayTablebundle = new Bundle();
				DayTablebundle.putStringArrayList("date_table", (ArrayList<String>) dateTable);
				DayTablebundle.putSerializable("JXList", (Serializable) JXList);
				DayTablebundle.putString("ip", ip);
				DayTablebundle.putInt("port", port);
				DayTable_intent.putExtras(DayTablebundle);
				startActivity(DayTable_intent); // 槽日报
				break;
			case 2:
				Intent Ae5day_intent = new Intent(getActivity(), Ae5DayActivity.class);
				Bundle bundle_Ae5 = new Bundle();
				bundle_Ae5.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				bundle_Ae5.putSerializable("JXList", (Serializable) JXList);
				bundle_Ae5.putString("ip", ip);
				bundle_Ae5.putInt("port", port);
				Ae5day_intent.putExtras(bundle_Ae5);
				startActivity(Ae5day_intent); // 效应情报表
				break;
			case 3:
				Intent Potage_intent = new Intent(getActivity(), PotAgeActivity.class);
				Bundle PotageBundle = new Bundle();
				PotageBundle.putString("ip", ip);
				PotageBundle.putInt("port", port);
				Potage_intent.putExtras(PotageBundle);
				startActivity(Potage_intent); // 槽龄表
				break;
			case 4:
				Intent potv_intent = new Intent(getActivity(), PotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putString("PotNo", "");
				potv_bundle.putString("Begin_Date", "");
				potv_bundle.putString("End_Date", "");
				potv_bundle.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				potv_bundle.putSerializable("JXList", (Serializable) JXList);
				potv_bundle.putString("ip", ip);
				potv_bundle.putInt("port", port);
				potv_intent.putExtras(potv_bundle);
				startActivity(potv_intent); // 槽压曲线
				break;
			case 5:
				Intent faultRec_intent = new Intent(getActivity(), FaultRecActivity.class);
				Bundle bundle_faultRec = new Bundle();
				bundle_faultRec.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				bundle_faultRec.putBoolean("Hide_Action", false);
				bundle_faultRec.putString("PotNo", "1101");
				bundle_faultRec.putString("Begin_Date", dateRecord.get(0));
				bundle_faultRec.putString("End_Date", dateRecord.get(0));
				bundle_faultRec.putSerializable("JXList", (Serializable) JXList);
				bundle_faultRec.putString("ip", ip);
				bundle_faultRec.putInt("port", port);
				faultRec_intent.putExtras(bundle_faultRec);
				startActivity(faultRec_intent); // 故障记录
				break;
			case 6:
				Intent realRec_intent = new Intent(getActivity(), RealRecActivity.class);
				Bundle realbundle = new Bundle();
				realbundle.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				realbundle.putSerializable("JXList", (Serializable) JXList);
				realbundle.putString("ip", ip);
				realbundle.putInt("port", port);
				realRec_intent.putExtras(realbundle);
				startActivity(realRec_intent); // 实时记录
				break;
			case 7:
				Intent operate_intent = new Intent(getActivity(), OperateRecActivity.class);
				Bundle operateBundle = new Bundle();
				operateBundle.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				operateBundle.putString("ip", ip);
				operateBundle.putInt("port", port);
				operate_intent.putExtras(operateBundle);
				startActivity(operate_intent); // 操作记录
				break;
			case 0:
				Intent PotStatus_intent = new Intent(getActivity(), PotStatusActivity.class);
				Bundle PotStatusBundle = new Bundle();				
				PotStatusBundle.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				PotStatusBundle.putSerializable("JXList", (Serializable) JXList);
				PotStatusBundle.putString("ip", ip);
				PotStatusBundle.putInt("port", port);
				PotStatus_intent.putExtras(PotStatusBundle);
				startActivity(PotStatus_intent); // 槽状态表
				break;
			case 9:
				Intent measue_intent = new Intent(getActivity(), MeasueTableActivity.class);
				Bundle measueBundle = new Bundle();
				measueBundle.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				measueBundle.putString("ip", ip);
				measueBundle.putInt("port", port);
				measue_intent.putExtras(measueBundle);
				startActivity(measue_intent); // 测量数据
				break;

			case 10:
				Intent aemost_intent = new Intent(getActivity(), AeMostActivity.class);
				Bundle aemostBundle = new Bundle();
				aemostBundle.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				aemostBundle.putSerializable("JXList", (Serializable) JXList);
				aemostBundle.putString("ip", ip);
				aemostBundle.putInt("port", port);
				aemost_intent.putExtras(aemostBundle);
				startActivity(aemost_intent); // 效应槽
				break;
			case 11:
				Intent aeRec_intent = new Intent(getActivity(), AeRecActivity.class);
				Bundle bundle_AeRec = new Bundle();
				bundle_AeRec.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				bundle_AeRec.putBoolean("Hide_Action", false);
				bundle_AeRec.putString("PotNo", "1101");
				bundle_AeRec.putString("Begin_Date", dateRecord.get(0));
				bundle_AeRec.putString("End_Date", dateRecord.get(0));
				bundle_AeRec.putSerializable("JXList", (Serializable) JXList);
				bundle_AeRec.putString("ip", ip);
				bundle_AeRec.putInt("port", port);
				aeRec_intent.putExtras(bundle_AeRec);
				startActivity(aeRec_intent); // 效应记录
				break;
			case 12:
				Intent faultmost_intent = new Intent(getActivity(), FaultMostActivity.class);
				Bundle bundle_faultmost = new Bundle();
				bundle_faultmost.putSerializable("JXList", (Serializable) JXList);
				bundle_faultmost.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				bundle_faultmost.putString("ip", ip);
				bundle_faultmost.putInt("port", port);
				faultmost_intent.putExtras(bundle_faultmost);
				startActivity(faultmost_intent); // 故障率排序
				break;
			case 13:
				Intent craft_intent = new Intent(getActivity(), CraftLineActivity.class);
				Bundle craftBundle = new Bundle();
				craftBundle.putStringArrayList("date_table", (ArrayList<String>) dateTable);
				craftBundle.putString("PotNo_Selected", "1101");
				craftBundle.putString("ip", ip);
				craftBundle.putInt("port", port);
				craft_intent.putExtras(craftBundle);
				startActivity(craft_intent); // 工艺曲线
				break;
			case 14:
				Intent alarmRec_intent = new Intent(getActivity(), AlarmRecActivity.class);
				Bundle bundle_alarm = new Bundle();
				bundle_alarm.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				bundle_alarm.putSerializable("JXList", (Serializable) JXList);
				bundle_alarm.putString("ip", ip);
				bundle_alarm.putInt("port", port);
				alarmRec_intent.putExtras(bundle_alarm);
				startActivity(alarmRec_intent); // 报警记录
				break;
			case 16:
				Intent abnormalmost_intent = new Intent(getActivity(), AbNormalMostActivity.class);
				Bundle bundle_abnormalmost = new Bundle();
				bundle_abnormalmost.putSerializable("JXList", (Serializable) JXList);
				bundle_abnormalmost.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				bundle_abnormalmost.putString("ip", ip);
				bundle_abnormalmost.putInt("port", port);
				abnormalmost_intent.putExtras(bundle_abnormalmost);
				startActivity(abnormalmost_intent); // 下料异常频繁排序
				break;
			case 17:
				Intent areaAvgV_intent = new Intent(getActivity(), AreaAvgVActivity.class);
				Bundle bundle_areaAvgV = new Bundle();
				bundle_areaAvgV.putSerializable("JXList", (Serializable) JXList);
				bundle_areaAvgV.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				bundle_areaAvgV.putString("ip", ip);
				bundle_areaAvgV.putInt("port", port);
				areaAvgV_intent.putExtras(bundle_areaAvgV);
				startActivity(areaAvgV_intent); // 区平均电压
				break;
			case 18:
				Intent areaAe_intent = new Intent(getActivity(), AreaAeActivity.class);
				Bundle bundle_areaAe = new Bundle();
				bundle_areaAe.putSerializable("JXList", (Serializable) JXList);
				bundle_areaAe.putStringArrayList("date_record", (ArrayList<String>) dateRecord);
				bundle_areaAe.putString("ip", ip);
				bundle_areaAe.putInt("port", port);
				areaAe_intent.putExtras(bundle_areaAe);
				startActivity(areaAe_intent); // 区效应系数
				break;
			case 1:
				Intent realtime_intent = new Intent(getActivity(), RealTimeLineActivity.class);
				Bundle bundle_realtime = new Bundle();
				// bundle_realtime.putStringArrayList("date_record",
				// (ArrayList<String>) date_record);
				bundle_realtime.putBoolean("Hide_Action", false);
				bundle_realtime.putString("PotNo", "1101");
				bundle_realtime.putString("ip", ip);
				bundle_realtime.putInt("port", port);
				// realtime_intent.putStringArrayListExtra("date_table",
				// (ArrayList<String>) date_table);
				realtime_intent.putExtras(bundle_realtime);
				startActivity(realtime_intent); // 实时曲线
				break;
			}
		} else {
			// Toast.makeText(mContext, "请稍后再点击，数据初始化....", 1).show();
			// 从此初始化日期和解析记录数据

			if (GetJXCnt > 3) {
				tvTitle.setTextSize(12);
				tvTitle.setText("工作站:" + "网络不给力或请检查远程服务器IP和端口是否正确！");
				// Toast.makeText(getApplicationContext(), "第" + GetJXCnt +
				// "次尝试获取解析记录数据失败，请检查远程服务器IP和端口是否正确！", //
				// Toast.LENGTH_LONG).show();
			} else {
				init_GetJXRecord();
				Toast.makeText(mContext, "第" + GetJXCnt + " 次尝试获取解析记录数据", Toast.LENGTH_SHORT).show();
			}

			if (GetDateCnt > 3) {
				// Toast.makeText(getApplicationContext(), "第" + GetDateCnt + "
				// 次尝试获取日期失败，请检查远程服务器IP和端口是否正确！", Toast.LENGTH_LONG).show();
				tvTitle.setTextSize(14);
				tvTitle.setText("工作站:" + "网络不给力或请检查远程服务器IP和端口是否正确！");
			} else {
				Toast.makeText(mContext, "第" + GetDateCnt + " 次尝试获取日期数据", Toast.LENGTH_SHORT).show();
				init_GetDate();
			}

		}
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

}
