package com.example.android.spotify1.utils;

/**
 * Created by dgonzalez on 6/17/15.
 */
public class TopTenListItem {
    private String mTrackName;
    private String mAlbumName;
    private String mLargeImage;
    private String mSmallImage;
    private String mUrlStream;

    public String getTrackName() {
        return mTrackName;
    }

    public void setTrackName(final String mTrackName) {
        this.mTrackName = mTrackName;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(final String mAlbumName) {
        this.mAlbumName = mAlbumName;
    }

    public String getLargeImage() {
        return mLargeImage;
    }

    public void setLargeImage(final String mLargeImage) {
        this.mLargeImage = mLargeImage;
    }

    public String getSmallImage() {
        return mSmallImage;
    }

    public void setSmallImage(final String mSmallImage) {
        this.mSmallImage = mSmallImage;
    }

    public String getUrlStream() {
        return mUrlStream;
    }

    public void setUrlStream(final String mUrlStream) {
        this.mUrlStream = mUrlStream;
    }

    public TopTenListItem(final String mTrackName, final String mAlbumName, final String mLargeImage, final String mSmallImage, final String mUrlStream) {
        this.mTrackName = mTrackName;
        this.mAlbumName = mAlbumName;
        this.mLargeImage = mLargeImage;
        this.mSmallImage = mSmallImage;
        this.mUrlStream = mUrlStream;
    }
}
