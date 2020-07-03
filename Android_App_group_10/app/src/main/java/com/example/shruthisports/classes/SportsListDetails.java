package com.example.shruthisports.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class SportsListDetails implements Parcelable {

    public final String name;

    public SportsListDetails(String name) {
        this.name = name;
    }

    protected SportsListDetails(Parcel in) {
        name = in.readString();
    }

    public static final Creator<SportsListDetails> CREATOR = new Creator<SportsListDetails>() {
        @Override
        public SportsListDetails createFromParcel(Parcel in) {
            return new SportsListDetails(in);
        }

        @Override
        public SportsListDetails[] newArray(int size) {
            return new SportsListDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
