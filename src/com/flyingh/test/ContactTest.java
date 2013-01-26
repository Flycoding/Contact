package com.flyingh.test;

import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

public class ContactTest extends AndroidTestCase {
	private static final String TAG = "ContactTest";

	public void testQuery() {
		Cursor cursor = getContext().getContentResolver().query(
				Uri.parse("content://com.android.contacts/contacts"),
				new String[] { "_id", "display_name" }, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String display_name = cursor.getString(cursor
					.getColumnIndex("display_name"));
			Log.i(TAG, id + "---" + display_name);
		}
	}
}
