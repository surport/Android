package com.ruyicai.util.wap;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class UpdateAPN {
	public String current;

	public String name;

	private Uri uriApn;

	private Uri uriNowApn;

	private Context context;

	public UpdateAPN(Context context) {
		this.uriApn = Uri.parse("content://telephony/carriers");
		this.uriNowApn = Uri.parse("content://telephony/carriers/preferapn");
		this.name = "cmnet";
		this.current = "1";
		this.context = context;
	}

	private String getAPN() {
		String str1 = null;
		ContentResolver localContentResolver = context.getContentResolver();
		Cursor localCursor = localContentResolver.query(this.uriApn, null,
				"apn LIKE '%cmnet%'  ", null, null);
		if (localCursor == null) {
			return null;
		}
		for (localCursor.moveToFirst(); !localCursor.isAfterLast(); localCursor
				.moveToNext()) {
			String apnName = localCursor.getString(
					localCursor.getColumnIndex("apn")).toLowerCase();
			if (name.equals(apnName)) {
				int m = localCursor.getColumnIndex("_id");
				str1 = localCursor.getString(m);
				return str1;
			}
		}
		localCursor.close();
		return null;
	}

	private String getNowApn() {
		String str1 = null;
		ContentResolver localContentResolver = context.getContentResolver();
		Uri localUri = this.uriNowApn;

		Cursor localCursor = localContentResolver.query(localUri, null, null,
				null, null);
		while (true) {
			if ((localCursor == null) || (!localCursor.moveToNext())) {
				localCursor.close();
				return str1;
			}
			int i = localCursor.getColumnIndex("_id");
			str1 = localCursor.getString(i);
			return str1;

		}
	}

	public boolean updateApn() {
		try {
			String str1 = getAPN();// 列表id cmnet

			String str2 = getNowApn();// 当前连接id cmwap

			if (str1.equals(str2))
				return false;

			ContentResolver localContentResolver = context.getContentResolver();

			ContentValues localContentValues = new ContentValues();

			String str3 = getAPN();// 列表id cmnet

			localContentValues.put("apn_id", str3);

			Uri localUri = this.uriNowApn;
			int i = localContentResolver.update(localUri, localContentValues,
					null, null);

			return true;
		} catch (Exception localException) {
			String str4 = String.valueOf(localException);
			return false;
		}
	}

}
