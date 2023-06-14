package happening.betshops.home.ui

import happening.betshops.home.ui.model.Betshop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


data class HomeState(
    val selectedBetshop: Betshop? = null,
//    val betshops: Flow<List<Betshop>> = emptyFlow()
)
