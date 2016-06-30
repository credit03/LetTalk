package com.lettalk.gy.ui.euclidui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.ui.view.GifImageView;
import com.lettalk.gy.util.ViewUtil;
import com.nhaarman.listviewanimations.appearance.ViewAnimator;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Oleksii Shliama on 1/27/15.
 */
public abstract class EuclidActivity extends Activity {

    private static final int REVEAL_ANIMATION_DURATION = 1000;
    private static final int MAX_DELAY_SHOW_DETAILS_ANIMATION = 500;
    private static final int ANIMATION_DURATION_SHOW_PROFILE_DETAILS = 500;
    private static final int STEP_DELAY_HIDE_DETAILS_ANIMATION = 80;
    private static final int ANIMATION_DURATION_CLOSE_PROFILE_DETAILS = 500;
    private static final int ANIMATION_DURATION_SHOW_PROFILE_BUTTON = 300;
    private static final int CIRCLE_RADIUS_DP = 50;


    protected static final String NICKNAME = "nickname";
    protected static final String USERNAME = "username";
    protected static final String SEX = "sex";
    protected static final String TM = "tm";
    protected static final String PERSONALIZED = "Personalized";
    protected static final String ISCURRENTID = "currentId";
    protected static final String ISFRIEND = "isfriend";
    private static final String TAG = "EuclidActivity";

    protected RelativeLayout mWrapper;
    protected ListView mListView;
    protected RelativeLayout mToolbarProfile;
    protected LinearLayout mProfileDetails;
    protected TextView mTextViewProfileName;
    protected TextView mTextViewProfileDescription;
    protected View mButtonProfile;

    public static ShapeDrawable sOverlayShape;
    protected static int sScreenWidth;
    protected static int sScreenHeight;
    protected static int sProfileImageHeight;

    private SwingLeftInAnimationAdapter mListViewAnimationAdapter;
    private ViewAnimator mListViewAnimator;

    protected View mOverlayListItemView;

    private EuclidState mState = EuclidState.Closed;

    private float mInitialProfileButtonX;

    private AnimatorSet mOpenProfileAnimatorSet;
    private AnimatorSet mCloseProfileAnimatorSet;
    private Animation mProfileButtonShowAnimation;

    protected TextView tv_sex;
    protected TextView tv_userid;
    protected TextView tv_tm;

    protected LinearLayout ll_nickname;
    protected LinearLayout ll_sex;
    protected LinearLayout ll_tm;
    protected LinearLayout ll_pre;

    protected ProgressBar pb_prohro;
    protected GifImageView show_load;

    protected TextView tv_tool_title;

    protected HashMap<String, String> userInfoMap = new HashMap<>();

