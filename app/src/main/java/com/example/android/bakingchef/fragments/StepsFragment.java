package com.example.android.bakingchef.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingchef.R;
import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.helpers.PaneUtils;
import com.example.android.bakingchef.helpers.MyTextUtils;
import com.example.android.bakingchef.models.Recipe;
import com.example.android.bakingchef.models.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class StepsFragment extends Fragment implements View.OnClickListener {
    private Recipe recipe;
    private int step;
    private Button nextStepBtn;
    private Button prevStepBtn;
    private ViewPager viewPager;
    private SharedPreferences sharedPrefs;

    private static SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;
    private DefaultDataSourceFactory dataSourceFactory;

    private SlidingPaneLayout slidingPaneLayout;

    public StepsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getActivity().getSharedPreferences(DetailActivity.DETAILS_PREFS, Context.MODE_PRIVATE);

        if (getArguments().containsKey(DetailActivity.RECIPE)) {
            recipe = getArguments().getParcelable(DetailActivity.RECIPE);
        }

        nextStepBtn = (Button) getActivity().findViewById(R.id.button_next_step);
        prevStepBtn = (Button) getActivity().findViewById(R.id.button_prev_step);

        if(nextStepBtn != null && prevStepBtn != null) {
            nextStepBtn.setOnClickListener(this);
            prevStepBtn.setOnClickListener(this);
        }

        viewPager = (ViewPager)getActivity().findViewById(R.id.viewpager);
        if(sharedPrefs != null && sharedPrefs.contains(DetailActivity.CURRENT_STEP)) {
            step = sharedPrefs.getInt(DetailActivity.CURRENT_STEP, 0);
        }
        else {
            step = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        if (recipe != null) {
            LinearLayout stepsLayout = (LinearLayout) rootView.findViewById(R.id.steps_fragment_container);
            if(stepsLayout == null) return rootView;


            if (PaneUtils.isTwoPane(getActivity()) || PaneUtils.isLandscape(getActivity())) {
                addStepsViews(stepsLayout);
                showStepDetailsTwoPane(step);
            } else {
                if (step > 0 && !isStepsOnFocus()) {
                    setFocusOnStep();
                }
                showStepDetails(stepsLayout, step);
            }

            exoPlayerView = (SimpleExoPlayerView) getActivity().findViewById(R.id.player_view);
            exoPlayVideoUrl(step);
        }


        if(!PaneUtils.isTablet(getActivity())) {
            setUpSlidingPane();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        LinearLayout stepsLayout = (LinearLayout) getActivity().findViewById(R.id.steps_fragment_container);

        int id = v.getId();
        switch (id) {
            case R.id.button_next_step:
                if(!isStepsOnFocus()) {
                    setFocusOnStep();
                    return;
                }
                step++;
                showStepDetails(stepsLayout, step);
                releasePlayer();
                exoPlayVideoUrl(step);
                break;
            case R.id.button_prev_step:
                if(!isStepsOnFocus()) {
                    setFocusOnStep();
                    return;
                }
                step--;
                showStepDetails(stepsLayout, step);
                releasePlayer();
                exoPlayVideoUrl(step);
                break;
        }

        if(v instanceof Button &&
                (PaneUtils.isTwoPane(getActivity()) || PaneUtils.isLandscape(getActivity()))) {
            int tag = (int)v.getTag();
            step = tag;
            showStepDetailsTwoPane(tag);
            releasePlayer();
            exoPlayVideoUrl(step);
        }

        saveCurrentStep();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(exoPlayer != null) {
            if (exoPlayer.getPlaybackState() != PlaybackState.STATE_PLAYING) {
                exoPlayer.seekTo(0);
            }
            pausePlayer();
        }
    }

    private void showStepDetailsTwoPane(int stepIdx) {
        LinearLayout stepsDetailsContainer = (LinearLayout) getActivity().findViewById(R.id.steps_details_container);
        if(stepsDetailsContainer == null) return;
        TextView sDescView = new TextView(getContext());
        TextView lDescView = new TextView(getContext());
        removeLayoutViews(stepsDetailsContainer);

        List<Step> steps = recipe.getSteps();
        if(steps == null || steps.size() == 0)
            return;
        Step step = steps.get(stepIdx);
        String sDesc = step.getShortDescription();
        String lDesc = MyTextUtils.getTextFromHTML(step.getDescription());

        MyTextUtils.setTextStyle(getContext(), sDescView, DetailActivity.LARGE_APPEARANCE);
        sDescView.setText(sDesc);

        if(!PaneUtils.isTablet(getActivity()) && PaneUtils.isLandscape(getActivity())) {
            MyTextUtils.setTextStyle(getContext(), lDescView, DetailActivity.SMALL_APPEARANCE);
            int padding = (int)getResources().getDimension(R.dimen.description_video_padding);
            lDescView.setPadding(padding,padding,padding, padding);
        }
        else {
            MyTextUtils.setTextStyle(getContext(), lDescView, DetailActivity.MEDIUM_APPEARANCE);
        }
        lDescView.setText(lDesc);

        if(PaneUtils.isTwoPane(getActivity()) && PaneUtils.isTablet(getActivity())) {
            stepsDetailsContainer.addView(sDescView);
        }
        stepsDetailsContainer.addView(lDescView);
    }

    private void setFocusOnStep() {
        viewPager.setCurrentItem(1);
    }

    private boolean isStepsOnFocus() {
        return viewPager.getCurrentItem() == 1;
    }

    private void showStepDetails(LinearLayout stepsLayout, int stepIdx) {
        List<Step> steps = recipe.getSteps();
        if (steps == null || steps.size() == 0)
            return;
        int allSteps = steps.size();
        if (stepIdx < 0) {
            step = 0;
            return;
        }
        if (stepIdx > allSteps - 1) {
            step = allSteps - 1;
            return;
        }
        setButtonsActiveInactive();

        Step step = steps.get(stepIdx);
        String shortDescription = step.getShortDescription();
        String description = step.getDescription();

        removeLayoutViews(stepsLayout);

        TextView shortDescriptionView = new TextView(getContext());
        shortDescriptionView.setText(shortDescription);
        MyTextUtils.setTextStyle(getContext(), shortDescriptionView, DetailActivity.LARGE_APPEARANCE);

        TextView descriptionView = new TextView(getContext());
        descriptionView.setText(description);
        MyTextUtils.setTextStyle(getContext(), descriptionView, DetailActivity.MEDIUM_APPEARANCE);

        stepsLayout.addView(shortDescriptionView);
        stepsLayout.addView(descriptionView);
    }

    private void setButtonsActiveInactive() {
        if(nextStepBtn != null && prevStepBtn != null) {
            if (step <= 0) {
                nextStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
                prevStepBtn.setTextColor(getResources().getColor(R.color.button_inactive));
                nextStepBtn.setEnabled(true);
                prevStepBtn.setEnabled(false);
            } else if (recipe.getSteps() != null && step >= recipe.getSteps().size() - 1) {
                nextStepBtn.setTextColor(getResources().getColor(R.color.button_inactive));
                prevStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
                nextStepBtn.setEnabled(false);
                prevStepBtn.setEnabled(true);
            } else {
                nextStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
                prevStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
                nextStepBtn.setEnabled(true);
                prevStepBtn.setEnabled(true);
            }
        }
    }

    private void removeLayoutViews(LinearLayout layout) {
        if(layout != null && layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
    }

    private void addStepsViews(LinearLayout layout) {
        List<Step> stepsList = recipe.getSteps();
        if (stepsList == null || stepsList.size() == 0)
            return;

        for (Step step : stepsList) {
            Button stepButton = new Button(getContext());
            String shortDescription = step.getShortDescription();
            stepButton.setOnClickListener(this);
            stepButton.setTag(step.getStepId());
            stepButton.setText(shortDescription);
            int padding = (int)getResources().getDimension(R.dimen.step_button_padding);
            stepButton.setPadding(padding, 0, padding, 0);
            stepButton.setTextColor(getResources().getColor(R.color.main_txt_color));
            stepButton.setBackgroundResource(R.drawable.selector);
            stepButton.setSingleLine();
            stepButton.setEllipsize(android.text.TextUtils.TruncateAt.END);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources()
                    .getDimension(R.dimen.button_width), (int) getResources().getDimension(R.dimen.button_height));
            params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.standard_margin));
            params.gravity = Gravity.CENTER_HORIZONTAL;
            layout.addView(stepButton, params);
        }

        View emptyView = new View(getContext());
        emptyView.setMinimumHeight((int) getContext().getResources().getDimension(R.dimen.standard_margin));
        layout.addView(emptyView);
    }

    private void saveCurrentStep() {
        if(sharedPrefs != null) {
            sharedPrefs.edit().putInt(DetailActivity.CURRENT_STEP, step).commit();
        }
    }

    private void initializeExoPlayer(Uri uri) {
        if(exoPlayerView == null) return;
        if(exoPlayer != null) {
            exoPlayerView.setPlayer(exoPlayer);
            return;
        }

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        exoPlayerView.setPlayer(exoPlayer);

        dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), getResources().getString(R.string.app_name)), bandwidthMeter);

        MediaSource mediaSource = new ExtractorMediaSource(
                uri,
                dataSourceFactory,
                new DefaultExtractorsFactory(),
                null,
                null
        );

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    private void exoPlayVideoUrl(int step) {
        String url = "";
        String videoURL = recipe.getSteps().get(step).getVideoURL();
        String thumbnailURL = recipe.getSteps().get(step).getThumbnailURL();
        if(!TextUtils.isEmpty(videoURL)) url = videoURL;
        else if(thumbnailURL != null && !thumbnailURL.isEmpty()) url = thumbnailURL;

        if(!TextUtils.isEmpty(url)) {
            setExoPlayerVisibility(true);
            initializeExoPlayer(Uri.parse(url));
        }
        else {
            setExoPlayerVisibility(false);
        }
    }

    private void setExoPlayerVisibility(boolean isVisible) {
        ImageView emptyVideoImg = (ImageView) getActivity().findViewById(R.id.empty_video_img);
        if(emptyVideoImg == null) return;
        if(isVisible) {
            emptyVideoImg.setVisibility(View.INVISIBLE);
            exoPlayerView.setVisibility(View.VISIBLE);
        }
        else {
            emptyVideoImg.setVisibility(View.VISIBLE);
            exoPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    public static void releasePlayer() {
        if(exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    public static void pausePlayer() {
        /** Pause the Player when orientation happend **/
        if(exoPlayer != null)
            exoPlayer.setPlayWhenReady(false);
    }

    private void setUpSlidingPane() {
        slidingPaneLayout = (SlidingPaneLayout) getActivity().findViewById(R.id.sliding_pane_layout);
        boolean isPaneWasOpened = sharedPrefs.getBoolean(DetailActivity.IS_PANE_OPENED, false);
        if(PaneUtils.isLandscape(getActivity()) && !isPaneWasOpened) {
            slidingPaneLayout.openPane();
            new Handler().postDelayed(openDrawerRunnable(), DetailActivity.PANE_DELAY);
        }
    }

    private Runnable openDrawerRunnable() {
        return new Runnable() {

            @Override
            public void run() {
                slidingPaneLayout.closePane();
                sharedPrefs.edit().putBoolean(DetailActivity.IS_PANE_OPENED, true).commit();
            }
        };
    }
}
