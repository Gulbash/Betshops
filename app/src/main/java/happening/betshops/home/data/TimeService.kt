package happening.betshops.home.data

import com.google.android.gms.maps.model.LatLng
import happening.betshops.home.data.model.ApiTime
import happening.betshops.home.data.util.Mapper
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.flow.flow

private const val BASE_URL =
    "https://maps.googleapis.com/maps/api/timezone/json?language=en&location="
private const val API_KEY = "AIzaSyDqMHeLa2yQck912F78zl4SFbmK6kND1zI"

object TimeService {

    fun getTime(location: LatLng) = flow {
        val response = Client.client.get {
            url(
                "${BASE_URL}${location.latitude},${location.longitude}" +
                        "&timestamp=${System.currentTimeMillis() / 1000}&key=${API_KEY}"
            )
        }
        emit(Mapper.mapToTime((response.body() as ApiTime)))
    }
}