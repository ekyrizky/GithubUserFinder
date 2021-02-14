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
import com.ekyrizky.githubezfinder.databinding.FragmentFollowingBinding
import com.ekyrizky.githubezfinder.view.UserDetailActivity
import com.ekyrizky.githubezfinder.viewmodel.FollowViewModel


class FollowingFragment : Fragment() {
    private lateinit var followBinding: FragmentFollowingBinding
    private lateinit var adapter: FollowAdapter
    private lateinit var followingViewModel: FollowViewModel
    private var listFollow: ArrayList<UserEntity> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        followBinding = FragmentFollowingBinding.inflate(layoutInflater)
        return followBinding.root.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowAdapter(listFollow)

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(FollowViewModel::class.java)

        val user = requireActivity().intent.getParcelableExtra<UserEntity>(UserDetailActivity.EXTRA_USER) as UserEntity

        showRecyclerView()
        setUserData(user)
        getUserFollowing(adapter)
    }

    private fun setUserData(user: UserEntity) {
        followingViewModel.setUserFollowing(requireActivity().applicationContext, user.username)
        showLoading(true)
    }

    private fun getUserFollowing(adapter: FollowAdapter) {
        followingViewModel.getUser().observe(viewLifecycleOwner, { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            followBinding.pbFollowing.visibility = View.VISIBLE
        } else {
            followBinding.pbFollowing.visibility = View.GONE
        }
    }

    private fun showRecyclerView() {
        followBinding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        followBinding.rvFollowing.setHasFixedSize(true)
        followBinding.rvFollowing.adapter = adapter
    }

}