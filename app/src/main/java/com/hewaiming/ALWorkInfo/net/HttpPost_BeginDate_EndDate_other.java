package com.hewaiming.ALWorkInfo.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener_other;
import com.hewaiming.ALWorkInfo.json.JSONArrayParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpPost_BeginDate_EndDate_other extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog;
	private Context mContext;
	private String url;
	private String Area_PotNo;
	private int type;
	private String BeginDate, EndDate;
	// 声明接口
	private HttpGetListener_other listener;
	private JSONArrayParser jsonParser = new JSONArrayParser();

	public HttpPost_BeginDate_EndDate_other() {

	}

	public HttpPost_BeginDate_EndDate_other(String url, int type, String area, String beginDate, String endDate,
			HttpGetListener_other listener, Context mContext) {

		this.mContext = mContext;
		this.url = url;
		this.Area_PotNo = area;
		this.type = type;
		this.BeginDate = beginDate;
		this.EndDate = endDate;
		this.listener = listener;
	}

	public HttpPost_BeginDate_EndDate_other(String url) {
		this.url = url;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("玩命加载中...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();

	}

	@Override
	protected String doInBackground(String... params) {
		// Building Parameters
		List<NameValuePair> mparams = new ArrayList<NameValuePair>();
		mparams.clear();
		if (type == 1) {
			mparams.add(new BasicNameValuePair("areaID", Area_PotNo)); // 全部槽号
			mparams.add(new BasicNameValuePair("BeginDate", BeginDate));
			mparams.add(new BasicNameValuePair("EndDate", EndDate));
		} else if (type == 2) {
			mparams.add(new BasicNameValuePair("PotNo", Area_PotNo)); // 槽号
			mparams.add(new BasicNameValuePair("BeginDate", BeginDate));
			mparams.add(new BasicNameValuePair("EndDate", EndDate));

		}
        
		JSONArray json = jsonParser.makeHttpRequest(url, "POST", mparams);
		if (json != null) {
			//Log.d("json_area_potno", json.toString());// 从服务器返回有数据
			return json.toString();
		} else {
			Log.i("PHP服务器数据返回情况：---", "从PHP服务器无数据返回！");
			return "";
		}

	}

	@Override
	protected void onPostExecute(String result) {
		pDialog.dismiss();
		listener.GetOtherDataUrl(result);
		super.onPostExecute(result);
	}

}
