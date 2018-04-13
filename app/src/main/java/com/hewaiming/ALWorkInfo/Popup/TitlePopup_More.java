package com.hewaiming.ALWorkInfo.Popup;

import java.util.ArrayList;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.ui.CraftLineActivity;
import com.hewaiming.ALWorkInfo.ui.MeasueTableActivity;
import com.hewaiming.ALWorkInfo.ui.RealTimeLineActivity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yangyu
 *	�������������ⰴť�ϵĵ������̳���PopupWindow��
 */
public class TitlePopup_More extends PopupWindow {
	private Context mContext;
	private String  PotNo,ip;
	private boolean IsHide;
	private int port;
    private List<String> dateBean;
	//�б����ļ��
	protected final int LIST_PADDING = 10;
	
	//ʵ����һ������
	private Rect mRect = new Rect();
	
	//�����λ�ã�x��y��
	private final int[] mLocation = new int[2];
	
	//��Ļ�Ŀ�Ⱥ͸߶�
	private int mScreenWidth,mScreenHeight;

	//�ж��Ƿ���Ҫ��ӻ�����б�������
	private boolean mIsDirty;
	
	//λ�ò�������
	private int popupGravity = Gravity.NO_GRAVITY;	
	
	//����������ѡ��ʱ�ļ���
	private OnItemOnClickListener mItemOnClickListener;
	
	//�����б����
	private ListView mListView;
	
	//���嵯���������б�
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();			
	
	public TitlePopup_More(Context context,String mPotNo,String mIP,int mPort,ArrayList<String> mBean,boolean mIsHide){
		//���ò��ֵĲ���
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, mPotNo,mIP,mPort,mBean,mIsHide);
	}
	
	
	public TitlePopup_More(Context context, int width, int height,String mPotNo,String mIP,int mPort,List<String> mBean,boolean mIsHide){
		this.mContext = context;
		this.PotNo=mPotNo;
		this.ip=mIP;
		this.port=mPort;
		this.dateBean=mBean;
		this.IsHide=mIsHide;
		//���ÿ��Ի�ý���
		setFocusable(true);
		//���õ����ڿɵ��
		setTouchable(true);	
		//���õ�����ɵ��
		setOutsideTouchable(true);
		
		//�����Ļ�Ŀ�Ⱥ͸߶�
		mScreenWidth = Util.getScreenWidth(mContext);
		mScreenHeight = Util.getScreenHeight(mContext);
		
		//���õ����Ŀ�Ⱥ͸߶�
		setWidth(width);
		setHeight(height);
		
		setBackgroundDrawable(new BitmapDrawable());
		
		//���õ����Ĳ��ֽ���
		setContentView(LayoutInflater.from(mContext).inflate(R.layout.title_popup, null));
		
		initUI();
	}
		
	
	/**
	 * ��ʼ�������б�
	 */
	private void initUI(){
		mListView = (ListView) getContentView().findViewById(R.id.title_list);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {
				//���������󣬵�����ʧ
				dismiss();				
				switch (index) {
				case 0:					
					Intent craft_intent = new Intent(mContext, CraftLineActivity.class);
					Bundle craftBundle = new Bundle();
					craftBundle.putStringArrayList("date_table", (ArrayList<String>)dateBean );
					craftBundle.putString("PotNo_Selected", PotNo);
					craftBundle.putString("ip", ip);
					craftBundle.putInt("port", port);
					craft_intent.putExtras(craftBundle);									 				
					arg1.getContext().startActivity(craft_intent);	// ��������				
					break;
				case 1:
					 Intent mIntent=new Intent(mContext, RealTimeLineActivity.class);	
					 Bundle mBundle=new Bundle();
					 mBundle.putString("PotNo", PotNo);
					 mBundle.putBoolean("Hide_Action", IsHide);
					 mBundle.putString("ip", ip);
					 mBundle.putInt("port", port);
					 mIntent.putExtras(mBundle);
					 arg1.getContext().startActivity(mIntent);	
					break;
				
				}
				if(mItemOnClickListener != null){
					mItemOnClickListener.onItemClick(mActionItems.get(index), index);					
				}
					
			}
		}); 
	}
	
	/**
	 * ��ʾ�����б����
	 */
	public void show(View view){
		//��õ����Ļ��λ������
		view.getLocationOnScreen(mLocation);
		
		//���þ��εĴ�С
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),mLocation[1] + view.getHeight());
		
		//�ж��Ƿ���Ҫ��ӻ�����б�������
		if(mIsDirty){
			populateActions();
		}
		
		//��ʾ������λ��
		showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - (getWidth()/2), mRect.bottom);
	}
	
	/**
	 * ���õ����б�����
	 */
	private void populateActions(){
		mIsDirty = false;
		
		//�����б��������
		mListView.setAdapter(new BaseAdapter() {			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = null;
				
				if(convertView == null){
					textView = new TextView(mContext);
					textView.setTextColor(mContext.getResources().getColor(android.R.color.white));
					textView.setTextSize(12);
					//�����ı�����
					textView.setGravity(Gravity.CENTER);
					//�����ı���ķ�Χ
					textView.setPadding(0, 10, 0, 10);
					//�����ı���һ������ʾ�������У�
					textView.setSingleLine(true);
				}else{
					textView = (TextView) convertView;
				}
				
				ActionItem item = mActionItems.get(position);
				
				//�����ı�����
				textView.setText(item.mTitle);
				//����������ͼ��ļ��
				textView.setCompoundDrawablePadding(10);
				//���������ֵ���߷�һ��ͼ��
                textView.setCompoundDrawablesWithIntrinsicBounds(item.mDrawable, null , null, null);
				
                return textView;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return mActionItems.get(position);
			}
			
			@Override
			public int getCount() {
				return mActionItems.size();
			}
		}) ;
	}
	
	/**
	 * ���������
	 */
	public void addAction(ActionItem action){
		if(action != null){
			mActionItems.add(action);
			mIsDirty = true;
		}
	}
	
	/**
	 * ���������
	 */
	public void cleanAction(){
		if(mActionItems.isEmpty()){
			mActionItems.clear();
			mIsDirty = true;
		}
	}
	
	/**
	 * ����λ�õõ�������
	 */
	public ActionItem getAction(int position){
		if(position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}			
	
	/**
	 * ���ü����¼�
	 */
	public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
		this.mItemOnClickListener = onItemOnClickListener;
	}
	
	/**
	 * @author yangyu
	 *	�������������������ť�����¼�
	 */
	public static interface OnItemOnClickListener{
		public void onItemClick(ActionItem item , int position);
	}
}
