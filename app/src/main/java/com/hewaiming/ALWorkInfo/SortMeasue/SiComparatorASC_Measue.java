package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class SiComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		float si1,si2;		
		try {
			si1=Float.valueOf(((MeasueTable)o1).getSiCnt());
		} catch (Exception e) {
			si1=0;
		}
		try {
			si2=Float.valueOf(((MeasueTable)o2).getSiCnt());
		} catch (Exception e) {
			si2=0;
		}				
		return (si1 == si2 ? 0 : (si1 > si2 ? 1 : -1));
	}	
	
}
