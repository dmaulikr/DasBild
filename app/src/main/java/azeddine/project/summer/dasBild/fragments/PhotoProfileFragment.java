package azeddine.project.summer.dasBild.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import azeddine.project.summer.dasBild.R;
import azeddine.project.summer.dasBild.objectsUtils.Photo;

/**
 * Created by azeddine on 28/08/17.
 */

public class PhotoProfileFragment extends Fragment {
    public static final String TAG = "PhotoProfileFragment";
    private static final String SLIDE_STATE = "SLIDE_STATE";
    private static final float DRAG_ALPHA_OFFSET = 0.7f;

    private ImageView mPhotoImageView;
    private AppCompatTextView mPhotographerNameTextView;
    private AppCompatTextView mPhotoTitle;
    private AppCompatTextView mPhotoDate;
    private ImageView mPhotographerProfileImage;
    private ProgressBar mProgressBar;
    private ImageView mExpendIconImageView;
    private SlidingUpPanelLayout mSlidingPaneLayout;
    private View mDragView;

    public PhotoProfileFragment() {
    }


    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final AppCompatActivity activity = ((AppCompatActivity) getActivity());
        View view = inflater.inflate(R.layout.fragment_photo_profile, container, false);

        mPhotoImageView = view.findViewById(R.id.photo);
        mPhotographerProfileImage = view.findViewById(R.id.photographer_profile_image);
        mPhotoTitle = view.findViewById(R.id.photo_title);
        mPhotoDate = view.findViewById(R.id.photo_taken_date);
        mPhotographerNameTextView = view.findViewById(R.id.photographer_name);
        mProgressBar = view.findViewById(R.id.load_more_progress_bar);
        mSlidingPaneLayout = view.findViewById(R.id.sliding_layout);
        mExpendIconImageView = view.findViewById(R.id.expend_icon);
        mDragView = mSlidingPaneLayout.findViewById(R.id.dragView);

        mSlidingPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                mExpendIconImageView.setRotation(v * 180);
                view.setAlpha(DRAG_ALPHA_OFFSET + v);
            }

            @Override
            public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState, SlidingUpPanelLayout.PanelState panelState1) {

            }
        });

        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        final ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
            actionBar.setShowHideAnimationEnabled(true);
        }


        mPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSlidingPaneLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mSlidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {
                    if (actionBar != null && actionBar.isShowing()) {
                        mSlidingPaneLayout.setTouchEnabled(false);
                        mDragView.setSoundEffectsEnabled(false);
                        hideForegroundLayout(actionBar, mDragView);
                    } else {
                        mSlidingPaneLayout.setTouchEnabled(true);
                        mDragView.setSoundEffectsEnabled(true);
                        showForegroundLayout(actionBar, mDragView);
                    }
                }
            }
        });
        return view;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Photo photo = (Photo) getArguments().getSerializable("Photo");
        if (photo != null) {

            Glide.with(this)
                    .load(photo.getUnCroppedPhotoUrl())
                    .apply(new RequestOptions().error(R.drawable.ic_terrain_))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(mPhotoImageView);

            Glide.with(this)
                    .load(photo.getPhotographerImageUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(mPhotographerProfileImage);

            String text = photo.getPhotographerUsername();
            if (text != null) mPhotographerNameTextView.setText(text);
            text = photo.getTitle();
            if (text != null) mPhotoTitle.setText(text);
            text = photo.getDateString();
            if (text != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    Date date = simpleDateFormat.parse(text);
                    simpleDateFormat.applyPattern("MMMMMMMM yyyy");
                    mPhotoDate.setText(simpleDateFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                mPhotoDate.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable parcelable = savedInstanceState.getParcelable(SLIDE_STATE);
            if (parcelable != null) mSlidingPaneLayout.onRestoreInstanceState(parcelable);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(android.R.color.black));
        }
        if (mSlidingPaneLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
            startSidingUpPanelEnterTransition(mDragView, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mPhotographerProfileImage.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPhotographerProfileImage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SLIDE_STATE, mSlidingPaneLayout.onSaveInstanceState());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        }
    }


    private void hideForegroundLayout(ActionBar actionBar, View panel) {
        actionBar.hide();
        Animation leaveAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down_fade_out);
        leaveAnimation.setDuration(250);
        leaveAnimation.setInterpolator(AnimationUtils.loadInterpolator(
                getContext(),
                android.R.interpolator.linear
        ));
        panel.startAnimation(leaveAnimation);
    }

    private void showForegroundLayout(ActionBar actionBar, View panel) {
        actionBar.show();
        Animation enterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_fade_in);
        enterAnimation.setDuration(250);
        enterAnimation.setInterpolator(AnimationUtils.loadInterpolator(
                getContext(),
                android.R.interpolator.linear
        ));
        panel.startAnimation(enterAnimation);
    }

    private void startSidingUpPanelEnterTransition(View panel, Animation.AnimationListener listener) {
        Animation enterAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down_to_postion);
        enterAnimation.setDuration(350);
        enterAnimation.setInterpolator(AnimationUtils.loadInterpolator(
                getContext(),
                android.R.interpolator.accelerate_decelerate
        ));
        enterAnimation.setAnimationListener(listener);
        panel.startAnimation(enterAnimation);
    }

    public boolean isSlidingPanelOpen() {
        return mSlidingPaneLayout.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED;
    }

    public void setSlidingPanelState(SlidingUpPanelLayout.PanelState panelState) {
        mSlidingPaneLayout.setPanelState(panelState);
    }
}
