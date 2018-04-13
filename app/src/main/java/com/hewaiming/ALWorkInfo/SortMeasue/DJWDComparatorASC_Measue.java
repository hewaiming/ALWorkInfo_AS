package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class DJWDComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int DJWD1,DJWD2;		
		try {
			DJWD1=Integer.valueOf(((MeasueTable)o1).getDJWD());
		} catch (Exception e) {
			DJWD1=0;
		}
		try {
			DJWD2=Integer.valueOf(((MeasueTable)o2).getDJWD());
		} catch (Exception e) {
			DJWD2=0;
		}				
		return (DJWD1 == DJWD2 ? 0 : (DJWD1 > DJWD2 ? 1 : -1));
	}	
	
}
