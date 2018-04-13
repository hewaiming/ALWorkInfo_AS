package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class AeCntComparatorDESC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int aeCnt1=((dayTable)o1).getAeCnt();
		int aeCnt2=((dayTable)o2).getAeCnt();		
		return (aeCnt1 == aeCnt2 ? 0 : (aeCnt2 > aeCnt1 ? 1 : -1));
	}
	
	
}
