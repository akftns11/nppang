package com.dream2d.nppang;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterOfListOfNppang extends BaseAdapter{
	Context mContext;
	ArrayList<NppangClass> mArrayListNppang;
	public AdapterOfListOfNppang(Context context, ArrayList<NppangClass> arrayListNppang){
		mContext = context;
		mArrayListNppang = arrayListNppang;
	}
	@Override
	public int getCount() {		
		return mArrayListNppang.size();
	}

	@Override
	public NppangClass getItem(int position) {
		// TODO Auto-generated method stub
		return mArrayListNppang.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mArrayListNppang.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if(v == null){
			v = LayoutInflater.from(mContext).inflate(R.layout.each_nppang_list, null);            
		}
		//		((Button)v.findViewById(R.id.button_push_into_current)).setOnClickListener(buttonClickListener);

		String mSetMessage;
		mSetMessage = "총액 : " + mArrayListNppang.get(position).getTotalMoney() + "원" + System.getProperty("line.separator") 
				+ "N : " + mArrayListNppang.get(position).getN() + "명" + System.getProperty("line.separator") 
				+ "N빵 : " + (mArrayListNppang.get(position).getTotalMoney() / mArrayListNppang.get(position).getN()) + System.getProperty("line.separator")  ;

		((TextView)v.findViewById(R.id.text_view_message)).setText(mSetMessage);
		v.setBackgroundColor(mArrayListNppang.get(position).getColor());

		return v;
	}
	/*
	private View.OnClickListener buttonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			// 버튼 클릭
			case R.id.button_push_into_current:
				Toast.makeText(mContext, "bbbbb", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};
	 */
	public void setArrayList(ArrayList<NppangClass> arrayListNppang){
		mArrayListNppang = arrayListNppang;
	}

	public ArrayList<NppangClass> getArrayList(){
		return mArrayListNppang;
	}

	public NppangClass getNppangClass(int id){
		for(int i=0; i<mArrayListNppang.size(); i++){
			if(mArrayListNppang.get(i).getId() == id){
				return mArrayListNppang.get(i);
			}
		}
		return null;
	}

	public void setBackgroundColor(int id, int color){    	
		for(int i=0; i<mArrayListNppang.size(); i++){
			if(mArrayListNppang.get(i).getId() == id){
				mArrayListNppang.get(i).setColor(color);
				// Update 된 정보가 있다가 알린다. 화면이 다시 그려짐
				notifyDataSetChanged();    			
				break;
			}
		}
	}
	public void setAllBackgroundColor(int color){
		for(int i=0; i<mArrayListNppang.size(); i++){    		
			mArrayListNppang.get(i).setColor(color);
		}
		// Update 된 정보가 있다가 알린다. 화면이 다시 그려짐
		notifyDataSetChanged();
	}
}
