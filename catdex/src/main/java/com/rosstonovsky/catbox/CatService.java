package com.rosstonovsky.catbox;

import static com.rosstonovsky.catbox.Main.TAG;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IPowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class CatService extends ICatService.Stub {

	/**
	 * Meow!
	 */
	@Override
	public void meow() {
		Log.d(TAG, "Meow!");
	}

	@Override
	public boolean delete(String path) {
		return new File(path).delete();
	}

	@Override
	public boolean isAbsolute(String path) {
		return new File(path).isAbsolute();
	}

	@Override
	public boolean isDirectory(String path) {
		return new File(path).isDirectory();
	}

	@Override
	public boolean isFile(String path) {
		return new File(path).isFile();
	}

	@Override
	public boolean isHidden(String path) {
		return new File(path).isHidden();
	}

	@Override
	public long length(String path) {
		return new File(path).length();
	}

	@Override
	public String[] list(String path) {
		return new File(path).list();
	}

	@Override
	public boolean mkdir(String path) {
		return new File(path).mkdir();
	}

	@Override
	public boolean mkdirs(String path) {
		return new File(path).mkdirs();
	}

	@Override
	public boolean setExecutable(String path, boolean executable, boolean ownerOnly) {
		return new File(path).setExecutable(executable, ownerOnly);
	}

	@Override
	public boolean setReadable(String path, boolean readable, boolean ownerOnly) {
		return new File(path).setReadable(readable, ownerOnly);
	}

	@Override
	public boolean setReadonly(String path) {
		return new File(path).setReadOnly();
	}

	@Override
	public boolean setWritable(String path, boolean writable, boolean ownerOnly) {
		return new File(path).setWritable(writable, ownerOnly);
	}

	@Override
	public boolean exists(String path) {
		return new File(path).exists();
	}

	@Override
	public boolean rename(String path, String newName) {
		File oldFile = new File(path);
		File newFile = new File(oldFile.getParent(), newName);
		return oldFile.renameTo(newFile);
	}

	@Override
	public boolean canExecute(String path) {
		return new File(path).canExecute();
	}

	@Override
	public boolean canWrite(String path) {
		return new File(path).canWrite();
	}

	@Override
	public boolean createNewFile(String path) throws RemoteException {
		try {
			return new File(path).createNewFile();
		} catch (IOException e) {
			Log.e(TAG, "createNewFile: ", e);
			return false;
		}
	}

	@Override
	public long lastModified(String path) {
		return new File(path).lastModified();
	}

	@Override
	public byte[] readBytes(String path) throws RemoteException {
		try {
			RandomAccessFile f = new RandomAccessFile(path, "r");
			byte[] b = new byte[(int) f.length()];
			f.readFully(b);
			f.close();
			return b;
		} catch (IOException e) {
			Log.e(TAG, "readAllBytes: ", e);
			return new byte[0];
		}
	}

	@Override
	public void writeBytes(String path, byte[] bytes) throws RemoteException {
		File file = new File(path);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			if (!file.exists() && file.createNewFile())
				Log.e(TAG, "writeBytes: cannot create new file");
			outputStream.write(bytes);
		} catch (IOException e) {
			Log.e(TAG, "writeAllBytes: ", e);
		}
	}

	private IActivityManager am;

	private void getActivityManager() {
		if (am != null)
			return;
		IBinder binder = ServiceManager.getService(Context.ACTIVITY_SERVICE);
		am = Build.VERSION.SDK_INT > 25 ?
				IActivityManager.Stub.asInterface(binder) : ActivityManagerNative.asInterface(binder);

		if (am == null)
			throw new RuntimeException("Failed to get am");
	}

	@Override
	public void kill(String packageName) throws RemoteException {
		getActivityManager();
		am.forceStopPackage(packageName, am.getCurrentUser().id);
	}

	@Override
	public void killPid(int pid) {
		Process.killProcess(pid);
	}

	@Override
	public void start(String packageName) throws RemoteException {
		getActivityManager();
		am.startActivityAsUser((IApplicationThread) null,
				"com.android.launcher3",
				new Intent()
						.setPackage(packageName)
						.addCategory(Intent.CATEGORY_DEFAULT)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED),
				null,
				(IBinder) null,
				(String) null,
				0,
				0,
				(ProfilerInfo) null,
				(Bundle) null,
				am.getCurrentUser().id);
	}

	@Override
	public CatProcessInfo[] getRunningProcesses() {
		getActivityManager();
		List<ActivityManager.RunningAppProcessInfo> taskInfos = am.getRunningAppProcesses();
		CatProcessInfo[] processInfos = new CatProcessInfo[taskInfos.size()];
		for (int i = 0; i < taskInfos.size(); i++) {
			ActivityManager.RunningAppProcessInfo info = taskInfos.get(i);
			processInfos[i] = new CatProcessInfo(info.processName, info.pid);
		}
		return processInfos;
	}

	private IPowerManager pm;

	private void getPowerManager() {
		if (pm != null)
			return;
		IBinder binder = ServiceManager.getService(Context.POWER_SERVICE);
		pm = IPowerManager.Stub.asInterface(binder);

		if (pm == null)
			throw new RuntimeException("Failed to get pm");
	}


	@Override
	public void reboot(boolean confirm, String reason, boolean wait) {
		getPowerManager();
		pm.reboot(confirm, reason, wait);
	}

	@Override
	public void quickReboot() {
		CatProcessInfo[] infos = getRunningProcesses();
		int systemPid = -1;
		for (CatProcessInfo info : infos) {
			if ("system".equals(info.getPackageName())) {
				systemPid = info.getPid();
				break;
			}
		}
		if (systemPid == -1) {
			Log.e(TAG, "quickReboot: failed to get pid");
		}
		Process.killProcess(systemPid);
	}

	@Override
	public void shutdown(boolean confirm, boolean wait) {
		getPowerManager();
		pm.shutdown(confirm, wait);
	}
}
