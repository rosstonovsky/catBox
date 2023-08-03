package com.rosstonovsky.catbox;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Create and hold {@link ICatService} binder
 */
public class CatManager {
	private final static String TAG = "CatManager";

	static ICatService catService;

	/**
	 * Initialize catbox and its components.
	 */
	public static void init(@NotNull Context context) throws IOException, RuntimeException {
		if (catService != null) {
			Log.e(TAG, "CatManager.init: init already called");
			return;
		}
		unpackCatDex(context);
		CatUser.makeUser(context);
		Process process = Runtime.getRuntime().exec("su");
		try {
			OutputStream out = new DataOutputStream(process.getOutputStream());
			InputStream in = process.getInputStream();
			String dex = "\"" + CatUser.getAppFilesFolder() + "/catbox.dex\"";

			/*
			 Need both Djava.class.path and cp to work on some weird systems
			 some arguments may not exist such as --nice-name or cmd dir
			 */
			out.write(("app_process -Djava.class.path=" + dex +
							" -cp " + dex +
							" /system/bin com.rosstonovsky.catbox.Main " +
							context.getPackageName() +
							" || " +
							"app_process -Djava.class.path=" + dex +
							" -cp " + dex +
							" com.rosstonovsky.catbox.Main " +
							context.getPackageName() +
							"\nexit\n")
					.getBytes(StandardCharsets.UTF_8));
			out.flush();
			//wait until got binder
			int code = in.read();
			Log.i(TAG, "cat code: " + code);
			switch (code) {
				case 0:
					if (catService == null) {
						Log.e(TAG, "catService is null :(");
						return;
					}
					Log.i(TAG, "Successfully got catService");
					break;
				case 1:
					try {
						out.close();
						in.close();
					} catch (Exception ignored) {
					}
					throw new RuntimeException("Root access denied");
				case 2:
					try {
						out.close();
						in.close();
					} catch (Exception ignored) {
					}
					throw new RuntimeException("No arguments passed");
				case 255:
					try {
						out.close();
						in.close();
					} catch (Exception ignored) {
					}
					throw new RuntimeException("catbox threw an exception, check logcat for more details");
				case -1:
					try {
						out.close();
						in.close();
					} catch (Exception ignored) {
					}
					throw new RuntimeException("catbox reached end of the stream");
				default:
					try {
						out.close();
						in.close();
					} catch (Exception ignored) {
					}
					throw new RuntimeException("catbox send unknown character to output stream: " + code);
			}
		} catch (IOException e) {
			// Log exception from error stream if available and rethrow original IOException
			e.printStackTrace();
			try {
				DataInputStream err = new DataInputStream(process.getErrorStream());
				StringBuilder sb = new StringBuilder();
				while (err.available() != 0)
					sb.append((char) err.readByte());
				Log.e(TAG, "init err: " + sb);
				err.close();
			} catch (IOException ignored) {
			}
			throw e;
		}
	}

	private static void unpackCatDex(Context context) throws IOException {
		SharedPreferences sp = context.getSharedPreferences("cat_prefs", Activity.MODE_PRIVATE);
		//if app updates should also update catbox dex
		int version = 0;
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					context.getPackageName(), 0);
			version = info.versionCode;
		} catch (PackageManager.NameNotFoundException ignored) {
		}

		if (sp.getInt("lastAppVersion", 0) == version)
			return;

		InputStream is = context.getAssets().open("catbox.dex");
		FileOutputStream outputStream = new FileOutputStream(context.getFilesDir() + "/catbox.dex", false);
		int read;
		byte[] bytes = new byte[8192];
		while ((read = is.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("lastAppVersion", version);
		editor.apply();

		outputStream.close();
		is.close();
	}


	/**
	 * Make log<em>cat</em> meow
	 */
	public static void meow() {
		try {
			catService.meow();
		} catch (RemoteException ignored) {
		}
	}
}
