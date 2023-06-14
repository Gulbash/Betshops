package happening.betshops.home.data

import happening.betshops.home.data.model.ApiBetshopResults
import happening.betshops.home.data.util.Mapper
import happening.betshops.home.ui.model.Betshop
import happening.betshops.home.ui.model.BoundingBox
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

private const val BASE_URL = "https://interview.superology.dev/betshops?boundingBox="

object BetshopsSourceImpl: BetshopsSource {

    override suspend fun getBetshops(boundingBox: BoundingBox): List<Betshop> {
        val stringBox = boundingBox.topRight.latitude.toString() + ',' +
                boundingBox.topRight.longitude + ',' +
                boundingBox.bottomLeft.latitude.toString() + ',' +
                boundingBox.bottomLeft.longitude
        val response = Client.client.get {
            url("${BASE_URL}${stringBox}")
        }
        return (response.body() as ApiBetshopResults).betshops.map { Mapper.mapToBetshop(it) }
    }
}
