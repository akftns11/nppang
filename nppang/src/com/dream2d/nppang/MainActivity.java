package com.dream2d.nppang;





// ������ ������ ����
// http://blog.naver.com/PostView.nhn?blogId=wiseyess&logNo=150133964295



import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
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
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fsn.cauly.CaulyAdView;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity implements OnItemSelectedListener{

	// Action Bar �� �����ϴ� Toggle
	private ActionBarDrawerToggle mDrawerToggle;	
	// �Ʒ����� ���� �ö���� drawer
	private SlidingDrawer mSlidingDrawerDownToUp;
	// ���ʿ��� ���������� ���̴� drawer
	private DrawerLayout mDrawerLayoutLeftToRight;
	// ���� List view �� ���̴� drawer
	private ListView mListViewLeftDrawer;
	// ���� List view �� �� String
	private String[] mStringsOfAdapterOfLeftDrawerListView;
	// ���� List view �� �� Image
	private int[] mImagesOfLeftDrawerListView;
	// �����͸� ������ Adapter
	AdapterOfLeftDrawerListView mAdapterOfLeftDrawerListView;
	// Nppang Item �� �� String �迭
	String[] mStringsOfNppangItem;
	// n�� Item�� ���� ArrayAdapter
	ArrayAdapter<String> mArrayAdapterNppangItem;
	// ���� List�� �� String �迭
	String[] mStringsOfBankList;
	// ���� List�� ���� ArrayAdapter
	ArrayAdapter<String> mArrayAdapterBankList;
	// ActionBar ��ü
	ActionBar mActionBar;
	// ActionBar �� View
	//View mViewActionBar; 
	Menu mMenu;

	// Display�� ���� ����
	int mWidth;

	// n�� ���� ��� ����� ���� ArrayAdapter
	ArrayAdapter<String> mArrayAdapterNppangCountOfPerson; 


	LinearLayout mLinearLayoutMain;

	// �ϴܿ� Nppang �� �̽õ� Grid View
	GridView mGridViewNppangList;

	// ���� View
	CaulyAdView mCaulyAdView;

	// N�� ����Ʈ�� ����ִ� adapter
	AdapterOfListOfNppang mAdapterOfListOfNppang;

	// Back�� ���°��� �����ϱ� ���� ����. back key �� �ι� ���� App �� ���� �ϱ� ����
	Boolean mCloseApplicationFlag= false;
	// ���� ���¸� �����ϱ� ���� ���� (Nomal or Edit)
	int mCurrentMode = EtcClass.NOMAL_MODE;


	TextView mTextViewNppangAmount;	
	Spinner mSpinnerNppangCountOfPerson;
	Spinner mSpinnerBankList;
	Spinner mSpinnerItemList;
	EditText mEditTextAccountNumber;
	EditText mEditTextAccountOwner;
	EditText mEditTextTotalAmount;

	// ���� �ð� �� ���°��� �ʱ�ȭ�ϱ� ���� �ڵ鷯
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

		// Left drawer �ʱ�ȭ start
		// Left drawer list view �� �� String �� �����Ѵ�.
		mStringsOfAdapterOfLeftDrawerListView = getResources().getStringArray(R.array.strings_of_adapter_of_left_drawer_list_view);

		////////// Left drawer list view �� �� Image �� �����Ѵ�.		
		mImagesOfLeftDrawerListView = new int[mStringsOfAdapterOfLeftDrawerListView.length];
		mImagesOfLeftDrawerListView[0] = R.drawable.ic_launcher;
		mImagesOfLeftDrawerListView[1] = R.drawable.alarm;
		mImagesOfLeftDrawerListView[2] = R.drawable.pallet;
		mImagesOfLeftDrawerListView[3] = R.drawable.save;
		mImagesOfLeftDrawerListView[4] = R.drawable.not_complete;
		mImagesOfLeftDrawerListView[5] = R.drawable.complete;

		// adapter �� �����Ѵ�.
		mAdapterOfLeftDrawerListView = new AdapterOfLeftDrawerListView(this, mStringsOfAdapterOfLeftDrawerListView, mImagesOfLeftDrawerListView);
		mListViewLeftDrawer.setAdapter(mAdapterOfLeftDrawerListView);
		mListViewLeftDrawer.setOnItemClickListener(new DrawerItemClickListener());

		///////// Left drawer �ʱ�ȭ end

		
		////////// action bar �ʱ�ȭ start 
		mActionBar = getActionBar(); 
		// action bar ��ư�� �����Բ� �Ѵ�. ���� ������ onOptionsItemSelected ���⿡ �����Ѵ�.
		mActionBar.setHomeButtonEnabled(true);
		// custom action bar �� ���̰� �Ѵ�.
		mActionBar.setDisplayShowCustomEnabled(true);;
		

		// Action bar �� ���� ���̰� �Ѵ�. ���� ������ onPostCreate ��  onConfigurationChanged �� ���ǵǾ��ִ�.
		mActionBar.setDisplayHomeAsUpEnabled(true);
		//((TextView)(mViewActionBar.findViewById(R.id.text_view_title))).setText(R.string.app_name);

		// action bar �� DrawerLayout�� �����Ѵ�.
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
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		//mDrawerLayoutLeftToRight.setDrawerListener(mDrawerToggle);

		////////// action bar �ʱ�ȭ end

		// Spinner �� Ŭ���������� ���� ���� onItemSelected �Լ����� ó����
		mSpinnerNppangCountOfPerson.setOnItemSelectedListener(this);

		String[] count = new String[EtcClass.MAX_NPPANG_COUNT_OF_PERSON];
		for(int i=0; i<10; i++){
			count[i] = "" + (i+1);
		}
		mArrayAdapterNppangCountOfPerson = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, count);
		mArrayAdapterNppangCountOfPerson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerNppangCountOfPerson.setAdapter(mArrayAdapterNppangCountOfPerson);

		// Nppang Item �� �ִ´�.
		mStringsOfNppangItem = getResources().getStringArray(R.array.strings_of_npping_item);
		mArrayAdapterNppangItem = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStringsOfNppangItem);
		mArrayAdapterNppangItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerItemList.setAdapter(mArrayAdapterNppangItem);
		
		// ���� ����Ʈ�� �ִ´�.
		mStringsOfBankList = getResources().getStringArray(R.array.strings_of_bank_list);
		mArrayAdapterBankList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStringsOfBankList);
		mArrayAdapterBankList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerBankList.setAdapter(mArrayAdapterBankList);

		ArrayList<NppangClass> mArrayListNppang = new ArrayList<NppangClass>();
		///////////////////// Test �� ���� ���� �����͸� �ִ´�. ///////////
		mArrayListNppang.add(new NppangClass(10, 2013, 11, 1, 10000, 2, "ȸ��", getResources().getColor(R.color.nomal_mode), "000-000-001", "ù����", "�츮����", 1));
		mArrayListNppang.add(new NppangClass(11, 2013, 11, 2, 20000, 3, "����", getResources().getColor(R.color.nomal_mode), "000-000-002", "�ι���", "��������", 2));
		mArrayListNppang.add(new NppangClass(22, 2013, 11, 3, 30000, 4, "����", getResources().getColor(R.color.nomal_mode), "000-000-003", "������", "�ϳ�����", 3));
		mArrayListNppang.add(new NppangClass(33, 2013, 11, 4, 30000, 5, "Ŀ��", getResources().getColor(R.color.nomal_mode), "000-000-003", "�׹���", "����", 4));
		mArrayListNppang.add(new NppangClass(44, 2013, 11, 5, 30000, 6, "����", getResources().getColor(R.color.nomal_mode), "000-000-003", "�ټ�����", "�������", 5));
		mArrayListNppang.add(new NppangClass(55, 2013, 11, 6, 30000, 7, "����", getResources().getColor(R.color.nomal_mode), "000-000-003", "��������", "����6", 6));
		mArrayListNppang.add(new NppangClass(66, 2013, 11, 7, 30000, 8, "����", getResources().getColor(R.color.nomal_mode), "000-000-003", "�ϰ�����", "����7", 7));
		mArrayListNppang.add(new NppangClass(77, 2013, 11, 8, 30000, 9, "����", getResources().getColor(R.color.nomal_mode), "000-000-003", "��������", "����8", 8));
		mArrayListNppang.add(new NppangClass(88, 2013, 11, 9, 30000, 10, "����", getResources().getColor(R.color.nomal_mode), "000-000-003", "��ȩ����", "����9", 9));
		mArrayListNppang.add(new NppangClass(99, 2013, 11, 10, 30000, 11, "����", getResources().getColor(R.color.nomal_mode), "000-000-003", "������", "����10", 10));
		mAdapterOfListOfNppang = new AdapterOfListOfNppang(this, mArrayListNppang);

		mGridViewNppangList = (GridView )findViewById(R.id.grid_view_nppang_list);
		mGridViewNppangList.setAdapter(mAdapterOfListOfNppang);
		mGridViewNppangList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if(isEditMode()){
					// Edit mode �������� �ٲ۴�.
					mAdapterOfListOfNppang.setBackgroundColor((int)id, getResources().getColor(R.color.edit_mode));
					return;
				}
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
				
				// Spinner �� ���� ������ �־��� �׸��� �ҷ������� �ϴ� ������ ��� ��� ó���� ���ΰ�? ����� 0�� position �� selection �Ѵ�.
				mSpinnerNppangCountOfPerson.setSelection(mArrayAdapterNppangCountOfPerson.getPosition(String.valueOf(nppangClass.getN())));
				mSpinnerBankList.setSelection(mArrayAdapterBankList.getPosition(String.valueOf(nppangClass.getAccountBank())));
				mSpinnerItemList.setSelection(mArrayAdapterNppangItem.getPosition(String.valueOf(nppangClass.getItem())));
				// return ���� false �̸� long click �� ���� �� click �̺�Ʈ�� �Ҹ���.
				
				
				setEditMode();
				// Edit mode �������� �ٲ۴�.
				mAdapterOfListOfNppang.setBackgroundColor((int)id, getResources().getColor(R.color.edit_mode));				

				return true;
			}

		});
		
		
		
		
		

	}
	
	private void setEditMode(){
		// Edit mode �� �ٲ۴�.
		mCurrentMode = EtcClass.EDIT_MODE;

		// �޴��� ��� �� �ٽ� �����Ѵ�. (ActionBar �� ����� �ϱ� ����)
		mMenu.clear();
		onCreateOptionsMenu(mMenu);
	}
	private void setNomalMode(){
		mCurrentMode = EtcClass.NOMAL_MODE;
		// Nomal mode �������� �ٲ۴�.
		mAdapterOfListOfNppang.setAllBackgroundColor(getResources().getColor(R.color.nomal_mode));

		// �޴��� ��� �� �ٽ� �����Ѵ�. (ActionBar �� ����� �ϱ� ����)
		mMenu.clear();
		onCreateOptionsMenu(mMenu);
	}
	private boolean isEditMode(){
		if(mCurrentMode == EtcClass.EDIT_MODE){
			return true;
		}
		return false;
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
		
		// ActionBar�� Icon ũ�⸦ ��ü ���� ȭ���� 1/10 �� �Ѵ�.
		int iconSize = mWidth/2/5;
		MarginLayoutParams margin;
		margin = new ViewGroup.MarginLayoutParams(new LinearLayout.LayoutParams(iconSize, iconSize));
		//setMargins(int left, int top, int right, int bottom)
		margin.setMargins(0, 0, (int)(mActionBar.getHeight()*0.2), 0);	// ActionBar�� ���̸� �������� Icon�� 20%�� ������ �д�.
		
		// Edit mode �϶�
		if(isEditMode()){
			// edit mode �� menu set
			getMenuInflater().inflate(R.menu.edit_mode, menu);
			// action bar �� �����´�. custom_actionbar_at_edit_mode.xml �� �������Ե�
			View v = menu.findItem(R.id.action_bar).getActionView();
			// ������ ũ�� ����
			v.findViewById(R.id.image_button_share).setLayoutParams(new LinearLayout.LayoutParams(margin));			
			v.findViewById(R.id.image_button_pallet).setLayoutParams(new LinearLayout.LayoutParams(margin));
			v.findViewById(R.id.image_button_alarm).setLayoutParams(new LinearLayout.LayoutParams(margin));
			v.findViewById(R.id.image_button_save).setLayoutParams(new LinearLayout.LayoutParams(margin));
			v.findViewById(R.id.image_button_refresh).setLayoutParams(new LinearLayout.LayoutParams(margin));
			// ActionBar Title ����
			((TextView)v.findViewById(R.id.text_view_title)).setText(R.string.app_name);
		}
		else{
			// nomal mode �� menu set
			getMenuInflater().inflate(R.menu.nomal_mode, menu);	
			// action bar �� �����´�. custom_actionbar_at_nomal_mode.xml �� �������Ե�
			View v = menu.findItem(R.id.action_bar).getActionView();
			// ������ ũ�� ����
			v.findViewById(R.id.image_button_calender).setLayoutParams(new LinearLayout.LayoutParams(margin));
			v.findViewById(R.id.image_button_help).setLayoutParams(new LinearLayout.LayoutParams(margin));
			// ActionBar Title ����
			((TextView)v.findViewById(R.id.text_view_title)).setText(R.string.app_name);
		}
		mMenu = menu;
		return true;
	}
	
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		
		
		Rect notificationBar = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(notificationBar);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mWidth = metrics.widthPixels;

		// SlidingDrawer �� ��ġ �� Bottom Offset �� �������ش�. (����, ��, ������, �Ʒ�, offset)
		// Offset �� �Ʒ� ����, ActionBar, Notibar, Main ȭ���� ������ �κ��� 90%�� �Ѵ�.
		//mSlidingDrawerDownToUp.setBottomOffset(-400);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, metrics.heightPixels/10);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mCaulyAdView.setLayoutParams(lp);
		
		mSlidingDrawerDownToUp.setOffset(0, 0, 
				metrics.widthPixels, metrics.heightPixels-metrics.heightPixels/10 - notificationBar.top,				
				//-((int)((metrics.heightPixels-metrics.heightPixels/10-mActionBar.getHeight() - mLinearLayoutMain.getHeight())*0.9)));
				-400);
				
		// �⺻���� ���� �ִ� ����
		mSlidingDrawerDownToUp.close();
		
		
		mGridViewNppangList.setColumnWidth(mGridViewNppangList.getWidth()/2);
		
		// Nomal ���� ���� ���ش�.
		setNomalMode();
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	// Action bar ���ʿ� ������� ���Ѵ�.
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
		// Left Drawer �� ������ �Ѵ�.
		if (mDrawerToggle.onOptionsItemSelected(item)) {			
			return true;
		}
		Toast.makeText(MainActivity.this, ""+item.getItemId(), Toast.LENGTH_SHORT).show();
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
		// Edit ����̸� Nomal ���� �ٲ��ش�.
		if(isEditMode()){
			setNomalMode();

			return;
		}
		// mCloaeApplicationFlag �� false �̸� ù��°�� Ű�� ���� ���̴�.
		if(mCloseApplicationFlag == false) { // Back Ű�� ù��°�� ���� ���
			// �ȳ� �޼����� �佺Ʈ�� ����Ѵ�.
			Toast.makeText(this, "���Ű�� ���� �ѹ� �� �����ø� ����˴ϴ�.", Toast.LENGTH_LONG).show();
			// ���°� ����
			mCloseApplicationFlag = true;
			// �ڵ鷯�� �̿��Ͽ� 3�� �Ŀ� 0�� �޼����� �����ϵ��� �����Ѵ�.
			mCloseApplicationHandler.sendEmptyMessageDelayed(0, EtcClass.BACK_DELEAY_TIME_FOR_FINISH_ACTIVITY);
		} else { // Back Ű�� 3�� ���� ���޾Ƽ� �ι� ���� ���

			// ��Ƽ��Ƽ�� �����ϴ� ���� Ŭ������ onBackPressed �޼ҵ带 ȣ���Ѵ�.
			super.onBackPressed();
		}
	}
}
