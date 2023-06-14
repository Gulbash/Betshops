package happening.betshops.home.data

import happening.betshops.home.data.model.ApiBetshopResults
import happening.betshops.home.data.util.Mapper
import happening.betshops.home.ui.model.BoundingBox
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.flow.flow

private const val BASE_URL = "https://interview.superology.dev/betshops?boundingBox="

object BetshopsService {

    fun getBetshops(boundingBox: BoundingBox) = flow {
        val stringBox = boundingBox.topRight.latitude.toString() + ',' +
                boundingBox.topRight.longitude + ',' +
                boundingBox.bottomLeft.latitude.toString() + ',' +
                boundingBox.bottomLeft.longitude
        val response = Client.client.get {
            url("${BASE_URL}${stringBox}")
        }
        emit((response.body() as ApiBetshopResults).betshops.map { Mapper.mapToBetshop(it) })
    }
}
