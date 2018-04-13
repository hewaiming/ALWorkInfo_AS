package com.hewaiming.ALWorkInfo.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.bean.dayTable;

import android.util.Log;

public class JsonToMultiList {

	public static List<dayTable> JsonArrayToDayTableBean(String data) {

		List<dayTable> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<dayTable>();
			listBean.clear();
			//System.out.println("jsonarray. DayTable---length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				dayTable mBean = new dayTable();

				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("PotNo"));
				}

				if (jsonobj.get("SetNB").equals(null)) {
					mBean.setSetNB(0);
				} else {
					mBean.setSetNB(jsonobj.getInt("SetNB"));
				}

				if (jsonobj.get("SetV").equals(null)) {
					mBean.setSetV(0);
				} else {
					mBean.setSetV(jsonobj.getDouble("SetV"));
				}
				if (jsonobj.get("AverageV").equals(null)) {
					mBean.setAverageV(0);
				} else {
					mBean.setAverageV(jsonobj.getDouble("AverageV"));
				}

				if (jsonobj.get("WorkV").equals(null)) {
					mBean.setWorkV(0);
				} else {
					mBean.setWorkV(jsonobj.getDouble("WorkV"));
				}
				if (jsonobj.get("YhlCnt").equals(null)) {
					mBean.setYhlCnt(0);
				} else {
					mBean.setYhlCnt(jsonobj.getInt("YhlCnt"));
				}
				if (jsonobj.get("FhlCnt").equals(null)) {
					mBean.setFhlCnt(0);
				} else {
					mBean.setFhlCnt(jsonobj.getInt("FhlCnt"));
				}

				if (jsonobj.get("DybTime").equals(null)) {
					mBean.setDybTime(0);
				} else {
					mBean.setDybTime(jsonobj.getInt("DybTime"));
				}
				if (jsonobj.get("AeCnt").equals(null)) {
					mBean.setAeCnt(0);
				} else {
					mBean.setAeCnt(jsonobj.getInt("AeCnt"));
				}
				if (jsonobj.get("AlCntZSL").equals(null)) {
					mBean.setAlCntZSL(0); // 指示出铝量
				} else {
					mBean.setAlCntZSL(jsonobj.getInt("AlCntZSL"));
				}
				if (jsonobj.get("ZF").equals(null)) {
					mBean.setZF(0); // 噪音
				} else {
					mBean.setZF(jsonobj.getInt("ZF"));
				}
				if (jsonobj.get("Ddate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("Ddate"));
				}
				listBean.add(mBean);
			}
		} catch (JSONException e) {
			Log.e("JsonToMultiList", e.getMessage());			
		}
		return listBean;
	}

	//效应情报表数据
	public static Map<String, List<AeRecord>> JsonArrayToAeRecord_5DayBean(String data) {
		Map<String, List<AeRecord>> mapBean = null;
		List<AeRecord> list1 = null;
		List<AeRecord> list2 = null;
		List<AeRecord> list3 = null;
		List<AeRecord> list4 = null;
		List<AeRecord> list5 = null;
		int listNo=-1;
		int potNo=0;
		
		try {
			JSONArray jsonarray = new JSONArray(data);

			mapBean = new HashMap<String, List<AeRecord>>();
			list1 = new ArrayList<AeRecord>();
			list2 = new ArrayList<AeRecord>();
			list3 = new ArrayList<AeRecord>();
			list4 = new ArrayList<AeRecord>();
			list5 = new ArrayList<AeRecord>();			
			//System.out.println("jsonarray. AE5day_Table---length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {				
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				//第一次给POTNO变量赋值
				if(i==0) {
					potNo=jsonobj.getInt("POTNO");
				}
				AeRecord mBean = new AeRecord();
				if (jsonobj.get("POTNO").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("POTNO"));					
				}

				if (jsonobj.get("DDate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("DDate"));
				}
				if (jsonobj.get("AverageVoltage").equals(null)) {
					mBean.setAverageV(0);
				} else {
					mBean.setAverageV(jsonobj.getDouble("AverageVoltage"));
				}
				if (jsonobj.get("ContinuanceTime").equals(null)) {
					mBean.setContinueTime(0);
				} else {
					mBean.setContinueTime(jsonobj.getInt("ContinuanceTime"));
				}

				if (jsonobj.get("WaitTime").equals(null)) {
					mBean.setWaitTime(0);
				} else {
					mBean.setWaitTime(jsonobj.getInt("WaitTime"));
				}

				if (jsonobj.get("Status").equals(null)) {
					mBean.setStatus("");
				} else {
					mBean.setStatus(jsonobj.getString("Status"));
				}
				if (jsonobj.get("MaxVoltage").equals(null)) {
					mBean.setMaxV(0);
				} else {
					mBean.setMaxV(jsonobj.getDouble("MaxVoltage"));
				}
				//判断后续数据与前一数据是否相等
				if(potNo==mBean.getPotNo()){
					listNo++;
				}else{
					listNo=0;
					potNo=mBean.getPotNo();
				}
				switch (listNo % 5) {
				case 0:
					list1.add(mBean);
					break;
				case 1:
					list2.add(mBean);
					break;
				case 2:
					list3.add(mBean);
					break;
				case 3:
					list4.add(mBean);
					break;
				case 4:
					list5.add(mBean);
					break;
				}
			}
			mapBean.put("ae1", list1);
			mapBean.put("ae2", list2);
			mapBean.put("ae3", list3);
			mapBean.put("ae4", list4);
			mapBean.put("ae5", list5);

		} catch (JSONException e) {
			Log.e("JsonToAE5Record", e.getMessage());			
		}
		return mapBean;
 }

}
