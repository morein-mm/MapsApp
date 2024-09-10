package ru.netology.nmedia.ui.repository

import androidx.lifecycle.map
import ru.netology.nmedia.ui.dao.PlacemarkDao
import ru.netology.nmedia.ui.dto.Placemark
import ru.netology.nmedia.ui.entity.PlacemarkEntity

class PlacemarkRepository(
    private val dao: PlacemarkDao,
) {
    fun getAll() = dao.getAll().map { list ->
        list.map {
            it.toDto()
        }
    }

    fun save(placemark: Placemark) {
        dao.save(PlacemarkEntity.fromDto(placemark))
    }

    fun delete(placemark: Placemark) {
        dao.delete(placemark.id)

    }

}