    protected void setUserInfoMap(HashMap<String, String> map) {
        if (map != null && map.size() > 0) {
            userInfoMap.clear();
            userInfoMap.putAll(map);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_euclid);

        findById();
        mButtonProfile.post(new Runnable() {
            @Override
            public void run() {
                mInitialProfileButtonX = mButtonProfile.getX();
            }
        });
        findViewById(R.id.toolbar_profile_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == EuclidState.Closed) {
                    EuclidActivity.this.finish();
                } else {
                    animateCloseProfileDetails();
                }
            }
        });

        sScreenWidth = getResources().getDisplayMetrics().widthPixels;
        sScreenHeight = getResources().getDisplayMetrics().heightPixels;
        sProfileImageHeight = getResources().getDimensionPixelSize(R.dimen.height_profile_image);
        sOverlayShape = buildAvatarCircleOverlay();

        initList();
    }

    private void findById() {
        mWrapper = (RelativeLayout) findViewById(R.id.wrapper);
        mListView = (ListView) findViewById(R.id.list_view);
        mToolbarProfile = (RelativeLayout) findViewById(R.id.toolbar_profile);
        mProfileDetails = (LinearLayout) findViewById(R.id.wrapper_profile_details);
        mTextViewProfileName = (TextView) findViewById(R.id.text_view_profile_name);
        mTextViewProfileDescription = (TextView) findViewById(R.id.text_view_profile_description);
        tv_tool_title = (TextView) findViewById(R.id.tv_tool_title);
        mButtonProfile = findViewById(R.id.button_profile);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_tm = (TextView) findViewById(R.id.tv_tm);
        tv_userid = (TextView) findViewById(R.id.tv_userid);
        ll_nickname = (LinearLayout) findViewById(R.id.ll_nickname);
        ll_sex = (LinearLayout) findViewById(R.id.ll_sex);
        ll_tm = (LinearLayout) findViewById(R.id.ll_tm);
        ll_pre = (LinearLayout) findViewById(R.id.ll_pre);
        pb_prohro = (ProgressBar) findViewById(R.id.pb_prohor);
        show_load = (GifImageView) findViewById(R.id.loadshow);
        pb_prohro.setMax(100);
    }


    protected abstract void initData();

    private void initList() {
        mListViewAnimationAdapter = new SwingLeftInAnimationAdapter(getAdapter());
        mListViewAnimationAdapter.setAbsListView(mListView);
        mListViewAnimator = mListViewAnimationAdapter.getViewAnimator();
        if (mListViewAnimator != null) {
            mListViewAnimator.setAnimationDurationMillis(getAnimationDurationCloseProfileDetails());
            mListViewAnimator.disableAnimations();
        }
        mListView.setAdapter(mListViewAnimationAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mState = EuclidState.Opening;
                showProfileDetails((Map<String, Object>) parent.getItemAtPosition(position), view);
            }
        });
    }


    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }

    /**
     * This method counts delay before profile toolbar and profile details start their transition
     * animations, depending on clicked list item on-screen position.
     *
     * @param item - data from adapter, that will be set into overlay view.
     * @param view - clicked view.
     */
    private void showProfileDetails(Map<String, Object> item, final View view) {
        mListView.setEnabled(false);

        int profileDetailsAnimationDelay = getMaxDelayShowDetailsAnimation() * Math.abs(view.getTop())
                / sScreenWidth;

        addOverlayListItem(item, view);
        startRevealAnimation(profileDetailsAnimationDelay);
        animateOpenProfileDetails(profileDetailsAnimationDelay);
    }

    /**
     * This method inflates a clone of clicked view directly above it. Sets data into it.
     *
     * @param item - data from adapter, that will be set into overlay view.
     * @param view - clicked view.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void addOverlayListItem(Map<String, Object> item, View view) {
        if (mOverlayListItemView == null) {
            mOverlayListItemView = getLayoutInflater().inflate(R.layout.overlay_list_item, mWrapper, false);
        } else {
            mWrapper.removeView(mOverlayListItemView);
        }

        mOverlayListItemView.findViewById(R.id.view_avatar_overlay).setBackground(sOverlayShape);

        ViewUtil.setImageView((String) item.get(EuclidListAdapter.KEY_AVATAR), R.drawable.temp_bg, R.drawable.load, (ImageView) mOverlayListItemView.findViewById(R.id.image_view_reveal_avatar));
        ViewUtil.setImageView((String) item.get(EuclidListAdapter.KEY_AVATAR), R.drawable.temp_bg, R.drawable.load, (ImageView) mOverlayListItemView.findViewById(R.id.image_view_avatar));

      /*  Picasso.with(EuclidActivity.this).load((String) item.get(EuclidListAdapter.KEY_AVATAR))
                .resize(sScreenWidth, sProfileImageHeight).centerCrop()
                .placeholder(R.color.blue)
                .into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_reveal_avatar));

        Picasso.with(EuclidActivity.this).load((String) item.get(EuclidListAdapter.KEY_AVATAR))
                .resize(sScreenWidth, sProfileImageHeight).centerCrop()
                .placeholder(R.color.blue)
                .into((ImageView) mOverlayListItemView.findViewById(R.id.image_view_avatar));
*/
        ((TextView) mOverlayListItemView.findViewById(R.id.text_view_name)).setText((String) item.get(EuclidListAdapter.KEY_NAME));
        ((TextView) mOverlayListItemView.findViewById(R.id.text_view_description)).setText((String) item.get(EuclidListAdapter.KEY_DESCRIPTION_SHORT));
        setProfileDetailsInfo(item);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = view.getTop() + mToolbarProfile.getHeight();
        params.bottomMargin = -(view.getBottom() - mListView.getHeight());
        mWrapper.addView(mOverlayListItemView, params);
        mToolbarProfile.bringToFront();
    }

    /**
     * This method sets data of the clicked list item to profile details view.
     *
     * @param item - data from adapter, that will be set into overlay view.
     */
    private void setProfileDetailsInfo(Map<String, Object> item) {

        mTextViewProfileName.setText((String) item.get(EuclidListAdapter.KEY_NAME));


        mTextViewProfileDescription.setText((String) item.get(EuclidListAdapter.KEY_DESCRIPTION_FULL));
    }

    /**
     * This method starts circle reveal animation on list item overlay view, to show full-sized
     * avatar image underneath it. And starts transition animation to position clicked list item
     * under the toolbar.
     *
     * @param profileDetailsAnimationDelay - delay before profile toolbar and profile details start their transition
     *                                     animations.
     */
    private void startRevealAnimation(final int profileDetailsAnimationDelay) {
        mOverlayListItemView.post(new Runnable() {
            @Override
            public void run() {
                getAvatarRevealAnimator().start();
                getAvatarShowAnimator(profileDetailsAnimationDelay).start();
            }
        });
    }

    /**
     * This method creates and setups circle reveal animation on list item overlay view.
     *
     * @return - animator object that starts circle reveal animation.
     */
    private SupportAnimator getAvatarRevealAnimator() {
        final LinearLayout mWrapperListItemReveal = (LinearLayout) mOverlayListItemView.findViewById(R.id.wrapper_list_item_reveal);

        int finalRadius = Math.max(mOverlayListItemView.getWidth(), mOverlayListItemView.getHeight());

        final SupportAnimator mRevealAnimator = ViewAnimationUtils.createCircularReveal(
                mWrapperListItemReveal,
                sScreenWidth / 2,
                sProfileImageHeight / 2,
                dpToPx(getCircleRadiusDp() * 2),
                finalRadius);
        mRevealAnimator.setDuration(getRevealAnimationDuration());
        mRevealAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                mWrapperListItemReveal.setVisibility(View.VISIBLE);
                mOverlayListItemView.setX(0);
            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });
        return mRevealAnimator;
    }

    /**
     * This method creates transition animation to move clicked list item under the toolbar.
     *
     * @param profileDetailsAnimationDelay - delay before profile toolbar and profile details start their transition
     *                                     animations.
     * @return - animator object that starts transition animation.
     */
    private Animator getAvatarShowAnimator(int profileDetailsAnimationDelay) {
        final Animator mAvatarShowAnimator = ObjectAnimator.ofFloat(mOverlayListItemView, View.Y, mOverlayListItemView.getTop(), mToolbarProfile.getBottom());
        mAvatarShowAnimator.setDuration(profileDetailsAnimationDelay + getAnimationDurationShowProfileDetails());
        mAvatarShowAnimator.setInterpolator(new DecelerateInterpolator());
        return mAvatarShowAnimator;
    }

    /**
     * This method starts set of transition animations, which show profile toolbar and profile
     * details views, right after the passed delay.
     *
     * @param profileDetailsAnimationDelay - delay before profile toolbar and profile details
     *                                     start their transition animations.
     */
    private void animateOpenProfileDetails(int profileDetailsAnimationDelay) {
        createOpenProfileButtonAnimation();
        getOpenProfileAnimatorSet(profileDetailsAnimationDelay).start();
    }


    private void animateCloseProfileDetails(int por) {

    }

    /**
     * This method creates if needed the set of transition animations, which show profile toolbar and profile
     * details views, right after the passed delay.
     *
     * @param profileDetailsAnimationDelay- delay before profile toolbar and profile details
     *                                      start their transition animations.
     * @return - animator set that starts transition animations.
     */
    private AnimatorSet getOpenProfileAnimatorSet(int profileDetailsAnimationDelay) {
        if (mOpenProfileAnimatorSet == null) {
            List<Animator> profileAnimators = new ArrayList<>();
            profileAnimators.add(getOpenProfileToolbarAnimator());
            profileAnimators.add(getOpenProfileDetailsAnimator());

            mOpenProfileAnimatorSet = new AnimatorSet();
            mOpenProfileAnimatorSet.playTogether(profileAnimators);
            mOpenProfileAnimatorSet.setDuration(getAnimationDurationShowProfileDetails());
        }
        mOpenProfileAnimatorSet.setStartDelay(profileDetailsAnimationDelay);
        mOpenProfileAnimatorSet.setInterpolator(new DecelerateInterpolator());
        return mOpenProfileAnimatorSet;
    }

    /**
     * This method, if needed, creates and setups animation of scaling button from 0 to 1.
     */
    private void createOpenProfileButtonAnimation() {
        if (mProfileButtonShowAnimation == null) {
            mProfileButtonShowAnimation = AnimationUtils.loadAnimation(this, R.anim.profile_button_scale);
            mProfileButtonShowAnimation.setDuration(getAnimationDurationShowProfileButton());
            mProfileButtonShowAnimation.setInterpolator(new AccelerateInterpolator());
            mProfileButtonShowAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                    if (userInfoMap.get(ISCURRENTID).equals("TRUE"))
                        mButtonProfile.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    /**
     * This method creates and setups animator which shows profile toolbar.
     *
     * @return - animator object.
     */
    private Animator getOpenProfileToolbarAnimator() {
        Animator mOpenProfileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.Y, -mToolbarProfile.getHeight(), 0);
        mOpenProfileToolbarAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mToolbarProfile.setX(0);
                mToolbarProfile.bringToFront();
                mToolbarProfile.setVisibility(View.VISIBLE);
                mProfileDetails.setX(0);
                mProfileDetails.bringToFront();
                mProfileDetails.setVisibility(View.VISIBLE);

                if (userInfoMap.get(ISCURRENTID).equals("TRUE")) {
                    mButtonProfile.setX(mInitialProfileButtonX);
                    mButtonProfile.bringToFront();
                }

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (userInfoMap.get(ISCURRENTID).equals("TRUE")) {
                    mButtonProfile.startAnimation(mProfileButtonShowAnimation);
                    //  mState = EuclidState.Opened;
                }
                mState = EuclidState.Opened;

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return mOpenProfileToolbarAnimator;
    }

    /**
     * This method creates animator which shows profile details.
     *
     * @return - animator object.
     */
    private Animator getOpenProfileDetailsAnimator() {
        Animator mOpenProfileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.Y,
                getResources().getDisplayMetrics().heightPixels,
                getResources().getDimensionPixelSize(R.dimen.height_profile_picture_with_toolbar));
        return mOpenProfileDetailsAnimator;
    }

    /**
     * This method starts set of transition animations, which hides profile toolbar, profile avatar
     * and profile details views.
     */
    private void animateCloseProfileDetails() {
        mState = EuclidState.Closed;
        getCloseProfileAnimatorSet().start();
    }

    /**
     * This method creates if needed the set of transition animations, which hides profile toolbar, profile avatar
     * and profile details views. Also it calls notifyDataSetChanged() on the ListView's adapter,
     * so it starts slide-in left animation on list items.
     *
     * @return - animator set that starts transition animations.
     */
    private AnimatorSet getCloseProfileAnimatorSet() {
        if (mCloseProfileAnimatorSet == null) {
           /* Animator profileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.X,
                    0, mToolbarProfile.getWidth());*/
            List<Animator> profileAnimators = new ArrayList<>();
            Animator profilePhotoAnimator = ObjectAnimator.ofFloat(mOverlayListItemView, View.X,
                    0, mOverlayListItemView.getWidth());
            profilePhotoAnimator.setStartDelay(getStepDelayHideDetailsAnimation());


            if (userInfoMap.get(ISCURRENTID).equals("TRUE")) {

                Animator profileButtonAnimator = ObjectAnimator.ofFloat(mButtonProfile, View.X,
                        mInitialProfileButtonX, mOverlayListItemView.getWidth() + mInitialProfileButtonX);
                profileButtonAnimator.setStartDelay(getStepDelayHideDetailsAnimation() * 2);
                profileAnimators.add(profileButtonAnimator);
            }

            Animator profileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.X,
                    0, mToolbarProfile.getWidth());
            profileDetailsAnimator.setStartDelay(getStepDelayHideDetailsAnimation() * 2);


            //  profileAnimators.add(profileToolbarAnimator);
            profileAnimators.add(profilePhotoAnimator);

            profileAnimators.add(profileDetailsAnimator);

            mCloseProfileAnimatorSet = new AnimatorSet();
            mCloseProfileAnimatorSet.playTogether(profileAnimators);
            mCloseProfileAnimatorSet.setDuration(getAnimationDurationCloseProfileDetails());
            mCloseProfileAnimatorSet.setInterpolator(new AccelerateInterpolator());
            mCloseProfileAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (mListViewAnimator != null) {
                        mListViewAnimator.reset();
                        mListViewAnimationAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mToolbarProfile.setVisibility(View.VISIBLE);

                    if (userInfoMap.get(ISCURRENTID).equals("TRUE"))
                        mButtonProfile.setVisibility(View.VISIBLE);

                    mProfileDetails.setVisibility(View.VISIBLE);

                    mListView.setEnabled(true);
                    mListViewAnimator.disableAnimations();
                    mState = EuclidState.Closed;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return mCloseProfileAnimatorSet;
    }

    /**
     * This method creates a view with empty/transparent circle in it's center. This view is used
     * to cover the profile avatar.
     *
     * @return - ShapeDrawable object.
     */
    private ShapeDrawable buildAvatarCircleOverlay() {
        int radius = 666;
        ShapeDrawable overlay = new ShapeDrawable(new RoundRectShape(null,
                new RectF(
                        sScreenWidth / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sProfileImageHeight / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sScreenWidth / 2 - dpToPx(getCircleRadiusDp() * 2),
                        sProfileImageHeight / 2 - dpToPx(getCircleRadiusDp() * 2)),
                new float[]{radius, radius, radius, radius, radius, radius, radius, radius}));
        overlay.getPaint().setColor(getResources().getColor(R.color.gray));

        return overlay;
    }

    public int dpToPx(int dp) {
        return Math.round((float) dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {

        if (getState() == EuclidState.Opened) {
            animateCloseProfileDetails();

        } else if (getState() == EuclidState.Closed) {
            super.onBackPressed();


        }
    }

    protected abstract void setEuClidItemButtonListener();

    /**
     * To use EuclidActivity class, at least this method must be implemented, with your own data.
     *
     * @return - adapter with data. Check {@link EuclidListAdapter}
     */
    protected abstract BaseAdapter getAdapter();

    /**
     * Returns current profile details state.
     *
     * @return - {@link EuclidState}
     */
    public EuclidState getState() {
        return mState;
    }

    /**
     * Duration of circle reveal animation.
     *
     * @return - duration in milliseconds.
     */
    protected int getRevealAnimationDuration() {
        return REVEAL_ANIMATION_DURATION;
    }

    /**
     * Maximum delay between list item click and start of profile toolbar and profile details
     * transition animations. If clicked list item was positioned right at the top - we start
     * profile toolbar and profile details transition animations immediately, otherwise increase
     * start delay up to this value.
     *
     * @return - duration in milliseconds.
     */
    protected int getMaxDelayShowDetailsAnimation() {
        return MAX_DELAY_SHOW_DETAILS_ANIMATION;
    }

    /**
     * Duration of profile toolbar and profile details transition animations.
     *
     * @return - duration in milliseconds.
     */
    protected int getAnimationDurationShowProfileDetails() {
        return ANIMATION_DURATION_SHOW_PROFILE_DETAILS;
    }

    /**
     * Duration of delay between profile toolbar, profile avatar and profile details close animations.
     *
     * @return - duration in milliseconds.
     */
    protected int getStepDelayHideDetailsAnimation() {
        return STEP_DELAY_HIDE_DETAILS_ANIMATION;
    }

    /**
     * Duration of profile details close animation.
     *
     * @return - duration in milliseconds.
     */
    protected int getAnimationDurationCloseProfileDetails() {
        return ANIMATION_DURATION_CLOSE_PROFILE_DETAILS;
    }

    protected int getAnimationDurationShowProfileButton() {
        return ANIMATION_DURATION_SHOW_PROFILE_BUTTON;
    }

    /**
     * Radius of empty circle inside the avatar overlay.
     *
     * @return - size dp.
     */
    protected int getCircleRadiusDp() {
        return CIRCLE_RADIUS_DP;
    }

    /**
     * 启动指定Activity
     *
     * @param target
     * @param bundle
     */
    public void startActivity(Class<? extends Activity> target, Bundle bundle, boolean fisish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(this.getPackageName(), bundle);
        startActivity(intent);
        if (fisish)
            this.finish();
    }
}
