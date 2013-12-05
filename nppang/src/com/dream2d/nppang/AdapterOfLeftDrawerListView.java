package com.dream2d.nppang;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AdapterOfLeftDrawerListView extends BaseAdapter {
	// 레이아웃 XML을 읽어들이기 위한 객체
	private LayoutInflater mInflater;
	String[] mListStrings;
	int[] mListImages;

	public AdapterOfLeftDrawerListView(Context context, String[] listStrings, int[] listImages) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		mListStrings = listStrings;
		mListImages = listImages;
	}

	// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		View view = null;

		// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기

		// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
		view = mInflater.inflate(R.layout.left_drawer_list_each_item, null);
		
		// 화면 출력
		ImageView imageview = (ImageView) view.findViewById(R.id.image_view);
		TextView textview = (TextView) view.findViewById(R.id.text_view);
		textview.setText(mListStrings[position]);
		
		imageview.setBackgroundResource(mListImages[position]);
		return view;

	}
	
	@Override
	 public int getCount() {
	  return mListStrings.length;
	 }
	 @Override
	 public String getItem(int position) {
	  return mListStrings[position];
	 }
	 @Override
	 public long getItemId(int position) {
	  return position;
	 }
	

}