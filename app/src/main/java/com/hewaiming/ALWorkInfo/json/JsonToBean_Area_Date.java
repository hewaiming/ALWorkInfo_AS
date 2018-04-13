package com.hewaiming.ALWorkInfo.json;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.bean.Ae_Area;
import com.hewaiming.ALWorkInfo.bean.AvgV;
import com.hewaiming.ALWorkInfo.bean.AvgV_Area;
import com.hewaiming.ALWorkInfo.bean.DJWD;
import com.hewaiming.ALWorkInfo.bean.HY_item;
import com.hewaiming.ALWorkInfo.bean.FaultMost;
import com.hewaiming.ALWorkInfo.bean.FaultRecord;
import com.hewaiming.ALWorkInfo.bean.MeasueTable;
import com.hewaiming.ALWorkInfo.bean.OperateRecord;
import com.hewaiming.ALWorkInfo.bean.PotCtrl;
import com.hewaiming.ALWorkInfo.bean.PotV;
import com.hewaiming.ALWorkInfo.bean.PotV_plus;
import com.hewaiming.ALWorkInfo.bean.RealRecord;
import com.hewaiming.ALWorkInfo.bean.dayTable;

import android.R.integer;
import android.util.Log;

public class JsonToBean_Area_Date {

	public static List<dayTable> JsonArrayToDayTableBean_Other(String data) {

		ArrayList<dayTable> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<dayTable>();
			// System.out.println("jsonarray.DayTable length()---" +
			// jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				dayTable mday = new dayTable();
				mday.setPotNo(jsonobj.getInt("PotNo"));
				// mday.setPotSt(jsonobj.getString("PotST"));

				String Pot_status = jsonobj.getString("PotST").toUpperCase();
				switch (Pot_status) {
				case "NORM":
					mday.setPotSt("正常");
					break;
				case "STOP":
					mday.setPotSt("停槽");
					break;
				case "PREHEAT":
					mday.setPotSt("预热");
					break;
				case "START":
					mday.setPotSt("启动");
					break;
				}

				if (Pot_status.equals("STOP")) {
					mday.setAeTime(0);
					mday.setAeV(0);
					mday.setAeCnt(0);
					mday.setDybTime(0);
					mday.setRunTime(0);
					mday.setSetV(0);
					mday.setRealSetV(0);
					mday.setWorkV(0);
				} else {
					mday.setAeTime(jsonobj.getInt("AeTime"));
					mday.setAeV(jsonobj.getDouble("AeV"));
					mday.setAeCnt(jsonobj.getInt("AeCnt"));
					mday.setDybTime(jsonobj.getInt("DybTime"));
					mday.setRunTime(jsonobj.getInt("RunTime"));
					mday.setSetV(jsonobj.getDouble("SetV"));
					mday.setRealSetV(jsonobj.getDouble("RealSetV"));
					mday.setWorkV(jsonobj.getDouble("WorkV"));
				}
				mday.setAverageV(jsonobj.getDouble("AverageV"));

				String mdata = jsonobj.getString("Ddate");
				int location = mdata.indexOf(" ");
				mday.setDdate(mdata.substring(0, location));

				listBean.add(mday);
			}
		} catch (JSONException e) {
			Log.e("JsonTo日报","JsonTo日报 JSONException" );
		
		}
		return listBean;
	}

	public static List<PotV> JsonArrayToPotVBean(String data) {
		ArrayList<PotV> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<PotV>();
			// System.out.println("jsonarray.length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				PotV mPotV = new PotV();
				mPotV.setDdate(jsonobj.getString("DDate"));
				mPotV.setCur(jsonobj.getInt("Cur"));
				mPotV.setPotV(jsonobj.getInt("PotNoV"));
				listBean.add(mPotV);
			}
		} catch (JSONException e) {
			Log.e("JsonTo槽电压","JsonTo槽电压 JSONException");			
		}
		return listBean;
	}

	public static List<PotV_plus> JsonArrayToPotV_plusBean(String data) {
		ArrayList<PotV_plus> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<PotV_plus>();
			// System.out.println("jsonarray.length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				PotV_plus mPotV = new PotV_plus();
				mPotV.setDdate(jsonobj.getString("DDate"));
				mPotV.setCur(jsonobj.getInt("Cur"));
				mPotV.setAction(jsonobj.getInt("Action"));
				mPotV.setPotV(jsonobj.getInt("PotNoV"));
				mPotV.setTargetV(jsonobj.getInt("TargetV"));
				mPotV.setMoreLess(jsonobj.getInt("MoreLess"));
				mPotV.setInterval(jsonobj.getInt("Target_Add"));
				listBean.add(mPotV);
			}
		} catch (JSONException e) {

			Log.e("JsonTo槽电压Plus","JsonTo槽电压Plus JSONException" );
		}
		return listBean;
	}

	public static List<FaultRecord> JsonArrayToFaultRecordBean(String data, List<Map<String, Object>> JXList) {
		List<FaultRecord> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<FaultRecord>();
			listBean.clear();
			// System.out.println("jsonarray.FaultRecord---length()---" +
			// jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				FaultRecord mFault = new FaultRecord();
				mFault.setPotNo(jsonobj.getInt("PotNo"));
				mFault.setRecTime(jsonobj.getString("DDate"));

				int recNo = jsonobj.getInt("RecordNo");
				recNo = recNo - 1;
				Map<String, Object> mMap = JXList.get(recNo);
				// System.out.println("jx_name" +
				// mMap.get("jx_name").toString());
				mFault.setRecordNo(mMap.get("jx_name").toString());
				listBean.add(mFault);
			}
		} catch (JSONException e) {

			Log.e("JsonTo故障表","JsonTo故障表 JSONException");
		}
		return listBean;
	}

	public static List<RealRecord> JsonArrayToRealRecordBean(String data, List<Map<String, Object>> JXList) {

		List<RealRecord> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<RealRecord>();
			listBean.clear();
			// System.out.println("jsonarray.RealRecord :length()---" +
			// jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				RealRecord mReal = new RealRecord();
				mReal.setPotNo(jsonobj.getInt("PotNo"));
				mReal.setRecTime(jsonobj.getString("DDate"));

				int recNo = jsonobj.getInt("RecordNo");
				recNo = recNo - 1;
				Map<String, Object> mMap = JXList.get(recNo);
				// System.out.println("jx_name:" +
				// mMap.get("jx_name").toString());

				String name2_tmp = mMap.get("jx_name2").toString(); // 记录名 参数2
				String name3_tmp = mMap.get("jx_name3").toString();

				mReal.setRecordNo(mMap.get("jx_name").toString());

				if (jsonobj.get("Val2").equals(null)) {
					mReal.setParam1("");
				} else {
					mReal.setParam1(name2_tmp + jsonobj.getInt("Val2"));
				}
				if (jsonobj.get("Val3").equals(null)) {
					mReal.setParam2("");
				} else {
					mReal.setParam2(name3_tmp + jsonobj.getInt("Val3") + "");
				}

				listBean.add(mReal);
			}
		} catch (JSONException e) {

			Log.e("JsonTo实时记录", "JsonTo实时记录 JSONException");
		}
		return listBean;
	}

	public static List<OperateRecord> JsonArrayToOperateRecordBean(String data) {

		List<OperateRecord> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<OperateRecord>();
			listBean.clear();
			// System.out.println("jsonarray.RealRecord---length()---" +
			// jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);

				OperateRecord mOp = new OperateRecord();
				mOp.setObjectName(jsonobj.getString("ObjectName"));
				mOp.setParamNameCH(jsonobj.getString("ParaNameCH"));
				mOp.setDescription(jsonobj.getString("Description"));
				mOp.setUserName(jsonobj.getString("UserName"));
				mOp.setRecTime(jsonobj.getString("DDate"));

				/*
				 * if (jsonobj.get("Val2").equals(null)){ mReal.setParam1("");
				 * }else{ mReal.setParam1(jsonobj.getInt("Val2")+""); } if
				 * (jsonobj.get("Val3").equals(null)){ mReal.setParam2("");
				 * }else{ mReal.setParam2(jsonobj.getInt("Val3")+""); }
				 */

				listBean.add(mOp);
			}
		} catch (JSONException e) {

			Log.e("JsonTo操作记录","JsonTo操作记录  JSONException" );
		}
		return listBean;
	}

	public static List<AeRecord> JsonArrayToAeRecordBean(String data) {

		List<AeRecord> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<AeRecord>();
			listBean.clear();
			// System.out.println("jsonarray.AeRecord---length()---" +
			// jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);

				AeRecord mBean = new AeRecord();
				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("PotNo"));
				}

				if (jsonobj.get("Ddate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("Ddate"));
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
				if (jsonobj.get("MaxVoltage")==null) {
					mBean.setMaxV(0);
				} else {
					mBean.setMaxV(jsonobj.getDouble("MaxVoltage"));
				}
				/*
				 * if (jsonobj.get("Val2").equals(null)){ mReal.setParam1("");
				 * }else{ mReal.setParam1(jsonobj.getInt("Val2")+""); } if
				 * (jsonobj.get("Val3").equals(null)){ mReal.setParam2("");
				 * }else{ mReal.setParam2(jsonobj.getInt("Val3")+""); }
				 */
				listBean.add(mBean);
			}
		} catch (JSONException e) {

			Log.e("JsonToAE记录","JsonToAE记录 JSONException" );
		}
		return listBean;
	}

	public static List<dayTable> JsonArrayToDayTableBean(String GetData) {

		List<dayTable> listDayTable = null;
		try {
			JSONArray jsonarray = new JSONArray(GetData);

			listDayTable = new ArrayList<dayTable>();
			listDayTable.clear();
			// System.out.println("jsonarray. DayTable---length()---" +
			// jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				dayTable mBean = new dayTable();

				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("PotNo"));
				}
				String Pot_status = jsonobj.getString("PotST").toUpperCase();
				switch (Pot_status) {
				case "NORM":
					mBean.setPotSt("正常");
					break;
				case "STOP":
					mBean.setPotSt("停槽");
					break;
				case "PREHEAT":
					mBean.setPotSt("预热");
					break;
				case "START":
					mBean.setPotSt("启动");
					break;
				}

				if (jsonobj.get("RunTime").equals(null)) {
					mBean.setRunTime(0);
				} else {
					mBean.setRunTime(jsonobj.getInt("RunTime"));
				}
				// 加料量
				if (jsonobj.get("YhlCnt").equals(null)) {
					mBean.setYhlCnt(0);
				} else {
					mBean.setYhlCnt(jsonobj.getInt("YhlCnt"));
				}
				// 加料次数
				if (jsonobj.get("JlCnt").equals(null)) {
					mBean.setJLCnt(0);
				} else {
					mBean.setJLCnt(jsonobj.getInt("JlCnt"));
				}

				if (jsonobj.get("SetV").equals(null)) {
					mBean.setSetV(0);
				} else {
					mBean.setSetV(jsonobj.getDouble("SetV"));
				}
				if (jsonobj.get("RealSetV").equals(null)) {
					mBean.setRealSetV(0);
				} else {
					mBean.setRealSetV(jsonobj.getDouble("RealSetV"));
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
				if (jsonobj.get("AeV").equals(null)) {
					mBean.setAeV(0);
				} else {
					mBean.setAeV(jsonobj.getDouble("AeV"));
				}
				if (jsonobj.get("AeTime").equals(null)) {
					mBean.setAeTime(0);
				} else {
					mBean.setAeTime(jsonobj.getInt("AeTime"));
				}
				if (jsonobj.get("AeCnt").equals(null)) {
					mBean.setAeCnt(0);
				} else {
					mBean.setAeCnt(jsonobj.getInt("AeCnt"));
				}
				if (jsonobj.get("DybTime").equals(null)) {
					mBean.setDybTime(0);
				} else {
					mBean.setDybTime(jsonobj.getInt("DybTime"));
				}
				// 氟化铝量
				if (jsonobj.get("FhlCnt").equals(null)) {
					mBean.setFhlCnt(0);
				} else {
					mBean.setFhlCnt(jsonobj.getInt("FhlCnt"));
				}
				// 出铝指示量
				if (jsonobj.get("AlCntZSL").equals(null)) {
					mBean.setAlCntZSL(0);
				} else {
					mBean.setAlCntZSL(jsonobj.getInt("AlCntZSL"));
				}
				if (jsonobj.get("Ddate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("Ddate"));
				}
				listDayTable.add(mBean);
			}
		} catch (JSONException e) {

			Log.e("JsonTo日报", "JsonTo日报 JSONException");
		}
		return listDayTable;
	}

	// 效应次数统计
	public static List<AeRecord> JsonArrayToAeCntBean(String data) {

		List<AeRecord> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<AeRecord>();
			listBean.clear();
			// System.out.println("jsonarray.AeCnt---length()---" +
			// jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);

				AeRecord mBean = new AeRecord();
				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("PotNo"));
				}

				if (jsonobj.get("AeCnt").equals(null)) {
					mBean.setWaitTime(0);
				} else {
					mBean.setWaitTime(jsonobj.getInt("AeCnt"));
				}
				listBean.add(mBean);
			}
		} catch (JSONException e) {

			Log.e("jsonToAERecord","jsonToAERecord JSONException");
		}
		return listBean;
	}

	// 效应时间长
	public static List<AeRecord> JsonArrayToAeTimeBean(String data) {

		List<AeRecord> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<AeRecord>();
			listBean.clear();
			// System.out.println("jsonarray.AeRecord---length()---" +
			// jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);

				AeRecord mBean = new AeRecord();
				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("PotNo"));
				}

				if (jsonobj.get("Ddate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("Ddate"));
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

				// if (jsonobj.get("WaitTime").equals(null)) {
				// mBean.setWaitTime(0);
				// } else {
				// mBean.setWaitTime(jsonobj.getInt("WaitTime"));
				// }

				// if (jsonobj.get("Status").equals(null)) {
				// mBean.setStatus("");
				// } else {
				// mBean.setStatus(jsonobj.getString("Status"));
				// }
				if (jsonobj.get("MaxVoltage").equals(null)) {
					mBean.setMaxV(0);
				} else {
					mBean.setMaxV(jsonobj.getDouble("MaxVoltage"));
				}
				listBean.add(mBean);
			}
		} catch (JSONException e) {
            Log.e("JsonTo效应时间长","JsonTo效应时间长 JSONException" );			
		}
		return listBean;
	}

	// 网络字符流数据转换为 测量数据LIST
	public static List<MeasueTable> JsonArrayToMeasueTableBean(String data) {

		List<MeasueTable> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<MeasueTable>();
			listBean.clear();
			// System.out.println("jsonarray. MeasueTable---length()---" +
			// jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				MeasueTable mBean = new MeasueTable();
				// PotNo, Ddate,ALCnt, LSP,
				// DJZSP;DJWD;FZB;FeCnt;SiCnt;ALOCnt;CaFCnt;MgCnt;MLSP;LDYJ;JHCL;

				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo("");
				} else {
					mBean.setPotNo(String.valueOf(jsonobj.getInt("PotNo")));
				}

				if (jsonobj.get("DDate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("DDate"));
				}
				if (jsonobj.get("AlCnt").equals(null)) {
					mBean.setALCnt("");
				} else {
					mBean.setALCnt(String.valueOf(jsonobj.getInt("AlCnt")));
				}
				if (jsonobj.get("Lsp").equals(null)) {
					mBean.setLSP("");
				} else {
					mBean.setLSP(String.valueOf(jsonobj.getInt("Lsp")));
				}
				if (jsonobj.get("Djzsp").equals(null)) {
					mBean.setDJZSP("");
				} else {
					mBean.setDJZSP(String.valueOf(jsonobj.getInt("Djzsp")));
				}

				if (jsonobj.get("Djwd").equals(null)) {
					mBean.setDJWD("");
				} else {
					mBean.setDJWD(String.valueOf(jsonobj.getInt("Djwd")));
				}
				if (jsonobj.get("Fzb").equals(null)) {
					mBean.setFZB("");
				} else {
					mBean.setFZB(String.valueOf(jsonobj.getDouble("Fzb")));
				}
				if (jsonobj.get("FeCnt").equals(null)) {
					mBean.setFeCnt("");
				} else {
					mBean.setFeCnt(String.valueOf(jsonobj.getDouble("FeCnt")));
				}

				if (jsonobj.get("SiCnt").equals(null)) {
					mBean.setSiCnt("");
				} else {
					mBean.setSiCnt(String.valueOf(jsonobj.getDouble("SiCnt")));
				}

				if (jsonobj.get("AlOCnt").equals(null)) {
					mBean.setALOCnt("");
				} else {
					mBean.setALOCnt(String.valueOf(jsonobj.getDouble("AlOCnt")));
				}

				if (jsonobj.get("CaFCnt").equals(null)) {
					mBean.setCaFCnt("");
				} else {
					mBean.setCaFCnt(String.valueOf(jsonobj.getDouble("CaFCnt")));
				}

				if (jsonobj.get("MgCnt").equals(null)) {
					mBean.setMgCnt("");
				} else {
					mBean.setMgCnt(String.valueOf(jsonobj.getDouble("MgCnt")));
				}

				if (jsonobj.get("MLsp").equals(null)) {
					mBean.setMLSP("");
				} else {
					mBean.setMLSP(String.valueOf(jsonobj.getInt("MLsp")));
				}
				if (jsonobj.get("LDYJ").equals(null)) {
					mBean.setLDYJ("");
				} else {
					mBean.setLDYJ(String.valueOf(jsonobj.getInt("LDYJ")));
				}
				if (jsonobj.get("JHCL").equals(null)) {
					mBean.setJHCL("");
				} else {
					mBean.setJHCL(String.valueOf(jsonobj.getInt("JHCL")));
				}
				listBean.add(mBean);
			}
		} catch (JSONException e) {
			Log.e("数据转换为测量数据LIST","数据转换为测量数据LIST JSONException");			
		}
		return listBean;
	}

	// 故障率统计
	public static List<FaultMost> JsonArrayToFaultCntBean(String data) {

		List<FaultMost> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<FaultMost>();
			listBean.clear();		
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);

				FaultMost mBean = new FaultMost();
				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("PotNo"));
				}

				if (jsonobj.get("FaultCnt").equals(null)) {
					mBean.setFaultCnt(0);
				} else {
					mBean.setFaultCnt(jsonobj.getInt("FaultCnt"));
				}
				listBean.add(mBean);
			}
		} catch (JSONException e) {
			Log.e("JSON TO故障率统计","JSON TO故障率统计 JSONException");
		
		}
		return listBean;
	}

	// JSON转换成LIST数据 （平均电压）
	public static List<AvgV> JsonArrayToAvgVDayTableBean(String data) {

		List<AvgV> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);
			listBean = new ArrayList<AvgV>();
			listBean.clear();
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				AvgV mBean = new AvgV();

				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("PotNo"));
				}
				String Pot_status = jsonobj.getString("PotST").toUpperCase();
				switch (Pot_status) {
				case "NORM":
					mBean.setPotST("正常");
					break;
				case "STOP":
					mBean.setPotST("停槽");
					break;
				case "PREHEAT":
					mBean.setPotST("预热");
					break;
				case "START":
					mBean.setPotST("启动");
					break;
				}

				if (jsonobj.get("AverageV").equals(null)) {
					mBean.setAverageV(0);
				} else {
					mBean.setAverageV(jsonobj.getDouble("AverageV"));
				}

				if (jsonobj.get("Ddate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("Ddate"));
				}
				listBean.add(mBean);
			}
		} catch (JSONException e) {
			Log.e("JSON转换成LIST数据（平均电压）","JSON转换成LIST数据（平均电压）JSONException");
		}
		return listBean;
	}

	// JSON转换成LIST数据 （电解温度）
	public static List<DJWD> JsonArrayToDJWDBean(String data) {
		List<DJWD> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);
			listBean = new ArrayList<DJWD>();
			listBean.clear();		
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				DJWD mBean = new DJWD();
				// PotNo, Ddate,ALCnt, LSP,
				// DJZSP;DJWD;FZB;FeCnt;SiCnt;ALOCnt;CaFCnt;MgCnt;MLSP;LDYJ;JHCL;

				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo("");
				} else {
					mBean.setPotNo(String.valueOf(jsonobj.getInt("PotNo")));
				}

				if (jsonobj.get("DDate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("DDate"));
				}

				if (jsonobj.get("Djwd").equals(null)) {
					mBean.setDJWD("");
				} else {
					mBean.setDJWD(String.valueOf(jsonobj.getInt("Djwd")));
				}
				/*
				 * if (jsonobj.get("Fzb").equals(null)) { mBean.setFZB(""); }
				 * else {
				 * mBean.setFZB(String.valueOf(jsonobj.getDouble("Fzb"))); }
				 */

				/*
				 * if (jsonobj.get("AlOCnt").equals(null)) {
				 * mBean.setALOCnt(""); } else {
				 * mBean.setALOCnt(String.valueOf(jsonobj.getDouble("AlOCnt")));
				 * }
				 */

				listBean.add(mBean);
			}
		} catch (JSONException e) {
	        Log.e("JSON转换成LIST电解温度","JSON转换成LIST电解温度 JSONException");		
		}
		return listBean;
	}

	// JSON转换成LIST数据 （分子比）
	public static List<HY_item> JsonArrayToFZBBean(String data) {
		List<HY_item> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);
			listBean = new ArrayList<HY_item>();
			listBean.clear();
			// System.out.println("jsonarray. MeasueTable---length()---"
			// +jsonarray.length());
			JSONObject prior_jsonobj = jsonarray.getJSONObject(0);
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				if (i > 0) {
					prior_jsonobj = jsonarray.getJSONObject(i - 1);
				}
				// 前一个数据
				String priorPot = prior_jsonobj.get("PotNo").toString();
				String priorDdate = prior_jsonobj.get("DDate").toString();
				HY_item mBean = new HY_item();
				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo("");
				} else {
					mBean.setPotNo(String.valueOf(jsonobj.getInt("PotNo")));
				}
				String presentPot = mBean.getPotNo();

				if (jsonobj.get("DDate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("DDate"));
				}
				String presentDdate = mBean.getDdate();
				if (jsonobj.get("Fzb").equals(null)) {
					mBean.setFZB("0");
				} else {
					mBean.setFZB(String.valueOf(jsonobj.getDouble("Fzb")));
				}

				/*
				 * if (jsonobj.get("AlOCnt").equals(null)) {
				 * mBean.setALOCnt(""); } else {
				 * mBean.setALOCnt(String.valueOf(jsonobj.getDouble("AlOCnt")));
				 * }
				 */
				if (i == 0) {
					listBean.add(mBean);
					presentPot = mBean.getPotNo();
					priorDdate = mBean.getDdate();
				}
				if (priorPot.equals(presentPot)) {

				} else {
					if (priorDdate.equals(presentDdate)) {

					} else {
						listBean.add(mBean);
					}
				}
			}
		} catch (JSONException e) {
			Log.e("JSON转换成LIST分子比", "JSON转换成LIST分子比 JSONException");
		}	
		return listBean;
	}

	// JSON转换成LIST数据 （氧化铝浓度）
	public static List<HY_item> JsonArrayToYHLNDBean(String data) {
		List<HY_item> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);
			listBean = new ArrayList<HY_item>();
			listBean.clear();		

			JSONObject prior_jsonobj = jsonarray.getJSONObject(0);
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				if (i > 0) {
					prior_jsonobj = jsonarray.getJSONObject(i - 1);
				}
				// 前一个数据
				String priorPot = prior_jsonobj.get("PotNo").toString();
				String priorDdate = prior_jsonobj.get("DDate").toString();
				HY_item mBean = new HY_item();
				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo("");
				} else {
					mBean.setPotNo(String.valueOf(jsonobj.getInt("PotNo")));
				}
				String presentPot = mBean.getPotNo();

				if (jsonobj.get("DDate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("DDate"));
				}

				String presentDdate = mBean.getDdate();
				if (jsonobj.get("AlOCnt").equals(null)) {
					mBean.setFZB("0");
				} else {
					mBean.setFZB(String.valueOf(jsonobj.getDouble("AlOCnt")));
				}
				if (i == 0) {
					listBean.add(mBean);
					presentPot = mBean.getPotNo();
					priorDdate = mBean.getDdate();
				}
				if (priorPot.equals(presentPot)) {

				} else {
					if (priorDdate.equals(presentDdate)) {

					} else {
						listBean.add(mBean);
					}
				}
			}
		} catch (JSONException e) {
			Log.e("JSON转换成LIST数据 氧化铝浓度","JSON转换成LIST数据 氧化铝浓度 JSONException");
		}
	
		return listBean;
	}

	public static HY_item JsonArrayToFZBItem(String id, String data) {
		try {
			JSONArray jsonarray = new JSONArray(data);
			JSONObject jsonobj = jsonarray.getJSONObject(0);
			HY_item mItem = new HY_item();
			mItem.setPotNo(id);
			if (jsonobj.get("DDate").equals(null)) {
				mItem.setDdate("");
			} else {
				mItem.setDdate(jsonobj.getString("DDate"));
			}
			if (jsonobj.get("AvgFzb").equals(null)) {
				mItem.setFZB("0");
			} else {
				mItem.setFZB(String.valueOf(jsonobj.getDouble("AvgFzb")));
			}
			return mItem;
		} catch (JSONException e) {
			Log.e("json to fzb","json to fzb JSONException");		
			return null;
		}
	}

	public static HY_item JsonArrayToYHLNDItem(String id, String data) {
		try {
			JSONArray jsonarray = new JSONArray(data);
			JSONObject jsonobj = jsonarray.getJSONObject(0);
			HY_item mItem = new HY_item();
			mItem.setPotNo(id);
			if (jsonobj.get("DDate").equals(null)) {
				mItem.setDdate("");
			} else {
				mItem.setDdate(jsonobj.getString("DDate"));
			}
			if (jsonobj.get("AvgYHLND").equals(null)) {
				mItem.setFZB("0");
			} else {
				mItem.setFZB(String.valueOf(jsonobj.getDouble("AvgYHLND")));
			}
			return mItem;
		} catch (JSONException e) {
			Log.e("JsonArrayToYHLNDItem","JsonArrayToYHLNDItem JSONException");
			return null;
		}

	}

	// JSON转换成LIST数据 （区平均电压）
	public static List<AvgV_Area> JsonArrayToAreaAvgVBean(String data) {

		List<AvgV_Area> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);
			listBean = new ArrayList<AvgV_Area>();
			listBean.clear();
			DecimalFormat decimalFormat = new DecimalFormat("##0.000");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				AvgV_Area mBean = new AvgV_Area();

				if (jsonobj.get("AvgV").equals(null)) {
					mBean.setAverageV(0);
				} else {					
					mBean.setAverageV(Double.parseDouble(decimalFormat.format(jsonobj.getDouble("AvgV"))));
				}

				if (jsonobj.get("DDate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("DDate"));
				}
				listBean.add(mBean);
			}
		} catch (JSONException e) {
            Log.e("JSON转换成LIST 区平均电压","JSON转换成LIST 区平均电压 JSONException");
		
		}
		return listBean;
	}
	
	// JSON解析为Bean数据 （区效应系数）
	public static List<Ae_Area> JsonArrayToAreaAeBean(String data) {

		List<Ae_Area> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);
			listBean = new ArrayList<Ae_Area>();
			listBean.clear();
			DecimalFormat decimalFormat = new DecimalFormat("##0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				Ae_Area mBean = new Ae_Area();

				if (jsonobj.get("AeCntS").equals(null)) {
					mBean.setAeCnt(0);
				} else {					
					mBean.setAeCnt(jsonobj.getInt("AeCntS"));
				}
				if (jsonobj.get("PotS").equals(null)) {
					mBean.setPots(0);
					
				} else {					
					mBean.setPots(jsonobj.getInt("PotS"));					
				}

				if (jsonobj.get("DDate").equals(null)) {
					mBean.setDdate("未知");
				} else {
					mBean.setDdate(jsonobj.getString("DDate"));
				}
				if(mBean.getPots()!=0){
					double AeXS=mBean.getAeCnt()/(double)mBean.getPots();
					mBean.setAeXS(Double.parseDouble(decimalFormat.format(AeXS)));
				}else{
					
				}
				listBean.add(mBean);
			}
		} catch (JSONException e) {
            Log.e("JSON解析为Bean数据 区效应系数","JSON解析为Bean数据 区效应系数 JSONException"); 		
		}
		return listBean;
	}
}
