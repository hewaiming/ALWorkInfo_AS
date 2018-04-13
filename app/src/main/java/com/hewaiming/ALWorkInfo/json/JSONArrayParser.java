package com.hewaiming.ALWorkInfo.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class JSONArrayParser {
	 private static final String TAG = "JSONArrayParser";
	InputStream is = null;
	 JSONArray jObj = null;
	 String json = "";

	// constructor
	public JSONArrayParser() {
	}

	// by making HTTP POST or GET method // function get json from url
	public JSONArray makeHttpRequest(String url, String method, List<NameValuePair> params) {

		// Making HTTP request
		try {
			// check for request method
			if (method == "POST") {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} else if (method == "GET") {
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "UnsupportedEncodingException");
		} catch (ClientProtocolException e) {
			Log.e(TAG, "ClientProtocolException");
		} catch (IOException e) {
			Log.e(TAG, "IOException");
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"), 8);
			StringBuilder sb = new StringBuilder();
					// String line = null;
					// while ((line = reader.readLine()) != null) {
					// sb.append(line);
					// }
			int b;
			while ((b = reader.read()) != -1) {
				if (b == '}') {
					sb.append((char) b);
					sb.append(',');
				} else {
					sb.append((char) b);
				}
			}
			is.close();
			json="";
			json = sb.toString();	
			//System.out.println("json before---" + json);
			if (json.equals("")){
				return jObj;  //无数据，直接返回空
			}else{
				json = json.substring(0, json.length() - 1);
				json = '[' + json + ']';
				//System.out.println("json after---" + json);
			}
			

		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result" + e.toString());
			return jObj;
		}
		try {
			if (!(json.equals(""))) {
				jObj = new JSONArray(json);
			}

		} catch (JSONException e) {
			Log.e("JSON PArser", "Error Parsing data" + e.toString());
		}
		return jObj;
	}
}