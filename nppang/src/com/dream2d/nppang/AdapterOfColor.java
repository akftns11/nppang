package com.dream2d.nppang;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdapterOfColor extends BaseAdapter{
	Context mContext;
	ArrayList<Integer> mArrayListColor;
	int mSize;
	public AdapterOfColor(Context context, ArrayList<Integer> arrayListColor, int size){
		mContext = context;
		mArrayListColor = arrayListColor;
		mSize = size;
	}
	@Override
	public int getCount() {		
		return mArrayListColor.size();
	}

	@Override
	public Integer getItem(int position) {
		// TODO Auto-generated method stub
		return mArrayListColor.get(position);		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if(v == null){
			v = LayoutInflater.from(mContext).inflate(R.layout.color, null); 
			
			/*
			MarginLayoutParams margin;
			Log.e("nppang", "1");
			margin = new ViewGroup.MarginLayoutParams(new LinearLayout.LayoutParams(mSize, mSize));
			Log.e("nppang", "2");
			margin.setMargins(0, 0, (int)(mSize*0.1), 0);
			Log.e("nppang", "3");
			v.setLayoutParams(new LinearLayout.LayoutParams(margin));
			*/		
		}
				
		((TextView)v.findViewById(R.id.text_view_color)).setText(" ");
		Log.e("nppang", "5");
		v.setBackgroundColor(mArrayListColor.get(position));
		Log.e("nppang", "6");

		return v;
	}
	
	public void setArrayList(ArrayList<Integer> arrayListColor){
		mArrayListColor = arrayListColor;
	}

	public ArrayList<Integer> getArrayList(){
		return mArrayListColor;
	}
}
