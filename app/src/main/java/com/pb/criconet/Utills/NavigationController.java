package com.pb.criconet.Utills;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.pb.criconet.R;
import com.pb.criconet.activity.MainActivity;
import com.pb.criconet.fragments.CoachFragment;
import com.pb.criconet.fragments.FeedsPhotos;
import com.pb.criconet.fragments.Followers;
import com.pb.criconet.fragments.Following;
import com.pb.criconet.fragments.FollowingRequest;
import com.pb.criconet.fragments.FragmentAvility;
import com.pb.criconet.fragments.FragmentEditProfile;
import com.pb.criconet.fragments.FragmentExperienceSetting;
import com.pb.criconet.fragments.FragmentPager;
import com.pb.criconet.fragments.HomeFragment;
import com.pb.criconet.fragments.LiveMatches;
import com.pb.criconet.fragments.RecMatches;
import com.pb.criconet.fragments.TimePreode;
import com.pb.criconet.fragments.TimePreodee;
import com.pb.criconet.fragments.WebViewGameFragment;
import com.pb.criconet.models.TimeSlot;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NavigationController {

    protected final int containerId;
    protected final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity activity) {
        this.containerId = R.id.container;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    public NavigationController(FragmentActivity activity) {
        this.containerId = R.id.container;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    public void popOneStepBack() {
        fragmentManager.popBackStack();
    }

    public void popBackAll() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Dismiss all DialogFragments added to given FragmentManager and child fragments
     */
    public void dismissAllDialogs() {
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments == null)
            return;

        for (Fragment fragment : fragments) {
            if (fragment instanceof DialogFragment) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    public void navigateToHomeFragment() {
        popBackAll();
        fragmentManager.beginTransaction()
                .replace(containerId, HomeFragment.newInstance())
                .commit();
    }

    public void navigatoCoachFragment() {
        fragmentManager.beginTransaction()
                .replace(containerId, CoachFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }


    public void navigatoLiveMatchesFragment() {
        fragmentManager.beginTransaction()
                .replace(containerId, LiveMatches.newInstance())
                .addToBackStack(null)
                .commit();
    }
    public void navigatoRecMatchesFragment() {
        MainActivity.bottomNavigation.setMenuItemEnabled(3,true);
        fragmentManager.beginTransaction()
                .replace(containerId, RecMatches.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void navigatoMenuFragment(boolean isValue) {
        fragmentManager.beginTransaction()
                .replace(containerId, FragmentPager.newInstance(isValue))
                .addToBackStack(null)
                .commit();
    }


    public void navigatoBookingFragment() {
//        fragmentManager.beginTransaction()
//                .replace(containerId, BookingHistoryFragment.newInstance())
//                .addToBackStack(null)
//                .commit();
    }
    public void navigatoFeedPhotoFragment() {
        fragmentManager.beginTransaction()
                .replace(containerId, FeedsPhotos.newInstance())
                .commit();
    }
    public void navigatoAvilityFragment() {
        fragmentManager.beginTransaction()
                .replace(containerId, FragmentAvility.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void navigatoEditProfileFragment() {
        fragmentManager.beginTransaction()
                .replace(containerId, FragmentEditProfile.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void navigatoExperienceSettingFragment() {
        fragmentManager.beginTransaction()
                .replace(containerId, FragmentExperienceSetting.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void navigatoWebViewGameFragment() {
        fragmentManager.beginTransaction()
                .replace(containerId, WebViewGameFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
    public void navigatoFollowers() {
        fragmentManager.beginTransaction()
                .replace(containerId, Followers.newInstance())
                .commit();
    }
    public void navigatoFollowing() {
        fragmentManager.beginTransaction()
                .replace(containerId, Following.newInstance())
                .addToBackStack(null)
                .commit();
    }
    public void navigatoFollowingRequest() {
        fragmentManager.beginTransaction()
                .replace(containerId, FollowingRequest.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void showTimePreode(TimeSlot mmodelArrayList) {
        TimePreode fragment = new TimePreode(mmodelArrayList);
        fragment.setCancelable(true);
        fragment.show(fragmentManager, TimePreode.class.getName());
    }

    public void showTimePreodee(TimeSlot mmodelArrayList) {
        TimePreodee fragment = new TimePreodee(mmodelArrayList);
        fragment.setCancelable(true);
        fragment.show(fragmentManager, TimePreodee.class.getName());
    }
}