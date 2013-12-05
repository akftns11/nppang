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
	// ���̾ƿ� XML�� �о���̱� ���� ��ü
	private LayoutInflater mInflater;
	String[] mListStrings;
	int[] mListImages;

	public AdapterOfLeftDrawerListView(Context context, String[] listStrings, int[] listImages) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		mListStrings = listStrings;
		mListImages = listImages;
	}

	// �������� ��Ÿ���� �ڽ��� ���� xml�� ���̱� ���� ����
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		View view = null;

		// ���� ����Ʈ�� �ϳ��� �׸� ���� ��Ʈ�� ���

		// XML ���̾ƿ��� ���� �о ����Ʈ�信 ����
		view = mInflater.inflate(R.layout.left_drawer_list_each_item, null);
		
		// ȭ�� ���
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