package ru.netology.nmedia.ui.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.ui.entity.PlacemarkEntity

@Dao
interface PlacemarkDao {
    @Query("SELECT * FROM PlacemarkEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PlacemarkEntity>>

    @Insert
    fun insert(placemark: PlacemarkEntity)

    @Query("UPDATE PlacemarkEntity SET description = :description, latitude = :latitude, longitude = :longitude WHERE id = :id")
    fun updateById(id: Long, description: String, latitude: Double, longitude: Double)

    fun save(placemark: PlacemarkEntity) =
        if (placemark.id == 0L) insert(placemark) else updateById(placemark.id, placemark.description, placemark.latitude, placemark.longitude)

    @Query("DELETE FROM PlacemarkEntity WHERE id = :id")
    fun delete(id: Long)

}
