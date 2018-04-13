package com.hewaiming.ALWorkInfo.SortParams;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.SetParams;

@SuppressWarnings("rawtypes")
public class PotNoComparatorASC_Params implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int potno1=((SetParams)o1).getPotNo();
		int potno2=((SetParams)o2).getPotNo();		
		return (potno1 == potno2 ? 0 : (potno1 > potno2 ? 1 : -1));
	}
	
	
}
