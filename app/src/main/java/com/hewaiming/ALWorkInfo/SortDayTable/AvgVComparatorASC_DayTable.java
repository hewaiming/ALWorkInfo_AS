package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class AvgVComparatorASC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		double avgV1=((dayTable)o1).getAverageV();
		double avgV2=((dayTable)o2).getAverageV();		
		return (avgV1 == avgV2 ? 0 : (avgV1 > avgV2 ? 1 : -1));
	}
	
}
