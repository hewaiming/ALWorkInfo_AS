package com.hewaiming.ALWorkInfo.view;

import com.hewaiming.ALWorkInfo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class FooterListView_plus extends RelativeLayout {
	private Context context;


	public FooterListView_plus(Context context) {
		super(context);
		this.context = context;
		View view = LayoutInflater.from(this.context).inflate(R.layout.footer_plus, null);
		// 以下两句的顺序不能调换，要先addView，然后才能通过findViewById找到该TextView
		addView(view);		
	}
}