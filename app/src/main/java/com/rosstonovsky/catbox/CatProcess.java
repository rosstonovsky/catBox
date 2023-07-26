package com.rosstonovsky.catbox;

import static com.rosstonovsky.catbox.CatManager.catService;

import android.os.RemoteException;

/**
 * Manage package processes
 */
public class CatProcess {
	private final String packageName;

	public CatProcess(String packageName) {
		this.packageName = packageName;
	}

	public void kill() throws RemoteException {
		catService.kill(packageName);
	}

	public void start() throws RemoteException {
		catService.start(packageName);
	}

	public static void kill(int pid) {
		try {
			catService.killPid(pid);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get running processes. Usually processName is equal to package name of the app
	 * @return
	 */
	public static CatProcessInfo[] getRunningProcesses() {
		try {
			return catService.getRunningProcesses();
		} catch (RemoteException e) {
			e.printStackTrace();
			return new CatProcessInfo[0];
		}
	}
}
