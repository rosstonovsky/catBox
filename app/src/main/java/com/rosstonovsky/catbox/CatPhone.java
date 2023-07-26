package com.rosstonovsky.catbox;

import static com.rosstonovsky.catbox.CatManager.catService;

import android.os.Build;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

/**
 * Various functions to manage phone.
 */
public class CatPhone {

	/**
	 * The value to pass as the 'reason' argument to reboot() to reboot into
	 * recovery mode for tasks other than applying system updates, such as
	 * doing factory resets.
	 */
	public static final String REBOOT_RECOVERY = "recovery";

	/**
	 * The value to pass as the 'reason' argument to reboot() to reboot into
	 * recovery mode for applying system updates.
	 */
	public static final String REBOOT_RECOVERY_UPDATE = "recovery-update";

	/**
	 * The value to pass as the 'reason' argument to reboot() when device owner requests a reboot on
	 * the device.
	 */
	public static final String REBOOT_REQUESTED_BY_DEVICE_OWNER = "deviceowner";

	/**
	 * The 'reason' value used when rebooting in safe mode
	 */
	public static final String REBOOT_SAFE_MODE = "safemode";

	/**
	 * The 'reason' value used for rebooting userspace.
	 */
	@RequiresApi(Build.VERSION_CODES.R)
	public static final String REBOOT_USERSPACE = "userspace";

	/**
	 * The 'reason' value used when rebooting the device without turning on the screen.
	 */
	public static final String REBOOT_QUIESCENT = "quiescent";

	/**
	 * The value to pass as the 'reason' argument to android_reboot().
	 */
	public static final String SHUTDOWN_USER_REQUESTED = "userrequested";

	/**
	 * The value to pass as the 'reason' argument to android_reboot() when battery temperature
	 * is too high.
	 */
	public static final String SHUTDOWN_BATTERY_THERMAL_STATE = "thermal,battery";

	/**
	 * The value to pass as the 'reason' argument to android_reboot() when device temperature
	 * is too high.
	 */
	public static final String SHUTDOWN_THERMAL_STATE = "thermal";

	/**
	 * The value to pass as the 'reason' argument to android_reboot() when device is running
	 * critically low on battery.
	 */
	public static final String SHUTDOWN_LOW_BATTERY = "battery";

	/**
	 * constant for shutdown reason being unknown.
	 */
	public static final int SHUTDOWN_REASON_UNKNOWN = 0;

	/**
	 * constant for shutdown reason being normal shutdown.
	 */
	public static final int SHUTDOWN_REASON_SHUTDOWN = 1;

	/**
	 * constant for shutdown reason being reboot.
	 */
	public static final int SHUTDOWN_REASON_REBOOT = 2;

	/**
	 * constant for shutdown reason being user requested.
	 */
	public static final int SHUTDOWN_REASON_USER_REQUESTED = 3;

	/**
	 * constant for shutdown reason being overheating.
	 */
	public static final int SHUTDOWN_REASON_THERMAL_SHUTDOWN = 4;

	/**
	 * constant for shutdown reason being low battery.
	 */
	public static final int SHUTDOWN_REASON_LOW_BATTERY = 5;

	/**
	 * constant for shutdown reason being critical battery thermal state.
	 */
	public static final int SHUTDOWN_REASON_BATTERY_THERMAL = 6;

	/**
	 * Turn off the device.
	 *
	 * @param confirm If true, shows a shutdown confirmation dialog.
	 * @param reason code to pass to android_reboot() (e.g. "userrequested"), or null.
	 * @param wait If true, this call waits for the shutdown to complete and does not return.
	 *
	 */
	public static void reboot(boolean confirm, String reason, boolean wait) throws RemoteException {
		catService.reboot(confirm, reason, wait);
	}

	public static void reboot(String reason) throws RemoteException {
		reboot(false, reason, false);
	}

	/**
	 * Perform a quick reboot
	 */
	public static void quickReboot() {
		try {
			catService.quickReboot();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reboot to system
	 * @throws RemoteException
	 */
	public static void reboot() throws RemoteException {
		reboot(false, REBOOT_REQUESTED_BY_DEVICE_OWNER, false);
	}

	/**
	 * Turn off the device.
	 *
	 * @param confirm If true, shows a shutdown confirmation dialog.
	 * @param wait If true, this call waits for the shutdown to complete and does not return.
	 *
	 */
	public static void shutdown(boolean confirm, boolean wait) throws RemoteException {
		catService.shutdown(confirm, wait);
	}

	/**
	 * Turn off the device.
	 */
	public static void shutdown() throws RemoteException {
		shutdown(false, false);
	}
}
