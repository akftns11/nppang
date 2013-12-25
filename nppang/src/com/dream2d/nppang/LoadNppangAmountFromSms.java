package com.dream2d.nppang;
import java.util.ArrayList;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dream2d.nppang.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.WindowManager;

// 커스텀 다이얼 로그 : http://khie74.tistory.com/1169521457
// 커스텀 리스트뷰 : http://androiddeveloper.tistory.com/73
public class LoadNppangAmountFromSms extends Activity implements OnScrollListener{
	final Uri allMessage = Uri.parse("content://sms");
	final public int RECEIVE_SMS = 1;	// send sms = 2
	private int mDeviceScreenWidth;
	private int mDeviceScreenHeight;
	
	// 리스트뷰 선언
	private ListView mListview;
	// 데이터를 연결할 Adapter
	DataAdapter adapter;
	// 데이터를 담을 자료구조
	ArrayList<CData> alist;	
	Context mContext;

	boolean mLockListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.load_nppang_amount_from_sms);
		
		mContext = this;
		mLockListView = true;
		
		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		mDeviceScreenWidth = display.getWidth();
		mDeviceScreenHeight = display.getHeight();

		// 선언한 리스트뷰에 사용할 리스뷰연결
		mListview = (ListView) findViewById(R.id.list_view_for_sms);

		// 객체를 생성합니다
		alist = new ArrayList<CData>();

		// 데이터를 받기위해 데이터어댑터 객체 선언
		adapter = new DataAdapter(this, alist);

		// 리스트뷰에 어댑터 연결
		mListview.setAdapter(adapter);		

		// ListView의 이벤트 설정	http://gandus.tistory.com/476
		mListview.setOnItemClickListener( new ListViewItemClickListener() );	// 리스트뷰가 클릭 되었을때
		
		// 스크롤 리스너를 등록합니다. onScroll에 추가구현을 해줍니다. 동적으로 데이터를 read 하기 위함
		mListview.setOnScrollListener(this);

		addItems(EtcClass.SMS_LOAD_COUNT);
		
		// 투명
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		// ArrayAdapter를 통해서 ArrayList에 자료 저장
		// 하나의 String값을 저장하던 것을 CData클래스의 객체를 저장하던것으로 변경
		// CData 객체는 생성자에 리스트 표시 텍스트뷰1의 내용과 텍스트뷰2의 내용 그리고 사진이미지값을 어댑터에 연결

		
	}

	class ListViewItemClickListener implements AdapterView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
		{
			AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
			alertDlg.setPositiveButton( "읽어오기", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick( DialogInterface dialog, int which ) {
					

					String temp = alist.get(position).getContents();
					String money="";
					boolean parsing_from_sms = false;
					for(int i=0; i<temp.length()-1; i++){
						if(Character.isDigit(temp.charAt(i))){
							money += temp.charAt(i);				
							if(temp.substring(i+1, i+2).equals("원")){
								parsing_from_sms = true;
								break;
							}
						}
						else if(temp.charAt(i) == ',')
						{
							continue;
						}
						else{
							money = "";
						}
					}
					if(money.equals("") || !parsing_from_sms){
						Toast.makeText(mContext, "금액을 읽어올 수 없습니다.", Toast.LENGTH_SHORT).show();
						return;
					}
					
					Intent intent = new Intent(); 
					Bundle extra = new Bundle();									
					extra.putInt(EtcClass.NPPANG_TOTAL_AMOUNT, Integer.parseInt(money));
					intent.putExtras(extra);
					LoadNppangAmountFromSms.this.setResult(RESULT_OK, intent);
					LoadNppangAmountFromSms.this.finish();
						                 
				}
			});

			alertDlg.setNegativeButton("취소", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick( DialogInterface dialog, int which ) {
					dialog.dismiss();  // AlertDialog를 닫는다.
				}
			});	        
			//alertDlg.setMessage( adapter.getPosition(item).get(position) );
			alertDlg.setMessage( alist.get(position).getContents());
			alertDlg.show();
		}        
	}


	class DataAdapter extends ArrayAdapter<CData> {
		// 레이아웃 XML을 읽어들이기 위한 객체
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {

			// 상위 클래스의 초기화 과정
			// context, 0, 자료구조
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		// 보여지는 스타일을 자신이 만든 xml로 보이기 위한 구문
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;

			// 현재 리스트의 하나의 항목에 보일 컨트롤 얻기

			if (v == null) {
				// XML 레이아웃을 직접 읽어서 리스트뷰에 넣음
				view = mInflater.inflate(R.layout.load_nppang_amount_from_sms_each_item, null);
			} else {
				view = v;
			}

			// 자료를 받는다.
			final CData data = this.getItem(position);

			if (data != null) {
				// 화면 출력
				TextView tv = (TextView) view.findViewById(R.id.text_view_number_of_sms_message);
				TextView tv2 = (TextView) view.findViewById(R.id.text_view_contents_of_sms_message);
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값
				tv.setText(data.getNumber());
				// 텍스트뷰2에 getData()을 출력 즉 두번째 인수값
				tv2.setText(data.getContents());
				//tv2.setTextColor(Color.WHITE);
			}

			return view;

		}

	}

	// CData안에 받은 값을 직접 할당

	class CData {
		private String mNnumber;
		private String mContents;	

		public CData(Context context, String number, String contents) {
			mNnumber = number;
			mContents = contents;
		}

		public String getNumber() {
			return mNnumber;
		}

		public String getContents() {
			return mContents;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		  {
		// 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
	    // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
	    int count = totalItemCount - visibleItemCount;

	    if(firstVisibleItem >= count && totalItemCount != 0 && mLockListView == false)
	    {	      
	      addItems(EtcClass.SMS_LOAD_COUNT);
	    }  
		
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	private void addItems(final int size)
	{

		// 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
		mLockListView = true;

		Runnable run = new Runnable()
		{
			@Override
			public void run()
			{
				int i=0;
				// SMS 읽어오기
				// http://maluchi.com/xe/?mid=MyAndroidTips&comment_srl=26749&listStyle=webzine&sort_index=voted_count&order_type=asc&page=4&document_srl=27226
				ContentResolver cr = mContext.getContentResolver();
				Cursor cursor = cr.query(allMessage, new String[] { "_id", "thread_id",
						"address", "person", "date", "body", "protocol", "read",
						"status", "type", "reply_path_present", "subject",
						"service_center", "locked", "error_code", "seen" }, null, null,
						"date DESC");

				// CData클래스를 만들때의 순서대로 해당 인수값을 입력
				// 한줄 한줄이 리스트뷰에 뿌려질 한줄 한줄이라고 생각하면 됩니다.
				for(i=0; i<adapter.getCount(); i++){
					cursor.moveToNext();
					if(cursor == null){
						break;
					}
					long type = cursor.getLong(9);
					if (type != RECEIVE_SMS) {
						i--;
					}					
				}
				for (i = 0; i <size; i++) {
					cursor.moveToNext();
					if (cursor == null) {
						return;
					}
					long messageId = cursor.getLong(0);
					long threadId = cursor.getLong(1);
					String address = cursor.getString(2);
					long contactId = cursor.getLong(3);
					String contactId_string = String.valueOf(contactId);
					long timestamp = cursor.getLong(4);
					String body = cursor.getString(5);
					long protocol = cursor.getLong(6);
					long read = cursor.getLong(7);
					long status = cursor.getLong(8);
					long type = cursor.getLong(9);
					String reply_path = cursor.getString(10);
					String subject = cursor.getString(11);
					String servicecenter = cursor.getString(12);
					long locked = cursor.getLong(13);
					long error = cursor.getLong(14);
					long seen = cursor.getLong(15);
					if (type == RECEIVE_SMS) {
						// Log.e("Nppang", address + "  " + body);
						adapter.add(new CData(mContext.getApplicationContext(), address,
								body));
					}
				}
				cursor.close();


				// 모든 데이터를 로드하여 적용하였다면 어댑터에 알리고
				// 리스트뷰의 락을 해제합니다.
				adapter.notifyDataSetChanged();
				mLockListView = false;
			}
		};

		// 속도의 딜레이를 구현하기 위한 꼼수
		Handler handler = new Handler();
		handler.postDelayed(run, EtcClass.POST_DELEAY_TIME_FOR_LOAD_SMS_LOAD);
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