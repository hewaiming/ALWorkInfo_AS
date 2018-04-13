package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class PotNoComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int potno1=Integer.valueOf(((MeasueTable)o1).getPotNo());
		int potno2=Integer.valueOf(((MeasueTable)o2).getPotNo());			
		return (potno1 == potno2 ? 0 : (potno1 > potno2 ? 1 : -1));
	}	
	
}
