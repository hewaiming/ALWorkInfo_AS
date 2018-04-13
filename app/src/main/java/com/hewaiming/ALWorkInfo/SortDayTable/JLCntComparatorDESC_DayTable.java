package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class JLCntComparatorDESC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int JLCnt1=((dayTable)o1).getJLCnt();
		int JLCnt2=((dayTable)o2).getJLCnt();		
		return (JLCnt1 ==JLCnt2 ? 0 : (JLCnt2 > JLCnt1 ? 1 : -1));
	}
	
	
}
