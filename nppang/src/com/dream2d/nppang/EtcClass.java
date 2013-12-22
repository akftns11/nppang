package com.dream2d.nppang;

import android.app.ActionBar;

public class EtcClass {
	// action bar 에 나타나는 이미지의 margin
	public static final int ACTIONBAR_IMAGE_SIZE_MARGIN = 50;
	// 최대 Nppang 할 수 있는 인원수
	public static final int MAX_NPPANG_COUNT_OF_PERSON = 10;
	// back key 를 두번 눌러 Activity 를 종료할때 사용하는 Deleay time
	public static final int BACK_DELEAY_TIME_FOR_FINISH_ACTIVITY = 3000;
	// Nomal Mode
	public static final int NOMAL_MODE = 1;
	// Edit Mode
	public static final int EDIT_MODE = 2;
	
	// Account DB
	public static final String DB_TABLE_ACCOUNT = "account";	
	public static final String ACCOUNT_PRIMARY_KEY = "account_primary_key";
	public static final String ACCOUNT_BANK = "account_bank";
	public static final String ACCOUNT_NUMBER = "account_number";
	public static final String ACCOUNT_OWNER = "account_owner";
	// 마지막으로 선택한 값 DB 
	public static final String DB_TABLE_LAST_SELECTED = "last_selected";
	public static final String LAST_SELECTED_PRIMARY_KEY = "last_selected_primary_key";
	public static final String LAST_SELECTED_NPPANG_ITEM = "last_selected_nppang_item";
	public static final String LAST_SELECTED_ACCOUNT_BANK = "last_selected_account_bank";
	public static final String LAST_SELECTED_ACCOUNT_NUMBER = "last_selected_account_number";
	public static final String LAST_SELECTED_ACCOUNT_OWNER = "last_selected_account_owner";
	// N빵 Item DB
	public static final String DB_TABLE_NPPANG_ITEM = "nppang_item";
	public static final String NPPANG_ITEM_PRIMARY_KEY = "nppang_item_primary_key";
	public static final String NPPANG_ITEM = "nppang_item";	

	// N빵 Item DB
	public static final String DB_TABLE_NPPANG = "nppang";
	public static final String NPPANG_PRIMARY_KEY = "nppang_primary_key";
	public static final String NPPANG_TOTAL_AMOUNT= "nppang_total_amount";	
	public static final String NPPANG_DAY = "nppang_day";	
	public static final String NPPANG_MONTH = "nppang_month";	
	public static final String NPPANG_YEAR = "nppang_year";	
	public static final String NPPANG_N = "nppang_n";	
	public static final String NPPANG_COLOR = "nppang_color";	
	public static final String NPPANG_STATUS = "nppang_status";	
	
	// 초기값
	public static final String INVALID_VALUE = "-1";
		
	
}
