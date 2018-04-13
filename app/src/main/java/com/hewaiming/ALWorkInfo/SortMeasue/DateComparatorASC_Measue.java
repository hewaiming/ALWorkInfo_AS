package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

public class DateComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {	
	
		String name1=((MeasueTable)o1).getDdate();
		String name2=((MeasueTable)o2).getDdate();
		int potno1=Integer.valueOf(((MeasueTable)o1).getPotNo());
		int potno2=Integer.valueOf(((MeasueTable)o2).getPotNo());
		int compareName = name1.compareTo(name2);
		if (compareName == 0) {
			return (potno1 == potno2 ? 0 : (potno1 > potno2 ? 1 : -1));
		}
		return compareName;
	}
	
	
}
