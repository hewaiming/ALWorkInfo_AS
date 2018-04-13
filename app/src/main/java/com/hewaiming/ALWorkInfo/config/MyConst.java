package com.hewaiming.ALWorkInfo.config;

import com.hewaiming.ALWorkInfo.R;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MyConst {

	/**
	 * ��ŷ���ͼƬ��Ŀ¼
	 */
	public static String ALWorkInfo_PICTURE_PATH = Environment.getExternalStorageDirectory() + "/ALWorkInfo/image/";

	/**
	 * �ҵ�ͷ�񱣴�Ŀ¼
	 */
	public static String MyAvatarDir = "/sdcard/ALWorkInfo/avatar/";
	/**
	 * ���ջص�
	 */
	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;// �����޸�ͷ��
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;// ��������޸�ͷ��
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;// ϵͳ�ü�ͷ��

	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;// ����
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;// ����ͼƬ
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;// λ��
	public static final String EXTRA_STRING = "extra_string";
	
	public static final String[] Areas = { "һ����һ��", "һ��������", "һ��������", "������һ��", "����������", "����������" };
	
	public static final String[] PIC_ADDRESS = { ":8000/scgy/android/banner/1.jpg",
			":8000/scgy/android/banner/2.jpg",
			":8000/scgy/android/banner/3.jpg", 
			":8000/scgy/android/banner/4.jpg",
			":8000/scgy/android/banner/5.jpg" };
	
	public static final String[] iconName = { "��״̬", "ʵʱ����", "ЧӦ����", "����", "��ѹ����", "���ϼ�¼", "ʵʱ��¼", "������¼", 
			"���ò���","��������", "ЧӦ��", "ЧӦ��¼","�۹������","��������", "������¼" ,"�ձ�","�����쳣","��ƽ����ѹ","��ЧӦϵ��"};
	
	public static final int[] drawable = { R.drawable.potstatus, R.drawable.realtime, R.drawable.aetable, R.drawable.ages,
			R.drawable.potv, R.drawable.faultrecord, R.drawable.realrecord, R.drawable.operaterecord,
			R.drawable.params, R.drawable.measue, R.drawable.aemost, R.drawable.aerecord,
			R.drawable.faultmost,R.drawable.gongyi ,R.drawable.alertrecord,R.drawable.daytable,R.drawable.abnormal,R.drawable.area_avgv,R.drawable.area_ae};
	
	public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";// ע��ɹ�֮���½ҳ���˳�

	public static final String[] Areas_ALL = { "���г���","һ����","������","һ����һ��", "һ��������", "һ��������", "������һ��", "����������", "����������" };;
	
	public static void GuideDialog_show(final Context mContext,final String showName) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog_Fullscreen);
		dialog.setContentView(R.layout.activity_guide_dialog);
		RelativeLayout mLayout=(RelativeLayout) dialog.findViewById(R.id.Layout_backgroup);
		mLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				SharedPreferences sp;
				sp = mContext.getSharedPreferences("GuideDiaLogIsShow", mContext.MODE_PRIVATE);
				if (sp!=null){
					Editor editor = sp.edit();
					editor.putBoolean(showName, true);				
					if (!editor.commit()){
						Toast.makeText(mContext.getApplicationContext(), "���桾����������ʾ������ʧ��"+showName, Toast.LENGTH_SHORT).show();
					}
				}
				
			}
		});
		ImageView iv = (ImageView) dialog.findViewById(R.id.ivNavigater_click);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				SharedPreferences sp;
				sp = mContext.getSharedPreferences("GuideDiaLogIsShow", mContext.MODE_PRIVATE);
				if (sp!=null){
					Editor editor = sp.edit();
					editor.putBoolean(showName, true);				
					if (!editor.commit()){
						Toast.makeText(mContext.getApplicationContext(), "���桾����������ʾ������ʧ��"+showName, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		dialog.show();	
	}	
	
	public static boolean isEqual(double a, double b) {
	    if (Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b)) {
	       return false;
	    }
	    return (a - b) < 0.001d;
	}
	public static boolean GetDataFromSharePre(Context mContext,String showName) {		
		SharedPreferences sp;
		sp = mContext.getSharedPreferences("GuideDiaLogIsShow", mContext.MODE_PRIVATE);
		if(sp!=null){
			 return sp.getBoolean(showName, false);			
		}	
		return false;
	}
	public static void showShare(Context mContext) {
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ��QQ�ռ�ʹ��
		oks.setTitle("����⹤��վ��׿��");
		// titleUrl�Ǳ�����������ӣ�����Linked-in,QQ��QQ�ռ�ʹ��
		oks.setTitleUrl("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("����ʹ���ֻ�������⹤��վ��һ��ʱ�����������ַ��http://125.64.59.11:8000/scgy/android/alworkinfo.apk");
		// ��������ͼƬ������΢����������ͼƬ��Ҫͨ����˺�����߼�д��ӿڣ�������ע�͵���������΢��
		oks.setImageUrl("http://125.64.59.11:8000/scgy/android/banner/share.png");
		oks.setFilePath("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
		
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		// oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
	
		oks.setUrl("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("����ܺ��ã�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");

		// ��������GUI
		oks.show(mContext);
	}	
}
