package com.hewaiming.ALWorkInfo.ui;

import java.io.Serializable;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.Popup.MyProgressDialog;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_DayTableAdapter;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_PotStatusAdapter;
import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.ALWorkInfo.config.DemoBase;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.net.NetDetector;
import com.hewaiming.ALWorkInfo.socket.SocketTransceiver;
import com.hewaiming.ALWorkInfo.socket.TcpClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bean.PotStatus;
import bean.PotStatusDATA;
import bean.RealTime;
import bean.RequestAction;

public class PotStatusActivity extends DemoBase implements OnScrollListener, OnClickListener {
	private String ip;
	private int port;
	private List<String> dateBean = new ArrayList<String>();
	// private SharedPreferences sp;
	private Spinner spinner_area;
	private Button backBtn, refreshBtn;
	// private ImageButton isShowingBtn;
	private TextView tv_title, tv_SysV, tv_SysI, tv_RoomV;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter;

	private String PotNo, BeginDate, EndDate;
	private List<PotStatus> listBean = new ArrayList<PotStatus>();

	private RelativeLayout mHead;
	private ListView lv_PotStatus;
	private LinearLayout showArea = null;
	// private View layout_PotStatus;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private Context mContext;
	protected HSView_PotStatusAdapter PotStatus_Adapter = null;
	private Timer timerPotStatus = null;
	private TimerTask timerTaskPotStatus = null;

	private Handler handler = new Handler(Looper.getMainLooper());

