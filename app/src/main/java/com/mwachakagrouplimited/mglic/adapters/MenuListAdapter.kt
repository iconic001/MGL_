package com.mwachakagrouplimited.mglic.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.mwachakagrouplimited.mglic.R
import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.widget.TextView
import android.widget.LinearLayout
import com.mwachakagrouplimited.mglic.model.Menu

class MenuListAdapter(
    private var menuList: List<Menu>,
    private val clickListener: MenuListClickListener
) : RecyclerView.Adapter<MenuListAdapter.MyViewHolder>() {
    fun updateData(menuList: List<Menu>) {
        this.menuList = menuList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.menu_recycler_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.menuName.text = menuList[position].name
        holder.menuPrice.text = "Price: $" + menuList[position].price
        holder.addToCartButton.setOnClickListener {
            val menu = menuList[position]
            menu.totalInCart = 1
            clickListener.onAddToCartClick(menu)
            holder.addMoreLayout.visibility = View.VISIBLE
            holder.addToCartButton.visibility = View.GONE
            holder.tvCount.text = menu.totalInCart.toString() + ""
        }
        holder.imageMinus.setOnClickListener {
            val menu = menuList[position]
            var total = menu.totalInCart
            total--
            if (total > 0) {
                menu.totalInCart = total
                clickListener.onUpdateCartClick(menu)
                holder.tvCount.text = total.toString() + ""
            } else {
                holder.addMoreLayout.visibility = View.GONE
                holder.addToCartButton.visibility = View.VISIBLE
                menu.totalInCart = total
                clickListener.onRemoveFromCartClick(menu)
            }
        }
        holder.imageAddOne.setOnClickListener {
            val menu = menuList[position]
            var total = menu.totalInCart
            total++
            if (total <= 12) {
                menu.totalInCart = total
                clickListener.onUpdateCartClick(menu)
                holder.tvCount.text = total.toString() + ""
            }
        }
        Glide.with(holder.thumbImage)
            .load(menuList[position].url)
            .into(holder.thumbImage)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var menuName: TextView
        var menuPrice: TextView
        var addToCartButton: TextView
        var thumbImage: ImageView
        var imageMinus: ImageView
        var imageAddOne: ImageView
        var tvCount: TextView
        var addMoreLayout: LinearLayout

        init {
            menuName = view.findViewById(R.id.menuName)
            menuPrice = view.findViewById(R.id.menuPrice)
            addToCartButton = view.findViewById(R.id.addToCartButton)
            thumbImage = view.findViewById(R.id.thumbImage)
            imageMinus = view.findViewById(R.id.imageMinus)
            imageAddOne = view.findViewById(R.id.imageAddOne)
            tvCount = view.findViewById(R.id.tvCount)
            addMoreLayout = view.findViewById(R.id.addMoreLayout)
        }
    }

    interface MenuListClickListener {
        fun onAddToCartClick(menu: Menu?)
        fun onUpdateCartClick(menu: Menu?)
        fun onRemoveFromCartClick(menu: Menu?)
    }
}