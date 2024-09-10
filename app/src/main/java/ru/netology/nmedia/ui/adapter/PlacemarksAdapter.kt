package ru.netology.nmedia.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PlacemarkCardBinding
import ru.netology.nmedia.ui.dto.Placemark
import androidx.navigation.fragment.findNavController

interface OnInteractionListener {
    fun onPlacemarkClick(placemark: Placemark) {}
}

class PlacemarksAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Placemark, PlacemarkViewHolder>(PlacemarkDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacemarkViewHolder {
        val binding =
            PlacemarkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlacemarkViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: PlacemarkViewHolder, position: Int) {
        val placemark = getItem(position)
        holder.bind(placemark)
    }
}

class PlacemarkViewHolder(
    private val onInteractionListener: OnInteractionListener,
    private val binding: PlacemarkCardBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(placemark: Placemark) {
        binding.apply {
            placemarkDescription.text = placemark.description
            placemarkDescription.setOnClickListener {
                onInteractionListener.onPlacemarkClick(placemark)
            }
        }
    }
}

class PlacemarkDiffCallback : DiffUtil.ItemCallback<Placemark>() {
    override fun areItemsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
        return oldItem == newItem
    }
}