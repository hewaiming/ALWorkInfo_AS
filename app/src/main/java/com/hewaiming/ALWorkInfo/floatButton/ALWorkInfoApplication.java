package com.hewaiming.ALWorkInfo.floatButton;

import android.app.Application;
import android.view.WindowManager;
/**
 * 
 * @author ∞§Ãﬂ»À…˙
 * @website www.glmei.cn
 * @date Nov 29, 2014
 * @version 1.0.0
 *
 */
public class ALWorkInfoApplication extends Application {
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getWindowParams() {
		return windowParams;
	}
}
