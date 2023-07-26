package com.rosstonovsky.catbox;

import android.app.ActivityManagerNative;
import android.app.ContentProviderHolder;
import android.app.IActivityManager;
import android.app.IActivityManagerPre26;
import android.content.AttributionSource;
import android.content.IContentProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.system.Os;
import android.util.Log;

public class Main {

	public static String TAG = "pussybox";

	public static void main(String[] args) {
		if (Os.getuid() != 0) {
			System.out.print("\1");
			System.out.flush();
			System.exit(0);
			return;
		}
		if (args.length == 0) {
			System.out.print("\2");
			System.out.flush();
			System.exit(0);
			return;
		}
		if (Looper.getMainLooper() == null) {
			Looper.prepareMainLooper();
		}
		Log.i(TAG, "Sending binder...");
		try {
			sendBinder(args[0]);
		} catch (Exception e) {
			System.out.print("\255");
			Log.e(TAG, "Failed to send binder", e);
			return;
		}
		System.out.print("\0");
		System.out.flush();
		Log.i(TAG, "Binder sent successfully");
		Looper.loop();
		Log.i(TAG, "exiting...");
	}

	private static void sendBinder(final String packageName) throws RemoteException {
		CatService service = new CatService();

		IBinder binder = ServiceManager.getService("activity");
		IActivityManager am = Build.VERSION.SDK_INT > 25 ?
				IActivityManager.Stub.asInterface(binder) : ActivityManagerNative.asInterface(binder);

		if (am == null)
			throw new RuntimeException("sendBinder: am is null");

		ContentProviderHolder contentProviderHolder;
		IContentProvider provider;
		String authority = packageName + ".kitten";

		long userId = am.getCurrentUser().id;

		if (Build.VERSION.SDK_INT > 28) {
			contentProviderHolder = am.getContentProviderExternal(authority, (int) userId, null, authority);
			provider = contentProviderHolder != null ? contentProviderHolder.provider : null;
		} else if (Build.VERSION.SDK_INT > 25) {
			contentProviderHolder = am.getContentProviderExternal(authority, (int) userId, null);
			provider = contentProviderHolder != null ? contentProviderHolder.provider : null;
		} else {
			provider = ((IActivityManagerPre26) am).getContentProviderExternal(authority, (int) userId, null).provider;
		}

		Bundle result;
		Bundle pussyBundle = new Bundle();
		pussyBundle.putBinder("kitten", service);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			result = provider.call((new AttributionSource.Builder(0)).setPackageName(null).build(), authority, null, null, pussyBundle);
		} else if (Build.VERSION.SDK_INT == 30) {
			result = provider.call(null, null, authority, null, null, pussyBundle);
		} else if (Build.VERSION.SDK_INT == 29) {
			result = provider.call((String) null, authority, null, null, pussyBundle);
		} else {
			result = provider.call(null, null, null, pussyBundle);
		}

		if (result == null)
			throw new RuntimeException("sendBinder: Bundle result is null, is the app running?");

		Log.i(TAG, "sendBinder: result isn't null");
	}
}