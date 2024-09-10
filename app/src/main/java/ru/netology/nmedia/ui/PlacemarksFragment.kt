package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentMapsBinding
import ru.netology.nmedia.databinding.FragmentPlacemarksBinding
import ru.netology.nmedia.ui.MapsFragment.Companion.latitudeArg
import ru.netology.nmedia.ui.MapsFragment.Companion.longitudeArg
import ru.netology.nmedia.ui.adapter.OnInteractionListener
import ru.netology.nmedia.ui.adapter.PlacemarksAdapter
import ru.netology.nmedia.ui.dto.Placemark
import ru.netology.nmedia.ui.viewmodel.PlacemarkViewModel

class PlacemarksFragment : Fragment() {

    private val viewModel: PlacemarkViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPlacemarksBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PlacemarksAdapter(
            object : OnInteractionListener{
                override fun onPlacemarkClick(placemark: Placemark) {
                    findNavController().navigate(
                        R.id.action_placemarksFragment_to_mapsFragment,
                        Bundle().apply {
                            latitudeArg = placemark.latitude
                            longitudeArg = placemark.longitude
                        })

                }
            }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { placemarks ->
            println("OBSERVE IN LIST")
            println(placemarks.count())
            adapter.submitList(placemarks)
        }

        return binding.root

    }
}