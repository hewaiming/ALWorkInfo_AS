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
	 * 存放发送图片的目录
	 */
	public static String ALWorkInfo_PICTURE_PATH = Environment.getExternalStorageDirectory() + "/ALWorkInfo/image/";

	/**
	 * 我的头像保存目录
	 */
	public static String MyAvatarDir = "/sdcard/ALWorkInfo/avatar/";
	/**
	 * 拍照回调
	 */
	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;// 拍照修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;// 本地相册修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;// 系统裁剪头像

	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;// 拍照
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;// 本地图片
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;// 位置
	public static final String EXTRA_STRING = "extra_string";
	
	public static final String[] Areas = { "一厂房一区", "一厂房二区", "一厂房三区", "二厂房一区", "二厂房二区", "二厂房三区" };
	
	public static final String[] PIC_ADDRESS = { ":8000/scgy/android/banner/1.jpg",
			":8000/scgy/android/banner/2.jpg",
			":8000/scgy/android/banner/3.jpg", 
			":8000/scgy/android/banner/4.jpg",
			":8000/scgy/android/banner/5.jpg" };
	
	public static final String[] iconName = { "槽状态", "实时曲线", "效应报表", "槽龄", "槽压曲线", "故障记录", "实时记录", "操作记录", 
			"常用参数","测量数据", "效应槽", "效应记录","槽故障最多","工艺曲线", "报警记录" ,"日报","下料异常","区平均电压","区效应系数"};
	
	public static final int[] drawable = { R.drawable.potstatus, R.drawable.realtime, R.drawable.aetable, R.drawable.ages,
			R.drawable.potv, R.drawable.faultrecord, R.drawable.realrecord, R.drawable.operaterecord,
			R.drawable.params, R.drawable.measue, R.drawable.aemost, R.drawable.aerecord,
			R.drawable.faultmost,R.drawable.gongyi ,R.drawable.alertrecord,R.drawable.daytable,R.drawable.abnormal,R.drawable.area_avgv,R.drawable.area_ae};
	
	public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";// 注册成功之后登陆页面退出

	public static final String[] Areas_ALL = { "所有厂房","一厂房","二厂房","一厂房一区", "一厂房二区", "一厂房三区", "二厂房一区", "二厂房二区", "二厂房三区" };;
	
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
						Toast.makeText(mContext.getApplicationContext(), "保存【引导界面显示】参数失败"+showName, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(mContext.getApplicationContext(), "保存【引导界面显示】参数失败"+showName, Toast.LENGTH_LONG).show();
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
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
		oks.setTitle("铝电解工作站安卓版");
		// titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
		oks.setTitleUrl("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我已使用手机版铝电解工作站有一段时间啦！程序地址：http://125.64.59.11:8000/scgy/android/alworkinfo.apk");
		// 分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		oks.setImageUrl("http://125.64.59.11:8000/scgy/android/banner/share.png");
		oks.setFilePath("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
		
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
	
		oks.setUrl("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("软件很好用！");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://125.64.59.11:8000/scgy/android/ALWorkInfo.apk");

		// 启动分享GUI
		oks.show(mContext);
	}	
}
