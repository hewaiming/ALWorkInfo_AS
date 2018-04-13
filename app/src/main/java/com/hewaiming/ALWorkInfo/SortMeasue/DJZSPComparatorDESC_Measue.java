package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class DJZSPComparatorDESC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int DJZSP1,DJZSP2;		
		try {
			DJZSP1=Integer.valueOf(((MeasueTable)o1).getDJZSP());
		} catch (Exception e) {
			DJZSP1=0;
		}
		try {
			DJZSP2=Integer.valueOf(((MeasueTable)o2).getDJZSP());
		} catch (Exception e) {
			DJZSP2=0;
		}				
		return (DJZSP1 == DJZSP2 ? 0 : (DJZSP2 > DJZSP1 ? 1 : -1));
	}	
	
}
