package com.hewaiming.ALWorkInfo.ui;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.PermisionUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {
	private Button btnsave, btnFinish;
	private AutoCompleteTextView edtip;
	private EditText edtport;
	SharedPreferences sp;
	private String TAG = "=Setting=";
	private TextView tv_title;
	private Context ctx;
	private String ip;
	private int port;
	private String[] IPAddress = { "125.64.59.11", "218.203.253.168" };
	protected boolean savePass=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		MyApplication.getInstance().addActivity(this);	
		ctx = this;
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("远程服务器设置");

		btnsave = (Button) findViewById(R.id.btn_save);
		edtip = (AutoCompleteTextView) findViewById(R.id.et_IP);

		ArrayAdapter<String> av = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
				IPAddress);
		edtip.setAdapter(av);

		edtport = (EditText) findViewById(R.id.et_PORT);
		sp = this.getSharedPreferences("SP", MODE_PRIVATE);
		if (sp != null) {
			edtip.setText(sp.getString("ipstr", "125.64.59.11"));
			edtport.setText(sp.getString("port", "1234"));
		}
		btnFinish = (Button) findViewById(R.id.btn_back);
		btnFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		btnsave.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// Log.i(TAG, "锟斤拷始锟睫革拷");
				String ip = edtip.getText().toString();//
				String port = edtport.getText().toString();//
				Editor editor = sp.edit();
				editor.putString("ipstr", ip);
				editor.putString("port", port);
				if (editor.commit() && !savePass) {
					// Toast.makeText(getApplicationContext(),
					// "远程服务器设置成功,将重启程序！", 0).show();
					savePass=true;
					//Intent i = getBaseContext().getPackageManager()
					//		.getLaunchIntentForPackage(getBaseContext().getPackageName());
					Intent i = new Intent(SettingActivity.this, MainActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i); // 第一种重启程序
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "远程服务器设置失败", 1).show();
				}			

			}
		});
	}

	public void initdate() {
		sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		if (sp != null) {
			ip = sp.getString("ipstr", ip);
			port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
		}
		// MyLog.i(TAG, "获取到ip端口:" + ip + ";" + port);
	}
}
