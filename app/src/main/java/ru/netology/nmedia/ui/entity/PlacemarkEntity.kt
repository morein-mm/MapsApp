package ru.netology.nmedia.ui.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.ui.dto.Placemark

@Entity
data class PlacemarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val description: String,
    var latitude: Double,
    var longitude: Double,
) {
    fun toDto() = Placemark(id, description, latitude, longitude)

    companion object {
        fun fromDto(dto: Placemark) =
            PlacemarkEntity(dto.id, dto.description, dto.latitude, dto.longitude)

    }
}

fun List<PlacemarkEntity>.toDto(): List<Placemark> = map(PlacemarkEntity::toDto)
//fun List<Placemark>.toEntity(): List<PlacemarkEntity> = map {PlacemarkEntity().fromDto(it)}
