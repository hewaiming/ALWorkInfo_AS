package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class RealSetVComparatorDESC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		double RealsetV1=((dayTable)o1).getRealSetV();
		double RealsetV2=((dayTable)o2).getRealSetV();		
		return (RealsetV1 == RealsetV2 ? 0 : (RealsetV2 > RealsetV1 ? 1 : -1));
	}
	
}
