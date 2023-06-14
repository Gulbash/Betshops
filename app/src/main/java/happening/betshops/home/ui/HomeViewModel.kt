package happening.betshops.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import happening.betshops.home.data.BetshopsSource
import happening.betshops.home.data.BetshopsSourceImpl
import happening.betshops.home.data.TimeSource
import happening.betshops.home.data.TimeSourceImpl
import happening.betshops.home.ui.model.Betshop
import happening.betshops.home.ui.model.BoundingBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

private const val OPEN_HOUR = 8
private const val CLOSE_HOUR = 16

class HomeViewModel : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val betshopsSource: BetshopsSource = BetshopsSourceImpl
    private val timeSource: TimeSource = TimeSourceImpl

    fun cameraMoved(boundingBox: BoundingBox) {
        viewModelScope.launch(Dispatchers.IO) {
            _homeState.value =
                _homeState.value.copy(betshops = betshopsSource.getBetshops(boundingBox))
        }
    }

    fun selectBetshop(betshop: Betshop?) {
        if (betshop == null) {
            _homeState.value = _homeState.value.copy(selectedBetshop = null, isSelectedOpen = false)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val time = timeSource.getTime(betshop.location)
                val timestamp = System.currentTimeMillis() / 1000 + time.dstOffset + time.rawOffset
                val result = isTimeInRange(timestamp)
                _homeState.value =
                    _homeState.value.copy(selectedBetshop = betshop, isSelectedOpen = result)
            }
        }
    }

    private fun isTimeInRange(timestampInSeconds: Long): Boolean {
        val instant = Instant.ofEpochSecond(timestampInSeconds)
        val zoneId = ZoneId.systemDefault()

        val dateTime = instant.atZone(zoneId)
        val time = dateTime.toLocalTime()

        val startTime = LocalTime.of(OPEN_HOUR, 0)
        val endTime = LocalTime.of(CLOSE_HOUR, 0)

        return time in startTime..endTime
    }
}