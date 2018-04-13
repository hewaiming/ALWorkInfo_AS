package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class FeComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		float fe1,fe2;		
		try {
			fe1=Float.valueOf(((MeasueTable)o1).getFeCnt());
		} catch (Exception e) {
			fe1=0;
		}
		try {
			fe2=Float.valueOf(((MeasueTable)o2).getFeCnt());
		} catch (Exception e) {
			fe2=0;
		}				
		return (fe1 == fe2 ? 0 : (fe1 > fe2 ? 1 : -1));
	}	
	
}
