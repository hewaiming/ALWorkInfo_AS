package com.hewaiming.ALWorkInfo.ui;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.Share.AndroidShare;
import com.hewaiming.ALWorkInfo.Update.UpdateManager;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.MyConst;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends Activity {
	private Button btnCheck, btnFinish,btnShare;

	private String TAG = "=Setting=";
	private TextView tv_title, tv_ID,tv_date;
	private Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_about);
		MyApplication.getInstance().addActivity(this);
		ctx=this;
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("关于");
		tv_ID=(TextView) findViewById(R.id.tv_id);
		tv_date=(TextView) findViewById(R.id.tv_datetime);
		tv_date.setText(R.string.publicDate);
		String myId;
		try {
			myId = getVersionName();
			tv_ID.setText("版本号："+myId);
		} catch (Exception e) {			
			Log.e("版本号", "版本号 Exception");
			tv_ID.setText("版本号：未知");
		}		
		btnShare=(Button) findViewById(R.id.btn_share);
		btnCheck = (Button) findViewById(R.id.btn_check_ver);		
		btnFinish=(Button) findViewById(R.id.btn_back);		
		btnFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});

		btnCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				UpdateManager manager = new UpdateManager(AboutActivity.this,true);				
				manager.checkUpdate();
			}
		});
		//分享按钮
		btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/*AndroidShare as = new AndroidShare(
						AboutActivity.this,
						"我们现在通过使用安卓版工作站APP，了解槽信息！",
						"http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
				as.show();*/
				 MyConst.showShare(ctx);
				
			}
		});
	}
	
	private String getVersionName() throws Exception  
	{  
	        // 获取packagemanager的实例  
	        PackageManager packageManager = getPackageManager();  
	        // getPackageName()是你当前类的包名，0代表是获取版本信息  
	        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);  
	        String version = packInfo.versionName;  	      
	        return version;  
	}  

}
