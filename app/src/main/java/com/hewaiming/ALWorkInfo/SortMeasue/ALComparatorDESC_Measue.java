package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class ALComparatorDESC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int AL1,AL2;		
		try {
			AL1=Integer.valueOf(((MeasueTable)o1).getALCnt());
		} catch (Exception e) {
			AL1=0;
		}
		try {
			AL2=Integer.valueOf(((MeasueTable)o2).getALCnt());
		} catch (Exception e) {
			AL2=0;
		}				
		return (AL1 == AL2 ? 0 : (AL2 > AL1 ? 1 : -1));
	}	
	
}
