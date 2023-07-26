package com.rosstonovsky.catbox;

import static com.rosstonovsky.catbox.CatManager.catService;

import android.os.RemoteException;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Access files with root permissions.
 * Don't use these "files" to make streams, you can only interact with the files using this class.
 */
public class CatFile extends File {

	public CatFile(String pathname) {
		super(pathname);
	}

	public CatFile(String parent, String child) {
		super(parent, child);
	}

	public CatFile(File parent, String child) {
		super(parent, child);
	}

	public CatFile(URI uri) {
		super(uri);
	}

	@Override
	public boolean delete() {
		try {
			return catService.delete(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isAbsolute() {
		try {
			return catService.isAbsolute(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isDirectory() {
		try {
			return catService.isDirectory(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isFile() {
		try {
			return catService.isFile(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isHidden() {
		try {
			return catService.isHidden(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long length() {
		try {
			return catService.length(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public String[] list() {
		try {
			return catService.list(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return new String[0];
		}
	}

	@Override
	public String[] list(FilenameFilter filter) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public CatFile[] listFiles() {
		String[] sList = list();
		CatFile[] files = new CatFile[sList.length];
		for (int i = 0; i < sList.length; i++) {
			files[i] = new CatFile(sList[i]);
		}
		return files;
	}

	@Override
	public File[] listFiles(FileFilter fileFilter) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public File[] listFiles(FilenameFilter filter) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public boolean mkdir() {
		try {
			return catService.mkdir(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean mkdirs() {
		try {
			return catService.mkdirs(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setExecutable(boolean executable) {
		return setExecutable(executable, false);
	}

	@Override
	public boolean setExecutable(boolean executable, boolean ownerOnly) {
		try {
			return catService.setExecutable(getPath(), executable, ownerOnly);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setReadable(boolean readable) {
		return setReadable(readable, false);
	}

	@Override
	public boolean setReadable(boolean readable, boolean ownerOnly) {
		try {
			return catService.setReadable(getPath(), readable, ownerOnly);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setReadOnly() {
		try {
			return catService.setReadonly(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setWritable(boolean writable) {
		return setWritable(writable, false);
	}

	@Override
	public boolean setWritable(boolean writable, boolean ownerOnly) {
		try {
			return catService.setWritable(getPath(), writable, ownerOnly);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean exists() {
		try {
			return catService.exists(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean renameTo(File newFile) {
		if (!exists())
			return false;
		try {
			return catService.rename(getPath(), newFile.getName());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean canExecute() {
		try {
			return catService.canExecute(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean canWrite() {
		try {
			return catService.canWrite(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean createNewFile() throws IOException {
		try {
			return catService.createNewFile(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long lastModified() {
		if (!exists())
			return -1;
		try {
			return catService.lastModified(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public byte[] readBytes() throws IOException {
		if (!exists())
			throw new IOException("File doesn't exist");
		try {
			return catService.readBytes(getPath());
		} catch (RemoteException e) {
			e.printStackTrace();
			return new byte [0];
		}
	}

	public String readText(Charset charset) throws IOException {
		return new String(readBytes(), charset);
	}

	public String readText() throws IOException {
		return readText(StandardCharsets.UTF_8);
	}

	public void writeBytes(byte[] bytes) {
		try {
			catService.writeBytes(getPath(), bytes);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void writeText(String text, Charset charset) {
		writeBytes(text.getBytes(charset));
	}

	public void writeText(String text) {
		writeText(text, StandardCharsets.UTF_8);
	}
}
