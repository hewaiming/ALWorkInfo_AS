package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class AeVComparatorASC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		double aeV1=((dayTable)o1).getAeV();
		double aeV2=((dayTable)o2).getAeV();		
		return (aeV1 == aeV2 ? 0 : (aeV1 > aeV2 ? 1 : -1));
	}
	
}
