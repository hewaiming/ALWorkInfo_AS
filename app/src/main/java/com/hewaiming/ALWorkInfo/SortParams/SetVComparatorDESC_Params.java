package com.hewaiming.ALWorkInfo.SortParams;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.SetParams;
import com.hewaiming.ALWorkInfo.bean.dayTable;

public class SetVComparatorDESC_Params implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		double setV1=((SetParams)o1).getSetV();
		double setV2=((SetParams)o2).getSetV();		
		return (setV1 == setV2 ? 0 : (setV2 > setV1 ? 1 : -1));
	}
	
}
