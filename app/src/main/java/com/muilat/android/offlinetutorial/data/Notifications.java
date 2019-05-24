package com.muilat.android.offlinetutorial.data;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Notifications implements Parcelable {
    private int mID;
    private String mTitle;
    private String mMessage;
    private String mLink;
    private String mImage;


    public Notifications(Cursor data) {
        mID = OfflineTutorialDbHelper.getColumnInt(data, OfflineTutorialContract.NotificationEntry._ID);
        mTitle = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.NotificationEntry.COLUMN_TITLE);
        mMessage = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.NotificationEntry.COLUMN_MESSAGE);
        mImage = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.NotificationEntry.COLUMN_IMAGE);
        mLink = OfflineTutorialDbHelper.getColumnString(data, OfflineTutorialContract.NotificationEntry.COLUMN_LINK);

    }

    protected Notifications(Parcel in) {
        mID = in.readInt();
        mTitle = in.readString();
        mMessage = in.readString();
        mLink = in.readString();
        mImage = in.readString();
    }

    public static final Creator<Notifications> CREATOR = new Creator<Notifications>() {
        @Override
        public Notifications createFromParcel(Parcel in) {
            return new Notifications(in);
        }

        @Override
        public Notifications[] newArray(int size) {
            return new Notifications[size];
        }
    };

    public int getId() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getImage() {
        return mImage;
    }

    public String getLink() {
        return mLink;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
        parcel.writeString(mTitle);
        parcel.writeString(mMessage);
        parcel.writeString(mLink);
        parcel.writeString(mImage);
    }
}

