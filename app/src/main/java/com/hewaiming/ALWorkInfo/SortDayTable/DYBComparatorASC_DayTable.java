package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class DYBComparatorASC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int dyb1=((dayTable)o1).getDybTime();
		int dyb2=((dayTable)o2).getDybTime();		
		return (dyb1 == dyb2 ? 0 : (dyb1 > dyb2 ? 1 : -1));
	}
	
	
}
