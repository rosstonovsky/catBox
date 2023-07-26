package com.rosstonovsky.catbox;

parcelable CatProcessInfo;

interface ICatService {
    void meow();

    boolean exists(String path);

    long lastModified(String path);

    boolean rename(String path, String newName);

    boolean canExecute(String path);

    boolean canWrite(String path);

    boolean createNewFile(String path);

    boolean delete(String path);

    boolean isAbsolute(String path);

    boolean isDirectory(String path);

    boolean isFile(String path);

    boolean isHidden(String path);

    long length(String path);

    String[] list(String path);

    boolean mkdir(String path);

    boolean mkdirs(String path);

    boolean setExecutable(String path, boolean executable, boolean ownerOnly);

    boolean setReadable(String path, boolean readable, boolean ownerOnly);

    boolean setReadonly(String path);

    boolean setWritable(String path, boolean writable, boolean ownerOnly);

    byte[] readBytes(String path);

    void writeBytes(String path, in byte[] bytes);

    void kill(String packageName);

    void killPid(int pid);

    void start(String packageName);

	CatProcessInfo[] getRunningProcesses();

    void reboot(boolean confirm, String reason, boolean wait);

    void quickReboot();

    void shutdown(boolean confirm, boolean wait);
}