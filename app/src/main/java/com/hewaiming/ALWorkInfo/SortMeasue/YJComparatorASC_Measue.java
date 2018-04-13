package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class YJComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int YJ1,YJ2;		
		try {
			YJ1=Integer.valueOf(((MeasueTable)o1).getLDYJ());
		} catch (Exception e) {
			YJ1=0;
		}
		try {
			YJ2=Integer.valueOf(((MeasueTable)o2).getLDYJ());
		} catch (Exception e) {
			YJ2=0;
		}				
		return (YJ1 == YJ2 ? 0 : (YJ1 > YJ2 ? 1 : -1));
	}	
	
}
