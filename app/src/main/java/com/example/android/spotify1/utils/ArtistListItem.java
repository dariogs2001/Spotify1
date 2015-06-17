package com.example.android.spotify1.utils;

/**
 * Created by Dario on 6/16/2015.
 */
public class ArtistListItem {
    String mArtistName;
    String mArtistId;
    String mImageUri;

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
 }
