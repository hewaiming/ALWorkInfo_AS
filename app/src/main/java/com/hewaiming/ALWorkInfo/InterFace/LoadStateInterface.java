package com.hewaiming.ALWorkInfo.InterFace;

import java.util.List;

import com.hewaiming.ALWorkInfo.bean.OperateRecord;

public interface LoadStateInterface {
	/* ������� */
	public void onLoadComplete(List<OperateRecord> remotDate);
}
