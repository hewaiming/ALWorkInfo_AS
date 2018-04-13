package com.hewaiming.ALWorkInfo.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.File;
import java.text.SimpleDateFormat;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.config.MyApplication;
import com.hewaiming.ALWorkInfo.config.PermisionUtils;
import com.hewaiming.ALWorkInfo.fragment.DJFragment;
import com.hewaiming.ALWorkInfo.fragment.HomeFragment;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import cn.sharesdk.framework.ShareSDK;
import io.github.ylbfdev.slideshowview.utils.FileCache;

public class MainActivity extends FragmentActivity {

	private HomeFragment mHomeFragment;
	private DJFragment mDJFragment;
	private Button[] mTabs;
	private Fragment[] fragments;
	private ImageView iv_home_tips;
	private ImageView iv_dj_tips;
	private int index;
	private int currentTabIndex;
	private Context mContext;
	private boolean verifyPass=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);
		// 检测读写权限
		if (Build.VERSION.SDK_INT >= 23 && !verifyPass) {
			PermisionUtils.verifyStoragePermissions(MainActivity.this);
			verifyPass=true;
		}
		mContext = this;
		FileCache mFileCache = new FileCache(mContext);
		// mFileCache.clear(); //清除本地SDCARD图片缓存
		ShareSDK.initSDK(this); // 初始化ShareSDK(一键分享)
		initView();
		initTab();
	}

	private void initView() {
		mTabs = new Button[2];
		mTabs[0] = (Button) findViewById(R.id.btn_home);
		mTabs[1] = (Button) findViewById(R.id.btn_dj);

		iv_home_tips = (ImageView) findViewById(R.id.iv_home_tips);
		iv_home_tips.setVisibility(View.GONE);
		iv_dj_tips = (ImageView) findViewById(R.id.iv_dj_tips);
		iv_dj_tips.setVisibility(View.GONE);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
	}

	private void initTab() {
		mHomeFragment = new HomeFragment();
		mDJFragment = new DJFragment();

		fragments = new Fragment[] { mHomeFragment, mDJFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, mDJFragment)
				.add(R.id.fragmentContainer, mHomeFragment).hide(mDJFragment).show(mHomeFragment).commit();
	}

	public void onTabSelect(View view) {
		switch (view.getId()) {
		case R.id.btn_home:
			index = 0;
			break;
		case R.id.btn_dj:
			index = 1;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragmentContainer, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog_exit();
			return true;
		}
		return true;
	}

	protected void dialog_exit() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				verifyPass=false;
				ShareSDK.stopSDK(); // 关闭一键分享
				dialog.dismiss();
				MyApplication.getInstance().exit();

			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

}
