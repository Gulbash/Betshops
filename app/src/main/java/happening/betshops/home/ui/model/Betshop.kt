package happening.betshops.home.ui.model

import com.google.android.gms.maps.model.LatLng

data class Betshop(
    val name: String,
    val location: LatLng,
    val county: String,
    val city: String,
    val address: String,
)
