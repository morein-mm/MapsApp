package ru.netology.nmedia.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.ui.db.AppDb
import ru.netology.nmedia.ui.dto.Placemark
import ru.netology.nmedia.ui.repository.PlacemarkRepository

private val empty = Placemark(
    id = 0,
    description = "",
    latitude = 0.0,
    longitude = 0.0,
)

class PlacemarkViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PlacemarkRepository = PlacemarkRepository(
        AppDb.getInstance(context = application).placemarkDao()
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(placemark: Placemark) {
        edited.value = placemark
        println("EDITED")
    }

    fun changeContent(description: String) {
        val text = description.trim()
        if (edited.value?.description == text) {
            return
        }
        edited.value = edited.value?.copy(description = text)
    }

    fun changePoint(latitude: Double, longitude: Double) {
        if (edited.value?.latitude == latitude && edited.value?.longitude == longitude) {
            return
        }
        edited.value = edited.value?.copy(latitude = latitude, longitude = longitude)
    }

    fun delete(placemark: Placemark) {
        repository.delete(placemark)
    }

}