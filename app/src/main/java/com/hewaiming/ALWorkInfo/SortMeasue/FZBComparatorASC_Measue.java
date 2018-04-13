package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class FZBComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		float fzb1,fzb2;		
		try {
			fzb1=Float.valueOf(((MeasueTable)o1).getFZB());
		} catch (Exception e) {
			fzb1=0;
		}
		try {
			fzb2=Float.valueOf(((MeasueTable)o2).getFZB());
		} catch (Exception e) {
			fzb2=0;
		}				
		return (fzb1 == fzb2 ? 0 : (fzb1 > fzb2 ? 1 : -1));
	}	
	
}
