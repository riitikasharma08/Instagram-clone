package com.neatroots.instagramclone2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.neatroots.instagramclone2.Models.User
import com.neatroots.instagramclone2.R
import com.neatroots.instagramclone2.databinding.SearchRvBinding
import com.neatroots.instagramclone2.utils.FOLLOW

class SearchAdapter(var context: Context, var userList: ArrayList<User>):RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: SearchRvBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         var binding = SearchRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isfolllow =false
        Glide.with(context).load(userList.get(position).image).placeholder(R.drawable.user).into(holder.binding.profileImage)
        holder.binding.name.text=userList.get(position).name
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).whereEqualTo("email",userList.get(position).email)
            .get().addOnSuccessListener {
                if (it.documents.size==0){
                   isfolllow=false
                }else{
                    holder.binding.follow.text="Unfollow"
                    isfolllow=true
                }
            }
        holder.binding.follow.setOnClickListener {
            if (isfolllow) {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                    .whereEqualTo("email", userList.get(position).email)
                    .get().addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                            .document(it.documents.get(0).id).delete()
                        holder.binding.follow.text = "follow"
                        isfolllow=false
                    }

            }else{
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).document()
                    .set(userList.get(position))
                holder.binding.follow.text="Unfollow"
                isfolllow=true
            }
            }
        }
    }


