package com.example.pharmacyshortage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacyshortage.databinding.ListItemShortageBinding
import com.example.pharmacyshortage.model.Shortage
import com.example.pharmacyshortage.model.getFormattedPrice

/**
 * ListAdapter for the list of [Shortage]s retrieved from the database
 */
class ShortageListAdapter(
    private val clickListener: (Shortage) -> Unit,
) : ListAdapter<Shortage, ShortageListAdapter.ShortageViewHolder>(DiffCallback) {

    class ShortageViewHolder(
        private val binding: ListItemShortageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shortage: Shortage) {
            binding.shortageName.text = shortage.name
            binding.shortageType.text = shortage.type.joinToString(",")
            if (shortage.previousPrice > 0) {
                binding.shortagePreviousPrice.text = shortage.getFormattedPrice()
            } else {
                binding.shortagePreviousPrice.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ShortageViewHolder(
            ListItemShortageBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShortageViewHolder, position: Int) {
        val shortage = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(shortage)
        }

        holder.bind(shortage)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Shortage>() {
        override fun areItemsTheSame(oldItem: Shortage, newItem: Shortage): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Shortage, newItem: Shortage): Boolean {
            return oldItem.id == newItem.id
        }
    }
}