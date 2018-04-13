package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class ALCntComparatorDESC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int ALCnt1=((dayTable)o1).getAlCntZSL();
		int ALCnt2=((dayTable)o2).getAlCntZSL();		
		return (ALCnt1 == ALCnt2 ? 0 : (ALCnt2 > ALCnt1 ? 1 : -1));
	}
	
	
}
