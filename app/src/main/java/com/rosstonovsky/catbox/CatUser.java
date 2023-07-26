package com.rosstonovsky.catbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.os.UserManager;
import android.util.Log;


/**
 * Contains information regarding to current user.
 * Make sure to call {@link CatManager#init(Context)} before you use this class
 */
public class CatUser {

	public static final String TAG = "CatUser";

	private static String appFilesFolder;

	private static String appDataFolder;

	private static String dataFolder;

	private static long id = -1;

	/**
	 * Depending on Android version, you can't access the folder outside of your app using path from context.getFilesDir()
	 * @return Files folder for own app or null if isn't initialized.
	 */
	public static String getAppFilesFolder() {
		return appFilesFolder;
	}

	/**
	 * Depending on Android version, you can't access the folder outside of your app using path from context.getDataDir()
	 * @return Data folder for own app or null if isn't initialized.
	 */
	public static String getAppDataFolder() {
		return appDataFolder;
	}

	/**
	 * @return Data folder of current user or null if isn't initialized.
	 */
	public static String getDataFolder() {
		return dataFolder;
	}

	/**
	 * @return ID of current user or -1 if isn't initialized.
	 */
	public static long getId() {
		return id;
	}

	/**
	 * Package-private method.
	 * Initialize CatUser class, called by {@link CatManager#init(Context)}
	 */
	@SuppressLint("SdCardPath")
	static void makeUser(Context context) {
		try {
			UserManager um = (UserManager) context.getSystemService(Context.USER_SERVICE);
			id = um.getSerialNumberForUser(Process.myUserHandle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (id == -1) {
			Log.e(TAG, "Failed to get user id, using default");
			id = 0;
		}
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
			dataFolder = "/data_mirror/data_ce/null/" + id;
			appDataFolder = "/data_mirror/data_ce/null/" + id + "/" + context.getPackageName();
			appFilesFolder = appDataFolder + "/files";
			return;
		}
		dataFolder = "/data/user/" + id;
		appDataFolder = "/data/user/" + id + "/" + context.getPackageName();
		appFilesFolder = appDataFolder + "/files";
	}
}
