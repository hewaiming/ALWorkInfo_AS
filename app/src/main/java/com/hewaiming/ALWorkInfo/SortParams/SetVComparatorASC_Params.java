package com.hewaiming.ALWorkInfo.SortParams;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.SetParams;

public class SetVComparatorASC_Params implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		double setV1=((SetParams)o1).getSetV();
		double setV2=((SetParams)o2).getSetV();		
		return (setV1 == setV2 ? 0 : (setV1 > setV2 ? 1 : -1));
	}
	
}
