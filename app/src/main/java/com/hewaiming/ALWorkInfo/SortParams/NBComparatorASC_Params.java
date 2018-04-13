package com.hewaiming.ALWorkInfo.SortParams;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.SetParams;

@SuppressWarnings("rawtypes")
public class NBComparatorASC_Params implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int nb1=((SetParams)o1).getNBTime();
		int nb2=((SetParams)o2).getNBTime();		
		return (nb1 == nb2 ? 0 : (nb1 > nb2 ? 1 : -1));
	}
	
	
}
