package com.vinnymac.bittool;

import android.os.Parcel;
import android.os.Parcelable;

public class Tick implements Parcelable {
	private final String tradableIdentifier;
	private final String last;
	private final String bid;
	private final String ask;
	private final String high;
	private final String low;
	private final String volume;
	private final String timestamp;

	// Constructor
	public Tick(String tradableIdentifier, String last, String bid, String ask,
			String high, String low, String volume, String timestamp) {
		this.tradableIdentifier = tradableIdentifier;
		this.last = last;
		this.bid = bid;
		this.ask = ask;
		this.high = high;
		this.low = low;
		this.volume = volume;
		this.timestamp = timestamp;
	}

	// Getter and setter methods

	public String getTradableIdentifier() {

		return tradableIdentifier;
	}

	public String getLast() {

		return last;
	}

	public String getBid() {

		return bid;
	}

	public String getAsk() {

		return ask;
	}

	public String getHigh() {

		return high;
	}

	public String getLow() {

		return low;
	}

	public String getVolume() {

		return volume;
	}

	public String getTimestamp() {

		return timestamp;
	}

	@Override
	public String toString() {

		return "Ticker [tradableIdentifier=" + tradableIdentifier + ", last="
				+ last + ", bid=" + bid + ", ask=" + ask + ", high=" + high
				+ ", low=" + low + ", volume=" + volume + ", timestamp="
				+ timestamp + "]";
	}

	// Parcelling part
	public Tick(Parcel in) {
		String[] data = new String[8];

		in.readStringArray(data);
		this.tradableIdentifier = data[0];
		this.last = data[1];
		this.bid = data[2];
		this.ask = data[3];
		this.high = data[4];
		this.low = data[5];
		this.volume = data[6];
		this.timestamp = data[7];
	}

	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Tick createFromParcel(Parcel in) {
			return new Tick(in);
		}

		public Tick[] newArray(int size) {
			return new Tick[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { this.tradableIdentifier,
				this.last, this.bid, this.ask, this.high, this.low,
				this.volume, this.timestamp });
	}

}