	protected MyProgressDialog dialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dialog.dismiss();
		};
	};

	private TcpClient client = new TcpClient() {

		@Override
		public void onConnect(SocketTransceiver transceiver) {

		}

		@Override
		public void onConnectFailed() {
			handler.post(new Runnable() {
				@Override
				public void run() {
					tv_title.setText("连接失败！检查服务器IP或网络正常？");

				}
			});

		}

		@Override
		public void onReceive(SocketTransceiver transceiver, RealTime realTime) {

		}

		@Override
		public void onDisconnect(SocketTransceiver transceiver) {
			transceiver.stop();
		}

		@Override
		public void onReceive(SocketTransceiver transceiver, final PotStatusDATA potStatus) {
			if (potStatus == null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						tv_title.setText("槽状态表：没有获取到槽状态数据！");
					}
				});

			} else {
				// 有数据情况
				handler.post(new Runnable() {
					@Override
					public void run() {
						tv_title.setText("槽状态表");
						tv_SysV.setText((potStatus.getSysV()) / 10.0 + "");
						tv_SysI.setText((potStatus.getSysI()) / 100.0 + "");
						tv_RoomV.setText((potStatus.getRoomV()) / 100.0 + "");
						listBean = new ArrayList<PotStatus>(potStatus.getPotData());
						if (listBean.size() > 0) {
							// listBean.clear(); // 清除LISTVIEW 以前的内容
							PotStatus_Adapter.onDateChange(listBean);
						}

					}
				});
			}
		}

	};

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_potstatus);
		// 第一种，是在主线程中直接忽略，强制执行。（不推荐这种方法，但是该方法修改起来简单）
		/*
		 * if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy
		 * policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 * StrictMode.setThreadPolicy(policy); }
		 */
		MyApplication.getInstance().addActivity(this);
		GetDataFromIntent();
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String todayValue = sdf.format(dt);
		BeginDate = todayValue;
		EndDate = todayValue;
		mContext = this;
		init_area();
		init_title();
		init_HSView();
		init_listview();
		PotStatus_Adapter = new HSView_PotStatusAdapter(mContext, R.layout.item_hsview_potstatus, listBean, mHead);
		lv_PotStatus.setAdapter(PotStatus_Adapter);
		timerPotStatus = new Timer();
		connect();
		SendActionToServer();
		if (!MyConst.GetDataFromSharePre(mContext, "PotStatus_Show")) {
			MyConst.GuideDialog_show(mContext, "PotStatus_Show"); // 第一次显示
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();		
		if (timerPotStatus != null) {
			timerPotStatus.cancel();
			if (client != null && client.isConnected()) { client.disconnect(); }
		}

	}

	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip = getIntent().getStringExtra("ip");
		port = getIntent().getIntExtra("port", 1234);
	}

	private void init_listview() {
		lv_PotStatus = (ListView) findViewById(R.id.lv_PotStatus);
		lv_PotStatus.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_PotStatus.setCacheColorHint(0);
		lv_PotStatus.setOnScrollListener(this);

		lv_PotStatus.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				PotNo = String.valueOf(listBean.get(position).getPotNo());
				// Toast.makeText(getApplicationContext(), PotNo, 1).show();
				Intent potv_intent = new Intent(PotStatusActivity.this, PotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putString("PotNo", PotNo);
				potv_bundle.putString("Begin_Date", BeginDate);
				potv_bundle.putString("End_Date", EndDate);
				potv_bundle.putSerializable("JXList", (Serializable) JXList);
				potv_bundle.putStringArrayList("date_record", (ArrayList<String>) dateBean);
				potv_bundle.putString("ip", ip);
				potv_bundle.putInt("port", port);
				potv_intent.putExtras(potv_bundle);
				startActivity(potv_intent); // 槽压曲线图
				return true;
			}
		});

	}

	private void init_HSView() {
		mHead = (RelativeLayout) findViewById(R.id.head); // 表头处理
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setBackgroundColor(Color.parseColor("#fffff7"));
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

	}

	private void init_title() {
		// layout_daytable = findViewById(R.id.Layout_daytable);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("槽状态表");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);
		tv_SysV = (TextView) findViewById(R.id.tv_sysV);
		tv_SysI = (TextView) findViewById(R.id.tv_sysI);
		tv_RoomV = (TextView) findViewById(R.id.tv_RoomV);
		dialog = MyProgressDialog.createDialog(mContext);
		refreshBtn = (Button) findViewById(R.id.btn_more);
		refreshBtn.setBackgroundResource(R.drawable.refresh_white);
		refreshBtn.setVisibility(View.VISIBLE);
		refreshBtn.setOnClickListener(this);
		// isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		// showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		// isShowingBtn.setOnClickListener(this);

	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);

		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// 设置下拉列表的风格
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_area.setAdapter(Area_adapter);
		spinner_area.setVisibility(View.VISIBLE);
		spinner_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (timerTaskPotStatus != null) {
					timerTaskPotStatus.cancel();
				}
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
				// 显示进程提示对话框
				dialog.setMessage("玩命加载槽状态数据...");
				if (!dialog.isShowing()) {
					dialog.show();
					mHandler.sendEmptyMessageDelayed(0, 1500);
				}
				ResetListView();
				SendActionToServer();//
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			if (timerPotStatus != null) {
				timerPotStatus.cancel();
			}
			if (client != null) {
				client.disconnect();
			}
			if (lv_PotStatus != null) {
				lv_PotStatus.destroyDrawingCache();
			}
			finish();
			break;
		case R.id.btn_more:
			// 刷新槽状态表数据
			ResetListView();
			// 显示进程提示对话框
			dialog.setMessage("玩命加载槽状态数据...");
			if (!dialog.isShowing()) {
				dialog.show();
				mHandler.sendEmptyMessageDelayed(0, 1500);
			}
			connect();
			SendActionToServer();
			break;
		}
	}

	private void ResetListView() {
		if (PotStatus_Adapter != null) {
			listBean.clear();
			PotStatus_Adapter.onDateChange(listBean);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		public boolean onTouch(View arg0, MotionEvent arg1) {
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	private void connect() {
		if (client.isConnected()) {
			// 断开连接
			client.disconnect();
		} /*
			 * else { try { client.connect(ip, port); } catch
			 * (NumberFormatException e) { Toast.makeText(this, "端口错误",
			 * Toast.LENGTH_SHORT).show(); e.printStackTrace(); } }
			 */
		try {
			client.connect(ip, port);
		} catch (NumberFormatException e) {
			Toast.makeText(this, "端口错误", Toast.LENGTH_SHORT).show();
		}
	}

	private void SendActionToServer() {
		if (timerPotStatus == null) {
			timerPotStatus = new Timer();
		}
		if (timerTaskPotStatus != null) {
			timerTaskPotStatus.cancel();
		}
		timerTaskPotStatus = new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						sendPotStatusAction();
					}
				});

			}
		};
		timerPotStatus.schedule(timerTaskPotStatus, 0, 2000);

	}

	private void sendPotStatusAction() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					RequestAction action = new RequestAction();
					action.setActionId(2);
					action.setPotNo_Area(String.valueOf(areaId));
					if (action != null && client.isConnected())
						client.getTransceiver().send(action);
				} catch (Exception e) {
					Log.e("sendPotStatusAction", "sendPotStatusAction Exception");
				}

			}
		}).start();
		/*
		 * RequestAction action = new RequestAction(); action.setActionId(2);
		 * action.setPotNo_Area(String.valueOf(areaId));
		 * client.getTransceiver().send(action);
		 */
	}

}
