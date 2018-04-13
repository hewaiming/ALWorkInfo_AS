package com.hewaiming.ALWorkInfo.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.config.ImageConfig;
import com.hewaiming.ALWorkInfo.config.ImageLoadOptions;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.PermisionUtils;
import com.hewaiming.ALWorkInfo.fragment.HomeFragment;
import com.hewaiming.ALWorkInfo.net.NetDetector;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
	// private Animation animation;
	private Button okBtn;
	private ImageView mImage;
	private DisplayImageOptions options;
	private Boolean runTimer = true;
	private Timer timer;
	private Context ctx;
	private SharedPreferences sp;
	private String ip;
	private boolean firstInit = false;
	private static String image_url = ":8000/scgy/android/banner/face.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		if (!firstInit) {
			firstInit = true;
			ctx = this;
			MyApplication.getInstance().addActivity(this);
			okBtn = (Button) findViewById(R.id.btn_ok);
			okBtn.getBackground().setAlpha(50); // 背景透明
			mImage = (ImageView) findViewById(R.id.welcome);
			ImageConfig config = new ImageConfig(this);
			config.initImageLoader(ctx);
			new ImageLoadOptions();
			options = ImageLoadOptions.getOptions();

			NetDetector netDetector = new NetDetector(getApplicationContext(), true); // 判断是否有WIFI
			sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
			if (sp != null) {
				ip = sp.getString("ipstr", ip);
				if (ip != null) {
					image_url = "http://" + ip + image_url;
					if (netDetector.isConnectingToInternet() == 1) {
						ImageLoader.getInstance().displayImage(image_url, mImage, options);
					}

				} else {
					Toast.makeText(ctx, "请设置远程服务器IP和端口", 1).show();
				}
			} else {
				Toast.makeText(ctx, "请设置远程服务器IP和端口", 1).show();
			}
		
			timer = new Timer();
			okBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					timer.cancel();
					Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
					finish();
				}
			});
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}, 3000);

		}
	}
}
