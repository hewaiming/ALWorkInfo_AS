package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class FHLCntComparatorDESC_DayTable implements Comparator<Object> {
	@Override
	public int compare(Object o1, Object o2) {
		int FHLCnt1=((dayTable)o1).getFhlCnt();
		int FHLCnt2=((dayTable)o2).getFhlCnt();		
		return (FHLCnt1 == FHLCnt2 ? 0 : (FHLCnt2 > FHLCnt1 ? 1 : -1));
	}
	
	
}
