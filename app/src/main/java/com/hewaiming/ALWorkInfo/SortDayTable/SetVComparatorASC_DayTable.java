package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class SetVComparatorASC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		double setV1=((dayTable)o1).getSetV();
		double setV2=((dayTable)o2).getSetV();		
		return (setV1 == setV2 ? 0 : (setV1 > setV2 ? 1 : -1));
	}
	
}
