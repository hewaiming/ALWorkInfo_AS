package com.hewaiming.ALWorkInfo.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.bean.FaultMost;
import com.hewaiming.ALWorkInfo.bean.FaultRecord;
import com.hewaiming.ALWorkInfo.bean.MeasueTable;
import com.hewaiming.ALWorkInfo.bean.OperateRecord;
import com.hewaiming.ALWorkInfo.bean.PotCtrl;
import com.hewaiming.ALWorkInfo.bean.PotV;
import com.hewaiming.ALWorkInfo.bean.RealRecord;
import com.hewaiming.ALWorkInfo.bean.dayTable;

import android.R.integer;
import android.util.Log;

public class JsonToBean_GetPublicData {

	public static List<String> JsonArrayToDate(String data) {

		ArrayList<String> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<String>();
			// System.out.println("jsonarray.length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				String mdata = jsonobj.getString("Ddate");
				int location = mdata.indexOf(" ");
				listBean.add(mdata.substring(0, location));
			}
		} catch (JSONException e) {
			Log.e("Json To PublicData",e.getMessage());
			
		}
		return listBean;
	}

	// 解析记录名 LIST
	public static List<Map<String, Object>> JsonArrayToJXRecord(String data) {

		List<Map<String, Object>> RXList = new ArrayList<Map<String, Object>>();
		try {
			JSONArray jsonarray = new JSONArray(data);

			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", jsonobj.getString("RecordNo"));
				map.put("jx_name", jsonobj.getString("Name1"));

				if (jsonobj.get("Name2")==null) {
					map.put("jx_name2", "");
				} else {
					map.put("jx_name2", jsonobj.getString("Name2"));
				}

				if (jsonobj.get("Name3")==null) {
					map.put("jx_name3", "");
				} else {
					map.put("jx_name3", jsonobj.getString("Name3"));
				}

				RXList.add(map);
			}
		} catch (JSONException e) {
			Log.e("Json to JXRecord", e.getMessage());		
		}

		return RXList;
	}	
	
	// 厂房正常槽数量 LIST
	public static List<PotCtrl> JsonArrayToNormPots(String data) {

		List<PotCtrl> NormPotsList = new ArrayList<PotCtrl>();
		try {
			JSONArray jsonarray = new JSONArray(data);

			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				PotCtrl potCtrl = new PotCtrl();
				if (!(jsonobj.get("PotNo")==null)){
					potCtrl.setPotNo(jsonobj.getInt("PotNo"));
				}
				if (!(jsonobj.get("p31")==null)){
					potCtrl.setCtrls(jsonobj.getInt("p31"));
				}
				
				NormPotsList.add(potCtrl);
			}
		} catch (JSONException e) {
			Log.e("厂房正常槽数量 LIST", e.getMessage());			
		}

		return NormPotsList;
	}

}
