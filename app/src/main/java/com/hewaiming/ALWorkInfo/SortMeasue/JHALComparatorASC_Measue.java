package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class JHALComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int AL1,AL2;		
		try {
			AL1=Integer.valueOf(((MeasueTable)o1).getJHCL());
		} catch (Exception e) {
			AL1=0;
		}
		try {
			AL2=Integer.valueOf(((MeasueTable)o2).getJHCL());
		} catch (Exception e) {
			AL2=0;
		}				
		return (AL1 == AL2 ? 0 : (AL1 > AL2 ? 1 : -1));
	}	
	
}
