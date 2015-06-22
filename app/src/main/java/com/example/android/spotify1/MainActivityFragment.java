package com.example.android.spotify1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.spotify1.utils.ArtistListItem;
import com.example.android.spotify1.utils.NamesIds;
import com.example.android.spotify1.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static String TAG = MainActivityFragment.class.getName();

    private ArrayList<ArtistListItem> mArtistsList;
    ArrayAdapter<ArtistListItem> mAdapter;
    private EditText mSearchEditText;
    private String mSearchText;

//    private android.support.v7.widget.SearchView mSearchView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mSearchEditText = (EditText) view.findViewById(R.id.search_edit_text);
//        mSearchView = (android.support.v7.widget.SearchView) view.findViewById(R.id.search_artist);


        mArtistsList = new ArrayList<>();
        mAdapter = new ArtistAdapterList(this.getActivity(), R.layout.artists_list_item);
        ListView searchResultsListView = (ListView) view.findViewById(R.id.results_list_view);
        searchResultsListView.setAdapter(mAdapter);
//
//        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(final String query) {
//                if (query.length() > 0) {
//                    FetchArtistsTask artistsTask = new FetchArtistsTask();
//                    mSearchText = query;
//                    artistsTask.execute(mSearchText);
//                } else {
//                    mAdapter.clear();
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(final String newText) {
//                return false;
//            }
//        });


        searchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArtistListItem item = mAdapter.getItem(position);
                Intent topTenIntent = new Intent(getActivity().getBaseContext(), TopTenActivity.class);
                topTenIntent.putExtra(NamesIds.ARTIST_ID, item.getArtistId());
                topTenIntent.putExtra(NamesIds.ARTIST_NAME, item.getArtistName());
                getActivity().startActivity(topTenIntent);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(NamesIds.SEARCH_RESULT_LIST)) {
            mArtistsList = savedInstanceState.getParcelableArrayList(NamesIds.SEARCH_RESULT_LIST);
            loadAdapter(mArtistsList, mAdapter);
        }
        else {
            SharedPreferences sp = getActivity().getSharedPreferences(NamesIds.SAHRED_PREFERENCES, Context.MODE_PRIVATE);
            mSearchText = sp.getString(NamesIds.SEARCH_TEXT, "");
            mSearchEditText.setText(mSearchText);
            if (mSearchText.length() > 0) {
//                mSearchView.setQuery(mSearchText, true);
                FetchArtistsTask artistsTask = new FetchArtistsTask();
                artistsTask.execute(mSearchText);
            }
        }

        return view;
    }

    private void loadAdapter(final ArrayList<ArtistListItem> mArtistsList, final ArrayAdapter<ArtistListItem> mAdapter) {
        mAdapter.clear();
        for (ArtistListItem item : mArtistsList) {
            String imageUri;
            try {
                imageUri = item.getImageUri();
            }catch (Exception ex){
                imageUri = null;
            }
            mAdapter.add(new ArtistListItem(item.getArtistId(), item.getArtistName(), imageUri));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mSearchEditText == null) return;

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSearchEditText.getText().length() > 0) {
                    FetchArtistsTask artistsTask = new FetchArtistsTask();
                    mSearchText = mSearchEditText.getText().toString();
                    artistsTask.execute(mSearchText);
                } else {
                    mAdapter.clear();
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        //So I also decided to save it on SharePreferences...
        //with this even when the app is closed the last search string will be saved.
        SharedPreferences sp = getActivity().getSharedPreferences(NamesIds.SAHRED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit =  sp.edit();
        edit.putString(NamesIds.SEARCH_TEXT, mSearchText);
        edit.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //This seems to not be working in some cases an in some of my emulators, works when rotating the screen but not when coming back from a different intent.
        outState.putString(NamesIds.SEARCH_TEXT, mSearchText);

        outState.putParcelableArrayList(NamesIds.SEARCH_RESULT_LIST, mArtistsList);
    }

    public class FetchArtistsTask extends AsyncTask<String, Void, ArtistsPager> {
        @Override
        protected ArtistsPager doInBackground(String... params) {

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                ArtistsPager results = spotify.searchArtists(params[0]);

                return results;
            } catch (Exception ex){

                Log.e(TAG, "Error calling Spotify Web API", ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            super.onPostExecute(artistsPager);
            try {
                mAdapter.clear();

                if (!Utils.isInternetConextion(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (artistsPager == null || artistsPager.artists == null || artistsPager.artists.items == null || artistsPager.artists.items.size() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.artists_not_found), Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Artist artist : artistsPager.artists.items) {
                    String imageUri;
                    try {
                        imageUri = artist.images.get(2).url;
                    } catch (Exception ex) {
                        imageUri = null;
                    }
                    mAdapter.add(new ArtistListItem(artist.id, artist.name, imageUri));
                }
            } catch (Exception ex) {

                Log.e(TAG, "Error on onPostExecute", ex);
                return;
            }
        }
    }

    private class ArtistAdapterList extends ArrayAdapter<ArtistListItem> {
        Context mContext;
        public ArtistAdapterList(Context context, int resource) {
            super(context, resource, mArtistsList);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {

                View itemView = convertView;
                if (itemView == null) {
                    itemView = LayoutInflater.from(mContext).inflate(R.layout.artists_list_item, parent, false);
                }

                if (mArtistsList == null) {
                    return null;
                }

                ArtistListItem current = mArtistsList.get(position);
                ImageView imageView = (ImageView) itemView.findViewById(R.id.artist_thumb_nail);

                if (current.getImageUri() != null) {
                    Picasso.with(mContext).load(current.getImageUri()).resize(60, 60).into(imageView);
                }

                TextView artistTextView = (TextView) itemView.findViewById(R.id.artist_name_text_view);
                artistTextView.setText(current.getArtistName());

                TextView idTextView = (TextView) itemView.findViewById(R.id.artist_id_text_view);
                idTextView.setText(current.getArtistId());

                return itemView;
            }
            catch (Exception ex) {
                Log.e(TAG, "Error on getView", ex);
                return null;
            }
        }
    }
}
