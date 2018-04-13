package com.hewaiming.ALWorkInfo.config;

import android.graphics.Bitmap;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.R.anim;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageLoadOptions {

	public static DisplayImageOptions getOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				// // ����ͼƬ�������ڼ���ʾ��ͼƬ
				.showStubImage(anim.progress_round_big)
				// // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				 .showImageForEmptyUri(R.drawable.banner_empty)
				// // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
				 .showImageOnFail(R.drawable.error1)
				.cacheInMemory(true)
				// �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc(false)
				// �������ص�ͼƬ�Ƿ񻺴���SD����
				
				.imageScaleType(ImageScaleType.EXACTLY)// ����ͼƬ����εı��뷽ʽ��ʾ
				.bitmapConfig(Bitmap.Config.RGB_565)// ����ͼƬ�Ľ�������
				// .decodingOptions(android.graphics.BitmapFactory.Options
				// decodingOptions)//����ͼƬ�Ľ�������
			
				// ����ͼƬ����ǰ���ӳ�
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillisΪ�����õ��ӳ�ʱ��
				// ����ͼƬ���뻺��ǰ����bitmap��������
				// ��preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// ����ͼƬ������ǰ�Ƿ����ã���λ
				//.displayer(new RoundedBitmapDisplayer(20))//�Ƿ�����ΪԲ�ǣ�����Ϊ����
				.displayer(new FadeInBitmapDisplayer(100))// ����
				.build();
		
		return options;
	}

}
