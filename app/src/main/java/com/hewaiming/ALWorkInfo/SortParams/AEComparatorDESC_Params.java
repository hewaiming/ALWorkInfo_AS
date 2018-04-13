package com.hewaiming.ALWorkInfo.SortParams;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.SetParams;

@SuppressWarnings("rawtypes")
public class AEComparatorDESC_Params implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int ae1=((SetParams)o1).getAETime();
		int ae2=((SetParams)o2).getAETime();		
		return (ae1 == ae2 ? 0 : (ae2 > ae1 ? 1 : -1));
	}
	
	
}
