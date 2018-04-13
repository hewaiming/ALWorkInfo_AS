package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class FHLCntComparatorASC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int FHLCnt1=((dayTable)o1).getFhlCnt();
		int FHLCnt2=((dayTable)o2).getFhlCnt();		
		return (FHLCnt1 == FHLCnt2 ? 0 : (FHLCnt1 > FHLCnt2 ? 1 : -1));
	}
	
	
}
