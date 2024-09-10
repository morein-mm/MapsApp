package ru.netology.nmedia.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectDragListener
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentMapsBinding
import ru.netology.nmedia.ui.dto.Placemark
import ru.netology.nmedia.ui.util.DoubleArg
import ru.netology.nmedia.ui.viewmodel.PlacemarkViewModel

class MapsFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var map: Map

    private val viewModel: PlacemarkViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private var placemarkTapListeners: MutableList<MapObjectTapListener> = mutableListOf()
    private var placemarkDragListeners: MutableList<MapObjectDragListener> = mutableListOf()
    private var placemarks: MutableList<PlacemarkMapObject> = mutableListOf()

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            showPlacemarkDialog(null, point)
        }

        override fun onMapLongTap(map: Map, point: Point) = Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMapsBinding.inflate(
            inflater,
            container,
            false
        )

        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())

        mapView = binding.root.findViewById(R.id.mapview)
        map = mapView.mapWindow.map
        map.addInputListener(inputListener)

        if (arguments?.latitudeArg != null && arguments?.longitudeArg != null) {
            map.move(
                CameraPosition(
                    Point(
                        requireArguments().latitudeArg,
                        requireArguments().longitudeArg
                    ), 17.0f, 150.0f, 30.0f
                )
            )
        } else {
            map.move(startPosition)
        }
        viewModel.data.observe(viewLifecycleOwner) { list ->
            removePlacemarksFromMap()
            list.forEach() {
                showPlacemarkOnMap(it)
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun removePlacemarksFromMap() {
        placemarks.forEach {
            map.mapObjects.remove(it)
        }
        placemarks = mutableListOf()
    }

    private fun showPlacemarkOnMap(placemark: Placemark) {
        val imageProvider =
            ImageProvider.fromResource(requireContext(), R.drawable.ic_dollar_pin)
        val textStyle = TextStyle().setPlacement(TextStyle.Placement.BOTTOM)
        placemarkTapListeners.add(object : MapObjectTapListener {
            override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
                showActionSelectDialog(placemark)
                return true
            }
        })
        placemarkDragListeners.add(object : MapObjectDragListener {
            override fun onMapObjectDragStart(p0: MapObject) {
            }

            override fun onMapObjectDrag(p0: MapObject, p1: Point) {
            }

            override fun onMapObjectDragEnd(p0: MapObject) {
                if (p0 is PlacemarkMapObject) {
                    viewModel.edit(placemark)
                    viewModel.changeContent(placemark.description)
                    viewModel.changePoint(p0.geometry.latitude, p0.geometry.longitude)
                    viewModel.save()
                }
            }

        })
        placemarks.add(map.mapObjects.addPlacemark().apply {
            geometry = Point(placemark.latitude, placemark.longitude)
            setText(placemark.description, textStyle)
            setIcon(imageProvider)
            isDraggable = true
            addTapListener(placemarkTapListeners.last())
            setDragListener(placemarkDragListeners.last())
        })

    }

    private fun showActionSelectDialog(placemark: Placemark) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_action))
            .setPositiveButton(getString(R.string.edit)) { dialog, which ->
                showPlacemarkDialog(placemark, null)
            }
            .setNeutralButton(getString(R.string.delete)) { dialog, which ->
                viewModel.delete(placemark)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            }
            .create()
            .show()
    }

    private fun showPlacemarkDialog(placemark: Placemark?, point: Point?) {
        val view = layoutInflater.inflate(R.layout.edit_text_dialog, null)
        val placemarkDescription =
            view.findViewById<EditText>(R.id.placemark_description_text)
        placemarkDescription.setText(placemark?.description)

        AlertDialog.Builder(requireContext())
            .setTitle(
                if (placemark == null) {
                    getString(R.string.create_placemark)
                } else {
                    getString(R.string.edit_placemark)
                }
            )
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { dialog, which ->
                placemark?.let { viewModel.edit(placemark) }
                viewModel.changeContent(placemarkDescription.text.toString())
                point?.let { viewModel.changePoint(point.latitude, point.longitude) }
                viewModel.save()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            }
            .create()
            .show()
    }


    companion object {
        private val startPoint = Point(59.939008, 30.314896)
        private val startPosition = CameraPosition(startPoint, 17.0f, 150.0f, 30.0f)
        var Bundle.latitudeArg: Double by DoubleArg
        var Bundle.longitudeArg: Double by DoubleArg

    }
}