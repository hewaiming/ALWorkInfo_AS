package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class CBWDComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		float cbwd1,cbwd2;		
		try {
			cbwd1=Float.valueOf(((MeasueTable)o1).getMLSP());
		} catch (Exception e) {
			cbwd1=0;
		}
		try {
			cbwd2=Float.valueOf(((MeasueTable)o2).getMLSP());
		} catch (Exception e) {
			cbwd2=0;
		}				
		return (cbwd1 == cbwd2 ? 0 : (cbwd1 > cbwd2 ? 1 : -1));
	}	
	
}
