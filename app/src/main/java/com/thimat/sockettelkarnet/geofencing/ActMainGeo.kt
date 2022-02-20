package com.thimat.sockettelkarnet.geofencing

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.thimat.sockettelkarnet.localDb.LocalDB
import com.thimat.sockettelkarnet.R
import kotlinx.android.synthetic.main.act_maingeo.*

class ActMainGeo: BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        private const val MY_LOCATION_REQUEST_CODE = 329
        private const val NEW_REMINDER_REQUEST_CODE = 330
        private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"
        lateinit var localDB:LocalDB
        internal var id_car=0
        lateinit var carposition:Marker
        fun newIntent(context: Context, latLng: LatLng): Intent {
            val intent = Intent(context, ActMainGeo::class.java)
            intent.putExtra(EXTRA_LAT_LNG, latLng)
            return intent
        }
    }

    private var map: GoogleMap? = null

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_maingeo)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        localDB = LocalDB(applicationContext)
        newReminder.visibility = View.GONE
        currentLocation.visibility = View.GONE
        newReminder.setOnClickListener {
            map?.run {

                val intent = NewReminderActivity.newIntent(
                        this@ActMainGeo,
                        cameraPosition.target,
                        cameraPosition.zoom,
                        id_car)
                startActivityForResult(intent, NEW_REMINDER_REQUEST_CODE)
            }
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_LOCATION_REQUEST_CODE)
        }
        //-------------------------------------------------------------
        val handler = Handler()
        val delay = 20000 //milliseconds

        handler.postDelayed(object : Runnable {
            override fun run() {
                centerCamera()
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
        //-------------------------------------------------------------
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NEW_REMINDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            showReminders()

            val reminder = getRepository().getLast()
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(reminder?.latLng, 15f))

            Snackbar.make(main, R.string.reminder_added_success, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            onMapAndPermissionReady()
        }
    }

    private fun onMapAndPermissionReady() {
        if (map != null
                && ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map?.isMyLocationEnabled = true
            newReminder.visibility = View.VISIBLE
            currentLocation.visibility = View.VISIBLE

            currentLocation.setOnClickListener {
                val bestProvider = locationManager.getBestProvider(Criteria(), false)
                val location = locationManager.getLastKnownLocation(bestProvider)
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }

            showReminders()

            centerCamera()
        }
    }

    private fun centerCamera() {
        id_car=intent.extras.get("id") as Int
        val latLng = LatLng(localDB.GetCarsID(id_car).lat, localDB.GetCarsID(id_car).lng)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        val icon = BitmapDescriptorFactory.fromResource(R.drawable.carlocation)
        try {
            if (carposition!=null){
                carposition.remove()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        carposition = map?.addMarker(MarkerOptions()
                .position(latLng)
                .title(localDB.GetCarsID(id_car).car_name)
                .icon(icon)
                .snippet(localDB.GetCarsID(id_car).id.toString()))!!
        carposition.showInfoWindow()
//        if (intent.extras != null && intent.extras.containsKey(EXTRA_LAT_LNG)) {
//            val latLng = intent.extras.get(EXTRA_LAT_LNG) as LatLng
//            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//        }
    }

    private fun showReminders() {
        map?.run {
            clear()
            val geofencing=localDB.GetGeofencing()
            for (i in 0..geofencing.size-1){
                var latLng= LatLng(geofencing.get(i)?.lat!!, geofencing.get(i)?.lon!!)
                var reminder = Reminder(id = geofencing.get(i)?.id!!
                        ,latLng = latLng, radius = geofencing.get(i)?.radius,
                        message = geofencing.get(i)?.namegeo
                        ,actdic=geofencing.get(i)?.actdeac,flag = false)
                showReminderInMap(this@ActMainGeo, this,reminder)
            }
//            for (reminder in getRepository().getAll()) {
//                    showReminderInMap(this@Act_MainGeo, this, reminder)
//
//            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.run {
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isMapToolbarEnabled = false
            setOnMarkerClickListener(this@ActMainGeo)
        }

        onMapAndPermissionReady()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        try {
            val reminder = getRepository().get(marker.tag as String)

            if (reminder != null) {
                showReminderRemoveAlert(reminder)
            }
        }catch (ex:Exception){

        }


        return true
    }

    private fun showReminderRemoveAlert(reminder: Reminder) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.run {
            setMessage(getString(R.string.reminder_removal_alert))
            setButton(AlertDialog.BUTTON_POSITIVE,
                    getString(R.string.reminder_removal_alert_positive)) { dialog, _ ->
                removeReminder(reminder)
                dialog.dismiss()
            }
            setButton(AlertDialog.BUTTON_NEGATIVE,
                    getString(R.string.reminder_removal_alert_negative)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun removeReminder(reminder: Reminder) {
        getRepository().remove(
                reminder,
                success = {
                    showReminders()
                    Snackbar.make(main, R.string.reminder_removed_success, Snackbar.LENGTH_LONG).show()
                    localDB.DeleteGeo(reminder.id)
                },
                failure = {
                    Snackbar.make(main, it, Snackbar.LENGTH_LONG).show()
                })
    }
}
