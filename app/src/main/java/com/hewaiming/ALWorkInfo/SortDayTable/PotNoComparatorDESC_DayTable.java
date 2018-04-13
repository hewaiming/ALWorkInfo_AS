package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class PotNoComparatorDESC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {		
		
		int potno1=((dayTable)o1).getPotNo();
		int potno2=((dayTable)o2).getPotNo();		
		return (potno1 == potno2 ? 0 : (potno2 > potno1 ? 1 : -1));
		
	}
	
	
}
