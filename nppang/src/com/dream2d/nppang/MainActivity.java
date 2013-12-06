package com.dream2d.nppang;





// 아이콘 사이즈 정리
// http://blog.naver.com/PostView.nhn?blogId=wiseyess&logNo=150133964295



import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fsn.cauly.CaulyAdView;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity implements OnItemSelectedListener{

	// Action Bar 에 연결하는 Toggle
	private ActionBarDrawerToggle mDrawerToggle;	
	// 아래에서 위로 올라오는 drawer
	private SlidingDrawer mSlidingDrawerDownToUp;
	// 왼쪽에서 오른쪽으로 보이는 drawer
	private DrawerLayout mDrawerLayoutLeftToRight;
	// 왼쪽 List view 에 보이는 drawer
	private ListView mListViewLeftDrawer;
	// 왼쪽 List view 에 들어갈 String
	private String[] mStringsOfAdapterOfLeftDrawerListView;
	// 왼쪽 List view 에 들어갈 Image
	private int[] mImagesOfLeftDrawerListView;
	// 데이터를 연결할 Adapter
	AdapterOfLeftDrawerListView mAdapterOfLeftDrawerListView;
	// Nppang Item 이 들어갈 String 배열
	String[] mStringsOfNppangItem;
	// n빵 Item에 대한 ArrayAdapter
	ArrayAdapter<String> mArrayAdapterNppangItem;
	// 은행 List가 들어갈 String 배열
	String[] mStringsOfBankList;
	// 은행 List에 대한 ArrayAdapter
	ArrayAdapter<String> mArrayAdapterBankList;
	// ActionBar 객체
	ActionBar mActionBar;
	// ActionBar 의 View
	View mViewActionBar; 



	// n빵 보낼 사람 명수에 대한 ArrayAdapter
	ArrayAdapter<String> mArrayAdapterNppangCountOfPerson; 


	LinearLayout mLinearLayoutMain;

	// 하단에 Nppang 가 뵤시될 Grid View
	GridView mGridViewNppangList;

	// 광고 View
	CaulyAdView mCaulyAdView;

	// N빵 리스트가 들어있는 adapter
	AdapterOfListOfNppang mAdapterOfListOfNppang;

	// Back의 상태값을 저장하기 위한 변수. back key 를 두번 눌러 App 을 종료 하기 위함
	Boolean mCloseApplicationFlag= false;
	// 현재 상태를 저장하기 위한 변수 (Nomal or Edit)
	int mCurrentMode = EtcClass.NOMAL_MODE;


	TextView mTextViewNppangAmount;	
	Spinner mSpinnerNppangCountOfPerson;
	Spinner mSpinnerBankList;
	Spinner mSpinnerItemList;
	EditText mEditTextAccountNumber;
	EditText mEditTextAccountOwner;
	EditText mEditTextTotalAmount;

	// 일정 시간 후 상태값을 초기화하기 위한 핸들러
	Handler mCloseApplicationHandler = new Handler() {
		public void handleMessage(Message msg) {
			mCloseApplicationFlag = false;
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_main);

		setMainLayout();

		// Left drawer 초기화 start
		// Left drawer list view 에 들어갈 String 를 설정한다.
		mStringsOfAdapterOfLeftDrawerListView = getResources().getStringArray(R.array.strings_of_adapter_of_left_drawer_list_view);

		////////// Left drawer list view 에 들어갈 Image 를 설정한다.		
		mImagesOfLeftDrawerListView = new int[mStringsOfAdapterOfLeftDrawerListView.length];
		mImagesOfLeftDrawerListView[0] = R.drawable.ic_launcher;
		mImagesOfLeftDrawerListView[1] = R.drawable.alarm;
		mImagesOfLeftDrawerListView[2] = R.drawable.pallet;
		mImagesOfLeftDrawerListView[3] = R.drawable.save;
		mImagesOfLeftDrawerListView[4] = R.drawable.not_complete;
		mImagesOfLeftDrawerListView[5] = R.drawable.complete;

		// adapter 에 연결한다.
		mAdapterOfLeftDrawerListView = new AdapterOfLeftDrawerListView(this, mStringsOfAdapterOfLeftDrawerListView, mImagesOfLeftDrawerListView);
		mListViewLeftDrawer.setAdapter(mAdapterOfLeftDrawerListView);
		mListViewLeftDrawer.setOnItemClickListener(new DrawerItemClickListener());

		///////// Left drawer 초기화 end




		/*
		btnClose = (Button)findViewById(R.id.button_close);
		btnClose.setOnClickListener(new OnClickListener() {   
			@Override
			public void onClick(View v) {				
				mSlidingDrawerDownToUp.animateClose();	// sliding 닫기

			}
		});
		 */

		////////// action bar 초기화 start 
		mActionBar = getActionBar(); 
		// custom actionbar view 를 얻어온다.
		mViewActionBar = getLayoutInflater().inflate(R.layout.custom_actionbar, null);
		// custom action bar 의 가로 세로 높이를 설정한다.
		mActionBar.setCustomView(mViewActionBar, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));

		// action bar 버튼이 눌리게끔 한다. 눌린 동작은 onOptionsItemSelected 여기에 정의한다.
		mActionBar.setHomeButtonEnabled(true);
		// custom action bar 가 보이게 한다.
		mActionBar.setDisplayShowCustomEnabled(true);;

		// Action bar 에 ㅌ가 보이게 한다. 관련 동작은 onPostCreate 와  onConfigurationChanged 에 정의되어있다.
		mActionBar.setDisplayHomeAsUpEnabled(true);
		((TextView)(mViewActionBar.findViewById(R.id.text_view_title))).setText(R.string.app_name);

		// action bar 와 DrawerLayout를 연결한다.
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  // host Activity 
				mDrawerLayoutLeftToRight,         // DrawerLayout object 
				R.drawable.ic_drawer,  // nav drawer image to replace 'Up' caret 
				R.string.drawer_open,  // "open drawer" description for accessibility 
				R.string.drawer_close  // "close drawer" description for accessibility 
				) {
			public void onDrawerClosed(View view) {

				((TextView)(mViewActionBar.findViewById(R.id.text_view_title))).setText(R.string.app_name);

				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				((TextView)(mViewActionBar.findViewById(R.id.text_view_title))).setText(R.string.app_name);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		//mDrawerLayoutLeftToRight.setDrawerListener(mDrawerToggle);

		////////// action bar 초기화 end

		// Spinner 가 클릭했을때의 동작 결정 onItemSelected 함수에서 처리됨
		mSpinnerNppangCountOfPerson.setOnItemSelectedListener(this);

		String[] count = new String[EtcClass.MAX_NPPANG_COUNT_OF_PERSON];
		for(int i=0; i<10; i++){
			count[i] = "" + (i+1);
		}
		mArrayAdapterNppangCountOfPerson = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, count);
		mArrayAdapterNppangCountOfPerson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerNppangCountOfPerson.setAdapter(mArrayAdapterNppangCountOfPerson);

		// Nppang Item 을 넣는다.
		mStringsOfNppangItem = getResources().getStringArray(R.array.strings_of_npping_item);
		mArrayAdapterNppangItem = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStringsOfNppangItem);
		mArrayAdapterNppangItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerItemList.setAdapter(mArrayAdapterNppangItem);
		
		// 은행 리스트를 넣는다.
		mStringsOfBankList = getResources().getStringArray(R.array.strings_of_bank_list);
		mArrayAdapterBankList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStringsOfBankList);
		mArrayAdapterBankList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerBankList.setAdapter(mArrayAdapterBankList);

		ArrayList<NppangClass> mArrayListNppang = new ArrayList<NppangClass>();
		///////////////////// Test 를 위해 더미 데이터를 넣는다. ///////////
		mArrayListNppang.add(new NppangClass(10, 2013, 11, 1, 10000, 2, "회식", getResources().getColor(R.color.nomal_mode), "000-000-001", "첫번쨰", "우리은행", 1));
		mArrayListNppang.add(new NppangClass(11, 2013, 11, 2, 20000, 3, "점심", getResources().getColor(R.color.nomal_mode), "000-000-002", "두번쨰", "국민은행", 2));
		mArrayListNppang.add(new NppangClass(22, 2013, 11, 3, 30000, 4, "저녁", getResources().getColor(R.color.nomal_mode), "000-000-003", "세번쨰", "하나은행", 3));
		mArrayListNppang.add(new NppangClass(33, 2013, 11, 4, 30000, 5, "커피", getResources().getColor(R.color.nomal_mode), "000-000-003", "네번쨰", "농협", 4));
		mArrayListNppang.add(new NppangClass(44, 2013, 11, 5, 30000, 6, "저녁", getResources().getColor(R.color.nomal_mode), "000-000-003", "다섯번쨰", "기업은행", 5));
		mArrayListNppang.add(new NppangClass(55, 2013, 11, 6, 30000, 7, "저녁", getResources().getColor(R.color.nomal_mode), "000-000-003", "여섯번쨰", "은행6", 6));
		mArrayListNppang.add(new NppangClass(66, 2013, 11, 7, 30000, 8, "저녁", getResources().getColor(R.color.nomal_mode), "000-000-003", "일곱번쨰", "은행7", 7));
		mArrayListNppang.add(new NppangClass(77, 2013, 11, 8, 30000, 9, "저녁", getResources().getColor(R.color.nomal_mode), "000-000-003", "여덜번쨰", "은행8", 8));
		mAdapterOfListOfNppang = new AdapterOfListOfNppang(this, mArrayListNppang);

		mGridViewNppangList = (GridView )findViewById(R.id.grid_view_nppang_list);
		mGridViewNppangList.setAdapter(mAdapterOfListOfNppang);
		mGridViewNppangList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if(mCurrentMode == EtcClass.EDIT_MODE){
					// Edit mode 색상으로 바꾼다.
					mAdapterOfListOfNppang.setBackgroundColor((int)id, getResources().getColor(R.color.edit_mode));
					return;
				}
				Toast.makeText(MainActivity.this,
						"1111"  , Toast.LENGTH_SHORT).show();
				

			}
		});

		mGridViewNppangList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				NppangClass nppangClass = mAdapterOfListOfNppang.getNppangClass((int)id);
				mTextViewNppangAmount.setText(String.valueOf(nppangClass.getTotalMoney()/nppangClass.getN()));
				mEditTextTotalAmount.setText(String.valueOf(nppangClass.getTotalMoney()));
				mEditTextAccountNumber.setText(String.valueOf(nppangClass.getAccountNumber()));
				mEditTextAccountOwner.setText(String.valueOf(nppangClass.getAccountOwner()));				
				
				// Spinner 에 대해 보낼땐 있었던 항목이 불러오려고 하니 없어진 경우 어떻게 처리할 것인가? 현재는 0번 position 을 selection 한다.
				mSpinnerNppangCountOfPerson.setSelection(mArrayAdapterNppangCountOfPerson.getPosition(String.valueOf(nppangClass.getN())));
				mSpinnerBankList.setSelection(mArrayAdapterBankList.getPosition(String.valueOf(nppangClass.getAccountBank())));
				mSpinnerItemList.setSelection(mArrayAdapterNppangItem.getPosition(String.valueOf(nppangClass.getItem())));
				// return 값이 false 이면 long click 이 끝난 후 click 이벤트가 불린다.
				
				// Edit mode 로 바꾼다.
				mCurrentMode = EtcClass.EDIT_MODE;
				// Edit mode 색상으로 바꾼다.
				mAdapterOfListOfNppang.setBackgroundColor((int)id, getResources().getColor(R.color.edit_mode));				

				return true;
			}

		});

	}

	private void setMainLayout(){
		mDrawerLayoutLeftToRight = (DrawerLayout) findViewById(R.id.drawer_layout_left_to_right);
		mListViewLeftDrawer = (ListView) findViewById(R.id.list_view_left_drawer);
		mSpinnerNppangCountOfPerson = (Spinner)findViewById(R.id.spinner_nppang_count_of_person);
		mSlidingDrawerDownToUp = (SlidingDrawer)findViewById(R.id.sliding_drawer_down_to_up);
		mLinearLayoutMain = (LinearLayout)findViewById(R.id.linear_layout_main);
		mCaulyAdView = (CaulyAdView) findViewById(R.id.cauly_ad_view);

		mTextViewNppangAmount = (TextView) findViewById(R.id.text_view_nppang_amount);		
		mSpinnerNppangCountOfPerson = (Spinner) findViewById(R.id.spinner_nppang_count_of_person);
		mSpinnerBankList = (Spinner) findViewById(R.id.spinner_bank_list);
		mSpinnerItemList = (Spinner) findViewById(R.id.spinner_item_list);
		mEditTextAccountNumber = (EditText) findViewById(R.id.edit_text_account_number);
		mEditTextAccountOwner = (EditText) findViewById(R.id.edit_text_account_owner);
		mEditTextTotalAmount = (EditText) findViewById(R.id.edit_text_total_amount);		
	}
	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Toast.makeText(MainActivity.this, "aaa " + position, Toast.LENGTH_SHORT).show();
			mDrawerLayoutLeftToRight.closeDrawer(mListViewLeftDrawer);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {	
		Rect notificationBar = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(notificationBar);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// SlidingDrawer 의 위치 및 Bottom Offset 을 설정해준다. (왼쪽, 위, 오른쪽, 아래, offset)
		// Offset 는 아래 광고, ActionBar, Notibar, Main 화면을 제외한 부분의 90%로 한다.
		mSlidingDrawerDownToUp.setOffset(0, getActionBar().getHeight(), 
				metrics.widthPixels, metrics.heightPixels-metrics.heightPixels/10 - notificationBar.top,
				//(metrics.heightPixels-metrics.heightPixels/10-getActionBar().getHeight())/2-200);
				(int)((metrics.heightPixels-metrics.heightPixels/10-getActionBar().getHeight() - mLinearLayoutMain.getHeight())*0.9));
		// 기본으로 닫혀 있는 상태
		mSlidingDrawerDownToUp.close();


		// Action Bar 의 Image 크기를 결정해준다. Action Bar 의 높이 - EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN 이다. 
		((ImageView)(mViewActionBar.findViewById(R.id.image_button_complete))).setLayoutParams(new LinearLayout.LayoutParams(mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN, mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN));
		((ImageView)(mViewActionBar.findViewById(R.id.image_button_alarm))).setLayoutParams(new LinearLayout.LayoutParams(mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN, mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN));
		((ImageView)(mViewActionBar.findViewById(R.id.image_button_pallet))).setLayoutParams(new LinearLayout.LayoutParams(mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN, mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN));
		((ImageView)(mViewActionBar.findViewById(R.id.image_button_save))).setLayoutParams(new LinearLayout.LayoutParams(mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN, mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN));
		((ImageView)(mViewActionBar.findViewById(R.id.image_button_refresh))).setLayoutParams(new LinearLayout.LayoutParams(mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN, mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN));
		((ImageView)(mViewActionBar.findViewById(R.id.image_button_delete))).setLayoutParams(new LinearLayout.LayoutParams(mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN, mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN));
		((ImageView)(mViewActionBar.findViewById(R.id.image_button_help))).setLayoutParams(new LinearLayout.LayoutParams(mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN, mViewActionBar.getHeight()-EtcClass.ACTIONBAR_IMAGE_SIZE_MARGIN));
		mGridViewNppangList.setColumnWidth(mGridViewNppangList.getWidth()/2);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	// Action bar 왼쪽에 ㅌ모양이 변한다.
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void onBackPressed () 
	{
		// Edit 모드이면 Nomal 모드로 바꿔준다.
		if(mCurrentMode == EtcClass.EDIT_MODE){
			mCurrentMode = EtcClass.NOMAL_MODE;
			// Nomal mode 색상으로 바꾼다.
			mAdapterOfListOfNppang.setAllBackgroundColor(getResources().getColor(R.color.nomal_mode));
			return;
		}
		// mCloaeApplicationFlag 가 false 이면 첫번째로 키가 눌린 것이다.
		if(mCloseApplicationFlag == false) { // Back 키가 첫번째로 눌린 경우
			// 안내 메세지를 토스트로 출력한다.
			Toast.makeText(this, "취소키를 빨리 한번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show();
			// 상태값 변경
			mCloseApplicationFlag = true;
			// 핸들러를 이용하여 3초 후에 0번 메세지를 전송하도록 설정한다.
			mCloseApplicationHandler.sendEmptyMessageDelayed(0, EtcClass.BACK_DELEAY_TIME_FOR_FINISH_ACTIVITY);
		} else { // Back 키가 3초 내에 연달아서 두번 눌린 경우

			// 액티비티를 종료하는 상위 클래스의 onBackPressed 메소드를 호출한다.
			super.onBackPressed();
		}
	}
}
