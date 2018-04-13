package com.hewaiming.ALWorkInfo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class HttpPost_JsonArray {
	static InputStream is = null;
	static JSONArray jObj = null;
	static String json = "";

	// constructor
	public HttpPost_JsonArray() {
	}
	// function get json from url
	// by making HTTP POST or GET method
	public JSONArray makeHttpRequest(String url, String method) {

		// Making HTTP request
		try {
			// check for request method
			if (method == "POST") {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
//				httpPost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} 
		} catch (UnsupportedEncodingException e) {
			Log.i("HTTPRequest", e.getMessage());
		} catch (ClientProtocolException e) {
			Log.i("HTTPRequest", e.getMessage());
		} catch (IOException e) {
			Log.i("HTTPRequest", e.getMessage());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"), 8);
			StringBuilder sb = new StringBuilder();
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				sb.append(line);				
//			}
			int b;
			while((b=reader.read())!=-1){
				if(b=='}'){
					sb.append((char)b);	
					sb.append(',');
				}else{
					sb.append((char)b);
				}					
			}
			is.close();
			json = sb.toString();

			json=json.substring(0, json.length()-1);
			json='['+json+']';
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result" + e.toString());
			return null;
		}		
		// try parse the string to a JSON object
		try {
			
			jObj = new JSONArray(json);
			
		} catch (JSONException e) {			
			Log.e("JSONPArser--NoParams", "Error Parsing data" + e.toString());
			return null;
		}
		return jObj;
	}
}