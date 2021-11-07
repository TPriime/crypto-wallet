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
        return FeaturedItemHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FeaturedItemHolder).bind(featuredItems[position])
    }

    /*fun setItem(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }*/

    /**
     * Fragment to hold a featured item
     */
    class FeaturedItemHolder(v: View): RecyclerView.ViewHolder(v) {
        constructor(parent: ViewGroup) :
                this(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_credit_card, parent, false))

        fun bind(urls: String) {
            // TODO bind image and action urls to view
        }
    }
}