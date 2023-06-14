package happening.betshops.home.data

import com.google.android.gms.maps.model.LatLng
import happening.betshops.home.ui.model.Time

interface TimeSource {
    suspend fun getTime(location: LatLng): Time
}