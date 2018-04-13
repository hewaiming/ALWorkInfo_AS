package com.hewaiming.ALWorkInfo.SortParams;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.SetParams;

@SuppressWarnings("rawtypes")
public class FHLComparatorASC_Params implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int fhl1=((SetParams)o1).getALF();
		int fhl2=((SetParams)o2).getALF();		
		return (fhl1 == fhl2 ? 0 : (fhl1 > fhl2 ? 1 : -1));
	}
	
	
}
