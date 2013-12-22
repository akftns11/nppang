package com.dream2d.nppang;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


// http://blog.naver.com/PostView.nhn?blogId=javaking75&logNo=140177619818

// Singleton pattern

/**
 * 싱글톤으로 구현해본 SQLITE 이용 클래스.
 * 북마크 관리하는 테이블을 예로 들어보았습니다.
 * 이용방법은 먼저 DBAdapter.initialize 메소드를 호출후에 Connection을 생성후 사용합니다.
 */
public class DBAdapter {

	protected static final String DATABASE_NAME = "nppang.db";
	protected static final int DATABASE_VERSION = 1;

	private static Context mContext;
	private static SQLiteOpenHelper dbHelper;
	private static SQLiteDatabase connection;
	

	private static DBAdapter instance;

	public static void connect(Context context) {
		mContext = context;
		dbHelper = new DatabaseHelper(mContext);
		connection = dbHelper.getWritableDatabase();
	}
	
	private DBAdapter() {}
	public synchronized static DBAdapter getInstance() {
		if ( instance == null ) {
			instance = new DBAdapter();
		}
		return instance;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "
					+ EtcClass.DB_TABLE_ACCOUNT
					+ " (" + EtcClass.ACCOUNT_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ EtcClass.ACCOUNT_BANK + " TEXT, "
					+ EtcClass.ACCOUNT_NUMBER + " TEXT, "
					+ EtcClass.ACCOUNT_OWNER + " TEXT);");
			db.execSQL("CREATE TABLE "
					+ EtcClass.DB_TABLE_LAST_SELECTED
					+ " (" + EtcClass.LAST_SELECTED_PRIMARY_KEY  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ EtcClass.LAST_SELECTED_NPPANG_ITEM  + " TEXT, "
					+ EtcClass.LAST_SELECTED_ACCOUNT_BANK   + " TEXT, "
					+ EtcClass.LAST_SELECTED_ACCOUNT_NUMBER   + " TEXT, "
					+ EtcClass.LAST_SELECTED_ACCOUNT_OWNER  + " TEXT);");		
			
			db.execSQL("CREATE TABLE "
					+ EtcClass.DB_TABLE_NPPANG_ITEM
					+ " (" + EtcClass.NPPANG_ITEM_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "					
					+ EtcClass.NPPANG_ITEM + " TEXT);");
			
			db.execSQL("CREATE TABLE "
					+ EtcClass.DB_TABLE_NPPANG
					+ " (" + EtcClass.NPPANG_PRIMARY_KEY  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ EtcClass.NPPANG_TOTAL_AMOUNT + " TEXT, "
					+ EtcClass.NPPANG_DAY + " TEXT, "
					+ EtcClass.NPPANG_MONTH + " TEXT, "
					+ EtcClass.NPPANG_YEAR + " TEXT, "
					+ EtcClass.NPPANG_N + " TEXT, "
					+ EtcClass.NPPANG_ITEM + " TEXT, "
					+ EtcClass.NPPANG_COLOR + " TEXT, "
					+ EtcClass.NPPANG_STATUS + " TEXT, "
					+ EtcClass.ACCOUNT_BANK + " TEXT, "
					+ EtcClass.ACCOUNT_NUMBER + " TEXT, "
					+ EtcClass.ACCOUNT_OWNER + " TEXT);");
			
			
			inputInitDatebaseValue(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// DB 가 업데이트될때 실행됨
			db.execSQL("DROP TABLE IF EXISTS " + EtcClass.DB_TABLE_ACCOUNT);
			db.execSQL("DROP TABLE IF EXISTS " + EtcClass.DB_TABLE_LAST_SELECTED);
			db.execSQL("DROP TABLE IF EXISTS " + EtcClass.DB_TABLE_NPPANG);
			db.execSQL("DROP TABLE IF EXISTS " + EtcClass.DB_TABLE_NPPANG_ITEM);
			onCreate(db);
		}
		
