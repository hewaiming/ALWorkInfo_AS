package com.hewaiming.ALWorkInfo.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class NetDetector {

	private Context context;
	int IsNet = 0;
	private boolean ShowToast;

	public NetDetector(Context context,boolean isShow) {
		this.context = context;
		this.ShowToast = isShow;
	}	

	public int isConnectingToInternet() {

		ConnectivityManager connectivity = (ConnectivityManager)context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if ((info[i].getState() == NetworkInfo.State.CONNECTED)
							&& (info[i].getType() == ConnectivityManager.TYPE_WIFI)) {
						if(ShowToast){
							Toast.makeText(context, "��ǰ����:WIFI", Toast.LENGTH_LONG).show();
						}						
						IsNet = 1;
						break;
					}
					if ((info[i].getState() == NetworkInfo.State.CONNECTED)
							&& (info[i].getType() == ConnectivityManager.TYPE_MOBILE)) {
						Toast.makeText(context, "��ǰ����:3G�����4G���磡", Toast.LENGTH_SHORT).show();
						IsNet = 2;
						break;
					}
				}
			}

		}
		return IsNet;
	}
	
	public int isConnectingToInternetNoShow() {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if ((info[i].getState() == NetworkInfo.State.CONNECTED)
							&& (info[i].getType() == ConnectivityManager.TYPE_WIFI)) {
						//Toast.makeText(_context, "��ǰ����:WIFI", Toast.LENGTH_LONG).show();
						IsNet = 1;
						break;
					}
					if ((info[i].getState() == NetworkInfo.State.CONNECTED)
							&& (info[i].getType() == ConnectivityManager.TYPE_MOBILE)) {
						//Toast.makeText(_context, "��ǰ����:3G�����4G���磬��ע�����г������ܻ�����ϴ�������", Toast.LENGTH_LONG).show();
						IsNet = 2;
						break;
					}
				}
			}

		}
		return IsNet;
	}
}