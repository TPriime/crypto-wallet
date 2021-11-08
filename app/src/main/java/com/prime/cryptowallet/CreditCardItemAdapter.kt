package com.prime.cryptowallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// TODO implement BindableAdapter
class CreditCardItemAdapter(private val featuredItems: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = featuredItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FeaturedItemHolder(parent, viewType!=99)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FeaturedItemHolder).bind(featuredItems[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==1) 99 else super.getItemViewType(position)
    }

    /*fun setItem(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }*/

    /**
     * Fragment to hold a featured item
     */
    class FeaturedItemHolder(v: View): RecyclerView.ViewHolder(v) {
        constructor(parent: ViewGroup, isVisa: Boolean = true) :
                this(LayoutInflater.from(parent.context)
                    .inflate(if(isVisa) R.layout.item_visa_card else R.layout.item_master_card, parent, false))

        fun bind(urls: String) {
            // TODO bind image and action urls to view
        }
    }
}