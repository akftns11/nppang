package com.dream2d.nppang;

import java.util.ArrayList;

import android.content.Context;
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
		mSetMessage = "ÃÑ¾× : " + mArrayListNppang.get(position).getTotalMoney() + "¿ø" + System.getProperty("line.separator") 
				+ "N : " + mArrayListNppang.get(position).getN() + "¸í" + System.getProperty("line.separator") 
				+ "N»§ : " + (mArrayListNppang.get(position).getTotalMoney() / mArrayListNppang.get(position).getN()) + System.getProperty("line.separator")  ;
		
		((TextView)v.findViewById(R.id.text_view_message)).setText(mSetMessage);
		return v;
	}
/*
	private View.OnClickListener buttonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			// ¹öÆ° Å¬¸¯
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
}
