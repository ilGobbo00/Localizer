package com.unipd.localizer

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class LocationRepository(private val locationDao: LocationDao) {
    val allLocations: LiveData<List<LocationEntity>> = locationDao.getAllLocations()
//    val allLocationsStringTimestamp: List<String> = locationDao.getAllLocationsStringTimestamp()

    @WorkerThread
    suspend fun insert(location: LocationEntity){
        locationDao.insertLocation(location)
    }

//    @WorkerThread
//    suspend fun getLocation(timestamp: Long): LiveData<LocationEntity> {
//        return locationDao.getLocation(timestamp)
//    }
    @WorkerThread
    suspend fun getLocation(timestamp: Long): LocationEntity {
        return locationDao.getLocation(timestamp)
    }

    @WorkerThread
    suspend fun deleteOld(){
        locationDao.deleteOld()
    }

}