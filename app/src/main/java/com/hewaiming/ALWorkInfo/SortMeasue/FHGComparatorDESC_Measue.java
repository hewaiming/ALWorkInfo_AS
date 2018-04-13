package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class FHGComparatorDESC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		float FHG1,FHG2;		
		try {
			FHG1=Float.valueOf(((MeasueTable)o1).getCaFCnt());
		} catch (Exception e) {
			FHG1=0;
		}
		try {
			FHG2=Float.valueOf(((MeasueTable)o2).getCaFCnt());
		} catch (Exception e) {
			FHG2=0;
		}				
		return (FHG1 == FHG2 ? 0 : (FHG2 > FHG1 ? 1 : -1));
	}	
	
}