		public void inputInitDatebaseValue(SQLiteDatabase connection){
			ContentValues contentvalue = new ContentValues();
			
			// 마지막으로 선택한 값을 invalid 값으로 초기화한다.
			contentvalue.put(EtcClass.LAST_SELECTED_NPPANG_ITEM , EtcClass.INVALID_VALUE);
			contentvalue.put(EtcClass.LAST_SELECTED_ACCOUNT_BANK , EtcClass.INVALID_VALUE);		
			contentvalue.put(EtcClass.LAST_SELECTED_ACCOUNT_NUMBER  , EtcClass.INVALID_VALUE);
			contentvalue.put(EtcClass.LAST_SELECTED_ACCOUNT_OWNER  , EtcClass.INVALID_VALUE);
			if(connection.insert(EtcClass.DB_TABLE_LAST_SELECTED , null, contentvalue) == 0){
				Toast.makeText(mContext, "DB 입력 실패", Toast.LENGTH_SHORT).show();
			}
			
			// 기본으로 나타날 N빵 Item 을 입력한다.
			String[] mNppangItemList= mContext.getResources().getStringArray(R.array.strings_of_npping_item);
			for(int i=0; i<mNppangItemList.length; i++){
				contentvalue = new ContentValues();
				contentvalue.put(EtcClass.NPPANG_ITEM, mNppangItemList[i]);
				if(connection.insert(EtcClass.DB_TABLE_NPPANG_ITEM, null, contentvalue) == 0){
					Toast.makeText(mContext, "DB 입력 실패 in appendNppangItem", Toast.LENGTH_SHORT).show();
				}					
			}
		}
	}
	
	public static ArrayList<String> getNppangItemList() {	
		ArrayList<String> returnValue = new ArrayList<String>();
		Cursor cursor = connection.rawQuery("SELECT * FROM " + EtcClass.DB_TABLE_NPPANG_ITEM, null);
		while(cursor.moveToNext()){			
			returnValue.add(cursor.getString(cursor.getColumnIndex(EtcClass.NPPANG_ITEM)).toString());			
		}		
		cursor.close();
		return returnValue;
	}

	public static ArrayList<Map<String, String>> getAccountList() {	
		ArrayList<Map<String, String>> returnValue = new ArrayList<Map<String, String>>();
		Cursor cursor = connection.rawQuery("SELECT * FROM " + EtcClass.DB_TABLE_ACCOUNT, null);
		while(cursor.moveToNext()){
			Map<String, String> accountInfomation = new HashMap<String, String>();
			accountInfomation.put(EtcClass.ACCOUNT_PRIMARY_KEY, cursor.getString(cursor.getColumnIndex(EtcClass.ACCOUNT_PRIMARY_KEY)).toString());
			accountInfomation.put(EtcClass.ACCOUNT_BANK, cursor.getString(cursor.getColumnIndex(EtcClass.ACCOUNT_BANK)).toString());
			accountInfomation.put(EtcClass.ACCOUNT_NUMBER, cursor.getString(cursor.getColumnIndex(EtcClass.ACCOUNT_NUMBER)).toString());
			accountInfomation.put(EtcClass.ACCOUNT_OWNER, cursor.getString(cursor.getColumnIndex(EtcClass.ACCOUNT_OWNER)).toString());
			returnValue.add(accountInfomation);
		}		
		cursor.close();
		return returnValue;
	}
	
	public static Map<String, String> getLastSelected() {	
		Map<String, String> returnValue = new HashMap<String, String>();
		Cursor cursor = connection.rawQuery("SELECT * FROM " + EtcClass.DB_TABLE_LAST_SELECTED, null);
		while(cursor.moveToNext()){			
			returnValue.put(EtcClass.LAST_SELECTED_PRIMARY_KEY , cursor.getString(cursor.getColumnIndex(EtcClass.LAST_SELECTED_PRIMARY_KEY )).toString());
			returnValue.put(EtcClass.LAST_SELECTED_NPPANG_ITEM , cursor.getString(cursor.getColumnIndex(EtcClass.LAST_SELECTED_NPPANG_ITEM )).toString());
			returnValue.put(EtcClass.LAST_SELECTED_ACCOUNT_BANK , cursor.getString(cursor.getColumnIndex(EtcClass.LAST_SELECTED_ACCOUNT_BANK )).toString());
			returnValue.put(EtcClass.LAST_SELECTED_ACCOUNT_NUMBER , cursor.getString(cursor.getColumnIndex(EtcClass.LAST_SELECTED_ACCOUNT_NUMBER )).toString());
			returnValue.put(EtcClass.LAST_SELECTED_ACCOUNT_OWNER , cursor.getString(cursor.getColumnIndex(EtcClass.LAST_SELECTED_ACCOUNT_OWNER )).toString());			
		}		
		cursor.close();
		return returnValue;
	}
	
	public static void setLastSelected(Map<String, String> lastSelected) {	
		ContentValues contentvalue = new ContentValues();
		contentvalue.put(EtcClass.LAST_SELECTED_NPPANG_ITEM, lastSelected.get(EtcClass.LAST_SELECTED_NPPANG_ITEM));
		contentvalue.put(EtcClass.LAST_SELECTED_ACCOUNT_BANK , lastSelected.get(EtcClass.LAST_SELECTED_ACCOUNT_BANK ));
		contentvalue.put(EtcClass.LAST_SELECTED_ACCOUNT_NUMBER , lastSelected.get(EtcClass.LAST_SELECTED_ACCOUNT_NUMBER ));
		contentvalue.put(EtcClass.LAST_SELECTED_ACCOUNT_OWNER , lastSelected.get(EtcClass.LAST_SELECTED_ACCOUNT_OWNER ));
		if(connection.update(EtcClass.DB_TABLE_LAST_SELECTED , contentvalue, EtcClass.LAST_SELECTED_PRIMARY_KEY + "=1", null) == 0){
			Toast.makeText(mContext, "DB 입력 실패", Toast.LENGTH_SHORT).show();
		}
	}
	
	public static void deleteAccount(int accountId){
		connection.execSQL("DELETE FROM " + EtcClass.DB_TABLE_ACCOUNT + " WHERE " + EtcClass.ACCOUNT_PRIMARY_KEY + "=" + accountId + ";");		
	}
	
	public static void appendNppangItem(String item){
		ContentValues contentvalue = new ContentValues();
		contentvalue.put(EtcClass.NPPANG_ITEM, item);
		if(connection.insert(EtcClass.DB_TABLE_NPPANG_ITEM, null, contentvalue) == 0){
			Toast.makeText(mContext, "DB 입력 실패 in appendNppangItem", Toast.LENGTH_SHORT).show();
		}
	}
	
	public static void appendAccount(String bank, String number, String owner){
		ContentValues contentvalue = new ContentValues();
		contentvalue.put(EtcClass.ACCOUNT_BANK, bank);
		contentvalue.put(EtcClass.ACCOUNT_NUMBER, number);
		contentvalue.put(EtcClass.ACCOUNT_OWNER, owner);
		if(connection.insert(EtcClass.DB_TABLE_ACCOUNT, null, contentvalue) == 0){					
			Toast.makeText(mContext, "DB 입력 실패", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public void close() {
		try {
			connection.close();
			dbHelper.close();
		} catch (Exception e) {}
	}
}