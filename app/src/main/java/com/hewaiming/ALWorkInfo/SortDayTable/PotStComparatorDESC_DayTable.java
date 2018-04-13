package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class PotStComparatorDESC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		String name1=((dayTable)o1).getPotSt();
		String name2=((dayTable)o2).getPotSt();
		int potno1=((dayTable)o1).getPotNo();
		int potno2=((dayTable)o2).getPotNo();
		int compareName = name2.compareTo(name1);
		if (compareName == 0) {
			return (potno1 == potno2 ? 0 : (potno1 > potno2 ? 1 : -1));
		}
		return compareName;
	}
	
	
}
