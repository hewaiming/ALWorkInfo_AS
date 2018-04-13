package com.hewaiming.ALWorkInfo.SortMeasue;

import java.util.Comparator;

import com.hewaiming.ALWorkInfo.bean.MeasueTable;

@SuppressWarnings("rawtypes")
public class LSPComparatorASC_Measue implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {
		int LSP1,LSP2;		
		try {
			LSP1=Integer.valueOf(((MeasueTable)o1).getLSP());
		} catch (Exception e) {
			LSP1=0;
		}
		try {
			LSP2=Integer.valueOf(((MeasueTable)o2).getLSP());
		} catch (Exception e) {
			LSP2=0;
		}				
		return (LSP1 == LSP2 ? 0 : (LSP1 > LSP2 ? 1 : -1));
	}	
	
}
