package com.example.android.spotify1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.spotify1.utils.TopTenListItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTenActivityFragment extends Fragment {

    public static String TAG = TopTenActivity.class.getName();

    private List<TopTenListItem> mTopTenList;
    ArrayAdapter<TopTenListItem> mAdapter;
    private String mArtistName;

    public TopTenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ten, container, false);

        mTopTenList = new ArrayList<>();
        mAdapter = new TopTenAdapterList(this.getActivity(), R.layout.top_ten_list_item);
        ListView searchResultsListView = (ListView) view.findViewById(R.id.top_ten_list_view);
        searchResultsListView.setAdapter(mAdapter);

        Intent intent = getActivity().getIntent();
        String artistId = intent.getStringExtra("artistId");
        mArtistName = intent.getStringExtra("artistName");

        FetchTopTenTask task = new FetchTopTenTask();
        task.execute(artistId);

        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(mArtistName);
        }
    }

    public class FetchTopTenTask extends AsyncTask<String, Void, Tracks> {

        @Override
        protected Tracks doInBackground(String... params) {

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                Map<String, Object> mapParameters = new HashMap<String, Object>();
                mapParameters.put("country", "US");
                Tracks results = spotify.getArtistTopTrack(params[0], mapParameters);

                return results;
            } catch (Exception ex){

                Log.e(TAG, "Error calling Spotify Web API", ex);

                return null;
            }
        }

        @Override
        protected void onPostExecute(Tracks topTenTracks) {
            super.onPostExecute(topTenTracks);

            mAdapter.clear();

            if (topTenTracks == null) {
                return;
            }

            for (Track track : topTenTracks.tracks) {
                AlbumSimple album = track.album;
                String imageUriSmall;
                String imageUriLarge;
                try {
                    imageUriSmall = album.images.get(2).url;
                    imageUriLarge = album.images.get(0).url;
                }catch (Exception ex){
                    imageUriLarge = imageUriSmall = null;
                }
                mAdapter.add(new TopTenListItem(track.name, album.name, imageUriLarge, imageUriSmall, album.uri));
            }
        }
    }

    private class TopTenAdapterList extends ArrayAdapter<TopTenListItem> {
        Context mContext;
        public TopTenAdapterList(Context context, int resource) {
            super(context, resource, mTopTenList);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = LayoutInflater.from(mContext).inflate(R.layout.top_ten_list_item, parent, false);
            }
            TopTenListItem current = mTopTenList.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.song_thumb_nail);

            if (current.getSmallImage() != null) {
                Picasso.with(mContext).load(current.getSmallImage()).resize(60, 60).into(imageView);
            }

            TextView tractNameTextView = (TextView) itemView.findViewById(R.id.track_name_text_view);
            tractNameTextView.setText(current.getTrackName());

            TextView albmNameTextView = (TextView) itemView.findViewById(R.id.album_name_text_view);
            albmNameTextView.setText(current.getAlbumName());

            return itemView;
        }
    }
}
