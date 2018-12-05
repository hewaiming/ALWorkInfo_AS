package com.hewaiming.ALWorkInfo.Update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import com.hewaiming.ALWorkInfo.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateManager {

	private static final int DOWNLOAD = 1;

	private static final int DOWNLOAD_FINISH = 2;
	
	private static final int NO_UPDATE = 3;
	private static final int GET_UNDATAINFO_ERROR=4;
	private static final int CAN_UPDATE=5;
	private static final String TAG = "UpdateManager";

	HashMap<String, String> mHashMap;

	private String mSavePath;

	private int progress;

	private boolean cancelUpdate = false;

	private Context mContext;
	private boolean IsShow=true;

	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	//private String update_info="槽状态表增加（功能控制字）数据等";	
	UpdataInfo info=null;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CAN_UPDATE:
				showNoticeDialog();
				break;
			case DOWNLOAD:
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				installApk();
				break;
			case NO_UPDATE:
				if (IsShow){					
					Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
				}				
				break;
			case GET_UNDATAINFO_ERROR:
				Toast.makeText(mContext, "获取最新版本信息失败！", Toast.LENGTH_LONG).show();
				break;	
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context,Boolean IsShow) {
		this.mContext = context;
		this.IsShow=IsShow;
	}

	/**
	 * 妫�娴嬭蒋浠舵洿鏂�
	 */
	public void checkUpdate() {

		new CheckVersionTask().start();
	}

	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo("com.hewaiming.ALWorkInfo", 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "NameNotFoundException");
		}
		return versionCode;
	}
	
	private String getVersionName(Context context) {	
		
		String versionName ="0" ;
		try {
			versionName = context.getPackageManager().getPackageInfo("com.hewaiming.ALWorkInfo", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "NameNotFoundException");
		}
		return versionName;
	}


	private void showNoticeDialog() {

		AlertDialog.Builder builder = new Builder(mContext);		
		builder.setTitle(R.string.soft_update_title);
		
		builder.setMessage(info.getInfo());

		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				showDownloadDialog();
			}
		});

		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	private void showDownloadDialog() {

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);

		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		downloadApk();
	}

	/**
	 *下载文件
	 */
	private void downloadApk() {

		new downloadApkThread().start();
	}

	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					//URL url = new URL(mHashMap.get("url"));
					URL url = new URL(info.getUrl());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					File file = new File(mSavePath);

					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, info.getFilename());
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;

					byte buf[] = new byte[1024];

					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException e) {
				Log.e(TAG, "IOException");
			}
			mDownloadDialog.dismiss();
		}
	};

	// 安装APK
	private void installApk() {		
		File apkfile = new File(mSavePath, info.getFilename());
		if (!apkfile.exists()) {
			return;
		}

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");

		mContext.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/*
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
	 */
	public static UpdataInfo getUpdataInfo(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");// 设置解析的数据源
		int type = parser.getEventType();
		UpdataInfo info = new UpdataInfo();// 实体
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					info.setVersion(parser.nextText());
				} else if ("url".equals(parser.getName())) {
					info.setUrl(parser.nextText()); // 获取要升级的APK文件
				} else if ("name".equals(parser.getName())) {
					info.setName(parser.nextText()); // 获取该文件的信息
				}else if ("filename".equals(parser.getName())) {
					info.setFilename(parser.nextText()); // 获取该文件的信息
				}else if("info".equals(parser.getName())){
					info.setInfo(parser.nextText());// 获取该升级提示的信息
				}
				break;
			}
			type = parser.next();
		}
		return info;
	}

	public class CheckVersionTask extends Thread {
		@Override
		public void run() {
			try {
				// 从资源文件获取服务器 地址
				//int versionCode = getVersionCode(mContext);
				String versionName=getVersionName(mContext);
				String path = mContext.getResources().getString(R.string.serverUrl);
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				info = getUpdataInfo(is);
				if ((Float.valueOf(info.getVersion())- Float.valueOf(versionName))>0.0) {					
					Message msg = new Message();
					 msg.what = CAN_UPDATE;
					 mHandler.sendMessage(msg);					
				} else {				
					 Message msg = new Message();
					 msg.what = NO_UPDATE;
					 mHandler.sendMessage(msg);					
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				mHandler.sendMessage(msg);
				Log.e(TAG, "Exception");
			}
		}
	}

}
