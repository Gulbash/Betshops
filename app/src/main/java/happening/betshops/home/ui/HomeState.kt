package happening.betshops.home.ui

import happening.betshops.home.ui.model.Betshop

data class HomeState(
    val betshops: List<Betshop> = emptyList(),
    val selectedBetshop: Betshop? = null,
    val isSelectedOpen: Boolean = false
)
