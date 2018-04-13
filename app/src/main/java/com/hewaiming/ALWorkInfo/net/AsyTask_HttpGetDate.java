package com.hewaiming.ALWorkInfo.net;

import org.json.JSONArray;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyTask_HttpGetDate extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog;
	private Context mContext;
	private String url;
	// 声明接口
	private HttpGetDate_Listener listener;

	private HttpPost_JsonArray jsonParser = new HttpPost_JsonArray();

	public AsyTask_HttpGetDate() {

	}

	public AsyTask_HttpGetDate(String url, HttpGetDate_Listener listener) {
		super();
		this.url = url;
		this.listener = listener;
	}

	public AsyTask_HttpGetDate(String url) {
		this.url = url;
	}	

	public AsyTask_HttpGetDate(String url, HttpGetDate_Listener listener, Context context) {
		this.url = url;
		this.listener = listener;
		this.mContext = context;	
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	/*	pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("初始化日期数据...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();*/
	}

	@Override
	protected String doInBackground(String... params) {
		// List<NameValuePair> mparams = new ArrayList<NameValuePair>(); //
		// Building Parameters
		// mparams.add(new BasicNameValuePair("date","" ));
		JSONArray json = jsonParser.makeHttpRequest(url, "POST");
		
		if(json!=null){
//			Log.d("日期数据：json.toString()", json.toString());// full json response
			return json.toString();
		}else{
			Log.i("PHP服务器数据返回情况：---", "从PHP服务器无数据返回！");
			return "";
		}			
		
	}

	@Override
	protected void onPostExecute(String result) {
	//	pDialog.dismiss();
		listener.GetALLDayUrl(result);
		super.onPostExecute(result);
	}

}
