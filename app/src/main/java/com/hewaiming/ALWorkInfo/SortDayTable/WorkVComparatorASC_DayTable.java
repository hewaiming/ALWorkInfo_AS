package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class WorkVComparatorASC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		double WorkV1=((dayTable)o1).getWorkV();
		double WorkV2=((dayTable)o2).getWorkV();		
		return (WorkV1 == WorkV2 ? 0 : (WorkV1 > WorkV2 ? 1 : -1));
	}
	
}
