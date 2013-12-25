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

// Ŀ���� ���̾� �α� : http://khie74.tistory.com/1169521457
// Ŀ���� ����Ʈ�� : http://androiddeveloper.tistory.com/73
public class LoadNppangAmountFromSms extends Activity implements OnScrollListener{
	final Uri allMessage = Uri.parse("content://sms");
	final public int RECEIVE_SMS = 1;	// send sms = 2
	private int mDeviceScreenWidth;
	private int mDeviceScreenHeight;
	
	// ����Ʈ�� ����
	private ListView mListview;
	// �����͸� ������ Adapter
	DataAdapter adapter;
	// �����͸� ���� �ڷᱸ��
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

		// ������ ����Ʈ�信 ����� �����俬��
		mListview = (ListView) findViewById(R.id.list_view_for_sms);

		// ��ü�� �����մϴ�
		alist = new ArrayList<CData>();

		// �����͸� �ޱ����� �����;���� ��ü ����
		adapter = new DataAdapter(this, alist);

		// ����Ʈ�信 ����� ����
		mListview.setAdapter(adapter);		

		// ListView�� �̺�Ʈ ����	http://gandus.tistory.com/476
		mListview.setOnItemClickListener( new ListViewItemClickListener() );	// ����Ʈ�䰡 Ŭ�� �Ǿ�����
		
		// ��ũ�� �����ʸ� ����մϴ�. onScroll�� �߰������� ���ݴϴ�. �������� �����͸� read �ϱ� ����
		mListview.setOnScrollListener(this);

		addItems(EtcClass.SMS_LOAD_COUNT);
		
		// ����
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		// ArrayAdapter�� ���ؼ� ArrayList�� �ڷ� ����
		// �ϳ��� String���� �����ϴ� ���� CDataŬ������ ��ü�� �����ϴ������� ����
		// CData ��ü�� �����ڿ� ����Ʈ ǥ�� �ؽ�Ʈ��1�� ����� �ؽ�Ʈ��2�� ���� �׸��� �����̹������� ����Ϳ� ����

		
	}

	class ListViewItemClickListener implements AdapterView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
		{
			AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
			alertDlg.setPositiveButton( "�о����", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick( DialogInterface dialog, int which ) {
					

					String temp = alist.get(position).getContents();
					String money="";
					boolean parsing_from_sms = false;
					for(int i=0; i<temp.length()-1; i++){
						if(Character.isDigit(temp.charAt(i))){
							money += temp.charAt(i);				
							if(temp.substring(i+1, i+2).equals("��")){
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
						Toast.makeText(mContext, "�ݾ��� �о�� �� �����ϴ�.", Toast.LENGTH_SHORT).show();
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

			alertDlg.setNegativeButton("���", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick( DialogInterface dialog, int which ) {
					dialog.dismiss();  // AlertDialog�� �ݴ´�.
				}
			});	        
			//alertDlg.setMessage( adapter.getPosition(item).get(position) );
			alertDlg.setMessage( alist.get(position).getContents());
			alertDlg.show();
		}        
	}


	class DataAdapter extends ArrayAdapter<CData> {
		// ���̾ƿ� XML�� �о���̱� ���� ��ü
		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<CData> object) {

			// ���� Ŭ������ �ʱ�ȭ ����
			// context, 0, �ڷᱸ��
			super(context, 0, object);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		// �������� ��Ÿ���� �ڽ��� ���� xml�� ���̱� ���� ����
		@Override
		public View getView(int position, View v, ViewGroup parent) {
			View view = null;

			// ���� ����Ʈ�� �ϳ��� �׸� ���� ��Ʈ�� ���

			if (v == null) {
				// XML ���̾ƿ��� ���� �о ����Ʈ�信 ����
				view = mInflater.inflate(R.layout.load_nppang_amount_from_sms_each_item, null);
			} else {
				view = v;
			}

			// �ڷḦ �޴´�.
			final CData data = this.getItem(position);

			if (data != null) {
				// ȭ�� ���
				TextView tv = (TextView) view.findViewById(R.id.text_view_number_of_sms_message);
				TextView tv2 = (TextView) view.findViewById(R.id.text_view_contents_of_sms_message);
				// �ؽ�Ʈ��1�� getLabel()�� ��� �� ù��° �μ���
				tv.setText(data.getNumber());
				// �ؽ�Ʈ��2�� getData()�� ��� �� �ι�° �μ���
				tv2.setText(data.getContents());
				//tv2.setTextColor(Color.WHITE);
			}

			return view;

		}

	}

	// CData�ȿ� ���� ���� ���� �Ҵ�

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
		// ���� ���� ó���� ���̴� ����ȣ�� �������� ����ȣ�� ���Ѱ���
	    // ��ü�� ���ڿ� ���������� ���� �Ʒ��� ��ũ�� �Ǿ��ٰ� �����մϴ�.
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

		// �������� �߰��ϴ� ���� �ߺ� ��û�� �����ϱ� ���� ���� �ɾ�Ӵϴ�.
		mLockListView = true;

		Runnable run = new Runnable()
		{
			@Override
			public void run()
			{
				int i=0;
				// SMS �о����
				// http://maluchi.com/xe/?mid=MyAndroidTips&comment_srl=26749&listStyle=webzine&sort_index=voted_count&order_type=asc&page=4&document_srl=27226
				ContentResolver cr = mContext.getContentResolver();
				Cursor cursor = cr.query(allMessage, new String[] { "_id", "thread_id",
						"address", "person", "date", "body", "protocol", "read",
						"status", "type", "reply_path_present", "subject",
						"service_center", "locked", "error_code", "seen" }, null, null,
						"date DESC");

				// CDataŬ������ ���鶧�� ������� �ش� �μ����� �Է�
				// ���� ������ ����Ʈ�信 �ѷ��� ���� �����̶�� �����ϸ� �˴ϴ�.
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


				// ��� �����͸� �ε��Ͽ� �����Ͽ��ٸ� ����Ϳ� �˸���
				// ����Ʈ���� ���� �����մϴ�.
				adapter.notifyDataSetChanged();
				mLockListView = false;
			}
		};

		// �ӵ��� �����̸� �����ϱ� ���� �ļ�
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