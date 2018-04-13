package com.hewaiming.ALWorkInfo.adapter;

import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.bean.SetParams;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Params_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<SetParams> mList;
	private Context mContext;
//	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//	DisplayImageOptions options;

	public Params_Adapter(Context mContext, List<SetParams> mList) {
		this.inflater = LayoutInflater.from(mContext);
		this.mList = mList;
		this.mContext=mContext;
//		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.nopic).showImageOnLoading(R.drawable.loading)
//				.showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading(true).cacheOnDisc(true)
//				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
//				.considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();		
	}

	@Override
	public int getCount() {

		return mList.size();
	}
	
	@Override
	public Object getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SetParams entity = mList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_params, null);
			holder.PotNo_tv = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.PotNo_tv.setTextColor(mContext.getResources().getColor(R.color.text_black));
			holder.SetV_tv = (TextView) convertView.findViewById(R.id.tv_SetV);
			holder.SetV_tv.setTextColor(mContext.getResources().getColor(R.color.text_black));
			holder.NBTime_tv = (TextView) convertView.findViewById(R.id.tv_NbTime);
			holder.NBTime_tv.setTextColor(mContext.getResources().getColor(R.color.text_black));
			holder.AETime_tv = (TextView) convertView.findViewById(R.id.tv_AeTime);
			holder.AETime_tv.setTextColor(mContext.getResources().getColor(R.color.text_black));
			holder.ALF_tv = (TextView) convertView.findViewById(R.id.tv_ALF);
			holder.ALF_tv.setTextColor(mContext.getResources().getColor(R.color.text_black));
		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TextPaint tPaint = holder.PotNo_tv.getPaint();
		tPaint.setFakeBoldText(true);
		holder.PotNo_tv.setText(entity.getPotNo()+"");
		holder.SetV_tv.setText(entity.getSetV()+"");
		holder.NBTime_tv.setText(entity.getNBTime()+"");
		holder.AETime_tv.setText(entity.getAETime()+"");
		holder.ALF_tv.setText(entity.getALF()+"");
		return convertView;
	}

	class ViewHolder {
		TextView PotNo_tv;		
		TextView SetV_tv;
		TextView NBTime_tv;
		TextView AETime_tv;
		TextView ALF_tv;		
	}

	public void onDateChange(List<SetParams> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

	/*private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}*/
}
