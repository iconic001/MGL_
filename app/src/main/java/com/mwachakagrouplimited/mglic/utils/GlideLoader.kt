package com.mwachakagrouplimited.mglic.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mwachakagrouplimited.mglic.R
import java.io.IOException

class GlideLoader (val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.no_profile_image_background)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}