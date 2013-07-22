package com.ruyicai.databases;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OperatingDataBases {
//	private Context mContext;
	private List<String> infoList;
	private SystemPushInfoDBOpenHelper systemInfoDBOpenHelper;
	private String dbName = "system_push_info.db";
	public OperatingDataBases(Context context) {
//		this.mContext = context;
		infoList = new ArrayList<String>();
		systemInfoDBOpenHelper = new SystemPushInfoDBOpenHelper(context, dbName, null, 1);
	}

	public void insert(String info) {
		SQLiteDatabase db = systemInfoDBOpenHelper.getWritableDatabase();
		db.execSQL("insert into system_info(content) values(?)",
				new Object[]{info});
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	public List<String> getInfoList() {
		SQLiteDatabase db = systemInfoDBOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select content from system_info", null);
		if(cursor != null && cursor.moveToFirst()){
			while(cursor.moveToNext()) {
				String content = cursor.getString(cursor.getColumnIndex("content"));
				infoList.add(content);
			}
			cursor.close();
		}
		if (db != null && db.isOpen()) {
			db.close();
		}
		return infoList;
	}
}
