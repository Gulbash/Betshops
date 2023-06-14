package happening.betshops.home.data

import happening.betshops.home.ui.model.Betshop
import happening.betshops.home.ui.model.BoundingBox

interface BetshopsSource {
    suspend fun getBetshops(boundingBox: BoundingBox): List<Betshop>
}