package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class AeTimeComparatorDESC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int aeTime1=((dayTable)o1).getAeTime();
		int aeTime2=((dayTable)o2).getAeTime();		
		return (aeTime1 == aeTime2 ? 0 : (aeTime2 > aeTime1 ? 1 : -1));
	}
	
	
}
