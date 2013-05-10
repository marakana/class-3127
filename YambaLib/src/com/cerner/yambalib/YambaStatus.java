package com.cerner.yambalib;

import android.os.Parcel;
import android.os.Parcelable;

import com.marakana.android.yamba.clientlib.YambaClient.Status;

public class YambaStatus implements Parcelable {
	private long id;
	private String user, message;
	private long timestamp;

	public YambaStatus(Status status) {
		this(status.getId(), status.getUser(), status.getMessage(), status
				.getCreatedAt().getTime());
	}

	public YambaStatus(long id, String user, String message, long timestamp) {
		super();
		this.id = id;
		this.user = user;
		this.message = message;
		this.timestamp = timestamp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	// --- Parcelable Stuff ---

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(user);
		dest.writeString(message);
		dest.writeLong(timestamp);
	}

	public static final Parcelable.Creator<YambaStatus> CREATOR = new Parcelable.Creator<YambaStatus>() {

		@Override
		public YambaStatus createFromParcel(Parcel source) {
			return new YambaStatus(source.readLong(), source.readString(),
					source.readString(), source.readLong());
		}

		@Override
		public YambaStatus[] newArray(int size) {
			return new YambaStatus[size];
		}
	};
}
