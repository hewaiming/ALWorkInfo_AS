package com.hewaiming.ALWorkInfo.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.json.JSONArrayParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyTask_HttpPost_AreaDate extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog;
	private Context mContext;
	private String url;
	private String areaID;
	private String ddate;
	// 声明接口
	private HttpGetListener listener;
	private JSONArrayParser jsonParser = new JSONArrayParser();

	public AsyTask_HttpPost_AreaDate() {

	}

	public AsyTask_HttpPost_AreaDate(String url) {
		this.url = url;
	}

	public AsyTask_HttpPost_AreaDate(String url, HttpGetListener listener, Context context, String areaId,String ddate) {
		this.url = url;
		this.listener = listener;
		this.mContext = context;
		this.areaID = areaId;
		this.ddate=ddate;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("槽日报，玩命加载中...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();

	}

	@Override
	protected String doInBackground(String... params) {
		// Building Parameters
		List<NameValuePair> mparams = new ArrayList<NameValuePair>();
		mparams.add(new BasicNameValuePair("areaID", areaID));
		mparams.add(new BasicNameValuePair("Ddate",ddate)); 

		JSONArray json = jsonParser.makeHttpRequest(url, "POST", mparams);
		// full json response
		if(json!=null){
			Log.d("dayTabl-----json.toString()", json.toString());// full json response
			return json.toString();
		}else{
			Log.i("PHP服务器数据返回情况：---", "从PHP服务器无数据返回！");
			return "";
		}		
		
	}

	@Override
	protected void onPostExecute(String result) {
		pDialog.dismiss();
		listener.GetDataUrl(result);
		super.onPostExecute(result);
	}

}
