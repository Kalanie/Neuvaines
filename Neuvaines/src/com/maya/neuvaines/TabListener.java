package com.maya.neuvaines;


import com.maya.neuvaines.R;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar;

/**
 * Ecouteur d'event sur l'action bar.
 * 
 * @author maya
 * @param <T>
 * 
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {

	Fragment fragment;
	private Fragment mFragment;
	private final Activity mActivity;
	private final String mTag;
	private final Class<T> mClass;

	/**
	 * Constructor used each time a new tab is created.
	 * 
	 * @param activity
	 *            The host Activity, used to instantiate the fragment
	 * @param tag
	 *            The identifier tag for the fragment
	 * @param clz
	 *            The fragment's Class, used to instantiate the fragment
	 */
	public TabListener(Activity activity, String tag, Class<T> clz,
			Fragment fragment) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
		// Objet fragement dont se charge cette instance.
		this.fragment = fragment;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Lorsqu'un tab est choisi on effectue un fragment replace.
		ft.replace(R.id.fragment_container, fragment);
		/*
		// previous Fragment management
		Fragment prevFragment;
		FragmentManager fm = mActivity.getFragmentManager();
		prevFragment = fm.findFragmentByTag(mTag);
		if (prevFragment != null) {
			mFragment = prevFragment;
		} // \previous Fragment management

		// Check if the fragment is already initialized
		if (mFragment == null) {
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			ft.add(android.R.id.content, mFragment, mTag);
		} else {
			// If it exists, simply attach it in order to show it
			ft.attach(mFragment);
		}
		*/
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// Remove.
		ft.remove(fragment);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}