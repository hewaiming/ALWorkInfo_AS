package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class YHLCntComparatorASC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int yhlCnt1=((dayTable)o1).getYhlCnt();
		int yhlCnt2=((dayTable)o2).getYhlCnt();		
		return (yhlCnt1 == yhlCnt2 ? 0 : (yhlCnt1 > yhlCnt2 ? 1 : -1));
	}
	
	
}
