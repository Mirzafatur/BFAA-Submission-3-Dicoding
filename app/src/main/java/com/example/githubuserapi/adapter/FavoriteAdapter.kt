package com.example.githubuserapi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapi.R
import com.example.githubuserapi.main.DetailActivity
import com.example.githubuserapi.model.GithubUser
import kotlinx.android.synthetic.main.list_item.view.*

class FavoriteAdapter() : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    var list = ArrayList<GithubUser>()
        set(mData) {
            if (mData.size > 0) {
                this.list.clear()
            }
            this.list.addAll(mData)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        val listUser = list[position]

        Glide.with(holder.itemView.context)
            .load(listUser.avatar)
            .apply(RequestOptions().override(60,60))
            .into(holder.imgPhoto)
        holder.tvFullname.text = listUser.username
        holder.tvUsername.text = listUser.username

        val mContext = holder.itemView.context
        holder.itemView.setOnClickListener {
            val user = GithubUser(
                listUser.id,
                listUser.username,
                listUser.avatar,
                listUser.name,
                listUser.location,
                listUser.company,
                listUser.follower,
                listUser.following,
                listUser.repository,
                listUser.link
            )

            val moveToDetail = Intent(mContext, DetailActivity::class.java)
            moveToDetail.putExtra(DetailActivity.EXTRA_NAME, user.username)
            moveToDetail.putExtra(DetailActivity.EXTRA_AVATAR_URL, user.avatar)
            mContext.startActivity(moveToDetail)
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imgPhoto : ImageView = itemView.img_photo
        var tvFullname : TextView = itemView.tv_fullName
        var tvUsername : TextView = itemView.tv_username
    }
}