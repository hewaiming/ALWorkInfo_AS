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
	/* 涓嬭浇涓� */
	private static final int DOWNLOAD = 1;
	/* 涓嬭浇缁撴潫 */
	private static final int DOWNLOAD_FINISH = 2;
	
	private static final int NO_UPDATE = 3;
	private static final int GET_UNDATAINFO_ERROR=4;
	private static final int CAN_UPDATE=5;
	private static final String TAG = "UpdateManager";
	/* 淇濆瓨瑙ｆ瀽鐨刋ML淇℃伅 */
	HashMap<String, String> mHashMap;
	/* 涓嬭浇淇濆瓨璺緞 */
	private String mSavePath;
	/* 璁板綍杩涘害鏉℃暟閲� */
	private int progress;
	/* 鏄惁鍙栨秷鏇存柊 */
	private boolean cancelUpdate = false;

	private Context mContext;
	private boolean IsShow=true;
	/* 鏇存柊杩涘害鏉� */
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
			// 鑾峰彇杞欢鐗堟湰鍙凤紝瀵瑰簲AndroidManifest.xml涓媋ndroid:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.hewaiming.ALWorkInfo", 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "NameNotFoundException");
		}
		return versionCode;
	}
	
	private String getVersionName(Context context) {	
		
		String versionName ="0" ;
		try {
			// 鑾峰彇杞欢鐗堟湰鍙凤紝瀵瑰簲AndroidManifest.xml涓媋ndroid:versionCode
			versionName = context.getPackageManager().getPackageInfo("com.hewaiming.ALWorkInfo", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "NameNotFoundException");
		}
		return versionName;
	}

	/**
	 * 鏄剧ず杞欢鏇存柊瀵硅瘽妗�
	 */
	private void showNoticeDialog() {
		// 鏋勯�犲璇濇
		AlertDialog.Builder builder = new Builder(mContext);		
		builder.setTitle(R.string.soft_update_title);
		
		builder.setMessage(info.getInfo());
		// 鏇存柊
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 鏄剧ず涓嬭浇瀵硅瘽妗�
				showDownloadDialog();
			}
		});
		// 绋嶅悗鏇存柊
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 鏄剧ず杞欢涓嬭浇瀵硅瘽妗�
	 */
	private void showDownloadDialog() {
		// 鏋勯�犺蒋浠朵笅杞藉璇濇
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 缁欎笅杞藉璇濇澧炲姞杩涘害鏉�
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 鍙栨秷鏇存柊
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 璁剧疆鍙栨秷鐘舵��
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 鐜板湪鏂囦欢
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
				// 鍒ゆ柇SD鍗℃槸鍚﹀瓨鍦紝骞朵笖鏄惁鍏锋湁璇诲啓鏉冮檺
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 鑾峰緱瀛樺偍鍗＄殑璺緞
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					//URL url = new URL(mHashMap.get("url"));
					URL url = new URL(info.getUrl());
					// 鍒涘缓杩炴帴
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 鑾峰彇鏂囦欢澶у皬
					int length = conn.getContentLength();
					// 鍒涘缓杈撳叆娴�
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 鍒ゆ柇鏂囦欢鐩綍鏄惁瀛樺湪
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, info.getFilename());
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缂撳瓨
					byte buf[] = new byte[1024];
					// 鍐欏叆鍒版枃浠朵腑
					do {
						int numread = is.read(buf);
						count += numread;
						// 璁＄畻杩涘害鏉′綅缃�
						progress = (int) (((float) count / length) * 100);
						// 鏇存柊杩涘害
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 涓嬭浇瀹屾垚
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 鍐欏叆鏂囦欢
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 鐐瑰嚮鍙栨秷灏卞仠姝笅杞�.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException e) {
				Log.e(TAG, "IOException");
			}
			// 鍙栨秷涓嬭浇瀵硅瘽妗嗘樉绀�
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
					info.setVersion(parser.nextText()); // 获取版本号
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
				// 包装成url的对象
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
				// 待处理
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				mHandler.sendMessage(msg);
				Log.e(TAG, "Exception");
			}
		}
	}

}
