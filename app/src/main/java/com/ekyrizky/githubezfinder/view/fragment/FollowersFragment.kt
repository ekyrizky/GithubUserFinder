package com.ekyrizky.githubezfinder.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekyrizky.githubezfinder.adapter.FollowAdapter
import com.ekyrizky.githubezfinder.data.UserEntity
import com.ekyrizky.githubezfinder.databinding.FragmentFollowersBinding
import com.ekyrizky.githubezfinder.view.UserDetailActivity
import com.ekyrizky.githubezfinder.viewmodel.FollowViewModel

class FollowersFragment : Fragment() {
    private lateinit var followBinding: FragmentFollowersBinding
    private lateinit var adapter: FollowAdapter
    private lateinit var followersViewModel: FollowViewModel
    private var listFollow: ArrayList<UserEntity> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        followBinding = FragmentFollowersBinding.inflate(layoutInflater)
        return followBinding.root.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowAdapter(listFollow)

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(FollowViewModel::class.java)
        val user = requireActivity().intent.getParcelableExtra<UserEntity>(UserDetailActivity.EXTRA_USER) as UserEntity

        showRecyclerView()
        setUserData(user)
        getUserFollowing(adapter)
    }

    private fun setUserData(user: UserEntity) {
        followersViewModel.setUserFollowers(requireActivity().applicationContext, user.username)
        showLoading(true)
    }

    private fun getUserFollowing(adapter: FollowAdapter) {
        followersViewModel.getUser().observe(viewLifecycleOwner, { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            followBinding.pbFollowers.visibility = View.VISIBLE
        } else {
            followBinding.pbFollowers.visibility = View.GONE
        }
    }

    private fun showRecyclerView() {
        followBinding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        followBinding.rvFollowers.setHasFixedSize(true)
        followBinding.rvFollowers.adapter = adapter
    }
}