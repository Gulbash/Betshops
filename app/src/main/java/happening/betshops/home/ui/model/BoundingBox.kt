package happening.betshops.home.ui.model

import com.google.android.gms.maps.model.LatLng

data class BoundingBox(
    val topRight: LatLng,
    val bottomLeft: LatLng,
)
