package com.dream2d.nppang;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectColor extends Activity  {
	private int mDeviceScreenWidth;
	private int mDeviceScreenHeight;

	TextView mTitle;

	AdapterOfColor mAdapterOfColor;

	GridView mGridViewColor;
	ArrayList<Integer> mArrayListColor = new ArrayList<Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_color);

		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		mDeviceScreenWidth = display.getWidth();
		mDeviceScreenHeight = display.getHeight();




		mArrayListColor = new ArrayList<Integer>();
		mArrayListColor.add(getResources().getColor(R.color.red));
		mArrayListColor.add(getResources().getColor(R.color.cyan));
		mArrayListColor.add(getResources().getColor(R.color.white));
		mArrayListColor.add(getResources().getColor(R.color.green));
		mArrayListColor.add(getResources().getColor(R.color.gray));


		mAdapterOfColor = new AdapterOfColor(this, mArrayListColor, 40);


		mGridViewColor = (GridView )findViewById(R.id.grid_view_color);
		mGridViewColor.setAdapter(mAdapterOfColor);
		mGridViewColor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				Intent intent = new Intent(); 
				Bundle extra = new Bundle();									
				extra.putInt(EtcClass.NPPANG_COLOR, mArrayListColor.get(position));
				intent.putExtras(extra);
				SelectColor.this.setResult(RESULT_OK, intent);
				SelectColor.this.finish();
			}
		});

		// ≈ı∏Ì
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {			
			int x = (int)event.getX();
			int y = (int)event.getY();

			Bitmap bitmapScreen = Bitmap.createBitmap(mDeviceScreenWidth, mDeviceScreenHeight, Bitmap.Config.ARGB_8888);

			if(x < 0 || y < 0)
				return false;

			int ARGB = bitmapScreen.getPixel(x, y);

			if(Color.alpha(ARGB) == 0) {
				finish();
			}

			return true;
		}
		return false;
	}
}