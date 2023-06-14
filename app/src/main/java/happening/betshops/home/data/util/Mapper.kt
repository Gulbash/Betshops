package happening.betshops.home.data.util

import com.google.android.gms.maps.model.LatLng
import happening.betshops.home.data.model.ApiBetshop
import happening.betshops.home.data.model.ApiLocation
import happening.betshops.home.data.model.ApiTime
import happening.betshops.home.ui.model.Betshop
import happening.betshops.home.ui.model.Time

object Mapper {

    fun mapToTime(apiTime: ApiTime): Time {
        return Time(
            dstOffset = apiTime.dstOffset,
            rawOffset = apiTime.rawOffset
        )
    }

    fun mapToBetshop(apiBetshop: ApiBetshop): Betshop {
        return Betshop(
            name = apiBetshop.name.trim(),
            location = mapToLatLng(apiBetshop.location),
            county = apiBetshop.county,
            city = apiBetshop.city,
            address = apiBetshop.address
        )
    }

    private fun mapToLatLng(apiLocation: ApiLocation): LatLng {
        return LatLng(
            apiLocation.lat,
            apiLocation.lng
        )
    }
}