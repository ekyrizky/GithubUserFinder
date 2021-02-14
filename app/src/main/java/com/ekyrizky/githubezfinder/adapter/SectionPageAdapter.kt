package com.ekyrizky.githubezfinder.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ekyrizky.githubezfinder.R
import com.ekyrizky.githubezfinder.view.fragment.FollowersFragment
import com.ekyrizky.githubezfinder.view.fragment.FollowingFragment

class SectionPageAdapter (private val mContext: Context, fm: FragmentManager):
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val tabTitle = arrayOf(
            R.string.tabFollowing,
            R.string.tabFollowers
    )

    override fun getCount(): Int = tabTitle.size

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = FollowingFragment()
            1 -> fragment = FollowersFragment()
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence = mContext.resources.getString(tabTitle[position])
}