package com.hewaiming.ALWorkInfo.SortDayTable;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class RunTimeComparatorASC_DayTable implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int RunTime1=((dayTable)o1).getRunTime();
		int RunTime2=((dayTable)o2).getRunTime();		
		return (RunTime1 == RunTime2 ? 0 : (RunTime1 > RunTime2 ? 1 : -1));
	}
	
	
}
