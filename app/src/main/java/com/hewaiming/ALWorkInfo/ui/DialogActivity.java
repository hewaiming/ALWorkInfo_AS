package com.hewaiming.ALWorkInfo.ui;


import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.config.MyApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author yangyu
 *	功能描述：弹出Activity界面
 */
public class DialogActivity extends Activity implements OnClickListener{
	private LinearLayout layout01,layout02,layout03,layout04;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	/**
	 * 初始化组件
	 */
	private void initView(){
		//得到布局组件对象并设置监听事件
		layout01 = (LinearLayout)findViewById(R.id.llayout01);
		layout02 = (LinearLayout)findViewById(R.id.llayout02);
		layout03 = (LinearLayout)findViewById(R.id.llayout03);
		layout04 = (LinearLayout)findViewById(R.id.llayout04);

		layout01.setOnClickListener(this);
		layout02.setOnClickListener(this);
		layout03.setOnClickListener(this);
		layout04.setOnClickListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llayout01:
			 Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
			break;
		}
	}
}
