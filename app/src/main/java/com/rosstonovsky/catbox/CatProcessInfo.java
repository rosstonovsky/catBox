package com.rosstonovsky.catbox;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contains package name and pid of app.
 */
public class CatProcessInfo implements Parcelable {

	private final String packageName;

	private final int pid;

	public CatProcessInfo(String packageName, int pid) {
		this.packageName = packageName;
		this.pid = pid;
	}

	public CatProcessInfo(Parcel in) {
		packageName = in.readString();
		pid = in.readInt();
	}

	public int getPid() {
		return pid;
	}

	public String getPackageName() {
		return packageName;
	}

	public static final Creator<CatProcessInfo> CREATOR = new Creator<CatProcessInfo>() {
		@Override
		public CatProcessInfo createFromParcel(Parcel in) {
			return new CatProcessInfo(in);
		}

		@Override
		public CatProcessInfo[] newArray(int size) {
			return new CatProcessInfo[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(packageName);
		parcel.writeInt(pid);
	}
}
