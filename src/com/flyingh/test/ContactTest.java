package com.flyingh.test;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

public class ContactTest extends AndroidTestCase {
	private static final String TAG = "ContactTest";

	public void testAddContact() {
		ContentResolver contentResolver = getContext().getContentResolver();
		ContentValues values = new ContentValues();
		long id = ContentUris.parseId(contentResolver.insert(Uri.parse("content://com.android.contacts/raw_contacts"), values));
		Uri uri = Uri.parse("content://com.android.contacts/data");
		values.put("raw_contact_id", id);
		values.put("data1", "zhangsan");
		values.put("data2", "zhang");
		values.put("data3", "san");
		values.put("mimetype", "vnd.android.cursor.item/name");
		contentResolver.insert(uri, values);

		values.clear();
		values.put("raw_contact_id", id);
		values.put("data1", "15856788888");
		values.put("data2", 2);
		values.put("mimetype", "vnd.android.cursor.item/phone_v2");
		contentResolver.insert(uri, values);

		values.clear();
		values.put("raw_contact_id", id);
		values.put("data1", "zhangsan's address");
		values.put("data2", 1);
		values.put("mimetype", "vnd.android.cursor.item/postal-address_v2");
		contentResolver.insert(uri, values);

		values.clear();
		values.put("raw_contact_id", id);
		values.put("data1", "haha@gmail.com");
		values.put("data2", 1);
		values.put("mimetype", "vnd.android.cursor.item/email_v2");
		contentResolver.insert(uri, values);

	}

	public void testQueryNameByPhoneNumber() {
		Cursor cursor = getContext().getContentResolver().query(Uri.parse("content://com.android.contacts/data/phones/filter/" + "13512345678"),
				new String[] { "display_name" }, null, null, null);
		if (cursor.moveToFirst()) {
			Log.i(TAG, cursor.getString(cursor.getColumnIndex("display_name")));
		}
	}

	public void testQuery() {
		Cursor cursor = getContext().getContentResolver().query(Uri.parse("content://com.android.contacts/contacts"),
				new String[] { "_id", "display_name" }, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
			Log.i(TAG, id + "---" + display_name);
			Cursor dataCursor = getContext().getContentResolver().query(Uri.parse("content://com.android.contacts/contacts/" + id + "/data"),
					new String[] { "data1", "mimetype" }, "raw_contact_id=?", new String[] { String.valueOf(id) }, null);
			StringBuilder sb = new StringBuilder();
			while (dataCursor.moveToNext()) {
				String data1 = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				String mimetype = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
					sb.append("phone:" + data1).append("-");
				} else if ("vnd.android.cursor.item/postal-address_v2".equals(mimetype)) {
					sb.append("address:" + data1).append("-");
				} else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
					sb.append("email:" + data1).append("-");
				} else if ("vnd.android.cursor.item/name".equals(mimetype)) {
					sb.append("name:" + data1).append("-");
				}
			}
			Log.i(TAG, sb.deleteCharAt(sb.length() - 1).toString());
		}
	}
}
