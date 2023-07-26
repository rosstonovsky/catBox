package com.rosstonovsky.catbox;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Used to pass binder between processes, don't use it in apps
 */
public class CatProvider extends ContentProvider {

	String TAG = "CatProvider";

	@Override
	public Bundle call(String method, String arg, Bundle extras) {
		if (extras == null) {
			Log.e(TAG, "call: received null but expected Bundle");
			return null;
		}
		IBinder catBinder = extras.getBinder("kitten");
		if (catBinder == null) {
			Log.e(TAG, "call: received null but expected IBinder");
			return null;
		}
		CatManager.catService = ICatService.Stub.asInterface(catBinder);
		return new Bundle();
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		return null;
	}

	@Override
	public int delete(Uri uri, String s, String[] strings) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
		return 0;
	}
}
