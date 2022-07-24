package com.lgdevs.recyclerviewwithstatus

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lgdevs.recyclerviewwithstatus.databinding.ItemAdapterBinding
import com.lgdevs.recyclerviewwithstatus.model.Item

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    var onCheckListener: ((Item, Boolean) -> Unit)? = null
    var onItemClickListener: ((Item) -> Unit)? = null
    var onItemLongClickListener: ((Item) -> Unit)? = null

    var items: MutableList<Item> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isSelectedEnabled: Boolean = false
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            ItemAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(
            itemView,
            onCheckListener,
            onItemClickListener,
            onItemLongClickListener
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, isSelectedEnabled)
    }

    override fun getItemCount(): Int = items.size

    fun setItemSelected(item: Item, isSelected: Boolean) {
        val index = items.indexOfLast { it.id == item.id }
        items[index].isSelected = isSelected

        if (isSelected) {
            items.forEachIndexed { i, data ->
                if (i == index + 1) {
                    data.isEnabled = true
                    notifyItemChanged(i)
                }
            }
        } else {
            items.forEachIndexed { i, data ->
                if (i > index && i > 0) {
                    data.isEnabled = false
                    data.isSelected = false
                    notifyItemChanged(i)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onSelectedAll(isSelected: Boolean) {
        items.forEachIndexed { index, item ->
            item.isSelected = isSelected
            if (index != 0) {
                item.isEnabled = isSelected
            }
        }
        isSelectedEnabled = true
    }

    fun getAllSelectedItems(): List<Item> = items.filter { it.isSelected }


    class ItemViewHolder(
        private val itemBinding: ItemAdapterBinding,
        private val onCheckListener: ((Item, Boolean) -> Unit)? = null,
        private val onItemClickListener: ((Item) -> Unit)? = null,
        private val onItemLongClickListener: ((Item) -> Unit)? = null
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: Item, isSelectedEnabled: Boolean) {
            itemBinding.apply {
                tvTitle.text = item.name
                itemView.isEnabled = item.isEnabled

                selectBox.apply {
                    setOnCheckedChangeListener(null)
                    isChecked = item.isSelected && item.isEnabled
                    onCheckListener?.let {
                        setOnCheckedChangeListener { _, b ->
                            it.invoke(
                                item,
                                b
                            )
                        }
                    }
                    visibility = if (isSelectedEnabled) View.VISIBLE else View.GONE
                    isClickable = item.isEnabled
                }


                contentView.setOnLongClickListener {
                    onItemLongClickListener?.invoke(item)
                    true
                }
                contentView.setOnClickListener { onItemClickListener?.invoke(item) }
                itemView.setOnClickListener { selectBox.isChecked = selectBox.isChecked.not() }
            }
        }
    }
}