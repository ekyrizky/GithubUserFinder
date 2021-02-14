package com.ekyrizky.githubezfinder.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ekyrizky.githubezfinder.R
import com.ekyrizky.githubezfinder.data.UserEntity
import com.ekyrizky.githubezfinder.databinding.ItemRowUserBinding
import com.ekyrizky.githubezfinder.view.UserDetailActivity

class UserAdapter(private val listUser: ArrayList<UserEntity>) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    fun setData(items: ArrayList<UserEntity>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val userRowBinding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(userRowBinding.root) {
        fun bind (user: UserEntity) {
            with(userRowBinding) {
                Glide.with(itemView.context)
                        .load(user.avatar)
                        .placeholder(R.drawable.profile_black_48dp)
                        .error(R.drawable.profile_black_48dp)
                        .into(imgAvatar)
                tvName.text = user.name
                tvUsername.text = user.username
                tvCompany.text = user.company
                tvLocation.text = user.location

                userRowBinding.tvName.visibility = if (user.name == "null") View.GONE else View.VISIBLE
                userRowBinding.tvCompany.visibility = if (user.company == "null") View.GONE else View.VISIBLE
                userRowBinding.tvLocation.visibility = if (user.location == "null") View.GONE else View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
            ListViewHolder(
                    ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userData = listUser[position]

        holder.bind(userData)
        holder.itemView.setOnClickListener{
            val moveDetail = Intent(it.context, UserDetailActivity::class.java)
            moveDetail.putExtra(UserDetailActivity.EXTRA_USER, userData)
            it.context.startActivity(moveDetail)
        }
    }

    override fun getItemCount(): Int = listUser.size
}