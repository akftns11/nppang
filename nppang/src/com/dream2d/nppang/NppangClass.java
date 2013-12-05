package com.dream2d.nppang;


public class NppangClass {
	private int mTotalMoney;
	private int mDay;
	private int mMonth;
	private int mYear;
	private int mN;
	private String mItem;
	private int mColor;
	private int mId;
	private int mStatus;
	private String mAccountNumber;
	private String mAccountOwner;
	private String mAccountBank;
	public NppangClass(int id, int year, int month, int day, int totalMoney, int n, String item, int color, String accountNumber, String accountOwner, String accountBank, int status){
		mId = id;
		mYear = year;
		mMonth = month;
		mDay = day;
		mTotalMoney =totalMoney;
		mN = n;
		mItem = item;
		mColor = color;
		mAccountNumber = accountNumber;
		mAccountOwner = accountOwner;
		mAccountBank = accountBank;
		mStatus = status;
	}
	public int getId(){
		return mId;
	}
	public int getYear(){
		return mYear;
	}
	public int getMonth(){
		return mMonth;
	}
	public int getDay(){
		return mDay;
	}
	public int getTotalMoney(){
		return mTotalMoney;
	}
	public int getN(){
		return mN;
	}
	public String getItem(){
		return mItem;
	}
	public int getColor(){
		return mColor;
	}
	public String getAccountNumber(){
		return mAccountNumber;
	}
	public String getAccountBank(){
		return mAccountBank;
	}
	public String getAccountOwner(){
		return mAccountOwner;
	}
	public int getStatus(){
		return mStatus;
	}
	
}
