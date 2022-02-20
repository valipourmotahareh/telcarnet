package com.thimat.sockettelkarnet.geofencing

import android.app.IntentService
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.maps.model.LatLng
import com.thimat.sockettelkarnet.classes.Jalali
import com.thimat.sockettelkarnet.localDb.LocalDB
import com.thimat.sockettelkarnet.MyApplication
import com.thimat.sockettelkarnet.R
import java.util.*

class GeofenceTransitionsIntentService : IntentService("GeoTrIntentService") {

    companion object {
        private const val LOG_TAG = "GeoTrIntentService"
        lateinit var localDB: LocalDB
    }

    override fun onHandleIntent(intent: Intent?) {
        localDB = LocalDB(applicationContext)
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
//      val errorMessage = GeofenceErrorMessages.getErrorString(this,
//          geofencingEvent.errorCode)
            return
        }
        handleEvent(geofencingEvent)
    }

    private fun handleEvent(event: GeofencingEvent) {
        try {
            val transition = event.geofenceTransition
            if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                val reminder = getFirstReminder(event.triggeringGeofences)
                val message = reminder?.message
                val latLng = reminder?.latLng
                if (message != null && latLng != null) {
                    val strs =message.split(";").toTypedArray()
                    val currentTime = Date()
                    val datetoday = Jalali.getFarsiDate(currentTime)+" "+Jalali.getEnglishFormattedtime(currentTime)
                    localDB.SaveWarningModel(strs[1],strs[2].toInt(),strs[3],
                        datetoday.toString(),
                        applicationContext.getString(R.string.warning_exit))
                    sendNotification(this, message.split(";")[0].toString()+"از محدوده خارج شدید!", latLng)
                }
            }else if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                val reminder = getFirstReminder(event.triggeringGeofences)
                val message = reminder?.message
                val latLng = reminder?.latLng
                if (message != null && latLng != null) {
                    val strs =message.split(";").toTypedArray()
                    val currentTime = Date()
                    val datetoday =Jalali.getFarsiDate(currentTime)+" "+Jalali.getEnglishFormattedtime(currentTime)
                    localDB.SaveWarningModel(strs[1],strs[2].toInt(),strs[3],
                        datetoday.toString(),
                        applicationContext.getString(R.string.warning_Enter))
                    sendNotification(this, message.split(";")[0].toString()+"!به محدوده وارد شدید", latLng)
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            val latLng = LatLng(36.314196, 59.541207)
            sendNotification(this,e.message.toString(),latLng)
        }

    }

    private fun getFirstReminder(triggeringGeofences: List<Geofence>): Reminder? {
        val firstGeofence = triggeringGeofences[0]
        return (application as MyApplication).repository.get(firstGeofence.requestId)
    }
}