package happening.betshops.home.ui

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import happening.betshops.home.data.BetshopsService
import happening.betshops.home.data.TimeService
import happening.betshops.home.ui.model.Betshop
import happening.betshops.home.ui.model.BoundingBox
import happening.betshops.home.ui.model.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private var boundingBox =
        BoundingBox(
            topRight = LatLng(48.16124, 11.60912),
            bottomLeft = LatLng(48.12229, 11.52741),
        )

    var betshops: Flow<List<Betshop>> = BetshopsService.getBetshops(boundingBox)
    var time: Flow<Time> = TimeService.getTime(LatLng(48.16124, 11.60912))

    fun cameraMoved(boundingBox: BoundingBox) {
        this.betshops = BetshopsService.getBetshops(boundingBox)
//        _homeState.value = _homeState.value.copy(
//            betshops = BetshopsService.getBetshops(boundingBox)
//        )
    }

    fun selectBetshop(betshop: Betshop?) {
        _homeState.value = HomeState(betshop)
    }

    @SuppressLint("SimpleDateFormat")
    fun isOpen(location: LatLng): Boolean {
//        viewModelScope.launch {
//            launch {
//                time.collect{
//
//                }
//            }
//        }
////        time = TimeService.getTime(location).collect{
////
////        }
//        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
//        val currentDate = simpleDate.format(Date(System.currentTimeMillis() / 1000 + time.))
//        Log
        return true
    }
}