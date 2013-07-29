package com.ruyicai.databases;

import java.util.ArrayList;
import java.util.List;
import com.ruyicai.pojo.SystemInfoBean;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OperatingDataBases {
//	private Context mContext;
//	private List<SystemInfoBean> infoList;
	private SystemPushInfoDBOpenHelper systemInfoDBOpenHelper;
	private String dbName = "system_push_info.db";
	public OperatingDataBases(Context context) {
//		this.mContext = context;
		
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
	
	public void delete(List<String> list) {
		SQLiteDatabase db = systemInfoDBOpenHelper.getWritableDatabase();
		for (int i = 0; i<list.size(); i++) {
			db.execSQL("delete from system_info where _id in(?)",
					new Object[]{list.get(i)});
		}
		if (db != null && db.isOpen()) {
			db.close();
		}
//		delete table where id in (3,4,5,6,7)
	}

	public List<SystemInfoBean> getInfoList() {
		List<SystemInfoBean> infoList = new ArrayList<SystemInfoBean>();
		SQLiteDatabase db = systemInfoDBOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select _id, content from system_info  order by _id desc", null);
		if(cursor != null){
			while(cursor.moveToNext()) {
				SystemInfoBean bean = new SystemInfoBean();
				String _id = cursor.getString(cursor.getColumnIndex("_id"));
				String content = cursor.getString(cursor.getColumnIndex("content"));
				bean.set_id(_id);
				bean.setContent(content);
				infoList.add(bean);
			}
			cursor.close();
		}
		if (db != null && db.isOpen()) {
			db.close();
		}
		return infoList;
	}
}
