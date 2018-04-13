package com.hewaiming.ALWorkInfo.adapter.HScrollView;

import java.util.ArrayList;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView.OnScrollChangedListener;
import com.hewaiming.ALWorkInfo.bean.dayTable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bean.PotStatus;

public class HSView_PotStatusAdapter extends BaseAdapter {
	private static final String TAG = "HolderAdapter";

	private List<PotStatus> mList;
	/**
	 * ListViewͷ��
	 */
	private RelativeLayout mHead;
	/**
	 * layout ID
	 */
	private int id_row_layout;
	private LayoutInflater mInflater;
	int[] colors = { Color.rgb(224, 241, 242), Color.rgb(179, 213, 252) };
	// int[] colors = { Color.BLACK, Color.BLACK };

	public HSView_PotStatusAdapter(Context context, int id_row_layout, List<PotStatus> currentData,
			RelativeLayout mHead) {
		Log.v(TAG + ".HolderAdapter", " ��ʼ��");

		this.id_row_layout = id_row_layout;
		this.mInflater = LayoutInflater.from(context);
		this.mList = currentData;
		this.mHead = mHead;

	}

	public int getCount() {
		return this.mList.size();
	}

	public Object getItem(int position) {

		return mList.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	/**
	 * ��List���������
	 */
	public void addItem(List<PotStatus> items) {
		for (PotStatus item : items) {
			mList.add(item);
		}
	}

	/**
	 * ��յ�List�е�����
	 */
	public void cleanAll() {
		this.mList.clear();
	}

	public View getView(int position, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;
		PotStatus entity = mList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(id_row_layout, null);
			holder = new ViewHolder();
			MyHScrollView scrollView1 = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView1);

			holder.scrollView = scrollView1;
			holder.tvPotNo = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.tvPotSt = (TextView) convertView.findViewById(R.id.tv_PotSt);
			holder.tvAutoRun = (TextView) convertView.findViewById(R.id.tv_AutoRun);
			holder.tvOperation = (TextView) convertView.findViewById(R.id.tv_Operation);// ����ҵ
			holder.tvFaultNo = (TextView) convertView.findViewById(R.id.tv_FaultNo);
			holder.tvSetV = (TextView) convertView.findViewById(R.id.tv_SetV);
			holder.tvWorkV = (TextView) convertView.findViewById(R.id.tv_WorkV);

			holder.tvSetNb = (TextView) convertView.findViewById(R.id.tv_SetNb);
			holder.tvWorkNb = (TextView) convertView.findViewById(R.id.tv_WorkNb);
			holder.tvNbPlus = (TextView) convertView.findViewById(R.id.tv_NbPlus); // ��Ƿ

			holder.tvNbTime = (TextView) convertView.findViewById(R.id.tv_NBTime);
			holder.tvAeSpan = (TextView) convertView.findViewById(R.id.tv_AeSpan); // AE���
			holder.tvAeTime = (TextView) convertView.findViewById(R.id.tv_AeTime); // Aeʱ��
			holder.tvAeV = (TextView) convertView.findViewById(R.id.tv_AeV);
			holder.tvAeContinues = (TextView) convertView.findViewById(R.id.tv_AeContinues); // Aeʱ��
			holder.tvAeStatus = (TextView) convertView.findViewById(R.id.tv_AeStatus);
			holder.tvAeCnt = (TextView) convertView.findViewById(R.id.tv_AeCnt); // AE����

			holder.tvOStatus = (TextView) convertView.findViewById(R.id.tv_OStatus); // ����̬

			holder.tvYJWZ = (TextView) convertView.findViewById(R.id.tv_YJWZ); // ����λ��
			holder.tvNoise = (TextView) convertView.findViewById(R.id.tv_Noise);
			// �ۿ����ֿ���
			holder.tvNB = (TextView) convertView.findViewById(R.id.tv_NB);
			holder.tvRC = (TextView) convertView.findViewById(R.id.tv_RC);
			holder.tvNC = (TextView) convertView.findViewById(R.id.tv_NC);
			holder.tvALF = (TextView) convertView.findViewById(R.id.tv_ALF);
			holder.tvNI = (TextView) convertView.findViewById(R.id.tv_NI);

			MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));

			convertView.setTag(holder);
			// ���б�ɫ
			// convertView.setBackgroundColor(colors[position % 2]);
			// mHolderList.add(holder);
		} else {
			// ���б�ɫ
			// convertView.setBackgroundColor(colors[position % 2]);
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvPotNo.setText(entity.getPotNo() + "");
		if (entity.getStatus().equals("NORM")) {
			holder.tvPotSt.setText(""); // ������
		} else {
			holder.tvPotSt.setTextColor(Color.RED);
			holder.tvPotSt.setText(entity.getStatus());
		}

		if (entity.isAutoRun()) {
			holder.tvAutoRun.setText("");
		} else {
			holder.tvAutoRun.setTextColor(Color.RED);
			holder.tvAutoRun.setText("�ֶ�");
		}
		holder.tvOperation.setTextColor(Color.BLUE);
		holder.tvOperation.setText(entity.getOperation());
		if (entity.getFaultNo() == 0) {
			holder.tvFaultNo.setText("");
		} else {
			holder.tvFaultNo.setTextColor(Color.RED);
			holder.tvFaultNo.setText(entity.getFaultNo() + ""); // ���Ϻ�
		}
		int SetV = Integer.valueOf(entity.getSetV());
		holder.tvSetV.setTextColor(Color.DKGRAY);
		holder.tvSetV.setText(String.format("%.3f", SetV / 1000.0));

		// ������ѹ
		int WorkV = Integer.valueOf(entity.getWorkV());
		if (entity.isAeFlag()) {
			holder.tvWorkV.setTextColor(Color.RED);
		} else if ((entity.getAbnormal_Flag() & 0x40) != 0) {
			holder.tvWorkV.setTextColor(Color.GREEN);
		} else if ((entity.getAbnormal_Flag() & 0x20) != 0) {
			holder.tvWorkV.setTextColor(Color.rgb(220, 203, 24)); // �̻�ɫ
		} else {
			holder.tvWorkV.setTextColor(Color.BLACK);
		}
		holder.tvWorkV.setText(String.format("%.3f", WorkV / 1000.0));

		holder.tvSetNb.setTextColor(Color.DKGRAY);
		holder.tvSetNb.setText(entity.getSetNb() + "");
		holder.tvWorkNb.setTextColor(Color.BLACK);
		holder.tvWorkNb.setText(entity.getWorkNb() + "");
		holder.tvNbPlus.setTextColor(Color.DKGRAY);
		holder.tvNbPlus.setText(entity.getNbPlus() + ""); // ��Ƿ

		if (entity.getNbTime() == null) {
			holder.tvNbTime.setText("");
		} else {
			holder.tvNbTime.setTextColor(Color.BLACK);
			holder.tvNbTime.setText(entity.getNbTime().toString());
		}
		holder.tvAeSpan.setTextColor(Color.DKGRAY);
		holder.tvAeSpan.setText((entity.getAeSpan()) / 60 + "");
		holder.tvAeTime.setTextColor(Color.BLACK);
		holder.tvAeTime.setText(entity.getAeDateTime());

		int AeV = Integer.valueOf(entity.getAeV());
		holder.tvAeV.setTextColor(Color.DKGRAY);
		holder.tvAeV.setText(String.format("%.2f", AeV / 1000.0));

		holder.tvAeContinues.setTextColor(Color.BLACK);
		holder.tvAeContinues.setText(entity.getAeContinue() + "");
		holder.tvAeStatus.setTextColor(Color.DKGRAY);
		holder.tvAeStatus.setText(entity.getAeStatus());
		holder.tvAeCnt.setTextColor(Color.BLACK);
		holder.tvAeCnt.setText(entity.getAeCnt() + "");

		holder.tvOStatus.setTextColor(Color.DKGRAY);
		holder.tvOStatus.setText(entity.getOStatus());

		holder.tvYJWZ.setTextColor(Color.BLACK);
		holder.tvYJWZ.setText(entity.getYJWJ() + "");

		holder.tvNoise.setTextColor(Color.DKGRAY);
		holder.tvNoise.setText(entity.getNoise() + "");
		// �ۿ����ֿ��ش���
		if ((entity.getPotCtrl() & 0x10) == 0x10) {
			holder.tvNB.setTextColor(Color.BLACK);
			holder.tvNB.setText("��");
		} else {
			holder.tvNB.setTextColor(Color.RED);
			holder.tvNB.setText("��");
		}
		if ((entity.getPotCtrl() & 0x20) == 0x20) {
			holder.tvRC.setTextColor(Color.BLACK);
			holder.tvRC.setText("��");
		} else {
			holder.tvRC.setTextColor(Color.RED);
			holder.tvRC.setText("��");
		}
		//����������
		if ((entity.getPotCtrl() & 0x40) == 0x40) {
			holder.tvNC.setTextColor(Color.BLACK);
			holder.tvNC.setText("��"); 
		} else {
			holder.tvNC.setTextColor(Color.RED);
			holder.tvNC.setText("��");
		}
		//Ũ�ȿ�����
		if ((entity.getPotCtrl() & 0x04) == 0x04) {
			holder.tvNI.setTextColor(Color.BLACK);
			holder.tvNI.setText("��");
		} else {
			holder.tvNI.setTextColor(Color.RED);
			holder.tvNI.setText("��");
		}
		if ((entity.getPotCtrl() & 0x80) == 0x80) {
			holder.tvALF.setTextColor(Color.BLACK);
			holder.tvALF.setText("��");
		} else {
			holder.tvALF.setTextColor(Color.RED);
			holder.tvALF.setText("��");
		}
		if (!(entity.getStatus().equals("ͣ��")) && ((entity.getComerr() + 35) != 0)) {
			// �����ͣ�ۣ�ͨѶ����
			// holder.tvPotSt.setText("");
			holder.tvAutoRun.setText("");
			holder.tvOperation.setText("");
			holder.tvFaultNo.setTextColor(Color.RED);
			holder.tvFaultNo.setText(entity.getComerr() + "");
			holder.tvSetV.setText("");
			holder.tvWorkV.setText("");
			holder.tvSetNb.setText("");
			holder.tvWorkNb.setText("");
			holder.tvNbPlus.setText("");
			holder.tvNbTime.setText("");
			holder.tvAeSpan.setText("");
			holder.tvAeTime.setText("");
			holder.tvAeV.setText("");
			holder.tvAeContinues.setText("");
			holder.tvAeStatus.setText("");
			holder.tvAeCnt.setText("");
			holder.tvOStatus.setText("");
			holder.tvYJWZ.setText("");
			holder.tvNoise.setText("");
			holder.tvNB.setText("");
			holder.tvRC.setText("");
			holder.tvNC.setText("");
			holder.tvALF.setText("");
			holder.tvNI.setText("");
		}
		if (entity.getStatus().equals("ͣ��")) {

			holder.tvPotSt.setText("ͣ��");
			holder.tvAutoRun.setText("");
			holder.tvOperation.setText("");
			// holder.tvFaultNo.setTextColor(Color.RED);
			holder.tvFaultNo.setText("");
			holder.tvSetV.setText("");
			// holder.tvWorkV.setText("");
			holder.tvSetNb.setText("");
			holder.tvWorkNb.setText("");
			holder.tvNbPlus.setText("");
			holder.tvNbTime.setText("");
			holder.tvAeSpan.setText("");
			holder.tvAeTime.setText("");
			holder.tvAeV.setText("");
			holder.tvAeContinues.setText("");
			holder.tvAeStatus.setText("");
			holder.tvAeCnt.setText("");
			holder.tvOStatus.setText("");
			holder.tvYJWZ.setText("");
			holder.tvNoise.setText("");
			holder.tvNB.setText("");
			holder.tvRC.setText("");
			holder.tvNC.setText("");
			holder.tvALF.setText("");
			holder.tvNI.setText("");

		}
		return convertView;
	}

	class OnScrollChangedListenerImp implements OnScrollChangedListener {
		MyHScrollView mScrollViewArg;

		public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			mScrollViewArg.smoothScrollTo(l, t);
		}
	};

	class ViewHolder {
		TextView tvPotNo;
		TextView tvPotSt;
		TextView tvAutoRun;
		TextView tvOperation;
		TextView tvFaultNo; // ����
		TextView tvSetV;
		TextView tvWorkV;
		TextView tvSetNb;
		TextView tvWorkNb;
		TextView tvNbPlus; // ��Ƿ
		TextView tvNbTime; // NBʱ��
		TextView tvAeSpan; // AE���
		TextView tvAeTime; // AEʱ��
		TextView tvAeV;
		TextView tvAeContinues; // AEʱ��
		TextView tvAeStatus; // AE״̬
		TextView tvAeCnt;
		TextView tvOStatus; // ����̬
		TextView tvYJWZ; // ����λ��
		TextView tvNoise; // ����
		TextView tvNB; // NB����
		TextView tvRC; // NB����
		TextView tvNC; // NB����
		TextView tvALF; // NB����
		TextView tvNI; // NB����

		HorizontalScrollView scrollView;
	}

	public void onDateChange(List<PotStatus> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
