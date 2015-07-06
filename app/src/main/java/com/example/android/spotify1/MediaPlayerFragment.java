package com.example.android.spotify1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.spotify1.utils.NamesIds;
import com.example.android.spotify1.utils.TopTenListItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TransferQueue;

import kaaes.spotify.webapi.android.models.Album;


/**
 * A placeholder fragment containing a simple view.
 */
public class MediaPlayerFragment extends Fragment {

    private ArrayList<TopTenListItem> mTopTenList;
    private TopTenActivity mPlaying;
    private int mPosition;

    private TextView mArtistName;
    private TextView mAlbumName;
    private ImageView mAlbumImage;
    private TextView mTrackName;
    private TextView mTrackStart;
    private TextView mTrackEnd;
    private ImageButton mPreviousButton;
    private ImageButton mPlayPauseButton;
    private ImageButton mNextButton;

    public MediaPlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_player, container, false);

        Intent intent = getActivity().getIntent();
        mPosition = intent.getIntExtra(NamesIds.LIST_POSITION, 0);
        mTopTenList = intent.getParcelableArrayListExtra(NamesIds.LIST_MEDIA);

        mArtistName = (TextView) view.findViewById(R.id.artist_name_text_view);
        mAlbumName = (TextView) view.findViewById(R.id.album_name_text_view);
        mAlbumImage = (ImageView) view.findViewById(R.id.album_image_image_view);
        mTrackName = (TextView) view.findViewById(R.id.track_name_text_view);
        mTrackStart = (TextView) view.findViewById(R.id.track_start_text_view);
        mTrackEnd = (TextView) view.findViewById(R.id.track_end_text_view);
        mPreviousButton = (ImageButton) view.findViewById(R.id.prev_image_button);
        mPlayPauseButton = (ImageButton) view.findViewById(R.id.play_pause_image_button);
        mNextButton = (ImageButton) view.findViewById(R.id.next_image_button);

        return view;
    }
}
