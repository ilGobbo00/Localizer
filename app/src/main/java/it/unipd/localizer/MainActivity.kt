package it.unipd.localizer

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.SharedPreferences
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var persistentState: SharedPreferences
    private lateinit var persistentStateEditor: SharedPreferences.Editor

    private var permissionObtained = false

    companion object{
        const val PERMISSIONS = "permissions"
        const val SERVICE_RUNNING = "main_service"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        persistentState = this.getPreferences(MODE_PRIVATE)
        persistentStateEditor = persistentState.edit()

        val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            permissionObtained = true
//
//            for (permission in permissions) {
//                Log.i("Localizer/MA", "Checking: ${permission.key} -> ${permission.value}")
//                permissionObtained = permissionObtained && permission.value
//            }

            permissionObtained = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(applicationContext, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

            persistentStateEditor.putBoolean(PERMISSIONS, permissionObtained)
            persistentStateEditor.apply()

            if(!permissionObtained)
                persistentStateEditor.putBoolean(SERVICE_RUNNING, false)
        }

        if(!permissionObtained) {
            Log.i("Localizer/MA", "Asking permissions")
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                locationPermissionRequest.launch(arrayOf(Manifest.permission.FOREGROUND_SERVICE))

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
        }else
            Log.i("Localizer/MA", "Permissions already got")

        super.onStart()
    }
}