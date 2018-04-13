package com.hewaiming.ALWorkInfo.view;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.hewaiming.ALWorkInfo.R;

import android.content.Context;
import android.widget.TextView;

public class MyMarkerView extends MarkerView {
    private TextView mContentTv;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mContentTv = (TextView) findViewById(R.id.tv_content_marker_view);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
    	if(e.getVal()>2200){
    		 mContentTv.setText(e.getVal()/1000.0+"V");
    	}else{
    		  mContentTv.setText(e.getVal()/10.0+"KA");
    	}
      
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
	
}