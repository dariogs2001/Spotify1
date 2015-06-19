package com.example.android.spotify1.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dario on 6/16/2015.
 */
public class ArtistListItem implements Parcelable {
    String mArtistName;
    String mArtistId;
    String mImageUri;

    protected ArtistListItem(Parcel in) {
        mArtistName = in.readString();
        mArtistId = in.readString();
        mImageUri = in.readString();
    }

    public String getArtistId() {
        return mArtistId;
    }

    public void setmArtistId(String artistId){
        mArtistId = artistId;
    }

    public void setmArtistName(String artistName){
        mArtistName = artistName;
    }

    public String getArtistName(){
        return mArtistName;
    }

    public String getImageUri(){
        return mImageUri;
    }

    public void setImageUri(String imageUri){
        mImageUri = imageUri;
    }

    public ArtistListItem(String artistId, String artistName, String imageUri){
        mArtistId = artistId;
        mArtistName = artistName;
        mImageUri = imageUri;
    }

    public static final Creator<ArtistListItem> CREATOR = new Creator<ArtistListItem>() {
        @Override
        public ArtistListItem createFromParcel(Parcel in) {
            return new ArtistListItem(in);
        }

        @Override
        public ArtistListItem[] newArray(int size) {
            return new ArtistListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mArtistName);
        dest.writeString(mArtistId);
        dest.writeString(mImageUri);
    }
}
