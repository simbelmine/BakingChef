package com.example.android.bakingchef;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingchef.models.Recipe;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link DetailActivity}
 * on handsets.
 */
public class DetailFragment extends Fragment implements ExoPlayer.EventListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    /**
     * The dummy content this fragment is presenting.
     */
    private Recipe recipe;
    private boolean isTwoPane;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView titleView;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;

    private String songUrl = "http://www.mfiles.co.uk/mp3-downloads/edvard-grieg-peer-gynt1-morning-mood-piano.mp3";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(DetailActivity.RECIPE)) {
            recipe = getArguments().getParcelable(DetailActivity.RECIPE);
        }

        if (getArguments().containsKey(DetailActivity.IS_TWO_PANE)) {
            isTwoPane = getArguments().getBoolean(DetailActivity.IS_TWO_PANE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        initViews(rootView);

        if(!isTwoPane) {
            if(viewPager != null) {
                setupViewPager();
                tabLayout.setupWithViewPager(viewPager);
            }
        }

        if (recipe != null) {
            titleView.setText(recipe.getName());
        }

        exoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.recipe_video_720x340));
        initializeExoPlayer(Uri.parse(songUrl));

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void initializeExoPlayer(Uri uri) {
        if(exoPlayer != null) return;

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        exoPlayerView.setPlayer(exoPlayer);

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), getResources().getString(R.string.app_name)), new DefaultBandwidthMeter());
        MediaSource mediaSource = new ExtractorMediaSource(
                uri,
                dataSourceFactory,
                new DefaultExtractorsFactory(),
                null,
                null
        );

        exoPlayer.addListener(this);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.setRecipe(recipe);
        adapter.setIsTwoPane(isTwoPane);
        adapter.addFragment(new IngredientsFragment(), "Ingredients");
        adapter.addFragment(new StepsFragment(), "Steps");
        viewPager.setAdapter(adapter);
    }

    private void initViews(View rootView) {
        titleView = (TextView) rootView.findViewById(R.id.item_detail);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        exoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }
}
