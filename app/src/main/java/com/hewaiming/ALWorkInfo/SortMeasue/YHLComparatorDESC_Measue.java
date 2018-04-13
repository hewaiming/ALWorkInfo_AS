package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class YHLComparatorDESC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		float YHL1,YHL2;		
		try {
			YHL1=Float.valueOf(((MeasueTable)o1).getALOCnt());
		} catch (Exception e) {
			YHL1=0;
		}
		try {
			YHL2=Float.valueOf(((MeasueTable)o2).getALOCnt());
		} catch (Exception e) {
			YHL2=0;
		}				
		return (YHL1 == YHL2 ? 0 : (YHL2 > YHL1 ? 1 : -1));
	}	
	
}
