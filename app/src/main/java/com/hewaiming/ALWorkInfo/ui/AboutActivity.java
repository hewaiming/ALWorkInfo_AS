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
		tv_title.setText("����");
		tv_ID=(TextView) findViewById(R.id.tv_id);
		tv_date=(TextView) findViewById(R.id.tv_datetime);
		tv_date.setText(R.string.publicDate);
		String myId;
		try {
			myId = getVersionName();
			tv_ID.setText("�汾�ţ�"+myId);
		} catch (Exception e) {			
			Log.e("�汾��", "�汾�� Exception");
			tv_ID.setText("�汾�ţ�δ֪");
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
		//����ť
		btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/*AndroidShare as = new AndroidShare(
						AboutActivity.this,
						"��������ͨ��ʹ�ð�׿�湤��վAPP���˽����Ϣ��",
						"http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
				as.show();*/
				 MyConst.showShare(ctx);
				
			}
		});
	}
	
	private String getVersionName() throws Exception  
	{  
	        // ��ȡpackagemanager��ʵ��  
	        PackageManager packageManager = getPackageManager();  
	        // getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ  
	        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);  
	        String version = packInfo.versionName;  	      
	        return version;  
	}  

}
