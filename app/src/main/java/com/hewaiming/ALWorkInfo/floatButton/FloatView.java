package com.hewaiming.ALWorkInfo.floatButton;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
/**
 * 
 * @author ��������
 * @website www.glmei.cn
 * @date Nov 29, 2014
 * @version 1.0.0
 *
 */
public class FloatView extends ImageView{
	private float mTouchX;
	private float mTouchY;
	private float x;
	private float y;
	private float mStartX;
	private float mStartY;
	private OnClickListener mClickListener;

	private WindowManager windowManager = (WindowManager) getContext()
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	// ��windowManagerParams����Ϊ��ȡ��ȫ�ֱ��������Ա����������ڵ�����
	private WindowManager.LayoutParams windowManagerParams = ((ALWorkInfoApplication) getContext()
			.getApplicationContext()).getWindowParams();

	public FloatView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//��ȡ��״̬���ĸ߶�
		Rect frame =  new  Rect();  
		getWindowVisibleDisplayFrame(frame);
		int  statusBarHeight = frame.top - 48; 
		System.out.println("statusBarHeight:"+statusBarHeight);
		// ��ȡ�����Ļ�����꣬������Ļ���Ͻ�Ϊԭ��
		x = event.getRawX();
		y = event.getRawY() - statusBarHeight; // statusBarHeight��ϵͳ״̬���ĸ߶�
		Log.i("tag", "currX" + x + "====currY" + y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // ������ָ�������¶���
			// ��ȡ���View�����꣬���Դ�View���Ͻ�Ϊԭ��
			mTouchX = event.getX();
			mTouchY = event.getY();
			mStartX = x;
			mStartY = y;
			Log.i("tag", "startX" + mTouchX + "====startY"
					+ mTouchY);
			break;

		case MotionEvent.ACTION_MOVE: // ������ָ�����ƶ�����
			updateViewPosition();
			break;

		case MotionEvent.ACTION_UP: // ������ָ�����뿪����
			updateViewPosition();
			mTouchX = mTouchY = 0;
			if ((x - mStartX) < 5 && (y - mStartY) < 5) {
				if(mClickListener!=null) {
					mClickListener.onClick(this);
				}
			}
			break;
		}
		return true;
	}
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mClickListener = l;
	}
	private void updateViewPosition() {
		// ���¸�������λ�ò���
		windowManagerParams.x = (int) (x - mTouchX);
		windowManagerParams.y = (int) (y - mTouchY);
		windowManager.updateViewLayout(this, windowManagerParams); // ˢ����ʾ
	}


}
