package com.ekyrizky.githubezfinder.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ekyrizky.githubezfinder.R
import com.ekyrizky.githubezfinder.data.UserEntity
import com.ekyrizky.githubezfinder.databinding.ItemRowFollowBinding
import com.ekyrizky.githubezfinder.view.UserDetailActivity

class FollowAdapter(private val listFollow: ArrayList<UserEntity>) :
    RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

    fun setData(items: ArrayList<UserEntity>) {
        listFollow.clear()
        listFollow.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemRowFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .placeholder(R.drawable.profile_black_96dp)
                    .error(R.drawable.profile_black_96dp)
                    .into(imgAvatar)
                tvName.text = user.name
                tvUsername.text = user.username

                binding.tvName.visibility = if (user.name == "null") View.GONE else View.VISIBLE
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(
            ItemRowFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userData = listFollow[position]

        holder.bind(userData)
        holder.itemView.setOnClickListener{
            val moveDetail = Intent(it.context, UserDetailActivity::class.java)
            moveDetail.putExtra(UserDetailActivity.EXTRA_USER, userData)
            it.context.startActivity(moveDetail)
        }
    }

    override fun getItemCount(): Int = listFollow.size
}