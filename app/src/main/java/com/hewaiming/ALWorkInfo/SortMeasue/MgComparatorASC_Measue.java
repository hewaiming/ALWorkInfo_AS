package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class MgComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		float mg1,mg2;		
		try {
			mg1=Float.valueOf(((MeasueTable)o1).getMgCnt());
		} catch (Exception e) {
			mg1=0;
		}
		try {
			mg2=Float.valueOf(((MeasueTable)o2).getMgCnt());
		} catch (Exception e) {
			mg2=0;
		}				
		return (mg1 == mg2 ? 0 : (mg1 > mg2 ? 1 : -1));
	}	
	
}
