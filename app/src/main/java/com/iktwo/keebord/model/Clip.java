package com.iktwo.keebord.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Clip implements Parcelable {
    public static final Parcelable.Creator<Clip> CREATOR = new Parcelable.Creator<Clip>() {
        @Override
        public Clip createFromParcel(Parcel source) {
            return new Clip(source);
        }

        @Override
        public Clip[] newArray(int size) {
            return new Clip[size];
        }
    };

    private String content;

    public Clip() {
    }

    public Clip(String content) {
        this.content = content;
    }

    protected Clip(Parcel in) {
        this.content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }

    /// TODO: add date here
}
